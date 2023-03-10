package attic.primitives;

import ebrt.interactions.SurfaceInteraction;
import attic.lights.AreaLight;
import attic.material.Material;
import attic.math.Bounds3d;
import attic.math.Ray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PrimitiveList implements Primitive {

    private final List<Primitive> contents;
    private final Bounds3d bounds;

    public PrimitiveList(Collection<? extends Primitive> coll) {
        this.contents = new ArrayList<>(coll);
        this.bounds = Bounds3d.merge(coll.stream().map(Primitive::worldBound).collect(Collectors.toList()));
    }

    @Override
    public Bounds3d worldBound() {
        return bounds;
    }

    @Override
    public AreaLight areaLight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Material material() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SurfaceInteraction intersect(Ray r, double tmax, boolean testAlpha) {

        double bestT = Double.MAX_VALUE;
        SurfaceInteraction bestSi = null;

        for (Primitive prim : contents) {
            SurfaceInteraction si = prim.intersect(r, bestT, testAlpha);
            if (si != null && si.t < bestT) {
                bestT = si.t;
                bestSi = si;
            }
        }

        return bestSi;
    }

    public static PrimitiveList from(Primitive... prim) {
        return new PrimitiveList(Arrays.asList(prim));
    }
}
