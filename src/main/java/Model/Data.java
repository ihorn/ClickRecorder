package Model;

import Controller.Bot.Position;
import Vue.UI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

public class Data {

//    private List<Object[]> mainList, mouseList, keyBoardList;
    public Long totalRepititions;
    public Long sleepInterval;
    public List<Map<Integer, String>> mainList;
    public List<Map<Integer, String>> mouseList;
    public List<Map<Integer, String>> keyBoardList;

    public Data() {
        mainList = new ArrayList<>();
        mouseList = new ArrayList<>();
        keyBoardList = new ArrayList<>();
        totalRepititions = (long) 1;
    }

    public Data(List<Map<Integer, String>> ml, List<Map<Integer, String>> ll, List<Map<Integer, String>> kl) {
        this.mainList = ml;
        this.mouseList = ll;
        this.keyBoardList = kl;
    }

    public void addMainAction(Object[] object) {
        Map<Integer, String> m = new HashMap<>();
        m.put(Conf.COLMAINCOMMENT, (String) object[Conf.COLMAINCOMMENT]);
        m.put(Conf.COLMAINACTION, (String) object[Conf.COLMAINACTION]);
        m.put(Conf.COLMAINID, (String) object[Conf.COLMAINID]);
        getMainList().add(m);
    }

    public void addMouseAction(Object[] object) {
        Map<Integer, String> m = new HashMap<>();

        for (int col = 1; col <= Conf.colMouse.length; col++) {
            m.put(col, (String) object[col]);
        }
//        
//        m.put(Conf.COLMOUSECOMMENT, (String) object[Conf.COLMOUSECOMMENT]);
//        m.put(Conf.COLMOUSEPX, (String) object[Conf.COLMOUSEPX]);
//        m.put(Conf.COLMOUSEPY, (String) object[Conf.COLMOUSEPY]);
//        m.put(Conf.COLMOUSESLEEP1, (String) object[Conf.COLMOUSESLEEP1]);
//        m.put(Conf.COLMOUSESLEEP2, (String) object[Conf.COLMOUSESLEEP2]);
//        m.put(Conf.COLMOUSESLEEP3, (String) object[Conf.COLMOUSESLEEP3]);
//        m.put(Conf.COLMOUSEPRECISIONSLEEP, (String) object[Conf.COLMOUSEPRECISIONSLEEP]);
//        m.put(Conf.COLMOUSEPRECISIONMOUSE, (String) object[Conf.COLMOUSEPRECISIONMOUSE]);
//        m.put(Conf.COLMOUSEID, (String) object[Conf.COLMOUSEID]);
        getMouseList().add(m);
    }

    public void addKeyboardAction(Object[] object) {
        Map<Integer, String> m = new HashMap<>();

        for (int col = 1; col <= Conf.colKeyboard.length; col++) {
            m.put(col, (String) object[col]);
        }

//        m.put(Conf.getColKeyboard("COLKEYBOARDCOMMENT"), (String) object[Conf.getColKeyboard("COLKEYBOARDCOMMENT")]);
//        m.put(Conf.getColKeyboard("COLKEYBOARDKEY"), (String) object[Conf.getColKeyboard("COLKEYBOARDKEY")]);
//        m.put(Conf.getColKeyboard("COLKEYBOARDSLEEP"), (String) object[Conf.getColKeyboard("COLKEYBOARDSLEEP")]);
//        m.put(Conf.getColKeyboard("COLKEYBOARDID"), (String) object[Conf.getColKeyboard("COLKEYBOARDID")]);
        getKeyBoardList().add(m);
    }

    /**
     * Remove Item in the Main List.
     *
     * @param idDel The id to delete
     */
    public void removeMain(long idDel) {
        for (Map<Integer, String> h : getMainList()) {
            long id = Long.parseLong((String) h.get(Conf.COLMAINID));
            if (idDel == id) {
                getMainList().remove(h);
                break;
            }
        }
    }

    /**
     * Remove item in the mouseList.
     *
     * @param idDel The id to delete
     */
    public void removeMouse(long idDel) {
        for (Map<Integer, String> h : getMouseList()) {
            long id = Long.parseLong((String) h.get(Conf.getColMouse("COLMOUSEID")));
            if (idDel == id) {
                getMouseList().remove(h);
                break;
            }
        }
    }

