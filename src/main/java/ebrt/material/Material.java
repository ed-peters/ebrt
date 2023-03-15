package ebrt.material;

import ebrt.interactions.SurfaceInteraction;
import ebrt.interactions.TransportMode;

public interface Material {

    void computeScatteringFunctions(SurfaceInteraction interaction, boolean allowMultipleLobes, TransportMode mode);
}
