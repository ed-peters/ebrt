package attic.camera;

import ebrt.Color;

public class Pixel {

    private Color color = Color.BLACK;
    private Color splat = Color.BLACK;
    private double filterWeightSum = 0;

    public Color color() {
        return color;
    }

    public Color splat() {
        return splat;
    }

    public double filterWeightSum() {
        return filterWeightSum;
    }

    public void setColor(Color update, double filterWeightSum) {
        this.color = update;
        this.filterWeightSum = filterWeightSum;
    }

    public void mergeColor(Color update, double filterWeightSum) {
        this.color = color.plus(update);
        this.filterWeightSum += filterWeightSum;
    }

    public void mergeSplat(Color update) {
        this.splat = splat.plus(update);
    }

    public int toRgb(double splatScale) {
        Color c = Color.BLACK;
        if (filterWeightSum > 0) {
            c = color.div(filterWeightSum);
        }
        if (splatScale > 0) {
            c = c.plus(splat.mul(splatScale));
        }
        return c.toRgb();
    }
}
