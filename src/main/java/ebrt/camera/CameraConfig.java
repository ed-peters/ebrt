package ebrt.camera;

import ebrt.math.Bounds2d;
import ebrt.math.Transform;
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
