package attic.filters;

import attic.math.Point2d;

public class LanczosSincFilter extends Filter {

    private final double tau;

    public LanczosSincFilter(Point2d radius, double tau) {
        super(radius);
        this.tau = tau;
    }

    @Override
    public double evaluate(Point2d point) {
        return windowedSinc(point.x(), radius().x()) * windowedSinc(point.y(), radius().y());
    }

    private double windowedSinc(double x, double r) {
        x = Math.abs(x);
        return x > r ? 0 : sinc(x) * sinc(x / tau);
    }

    private double sinc(double x) {
        x = Math.abs(x);
        return x < 1e-5 ? 1 : Math.sin(Math.PI * x) / (Math.PI * x);
    }
}
