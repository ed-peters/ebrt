package attic.reflection;

import ebrt.Color;
import attic.math.Point2d;
import attic.math.Vector3d;

import java.util.EnumSet;
import java.util.List;

public interface BXDF {

    enum Type {
        REFLECTION,
        TRANSMISSION,
        SPECULAR
    }

    EnumSet<Type> SPECULAR_REFLECTION = EnumSet.of(Type.REFLECTION, Type.SPECULAR);
    EnumSet<Type> SPECULAR_TRANSMISSION = EnumSet.of(Type.TRANSMISSION, Type.SPECULAR);

    EnumSet<Type> getType();

    Color f(Vector3d wo, Vector3d wi);

    record SampleF(Vector3d wi, double pdf, Color color) {

        public SampleF mul(Color factor) {
            return new SampleF(wi, pdf, factor.mul(color));
        }
    }

    SampleF sampleF(Vector3d wo, Point2d sample, EnumSet<Type> type);

    /*
    virtual Spectrum rho(const Vector3f &wo, int nSamples,
                         const Point2f *samples) const;
    virtual Spectrum rho(int nSamples, const Point2f *samples1,
                         const Point2f *samples2) const;
    virtual Float Pdf(const Vector3f &wo, const Vector3f &wi) const;
     */

    record SampleRho(Color color, List<Point2d> samples1, List<Point2d> samples2) {

        public SampleRho mul(Color factor) {
            return new SampleRho(factor.mul(color), samples1, samples2);
        }
    }

    SampleRho rho(int sampleCount);

    SampleRho rho(Vector3d wo, int sampleCount);

    static BXDF scale(BXDF target, Color factor) {
        return new BXDF() {
            @Override
            public EnumSet<Type> getType() {
                return target.getType();
            }

            @Override
            public Color f(Vector3d wo, Vector3d wi) {
                return factor.mul(target.f(wo, wi));
            }

            @Override
            public SampleF sampleF(Vector3d wo, Point2d sample, EnumSet<Type> type) {
                return target.sampleF(wo, sample, type).mul(factor);
            }

            @Override
            public SampleRho rho(int sampleCount) {
                return target.rho(sampleCount).mul(factor);
            }

            @Override
            public SampleRho rho(Vector3d wo, int sampleCount) {
                return target.rho(wo, sampleCount).mul(factor);
            }
        };
    }
}
