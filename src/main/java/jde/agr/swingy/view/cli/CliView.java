package jde.agr.swingy.view.cli;

import jde.agr.swingy.Utility.EType;
import jde.agr.swingy.Utility.Logger;
import jde.agr.swingy.Utility.Print;
import jde.agr.swingy.controller.CharacterFactory;
import jde.agr.swingy.controller.MapFactory;
import jde.agr.swingy.controller.GameManager;
import jde.agr.swingy.database.DbHandler;
import jde.agr.swingy.model.characters.Hero;
import jde.agr.swingy.view.gui.GuiView;

import java.util.List;
import java.util.Scanner;

import static jde.agr.swingy.Utility.Global.*;

public class CliView {

    public static void run() {
        bIsGUI = false;
        Logger.print("------- Console mode -------");
        Print.printMenu();
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String arg = in.nextLine();
            if (arg.matches("\\s*[1-3]\\s*")) {
                Integer nb = Integer.parseInt(arg.trim());
                switch (nb) {
                    case 1:
                        createHero();
                        break;
                    case 2:
                        selectHero();
                        break;
                    case 3:
                        GuiView.run();
                        return;
                    default:
                        Logger.print(ANSI_RED + ">" + ANSI_RESET + " Incorrect choice. Please try again");
                        Print.printMenu();
                }
            } else {
                Logger.print(ANSI_RED + ">" + ANSI_RESET + " Incorrect choice. Please try again");
                Print.printMenu();
            }
        }
        in.close();
    }

    public static void moveHero() {
        Print.printDirections();
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String arg = in.nextLine();
            if (arg.matches("\\s*[1-4]\\s*")) {
                Integer nb = Integer.parseInt(arg.trim());
                GameManager.move(nb);
                GameManager.winCondition();
            } else {
                if (arg.matches("\\s*5\\s*")) {
                    Logger.print("- Level: " + hero.getLevel());
                    Logger.print("- Xp: " + hero.getXp());
                    Logger.print("- Attack: " + hero.getAttack());
                    Logger.print("- Defense: " + hero.getDefense());
                    Logger.print("- Hp: " + hero.getHp());
                    Logger.print("- Weapon: " + hero.getWeapon().getName());
                    Logger.print("- Armor: " + hero.getArmor().getName());
                    Logger.print("- Helm: " + hero.getHelm().getName());
                } else {
                    Logger.print(ANSI_RED + ">" + ANSI_RESET + " Incorrect choice.");
                }
            }
            Print.printDirections();
        }
        in.close();
    }

    private static void selectHero() {
        Logger.print(ANSI_GREEN + "You can pick:" + ANSI_RESET);
        DbHandler.getInstance().printDB();
        if (!bIsHero) {
            Logger.print(ANSI_RED + ">" + ANSI_RESET + " No saved hero");
            Print.printMenu();
            return ;
        } else {
            Logger.print(ANSI_GREEN + "Choose your hero and continue your adventure !" + ANSI_RESET);
        }
        bIsHero = false;
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String arg = in.nextLine();
            List<Hero> heroList = DbHandler.getInstance().getDB();
            boolean match = false;
            for (Hero h : heroList) {
                if (h.getName().equals(arg.trim())) {
                    hero = DbHandler.getInstance().getHeroData(h.getName());
                    squareMap = MapFactory.generateMap(hero);
                    moveHero();
                    match = true;
                }
            }
            if (!match) {
                Logger.print(ANSI_RED + ">" + ANSI_RESET + " Incorrect choice. No such hero name");
            }
        }
    }

    private static void nameHero(EType type) {
        Logger.print("Enter a name:");
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String arg = in.nextLine();
            if (CharacterFactory.newHero(arg, type) != null) {
                DbHandler.getInstance().insertHero((Hero) CharacterFactory.newHero(arg.trim(), type));
                break;
            } else {
                Logger.print("Enter a name:");
            }
        }
    }

    private static void createHero() {
        Print.printHeroList();
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String arg = in.nextLine();
            if (arg.matches("\\s*[1-3]\\s*")) {
                Integer nb = Integer.parseInt(arg.trim());
                switch (nb) {
                    case 1:
                        nameHero(EType.WARRIOR);
                        break;
                    case 2:
                        nameHero(EType.THIEF);
                        break;
                    case 3:
                        nameHero(EType.WIZARD);
                        break;
                    default:
                        Logger.print(ANSI_RED + ">" + ANSI_RESET + " Incorrect choice. Hero not created");
                        Print.printHeroList();
                        break;
                }
                break;
            } else {
                if (arg.matches("\\s*see\\s+[1-3]\\s*")) {
                    Print.printHeroDetail(Integer.parseInt(arg.split("\\s+")[1]));
                } else {
                    Logger.print(ANSI_RED + ">" + ANSI_RESET + " Incorrect choice.");
                    Print.printHeroList();
                }
            }
        }
        Print.printMenu();
    }
}
