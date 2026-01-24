/*   MIT License
 *   Copyright (c) [2025] [Base 10 Assets, LLC]
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:

 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.

 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */

package org.firstinspires.ftc.teamcode.Cogintilities.Prism;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;

import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.util.TypeConversion;
import static org.firstinspires.ftc.teamcode.Cogintilities.Prism.GoBildaPrismDriver.LayerHeight;

public class PrismAnimations {
    public enum AnimationType{
        NONE(0),
        SOLID(1),
        BLINK(2),
        PULSE(3),
        SINE_WAVE(4),
        DROID_SCAN(5),
        RAINBOW(6),
        SNAKES(7),
        RANDOM(8),
        SPARKLE(9),
        SINGLE_FILL(10),
        RAINBOW_SNAKES(11),
        POLICE_LIGHTS(12);

        public final int AnimationTypeIndex;
        AnimationType(int animationType){
            this.AnimationTypeIndex = animationType;
        }
    }

    public static abstract class AnimationBase {
        protected final AnimationType animationType;
        protected int brightness = 100;
        protected int startIndex = 0;
        protected int stopIndex  = 255;
        protected LayerHeight layerHeight;

        //region Constructors
        protected AnimationBase(AnimationType type){
            this.animationType = type;
        }

        /**
         * @param brightness from 0 to 100.
         */
        protected AnimationBase(AnimationType type, int brightness) { 
            this(type);
            this.brightness = Math.min(brightness, 100);
        }

        /**
         * Set both the first, and last LED in a string that you'd like this animation to show on.
         * @param startIndex from 0 to 255.
         * @param stopIndex from 0 to 255.
         */
        protected AnimationBase(AnimationType type, int startIndex, int stopIndex) {
            this(type);
            this.startIndex = Math.min(startIndex, 255);
            this.stopIndex  = Math.min(stopIndex, 255);
        }

        /**
         * @param brightness from 0 to 100.
         * @param startIndex from 0 to 255.
         * @param stopIndex from 0 to 255.
         */
        protected AnimationBase(AnimationType type, int brightness, int startIndex, int stopIndex){
            this(type, brightness);
            this.startIndex = Math.min(startIndex, 255);
            this.stopIndex  = Math.min(stopIndex, 255);
        }

        /**
         * @param brightness from 0 to 100.
         * @param startIndex from 0 to 255.
         * @param stopIndex from 0 to 255.
         * @param layerHeight the height that this animation should sit at from 0 to 9.
         */
        protected AnimationBase(AnimationType type, int brightness, int startIndex, int stopIndex, LayerHeight layerHeight){
            this(type, brightness, startIndex, stopIndex);
            this.layerHeight = layerHeight;
        }
        //endregion
        
        //region Setters / Getters
        public int getBrightness() { return brightness; }
        public int getStartIndex() { return startIndex; }
        public int getStopIndex()  { return stopIndex;  }

        /**
         * Brightness of selected animation.
         * @param brightness From 0 to 100.
         */
        public void setBrightness(int brightness) { this.brightness = Math.min(brightness, 100); }

        /**
         * The first LED in the string that you'd like this animation to display on.
         * @param startIndex from 0 to 255.
         */
        public void setStartIndex(int startIndex) { this.startIndex = Math.min(startIndex, 255); }

        /**
         * The last LED in the string that you'd like this animation to display on.
         * @param stopIndex from 0 to 255.
         */
        public void setStopIndex(int stopIndex)   { this.stopIndex  = Math.min(stopIndex, 255);   }

        /**
         * Set both the first, and last LED in a string that you'd like this animation to show on.
         * @param startIndex from 0 to 255.
         * @param stopIndex from 0 to 255.
         */
        public void setIndexes(int startIndex, int stopIndex){
            this.startIndex = (byte)startIndex;
            this.stopIndex = (byte)stopIndex;
        }
        //endregion

        protected abstract void updateAnimationSpecificValuesOverI2C(I2cDeviceSynchSimple deviceClient);

        protected void updateAnimationOverI2C(I2cDeviceSynchSimple deviceClient, boolean isInsertingAnimation)
        {
            if(isInsertingAnimation)
                deviceClient.write(layerHeight.register.address, GetByteArray(0, animationType.AnimationTypeIndex));
            deviceClient.write(layerHeight.register.address, GetByteArray(1, brightness));
            deviceClient.write(layerHeight.register.address, GetByteArray(2, startIndex));
            deviceClient.write(layerHeight.register.address, GetByteArray(3, stopIndex));
            updateAnimationSpecificValuesOverI2C(deviceClient);
        }

         protected byte[] floatToByteArray (float value, ByteOrder byteOrder)
         {
             return ByteBuffer.allocate(4).order(byteOrder).putFloat(value).array();
         }

         private byte unsignedIntToByte(int data)
         {
             int boundedData = Math.min(Math.max(data, 0), 255);
             ByteBuffer buffer = ByteBuffer.allocate(4).putInt(boundedData);

             return buffer.array()[3];
         }

        protected byte[] GetByteArray(int subRegister, PrismColor color)
        {
            return new byte[] {
                (byte)Math.max(0, Math.min(subRegister, 12)),
                (byte)color.red,
                (byte)color.green,
                (byte)color.blue
            };
        }

