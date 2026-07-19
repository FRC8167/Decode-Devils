package org.firstinspires.ftc.teamcode.Testing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A Self-Hosted Web Dashboard for motor visualization.
 * Open http://localhost:8081 in any browser to see real-time graphs.
 * Works perfectly in Android Studio's restricted unit test environment.
 */
public class SimulationVisualizer {
    private final int port = 8081;
    private final CopyOnWriteArrayList<String> dataStream = new CopyOnWriteArrayList<>();
    private volatile boolean running = true;
    private final double ticksPerRevolution;

    public SimulationVisualizer(double ticksPerRevolution) {
        this.ticksPerRevolution = ticksPerRevolution;
        new Thread(new Runnable() {
            @Override
            public void run() {
                startServer();
            }
        }).start();
        System.out.println("\n>>> VISUAL DASHBOARD READY <<<");
        System.out.println("Open: http://localhost:8081");
        System.out.println("------------------------------\n");
    }

    public SimulationVisualizer() {
        this(537.7); // Default to GoBILDA 19.2:1
    }

    public void updateData(double pos, double vel, double tarPos) {
        // Log to console for backup
        System.out.printf("P:%-6.0f V:%-8.2f *:%-5.2f\r", pos, vel, tarPos);
        
        // Add to web stream (Limit size to last 500 points)
        String json = "{\"t\":" + System.currentTimeMillis() + 
                      ",\"p\":" + String.format("%.1f", pos) + 
                      ",\"v\":" + String.format("%.1f", vel) + 
                      ",\"w\":" + String.format("%.2f", tarPos) + "}";
        dataStream.add(json);
        if (dataStream.size() > 500) dataStream.remove(0);
    }

    private void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (running) {
                Socket client = null;
                try {
                    client = serverSocket.accept();
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    
                    String request = in.readLine();
                    if (request != null) {
                        if (request.contains("/data")) {
                            sendData(out);
                        } else {
                            sendHtml(out);
                        }
                    }
                    client.close();
                } catch (Exception e) { 
                    if (running && client != null) client.close();
                }
            }
        } catch (IOException e) {
            if (running) e.printStackTrace();
        }
    }

    private void sendHtml(PrintWriter out) {
        out.println("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n" +
            "<html><head><title>Motor Sim</title><script src='https://cdn.jsdelivr.net/npm/chart.js'></script></head>" +
            "<body style='background:#111;color:white;font-family:sans-serif;padding:20px'>" +
            "<h2>Motor Real-time Telemetry</h2>" +
            "<div style='display:flex; gap:20px; align-items:flex-start'>" +
            "  <div style='flex:3; height:70vh'><canvas id='chart'></canvas></div>" +
            "  <div style='flex:1; text-align:center'>" +
            "    <h3>Physical View</h3>" +
            "    <canvas id='motor' width='200' height='200'></canvas>" +
            "    <p id='posText' style='font-family:monospace; margin-top:10px'></p>" +
            "  </div>" +
            "</div>" +
            "<script>" +
            "const ctx = document.getElementById('chart').getContext('2d');" +
            "const mCtx = document.getElementById('motor').getContext('2d');" +
            "const chart = new Chart(ctx, {type:'line', data:{labels:[], datasets:[" +
            "{label:'Position', borderColor:'cyan', backgroundColor:'cyan', data:[], fill:false, pointRadius:0, borderWidth:2}," +
            "{label:'Velocity', borderColor:'orange', backgroundColor:'orange', data:[], fill:false, pointRadius:0, borderWidth:2}]" +
//            "{label:'TargetPos', borderColor:'red', backgroundColor:'red', data:[], fill:false, pointRadius:0, borderWidth:2}]" +
            "}, options:{responsive:true, maintainAspectRatio:false, " +
            "scales:{y:{type:'linear',position:'left',grid:{color:'#333'}}}," +
            "animation:false, plugins:{legend:{labels:{color:'white'}}}}}); " +
            "function drawMotor(ticks) {" +
            "  const tpr = " + ticksPerRevolution + ";" +
            "  const angle = -(ticks % tpr) / tpr * 2 * Math.PI;" +
            "  mCtx.clearRect(0,0,200,200); mCtx.strokeStyle='white'; mCtx.lineWidth=2; " +
            "  mCtx.beginPath(); mCtx.arc(100,100,80,0,2*Math.PI); mCtx.stroke(); " + // Motor body
            "  mCtx.save(); mCtx.translate(100,100); mCtx.rotate(angle); " +
            "  mCtx.beginPath(); mCtx.moveTo(0,0); mCtx.lineTo(0,-70); mCtx.strokeStyle='cyan'; mCtx.lineWidth=5; mCtx.stroke(); " + // Indicator
//            "  mCtx.beginPath(); mCtx.moveTo(0,0); mCtx.rotate(-angle + -(tarTicks % tpr) / tpr * 2 * Math.PI); mCtx.lineTo(0,-70); mCtx.strokeStyle='red'; mCtx.lineWidth=5; mCtx.stroke();" +
            "  mCtx.beginPath(); mCtx.arc(0,0,10,0,2*Math.PI); mCtx.fillStyle='white'; mCtx.fill(); " + // Shaft
            "  mCtx.restore();" +
            "}" +
            "setInterval(async () => {" +
            "  try {" +
            "    const r = await fetch('/data'); const d = await r.json();" +
            "    if (d.length > 0) {" +
            "      const latest = d[d.length - 1];" +
            "      chart.data.labels = d.map(x => ''); " +
            "      chart.data.datasets[0].data = d.map(x => x.p);" +
            "      chart.data.datasets[1].data = d.map(x => x.v);" +
//            "      chart.data.datasets[2].data = d.map(x => x.w);" +
            "      chart.update();" +
            "      drawMotor(latest.p);" +
//            "      drawMotor(latest.p, latest.w);" +
            "      document.getElementById('posText').innerText = `POS: ${latest.p} | VEL: ${latest.v}`;" +
            "    }" +
            "  } catch(e) {}" +
            "}, 50);" +
            "</script></body></html>");
    }

    private void sendData(PrintWriter out) {
        out.println("HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n[" + String.join(",", dataStream) + "]");
    }

    public void dispose() {
        running = false;
        System.out.println("\nVisualizer Stopped.");
    }
}
