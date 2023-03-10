package attic.riow;

import attic.math.Point3d;
import attic.math.Vector3d;

public final class TracerConfig {

    public double aspectRatio = 1.0;

    public int imageWidth = 400;
    public int imageHeight;

    public double viewportHeight = 2.0;
    public double viewportWidth;

    public double focalLength = 1.0;

    public Point3d origin = Point3d.ORIGIN;
    public Vector3d horizontal;
    public Vector3d vertical;
    public Point3d lowerLeft;

    public TracerConfig complete() {

        imageHeight = (int) (imageWidth / aspectRatio);
        viewportWidth = aspectRatio * viewportHeight;
        horizontal = new Vector3d(viewportWidth, 0, 0);
        vertical = new Vector3d(0, viewportHeight, 0);
        lowerLeft = origin
                .minus(horizontal.div(2.0))
                .minus(vertical.div(2.0))
                .minus(new Vector3d(0, 0, focalLength));

        return this;
    }
}