        protected byte[] GetByteArray(int subRegister, int data)
        {
            byte[] packet = TypeConversion.intToByteArray(data, ByteOrder.LITTLE_ENDIAN);
            return new byte[] {
                (byte)Math.max(0, Math.min(subRegister, 12)),
                packet[0],
                packet[1],
                packet[2],
                packet[3]
            };
        }

        protected byte[] GetByteArray(int subRegister, float data)
        {
            byte[] packet = floatToByteArray(data, ByteOrder.LITTLE_ENDIAN);
            return new byte[] {
                (byte)Math.max(0, Math.min(subRegister, 12)),
                packet[0],
                packet[1],
                packet[2],
                packet[3]
            };
        }

        protected byte[] GetByteArray(int subRegister, byte data)
        {
            return new byte[]{
                (byte)subRegister,
                data
            };
        }

        protected byte[] GetByteArray(int subRegister, Direction direction)
        {
            boolean bool = direction == Direction.Forward;
            return new byte[]{
                (byte)Math.max(0, Math.min(subRegister, 12)),
                (byte) (bool ? 1 : 0)
            };
        }

        protected byte[] GetByteArray(int subRegister, PrismColor... colors)
        {
            byte[] array = new byte[1+(colors.length*3)];

            array[0] = (byte)Math.max(0, Math.min(subRegister, 12));

            for(int i = 0; i < colors.length; i++){
                array[1+(i*3)]   = unsignedIntToByte(colors[i].red);
                array[1+(i*3+1)] = unsignedIntToByte(colors[i].green);
                array[1+(i*3+2)] = unsignedIntToByte(colors[i].blue);
            }

            return array;
        }
    }

    public static class Blink extends AnimationBase{
        private PrismColor primaryColor     = PrismColor.BLUE;
        private PrismColor secondaryColor   = PrismColor.RED;
        private int period             = 2000;
        private int primaryColorPeriod = 1000;

        public Blink(){ super(AnimationType.BLINK); }
        public Blink(PrismColor primaryColor) {
            this();
            this.primaryColor = primaryColor;
        }
        public Blink(PrismColor primaryColor, PrismColor secondaryColor) {
            this(primaryColor);
            this.secondaryColor = secondaryColor;
        }
        public Blink(PrismColor primaryColor, PrismColor secondaryColor, int period) {
            this(primaryColor, secondaryColor);
            this.period = period;
        }
        public Blink(PrismColor primaryColor, PrismColor secondaryColor, int period, int primaryColorPeriod) {
            this(primaryColor, secondaryColor, period);
            this.primaryColorPeriod = primaryColorPeriod;
        }

        /**
         * set the length of one loop of an animation in milliseconds.
         * @param period from 0 - 4,294,697,295. Larger is longer.
         */
        public void setPeriod(int period) { this.period = period; }

        /**
         * set the length of one loop of an animation in specified unit.
         * @param duration duration of loop.
         * @param timeUnit unit to use.
         */
        public void setPeriod(int duration, TimeUnit timeUnit){
            this.period = Math.toIntExact(timeUnit.toMillis(duration));
        }
        public void setPrimaryColor(PrismColor color)   { primaryColor = color;   }
        public void setSecondaryColor(PrismColor color) { secondaryColor = color; }
        public void setPrimaryColor(int red, int green, int blue)   { primaryColor = new PrismColor(red, green, blue);   }
        public void setSecondaryColor(int red, int green, int blue) { secondaryColor = new PrismColor(red, green, blue); }
        /**
         * set the duration of the primary color in a blink.
         * @param primaryColorPeriod from 0 - 4,294,697,295. Larger is longer.
         */
        public void setPrimaryColorPeriod(int primaryColorPeriod)   { this.primaryColorPeriod = primaryColorPeriod; }

        /**
         * set the duration of the primary color in a blink.
         * @param duration duration of loop.
         * @param timeUnit unit to use.
         */
        public void setPrimaryColorPeriod(int duration, TimeUnit timeUnit){
            this.primaryColorPeriod = Math.toIntExact(timeUnit.toMillis(duration));
        }

        public int getPeriod()             { return period;             }
        public PrismColor getPrimaryColor()     { return primaryColor;       }
        public PrismColor getSecondaryColor()   { return secondaryColor;     }
        public int getPrimaryColorPeriod() { return primaryColorPeriod; }

        @Override
        protected void updateAnimationSpecificValuesOverI2C(I2cDeviceSynchSimple deviceClient)
        {
            deviceClient.write(layerHeight.register.address, GetByteArray(4, primaryColor));
            deviceClient.write(layerHeight.register.address, GetByteArray(5, secondaryColor));
            deviceClient.write(layerHeight.register.address, GetByteArray(6, period));
            deviceClient.write(layerHeight.register.address, GetByteArray(7, primaryColorPeriod));
        }
    }

    public static class DroidScan extends AnimationBase{
        private PrismColor primaryColor   = PrismColor.RED;
        private PrismColor secondaryColor = PrismColor.TRANSPARENT;
        private DroidScanStyle style  = DroidScanStyle.BOTH_TAIL;
        private float speed          = .4f;
        private int eyeWidth         = 3;
        private int trailWidth       = 3;

        public enum DroidScanStyle{
            NO_TAIL(0),
            FRONT_TAIL(1),
            BACK_TAIL(2),
            BOTH_TAIL(3);
            
