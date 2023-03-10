package ebrt.math;

import java.util.Iterator;

public record Bounds2i(Point2i min, Point2i max) implements Iterable<Point2i> {

    public Bounds2i(Point2i min, Point2i max) {
        this.min = min.min(max);
        this.max = min.max(max);
    }

    public int width() {
        return max.x() - min.x();
    }

    public int height() {
        return max.y() - min.y();
    }

    public int area() {
        return width() * height();
    }

    public Vector2i diagonal() {
        return min.towards(max);
    }

    public boolean containsExclusive(Point2i point) {
        return (point.x() >= min.x() && point.x() < max.x() &&
                point.y() >= min.y() && point.y() < max.y());
    }

    public Bounds2i intersect(Bounds2i other) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Point2i> iterator() {
        throw new UnsupportedOperationException();
    }

    public void forEach(BoundsConsumer consumer) {
        for (int x=min.x(); x<max.x(); x++) {
            for (int y=min.y(); y< max().y(); y++) {
                consumer.accept(x, y);
            }
        }
    }

    public interface BoundsConsumer {

        void accept(int x, int y);
    }
}
