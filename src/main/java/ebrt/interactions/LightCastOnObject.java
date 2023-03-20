package ebrt.interactions;

import ebrt.Color;
import ebrt.lights.VisibilityTester;
import ebrt.math.Vector3d;

public record LightCastOnObject(Color color, Vector3d direction, double pdf, VisibilityTester tester) {
}