            /* Package Private */ final byte styleValue;
            DroidScanStyle(int value){
                styleValue = (byte)value;
            }
        }

        public DroidScan(){ super(AnimationType.DROID_SCAN); }
        public DroidScan(PrismColor primaryColor) {
            this();
            this.primaryColor = primaryColor;
        }
        public DroidScan(PrismColor primaryColor, PrismColor secondaryColor) {
            this(primaryColor);
            this.secondaryColor = secondaryColor;
        }

        public void setSecondaryColor(PrismColor color)        { this.secondaryColor = color;  }
        public void setPrimaryColor(PrismColor color)          { this.primaryColor = color;    }

        /**
         * Sets the speed of the animation
         * @param speed from 0 to 1.
         */
        public void setSpeed(float speed)                 { this.speed = speed;           }

        /**
         * Sets the width of the "eye" in the animation in pixels
         * @param eyeWidth from 0 to 255.
         */
        public void setEyeWidth(int eyeWidth)             { this.eyeWidth = eyeWidth;     }

        /**
         * Sets the width of the trail of the eye in the animation in pixels
         * @param trailWidth from 0 to 255.
         */
        public void setTrailWidth(int trailWidth)         { this.trailWidth = trailWidth; }
        public void setDroidScanStyle(DroidScanStyle style) { this.style = style;           }

        public PrismColor getSecondaryColor()        { return secondaryColor; }
        public PrismColor getPrimaryColor()          { return primaryColor;   }
        public float getSpeed()                 { return speed;          }
        public int getEyeWidth()                { return eyeWidth;       }
        public int getTrailWidth()              { return trailWidth;     }
        public DroidScanStyle getDroidScanStyle() { return style;          }

        @Override
        protected void updateAnimationSpecificValuesOverI2C(I2cDeviceSynchSimple deviceClient)
        {
            deviceClient.write(layerHeight.register.address, GetByteArray(4 , primaryColor));
            deviceClient.write(layerHeight.register.address, GetByteArray(5 , secondaryColor));
            deviceClient.write(layerHeight.register.address, GetByteArray(6 , speed));
            deviceClient.write(layerHeight.register.address, GetByteArray(7 , (byte)eyeWidth));
            deviceClient.write(layerHeight.register.address, GetByteArray(8 , (byte)trailWidth));
            deviceClient.write(layerHeight.register.address, GetByteArray(11, style.styleValue));
        }
    }

    public static class PoliceLights extends AnimationBase {
        private PrismColor red               = PrismColor.RED;
        private PrismColor white             = PrismColor.WHITE;
        private PrismColor blue              = PrismColor.BLUE;
        private int period              = 1000;
        private PoliceLightsStyle style = PoliceLightsStyle.Style1;

        public enum PoliceLightsStyle{
            Style1(0),
            Style2(1),
            Style3(2);

            public byte styleValue;
            PoliceLightsStyle(int value){
                styleValue = (byte)value;
            }
        }

        public PoliceLights(){ super(AnimationType.POLICE_LIGHTS); }

        /**
         * set the length of one loop of an animation in milliseconds.
         * @param period from 0 - 4,294,697,295. Larger is longer.
         */
        public void setPeriod(int period) { this.period = period; }

        /**
         * set the length of one loop of an animation in specified unit.
         * @param duration duration of loop.
         * @param timeUnit unit to use.
         */
        public void setPeriod(int duration, TimeUnit timeUnit){
            this.period = Math.toIntExact(timeUnit.toMillis(duration));
        }
        public void setPoliceLightsStyle(PoliceLightsStyle style){this.style = style;}
        public void setPrimaryColor(PrismColor color){this.red = color;}
        public void setSecondaryColor(PrismColor color){this.white = color;}
        public void setTertiaryColor(PrismColor color){this.blue = color;}

        public PoliceLightsStyle getPoliceLightsStyle(){return style;}
        public PrismColor getPrimaryColor(){return this.red;}
        public PrismColor getSecondaryColor(){return this.white;}
        public PrismColor getTertiaryColor(){return this.blue;}

        @Override
        protected void updateAnimationSpecificValuesOverI2C(I2cDeviceSynchSimple deviceClient)
        {
            deviceClient.write(layerHeight.register.address, GetByteArray(6 , period));
            deviceClient.write(layerHeight.register.address, GetByteArray(7 , red));
            deviceClient.write(layerHeight.register.address, GetByteArray(8 , white));
            deviceClient.write(layerHeight.register.address, GetByteArray(9 , blue));
            deviceClient.write(layerHeight.register.address, GetByteArray(11, style.styleValue));
        }
    }

    public static class Pulse extends AnimationBase {
        private PrismColor primaryColor   = PrismColor.GREEN;
        private PrismColor secondaryColor = PrismColor.RED;
        private int period           = 1000;

        public Pulse(){ super(AnimationType.PULSE); }
        public Pulse(PrismColor primaryColor) {
            this();
            this.primaryColor = primaryColor;
        }
        public Pulse(PrismColor primaryColor, PrismColor secondaryColor) {
            this(primaryColor);
            this.secondaryColor = secondaryColor;
        }
        public Pulse(PrismColor primaryColor, PrismColor secondaryColor, int period) {
            this(primaryColor, secondaryColor);
            this.period = period;
        }

