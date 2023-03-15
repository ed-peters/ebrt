package ebrt.interactions;

import ebrt.Color;
import ebrt.lights.AreaLight;
import ebrt.math.Normal3d;
import ebrt.math.Point3d;
import ebrt.math.Transform;
import ebrt.math.Vector3d;
import ebrt.primitives.Primitive;
import ebrt.shapes.Shape;

public final class SurfaceInteraction extends Interaction {

    public final double u;
    public final double v;

    public final Shape shape;
    private Primitive primitive;
    private Object bsdf;
    private SurfaceDifferentials surfaceDiff;

    public SurfaceInteraction(
            Point3d point,
            Normal3d normal,
            Vector3d wo,
            double t,
            double u,
            double v,
            Shape shape) {
        super(point, normal, wo, t);
        this.u = u;
        this.v = v;
        this.shape = shape;
    }

    public Primitive primitive() {
        return primitive;
    }

    public Object bsdf() {
        return bsdf;
    }

    public void setPrimitive(Primitive primitive) {
        this.primitive = primitive;
    }

    public void setBsdf(Object bsdf) {
        this.bsdf = bsdf;
    }

    public void computeDifferentials(RayPack rays) {
        if (rays.hasDifferentials()) {
            // https://pbr-book.org/3ed-2018/Texture/Sampling_and_Antialiasing#SurfaceInteraction::ComputeDifferentials
            throw new UnsupportedOperationException();
        } else {
            surfaceDiff = SurfaceDifferentials.NONE;
        }
    }

    public void computeScatteringFunctions(RayPack rays, boolean allowMultipleLobes, TransportMode mode) {
        computeDifferentials(rays);
        primitive.computeScatteringFunctions(this, allowMultipleLobes, mode);
    }

    public Color le(Vector3d wo) {
        AreaLight light = primitive.areaLight();
        if (light != null) {
            return light.le(wo);
        }
        return Color.BLACK;
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
        private Shape shape;

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

        public Builder withShape(Shape shape) {
            this.shape = shape;
            return this;
        }

        public SurfaceInteraction build() {
            return new SurfaceInteraction(point, normal, wo, t, u, v, shape);
        }
    }
}
