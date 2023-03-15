package ebrt.lights;

import ebrt.Color;
import ebrt.Scene;
import ebrt.interactions.Ray;
import ebrt.interactions.SurfaceInteraction;
import ebrt.math.Point2d;

public interface Light {

//    virtual Spectrum Sample_Li(const Interaction &ref, const Point2f &u,
//                               Vector3f *wi, Float *pdf, VisibilityTester *vis) const = 0;

    Color le(Ray ray);

    default void preprocess(Scene scene) { }
}
