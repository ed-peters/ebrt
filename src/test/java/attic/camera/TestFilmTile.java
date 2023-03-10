package attic.camera;

import ebrt.Color;
import attic.filters.BoxFilter;
import attic.math.Bounds2i;
import attic.math.Point2d;
import attic.math.Point2i;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestFilmTile {

    @Test
    public void testSuccess() {

        FilmTile tile = new FilmTile(
                new Bounds2i(new Point2i(10, 15), new Point2i(20, 25)),
                new BoxFilter(new Point2d(2, 2)));
        tile.addSample(new Point2d(11, 21), Color.WHITE, 1.0);
        tile.addSample(new Point2d(10, 20), Color.WHITE, 1.0);
        tile.addSample(new Point2d(14, 22), Color.WHITE, 1.0);

        Map<Point2i,Color> colors = new HashMap<>();
        Map<Point2i,Double> weights = new HashMap<>();
        tile.forEach((x,y,c,w) -> {
            Point2i p = new Point2i(x, y);
            colors.put(p, c);
            weights.put(p, w);
        });
        assertEquals(new Color(2, 2, 2), colors.get(new Point2i(12, 20)));
        assertEquals(new Color(1, 1, 1), colors.get(new Point2i(13, 20)));
        assertEquals(new Color(0, 0, 0), colors.get(new Point2i(12, 26)));
        assertEquals(2, weights.get(new Point2i(12, 20)), 1e-8);
        assertEquals(1, weights.get(new Point2i(13, 20)), 1e-8);
        assertEquals(0, weights.get(new Point2i(12, 26)), 1e-8);
    }
}
