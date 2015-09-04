package Controller.Bot;

import java.awt.Point;

public class Position extends Point {

    /**
     * A pixel position defined with 2 coordinates : (x, y)
     *
     * @param xp
     * @param yp
     */
    public Position(int xp, int yp) {
        x = xp;
        y = yp;
    }

    /**
     * Create a position from a Point.
     *
     * @param p
     */
    Position(Point p) {
        x = p.x;
        y = p.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Print the position.
     */
    public void print() {
        System.out.println(this);
    }

    // ------------------------------------
    // GETTER AND SETTERS
    // ------------------------------------
    /**
     * @return the xPixel
     */
    public int getxPixel() {
        return x;
    }

    /**
     * @param xPixel the xPixel to set
     */
    public void setxPixel(int xPixel) {
        this.x = xPixel;
    }

    /**
     * @return the yPixel
     */
    public int getyPixel() {
        return y;
    }

    /**
     * @param yPixel the yPixel to set
     */
    public void setyPixel(int yPixel) {
        this.y = yPixel;
    }
}
