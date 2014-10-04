package clj_exif_orientation;

import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;

public class ScalrWrapper {
    public static BufferedImage rotate (BufferedImage src, String rotation) {
        return Scalr.rotate(src, Scalr.Rotation.valueOf(rotation));
    }
}