        /**
         * set the period of the pulse in milliseconds.
         * @param period from 0 - 4,294,697,295. Larger is longer.
         */
        public void setPeriod(int period) { this.period = period; }

        /**
         * set the period of the pulse in specified unit.
         * @param duration duration of loop.
         * @param timeUnit unit to use.
         */
        public void setPeriod(int duration, TimeUnit timeUnit){
            this.period = Math.toIntExact(timeUnit.toMillis(duration));
        }
        public void setPrimaryColor(PrismColor color)   { primaryColor = color;   }
        public void setSecondaryColor(PrismColor color) { secondaryColor = color; }
        public void setPrimaryColor(int red, int green, int blue)   { primaryColor = new PrismColor((byte)red, (byte)green, (byte)blue);   }
        public void setSecondaryColor(int red, int green, int blue) { secondaryColor = new PrismColor((byte)red, (byte)green, (byte)blue); }

        public int getPeriod()           { return period;         }
        public PrismColor getPrimaryColor()   { return primaryColor;   }
        public PrismColor getSecondaryColor() { return secondaryColor; }

        @Override
        protected void updateAnimationSpecificValuesOverI2C(I2cDeviceSynchSimple deviceClient)
        {
            deviceClient.write(layerHeight.register.address, GetByteArray(4, primaryColor));
            deviceClient.write(layerHeight.register.address, GetByteArray(5, secondaryColor));
            deviceClient.write(layerHeight.register.address, GetByteArray(6, period));
        }
    }

    public static class Rainbow extends AnimationBase {
        private float startHue  = 0.0f;
        private float stopHue   = 360.0f;
        private float speed     = 0.50f;    // 0.00 - 1.00
        private int repeatAfter = 25;
        private Direction direction = Direction.Forward;

        public Rainbow(){ super(AnimationType.RAINBOW); }
        public Rainbow(float startHue) {
            this();
            this.startHue = startHue;
        }
        public Rainbow(float startHue, float stopHue){
            this(startHue);
            this.stopHue = stopHue;
        }
        public Rainbow(float startHue, float stopHue, float speed){
            this(startHue, stopHue);
            this.speed = speed;
        }
        public Rainbow(float startHue, float stopHue, float speed, Direction direction){
            this(startHue, stopHue, speed);
            this.direction = direction;
        }
        public Rainbow(float startHue, float stopHue, float speed, Direction direction, int repeatAfter){
            this(startHue, stopHue, speed, direction);
            this.repeatAfter = repeatAfter;
        }

        //region Getters/Setters
        public float getSpeed()    { return speed;}
        public float getStopHue()  { return stopHue;  }
        public float getStartHue() { return startHue; }
        public Direction getDirection() { return direction; }
        public int getRepeatAfter() { return repeatAfter; }

        /**
         * Sets the speed of the animation
         * @param speed from 0 to 1.
         */
        public void setSpeed(float speed)      { this.speed = speed;       }

        /**
         * Can be used to limit the colors shown in the rainbow. This is a hue so it reflects
         * the position as an angle on a color wheel. Red is 0, Green is 120, Blue is 240, etc.
         * @param startHue from 0 to 360.
         */
        public void setStartHue(float startHue){ this.startHue = startHue; }

        /**
         * Can be used to limit the colors shown in the rainbow. This is a hue so it reflects
         * the position as an angle on a color wheel. Red is 0, Green is 120, Blue is 240, etc.
         * @param stopHue from 0 to 360.
         */
        public void setStopHue(float stopHue)  { this.stopHue = stopHue;   }
        public void setDirection(Direction direction) { this.direction = direction;     }

        /**
         * The number of pixels before the rainbow repeats. Default is 20.
         * @param repeatAfter From 0 to 255.
         */
        public void setRepeatAfter(int repeatAfter)   { this.repeatAfter = repeatAfter; }
        public void setHues(float startHue, float stopHue) {
            this.startHue = startHue;
            this.stopHue = stopHue;
        }
        //endregion
        @Override
        protected void updateAnimationSpecificValuesOverI2C(I2cDeviceSynchSimple deviceClient)
        {
            deviceClient.write(layerHeight.register.address, GetByteArray(4, startHue));
            deviceClient.write(layerHeight.register.address, GetByteArray(5, stopHue));
            deviceClient.write(layerHeight.register.address, GetByteArray(6, speed));
            deviceClient.write(layerHeight.register.address, GetByteArray(7, direction));
            deviceClient.write(layerHeight.register.address, GetByteArray(9, (byte)repeatAfter));
        }
    }

    public static class RainbowSnakes extends AnimationBase {
        private int numberOfSnakes    = 3;
        private int snakeLength       = 5;
        private int spacingBetween    = 2;
        private int repeatAfter       = 15;
        private float startHue        = 0.0f;
        private float stopHue         = 360.0f;
        private PrismColor backgroundColor = PrismColor.TRANSPARENT;
        private float speed           = 0.50f;
        private Direction direction   = Direction.Backward;

