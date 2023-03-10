package ebrt.samplers;

import ebrt.math.Point2d;
import ebrt.math.Point2i;

public interface Sampler {

    int samplesPerPixel();

    Sampler clone(long seed);

    double get1d();

    Point2d get2d();

    CameraSample cameraSample();

    void startPixel(Point2i pixel);
}
