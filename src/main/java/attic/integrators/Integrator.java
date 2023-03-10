package attic.integrators;

import attic.Scene;
import attic.camera.Film;

public interface Integrator {

    void render(Scene scene, Film film);
}
