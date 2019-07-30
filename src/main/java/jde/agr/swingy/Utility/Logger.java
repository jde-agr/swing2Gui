package jde.agr.swingy.Utility;


import static jde.agr.swingy.Utility.Global.bIsGUI;

public class Logger {

    static public void print(String content) {
        if (!bIsGUI) {
            System.out.println(content);
        } else {
            if (!Global.bFightPhase) {
                Global.logTextArea.setText(content + "\n");
            } else {
                Global.logTextArea.append(content + "\n");
            }
        }
    }
}
