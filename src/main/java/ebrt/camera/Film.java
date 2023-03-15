package ebrt.camera;

import ebrt.Color;
import ebrt.filters.BoxFilter;
import ebrt.filters.Filter;
import ebrt.math.Bounds2d;
import ebrt.math.Bounds2i;
import ebrt.math.Grid;
import ebrt.math.Point2d;
import ebrt.math.Point2i;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Film {

    private final Point2i resolution;
    private final double diagonal;
    private final Filter filter;
    private final Bounds2i croppedBounds;
    private final Bounds2i sampleBounds;
    private final Point2i pixelOffset;
    private final Grid<Pixel> pixels;

    public Film(Point2i resolution) {
        this(resolution, 0.3, new BoxFilter(Point2d.ONESIES), Bounds2d.UNIT);
    }

    public Film(Point2i resolution, double diagonal, Filter filter, Bounds2d crop) {
        this.resolution = resolution;
        this.diagonal = diagonal * 0.001;
        this.filter = filter;
        this.croppedBounds = crop.mul(resolution).ceil();
        this.sampleBounds = computeSampleBounds();
        this.pixelOffset = sampleBounds.min().abs();
        this.pixels = new Grid<>(sampleBounds, Pixel::new);
    }

    public Point2i resolution() {
        return resolution;
    }

    public Bounds2i sampleBounds() {
        return sampleBounds;
    }

    private Bounds2i computeSampleBounds() {
        Point2i min = croppedBounds.min().plus(Point2d.HALVSIES).minus(filter.radius()).floor();
        Point2i max = croppedBounds.max().minus(Point2d.HALVSIES).plus(filter.radius()).ceil();
        return new Bounds2i(min, max);
    }

    private Bounds2d computePhysicalExtent() {
        double aspect = (double) resolution.y() / (double) resolution.x();
        double x = Math.sqrt(diagonal * diagonal / (1 + aspect * aspect));
        double y = aspect * x;
        return new Bounds2d(new Point2d(-x / 2, -y / 2), new Point2d(x / 2, y / 2));
    }

    public FilmTile makeTile(Bounds2i sampleBounds) {
        Point2i p0 = sampleBounds.min().minus(Point2d.HALVSIES).minus(filter.radius()).ceil();
        Point2i p1 = sampleBounds.max().minus(Point2d.HALVSIES).plus(filter.radius()).plus(Point2d.ONESIES).floor();
        Bounds2i tileBounds = new Bounds2i(p0, p1).intersect(croppedBounds);
        return new FilmTile(tileBounds, filter);
    }

    public void mergeTile(FilmTile tile) {
        tile.forEach((x, y, c, w) -> {
            Pixel p = pixels.get(x, y);
            synchronized (p) {
                p.mergeColor(c, w);
            }
        });
    }

    public void fill(Color color) {
        for (int x=sampleBounds.min().x(); x<sampleBounds.max().x(); x++) {
            for (int y=sampleBounds.min().y(); y<sampleBounds.max().y(); y++) {
                Pixel p = pixels.get(x, y);
                p.setColor(color, 1);
            }
        }
    }

    public void splat(Point2i point, Color color) {
        if (!croppedBounds.containsExclusive(point)) {
            return;
        }
        Pixel p = pixels.get(point.x(), point.y());
        synchronized (p) {
            p.mergeSplat(color);
        }
    }

    public RenderedImage toImage(double splatScale) {
        BufferedImage image = new BufferedImage(croppedBounds.width(), croppedBounds.height(), BufferedImage.TYPE_INT_RGB);
        croppedBounds.forEach((x,y) -> {
            Pixel p = pixels.get(x, y);
            image.setRGB(x, croppedBounds.height() - y - 1, p.toRgb(splatScale));
        });
        return image;
    }

    public void writeToFile(double splatScale, String path) {
        try {
            ImageIO.write(toImage(splatScale), "png", new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
