package jde.agr.swingy.controller;

import jde.agr.swingy.Utility.*;
import jde.agr.swingy.database.DbHandler;
import jde.agr.swingy.model.artifacts.Armor;
import jde.agr.swingy.model.artifacts.Helm;
import jde.agr.swingy.model.artifacts.Weapon;
import jde.agr.swingy.model.characters.Foe;
import jde.agr.swingy.view.cli.CliView;

import java.util.Random;
import java.util.Scanner;
import static jde.agr.swingy.Utility.Global.*;
import static jde.agr.swingy.controller.CharacterFactory.newFoe;

public class GameManager {

    private final static int NORTH = 1;
    private final static int EAST = 2;
    private final static int SOUTH = 3;
    private final static int WEST = 4;

    private static int[] oldMove = new int[2];

    public static void winCondition() {
        if (hero.getX() == squareMap.getMapSize() - 1 ||
                hero.getY() == squareMap.getMapSize() - 1 ||
                hero.getX() == 0 || hero.getY() == 0) {
            Logger.print("You reached your goal !");
            squareMap = MapFactory.generateMap(hero);
            if (!bIsGUI) {
                CliView.moveHero();
            }
        }
    }

    private static void loot() {
        int random = new Random().nextInt(2);

        if (random == 1) {
            Logger.print("An artifact dropped !");
            int random2 = new Random().nextInt(4);
            int stats = hero.getLevel() + 1;
            switch (random2) {
                case 0:
                    artifact = new Weapon("Good sword", stats);
                    Logger.print(artifact.getName() + " - Attack: " + stats);
                    Logger.print("If you equip this artifact, you will gain " +
                            (((Weapon) artifact).getAttack() - hero.getWeapon().getAttack()) + " attack");
                    break;
                case 1:
                    artifact = new Armor("Good armor", stats);
                    Logger.print(artifact.getName() + " - Defense: " + stats);
                    Logger.print("If you equip this artifact, you will gain " +
                            (((Armor) artifact).getDefense() - hero.getArmor().getDefense()) + " defense");
                    break;
                case 2:
                    artifact = new Helm("Good helm", stats);
                    Logger.print(artifact.getName() + " - Hp: " + stats);
                    Logger.print("If you equip this artifact, you will gain " +
                            (((Helm) artifact).getHp() - hero.getHelm().getHp()) + " hp");
                    break;
                case 3:
                    hero.setHp(hero.getHp() + hero.getLevel() + 1);
                    Logger.print("You found a health potion ! Current health: " + hero.getHp());
                    return;
            }
            if (!bIsGUI) {
                Logger.print("Will you keep it ?");
                Logger.print("1 - Yes");
                Logger.print("2 - No");
                Scanner in = new Scanner(System.in);
                while (in.hasNextLine()) {
                    String arg = in.nextLine();
                    if (arg.matches("\\s*[1-2]\\s*")) {
                        Integer nb = Integer.parseInt(arg.trim());
                        if (nb == 1) {
                            hero.pickUp(artifact, artifact.getType());
                            Logger.print("<" + artifact.getName() + "> equipped");
                            break;
                        } else if (nb == 2) {
                            break;
                        } else {
                            Logger.print(ANSI_RED + ">" + ANSI_RESET + " Incorrect choice");
                        }
                    } else {
                        Logger.print(ANSI_RED + ">" + ANSI_RESET + " Incorrect choice");
                    }
                }
                bFightPhase = false;
            } else {
                bLootChoice = true;
            }
        }
    }

    public static void fight(boolean fled) {
        if (fled) {
            Logger.print("Enemy starts !");
            while (hero.getHp() > 0 && foe.getHp() > 0) {
                foe.attack(hero);
                foe.attack(hero);
                if (hero.getHp() > 0) {
                    hero.attack(foe);
                }
            }
        } else {
            Logger.print("You starts !");
            while (hero.getHp() > 0 && foe.getHp() > 0) {
                hero.attack(foe);
                if (foe.getHp() > 0) {
                    foe.attack(hero);
                }
            }
        }
        if (hero.getHp() <= 0) {
            Logger.print("Game Over");
            if (!bIsGUI) {
                CliView.run();
            }
        } else if (foe.getHp() <= 0) {
            DbHandler.getInstance().updateHero(hero);
            hero.setPosition(0, 0);
            Logger.print("You win !");
            loot();
        }
    }

    public static void run() {
        int random = new Random().nextInt(2);
        switch (random) {
            case 0:
                Logger.print("Escape failed !");
                fight(true);
                break;
            case 1:
                Logger.print("You fled from battle.");
                hero.setPosition(oldMove[0] * -1, oldMove[1] * -1);
                break;
        }
        bFightPhase = false;
    }

    private static void fightOrRun() {
        if (!bIsGUI) {
            Print.printFightOptions();
        }
        Scanner in = new Scanner(System.in);
        if (!bIsGUI) {
            while (in.hasNextLine()) {
                String arg = in.nextLine();
                if (arg.matches("\\s*[1-2]\\s*")) {
                    Integer nb = Integer.parseInt(arg.trim());
                    switch (nb) {
                        case 1:
                            fight(false);
                            return;
                        case 2:
                            run();
                            return;
                    }
                } else {
                    Logger.print(ANSI_RED + ">" + ANSI_RESET + " Incorrect choice");
                    Print.printFightOptions();
                }
            }
        } else {
            bEncounterPhase = true;
        }
    }

    public static void move(int direction) {
        switch (direction) {
            case NORTH:
                hero.setPosition(-1, 0);
                oldMove[0] = -1;
                oldMove[1] = 0;
                break;
            case EAST:
                hero.setPosition(0, 1);
                oldMove[0] = 0;
                oldMove[1] = 1;
                break;
            case SOUTH:
                hero.setPosition(1, 0);
                oldMove[0] = 1;
                oldMove[1] = 0;
                break;
            case WEST:
                hero.setPosition(0, -1);
                oldMove[0] = 0;
                oldMove[1] = -1;
                break;
        }
        if (squareMap.getMap()[hero.getX()][hero.getY()] == 8) {
            bFightPhase = true;
            int random = new Random().nextInt(3);
            foe = (Foe) newFoe((random == 0) ? EType.RAT : ((random == 1) ? EType.BAT : EType.SLIME), hero);
            Logger.print("Enemy encounter : \"" + foe.getName() + "\" level " + foe.getLevel() + " !");
            fightOrRun();
        }
    }
}
