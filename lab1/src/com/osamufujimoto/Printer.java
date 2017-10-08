package com.osamufujimoto;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static com.osamufujimoto.Util.PRINT;

public class Printer {

    public static void plotEdges(Set<Node> nodes, File img) throws IOException {


        BufferedImage image = new BufferedImage(395, 500, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < 500; y++) for (int x = 0; x < 395; x++)
        {
            image.setRGB(x, y, new Color(255, 255, 255).getRGB());
        }

        for (Node nds : nodes) {

            image.setRGB(nds.x, nds.y, new Color(0, 0, 0).getRGB());

        }

        ImageIO.write(image, "png", img);
        // raster.setPixels(0,0,width,height,pixels);

    }

}
