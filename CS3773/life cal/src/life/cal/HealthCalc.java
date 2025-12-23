/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package life.cal;

import java.time.LocalDate;
import java.time.Period;
/**
 *
 * @author kitti_yk
 */
public final class HealthCalc {

    private HealthCalc(){}

    public static int age(LocalDate birth) {
        if (birth == null) return 25;
        return Math.max(0, Period.between(birth, LocalDate.now()).getYears());
    }

    /** Mifflin-St Jeor */
    public static double bmr(User u) {
        int a = age(u.birthdate);
        double w = u.weightKg;
        double h = u.heightCm;
        if ("M".equalsIgnoreCase(u.gender)) {
            return 10*w + 6.25*h - 5*a + 5;
        } else {
            return 10*w + 6.25*h - 5*a - 161;
        }
    }

    public static double activityFactor(String act) {
        if (act == null) return 1.2;
        switch (act) {
            case "light":     return 1.375;
            case "moderate":  return 1.55;
            case "very":      return 1.725;
            case "extra":     return 1.9;
            default:          return 1.2; // sedentary
        }
    }

    public static double tdee(User u) {
        return bmr(u) * activityFactor(u.activityLevel);
    }

    public static double bmi(User u) {
        double m = u.heightCm / 100.0;
        return (m <= 0) ? 0 : (u.weightKg / (m*m));
    }

    /** แคลอรี่ที่ควรทาน/วันเพื่อคงน้ำหนัก */
    public static int maintenanceKcal(User u) {
        return (int)Math.round(tdee(u));
    }

    /** เป้าหมายลด/เพิ่ม 0.5 กก./สัปดาห์ ≈ ±500 kcal/วัน */
    public static int targetKcal(User u) {
        if (u.goalWeightKg == null) return maintenanceKcal(u);
        if (u.goalWeightKg < u.weightKg) return maintenanceKcal(u) - 500;
        if (u.goalWeightKg > u.weightKg) return maintenanceKcal(u) + 300;
        return maintenanceKcal(u);
    }
}