        //region Constructors
        public RainbowSnakes(){ super(AnimationType.RAINBOW_SNAKES); }
        public RainbowSnakes(float startHue, float stopHue) {
            this();
            this.startHue = startHue;
            this.stopHue = stopHue;
        }
        public RainbowSnakes(float startHue, float stopHue, int numberOfSnakes){
            this(startHue, stopHue);
            this.numberOfSnakes = (byte)numberOfSnakes;
        }
        public RainbowSnakes(float startHue, float stopHue, int numberOfSnakes, int snakeLength){
            this(startHue, stopHue, numberOfSnakes);
            this.snakeLength = (byte)snakeLength;
        }
        public RainbowSnakes(float startHue, float stopHue, int numberOfSnakes, int snakeLength, int spacingBetween){
            this(startHue, stopHue, numberOfSnakes, snakeLength);
            this.spacingBetween = (byte)spacingBetween;
        }
        public RainbowSnakes(float startHue, float stopHue, int numberOfSnakes, int snakeLength, int spacingBetween, int repeatAfter){
            this(startHue, stopHue, numberOfSnakes, snakeLength, spacingBetween);
            this.repeatAfter = (byte)repeatAfter;
        }
        public RainbowSnakes(float startHue, float stopHue, int numberOfSnakes, int snakeLength, int spacingBetween, int repeatAfter, PrismColor backgroundColor){
            this(startHue, stopHue, numberOfSnakes, snakeLength, spacingBetween, repeatAfter);
            this.backgroundColor = backgroundColor;
        }
        public RainbowSnakes(float startHue, float stopHue, int numberOfSnakes, int snakeLength, int spacingBetween, int repeatAfter, PrismColor backgroundColor, float speed){
            this(startHue, stopHue, numberOfSnakes, snakeLength, spacingBetween, repeatAfter, backgroundColor);
            this.speed = speed;
        }
        public RainbowSnakes(float startHue, float stopHue, int numberOfSnakes, int snakeLength, int spacingBetween, int repeatAfter, PrismColor backgroundColor, float speed, Direction direction){
            this(startHue, stopHue, numberOfSnakes, snakeLength, spacingBetween, repeatAfter, backgroundColor, speed);
            this.direction = direction;
        }
        //endregion

        //region Getters/Setters
        public float getSpeed()     { return speed;   }
        public float getStartHue()  { return startHue;}
        public float getStopHue()   { return stopHue; }
        public int getSnakeLength() { return snakeLength; }
        public int getRepeatAfter() { return repeatAfter; }
        public Direction getDirection() { return direction; }
        public int getSpacingBetween() { return spacingBetween; }
        public PrismColor getBackgroundColor() { return backgroundColor; }
        public int getNumberOfSnakes()    { return numberOfSnakes;  }

        /**
         * Sets the speed of the animation
         * @param speed from 0 to 1.
         */
        public void setSpeed(float speed) { this.speed = speed; }

        /**
         * Can be used to limit the colors shown in the rainbow. This is a hue so it reflects
         * the position as an angle on a color wheel. Red is 0, Green is 120, Blue is 240, etc.
         * @param startHue from 0 to 360.
         */
        public void setStartHue(float startHue) { this.startHue = startHue; }

        /**
         * Can be used to limit the colors shown in the rainbow. This is a hue so it reflects
         * the position as an angle on a color wheel. Red is 0, Green is 120, Blue is 240, etc.
         * @param stopHue from 0 to 360.
         */
        public void setStopHue(float stopHue) { this.stopHue = stopHue; }
        public void setHues(float startHue, float stopHue) {
            this.startHue = startHue;
            this.stopHue = stopHue;
        }
        public void setDirection(Direction direction) { this.direction = direction;     }

        /**
         * Length in pixels of each snake.
         * @param snakeLength from 0 to 255.
         */
        public void setSnakeLength(int snakeLength)   { this.snakeLength = snakeLength; }

        /**
         * The number of pixels between the last snake and the first snake of a repeating animation.
         * @param repeatAfter from 0 to 255.
         */
        public void setRepeatAfter(int repeatAfter)   { this.repeatAfter = repeatAfter; }

        /**
         * The number of pixels between consecutive snakes.
         * @param spacingBetween from 0 to 255.
         */
        public void setSpacingBetween(int spacingBetween)     { this.spacingBetween = spacingBetween;   }
        public void setBackgroundColor(PrismColor backgroundColor) { this.backgroundColor = backgroundColor; }
        public void setNumberOfSnakes(int numberOfSnakes)     { this.numberOfSnakes = numberOfSnakes;   }
        //endregion

        @Override
        protected void updateAnimationSpecificValuesOverI2C(I2cDeviceSynchSimple deviceClient)
        {
            deviceClient.write(layerHeight.register.address, GetByteArray(4 , startHue));
            deviceClient.write(layerHeight.register.address, GetByteArray(5 , stopHue));
            deviceClient.write(layerHeight.register.address, GetByteArray(6 , speed));
            deviceClient.write(layerHeight.register.address, GetByteArray(7 , direction));
            deviceClient.write(layerHeight.register.address, GetByteArray(8 , (byte)spacingBetween));
            deviceClient.write(layerHeight.register.address, GetByteArray(9 , (byte)repeatAfter));
            deviceClient.write(layerHeight.register.address, GetByteArray(10, backgroundColor));
            deviceClient.write(layerHeight.register.address, GetByteArray(11, (byte)snakeLength));
            deviceClient.write(layerHeight.register.address, GetByteArray(12, (byte)numberOfSnakes));
        }
    }

    public static class Random extends AnimationBase {
        private float startHue = 0f;
        private float stopHue  = 360f;
        private float speed    = 0.1f;

