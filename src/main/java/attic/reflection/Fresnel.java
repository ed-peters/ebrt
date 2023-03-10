package attic.reflection;

import ebrt.Color;
import attic.math.Utils;

public interface Fresnel {

    Color evaluate(double cosI);

    static Fresnel dialectric(double etaI, double etaT) {
        return (cosI) -> Color.from(dialectric(cosI, etaI, etaT));
    }

    static Fresnel conductor(Color etai, Color etat, Color k) {
        return (cosI) -> conductor(cosI, etai, etat, k);
    }

    static Fresnel noop() {
        return (cosI) -> Color.WHITE;
    }

    static double dialectric(double cosThetaI, double etaI, double etaT) {

        cosThetaI = Utils.clamp(cosThetaI, -1, 1);

        boolean entering = cosThetaI > 0.f;
        if (!entering) {
            double tmp = etaI;
            etaI = etaT;
            etaT = tmp;
            cosThetaI = Math.abs(cosThetaI);
        }

        double sinThetaI = Math.sqrt(Math.max(0, 1 - cosThetaI * cosThetaI));
        double sinThetaT = etaI / etaT * sinThetaI;
        if (sinThetaT >= 1) {
            return 1;
        }

        double cosThetaT = Math.sqrt(Math.max(0, 1 - sinThetaT * sinThetaT));

        double r_para = ((etaT * cosThetaI) - (etaI * cosThetaT)) / ((etaT * cosThetaI) + (etaI * cosThetaT));
        double r_perp = ((etaI * cosThetaI) - (etaT * cosThetaT)) / ((etaI * cosThetaI) + (etaT * cosThetaT));
        return (r_para * r_para + r_perp * r_perp) / 2;
    }

    static Color conductor(double cosThetaI, Color etai, Color etat, Color k) {

        cosThetaI = Utils.clamp(cosThetaI, -1, 1);

        double cosThetaI2 = cosThetaI * cosThetaI;
        double sinThetaI2 = 1 - cosThetaI2;
        Color eta2 = etat.div(etai).square();
        Color etak2 = k.div(etai).square();

        Color t0 = eta2.minus(etak2).minus(sinThetaI2);
        Color a2plusb2 = t0.mul(t0).minus(eta2.mul(etak2).mul(4));
        Color t1 = a2plusb2.plus(cosThetaI2);
        Color a = a2plusb2.plus(t0).mul(0.5).sqrt();
        Color t2 = a.mul(2 * cosThetaI);
        Color rs = t1.minus(t2).div(t1.plus(t2));
        Color t3 = a2plusb2.mul(cosThetaI2).plus(sinThetaI2 * sinThetaI2);
        Color t4 = t2.mul(sinThetaI2);
        Color rp = rs.mul(t3.minus(t4).div(t3.plus(t4)));
        return rp.plus(rs).mul(0.5);
    }
}
