package attic.samplers;

import attic.math.Point2d;
import attic.math.Point2i;

import java.util.concurrent.ThreadLocalRandom;

public class RandomSampler implements Sampler {

    private final int samplesPerPixel;

    public RandomSampler(int samplesPerPixel) {
        this(samplesPerPixel, -1);
    }

    public RandomSampler(int samplesPerPixel, long seed) {
        this.samplesPerPixel = samplesPerPixel;
        if (seed != -1) {
            ThreadLocalRandom.current().setSeed(seed);
        }
    }

    @Override
    public int samplesPerPixel() {
        return samplesPerPixel;
    }

    @Override
    public Sampler clone(long seed) {
        return new RandomSampler(samplesPerPixel, seed);
    }

    @Override
    public double get1d() {
        return ThreadLocalRandom.current().nextDouble();
    }

    @Override
    public Point2d get2d() {
        return new Point2d(get1d(), get1d());
    }

    @Override
    public CameraSample cameraSample() {
        return new CameraSample(get2d(), get2d(), get1d());
    }

    @Override
    public void startPixel(Point2i pixel) {

    }
}
