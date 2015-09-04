package Model;

import static Model.Conf.colKeyboard;
import static Model.Conf.colKeyboardDefault;
import Vue.UI;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;

public class FileBot {

//    private Data data;
//    private String[] firstLine;
//    private Long id;
    private UI ui;

    public FileBot(UI u) {
        ui = u;
    }

    /**
     * Load a .bot file.
     *
     * @param f
     * @param jtp
     */
    public void loadFile(File f, JTextPane jtp) {
        if (f == null) {
            System.out.println("updateUI: W: File is not set.");
        } else {
            ui.setData(new Data());
            try {
                try (CSVReader reader = new CSVReader(new FileReader(f))) {
                    String[] nextLine;
// 1. Load settings
                    if ((nextLine = reader.readNext()) != null) {
                        String description = nextLine[3];
//                        System.out.println("updateUI: I: " + description);
                        jtp.setText(description);
                        System.out.println("updateUI: I: Repetition: " + nextLine[1] + ", Sleep interval: " + nextLine[2] + "s");
                        ui.getData().setTotalRepititions(Long.parseLong(nextLine[1]));
                        ui.getData().setSleepInterval(Long.parseLong(nextLine[2]));
                    }
                    ui.setFirstLine(nextLine);
                    ui.setId(Conf.FIRSTID);
// 2. Load Data
                    while ((nextLine = reader.readNext()) != null) {
                        boolean error = false;
                        String action = (String) nextLine[1];
                        switch (action) {
                            case Conf.ACTIONMOUSE:
                                ui.getData().addMouseAction(new Object[]{
                                    "", // because addMainAction method extract from id=1
                                    nextLine[0],
                                    nextLine[2],// 1 is reserved for action switch before
                                    nextLine[3],
                                    nextLine[4],
                                    nextLine[5],
                                    nextLine[6],
                                    nextLine[7],
                                    nextLine[8],
                                    nextLine[9],
                                    Long.toString(ui.getId())
                                });
                                break;
                            case Conf.ACTIONKEYBOARD:
                                ui.getData().addKeyboardAction(new Object[]{
                                    "", // because addMainAction method extract from id=1
                                    nextLine[0],
                                    nextLine[2],
                                    nextLine[3],
                                    Long.toString(ui.getId())
                                });
                                break;
                            default:
                                error = true;
                                System.out.println("loadFile: E: '" + f.getName()
                                        + "' line " + (ui.getId() + 1)
                                        + ", skipping unrecognized action '"
                                        + action + "'.");
                                break;
                        }
                        if (!error) {
                            ui.getData().addMainAction(new Object[]{
                                "", // because addMainAction method extract from id=1
                                nextLine[0],
                                action,
                                Long.toString(ui.getId())
                            });
                        }
                        ui.setId((Long) (ui.getId() + 1));
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
            }
            ui.updateTables();
        }
    }

    /**
     * Save a .bot file.
     *
     * @param f
     */
    public void saveFile(File f) {
        try {
            try (CSVWriter writer = new CSVWriter(new FileWriter(f))) {
// 1. Write first line : default string[] in conf or first setting line as loaded in the last file.
                writer.writeNext(ui.getFirstLine());

// 2. For each element in the mainList, crfeate a new row which values are recovered from event tables.
                for (Map<Integer, String> mMain : ui.getData().getMainList()) {
                    String comment = mMain.get(Conf.COLMAINCOMMENT),
                            action = mMain.get(Conf.COLMAINACTION),
                            idMain = mMain.get(Conf.COLMAINID);
                    switch (action) {
                        case Conf.ACTIONMOUSE:
                            for (Map<Integer, String> mLeftMouse : ui.getData().getMouseList()) {
                                String idLeftMouse = mLeftMouse.get(Conf.getColMouse("COLMOUSEID"));
                                if (idLeftMouse.equals(idMain)) {

                                    String[] line = new String[Conf.colMouse.length];

                                    for (int col = 1; col <= Conf.colMouse.length; col++) {
                                        if (col == 1) {
                                            line[col - 1] = comment;
                                        } else if (col == 2) {
                                            line[col - 1] = action;
                                        } else {
//                                            line[col - 1] = (String) Conf.colMouseDefault[col - 1][0]; // default values
                                            line[col - 1] = mLeftMouse.get(col - 1);
                                        }
                                    }

//                                    String px = mLeftMouse.get(Conf.getColMouse("COLMOUSEPX")),
//                                            py = mLeftMouse.get(Conf.COLMOUSEPY),
//                                            sleep1 = mLeftMouse.get(Conf.COLMOUSESLEEP1),
//                                            sleep2 = mLeftMouse.get(Conf.COLMOUSESLEEP2),
//                                            sleep3 = mLeftMouse.get(Conf.COLMOUSESLEEP3),
//                                            precisionSleep = mLeftMouse.get(Conf.COLMOUSEPRECISIONSLEEP),
//                                            precisionMouse = mLeftMouse.get(Conf.COLMOUSEPRECISIONMOUSE);
//                                    String[] line = {
//                                        comment,
//                                        action,
//                                        px,
//                                        py,
//                                        sleep1,
//                                        sleep2,
//                                        sleep3,
//                                        precisionSleep,
//                                        precisionMouse
//                                    };
                                    writer.writeNext(line);
                                    break;
                                }
                            }
                            break;
                        case Conf.ACTIONKEYBOARD:
                            for (Map<Integer, String> mLeftMouse : ui.getData().getKeyBoardList()) {
                                String idKeyboard = mLeftMouse.get(Conf.getColKeyboard("COLKEYBOARDID"));
                                if (idKeyboard.equals(idMain)) {
                                    String keyboardKey = mLeftMouse.get(Conf.getColKeyboard("COLKEYBOARDKEY")),
                                            sleep = mLeftMouse.get(Conf.getColKeyboard("COLKEYBOARDSLEEP"));
                                    String[] line = {
                                        comment,
                                        action,
                                        keyboardKey,
                                        sleep
                                    };
                                    writer.writeNext(line);
                                    break;
                                }
                            }
                            break;
                        default:
                            System.out.println("saveFile: E: Unknown action : " + action);
                            break;
                    }

                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
