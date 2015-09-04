package Controller.ClickRecorder;

import Controller.Bot.Bot;
import Model.Conf;
import Vue.UI;
import java.util.List;
import java.util.Map;

public class ClickRecorder {

    public final Bot bot;

    public ClickRecorder() {
        bot = new Bot();
    }

    /**
     * Run all events from the GUI. Start at line firstRow.
     *
     * @param ui
     */
    public void runEvents(UI ui) {
// 1. Initialisation
        List<Map<Integer, String>> lM = ui.getData().getMainList(),
                lL = ui.getData().getMouseList(),
                lK = ui.getData().getKeyBoardList();

        for (int i = 0; i <= lM.size() - 1; i++) {
            if (ui.isStop()) {
                break;
            }

            boolean llFound = false,
                    lkFound = false;

// 2. Change selected line in the main table
            ui.getjTable2().setRowSelectionInterval(i, i);

// 3. Switch action
            String comment = lM.get(i).get(Conf.COLMAINCOMMENT),
                    id = lM.get(i).get(Conf.COLMAINID),
                    action = lM.get(i).get(Conf.COLMAINACTION);
            switch (action) {
// 3.1 ACTIONMOUSE
                case Conf.ACTIONMOUSE:
                    int iMouse = findMouseRow(Integer.toString(i + 1), ui);
                    ui.getjTable1().setRowSelectionInterval(iMouse, iMouse); // mouse
                    runMouse(lL, id, llFound, comment, ui);
                    break;
// 3.2 ACTIONKEYBOARD
                case Conf.ACTIONKEYBOARD:
                    int iKeyboard = findKeyboardRow(Integer.toString(i + 1), ui);
                    ui.getjTable3().setRowSelectionInterval(iKeyboard, iKeyboard); // keyboard
                    runKeyBoard(lK, id, lkFound, comment, ui);
                    break;
                default:
                    System.out.println("runEvents: W: 'MainList' line " + i
                            + ", skipping unrecognized action '"
                            + action + "'.");
                    break;
            }

// 5. Update current selected row
            i = getFirstRow(ui, i); // update selected row.
        }
    }

    /**
     * Find the line matching the same id in the event mouse table.
     *
     * @param idToFind
     * @return
     */
    private int findMouseRow(String idToFind, UI ui) {
        int res = 0;
        boolean found = false;

        for (int i = 0; i <= ui.data.getMouseList().size() - 1; i++) {
            String idMouse = ui.data.getMouseList().get(i).get(Conf.getColMouse("COLMOUSEID"));
            if (idToFind.equals(idMouse)) {
                res = i;
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("findMouseRow: E: Can't find the id " + idToFind + " in the mouse list.");
        }

        return res;
    }

    /**
     * Find the line matching the same id in the event keyboard table.
     *
     * @param idToFind
     * @return
     */
    private int findKeyboardRow(String idToFind, UI ui) {
        int res = 0;
        boolean found = false;

        for (int i = 0; i <= ui.data.getKeyBoardList().size() - 1; i++) {
            String idMouse = ui.data.getKeyBoardList().get(i).get(Conf.getColKeyboard("COLKEYBOARDID"));
            if (idToFind.equals(idMouse)) {
                res = i;
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("findKeyboardRow: E: Can't find the id " + idToFind + " in the keyboard list.");
        }

        return res;
    }

    /**
     * Run keyboard.
     */
    private void runKeyBoard(List<Map<Integer, String>> lK, String id, boolean lkFound, String comment, UI ui) {
        for (Map<Integer, String> m : lK) {
            String s = (String) m.get(Conf.getColKeyboard("COLKEYBOARDID"));
            if (s.equals(id)) {
                lkFound = true;
                String keyboardKey = m.get(Conf.getColKeyboard("COLKEYBOARDKEY"));
//                        sleep = m.get(Conf.getColKeyboard("COLKEYBOARDSLEEP"));
                int sleep = Integer.parseInt(m.get(Conf.getColKeyboard("COLKEYBOARDSLEEP")));
                ui.getjLabel7().setText("e" + id + ": " + (sleep / 1000) + "s");
                KeyboardEvent ke = new KeyboardEvent(comment, keyboardKey, sleep);
                ke.run(getBot());
                break;
            }
        }
        if (!lkFound) {
            System.out.println("runEvents: E: Line with id = " + id + " was"
                    + " not found in KeyBoardList.");
        }
    }

    /**
     * Run Mouse.
     */
    private void runMouse(List<Map<Integer, String>> lL, String id, boolean llFound, String comment, UI ui) {
        for (Map<Integer, String> m : lL) {
            String s = (String) m.get(Conf.getColMouse("COLMOUSEID"));
            if (s.equals(id)) {
                llFound = true;
                int px = Integer.parseInt(m.get(Conf.getColMouse("COLMOUSEPX"))),
                        py = Integer.parseInt(m.get(Conf.getColMouse("COLMOUSEPY"))),
                        sleep1 = Integer.parseInt(m.get(Conf.getColMouse("COLMOUSESLEEP1"))),
                        sleep2 = Integer.parseInt(m.get(Conf.getColMouse("COLMOUSESLEEP2"))),
                        sleep3 = Integer.parseInt(m.get(Conf.getColMouse("COLMOUSESLEEP3"))),
                        precisionSleep = Integer.parseInt(m.get(Conf.getColMouse("COLMOUSEPRECISIONSLEEP"))),
                        precisionMouse = Integer.parseInt(m.get(Conf.getColMouse("COLMOUSEPRECISIONMOUSE")));
                String action = m.get(Conf.getColMouse("COLMOUSEACTION"));
                ui.getjLabel7().setText("e" + id + ": " + (sleep3 / 1000) + "s");
                MouseClickEvent lmce = new MouseClickEvent(comment, action, px, py, sleep1, sleep2, sleep3, precisionSleep, precisionMouse);
                lmce.run(getBot());
                break;
            }
        }
        if (!llFound) {
            System.out.println("runEvents: E: Line with id = " + id + " was"
                    + " not found in mouseList.");
        }
    }

    /**
     * Get first row selected. If no row is selected, return currentRow.
     *
     * @return
     */
    private int getFirstRow(UI ui, int currentRow) {
        int res = currentRow;
        int[] rows = ui.getjTable2().getSelectedRows();

        if (rows.length != 0) {
            int firstSelectedRow = rows[0];
            if (currentRow != firstSelectedRow) { // user has clicked a row
                res = firstSelectedRow - 1;// "-1" because of next loop i++.
            }
        }

        return res;
    }

    /**
     * @return the bot
     */
    public Bot getBot() {
        return bot;
    }

}
