package jde.agr.swingy.Utility;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Utility {

    public static BufferedImage loadImage(String path) {
        BufferedImage myPicture = null;

        try {
            myPicture = ImageIO.read(new File(path));
        } catch (IOException e) {
            Logger.print("Image loader: fail");
            System.exit(0);
        }
        return (myPicture);
    }
}
