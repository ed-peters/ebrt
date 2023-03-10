package attic.camera;

import attic.math.Bounds2d;
import attic.math.Transform;
import ebrt.media.Medium;

public class CameraConfig {

    public Transform cameraToWorld;
    public Transform cameraToScreen;
    public Bounds2d screenWindow;
    public double lensRadius;
    public double focalDistance;
    public Medium medium;
    public Film film;
}
