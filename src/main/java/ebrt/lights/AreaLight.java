package ebrt.lights;

import ebrt.Color;
import ebrt.math.Vector3d;

public interface AreaLight {
    Color le(Vector3d wo);
}
