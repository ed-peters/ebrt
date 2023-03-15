package ebrt.main;

import ebrt.camera.Camera;
import ebrt.camera.Film;
import ebrt.camera.PerspectiveCamera;
import ebrt.math.Bounds2d;
import ebrt.math.Point2d;
import ebrt.math.Point2i;
import ebrt.math.Point3d;
import ebrt.math.Transform;
import ebrt.math.Vector3d;

public final class TracerConfig {

    public double aspectRatio = 1.0;

    public int imageWidth = 400;
    public int imageHeight;

    public double viewportHeight = 2.0;
    public double viewportWidth;
    public double focalLength = 1.0;
    public double fieldOfView = 90.0;
    public double aperture = 0; // 2.0;

    public int samplesPerPixel = 5;

    public Point3d origin = Point3d.ORIGIN;
    public Point3d lookAt = new Point3d(0, 0, 1);
    public Vector3d up = Vector3d.Y;

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

    public Camera makeCamera() {

        int imageHeight = (int) (imageWidth / aspectRatio);

        // camera at origin, looking down +z axis, y is up
        Transform cameraToWorld = Transform.lookAt(origin, lookAt, up);

        // assumption: screen is what RIOW calls "viewport"
        double screenHeight = 2.0;
        double screenWidth = aspectRatio * screenHeight;
//        Bounds2d screenWindow = new Bounds2d(
//                new Point2d(-screenWidth / 2, screenHeight / 2),
//                new Point2d(screenWidth / 2, -screenHeight / 2));
        Bounds2d screenWindow = new Bounds2d(
                new Point2d(-screenWidth/2, screenHeight/2),
                new Point2d(screenWidth/2, -screenHeight/2));

        // use simple lens model from RIOW to calculate from aperture
        double lensRadius = aperture / 2.0;
        double focalDistance = lookAt.minus(origin).length();
        return new PerspectiveCamera(
                cameraToWorld,
                screenWindow,
                lensRadius,
                focalDistance,
                fieldOfView,
                null,
                new Film(new Point2i(imageWidth, imageHeight)));
    }
}