        //region Constructors
        public Random(){ super(AnimationType.RANDOM); }
        public Random(float startHue, float stopHue){
            this();
            setHues(startHue, stopHue);
        }
        public Random(float startHue, float stopHue, float speed){
            this(startHue, stopHue);
            this.speed = speed;
        }
        //endregion

        //region Getters/Setters
        public float getSpeed() { return speed; }
        public float getStopHue() { return stopHue; }
        public float getStartHue() { return startHue; }

        /**
         * Sets the speed of the animation
         * @param speed from 0 to 1.
         */
        public void setSpeed(float speed) { this.speed = speed; }

        /**
         * Can be used to limit the colors shown in the rainbow. This is a hue so it reflects
         * the position as an angle on a color wheel. Red is 0, Green is 120, Blue is 240, etc.
         * @param startHue from 0 to 360.
         */
        public void setStartHue(float startHue) { this.startHue = startHue; }

        /**
         * Can be used to limit the colors shown in the rainbow. This is a hue so it reflects
         * the position as an angle on a color wheel. Red is 0, Green is 120, Blue is 240, etc.
         * @param stopHue from 0 to 360.
         */
        public void setStopHue(float stopHue) { this.stopHue = stopHue; }
        public void setHues(float startHue, float stopHue) {
            this.startHue = startHue;
            this.stopHue = stopHue;
        }
        //endregion
        @Override
        protected void updateAnimationSpecificValuesOverI2C(I2cDeviceSynchSimple deviceClient)
        {
            deviceClient.write(layerHeight.register.address, GetByteArray(4, startHue));
            deviceClient.write(layerHeight.register.address, GetByteArray(5, stopHue));
            deviceClient.write(layerHeight.register.address, GetByteArray(6, speed));
        }
    }

    public static class SineWave extends AnimationBase {
        private PrismColor secondaryColor = PrismColor.BLUE;
        private PrismColor primaryColor   = PrismColor.RED;
        private Direction direction  = Direction.Forward;
        private float offset         = 0.5f;
        private float speed          = 0.5f;
        private int period           = 1000;

        public SineWave(){ super(AnimationType.SINE_WAVE); }
        public SineWave(PrismColor primaryColor) {
            this();
            this.primaryColor = primaryColor;
        }
        public SineWave(PrismColor primaryColor, PrismColor secondaryColor) {
            this(primaryColor);
            this.secondaryColor = secondaryColor;
        }
        public SineWave(PrismColor primaryColor, PrismColor secondaryColor, int period) {
            this(primaryColor, secondaryColor);
            this.period = period;
        }
        public SineWave(PrismColor primaryColor, PrismColor secondaryColor, int period, float speed) {
            this(primaryColor, secondaryColor, period);
            this.speed = speed;
        }
        public SineWave(PrismColor primaryColor, PrismColor secondaryColor, int period, float speed, float offset) {
            this(primaryColor, secondaryColor, period, speed);
            this.offset = offset;
        }
        public SineWave(PrismColor primaryColor, PrismColor secondaryColor, int period, float speed, float offset, Direction direction) {
            this(primaryColor, secondaryColor, period, speed, offset);
            this.direction = direction;
        }

        /**
         * set the period of the sine wave in milliseconds.
         * @param period from 0 - 4,294,697,295. Larger is longer.
         */
        public void setPeriod(int period)   { this.period = period; }

        /**
         * set the period of the sine wave in specified unit.
         * @param duration duration of loop.
         * @param timeUnit unit to use.
         */
        public void setPeriod(int duration, TimeUnit timeUnit){
            this.period = Math.toIntExact(timeUnit.toMillis(duration));
        }

        /**
         * Sets the speed of the animation
         * @param speed from 0 to 1.
         */
        public void setSpeed(float speed)   { this.speed = speed;   }

        /**
         * Sets the vertical height of the sine wave, this will increase the contrast between the
         * two shown colors.
         * @param offset from 0 to 1.
         */
        public void setOffset(float offset) { this.offset = offset; }
        public void setPrimaryColor(PrismColor color)      { primaryColor = color;       }
        public void setSecondaryColor(PrismColor color)    { secondaryColor = color;     }
        public void setDirection(Direction direction) { this.direction = direction; }
        public void setPrimaryColor(int red, int green, int blue)   { primaryColor = new PrismColor((byte)red, (byte)green, (byte)blue);   }
        public void setSecondaryColor(int red, int green, int blue) { secondaryColor = new PrismColor((byte)red, (byte)green, (byte)blue); }

        public int getPeriod()  { return period; }
        public float getSpeed() { return speed;  }
        public float getOffset(){ return offset; }
        public Direction getDirection()  { return direction;      }
        public PrismColor getPrimaryColor()   { return primaryColor;   }
        public PrismColor getSecondaryColor() { return secondaryColor; }

        @Override
        protected void updateAnimationSpecificValuesOverI2C(I2cDeviceSynchSimple deviceClient)
        {
            deviceClient.write(layerHeight.register.address, GetByteArray(4, primaryColor));
            deviceClient.write(layerHeight.register.address, GetByteArray(5, secondaryColor));
            deviceClient.write(layerHeight.register.address, GetByteArray(6, period));
            deviceClient.write(layerHeight.register.address, GetByteArray(7, direction));
            deviceClient.write(layerHeight.register.address, GetByteArray(8, offset));
            deviceClient.write(layerHeight.register.address, GetByteArray(9, speed));
        }
    }