    /**
     * Remove Item in the Keyboard List.
     *
     * @param idDel The id to delete
     */
    public void removeKeyboard(long idDel) {
        for (Map<Integer, String> h : getKeyBoardList()) {
            long id = Long.parseLong((String) h.get(Conf.getColKeyboard("COLKEYBOARDID")));
            if (idDel == id) {
                getKeyBoardList().remove(h);
                break;
            }
        }
    }
//
//    /**
//     * Duplicate Item in the Main List.
//     *
//     * @param duplicateId The duplicated id
//     * @param newId The last id
//     */
//    public void duplicateMain(long duplicateId, long newId) {
//        for (Map<Integer, String> h : getMainList()) {
//            long id = Long.parseLong((String) h.get(Conf.COLMAINID));
//            if (duplicateId == id) {
//                Map<Integer, String> newH = new HashMap<>(h);
//                newH.put(Conf.COLMAINID, Long.toString(newId));
//                getMainList().add(newH);
//                break;
//            }
//        }
//    }
//
//    /**
//     * Duplicate item in the mouseList.
//     *
//     * @param duplicateId The duplicated id
//     * @param newId The last id
//     */
//    public void duplicateMouse(long duplicateId, long newId) {
//        for (Map<Integer, String> h : getMouseList()) {
//            long id = Long.parseLong((String) h.get(Conf.getColMouse("COLMOUSEID")));
//            if (duplicateId == id) {
//                Map<Integer, String> newH = new HashMap<>(h);
//                newH.put(Conf.getColMouse("COLMOUSEID"), Long.toString(newId));
//                getMouseList().add(newH);
//                break;
//            }
//        }
//    }
//
//    /**
//     * Duplicate Item in the Keyboard List.
//     *
//     * @param duplicateId The duplicated id
//     * @param newId The last id
//     */
//    public void duplicateKeyboard(long duplicateId, long newId) {
//        for (Map<Integer, String> h : getKeyBoardList()) {
//            long id = Long.parseLong((String) h.get(Conf.getColKeyboard("COLKEYBOARDID")));
//            if (duplicateId == id) {
//                Map<Integer, String> newH = new HashMap<>(h);
//                newH.put(Conf.getColKeyboard("COLKEYBOARDID"), Long.toString(newId));
//                getKeyBoardList().add(newH);
//                break;
//            }
//        }
//    }

    /**
     * Duplicate Item in the Main List.
     *
     * @param setId The duplicated id
     * @param comment
     */
    public void setMain(long setId, String comment) {
        for (Map<Integer, String> h : getMainList()) {
            long id = Long.parseLong((String) h.get(Conf.COLMAINID));
            if (setId == id) {
                h.put(Conf.COLMAINCOMMENT, comment);
                break;
            }
        }
    }

//    /**
//     * Duplicate item in the mouseList.
//     *
//     * @param setId The duplicated id
//     * @param comment
//     * @param px
//     * @param py
//     * @param precisionMouse
//     * @param sleep2
//     * @param sleep3
//     * @param sleep1
//     * @param precisionSleep
//     */
//    public void setMouseClick(long setId, String comment, String px, String py, String sleep1, String sleep2, String sleep3, String precisionSleep, String precisionMouse) {
//
//    }
//    /**
//     * Duplicate Item in the Keyboard List.
//     *
//     * @param setId The duplicated id
//     * @param comment
//     * @param keyboardKey
//     * @param sleep
//     */
//    public void setKeyboard(long setId, String comment, String keyboardKey, String sleep) {
//        for (Map<Integer, String> h : getKeyBoardList()) {
//            long id = Long.parseLong((String) h.get(Conf.getColKeyboard("COLKEYBOARDID")));
//            if (setId == id) {
//                h.put(Conf.getColKeyboard("COLKEYBOARDCOMMENT"), comment);
//                h.put(Conf.getColKeyboard("COLKEYBOARDKEY"), keyboardKey);
//                h.put(Conf.getColKeyboard("COLKEYBOARDSLEEP"), sleep);
//                break;
//            }
//        }
//    }
    /**
     * Synchronyse events when user changes comments in the main table.
     *
     * @param syncId
     * @param comment
     */
    private void synchroniseEvents(long syncId, String comment, String action) {
        switch (action) {
            case Conf.ACTIONMOUSE:
                for (Map<Integer, String> h : getMouseList()) {
                    long id = Long.parseLong((String) h.get(Conf.getColMouse("COLMOUSEID")));
                    if (syncId == id) {
                        h.put(Conf.getColMouse("COLMOUSECOMMENT"), comment);
                        break;
                    }
                }
                break;
            case Conf.ACTIONKEYBOARD:
                for (Map<Integer, String> h : getKeyBoardList()) {
                    long id = Long.parseLong((String) h.get(Conf.getColKeyboard("COLKEYBOARDID")));
                    if (syncId == id) {
                        h.put(Conf.getColKeyboard("COLKEYBOARDCOMMENT"), comment);
                        break;
                    }
                }
                break;
            default:
                System.out.println("synchroniseEvents: E: Unrecognized action : " + action);
                break;
        }
    }

