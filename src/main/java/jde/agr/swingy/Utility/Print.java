package jde.agr.swingy.Utility;

public class Print {

    public static void printMenu() {
        Logger.print("1 - Create a new hero\n" +
                "2 - Select a previously created hero\n" +
                "3 - Switch to GUI view");
    }

    public static void printHeroList() {
        System.out.println("Avatar list:\n" +
                "1 - Warrior\n" +
                "2 - Thief\n" +
                "3 - Wizard");
    }

    public static void printHeroDetail(int nb) {
        switch (nb) {
            case 1:
                Logger.print("Warrior level 0\n" +
                        "- Attack: 3\n" +
                        "- Defense: 5\n" +
                        "- Health: 8");
                break;
            case 2:
                Logger.print("Thief level 0\n" +
                        "- Attack: 4\n" +
                        "- Defense: 3\n" +
                        "- Health: 5");
                break;
            case 3:
                Logger.print("Wizard level 0\n" +
                        "- Attack: 5\n" +
                        "- Defense: 2\n" +
                        "- Health: 3");
                break;
        }
    }

    public static void printDirections() {
        Logger.print("Please choose a direction:\n" +
                "1: North\n" +
                "2: East\n" +
                "3: South\n" +
                "4: West\n" +
                "5: See hero stats");
    }

    public static void printFightOptions() {
        Logger.print("1 - Fight\n" +
                "2 - Run");
    }
}
