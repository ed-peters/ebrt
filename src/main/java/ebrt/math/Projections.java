package ebrt.math;

public class Projections {

    public static Transform screenToRaster(Point2i resolution, Bounds2d screenWindow) {
        Transform t1 = Transform.scale(resolution.x(), resolution.y(), 1);
        Transform t2 = Transform.scale(
                        1 / (screenWindow.max().x() - screenWindow.min().x()),
                        1 / (screenWindow.min().y() - screenWindow.max().y()),
                        1);
        Transform t3 = Transform.translate(-screenWindow.min().x(), -screenWindow.max().y(), 0);
        return Transform.from(Matrix.multiply(Matrix.multiply(t1.matrix(), t2.matrix()), t3.matrix()));
    }

    public static Transform rasterToCamera(Transform screenToRaster, Transform cameraToScreen) {
        return screenToRaster.invert().andThen(cameraToScreen.invert());
    }

    public static Transform orthographic() {
        return orthographic(0, 1);
    }

    public static Transform orthographic(double near, double far) {
        return Transform.scale(1, 1, 1 / (far - near)).andThen(Transform.translate(0, 0, -near));
    }

    public static Transform perspective(double fieldOfView) {
        return perspective(fieldOfView, 1e-2f, 1000);
    }

    public static Transform perspective(double fieldOfView, double near, double far) {
        double x = far / (far - near);
        double [][] m = new double[][]{
                { 1, 0, 0, 0 },
                { 0, 1, 0, 0 },
                { 0, 0, x, -near * x },
                { 0, 0, 1, 0 } };
        double invTanAng = 1 / Math.tan(Math.toRadians(fieldOfView) / 2);
        return Transform.from(m).andThen(Transform.scale(invTanAng, invTanAng, 1));
    }
}
