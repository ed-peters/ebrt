package ebrt.lights;

import ebrt.Color;
import ebrt.Scene;
import ebrt.interactions.Interaction;
import ebrt.interactions.LightCastOnObject;
import ebrt.interactions.Ray;
import ebrt.math.Point2d;

public interface Light {

    Color le(Ray ray);

    LightCastOnObject li(Interaction interaction, Point2d u);

    default void preprocess(Scene scene) { }
}
