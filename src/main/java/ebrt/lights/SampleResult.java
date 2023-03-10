package ebrt.lights;

import ebrt.Color;
import ebrt.math.Vector3d;

public record SampleResult(Color li, Vector3d wi, double pdf, VisibilityTester tester) {
}
