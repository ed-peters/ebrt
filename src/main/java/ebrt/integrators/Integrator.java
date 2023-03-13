package ebrt.integrators;

import ebrt.Scene;
import ebrt.camera.Film;

public interface Integrator {

    void render(Scene scene, Film film);
}
