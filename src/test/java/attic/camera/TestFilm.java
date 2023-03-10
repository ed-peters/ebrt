package attic.camera;

import ebrt.Color;
import attic.filters.BoxFilter;
import attic.math.Bounds2d;
import attic.math.Point2d;
import attic.math.Point2i;
import org.junit.Test;

public class TestFilm {

    @Test
    public void testGenerateImage() {
        Film film = new Film(new Point2i(300, 300), 3.0, new BoxFilter(new Point2d(2, 2)), Bounds2d.UNIT, 1.0);
        film.sampleBounds().forEach((x,y)-> {
            double r = (double) x / 640.0;
            double g = (double) y / 480.0;
            double b = 1.0;
            film.splat(new Point2i(x, y), new Color(r, g, b));
        });
        film.writeToFile(1.0, "/Users/QBX490/Desktop/film.png");
    }
}
