package ebrt.material;

import ebrt.interactions.SurfaceInteraction;
import ebrt.interactions.TransportMode;

public interface Material {

    BXDF bsdf();

    void computeScatteringFunctions(SurfaceInteraction interaction, boolean allowMultipleLobes, TransportMode mode);
}
