package ebrt.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestTransform {

    @Test
    public void testTranslate() {

        Transform t = Transform.translate(1, 2, 3);

        Point3d po = new Point3d(1, 2, 3);
        Point3d pf = new Point3d(2, 4, 6);
        assertTransformWorks(t, po, pf);

        Vector3d vo = new Vector3d(1, 2, 3);
        Vector3d vf = new Vector3d(1, 2, 3);
        assertTransformWorks(t, vo, vf);

        Normal3d no = new Normal3d(1, 2, 3);
        Normal3d nf = new Normal3d(1, 2, 3);
        assertTransformWorks(t, no, nf);
    }

    @Test
    public void testScale() {

        Transform t = Transform.scale(2.0, 3.0, 4.0);

        Point3d po = new Point3d(4, 9, 16);
        Point3d pf = new Point3d(8, 27, 64);
        assertTransformWorks(t, po, pf);

        Vector3d vo = new Vector3d(8, 27, 64);
        Vector3d vf = new Vector3d(16, 81, 256);
        assertTransformWorks(t, vo, vf);
    }

    @Test
    public void testRotate() {
        Transform t = Transform.rotate(Vector3d.Z, 90);

        Point3d po = new Point3d(1, 1, -1);
        Point3d pf = new Point3d(-1, 1, -1);
        assertTransformWorks(t, po, pf);

        Vector3d vo = new Vector3d(1, 1, -1);
        Vector3d vf = new Vector3d(-1, 1, -1);
        assertTransformWorks(t, vo, vf);
    }

    @Test
    public void testCompose() {
        Transform t = Transform.rotate(Vector3d.Z, 90)
                .andThen(Transform.translate(1, 2, 3));

        Point3d po = new Point3d(1, 1, -1);
        Point3d pf = new Point3d(0, 3, 2);
        assertTransformWorks(t, po, pf);

        Vector3d vo = new Vector3d(1, 1, -1);
        Vector3d vf = new Vector3d(-1, 1, -1);
        assertTransformWorks(t, vo, vf);
    }

    private void assertTransformWorks(Transform t, Vector3d o, Vector3d f) {
        assertVector3dEquals("forward fails", f, t.forward(o));
        assertVector3dEquals("reverse fails", o, t.reverse(f));
    }

    private void assertTransformWorks(Transform t, Point3d o, Point3d f) {
        assertPoint3dEquals("forward fails", f, t.forward(o));
        assertPoint3dEquals("reverse fails", o, t.reverse(f));
    }

    private void assertTransformWorks(Transform t, Normal3d o, Normal3d f) {
        assertNormal3dEquals("forward fails", f, t.forward(o));
        assertNormal3dEquals("reverse fails", o, t.reverse(f));
    }

    private void assertNormal3dEquals(String message, Normal3d l, Normal3d r) {
        assertEquals(message+": nx", l.x(), r.x(), 1e-8);
        assertEquals(message+": ny", l.y(), r.y(), 1e-8);
        assertEquals(message+": nz", l.z(), r.z(), 1e-8);
    }

    private void assertVector3dEquals(String message, Vector3d l, Vector3d r) {
        assertEquals(message+": vx", l.x(), r.x(), 1e-8);
        assertEquals(message+": vy", l.y(), r.y(), 1e-8);
        assertEquals(message+": vz", l.z(), r.z(), 1e-8);
    }

    private void assertPoint3dEquals(String message, Point3d l, Point3d r) {
        assertEquals(message+": px", l.x(), r.x(), 1e-8);
        assertEquals(message+": py", l.y(), r.y(), 1e-8);
        assertEquals(message+": pz", l.z(), r.z(), 1e-8);
    }
}
