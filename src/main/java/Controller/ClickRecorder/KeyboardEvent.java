package Controller.ClickRecorder;

import Controller.Bot.Bot;
import Model.Conf;

public class KeyboardEvent implements InterfaceEvent {

    public String comment;
    public String keyboardKey;
    public int sleep;
//    public Long id;

    /**
     *
     * @param c comment
     * @param k keyboardKey
     * @param s
     */
    public KeyboardEvent(String c, String k, int s) {
        comment = c;
        keyboardKey = k;
        sleep = s;
    }

    /**
     * Run the Bot with the KeyboardEvent.
     *
     * @param bot
     */
    @Override
    public void run(Bot bot) {
        System.out.println("--------------------- " + comment + " ------------------------");
        System.out.println("KeyboardEvent: I: Pressing string '" + keyboardKey + "'");
        bot.pressString(keyboardKey, Conf.randInt(0, Conf.SLEEPBETWEENCHARACTERS)); // default delay between each character typed
        bot.sleepB(sleep, Conf.DEFAULTSLEEPPRECISION); // default precision sleep
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
     * @return the keyboardKey
     */
    public String getKeyboardKey() {
        return keyboardKey;
    }

    /**
     * @param keyboardKey the keyboardKey to set
     */
    public void setKeyboardKey(String keyboardKey) {
        this.keyboardKey = keyboardKey;
    }
//
//    /**
//     * @return the id
//     */
//    public Long getId() {
//        return id;
//    }
//
//    /**
//     * @param id the id to set
//     */
//    public void setId(Long id) {
//        this.id = id;
//    }

}
