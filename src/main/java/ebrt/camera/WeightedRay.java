package ebrt.camera;

import ebrt.interactions.RayDifferential;

public record WeightedRay(RayDifferential ray, double weight) {
}
