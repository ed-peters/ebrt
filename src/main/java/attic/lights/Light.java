package attic.lights;

import ebrt.Color;
import attic.Scene;
import ebrt.interactions.SurfaceInteraction;
import attic.math.Point2d;
import attic.math.Ray;

public interface Light {

//    virtual Spectrum Sample_Li(const Interaction &ref, const Point2f &u,
//                               Vector3f *wi, Float *pdf, VisibilityTester *vis) const = 0;
    SampleResult sampleLi(SurfaceInteraction surfaceInteraction, Point2d u);

    Color le(Ray ray);

    default void preprocess(Scene scene) { }
}
