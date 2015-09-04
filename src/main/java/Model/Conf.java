package Model;

import Controller.Bot.Position;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.swing.table.DefaultTableModel;

public class Conf {

    public static final String VERSION = "0.5";

// ---------------------------- TABLE ----------------------------
    public static final Integer COLMAINCOMMENT = 1, // keep
            COLMAINACTION = 2, // keep
            COLMAINID = 3; // keep
// Columns to display Mouse events
    public final static Object[][] colMouse = new Object[][]{
        {"COLMOUSECOMMENT"}, // do not move
        {"COLMOUSEACTION"}, // do not move
        {"COLMOUSEPX"},
        {"COLMOUSEPY"},
        {"COLMOUSESLEEP1"},
        {"COLMOUSESLEEP2"},
        {"COLMOUSESLEEP3"},
        {"COLMOUSEPRECISIONSLEEP"},
        {"COLMOUSEPRECISIONMOUSE"},
        {"COLMOUSEID"} // always at the end
    }, colMouseDefault = new Object[][]{
        {"MouseComment"}, // do not move
        {"LeftClick"}, // do not move
        {"50"},
        {"50"},
        {"100"},
        {"50"},
        {"7000"},
        {"20"},
        {"2"}
    };

// Columns to display Keyboard events
    public final static Object[][] colKeyboard = new Object[][]{
        {"COLKEYBOARDCOMMENT"},
        {"COLKEYBOARDKEY"},
        {"COLKEYBOARDSLEEP"},
        {"COLKEYBOARDID"} // always at the end
    }, colKeyboardDefault = new Object[][]{
        {"KeyboardComment"},
        {"Escape"},
        {"7000"}
    };

// ---------------------------- FILES ----------------------------
    public static final String ACTIONMOUSE = "Mouse",
            ACTIONKEYBOARD = "Keyboard";

// ---------------------------- TEXT ----------------------------
    public static final String TITLE = "Dofus - Bot Recorder v" + VERSION,
            PATHRECORD = "./record/",
            FILEEXTENSION = ".csv",
            PATHIMG = "./img/",
            PATHTOFILEONDISK = PATHIMG + "icon.jpg",
            DEFAULTBOTSETTINGSNAME = "BotSettings",
            DEFAULTBOTREPEATNUMBER = "2",
            DEFAULTBOTDESCRIPTION = "Put here the description of this bot.";

// ------------------------------------------------------------
// BOT DEFAULT PARAMETERS
// ------------------------------------------------------------
    public static final Long FIRSTID = (long) 1;
    public static int SELLPBEFOREADDMOUSEPOS = 3,
            DEFAULTSLEEPPRECISION = 20,
            SLEEPBETWEENCHARACTERS = 1000;

    /**
     * New mouse current position.
     *
     * @param p
     * @param id
     * @return
     */
    public static final Map<Integer, String> getNewMouse(Position p, long id) {
        Map<Integer, String> res = getDefaultMouse(id);

        for (Object[] o : colMouseDefault) {
            for (int col = 0; col <= colMouse.length - 1; col++) {
                String colName = (String) colMouse[col][0];
                switch (colName) {
                    case "COLMOUSECOMMENT":
                        res.put(col + 1, p.toString());
                        break;
                    case "COLMOUSEPX":
                        res.put(col + 1, Integer.toString(p.getxPixel()));
                        break;
                    case "COLMOUSEPY":
                        res.put(col + 1, Integer.toString(p.getyPixel()));
                        break;
                }
            }
        }

//        res.put(COLMOUSECOMMENT, p.toString());
//        res.put(COLMOUSEPX, Integer.toString(p.getxPixel()));
//        res.put(COLMOUSEPY, Integer.toString(p.getyPixel()));
//        res.put(COLMOUSESLEEP1, DEFAULTSLEEP1);
//        res.put(COLMOUSESLEEP2, DEFAULTSLEEP2);
//        res.put(COLMOUSESLEEP3, "1000");
//        res.put(COLMOUSEPRECISIONSLEEP, DEFAULTPRECISIONSLEEP);
//        res.put(COLMOUSEPRECISIONMOUSE, DEFAULTPRECISIONMOUSE);
//        res.put(COLMOUSEID, Long.toString(id));
        return res;
    }

// ------------------------------------------------------------
// OTHERS METHODS
// ------------------------------------------------------------
    /**
     * Remove all lines from a Dtm
     *
     * @param tm
     */
    public static void cleanTable(DefaultTableModel tm) {
        if (tm.getRowCount() > 0) {
            for (int i = tm.getRowCount() - 1; i > -1; i--) {
                tm.removeRow(i);
            }
        }
    }

