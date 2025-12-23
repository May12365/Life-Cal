package life.cal;

import java.sql.*;
import java.time.LocalDate;

public class AuthService {

    public int registerBasic(String email, String password) throws SQLException {
        String salt = PasswordUtil.newSalt();
        String hash = PasswordUtil.hash(password, salt);

        // ค่าเริ่มต้นชั่วคราว (แก้ในหน้าโปรไฟล์ได้ทีหลัง)
        String name = email.split("@")[0];
        String gender = "F";
        String birth = LocalDate.of(2000,1,1).toString();
        double height = 165, weight = 60;
        Double goalWeight = null;
        String activity = "sedentary";

        String sql = "INSERT INTO User(name, gender, birthdate, height_cm, weight_kg, goal_weight_kg, activity_level, email, password_hash, password_salt) " +
                     "VALUES(?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = Db.get().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, gender);
            ps.setString(3, birth);
            ps.setDouble(4, height);
            ps.setDouble(5, weight);
            if (goalWeight == null) ps.setNull(6, Types.REAL); else ps.setDouble(6, goalWeight);
            ps.setString(7, activity);
            ps.setString(8, email.toLowerCase());
            ps.setString(9, hash);
            ps.setString(10, salt);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int id = rs.next()? rs.getInt(1):0;
            Db.get().commit();
            return id;
        } catch (SQLException e) {
            Db.get().rollback();
            if (e.getMessage()!=null && e.getMessage().toLowerCase().contains("unique"))
                throw new SQLException("อีเมลนี้ถูกใช้แล้ว");
            throw e;
        }
    }
    /** สมัครสมาชิกแบบเต็ม ใช้ข้อมูลจาก RegisterData แล้วคืนค่า userId */
    public int registerFull(RegisterData d) throws SQLException {
        String salt = PasswordUtil.newSalt();
        String hash = PasswordUtil.hash(d.password, salt);

        String sql =
            "INSERT INTO User(name, gender, birthdate, height_cm, weight_kg, goal_weight_kg, activity_level, email, password_hash, password_salt) " +
            "VALUES(?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement ps = Db.get().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.name);
            ps.setString(2, d.gender);                          // "M" หรือ "F"
            ps.setString(3, d.birthdate.toString());            // yyyy-MM-dd
            ps.setDouble(4, d.heightCm);
            ps.setDouble(5, d.weightKg);
            if (d.goalWeightKg == null) ps.setNull(6, Types.REAL); else ps.setDouble(6, d.goalWeightKg);
            ps.setString(7, d.activityLevel);                   // sedentary/light/...
            ps.setString(8, d.email.toLowerCase());
            ps.setString(9, hash);
            ps.setString(10, salt);

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                int id = rs.next() ? rs.getInt(1) : 0;         // >> คืนค่า userId
                Db.get().commit();
                return id;
            }
        } catch (SQLException e) {
            Db.get().rollback();
            if (e.getMessage()!=null && e.getMessage().toLowerCase().contains("unique"))
                throw new SQLException("อีเมลนี้ถูกใช้แล้ว");
            throw e;
        }
    }

    // (จะเก็บ registerBasic ไว้ก็ได้ แต่ไม่จำเป็นถ้าใช้ registerFull แล้ว)
    
    public Integer login(String email, String password) throws SQLException {
    String sql = "SELECT id, password_hash, password_salt FROM User WHERE LOWER(email)=?";
    try (PreparedStatement ps = Db.get().prepareStatement(sql)) {
        ps.setString(1, email.toLowerCase());
        try (ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) return null;
            int id = rs.getInt("id");
            String hash = rs.getString("password_hash");
            String salt = rs.getString("password_salt");
            if (hash != null && salt != null && PasswordUtil.verify(password, salt, hash)) {
                return id; // สำเร็จ
            } else {
                return null; // รหัสไม่ตรง
            }
        }
    }
}
    
    public User findUserById(int id) throws SQLException {
        String sql = "SELECT id,email,name,gender,birthdate,height_cm,weight_kg,goal_weight_kg,activity_level FROM User WHERE id=?";
        try (PreparedStatement ps = Db.get().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.id = rs.getInt("id");
                    u.email = rs.getString("email");
                    u.name = rs.getString("name");
                    u.gender = rs.getString("gender");
                    String bd = rs.getString("birthdate");
                    u.birthdate = (bd == null || bd.isEmpty()) ? null : java.time.LocalDate.parse(bd);
                    u.heightCm = rs.getObject("height_cm") == null ? 0 : rs.getDouble("height_cm");
                    u.weightKg = rs.getObject("weight_kg") == null ? 0 : rs.getDouble("weight_kg");
                    u.goalWeightKg = rs.getObject("goal_weight_kg") == null ? null : rs.getDouble("goal_weight_kg");
                    u.activityLevel = rs.getString("activity_level");
                    return u;
                }
            }
        }
        return null;
    }
    
    
    
    /*public User findUserById(int id) throws SQLException {
    String sql = "SELECT id, email, name, gender, birthdate, height_cm, weight_kg, goal_weight_kg, activity_level FROM User WHERE id=?";
    try (PreparedStatement ps = Db.get().prepareStatement(sql)) {
        ps.setInt(1, id);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return User.from(rs);
            return null;
        }
    }
}*/

}
