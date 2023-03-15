package ebrt.camera;

import ebrt.interactions.RayPack;

public record WeightedRay(RayPack ray, double weight) {
}