    /**
     * Default values when adding a new line.
     *
     * @param id
     * @return
     */
    public static final Map<Integer, String> getDefaultMainClick(long id) {
        Map<Integer, String> res = new HashMap<>();

        res.put(1, (String) colMouseDefault[0][0]);
        res.put(2, ACTIONMOUSE);
        res.put(3, Long.toString(id));

        return res;
    }

    /**
     * Default values when adding a new line.
     *
     * @param id
     * @return
     */
    public static final Map<Integer, String> getDefaultMouse(long id) {
        Map<Integer, String> res = new HashMap<>();

        for (int col = 1; col <= colMouse.length; col++) {
            if (col == colMouse.length) {
                res.put(col, Long.toString(id)); // current id
            } else {
                res.put(col, (String) colMouseDefault[col - 1][0]); // default values
            }
        }

//        res.put(COLMOUSECOMMENT, "");
//        res.put(COLMOUSEPX, "50");
//        res.put(COLMOUSEPY, "50");
//        res.put(COLMOUSESLEEP1, DEFAULTSLEEP1);
//        res.put(COLMOUSESLEEP2, DEFAULTSLEEP2);
//        res.put(COLMOUSESLEEP3, "");
//        res.put(COLMOUSEPRECISIONSLEEP, DEFAULTPRECISIONSLEEP);
//        res.put(COLMOUSEPRECISIONMOUSE, DEFAULTPRECISIONMOUSE);
//        res.put(COLMOUSEID, Long.toString(id));
        return res;
    }

    /**
     * Add current mouse position.
     *
     * @param p
     * @param id
     * @return
     */
    public static final Map<Integer, String> getMouseMainClick(Position p, long id) {
        Map<Integer, String> res = new HashMap<>();

        res.put(1, p.toString());
        res.put(2, (String) ACTIONMOUSE);
        res.put(3, Long.toString(id));

        return res;
    }

    /**
     * Default values when adding a new line.
     *
     * @param id
     * @return
     */
    public static final Map<Integer, String> getDefaultMainKeyboard(long id) {
        Map<Integer, String> res = new HashMap<>();

        res.put(1, (String) colKeyboardDefault[0][0]);
        res.put(2, (String) ACTIONKEYBOARD);
        res.put(3, Long.toString(id));

        return res;
    }

    /**
     * Default values when adding a new line.
     *
     * @param id
     * @return
     */
    public static final Map<Integer, String> getDefaultKeyboard(long id) {
        Map<Integer, String> res = new HashMap<>();

        for (int col = 1; col <= colKeyboard.length; col++) {
            if (col == colKeyboard.length) {
                res.put(col, Long.toString(id)); // current id
            } else {
                res.put(col, (String) colKeyboardDefault[col - 1][0]); // default values
            }
        }

//        res.put(COLKEYBOARDCOMMENT, DEFAULTCOMMENT);
//        res.put(COLKEYBOARDKEY, DEFAULTKEYBOARDKEY);
//        res.put(COLKEYBOARDSLEEP, DEFAULTSLEEP3);
//        res.put(COLKEYBOARDID, Long.toString(id));
        return res;
    }

    public static Object[] generateObjectMouse(Map<Integer, String> m) {
        Object[] res = new Object[colMouse.length + 1];

        for (int col = 1; col <= colMouse.length + 1; col++) {
            res[col - 1] = m.get(col);
        }

//        Object[] res = new Object[]{
//            m.get(Conf.COLMOUSECOMMENT),
//            m.get(Conf.COLMOUSEPX),
//            m.get(Conf.COLMOUSEPY),
//            m.get(Conf.COLMOUSESLEEP1),
//            m.get(Conf.COLMOUSESLEEP2),
//            m.get(Conf.COLMOUSESLEEP3),
//            m.get(Conf.COLMOUSEPRECISIONSLEEP),
//            m.get(Conf.COLMOUSEPRECISIONMOUSE),
//            m.get(Conf.COLMOUSEID)
//        };
        return res;
    }

