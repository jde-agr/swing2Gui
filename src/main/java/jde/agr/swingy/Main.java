package jde.agr.swingy;

import jde.agr.swingy.Utility.Logger;
import jde.agr.swingy.view.cli.CliView;
import jde.agr.swingy.view.gui.GuiView;

import javax.validation.Validation;

import static jde.agr.swingy.Utility.Global.*;

public class Main {
    public static void main(String[] arg) {
        try {
            switch (arg[0]) {
                case "console":
                    Logger.print(ANSI_CYAN + "Welcome to \"42 RPG\"" + ANSI_RESET);
                    factory = Validation.buildDefaultValidatorFactory();
                    CliView.run();
                    break;
                case "gui":
                    factory = Validation.buildDefaultValidatorFactory();
                    GuiView.run();
                    break;
                default:
                    Logger.print("Usage: java -jar target/swingy [console/gui]");
                    break;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            Logger.print("Usage: java -jar target/swingy [console/gui]");
        }
    }
}
