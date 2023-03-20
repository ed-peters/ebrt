package ebrt.material;

import ebrt.Color;
import ebrt.math.Vector3d;

public interface BXDF {

    Color f(Vector3d wo, Vector3d wi);

}
