package attic.camera;

import ebrt.Color;
import attic.filters.Filter;
import attic.math.Bounds2i;
import attic.math.Point2d;
import attic.math.Point2i;

public class FilmTile {

    private final Bounds2i bounds;
    private final Filter filter;
    private final double [] filterTable;
    private final Color [] colors;
    private final double [] weights;

    public FilmTile(Bounds2i sampleBounds, Filter filter) {
        Point2i p0 = sampleBounds.min().minus(Point2d.HALVSIES).minus(filter.radius()).ceil();
        Point2i p1 = sampleBounds.max().minus(Point2d.HALVSIES).plus(filter.radius()).plus(Point2d.ONESIES).floor();
        this.bounds = new Bounds2i(p0, p1);
        this.filter = filter;
        this.filterTable = filter.makeTable();
        this.weights = new double[bounds.area()];
        this.colors = Color.arrayOfBlack(bounds.area());
    }

    public Bounds2i bounds() {
        return bounds;
    }

    public void forEach(FilmTilePixelConsumer consumer) {
        bounds.forEach((x,y) -> {
            int offset = computePixelOffset(x, y);
            consumer.accept(x, y, colors[offset], weights[offset]);
        });
    }

    public void addSample(Point2d point, Color color, double sampleWeight) {

        // determine which pixels this sample will apply to
        Point2d discretePixel = point.minus(Point2d.HALVSIES);
        Point2i p0 = discretePixel.minus(filter.radius()).ceil().maxWith(bounds.min());
        Point2i p1 = discretePixel.plus(filter.radius()).plus(Point2d.ONESIES).floor().minWith(bounds.max());
        Bounds2i b = new Bounds2i(p0, p1);

        // determine offsets into the filter table
        int [] filterOffsetX = computerFilterTableOffset(discretePixel.x(), p0.x(), p1.x(), filter.inverseRadius().x());
        int [] filterOffsetY = computerFilterTableOffset(discretePixel.y(), p0.y(), p1.y(), filter.inverseRadius().y());

        // apply sample by summing
        b.forEach((x,y) -> {
            int offset = filterOffsetY[y - p0.y()] * filterTable.length + filterOffsetX[x - p0.x()];
            double filterWeight = filterTable[offset];
            int pixelOffset = computePixelOffset(x, y);
            colors[pixelOffset] = colors[pixelOffset].plus(color.mul(sampleWeight * filterWeight));
            weights[pixelOffset] += filterWeight;
        });
    }

    private int [] computerFilterTableOffset(double pd, int p0, int p1, double ifr) {
        int [] offset = new int[p1-p0];
        for (int i=p0; i<p1; i++) {
            double f = Math.abs((i - pd) * ifr / filterTable.length);
            offset[i-p0] = Math.min((int)Math.floor(f), filterTable.length - 1);
        }
        return offset;
    }

    private int computePixelOffset(int x, int y) {
        int width = bounds.max().x() - bounds.min().x();
        return (x - bounds.min().x()) + (y - bounds.min().y()) * width;
    }

    public interface FilmTilePixelConsumer {

        void accept(int x, int y, Color c, double w);
    }
}
