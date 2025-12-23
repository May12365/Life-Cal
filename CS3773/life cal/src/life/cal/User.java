/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package life.cal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
/**
 *
 * @author kitti_yk
 */
public class User {
    public int id;
    public String email;
    public String name;
    public String gender;         // "M" / "F"
    public LocalDate birthdate;   // yyyy-MM-dd
    public double heightCm;
    public double weightKg;
    public Double goalWeightKg;   // nullable
    public String activityLevel;
    
    public static User form(ResultSet rs)throws SQLException{
        User u = new User();
        u.id = rs.getInt("id");
        u.email = rs.getString("email");
        u.name = rs.getString("name");
        u.gender = rs.getString("gender");
        String bd = rs.getString("birthdate");
        u.birthdate = (bd == null || bd.isEmpty()) ? LocalDate.of(2000,1,1) : LocalDate.parse(bd);
        u.heightCm = rs.getObject("height_cm") == null ? 0 : rs.getDouble("height_cm");
        u.weightKg = rs.getObject("weight_kg") == null ? 0 : rs.getDouble("weight_kg");
        u.goalWeightKg = rs.getObject("goal_weight_kg") == null ? null : rs.getDouble("goal_weight_kg");
        u.activityLevel = rs.getString("activity_level");
        return u;
    }
}
