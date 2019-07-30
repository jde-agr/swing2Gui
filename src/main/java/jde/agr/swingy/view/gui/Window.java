package jde.agr.swingy.view.gui;

import jde.agr.swingy.Utility.EType;
import jde.agr.swingy.Utility.Logger;
import jde.agr.swingy.Utility.Print;
import jde.agr.swingy.Utility.Utility;
import jde.agr.swingy.controller.CharacterFactory;
import jde.agr.swingy.controller.GameManager;
import jde.agr.swingy.controller.MapFactory;
import jde.agr.swingy.database.DbHandler;
import jde.agr.swingy.model.characters.Character;
import jde.agr.swingy.model.characters.Hero;
import jde.agr.swingy.view.cli.CliView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static jde.agr.swingy.Utility.Global.*;

class Window extends JFrame {

    private JComboBox<String> comboListCreate = new JComboBox();
    private JComboBox<String> comboListSelect = new JComboBox();
    private JRadioButton jrFight = new JRadioButton("Fight");
    private JRadioButton jrRun = new JRadioButton("Run");
    private JRadioButton jrYes = new JRadioButton("Yes");
    private JRadioButton jrNo = new JRadioButton("No");
    private JLabel labelCreate = new JLabel("Create your hero");
    private JLabel labelSelect = new JLabel("Select the matching id");
    private JLabel labelAction = new JLabel("Action");
    private JLabel labelEquip = new JLabel("Equip");
    private JLabel labelLog = new JLabel("Log");
    private JLabel labelStats = new JLabel("Stats");
    private JLabel picLabel;
    private JLabel labelInput = new JLabel("Name");
    private JTextField inputTextField = new JTextField();

    private JPanel panelContainer = new JPanel();
    private JPanel panelMenu = new JPanel();
    private JPanel panelMap = new JPanel();
    private JPanel panelLog = new JPanel();
    private JPanel panelCreateHero = new JPanel();
    private JPanel panelSelectHero = new JPanel();
    private JPanel panelEncounter = new JPanel();
    private JPanel panelLoot = new JPanel();
    private JPanel panelJrEncounter = new JPanel();
    private JPanel panelJrLoot = new JPanel();
    private JPanel panelGrid = new JPanel();
    private JPanel panelStats = new JPanel();
    private JPanel panelInput = new JPanel();

    private JButton buttonCreate = new JButton("Create a new hero");
    private JButton buttonSelect = new JButton("Select a previously created hero");
    private JButton buttonSwitch = new JButton("Switch to CLI");
    private JButton buttonValidateCreation = new JButton("Create");
    private JButton buttonValidateEncounter = new JButton("Ok");
    private JButton buttonValidateLoot = new JButton("Ok");
    private JButton buttonDeleteHero = new JButton("Delete");
    private JButton buttonRunGame = new JButton("Run !");
    private JButton buttonCancelMain = new JButton("Cancel");
    private JButton buttonCancel = new JButton("Cancel");
    private ButtonGroup buttonGroupEncounter = new ButtonGroup();
    private ButtonGroup buttonGroupLoot = new ButtonGroup();
    private JScrollPane scrollPane;

    private BufferedImage unknownImage;
    private BufferedImage warriorImage;
    private BufferedImage thiefImage;
    private BufferedImage wizardImage;
    private BufferedImage fightImage;
    private Image scaled;

    private GridLayout gridLayout = new GridLayout();

    private static final String ASSETS_DIR = "src/main/java/jde/agr/swingy/assets/";

    Window() {
        setTitle("Swingy");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        panelContainer.setLayout(new BorderLayout());
        initComponents(this);
        Logger.print("Welcome to \"42 RPG\"");
        startScreen(this);
        this.setVisible(true);
    }

