package attic.riow;

import ebrt.Color;
import attic.camera.Film;
import ebrt.interactions.SurfaceInteraction;
import attic.math.Normal3d;
import attic.math.Point2i;
import attic.math.Point3d;
import attic.math.Ray;
import attic.math.Vector3d;
import attic.primitives.GeometricPrimitive;
import attic.primitives.Primitive;
import attic.primitives.PrimitiveList;
import attic.shapes.Sphere;

public class Tracer {

    private final TracerConfig config;
    private final Primitive world = PrimitiveList.from(
            new GeometricPrimitive(new Sphere(new Point3d(0, 0, -1), 0.5)),
            new GeometricPrimitive(new Sphere(new Point3d(0, -100.5, -1), 100)));

    public Tracer(TracerConfig config) {
        this.config = config.complete();
    }

    private Color computeColor(Ray ray) {

        SurfaceInteraction si = world.intersect(ray, Double.MAX_VALUE, false);
        if (si != null) {
            Normal3d n = si.normal;
            return new Color(n.x() + 1, n.y() + 1, n.z() + 1).mul(0.5);
        }

        double t = 0.5 * (ray.direction().normalize().y() + 1.0);
        return Color.WHITE.mul(1.0 - t).plus(Color.SKY_BLUE.mul(t));
    }

    private Ray castRay(double u, double v) {
        Vector3d dir = config.lowerLeft
                .plus(config.horizontal.mul(u))
                .plus(config.vertical.mul(v))
                .minus(config.origin);
        return new Ray(config.origin, dir);
    }

    public void run() {

        Film film = new Film(new Point2i(config.imageWidth, config.imageHeight));

        for (int j=config.imageHeight-1; j>=0; j--) {
            for (int i=0; i<config.imageWidth; i++) {
                double u = (double) i / ((config.imageWidth) - 1);
                double v = (double) j / ((config.imageHeight) - 1);
                Ray r = castRay(u, v);
                Color c = computeColor(r);
                Point2i p = new Point2i(i, j);
                film.splat(p, c);
            }
        }

        film.writeToFile(1.0, "/Users/QBX490/Desktop/ebrt.png");
    }

    public static void main(String [] args) {
        new Tracer(new TracerConfig()).run();
    }
}