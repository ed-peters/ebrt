package ebrt.lights;

import ebrt.Color;
import ebrt.interactions.Ray;

public class GradientLight implements Light {

    private final Color top;
    private final Color bottom;

    public GradientLight(Color top, Color bottom) {
        this.top = top;
        this.bottom = bottom;
    }

    @Override
    public Color le(Ray ray) {
        double t = 0.5 * (ray.direction().normalize().y() + 1.0);
        return top.mul(1.0 - t).plus(bottom.mul(t));
    }

    public static GradientLight sky() {
        return new GradientLight(Color.WHITE, Color.SKY_BLUE);
    }
}
