package Vue;

import Model.Data;
import Controller.ClickRecorder.ClickRecorder;
import Model.Conf;
import Model.FileBot;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class UI extends javax.swing.JFrame {

    public JFileChooser openFile;
    public String[] firstLine;
    public ClickRecorder cr;
    public Long id;
    public Data data;
    public FileBot fileBot;
    public boolean stop;

    /**
     * Creates new form ActionTable
     */
    public UI() {
        initComponents();
        setWindowToMiddle();
        setLayout(new GridLayout(3, 0));
        stop = false;
        data = new Data();
        id = (long) 0;
        cr = new ClickRecorder();
        openFile = new JFileChooser(Conf.PATHRECORD);
        firstLine = Conf.getFirstLine();
        fileBot = new FileBot(this);

        jTable1.setAutoCreateRowSorter(true); // Auto sort
        jTable1.setFillsViewportHeight(true);
        jTable2.setAutoCreateRowSorter(true); // Auto sort
        jTable2.setFillsViewportHeight(true);
        jTable3.setAutoCreateRowSorter(true); // Auto sort
        jTable3.setFillsViewportHeight(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                UI ui = new UI();
// 1. Init GUI
//                GridLayout g = new GridLayout(0, 1); // 2, 0 to get 4 pannels
//                at.setLayout(g); // StackOverflow : make the JFrame resizable on maximize.
                ui.setVisible(true);
                ui.setTitle(Conf.TITLE);
                ImageIcon img = new ImageIcon(Conf.PATHTOFILEONDISK);
                ui.setIconImage(img.getImage());
                ui.setAlwaysOnTop(true); // set always on top
// 2. Actions on start
                ui.initialiseDefault();
                ui.getFileBot().loadFile(Conf.getFirstFile(Conf.PATHRECORD), ui.getjTextPane1()); // temp autoload first file
            }
        });
    }

    /**
     * Update the GUI with the current Data
     */
    public void updateTables() {
        DefaultTableModel dtm1 = (DefaultTableModel) getjTable1().getModel();
        DefaultTableModel dtm2 = (DefaultTableModel) getjTable2().getModel();
        DefaultTableModel dtm3 = (DefaultTableModel) getjTable3().getModel();

        Conf.cleanTable(dtm1);
        Conf.cleanTable(dtm2);
        Conf.cleanTable(dtm3);

        for (Map<Integer, String> m : getData().getMainList()) {
            dtm2.addRow(Conf.generateObjectMain(m));
        }

        for (Map<Integer, String> m : getData().getMouseList()) {
            dtm1.addRow(Conf.generateObjectMouse(m));
        }

        for (Map<Integer, String> m : getData().getKeyBoardList()) {
            dtm3.addRow(Conf.generateObjectKeyBoard(m));
        }
    }

    /**
     * Execute main loop in a separated thread.
     */
    private void mainLoop() {
        final UI ui = this;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Long sleep = getData().getSleepInterval();
                for (int i = 1; i <= getData().getTotalRepititions(); i++) {

                    System.out.println("\n=================== Step " + i + "/"
                            + getData().getTotalRepititions() + " (w:" + sleep
                            + "s) ===================");
                    for (int j = (int) ((long) sleep); j >= 1; j--) {
                        getjLabel6().setText("[" + j + "s]");
                        getCr().getBot().sleepB(1000, Conf.DEFAULTSLEEPPRECISION);
                    }
                    getjLabel6().setText("");
// THE getCr().runEvents
                    getCr().runEvents(ui);
                    if (isStop()) { // after getCr().runEvents
                        setStop(false);
                        System.out.println("mainLoop: I: Programm stopped.");
                        break;
                    }
                }
                jLabel7.setText("All finished");
            }
        });
        t.start();
    }

    /**
     * Add your current mouse position as a new row.
     */
    private void addCurrentMousePosition() {

        final UI ui = this;

        Thread t = new Thread() {
            @Override
            public void run() {
                getData().addCurrentMouse(ui, getId());
                setId(getId() + 1);
                updateTables();
            }
        };
        t.start();
    }

    /**
     * Change default window location.
     */
    private void setWindowToMiddle() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        setLocation(x, y);
    }

    /**
     * Default initialisation.
     */
    private void initialiseDefault() {
        setId((Long) (long) 1);
        getData().initDefault(getId());
        setId((Long) (long) 3);
        updateTables();
    }

    /**
     * Delete all selected rows in the main table
     */
    private void deleteSelectedRows() {
        DefaultTableModel model = (DefaultTableModel) getjTable2().getModel();
        int[] rows = getjTable2().getSelectedRows();
        for (int i = 0; i <= rows.length - 1; i++) {
            int d = rows[i];
            String action = (String) model.getValueAt(d, Conf.COLMAINACTION - 1);
            long idDel = Long.parseLong((String) model.getValueAt(d, Conf.COLMAINID - 1));
            removeLine(action, idDel);
        }
        updateTables();
    }

    /**
     * Duplicate all selected rows in the main table
     */
    private void duplicateSelectedRows() {
//        DefaultTableModel model = (DefaultTableModel) getjTable2().getModel();
        int[] rows = getjTable2().getSelectedRows();

        if (rows.length == 0) {
            System.out.println("duplicateSelectedRows: W: Can't duplicate unselected line.");
        } else {
            for (int i = rows.length - 1; i >= 0; i--) { // start at the end to avoid swapping 2 times rows.
                int line = rows[i];
                String action = data.getMainList().get(line).get(Conf.COLMAINACTION),
                        idToFind = data.getMainList().get(line).get(Conf.COLMAINID);
// 1. Main List
                String oldId = data.getMainList().get(line).get(Conf.COLMAINID);
                data.getMainList().get(line).put(Conf.COLMAINID, getId().toString());
                Map<Integer, String> newH = new HashMap<>(data.getMainList().get(line));
                newH.put(Conf.COLMAINID, oldId);
                data.getMainList().add(line, newH);
                switch (action) {
                    case Conf.ACTIONMOUSE:
// 2. Mouse Events
                        line = findMouseRow(idToFind);
                        data.getMouseList().get(line).put(Conf.getColMouse("COLMOUSEID"), getId().toString());
                        Map<Integer, String> newHM = new HashMap<>(data.getMouseList().get(line));
                        newHM.put(Conf.getColMouse("COLMOUSEID"), oldId);
                        data.getMouseList().add(line, newHM);
                        break;
                    case Conf.ACTIONKEYBOARD:
// 3. Keyboard Events
                        line = findKeyboardRow(idToFind);
                        System.out.println(data.getKeyBoardList().get(line));
                        data.getKeyBoardList().get(line).put(Conf.getColKeyboard("COLKEYBOARDID"), getId().toString());
                        Map<Integer, String> newHK = new HashMap<>(data.getKeyBoardList().get(line));
                        newHK.put(Conf.getColKeyboard("COLKEYBOARDID"), oldId);
                        data.getKeyBoardList().add(line, newHK);
                        break;
                    default:
                        System.out.println("duplicateSelectedRows: E: Unknown action : " + action);
                        break;
                }
                setId((Long) (getId() + 1));
            }
            updateTables();
        }
    }

    /**
     * Find the line matching the same id in the event mouse table.
     *
     * @param idToFind
     * @return
     */
    private int findMouseRow(String idToFind) {
        int res = 0;
        boolean found = false;

        for (int i = 0; i <= data.getMouseList().size() - 1; i++) {
            String idMouse = data.getMouseList().get(i).get(Conf.getColMouse("COLMOUSEID"));
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
    private int findKeyboardRow(String idToFind) {
        int res = 0;
        boolean found = false;

        for (int i = 0; i <= data.getKeyBoardList().size() - 1; i++) {
            String idMouse = data.getKeyBoardList().get(i).get(Conf.getColKeyboard("COLKEYBOARDID"));
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
     * Duplicate all selected rows in the main table
     */
//    private void duplicateSelectedRowsOld() {
//        DefaultTableModel model = (DefaultTableModel) getjTable2().getModel();
//        int[] rows = getjTable2().getSelectedRows();
//        for (int i = 0; i <= rows.length - 1; i++) {
//            int d = rows[i];
//            String action = (String) model.getValueAt(d, Conf.COLMAINACTION - 1);
//            long idDuplicate = Long.parseLong((String) model.getValueAt(d, Conf.COLMAINID - 1));
//            duplicateLine(action, idDuplicate, getId());
//            setId((Long) (getId() + 1));
//        }
//        updateTables();
//    }
    /**
     * Remove the line with the same id in all tabs.
     *
     * @param action
     * @param idDel
     */
    private void removeLine(String action, long idDel) {
        switch (action) {
            case Conf.ACTIONMOUSE:
                getData().removeMouse(idDel);
                break;
            case Conf.ACTIONKEYBOARD:
                getData().removeKeyboard(idDel);
                break;
        }
        getData().removeMain(idDel);
    }

//    /**
//     * Duplicate the line with the same id in all tabs.
//     *
//     * @param action
//     * @param idDuplicate The duplicated id
//     * @param newId The last id
//     */
//    private void duplicateLine(String action, long idDuplicate, long newId) {
//        switch (action) {
//            case Conf.ACTIONMOUSE:
//                getData().duplicateMouse(idDuplicate, newId);
//                break;
//            case Conf.ACTIONKEYBOARD:
//                getData().duplicateKeyboard(idDuplicate, newId);
//                break;
//        }
//        getData().duplicateMain(idDuplicate, newId);
//    }
    /**
     * Update event tables. Prerequise : the switched lines have the same
     * actions. So mooving is required in 100% of cases.
     *
     * @param id
     */
    private void updateEventTables(String id, String action, int direction) {
        boolean error;
        int i;
        switch (action) {
            case Conf.ACTIONMOUSE:
                error = true;
                i = 0;
                for (Map<Integer, String> m : getData().getMouseList()) {
                    if (m.get(Conf.getColMouse("COLMOUSEID")).equals(id)) {
                        if (direction == 1) {
                            if (i >= 1) { // can't moove up first row
                                error = false;
                                Map<Integer, String> oldM = getData().getMouseList().get(i - direction),
                                        newM = getData().getMouseList().get(i);
                                getData().getMouseList().set(i - direction, newM); // update main table
                                getData().getMouseList().set(i, oldM); // update main table
                                break;
                            }
                        } else if (direction == -1) {
                            if (i <= getData().getMouseList().size() - 2) { // can't moove up first row
                                error = false;
                                Map<Integer, String> oldM = getData().getMouseList().get(i - direction),
                                        newM = getData().getMouseList().get(i);
                                getData().getMouseList().set(i - direction, newM); // update main table
                                getData().getMouseList().set(i, oldM); // update main table
                                break;
                            }
                        } else {
                            System.out.println("updateEventTables: E: Unknown direction : " + direction);
                        }
                    }
                    i++;
                }
                if (error) {
                    System.out.println("getLineByIdLeftMouseClick: E: Line with id = " + id + " not found in the table LeftMouseClick.");
                }
                break;
            case Conf.ACTIONKEYBOARD:
                error = true;
                i = 0;
                for (Map<Integer, String> m : getData().getKeyBoardList()) {
                    if (m.get(Conf.getColKeyboard("COLKEYBOARDID")).equals(id)) {
                        if (direction == 1) {
                            if (i >= 1) { // can't moove up first row
                                error = false;
                                Map<Integer, String> oldM = getData().getKeyBoardList().get(i - direction),
                                        newM = getData().getKeyBoardList().get(i);
                                getData().getKeyBoardList().set(i - 1, newM); // update main table
                                getData().getKeyBoardList().set(i, oldM); // update main table
                                break;
                            }
                        } else if (direction == -1) {
                            if (i <= getData().getKeyBoardList().size() - 2) { // can't moove up last row
                                error = false;
                                Map<Integer, String> oldM = getData().getKeyBoardList().get(i - direction),
                                        newM = getData().getKeyBoardList().get(i);
                                getData().getKeyBoardList().set(i - direction, newM); // update main table
                                getData().getKeyBoardList().set(i, oldM); // update main table
                                break;
                            }
                        } else {
                            System.out.println("updateEventTables: E: Unknown direction : " + direction);
                        }
                    }
                    i++;
                }
                if (error) {
                    System.out.println("getLineByIdLeftMouseClick: E: Line with id = " + id + " not found in the table LeftMouseClick.");
                }
                break;
            default:
                System.out.println("updateEventTables: E: Unrecognized action : " + action);
                break;
        }

    }

    /**
     * Moove rows up or down.
     *
     * @param i
     * @param direction
     */
    private void moveIn(int i, int direction) {
        if (direction == 1) {
            Map<Integer, String> oldM = getData().getMainList().get(i - direction),
                    newM = getData().getMainList().get(i);
            if (newM.get(Conf.COLMAINACTION).equals(oldM.get(Conf.COLMAINACTION))) {
                updateEventTables(newM.get(Conf.COLMAINID), newM.get(Conf.COLMAINACTION), direction);
            }
            getData().getMainList().set(i - direction, newM); // update main table
            getData().getMainList().set(i, oldM); // update main table
        } else if (direction == -1) {
            Map<Integer, String> oldM = getData().getMainList().get(i - direction),
                    newM = getData().getMainList().get(i);
            if (newM.get(Conf.COLMAINACTION).equals(oldM.get(Conf.COLMAINACTION))) {
                updateEventTables(newM.get(Conf.COLMAINID), newM.get(Conf.COLMAINACTION), direction);
            }
            getData().getMainList().set(i, oldM); // update main table
            getData().getMainList().set(i - direction, newM); // update main table

        } else {
            System.out.println("moveIn: E: Unknown direction : " + direction);
        }

    }

    /**
     * Move selected rows up (1) or down (-1).
     *
     * @param direction 1 to move up and -1 to move down.
     */
    private void moveRows(int direction) {
// 1. Switch rows
        int[] selectedRows = getjTable2().getSelectedRows();
        for (int i : selectedRows) { // bug here when trying to moove down many events. Solve it by reversing loop for ?
            if (direction == 1) {
                if (i >= 1) { // can't moove up first row
                    moveIn(i, direction);
                }
            } else if (direction == -1) {
                if (i <= getData().getMainList().size() - 2) { // can't moove up last row
                    moveIn(i, direction);
                }
            } else {
                System.out.println("moveRows: E: Unknown direction : " + direction);
            }
        }
        updateTables();
// 2. Update Selected rows
        for (int i : selectedRows) {
            if (direction == 1) {
                if (i >= 1) { // can't select first row
                    getjTable2().addRowSelectionInterval(i - direction, i - direction);
                }
            } else if (direction == -1) {
                if (i <= getData().getMainList().size() - 2) { // can't select first row
                    getjTable2().addRowSelectionInterval(i - direction, i - direction);
                }
            } else {
                System.out.println("moveRows: E: Unknown direction : " + direction);
            }

        }
    }

    /**
     * Force exit.
     */
    private void forceExit() {
        System.out.println("forceExit: I: Forcing exit.");
        System.exit(0);
    }

    // ---------------------------------------------
    // EVENTS
    // ---------------------------------------------
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton14 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Comment", "Action", "id"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Long.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                jTable2ComponentRemoved(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Comment", "Action", "Px", "Py", "Sleep1 (Time after mouse position is set)", "Sleep2 (Time where left click is pressed)", "Sleep3 (Time after left click is released)", "PrecisionSleep", "PrecisionMouse", "id"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Long.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, true, true, true, true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                jTable1ComponentRemoved(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Mouse", jPanel1);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Comment", "Keyboard Key", "Sleep", "id"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Long.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                jTable3ComponentRemoved(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Keyboard", jPanel2);

        jLabel4.setText("Bot Informations :");

        jScrollPane6.setViewportView(jTextPane1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(51, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(59, Short.MAX_VALUE))
        );

        jScrollPane5.setViewportView(jPanel3);

        jTabbedPane1.addTab("Bot Informations", jScrollPane5);

        jButton2.setText("Add");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setText("Delete");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton3.setText("Reset");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton8.setText("Duplicate");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel1.setText("1) Main Table");

        jLabel2.setText("2) File");

        jButton5.setText("Open File");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Save to File");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Open First File");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton1.setText("Run");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton9.setText("Up");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("Down");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setText("Force Exit");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setText("Record Mouse");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jLabel3.setText("3) Programm");

        jCheckBox1.setText("Hide when running");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jButton14.setText("Stop");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jButton9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jButton7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton6))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckBox1))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)))
                .addContainerGap(168, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton9)
                    .addComponent(jButton10)
                    .addComponent(jButton4)
                    .addComponent(jButton8)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton12)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jButton7)
                    .addComponent(jButton6)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox1)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(jLabel3)
                        .addComponent(jLabel6)
                        .addComponent(jButton11)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton14)
                    .addComponent(jLabel7))
                .addContainerGap(65, Short.MAX_VALUE))
        );

        jScrollPane4.setViewportView(jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(87, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        mainLoop();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        getData().addDefault(getId());
        setId(getId() + 2);
        updateTables();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        setId((Long) (long) 0);
        initialiseDefault();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        deleteSelectedRows();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        getOpenFile().showOpenDialog(null);
        File file = getOpenFile().getSelectedFile();
        getFileBot().loadFile(file, getjTextPane1());
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        getFileBot().loadFile(Conf.getFirstFile(Conf.PATHRECORD), getjTextPane1());
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        duplicateSelectedRows();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jTable2ComponentRemoved(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jTable2ComponentRemoved
        DefaultTableModel dtm2 = (DefaultTableModel) getjTable2().getModel(); // a value is changed in the table

        int[] rows = getjTable2().getSelectedRows();
        for (int d : rows) {
            getData().updateMainListFromTable(dtm2, d);
        }
        updateTables();
    }//GEN-LAST:event_jTable2ComponentRemoved

    private void jTable1ComponentRemoved(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jTable1ComponentRemoved
        DefaultTableModel dtm1 = (DefaultTableModel) getjTable1().getModel(); // a value is changed in the table

        int[] rows = getjTable1().getSelectedRows();
        for (int d : rows) {
            getData().updateMouseListFromTable(dtm1, d);
        }
        updateTables();
    }//GEN-LAST:event_jTable1ComponentRemoved

    private void jTable3ComponentRemoved(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jTable3ComponentRemoved
        DefaultTableModel dtm3 = (DefaultTableModel) getjTable3().getModel(); // a value is changed in the table

        int[] rows = getjTable3().getSelectedRows();
        for (int d : rows) {
            getData().updategetKeyBoardListFromTable(dtm3, d);
        }
        updateTables();
    }//GEN-LAST:event_jTable3ComponentRemoved

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        moveRows(1);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        moveRows(-1);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        getOpenFile().showSaveDialog(null);
        try {
            File f = getOpenFile().getSelectedFile();
            if (f != null) {
                String fileName = f.getAbsolutePath();
//                if (!fileName.contains(Conf.FILEEXTENSION)) {
//                    fileName += Conf.FILEEXTENSION;
//                }
                FileWriter fw = new FileWriter(fileName); // create file if not exists
                File file = getOpenFile().getSelectedFile();
                getFileBot().saveFile(file);
            }
        } catch (IOException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        forceExit();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        addCurrentMousePosition();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        setAlwaysOnTop(!jCheckBox1.isSelected());
        System.out.println("OnTop: " + !jCheckBox1.isSelected());
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        setStop(true);
    }//GEN-LAST:event_jButton14ActionPerformed

    // ---------------------------------------------
    // GETTER AND SETTERS
    // ---------------------------------------------

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JButton getjButton7() {
        return jButton7;
    }

    public void setjButton7(JButton jButton7) {
        this.jButton7 = jButton7;
    }

    public JScrollPane getjScrollPane3() {
        return jScrollPane3;
    }

    public void setjScrollPane3(JScrollPane jScrollPane3) {
        this.jScrollPane3 = jScrollPane3;
    }

    public JTable getjTable3() {
        return jTable3;
    }

    public void setjTable3(JTable jTable3) {
        this.jTable3 = jTable3;
    }

    public JFileChooser getOpenFile() {
        return openFile;
    }

    public void setOpenFile(JFileChooser openFile) {
        this.openFile = openFile;
    }

    public JButton getjButton1() {
        return jButton1;
    }

    public void setjButton1(JButton jButton1) {
        this.jButton1 = jButton1;
    }

    public JButton getjButton2() {
        return jButton2;
    }

    public void setjButton2(JButton jButton2) {
        this.jButton2 = jButton2;
    }

    public JButton getjButton3() {
        return jButton3;
    }

    public void setjButton3(JButton jButton3) {
        this.jButton3 = jButton3;
    }

    public JButton getjButton4() {
        return jButton4;
    }

    public void setjButton4(JButton jButton4) {
        this.jButton4 = jButton4;
    }

    public JButton getjButton5() {
        return jButton5;
    }

    public void setjButton5(JButton jButton5) {
        this.jButton5 = jButton5;
    }

    public JButton getjButton6() {
        return jButton6;
    }

    public void setjButton6(JButton jButton6) {
        this.jButton6 = jButton6;
    }

    public JPanel getjPanel5() {
        return jPanel5;
    }

    public void setjPanel5(JPanel jPanel5) {
        this.jPanel5 = jPanel5;
    }

    public JScrollPane getjScrollPane2() {
        return jScrollPane2;
    }

    public void setjScrollPane2(JScrollPane jScrollPane2) {
        this.jScrollPane2 = jScrollPane2;
    }

    public JTable getjTable2() {
        return jTable2;
    }

    public void setjTable2(JTable jTable2) {
        this.jTable2 = jTable2;
    }

    /**
     * @return the cr
     */
    public ClickRecorder getCr() {
        return cr;
    }

    /**
     * @param cr the cr to set
     */
    public void setCr(ClickRecorder cr) {
        this.cr = cr;
    }

    /**
     * @return the jPanel1
     */
    public javax.swing.JPanel getjPanel1() {
        return jPanel1;
    }

    /**
     * @param jPanel1 the jPanel1 to set
     */
    public void setjPanel1(javax.swing.JPanel jPanel1) {
        this.jPanel1 = jPanel1;
    }

    /**
     * @return the jPanel2
     */
    public javax.swing.JPanel getjPanel2() {
        return jPanel2;
    }

    /**
     * @param jPanel2 the jPanel2 to set
     */
    public void setjPanel2(javax.swing.JPanel jPanel2) {
        this.jPanel2 = jPanel2;
    }

    /**
     * @return the jScrollPane1
     */
    public javax.swing.JScrollPane getjScrollPane1() {
        return jScrollPane1;
    }

    /**
     * @param jScrollPane1 the jScrollPane1 to set
     */
    public void setjScrollPane1(javax.swing.JScrollPane jScrollPane1) {
        this.jScrollPane1 = jScrollPane1;
    }

    /**
     * @return the jTabbedPane1
     */
    public javax.swing.JTabbedPane getjTabbedPane1() {
        return jTabbedPane1;
    }

    /**
     * @param jTabbedPane1 the jTabbedPane1 to set
     */
    public void setjTabbedPane1(javax.swing.JTabbedPane jTabbedPane1) {
        this.jTabbedPane1 = jTabbedPane1;
    }

    /**
     * @return the jTable1
     */
    public javax.swing.JTable getjTable1() {
        return jTable1;
    }

    /**
     * @param jTable1 the jTable1 to set
     */
    public void setjTable1(javax.swing.JTable jTable1) {
        this.jTable1 = jTable1;
    }

    /**
     * @return the firstLine
     */
    public String[] getFirstLine() {
        return firstLine;
    }

    /**
     * @return the data
     */
    public Data getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Data data) {
        this.data = data;
    }

    /**
     * @return the fileBot
     */
    public FileBot getFileBot() {
        return fileBot;
    }

    /**
     * @param fileBot the fileBot to set
     */
    public void setFileBot(FileBot fileBot) {
        this.fileBot = fileBot;
    }

    /**
     * @return the jButton10
     */
    public javax.swing.JButton getjButton10() {
        return jButton10;
    }

    /**
     * @param jButton10 the jButton10 to set
     */
    public void setjButton10(javax.swing.JButton jButton10) {
        this.jButton10 = jButton10;
    }

    /**
     * @return the jButton11
     */
    public javax.swing.JButton getjButton11() {
        return jButton11;
    }

    /**
     * @param jButton11 the jButton11 to set
     */
    public void setjButton11(javax.swing.JButton jButton11) {
        this.jButton11 = jButton11;
    }

    /**
     * @return the jButton12
     */
    public javax.swing.JButton getjButton12() {
        return jButton12;
    }

    /**
     * @param jButton12 the jButton12 to set
     */
    public void setjButton12(javax.swing.JButton jButton12) {
        this.jButton12 = jButton12;
    }

    /**
     * @return the jButton8
     */
    public javax.swing.JButton getjButton8() {
        return jButton8;
    }

    /**
     * @param jButton8 the jButton8 to set
     */
    public void setjButton8(javax.swing.JButton jButton8) {
        this.jButton8 = jButton8;
    }

    /**
     * @return the jButton9
     */
    public javax.swing.JButton getjButton9() {
        return jButton9;
    }

    /**
     * @param jButton9 the jButton9 to set
     */
    public void setjButton9(javax.swing.JButton jButton9) {
        this.jButton9 = jButton9;
    }

    /**
     * @return the jCheckBox1
     */
    public javax.swing.JCheckBox getjCheckBox1() {
        return jCheckBox1;
    }

    /**
     * @param jCheckBox1 the jCheckBox1 to set
     */
    public void setjCheckBox1(javax.swing.JCheckBox jCheckBox1) {
        this.jCheckBox1 = jCheckBox1;
    }

    /**
     * @return the jLabel1
     */
    public javax.swing.JLabel getjLabel1() {
        return jLabel1;
    }

    /**
     * @param jLabel1 the jLabel1 to set
     */
    public void setjLabel1(javax.swing.JLabel jLabel1) {
        this.jLabel1 = jLabel1;
    }

    /**
     * @return the jLabel2
     */
    public javax.swing.JLabel getjLabel2() {
        return jLabel2;
    }

    /**
     * @param jLabel2 the jLabel2 to set
     */
    public void setjLabel2(javax.swing.JLabel jLabel2) {
        this.jLabel2 = jLabel2;
    }

    /**
     * @return the jLabel3
     */
    public javax.swing.JLabel getjLabel3() {
        return jLabel3;
    }

    /**
     * @param jLabel3 the jLabel3 to set
     */
    public void setjLabel3(javax.swing.JLabel jLabel3) {
        this.jLabel3 = jLabel3;
    }

    /**
     * @return the jLabel4
     */
    public javax.swing.JLabel getjLabel4() {
        return jLabel4;
    }

    /**
     * @param jLabel4 the jLabel4 to set
     */
    public void setjLabel4(javax.swing.JLabel jLabel4) {
        this.jLabel4 = jLabel4;
    }

    /**
     * @return the jLabel5
     */
    public javax.swing.JLabel getjLabel5() {
        return jLabel5;
    }

    /**
     * @param jLabel5 the jLabel5 to set
     */
    public void setjLabel5(javax.swing.JLabel jLabel5) {
        this.jLabel5 = jLabel5;
    }

    /**
     * @return the jPanel3
     */
    public javax.swing.JPanel getjPanel3() {
        return jPanel3;
    }

    /**
     * @param jPanel3 the jPanel3 to set
     */
    public void setjPanel3(javax.swing.JPanel jPanel3) {
        this.jPanel3 = jPanel3;
    }

    /**
     * @return the jScrollPane4
     */
    public javax.swing.JScrollPane getjScrollPane4() {
        return jScrollPane4;
    }

    /**
     * @param jScrollPane4 the jScrollPane4 to set
     */
    public void setjScrollPane4(javax.swing.JScrollPane jScrollPane4) {
        this.jScrollPane4 = jScrollPane4;
    }

    /**
     * @return the jScrollPane5
     */
    public javax.swing.JScrollPane getjScrollPane5() {
        return jScrollPane5;
    }

    /**
     * @param jScrollPane5 the jScrollPane5 to set
     */
    public void setjScrollPane5(javax.swing.JScrollPane jScrollPane5) {
        this.jScrollPane5 = jScrollPane5;
    }

    /**
     * @return the jScrollPane6
     */
    public javax.swing.JScrollPane getjScrollPane6() {
        return jScrollPane6;
    }

    /**
     * @param jScrollPane6 the jScrollPane6 to set
     */
    public void setjScrollPane6(javax.swing.JScrollPane jScrollPane6) {
        this.jScrollPane6 = jScrollPane6;
    }

    /**
     * @return the jTextPane1
     */
    public javax.swing.JTextPane getjTextPane1() {
        return jTextPane1;
    }

    /**
     * @param jTextPane1 the jTextPane1 to set
     */
    public void setjTextPane1(javax.swing.JTextPane jTextPane1) {
        this.jTextPane1 = jTextPane1;
    }

    /**
     * @return the jLabel6
     */
    public javax.swing.JLabel getjLabel6() {
        return jLabel6;
    }

    /**
     * @param jLabel6 the jLabel6 to set
     */
    public void setjLabel6(javax.swing.JLabel jLabel6) {
        this.jLabel6 = jLabel6;
    }

    /**
     * @param f the firstLine to set
     */
    public void setFirstLine(String[] f) {
        firstLine = f;
    }

    /**
     * @return the stop
     */
    public boolean isStop() {
        return stop;
    }

    /**
     * @param stop the stop to set
     */
    public void setStop(boolean stop) {
        this.stop = stop;
    }

    /**
     * @return the jButton14
     */
    public javax.swing.JButton getjButton14() {
        return jButton14;
    }

    /**
     * @param jButton14 the jButton14 to set
     */
    public void setjButton14(javax.swing.JButton jButton14) {
        this.jButton14 = jButton14;
    }

    /**
     * @return the jLabel7
     */
    public javax.swing.JLabel getjLabel7() {
        return jLabel7;
    }

    /**
     * @param jLabel7 the jLabel7 to set
     */
    public void setjLabel7(javax.swing.JLabel jLabel7) {
        this.jLabel7 = jLabel7;
    }

}
