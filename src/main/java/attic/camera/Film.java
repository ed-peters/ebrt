package attic.camera;

import ebrt.Color;
import attic.filters.BoxFilter;
import attic.filters.Filter;
import attic.math.Bounds2d;
import attic.math.Bounds2i;
import attic.math.Point2d;
import attic.math.Point2i;

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
    private final Bounds2d physicalExtent;
    private final List<Pixel> pixels;

    public Film(Point2i resolution) {
        this.resolution = resolution;
        this.diagonal = 0.3;
        this.filter = new BoxFilter(Point2d.ONESIES);
        this.croppedBounds = new Bounds2i(Point2i.ORIGIN, resolution);
        this.sampleBounds = computeSampleBounds();
        this.physicalExtent = computePhysicalExtent();
        this.pixels = makePixels(resolution.x() * resolution.y());
    }

    public Film(Point2i resolution, double diagonal, Filter filter, Bounds2d crop, double scale) {
        this.resolution = resolution;
        this.diagonal = diagonal * 0.001;
        this.filter = filter;
        this.croppedBounds = crop.mul(resolution).ceil();
        this.sampleBounds = computeSampleBounds();
        this.physicalExtent = computePhysicalExtent();
        this.pixels = makePixels(resolution.x() * resolution.y());
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

    private List<Pixel> makePixels(int count) {
        List<Pixel> l = new ArrayList<>(count);
        for (int i=0; i<count; i++) {
            l.add(new Pixel());
        }
        return l;
    }

    public FilmTile makeTile(Bounds2i sampleBounds) {
        Point2i p0 = sampleBounds.min().minus(Point2d.HALVSIES).minus(filter.radius()).ceil();
        Point2i p1 = sampleBounds.max().minus(Point2d.HALVSIES).plus(filter.radius()).plus(Point2d.ONESIES).floor();
        Bounds2i tileBounds = new Bounds2i(p0, p1).intersect(croppedBounds);
        return new FilmTile(tileBounds, filter);
    }

    private int pixelIndex(int x, int y) {
        return resolution.x() * y + x;
    }

    private Pixel getPixel(int x, int y) {
        return pixels.get(pixelIndex(x, y));
    }

    public void mergeTile(FilmTile tile) {
        tile.forEach((x, y, c, w) -> {
            Pixel p = getPixel(x, y);
            synchronized (p) {
                p.mergeColor(c, w);
            }
        });
    }

    public void fill(Color [] colors) {
        if (colors.length != pixels.size()) {
            throw new IllegalArgumentException("wrong number of sample colors");
        }
        for (int i=0; i<colors.length; i++) {
            Pixel p = pixels.get(i);
            p.setColor(colors[i], 1);
        }
    }

    public void splat(Point2i point, Color color) {
        if (!croppedBounds.containsExclusive(point)) {
            return;
        }
        Pixel p = getPixel(point.x(), point.y());
        synchronized (p) {
            p.mergeSplat(color);
        }
    }

    public RenderedImage toImage(double splatScale) {
        BufferedImage image = new BufferedImage(croppedBounds.width(), croppedBounds.height(), BufferedImage.TYPE_INT_RGB);
        croppedBounds.forEach((x,y) -> {
            Pixel p = getPixel(x, y);
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
