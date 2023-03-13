package ebrt.math;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public Bounds2d toBounds2d() {
        return new Bounds2d(min.toPoint2d(), max.toPoint2d());
    }

    public Bounds2i intersect(Bounds2i other) {
        int minX = Math.max(min().x(), other.min.x());
        int minY = Math.max(min().y(), other.min.y());
        int maxX = Math.min(max().x(), other.max.x());
        int maxY = Math.min(max().y(), other.max.y());
        return new Bounds2i(new Point2i(minX, minY), new Point2i(maxX, maxY));
    }

    @Override
    public Iterator<Point2i> iterator() {
        List<Point2i> points = new ArrayList<>();
        forEach((x,y) -> points.add(new Point2i(x,y)));
        return points.iterator();
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
