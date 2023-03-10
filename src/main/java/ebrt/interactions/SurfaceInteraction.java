package ebrt.interactions;

import ebrt.Color;
import ebrt.math.Normal3d;
import ebrt.math.Point3d;
import ebrt.math.Transform;
import ebrt.math.Vector3d;

public final class SurfaceInteraction extends Interaction {

    public final double u;
    public final double v;

    public final Object shape;
    private Object primitive;
    private Object bsdf;

    public SurfaceInteraction(
            Point3d point,
            Normal3d normal,
            Vector3d wo,
            double t,
            double u,
            double v,
            Object shape) {
        super(point, normal, wo, t);
        this.u = u;
        this.v = v;
        this.shape = shape;
    }

    public Object primitive() {
        return primitive;
    }

    public Object bsdf() {
        return bsdf;
    }

    public void setPrimitive(Object primitive) {
        this.primitive = primitive;
    }

    public void setBsdf(Object bsdf) {
        this.bsdf = bsdf;
    }

    public void computeScatteringFunctions(Ray ray) {
        throw new UnsupportedOperationException();
    }

    public Color le(Vector3d wo) {
        throw new UnsupportedOperationException();
    }

    public SurfaceInteraction transform(Transform t) {
        return build(this)
                .withPoint(point.transform(t))
                .withNormal(normal.transform(t))
                .build();
    }

    public static Builder build() {
        return new Builder();
    }

    public static Builder build(SurfaceInteraction other) {
        return build()
                .withPoint(other.point)
                .withNormal(other.normal)
                .withWo(other.wo)
                .withT(other.t)
                .withUV(other.u, other.v)
                .withShape(other.shape);
    }

    public static class Builder {

        private Point3d point;
        private Normal3d normal;
        private Vector3d wo;
        private double t;
        private double u;
        private double v;
        private Object shape;

        public Builder withPoint(Point3d point) {
            this.point = point;
            return this;
        }

        public Builder withNormal(Normal3d normal) {
            this.normal = normal;
            return this;
        }

        public Builder withWo(Vector3d wo) {
            this.wo = wo;
            return this;
        }

        public Builder withT(double t) {
            this.t = t;
            return this;
        }

        public Builder withUV(double u, double v) {
            this.u = u;
            this.v = v;
            return this;
        }

        public Builder withShape(Object shape) {
            this.shape = shape;
            return this;
        }

        public SurfaceInteraction build() {
            return new SurfaceInteraction(point, normal, wo, t, u, v, shape);
        }
    }
}