    public static class SingleFill extends AnimationBase {
        private PrismColor[] colors        = {PrismColor.WHITE, PrismColor.GREEN, PrismColor.BLUE};
        private int period            = 750;
        private float speed           = 0.5f;
        private int pixelLength       = 3;
        private Direction direction   = Direction.Backward;
        private SingleFillStyle style = SingleFillStyle.FillIn;

        public enum SingleFillStyle{
            FillIn(0),
            FillOut(1);

            final byte styleValue;
            SingleFillStyle(int styleValue){ this.styleValue = (byte)styleValue; }
        }

        public SingleFill(){ super(AnimationType.SINGLE_FILL); }
        public SingleFill(PrismColor... colors) {
            this();
            this.colors = colors;
        }

        public int getPeriod() { return period; }
        public float getSpeed()  { return speed; }
        public PrismColor[] getColors() { return colors; }
        public int getPixelLength() { return pixelLength; }
        public Direction getDirection() { return direction; }
        public SingleFillStyle getStyle() { return style; }

        /**
         * set the period of the single fill in milliseconds.
         * @param period from 0 - 4,294,697,295. Larger is longer.
         */
        public void setPeriod(int period)  { this.period = period; }

        /**
         * set the period of the single fill in specified unit.
         * @param duration duration of loop.
         * @param timeUnit unit to use.
         */
        public void setPeriod(int duration, TimeUnit timeUnit){
            this.period = Math.toIntExact(timeUnit.toMillis(duration));
        }

        /**
         * Sets the speed of the animation
         * @param speed from 0 to 1.
         */
        public void setSpeed(float speed)        { this.speed = speed;   }
        public void setColors(PrismColor... colors) {
            for(int i = 0; i < Math.min(colors.length,10); i++){
                this.colors[i] = colors[i];
            }
        }
        public void setPixelLength(int pixelLength)   { this.pixelLength = pixelLength; }
        public void setStyle(SingleFillStyle style)   { this.style = style;             }    
        public void setDirection(Direction direction) { this.direction = direction;     }

        @Override
        protected void updateAnimationSpecificValuesOverI2C(I2cDeviceSynchSimple deviceClient)
        {
            deviceClient.write(layerHeight.register.address, GetByteArray(4, (byte)colors.length));
            deviceClient.write(layerHeight.register.address, GetByteArray(5, colors));
            deviceClient.write(layerHeight.register.address, GetByteArray(6, period));
            deviceClient.write(layerHeight.register.address, GetByteArray(7, direction));
            deviceClient.write(layerHeight.register.address, GetByteArray(8, (byte)pixelLength));
            deviceClient.write(layerHeight.register.address, GetByteArray(9, speed));
            deviceClient.write(layerHeight.register.address, GetByteArray(11, style.styleValue));
        }
    }

    public static class Snakes extends AnimationBase {
        private PrismColor[] colors        = {PrismColor.RED, PrismColor.WHITE, PrismColor.BLUE};
        private int snakeLength       = 5;
        private int spacingBetween    = 2;
        private int repeatAfter       = 15;
        private PrismColor backgroundColor = PrismColor.TRANSPARENT;
        private float speed           = 0.50f;
        private Direction direction   = Direction.Backward;

        //region Constructors
        public Snakes(){ super(AnimationType.SNAKES); }
        public Snakes(PrismColor... colors) {
            this();
            this.colors = colors;
        }
        public Snakes(int snakeLength, PrismColor... colors){
            this(colors);
            this.snakeLength = (byte)snakeLength;
        }
        public Snakes(int snakeLength, int spacingBetween, PrismColor... colors){
            this(snakeLength, colors);
            this.spacingBetween = (byte)spacingBetween;
        }
        public Snakes(int snakeLength, int spacingBetween, int repeatAfter, PrismColor... colors){
            this(snakeLength, spacingBetween, colors);
            this.repeatAfter = (byte)repeatAfter;
        }
        public Snakes(int snakeLength, int spacingBetween, int repeatAfter, PrismColor backgroundColor, PrismColor... colors){
            this(snakeLength, spacingBetween, repeatAfter, colors);
            this.backgroundColor = backgroundColor;
        }
        public Snakes(int snakeLength, int spacingBetween, int repeatAfter, PrismColor backgroundColor, float speed, PrismColor... colors){
            this(snakeLength, spacingBetween, repeatAfter, backgroundColor, colors);
            this.speed = speed;
        }
        public Snakes(int snakeLength, int spacingBetween, int repeatAfter, PrismColor backgroundColor, float speed, Direction direction, PrismColor... colors){
            this(snakeLength, spacingBetween, repeatAfter, backgroundColor, speed, colors);
            this.direction = direction;
        }
        //endregion

        //region Getters/Setters
        public float getSpeed()      { return speed;       }
        public PrismColor[] getColors()   { return colors;      }
        public int getSnakeLength() { return snakeLength; }
        public int getRepeatAfter() { return repeatAfter; }
        public Direction getDirection() { return direction; }
        public int getSpacingBetween() { return spacingBetween; }
        public PrismColor getBackgroundColor() { return backgroundColor; }

        /**
         * Sets the speed of the animation.
         * @param speed from 0 to 1.
         */
        public void setSpeed(float speed)      { this.speed = speed;  }
        public void setColors(PrismColor... colors) { this.colors = colors;}
        public void setDirection(Direction direction) { this.direction = direction;     }

