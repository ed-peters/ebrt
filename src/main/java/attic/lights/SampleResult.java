package attic.lights;

import ebrt.Color;
import attic.math.Vector3d;

public record SampleResult(Color li, Vector3d wi, double pdf, VisibilityTester tester) {
}
