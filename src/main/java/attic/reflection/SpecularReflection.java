package attic.reflection;

import ebrt.Color;
import attic.math.Point2d;
import attic.math.Vector3d;

import java.util.EnumSet;

public class SpecularReflection implements BXDF {

    public static final EnumSet<Type> TYPE = EnumSet.of(Type.REFLECTION, Type.SPECULAR);

    private final Fresnel fresnel;
    private final Color r;

    public SpecularReflection(Fresnel fresnel, Color r) {
        this.fresnel = fresnel;
        this.r = r;
    }

    @Override
    public EnumSet<Type> getType() {
        return TYPE;
    }

    @Override
    public Color f(Vector3d wo, Vector3d wi) {
        return Color.BLACK;
    }

    @Override
    public SampleF sampleF(Vector3d wo, Point2d sample, EnumSet<Type> type) {
        Vector3d wi = new Vector3d(-wo.x(), -wo.y(), wo.z());
        Color c = fresnel.evaluate(wi.cosTheta()).mul(r).div(wi.absCosTheta());
        return new SampleF(wi, 1, c);
    }

    @Override
    public SampleRho rho(int sampleCount) {
        return null;
    }

    @Override
    public SampleRho rho(Vector3d wo, int sampleCount) {
        return null;
    }
}