        /**
         * Length in pixels of each snake.
         * @param snakeLength from 0 to 255.
         */
        public void setSnakeLength(int snakeLength)  { this.snakeLength = snakeLength; }

        /**
         * The number of pixels between the last snake and the first snake of a repeating animation.
         * @param repeatAfter from 0 to 255.
         */
        public void setRepeatAfter(int repeatAfter)  { this.repeatAfter = repeatAfter; }

        /**
         * The number of pixels between consecutive snakes.
         * @param spacingBetween from 0 to 255.
         */
        public void setSpacingBetween(int spacingBetween)    { this.spacingBetween = spacingBetween;   }
        public void setBackgroundColor(PrismColor backgroundColor) { this.backgroundColor = backgroundColor; }
        //endregion
        @Override
        protected void updateAnimationSpecificValuesOverI2C(I2cDeviceSynchSimple deviceClient)
        {
            deviceClient.write(layerHeight.register.address, GetByteArray(4,  (byte)colors.length));
            deviceClient.write(layerHeight.register.address, GetByteArray(5,  colors));
            deviceClient.write(layerHeight.register.address, GetByteArray(6,  speed));
            deviceClient.write(layerHeight.register.address, GetByteArray(7,  direction));
            deviceClient.write(layerHeight.register.address, GetByteArray(8,  (byte)spacingBetween));
            deviceClient.write(layerHeight.register.address, GetByteArray(9,  (byte)repeatAfter));
            deviceClient.write(layerHeight.register.address, GetByteArray(10, backgroundColor));
            deviceClient.write(layerHeight.register.address, GetByteArray(11, (byte)snakeLength));
        }
    }

    public static class Solid extends AnimationBase {
        private PrismColor primaryColor = PrismColor.RED;

        public Solid(){super(AnimationType.SOLID);}
        public Solid(PrismColor primaryColor) {
            super(AnimationType.SOLID);
            this.primaryColor = primaryColor;
        }
        public Solid(PrismColor primaryColor, int brightness) {
            super(AnimationType.SOLID, brightness);
            this.primaryColor = primaryColor;
        }
        public Solid(PrismColor primaryColor, int startIndex, int stopIndex){
            super(AnimationType.SOLID, startIndex, stopIndex);
            this.primaryColor = primaryColor;
        }
        public Solid(PrismColor primaryColor, int brightness, int startIndex, int stopIndex){
            super(AnimationType.SOLID, brightness, startIndex, stopIndex);
            this.primaryColor = primaryColor;
        }

        public void setPrimaryColor(int red, int green, int blue) { primaryColor = new PrismColor((byte)red, (byte)green, (byte)blue); }
        public void setPrimaryColor(PrismColor color) { primaryColor = color; }
        public PrismColor getPrimaryColor() { return primaryColor; }

        @Override
        protected void updateAnimationSpecificValuesOverI2C(I2cDeviceSynchSimple deviceClient)
        {
            deviceClient.write(layerHeight.register.address, GetByteArray(4, primaryColor));
        }
    }

    public static class Sparkle extends AnimationBase {
        private PrismColor primaryColor   = PrismColor.WHITE;
        private PrismColor secondaryColor = PrismColor.TRANSPARENT;
        private int sparkleProbability = 16;
        private int period = 100;

        //region Constructors
        public Sparkle() { super(AnimationType.SPARKLE); }
        public Sparkle(PrismColor primaryColor){
            this();
            this.primaryColor = primaryColor;
        }
        public Sparkle(PrismColor primaryColor, PrismColor secondaryColor){
            this(primaryColor); 
            this.secondaryColor = secondaryColor;
        }
        public Sparkle(PrismColor primaryColor, PrismColor secondaryColor, int period){
            this(primaryColor, secondaryColor); 
            this.period = period;
        }
        //endregion

        //region Getters/Setters
        public int getPeriod()         { return period;           }
        public PrismColor getPrimaryColor() { return primaryColor;     }
        public PrismColor getSecondaryColor() { return secondaryColor; }
        public int getSparkleProbability() { return sparkleProbability; }

        /**
         * sets the time between updating the sparkles.
         * @param period from 0 - 4,294,697,295. Larger is longer.
         */
        public void setPeriod(int period) { this.period = period; }
        public void setPrimaryColor(PrismColor primaryColor) { this.primaryColor = primaryColor; }
        public void setSecondaryColor(PrismColor secondaryColor) { this.secondaryColor = secondaryColor; }

        /**
         * Sets the probability/density of sparkles. Lower is denser. Default is 16.
         * @param sparkleProbability from 0 to 255.
         */
        public void setSparkleProbability(int sparkleProbability) { this.sparkleProbability = sparkleProbability; };
        //endregion
        @Override
        protected void updateAnimationSpecificValuesOverI2C(I2cDeviceSynchSimple deviceClient)
        {
            deviceClient.write(layerHeight.register.address, GetByteArray(4, primaryColor));
            deviceClient.write(layerHeight.register.address, GetByteArray(5, secondaryColor));
            deviceClient.write(layerHeight.register.address, GetByteArray(6, period));
            deviceClient.write(layerHeight.register.address, GetByteArray(7, (byte)sparkleProbability));
        }
    }
}
