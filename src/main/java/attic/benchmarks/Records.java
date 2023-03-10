package attic.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Threads;

public class Records {

    record V1(double x, double y, double z) {

        public V1 plus(V1 other) {
            return new V1(x + other.x, y + other.y, z + other.z);
        }

        public V1 minus(V1 other) {
            return new V1(x - other.x, y - other.y, z - other.z);
        }

        public double dot(V1 other) {
            return x * other.x + y * other.y + z * other.z;
        }
    }

    static class V2 {

        private final double x;
        private final double y;
        private final double z;

        public V2(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double x() {
            return x;
        }

        public double y() {
            return y;
        }

        public double z() {
            return z;
        }

        public V2 plus(V2 other) {
            return new V2(x + other.x, y + other.y, z + other.z);
        }

        public V2 minus(V2 other) {
            return new V2(x - other.x, y - other.y, z - other.z);
        }

        public double dot(V2 other) {
            return x * other.x + y * other.y + z * other.z;
        }
    }

    interface Ops {
        double x();
        double y();
        double z();
        Ops plus(Ops other);
        Ops minus(Ops other);
        double dot(Ops other);
    }

    static class V3 implements Ops {

        private final double x;
        private final double y;
        private final double z;

        public V3(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double x() {
            return x;
        }

        public double y() {
            return y;
        }

        public double z() {
            return z;
        }

        public V3 plus(Ops other) {
            return new V3(x + other.x(), y + other.y(), z + other.z());
        }

        public V3 minus(Ops other) {
            return new V3(x - other.x(), y - other.y(), z - other.z());
        }

        public double dot(Ops other) {
            return x * other.x() + y * other.y() + z * other.z();
        }
    }

    @Threads(4)
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public static void v1() {
        V1 a = new V1(1, 2, 3);
        V1 b = new V1(2, 3, 4);
        V1 c = new V1(4, 5, 6);
        V1 d = new V1(5, 6, 7);
        a.plus(b).minus(c).dot(d);
    }

    @Threads(4)
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public static void v2() {
        V2 a = new V2(1, 2, 3);
        V2 b = new V2(2, 3, 4);
        V2 c = new V2(4, 5, 6);
        V2 d = new V2(5, 6, 7);
        a.plus(b).minus(c).dot(d);
    }

    @Threads(4)
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public static void v3() {
        V3 a = new V3(1, 2, 3);
        V3 b = new V3(2, 3, 4);
        V3 c = new V3(4, 5, 6);
        V3 d = new V3(5, 6, 7);
        a.plus(b).minus(c).dot(d);
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
}