    public void updateMainListFromTable(DefaultTableModel dtm, int d) {
        String comment = getStringValue(dtm.getValueAt(d, Conf.COLMAINCOMMENT - 1)),
                action = getStringValue(dtm.getValueAt(d, Conf.COLMAINACTION - 1)),
                id = getStringValue(dtm.getValueAt(d, Conf.COLMAINID - 1));
        setMain(Long.parseLong(id), comment);
        synchroniseEvents(Long.parseLong(id), comment, action);
    }

    public void updateMouseListFromTable(DefaultTableModel dtm, int d) {
//        System.out.println("(" + d + ", " + (Conf.getColMouse("COLMOUSEID") - 1) + ") " + 
//                dtm.getValueAt(d, Conf.getColMouse("COLMOUSEID") - 1));
        String comment = getStringValue(dtm.getValueAt(d, Conf.getColMouse("COLMOUSECOMMENT") - 1)),
                //                px = getStringValue(dtm.getValueAt(d, Conf.getColMouse("COLMOUSEPX") - 1)),
                //                py = getStringValue(dtm.getValueAt(d, Conf.getColMouse("COLMOUSEPY") - 1)),
                //                sleep1 = getStringValue(dtm.getValueAt(d, Conf.getColMouse("COLMOUSESLEEP1") - 1)),
                //                sleep2 = getStringValue(dtm.getValueAt(d, Conf.getColMouse("COLMOUSESLEEP2") - 1)),
                //                sleep3 = getStringValue(dtm.getValueAt(d, Conf.getColMouse("COLMOUSESLEEP3") - 1)),
                //                precisionSleep = getStringValue(dtm.getValueAt(d, Conf.getColMouse("COLMOUSEPRECISIONSLEEP") - 1)),
                //                precisionMouse = getStringValue(dtm.getValueAt(d, Conf.getColMouse("COLMOUSEPRECISIONMOUSE") - 1)),
                setId = getStringValue(dtm.getValueAt(d, Conf.getColMouse("COLMOUSEID") - 1));

        for (Map<Integer, String> h : getMouseList()) {
            long id = Long.parseLong((String) h.get(Conf.getColMouse("COLMOUSEID")));
            if (Long.parseLong(setId) == id) {
                for (int col = 1; col <= Conf.colMouse.length + 1; col++) {
                    if (col == Conf.colMouse.length + 1) { // don't put the id
                    } else {
                        h.put(col, getStringValue(dtm.getValueAt(d, col - 1)));
                    }
                }

//                h.put(Conf.getColMouse("COLMOUSECOMMENT"), comment);
//                h.put(Conf.getColMouse("COLMOUSEPX"), px);
//                h.put(Conf.getColMouse("COLMOUSEPY"), py);
//                h.put(Conf.getColMouse("COLMOUSESLEEP1"), sleep1);
//                h.put(Conf.getColMouse("COLMOUSESLEEP2"), sleep2);
//                h.put(Conf.getColMouse("COLMOUSESLEEP3"), sleep3);
//                h.put(Conf.getColMouse("COLMOUSEPRECISIONSLEEP"), precisionSleep);
//                h.put(Conf.getColMouse("COLMOUSEPRECISIONMOUSE"), precisionMouse);
                break;
            }
        }

//        setMouseClick(Long.parseLong(id), comment, px, py, sleep1, sleep2, sleep3, precisionSleep, precisionMouse);
        setMain(Long.parseLong(setId), comment);
    }

