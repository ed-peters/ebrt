package ebrt.samplers;

import ebrt.math.Point2d;

public record CameraSample(Point2d film, Point2d lens, double time) {

    public CameraSample shift(double dx, double dy) {
        Point2d f = new Point2d(film.x() + dx, film.y() + dy);
        return new CameraSample(f, lens, time);
    }
}
