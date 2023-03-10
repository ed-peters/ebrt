package ebrt.camera;

import ebrt.interactions.Ray;
import ebrt.math.Point3d;
import ebrt.math.Transform;
import ebrt.math.Utils;
import ebrt.math.Vector3d;
import ebrt.math.Weighted;
import ebrt.media.Medium;
import ebrt.samplers.CameraSample;

public class FisheyeCamera extends Camera {

    public FisheyeCamera(Transform cameraToWorld, Medium medium, Film film) {
        super(cameraToWorld, medium, film);
    }

    @Override
    public Weighted<Ray> makeRay(CameraSample sample) {

        double theta = Math.PI * sample.film().y() / film().resolution().y();
        double ct = Math.cos(theta);
        double st = Math.sin(theta);

        double phi = Utils.TAU * sample.film().x() / film().resolution().x();
        double cp = Math.cos(phi);
        double sp = Math.sin(phi);

        Vector3d dir = new Vector3d(st * cp, ct, st * sp);
        return new Weighted<>(new Ray(Point3d.ORIGIN, dir).transform(cameraToWorld()), 1);
    }
}
