package ebrt.samplers;

import ebrt.math.Point2d;
import ebrt.math.Point2i;

import java.util.concurrent.ThreadLocalRandom;

public class RandomSampler implements Sampler {

    private final int samplesPerPixel;
    private Point2i currentPixel;

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
    public CameraSample cameraSample(Point2i raster) {
        Point2d film = raster.plus(get2d());
        Point2d lens = get2d();
        double time = get1d();
        return new CameraSample(film, lens, time);
    }

    @Override
    public void startPixel(Point2i pixel) {
        currentPixel = pixel;
    }
}
