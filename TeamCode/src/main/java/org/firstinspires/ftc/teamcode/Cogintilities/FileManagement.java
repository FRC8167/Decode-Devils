package org.firstinspires.ftc.teamcode.Cogintilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileManagement {

    public static void createFile(String name) {
        try {
            File myObj = new File(name); // Create File object
            if (myObj.createNewFile()) {           // Try to create the file
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Creation Failed");
        }
    }
    public static void appendToFile(String name, String data) {
        // true = append mode
        try (FileWriter myWriter = new FileWriter(name, true)) {
            myWriter.write("\n"+data);
            System.out.println("Successfully appended to the file.");
        } catch (IOException e) {
            throw new RuntimeException("Append Failed");
        }
    }

    public static void writeToFile(String name, String data) {
        // true = append mode
        try (FileWriter myWriter = new FileWriter(name, false)) {
            myWriter.write(data);
            System.out.println("Successfully appended to the file.");
        } catch (IOException e) {
            throw new RuntimeException("Write Failed");
        }
    }

    public static void readFile(String name) {
        File myObj = new File(name);

        // try-with-resources: Scanner will be closed automatically
        try (Scanner myReader = new Scanner(myObj)) {
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            throw new RuntimeException("Read Failed");
        }
    }

    public static void deleteFile(String name) {
        File myObj = new File(name);
        if (myObj.delete()) {
            System.out.println("Deleted the file: " + myObj.getName());
        } else {
            throw new RuntimeException("Delete Failed");
        }
    }
}