package ebrt.media;

import ebrt.Color;
import ebrt.interactions.Ray;
import ebrt.samplers.Sampler;

public interface Medium {

    Color tr(Ray ray, Sampler sampler);
}
