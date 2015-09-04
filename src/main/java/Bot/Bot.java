package Bot;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bot {

    private Robot myRobot;
    private Random random;

    public Bot() {
        random = new Random();
        try {
            myRobot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Create a Bot from an existing Robot r.
     *
     * @param r an existing Robot r.
     */
    public Bot(Robot r) {
        myRobot = r;
        random = new Random();
    }

// ---------------------------------------------------------
// -------------------- BASICS -----------------------------
    /**
     * Sleep an amount of time with a precision.
     *
     * @param d Time to sleep
     * @param p Precision
     */
    public void sleepB(int d, int p) {
        int min = -(d * p) / 200,
                max = -min;
        int randomDelay = random.nextInt(max - min) + min;
//        System.out.println("sleepB: random of " + randomDelay + " ms over " + d + " ms.");
        myRobot.delay(d + randomDelay);
    }
// --------------------------------------------------------
// -------------------- MOUSE -----------------------------

    /**
     * Moove mouse to pos (x, y)
     *
     * @param xPx x pixel
     * @param yPx y pixel
     */
    public void setMouse(int xPx, int yPx) {
        myRobot.mouseMove(xPx, yPx);
    }

    /**
     * Same as setMouse with a secured random pixel margin of p.
     *
     * @param pos Position in pixel
     * @param p Precision
     */
    public void setMouseB(Position pos, int p) {
        int randomPxAddX = -1 + (int) (Math.random() * p),
                randomPxAddY = -1 + (int) (Math.random() * p);
        setMouse(pos.getxPixel() + randomPxAddX, pos.getyPixel() + randomPxAddY);
    }

    /**
     * Moove mouse. TODO : use random in delay
     *
     * @param xPx x pixel
     * @param delay dalay in sec
     */
    public void moveMouseVertically(int xPx, int delay) {
        for (int i = 0; i < 100; i++) {
            myRobot.delay(delay);
            myRobot.mouseMove(xPx, i);
        }
    }

    /**
     * Scroll mouse.
     *
     * @param notch
     */
    public void scroll(int notch) {
        myRobot.mouseWheel(notch);
    }

    /**
     * Simple Mouse Left clixk
     *
     * @param d The time to wait.
     * @param p Precision of time in %. Ex : p=10 and d=300 => clickLeft in
     * [270, 330] s.
     */
    public void clickLeft(int d, int p) {
        myRobot.mousePress(InputEvent.BUTTON1_MASK);
        sleepB(d, p);
        myRobot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

// -----------------------------------------------------------
// -------------------- KEYBOARD -----------------------------
    /**
     * Press a string.
     *
     * @param string The string to press.
     * @param delay The time in ms to wait before each character is pressed.
     */
    public void pressString(String string, int delay) {
        Keyboard keyboard = new Keyboard(myRobot);
        keyboard.type(string, delay);
    }

    /**
     * Get the curret Mouse position.
     * @return Position(x, y)
     */
    public Position getMouseLocation() {
        Position res;

        Point p = MouseInfo.getPointerInfo().getLocation();
        res = new Position(p);
//        int nX = MouseInfo.getPointerInfo().getLocation().x; //Get location
//        int nY = MouseInfo.getPointerInfo().getLocation().y; //Get location
//        res = new Position(nY, nY);

        return res;
    }

// -----------------------------------------------------------
// -------------------- PIXEL --------------------------------
    /**
     * Find bobber pixel, waite for change of pixel color.
     */
    public void detectPixel() {
        boolean end = false;
        int eR = 176;
        int eG = 208;
        int eB = 129;

        while (!end) {
            try {
                //GET MOUSE LOCATION
                int nX = MouseInfo.getPointerInfo().getLocation().x; //Get location
                int nY = MouseInfo.getPointerInfo().getLocation().y; //Get location
                //GET PIXEL COLOR OF nX AND nY
                Color c = myRobot.getPixelColor(nX, nY);
                int fR = c.getRed(),
                        fG = c.getGreen(),
                        fB = c.getBlue();
                //CHECK TO SEE IF THEY MATCH WITH R G AND B
                if ((fR == eR) && (fG == eG) && (fB == eB)) {
                    System.out.println("Found bobber, waiting for change of pixel color");
                    end = true;
                } else {
                    System.out.println("[" + nX + ", " + nY + "] Couldnt find bobber.\n"
                            + "(" + fR + ", " + fG + ", " + fB + ") Found\n"
                            + "(" + eR + ", " + eG + ", " + eB + ") Expected\n");
                }
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

// -----------------------------------------------------------
// -------------------- LAUNCHER -----------------------------
    /**
     * Sample bot starter.
     */
    public void startBot() {
//        setMouse(500, 500);
//        moveMouseVertically(1005, 100);
//        press('1', 100);
//        pressString("I can speak now !", 100);
//        scroll(-100);

//        detectPixel();
//        DofusFishingBot df = new DofusFishingBot(this);
//        df.fish();
    }
}
