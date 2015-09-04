package Controller.ClickRecorder;

import Controller.Bot.Bot;
import Controller.Bot.Position;

public class MouseClickEvent implements InterfaceEvent {

    public String comment, action;
    public int px;
    public int py;
    public int sleep1;
    public int sleep2;
    public int sleep3;
    public int precisionSleep;
    public int precisionMouse;
//    public Long id;

    /**
     *
     * @param c comment
     * @param a action
     * @param x px
     * @param y py
     * @param s1 sleep1
     * @param s2 sleep2
     * @param s3 sleep3
     * @param ps precisionSleep
     * @param pm precisionMouse
     */
    public MouseClickEvent(String c, String a, int x, int y, int s1, int s2, int s3, int ps, int pm) {
        action = a;
        px = x;
        py = y;
        sleep1 = s1;
        sleep2 = s2;
        sleep3 = s3;
        precisionSleep = ps;
        precisionMouse = pm;
        comment = c;
    }

    /**
     * Run the Bot with the MouseClickEvent.
     *
     * @param bot
     */
    @Override
    public void run(Bot bot) {
        System.out.println("  " + getComment() + " | " + getAction());
        bot.setMouseB(new Position(getPx(), getPy()), getPrecisionMouse());
        bot.sleepB(getSleep1(), getPrecisionSleep());
        switch (action) {
            case "LeftClick":
                bot.clickLeft(getSleep2(), getPrecisionSleep());
                break;
            case "RightClick":
                bot.clickRight(getSleep2(), getPrecisionSleep());
                break;
            case "WheeleClick":
                bot.clickWheele(getSleep2(), getPrecisionSleep());
                break;
            case "DoubleLeftClick":
                bot.clickDoubleLeft(getSleep2(), getPrecisionSleep());
                break;
            default:
                System.out.println("run: E: Unknown Mouse Event : " + action);
                break;
        }

        bot.sleepB(getSleep3(), getPrecisionSleep());
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the px
     */
    public int getPx() {
        return px;
    }

    /**
     * @param px the px to set
     */
    public void setPx(int px) {
        this.px = px;
    }

    /**
     * @return the py
     */
    public int getPy() {
        return py;
    }

    /**
     * @param py the py to set
     */
    public void setPy(int py) {
        this.py = py;
    }

    /**
     * @return the sleep1
     */
    public int getSleep1() {
        return sleep1;
    }

    /**
     * @param sleep1 the sleep1 to set
     */
    public void setSleep1(int sleep1) {
        this.sleep1 = sleep1;
    }

    /**
     * @return the sleep2
     */
    public int getSleep2() {
        return sleep2;
    }

    /**
     * @param sleep2 the sleep2 to set
     */
    public void setSleep2(int sleep2) {
        this.sleep2 = sleep2;
    }

    /**
     * @return the sleep3
     */
    public int getSleep3() {
        return sleep3;
    }

    /**
     * @param sleep3 the sleep3 to set
     */
    public void setSleep3(int sleep3) {
        this.sleep3 = sleep3;
    }

    /**
     * @return the precisionSleep
     */
    public int getPrecisionSleep() {
        return precisionSleep;
    }

    /**
     * @param precisionSleep the precisionSleep to set
     */
    public void setPrecisionSleep(int precisionSleep) {
        this.precisionSleep = precisionSleep;
    }

    /**
     * @return the precisionMouse
     */
    public int getPrecisionMouse() {
        return precisionMouse;
    }

    /**
     * @param precisionMouse the precisionMouse to set
     */
    public void setPrecisionMouse(int precisionMouse) {
        this.precisionMouse = precisionMouse;
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

}
