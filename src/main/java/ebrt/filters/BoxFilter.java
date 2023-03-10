package ebrt.filters;

import ebrt.math.Point2d;

public class BoxFilter extends Filter {

    public BoxFilter(Point2d radius) {
        super(radius);
    }

    @Override
    public double evaluate(Point2d point) {
        return 1;
    }
}
