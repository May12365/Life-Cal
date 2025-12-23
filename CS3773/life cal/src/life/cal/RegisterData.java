package life.cal;

import java.time.LocalDate;

public class RegisterData {
    public String email;
    public String password;

    public String name;
    public String gender;          // "M"/"F"
    public LocalDate birthdate;
    public double heightCm;
    public double weightKg;
    public Double goalWeightKg;    // อาจเป็น null ได้
    public String activityLevel;   // sedentary/light/moderate/very/extra

    public RegisterData(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