    public static Object[] generateObjectMain(Map<Integer, String> m) {
        Object[] res = new Object[]{
            m.get(Conf.COLMAINCOMMENT),
            m.get(Conf.COLMAINACTION),
            m.get(Conf.COLMAINID)
        };
        return res;
    }

    public static Object[] generateObjectKeyBoard(Map<Integer, String> m) {

        Object[] res = new Object[colKeyboard.length];
        for (int col = 0; col <= colKeyboard.length - 1; col++) {
            res[col] = m.get(col + 1); // default values
        }
//        Object[] res = new Object[]{
//            m.get(Conf.COLKEYBOARDCOMMENT),
//            m.get(Conf.COLKEYBOARDKEY),
//            m.get(Conf.COLKEYBOARDSLEEP),
//            m.get(Conf.COLKEYBOARDID)
//        };
        return res;
    }

    /**
     * Return the first file in a directory.
     *
     * @param path
     * @return
     */
    public static File getFirstFile(String path) {
        File res = new File(path);
        File[] fileList = res.listFiles();
        if (fileList.length >= 1) {
            for (File f : fileList) {
                if (f.getName().contains(FILEEXTENSION)) {
                    res = f;
                    System.out.println("getFirstFile: I: Loading file '" + res.getName() + "'.");
                    break;
                }
            }
        } else {
            res = null;
            System.out.println("getFirstFile: E: No file in directory " + path + ".");
        }
        return res;
    }

    /**
     * Returns the first default line of a .bot file.
     *
     * @return
     */
    public static String[] getFirstLine() {
        String[] res = {
            DEFAULTBOTSETTINGSNAME,
            DEFAULTBOTREPEATNUMBER,
            DEFAULTBOTDESCRIPTION
        };
        return res;
    }

    /**
     * Find the column number with a string name of a column.
     *
     * @param s
     * @return
     */
    public static int getColMouse(String s) {
        int res = -1;
        boolean found = false;

        for (int i = 0; i <= colMouse.length - 1; i++) {
            if (colMouse[i][0].equals(s)) {
                found = true;
                res = i + 1;
            }
        }

        if (!found) {
            System.out.println("getCol: E: Column not found : " + s);
        }

        return res;
    }

    /**
     * Find the column number with a string name of a column.
     *
     * @param s
     * @return
     */
    public static int getColKeyboard(String s) {
        int res = -1;
        boolean found = false;

        for (int i = 0; i <= colKeyboard.length - 1; i++) {
            if (colKeyboard[i][0].equals(s)) {
                found = true;
                res = i + 1;
            }
        }

        if (!found) {
            System.out.println("getColKeyboard: E: Column not found : " + s);
        }

        return res;
    }

    /**
     * Generate a random number between min and max. NOTE: Usually this should
     * be a field rather than a method variable so that it is not re-seeded
     * every call.
     *
     * @param min
     * @param max
     * @return
     */
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min; // nextInt is normally exclusive of the top value, so add 1 to make it inclusive
        return randomNum;
    }

}

//    public static final Integer COLMOUSECOMMENT = 1,
//            COLMOUSEACTION = 2,
//            COLMOUSEPX = 3,
//            COLMOUSEPY = 4,
//            COLMOUSESLEEP1 = 5,
//            COLMOUSESLEEP2 = 6,
//            COLMOUSESLEEP3 = 7,
//            COLMOUSEPRECISIONSLEEP = 8,
//            COLMOUSEPRECISIONMOUSE = 9,
//            COLMOUSEID = 10;
//    public static final Integer COLKEYBOARDCOMMENT = 1,
//            COLKEYBOARDKEY = 2,
//            COLKEYBOARDSLEEP = 3,
//            COLKEYBOARDID = 4;
//    /**
//     * Columns to display Main events
//     */
//    public final static Object[][] colMain = new Object[][]{
//        {"COLMAINCOMMENT"},
//        {"COLMAINACTION"},
//        {"COLMAINID"}
//    }, colMainDefault = new Object[][]{
//        {"MainCommant"},
//        {"Mouse"}
//    };