    private void startScreen(Window win) {
        panelMenu.add(buttonCreate);
        panelMenu.add(buttonSelect);
        panelMenu.add(buttonSwitch);
        panelMenu.add(panelStats);
        panelMenu.add(panelCreateHero);
        panelMenu.add(panelSelectHero);
        panelMenu.add(panelEncounter);
        panelMenu.add(panelLoot);
        panelLog.add(labelLog);
        panelLog.add(scrollPane);
        panelMap.add(picLabel);
        panelMap.add(panelGrid);
        panelMenu.add(buttonCancelMain);

        panelContainer.add(panelMenu, BorderLayout.WEST);
        panelContainer.add(panelMap, BorderLayout.CENTER);
        panelContainer.add(panelLog, BorderLayout.EAST);

        win.setContentPane(panelContainer);
    }

    private void initComponents(Window win) {
        logTextArea = new JTextArea("", 40, 25);
        scrollPane = new JScrollPane(logTextArea);
        logTextArea.setEditable(false);

        ((FlowLayout)panelMap.getLayout()).setVgap(0);
        BufferedImage bgImage = Utility.loadImage(ASSETS_DIR + "img.png");
        unknownImage = Utility.loadImage(ASSETS_DIR + "unknown.png");
        warriorImage = Utility.loadImage(ASSETS_DIR + "warrior.png");
        thiefImage = Utility.loadImage(ASSETS_DIR + "thief.png");
        wizardImage = Utility.loadImage(ASSETS_DIR + "wizard.png");
        fightImage = Utility.loadImage(ASSETS_DIR + "fight.png");
        scaled = bgImage.getScaledInstance(win.getWidth() / 2, win.getHeight(), Image.SCALE_DEFAULT);
        picLabel = new JLabel(new ImageIcon(scaled));

        panelMenu.setPreferredSize(new Dimension(win.getWidth() / 4, win.getHeight()));
        panelMenu.setBackground(Color.LIGHT_GRAY);
        panelMap.setPreferredSize(new Dimension(win.getWidth() / 2, win.getHeight()));
        panelMap.setBackground(Color.LIGHT_GRAY);
        panelLog.setPreferredSize(new Dimension(win.getWidth() / 4, win.getHeight()));
        panelLog.setBackground(Color.LIGHT_GRAY);
        panelGrid.setPreferredSize(new Dimension(win.getWidth() / 2, win.getHeight()));
        panelGrid.setBackground(Color.LIGHT_GRAY);

        comboListCreate.addItem("Warrior");
        comboListCreate.addItem("Thief");
        comboListCreate.addItem("Wizard");
        comboListCreate.addActionListener(new ComboCreateAction());
        comboListCreate.setPreferredSize(new Dimension(200, 50));
        comboListSelect.setPreferredSize(new Dimension(200, 50));

        buttonCreate.setPreferredSize(new Dimension(300, 70));
        buttonCreate.addActionListener(new ButtonCreateListener());
        buttonSelect.setPreferredSize(new Dimension(300, 70));
        buttonSelect.addActionListener(new ButtonSelectListener());
        buttonSwitch.setPreferredSize(new Dimension(300, 70));
        buttonSwitch.addActionListener(new ButtonHeroSwitchListener());
        buttonValidateCreation.setPreferredSize(new Dimension(200, 40));
        buttonValidateCreation.addActionListener(new ButtonHeroCreateListener());
        buttonValidateCreation.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonValidateCreation.setMaximumSize(getSize());
        buttonValidateEncounter.setPreferredSize(new Dimension(200, 40));
        buttonValidateEncounter.addActionListener(new ButtonHeroEncounterListener());
        buttonValidateEncounter.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonValidateEncounter.setMaximumSize(getSize());
        buttonValidateLoot.setPreferredSize(new Dimension(200, 40));
        buttonValidateLoot.addActionListener(new ButtonHeroLootListener());
        buttonValidateLoot.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonValidateLoot.setMaximumSize(getSize());
        buttonRunGame.setPreferredSize(new Dimension(200, 40));
        buttonRunGame.addActionListener(new ButtonRunListener());
        buttonRunGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonRunGame.setMaximumSize(getSize());
        buttonDeleteHero.setPreferredSize(new Dimension(200, 40));
        buttonDeleteHero.addActionListener(new ButtonDeleteHeroListener());
        buttonDeleteHero.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonDeleteHero.setMaximumSize(getSize());
        buttonDeleteHero.setForeground(Color.RED);
        buttonCancel.setPreferredSize(new Dimension(200, 40));
        buttonCancel.addActionListener(new ButtonCancelListener());
        buttonCancel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonCancel.setMaximumSize(getSize());
        buttonCancelMain.setPreferredSize(new Dimension(200, 40));
        buttonCancelMain.addActionListener(new ButtonCancelMainListener());
        buttonCancelMain.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonCancelMain.setMaximumSize(getSize());
        labelCreate.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelSelect.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelAction.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputTextField.setPreferredSize(new Dimension(150, 40));

        Box box = Box.createVerticalBox();
        panelStats.setPreferredSize(new Dimension(300, 175));
        labelStats.setAlignmentY(Component.CENTER_ALIGNMENT);
        box.add(labelStats);
        panelStats.add(box);
        panelStats.setVisible(false);
        panelInput.add(labelInput);
        panelInput.add(inputTextField);

        panelCreateHero.setPreferredSize(new Dimension(300, 200));
        panelCreateHero.setLayout(new BoxLayout(panelCreateHero, BoxLayout.Y_AXIS));
        panelCreateHero.add(labelCreate);
        panelCreateHero.add(comboListCreate);
        panelCreateHero.add(panelInput);
        panelCreateHero.add(buttonValidateCreation);
        panelCreateHero.setVisible(false);
        comboListCreate.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelSelectHero.setPreferredSize(new Dimension(300, 200));
        panelSelectHero.setLayout(new BoxLayout(panelSelectHero, BoxLayout.Y_AXIS));
        panelSelectHero.add(labelSelect);
        panelSelectHero.add(comboListSelect);
        panelSelectHero.add(buttonRunGame);
        panelSelectHero.add(buttonDeleteHero);
        panelSelectHero.setVisible(false);
        comboListSelect.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelEncounter.setPreferredSize(new Dimension(300, 100));
        panelEncounter.setLayout(new BoxLayout(panelEncounter, BoxLayout.Y_AXIS));
        panelJrEncounter.setLayout(new BoxLayout(panelJrEncounter, BoxLayout.X_AXIS));
        buttonGroupEncounter.add(jrFight);
        buttonGroupEncounter.add(jrRun);
        jrFight.setSelected(true);
        panelJrEncounter.add(jrFight);
        panelJrEncounter.add(new Box.Filler(new Dimension(50,0),
                                    new Dimension(50,0),
                                    new Dimension(50,0)));
        panelJrEncounter.add(jrRun);
        panelEncounter.add(labelAction);
        panelEncounter.add(new Box.Filler(new Dimension(0,10),
                new Dimension(0,10),
                new Dimension(0,10)));
        panelEncounter.add(panelJrEncounter);
        panelEncounter.add(new Box.Filler(new Dimension(0,10),
                new Dimension(0,10),
                new Dimension(0,10)));
        panelEncounter.add(buttonValidateEncounter);

        panelLoot.setPreferredSize(new Dimension(300, 100));
        panelLoot.setLayout(new BoxLayout(panelLoot, BoxLayout.Y_AXIS));
        panelJrLoot.setLayout(new BoxLayout(panelJrLoot, BoxLayout.X_AXIS));
        buttonGroupLoot.add(jrYes);
        buttonGroupLoot.add(jrNo);
        jrYes.setSelected(true);
        panelJrLoot.add(jrYes);
        panelJrLoot.add(new Box.Filler(new Dimension(50,0),
                new Dimension(50,0),
                new Dimension(50,0)));
        panelJrLoot.add(jrNo);
        panelLoot.add(labelEquip);
        panelLoot.add(new Box.Filler(new Dimension(0,10),
                new Dimension(0,10),
                new Dimension(0,10)));
        panelLoot.add(panelJrLoot);
        panelLoot.add(new Box.Filler(new Dimension(0,10),
                new Dimension(0,10),
                new Dimension(0,10)));
        panelLoot.add(buttonValidateLoot);
        panelEncounter.setVisible(false);
        panelLoot.setVisible(false);
        panelGrid.setLayout(gridLayout);
        panelGrid.setVisible(false);

        buttonCancelMain.setVisible(false);
    }