    public void updategetKeyBoardListFromTable(DefaultTableModel dtm, int d) {
        String comment = getStringValue(dtm.getValueAt(d, Conf.getColKeyboard("COLKEYBOARDCOMMENT") - 1)),
                //                keyboardKey = getStringValue(dtm.getValueAt(d, Conf.getColKeyboard("COLKEYBOARDKEY") - 1)),
                //                sleep = getStringValue(dtm.getValueAt(d, Conf.getColKeyboard("COLKEYBOARDSLEEP") - 1)),
                setId = getStringValue(dtm.getValueAt(d, Conf.getColKeyboard("COLKEYBOARDID") - 1));
//        setKeyboard(Long.parseLong(id), comment, keyboardKey, sleep);
        for (Map<Integer, String> h : getKeyBoardList()) {
            long id = Long.parseLong((String) h.get(Conf.getColKeyboard("COLKEYBOARDID")));
            if (Long.parseLong(setId) == id) {
                for (int col = 1; col <= Conf.colKeyboard.length + 1; col++) {
                    if (col == Conf.colKeyboard.length + 1) { // don't put the id
                    } else {
                        h.put(col, getStringValue(dtm.getValueAt(d, col - 1)));
                    }
                }

//                h.put(Conf.getColKeyboard("COLKEYBOARDCOMMENT"), comment);
//                h.put(Conf.getColKeyboard("COLKEYBOARDKEY"), keyboardKey);
//                h.put(Conf.getColKeyboard("COLKEYBOARDSLEEP"), sleep);
                break;
            }
        }

        setMain(Long.parseLong(setId), comment);
    }

    public void initDefault(long id) {
        setMainList(new ArrayList<Map<Integer, String>>());
        setMouseList(new ArrayList<Map<Integer, String>>());
        setKeyBoardList(new ArrayList<Map<Integer, String>>());
        addDefault(id);
    }

    /**
     * Add a default set of events.
     *
     * @param id
     */
    public void addDefault(long id) {
        getMainList().add(Conf.getDefaultMainClick(id));
        getMouseList().add(Conf.getDefaultMouse(id));

        getMainList().add(Conf.getDefaultMainKeyboard(id + 1));
        getKeyBoardList().add(Conf.getDefaultKeyboard(id + 1));
    }

    /**
     * Add current mouse position.
     *
     * @param ui
     * @param id
     */
    public void addCurrentMouse(UI ui, long id) {
        System.out.println("addCurrentMouse: I: Sleeping 3s before getting mouse position. Set your mouse !");

        for (int j = Conf.SELLPBEFOREADDMOUSEPOS; j >= 1; j--) {
            ui.getjLabel5().setText("[" + j + "s]");
            Position p = ui.getCr().bot.getMouseLocation();
            ui.getjLabel7().setText("currentMousePosition: " + p);
            ui.getCr().bot.sleepB(1000, Conf.DEFAULTSLEEPPRECISION);
        }
        ui.getjLabel5().setText("");
        Position p = ui.getCr().bot.getMouseLocation();
        ui.getjLabel7().setText("currentMousePosition: " + p);
        System.out.println("addCurrentMouse: I: New position : " + p);
        getMainList().add(Conf.getMouseMainClick(p, id));
        getMouseList().add(Conf.getNewMouse(p, id));
        id++;

    }

    private String getStringValue(Object o) {
        String res = "";
        if (o instanceof String) {
            res = (String) o;
        } else if (o instanceof Integer) {
            res = String.valueOf((int) o);
        } else {
            System.out.println("getStringValue: E: Class '" + o.getClass() + "' not found.");
        }

        return res;
    }

    public List<Map<Integer, String>> getMainList() {
        return mainList;
    }

    public void setMainList(List<Map<Integer, String>> mainList) {
        this.mainList = mainList;
    }

    public List<Map<Integer, String>> getMouseList() {
        return mouseList;
    }

    public void setMouseList(List<Map<Integer, String>> mouseList) {
        this.mouseList = mouseList;
    }

    public List<Map<Integer, String>> getKeyBoardList() {
        return keyBoardList;
    }

    public void setKeyBoardList(List<Map<Integer, String>> keyBoardList) {
        this.keyBoardList = keyBoardList;
    }

    /**
     * @return the totalRepititions
     */
    public Long getTotalRepititions() {
        return totalRepititions;
    }

    /**
     * @param totalRepititions the totalRepititions to set
     */
    public void setTotalRepititions(Long totalRepititions) {
        this.totalRepititions = totalRepititions;
    }

    /**
     * @return the sleepInterval
     */
    public Long getSleepInterval() {
        return sleepInterval;
    }

    /**
     * @param sleepInterval the sleepInterval to set
     */
    public void setSleepInterval(Long sleepInterval) {
        this.sleepInterval = sleepInterval;
    }

}
