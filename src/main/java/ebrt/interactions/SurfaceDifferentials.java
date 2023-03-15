package ebrt.interactions;

import ebrt.math.Vector3d;

public record SurfaceDifferentials (
        double dudx,
        double dvdx,
        double dudy,
        double dvdy,
        Vector3d dpdx,
        Vector3d dpdy) {

    public static SurfaceDifferentials NONE = new SurfaceDifferentials(0, 0, 0, 0, null, null);

}
