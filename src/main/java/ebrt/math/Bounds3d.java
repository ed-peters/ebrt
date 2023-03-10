package ebrt.math;

import ebrt.interactions.Ray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public record Bounds3d(Point3d min, Point3d max) {

    public Bounds3d(Point3d min, Point3d max) {
        this.min = Point3d.min(min, max);
        this.max = Point3d.max(min, max);
    }

    public Hits intersects(Ray ray) {

        double t0 = 0;
        double t1 = Double.MAX_VALUE;

        for (Axis axis : Axis.values()) {

            double invdir = 1.0 / ray.direction().component(axis);
            double tnear = (min.component(axis) - ray.origin().component(axis)) * invdir;
            double tfar = (max.component(axis) - ray.origin().component(axis)) * invdir;

            if (tnear > tfar) {
                double tmp = tnear;
                tnear = tfar;
                tfar = tmp;
            }

            tfar *= 1.0 + 2.0 * Utils.gamma(3);

            t0 = Math.max(t0, tnear);
            t1 = Math.min(t1, tfar);
            if (t0 > t1) {
                return null;
            }
        }

        return new Hits(t0, t1);
    }
    
    public List<Point3d> corners() {
        List<Point3d> corners = new ArrayList<>();
        corners.add(new Point3d(min().x(), min().y(), min().z()));
        corners.add(new Point3d(max().x(), min().y(), min().z()));
        corners.add(new Point3d(min().x(), max().y(), min().z()));
        corners.add(new Point3d(min().x(), min().y(), max().z()));
        corners.add(new Point3d(max().x(), max().y(), min().z()));
        corners.add(new Point3d(min().x(), max().y(), max().z()));
        corners.add(new Point3d(max().x(), min().y(), max().z()));
        corners.add(new Point3d(max().x(), max().y(), max().z()));
        return corners;
    }

    public Bounds3d transform(Transform transform) {
        List<Point3d> corners = corners().stream()
                .map(p -> p.transform(transform))
                .collect(Collectors.toList());
        return around(corners);
    }

    public static Bounds3d around(Point3d... points) {
        return around(Arrays.asList(points));
    }

    public static Bounds3d around(Collection<Point3d> points) {

        double minX = 0;
        double minY = 0;
        double minZ = 0;
        double maxX = 0;
        double maxY = 0;
        double maxZ = 0;
        
        for (Point3d p : points) {
            minX = Math.min(minX, p.x());
            minY = Math.min(minY, p.y());
            minZ = Math.min(minZ, p.z());
            maxX = Math.max(maxX, p.x());
            maxY = Math.max(maxY, p.y());
            maxZ = Math.max(maxZ, p.z());
        }

        return new Bounds3d(new Point3d(minX, minY, minZ), new Point3d(maxX, maxY, maxZ));
    }

    public static Bounds3d merge(Bounds3d... bounds) {
        return merge(Arrays.asList(bounds));
    }
    
    public static Bounds3d merge(Collection<Bounds3d> bounds) {
        List<Point3d> points = bounds.stream()
                .flatMap(b -> b.corners().stream())
                .collect(Collectors.toList());
        return around(points);
    }
}