    private void mapHandler() {
        for (int i = 0; i < squareMap.getMapSize(); i++) {
            for (int j = 0; j < squareMap.getMapSize(); j++) {
                final int x = i;
                final int y = j;
                int value = squareMap.getMap()[i][j];
                final JPanel cell = new JPanel();
                ((FlowLayout)cell.getLayout()).setVgap(0);
                ((FlowLayout)cell.getLayout()).setHgap(0);
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                switch (value) {
                    case 1:
                        cell.setBackground(new Color(206, 234, 133));
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                switch (hero.getType()) {
                                    case "Warrior":
                                        scaled = warriorImage.getScaledInstance(cell.getWidth(), cell.getHeight(), Image.SCALE_DEFAULT);
                                        break;
                                    case "Thief":
                                        scaled = thiefImage.getScaledInstance(cell.getWidth(), cell.getHeight(), Image.SCALE_DEFAULT);
                                        break;
                                    case "Wizard":
                                        scaled = wizardImage.getScaledInstance(cell.getWidth(), cell.getHeight(), Image.SCALE_DEFAULT);
                                        break;
                                }
                                if (scaled != null) {
                                    JLabel picLabel = new JLabel(new ImageIcon(scaled));
                                    cell.add(picLabel);
                                    pack();
                                } else {
                                    Logger.print("Error: sprite not loaded");
                                }
                            }
                        });
                        break;
                    case 2:
                        cell.setBackground(new Color(58, 48, 87));
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                scaled = unknownImage.getScaledInstance(cell.getWidth(), cell.getHeight(), Image.SCALE_DEFAULT);
                                if (scaled != null) {
                                    JLabel picLabel = new JLabel(new ImageIcon(scaled));
                                    cell.add(picLabel);
                                    pack();
                                } else {
                                    Logger.print("Error: sprite not loaded");
                                }
                            }
                        });
                        break;
                    case 8:
                        cell.setBackground(new Color(255, 178, 102));
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                scaled = fightImage.getScaledInstance(cell.getWidth(), cell.getHeight(), Image.SCALE_DEFAULT);
                                if (scaled != null) {
                                    JLabel picLabel = new JLabel(new ImageIcon(scaled));
                                    cell.add(picLabel);
                                    pack();
                                } else {
                                    Logger.print("Error: sprite not loaded");
                                }
                            }
                        });
                        break;
                    default:
                        cell.setBackground(new Color(157, 129, 233));
                        break;
                }
                panelGrid.add(cell);
                cell.addMouseListener(new MouseListener() {
                    public void mouseClicked(MouseEvent e) {
                        if (x + 1 < squareMap.getMapSize() && squareMap.getMap()[x + 1][y] == 1) {
                            GameManager.move(1);
                        } else if (y - 1 >= 0 && squareMap.getMap()[x][y - 1] == 1) {
                            GameManager.move(2);
                        } else if (x - 1 >= 0 && squareMap.getMap()[x - 1][y] == 1) {
                            GameManager.move(3);
                        } else if (y + 1 < squareMap.getMapSize() && squareMap.getMap()[x][y + 1] == 1) {
                            GameManager.move(4);
                        }
                        if (bEncounterPhase) {
                            panelEncounter.setVisible(true);
                            bEncounterPhase = false;
                        } else {
                            GameManager.winCondition();
                        }
                        panelLoot.setVisible(false);
                        panelGrid.removeAll();
                        gridLayout.setRows(squareMap.getMapSize());
                        gridLayout.setColumns(squareMap.getMapSize());
                        gridLayout.setHgap(-1);
                        gridLayout.setVgap(-1);
                        panelGrid.setLayout(gridLayout);
                        mapHandler();
                        panelGrid.revalidate();
                        panelGrid.repaint();
                        pack();
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    }
                });
            }
        }
    }

    private class ButtonCreateListener implements ActionListener{
        public void actionPerformed(ActionEvent arg0) {
            panelSelectHero.remove(buttonCancel);
            panelCreateHero.add(buttonCancel);
            buttonCreate.setVisible(false);
            buttonSelect.setVisible(false);
            buttonSwitch.setVisible(false);
            panelCreateHero.setVisible(true);

            Print.printHeroDetail(1);
        }
    }

    private class ButtonSelectListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            DbHandler.getInstance().printDB();
            if (!bIsHero) {
                logTextArea.setText("> No saved hero");
            } else {
                panelCreateHero.remove(buttonCancel);
                panelSelectHero.add(buttonCancel);
                buttonCreate.setVisible(false);
                buttonSelect.setVisible(false);
                buttonSwitch.setVisible(false);
                comboListSelect.removeAllItems();
                List<Hero> heroList = DbHandler.getInstance().getDB();
                for (Hero h : heroList) {
                    comboListSelect.addItem(h.getName());
                }
                panelSelectHero.setVisible(true);
                bIsHero = false;
            }
        }
    }

    private class ButtonHeroCreateListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int index = comboListCreate.getSelectedIndex();
            Character character = null;
            switch (index) {
                case 0:
                    character = CharacterFactory.newHero(inputTextField.getText().trim(), EType.WARRIOR);
                    break;
                case 1:
                    character = CharacterFactory.newHero(inputTextField.getText().trim(), EType.THIEF);
                    break;
                case 2:
                    character = CharacterFactory.newHero(inputTextField.getText().trim(), EType.WIZARD);
                    break;
            }
            if (character != null) {
                DbHandler.getInstance().insertHero((Hero) character);
                panelCreateHero.setVisible(false);
                buttonCreate.setVisible(true);
                buttonSelect.setVisible(true);
                buttonSwitch.setVisible(true);
            }
        }
    }

    private class ButtonHeroSwitchListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Window.this.dispose();
            CliView.run();
        }
    }

    private class ButtonHeroEncounterListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            buttonCancelMain.setVisible(false);
            if (jrFight.isSelected()) {
                GameManager.fight(false);
            } else if (jrRun.isSelected()) {
                GameManager.run();
            }
            GameManager.winCondition();
            panelGrid.removeAll();
            gridLayout.setRows(squareMap.getMapSize());
            gridLayout.setColumns(squareMap.getMapSize());
            gridLayout.setHgap(-1);
            gridLayout.setVgap(-1);
            panelGrid.setLayout(gridLayout);
            mapHandler();
            panelGrid.revalidate();
            panelGrid.repaint();
            if (bLootChoice) {
                panelLoot.setVisible(true);
                bLootChoice = false;
            } else {
                buttonCancelMain.setVisible(true);
                labelStats.setText("<html>Name: " + hero.getName() + "<br>" +
                        "Type: " + hero.getType() + "<br>" +
                        "Level: " + hero.getLevel() + "<br>" +
                        "Experience: " + hero.getXp() + "<br>" +
                        "Attack: " + hero.getAttack() + "<br>" +
                        "Defense: " + hero.getDefense() + "<br>" +
                        "Health: " + hero.getHp() + "<br>" +
                        "Weapon: " + hero.getWeapon().getName() + "<br>" +
                        "Armor: " + hero.getArmor().getName() + "<br>" +
                        "Helm: " + hero.getHelm().getName() + "</html>");
            }
            panelEncounter.setVisible(false);
        }
    }

    private class ButtonHeroLootListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (jrYes.isSelected()) {
                hero.pickUp(artifact, artifact.getType());
                Logger.print("<" + artifact.getName() + "> equipped");
            }
            labelStats.setText("<html>Name: " + hero.getName() + "<br>" +
                    "Type: " + hero.getType() + "<br>" +
                    "Level: " + hero.getLevel() + "<br>" +
                    "Experience: " + hero.getXp() + "<br>" +
                    "Attack: " + hero.getAttack() + "<br>" +
                    "Defense: " + hero.getDefense() + "<br>" +
                    "Health: " + hero.getHp() + "<br>" +
                    "Weapon: " + hero.getWeapon().getName() + "<br>" +
                    "Armor: " + hero.getArmor().getName() + "<br>" +
                    "Helm: " + hero.getHelm().getName() + "</html>");
            panelLoot.setVisible(false);
            buttonCancelMain.setVisible(true);
            bFightPhase = false;
        }
    }

    private class ButtonRunListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            panelSelectHero.setVisible(false);
            picLabel.setVisible(false);
            hero = DbHandler.getInstance().getHeroData(comboListSelect.getSelectedItem().toString());
            squareMap = MapFactory.generateMap(hero);
            Logger.print(hero.getName() + " arrived in a hostile environment");
            gridLayout.setRows(squareMap.getMapSize());
            gridLayout.setColumns(squareMap.getMapSize());
            gridLayout.setHgap(-1);
            gridLayout.setVgap(-1);
            panelGrid.setVisible(true);
            picLabel.setVisible(false);
            labelStats.setText("<html>Name: " + hero.getName() + "<br>" +
                                "Type: " + hero.getType() + "<br>" +
                                "Level: " + hero.getLevel() + "<br>" +
                                "Experience: " + hero.getXp() + "<br>" +
                                "Attack: " + hero.getAttack() + "<br>" +
                                "Defense: " + hero.getDefense() + "<br>" +
                                "Health: " + hero.getHp() + "<br>" +
                                "Weapon: " + hero.getWeapon().getName() + "<br>" +
                                "Armor: " + hero.getArmor().getName() + "<br>" +
                                "Helm: " + hero.getHelm().getName() + "</html>");
            panelStats.setVisible(true);
            buttonCancelMain.setVisible(true);
            mapHandler();
        }
    }

    private class ButtonDeleteHeroListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int dialog = JOptionPane.YES_NO_OPTION;
            int input = JOptionPane.showConfirmDialog(Window.this, "Confirm deletion ?","Deletion", dialog);
            if (input == JOptionPane.YES_OPTION) {
                DbHandler.getInstance().deleteHero(comboListSelect.getSelectedItem().toString());
                DbHandler.getInstance().printDB();
                if (nbHero == 0) {
                    panelSelectHero.setVisible(false);
                    buttonCreate.setVisible(true);
                    buttonSelect.setVisible(true);
                    buttonSwitch.setVisible(true);
                    bIsHero = false;
                } else {
                    comboListSelect.removeAllItems();
                    List<Hero> heroList = DbHandler.getInstance().getDB();
                    for (Hero h : heroList) {
                        comboListSelect.addItem(h.getName());
                    }
                }
            }
        }
    }

    private class ButtonCancelMainListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            panelStats.setVisible(false);
            panelEncounter.setVisible(false);
            panelGrid.removeAll();
            panelGrid.setVisible(false);
            picLabel.setVisible(true);
            buttonCancelMain.setVisible(false);
            buttonCreate.setVisible(true);
            buttonSelect.setVisible(true);
            buttonSwitch.setVisible(true);
        }
    }

    private class ButtonCancelListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            panelEncounter.setVisible(false);
            panelCreateHero.setVisible(false);
            panelSelectHero.setVisible(false);
            buttonCreate.setVisible(true);
            buttonSelect.setVisible(true);
            buttonSwitch.setVisible(true);
        }
    }

    private class ComboCreateAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int index = comboListCreate.getSelectedIndex();
            Print.printHeroDetail(index + 1);
        }
    }
}
