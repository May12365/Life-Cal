package life.cal;

import java.sql.*;

/**
 * Db helper สำหรับ SQLite: เชื่อมต่อ + สร้าง/อัปเดตตาราง
 * ต้องมีไลบรารี: org.xerial:sqlite-jdbc (เพิ่ม JAR ใน Project Libraries)
 */
public final class Db {

    private static final String DB_FILE = "life_cal.db";
    private static final String URL = "jdbc:sqlite:" + DB_FILE;
    private static Connection conn;

    private Db() {}

    /* โหลด JDBC driver (กันพลาดบน JDK/IDE รุ่นเก่า) */
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("ไม่พบ SQLite JDBC driver — โปรดเพิ่ม JAR sqlite-jdbc เข้า Libraries", e);
        }
    }

    /** ได้ Connection ตัวเดียวใช้ทั้งแอป */
    public static synchronized Connection get() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(URL);
            conn.setAutoCommit(false);
            try (Statement s = conn.createStatement()) {
                s.execute("PRAGMA foreign_keys = ON;");
            }
        }
        return conn;
    }

    /** เรียกครั้งแรกของแอป: สร้างตาราง + เติมคอลัมน์ที่ขาด */
    public static void migrate() throws SQLException {
        try (Statement st = get().createStatement()) {

            // USER
            st.addBatch(
                "CREATE TABLE IF NOT EXISTS User(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "gender TEXT," +
                "birthdate TEXT," +
                "height_cm REAL," +
                "weight_kg REAL," +
                "goal_weight_kg REAL," +
                "activity_level TEXT," +
                "email TEXT," +             // จะเพิ่ม UNIQUE ด้านล่าง
                "password_hash TEXT," +
                "password_salt TEXT" +
                ");"
            );

            // FOOD
            st.addBatch(
                "CREATE TABLE IF NOT EXISTS Food(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "serving_size REAL NOT NULL," +
                "serving_unit TEXT NOT NULL," +
                "kcal_per_serving REAL NOT NULL," +
                "protein_g REAL DEFAULT 0," +
                "carb_g REAL DEFAULT 0," +
                "fat_g REAL DEFAULT 0," +
                "sugar_g REAL DEFAULT 0," +
                "sodium_mg REAL DEFAULT 0," +
                "category TEXT," +
                "source TEXT" +
                ");"
            );

            // EXERCISE
            st.addBatch(
                "CREATE TABLE IF NOT EXISTS Exercise(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "MET REAL NOT NULL," +
                "category TEXT" +
                ");"
            );

            // MEAL LOG
            st.addBatch(
                "CREATE TABLE IF NOT EXISTS MealLog(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "food_id INTEGER NOT NULL," +
                "logged_at TEXT NOT NULL," +
                "amount REAL NOT NULL," +
                "meal_type TEXT," +
                "kcal_total REAL NOT NULL," +
                "FOREIGN KEY(user_id) REFERENCES User(id)," +
                "FOREIGN KEY(food_id) REFERENCES Food(id)" +
                ");"
            );

            // EXERCISE LOG
            st.addBatch(
                "CREATE TABLE IF NOT EXISTS ExerciseLog(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "exercise_id INTEGER NOT NULL," +
                "start_at TEXT NOT NULL," +
                "duration_min REAL NOT NULL," +
                "kcal_burned REAL NOT NULL," +
                "FOREIGN KEY(user_id) REFERENCES User(id)," +
                "FOREIGN KEY(exercise_id) REFERENCES Exercise(id)" +
                ");"
            );

            // INDEXES
            st.addBatch("CREATE INDEX IF NOT EXISTS idx_meallog_user_time ON MealLog(user_id, logged_at);");
            st.addBatch("CREATE INDEX IF NOT EXISTS idx_exlog_user_time ON ExerciseLog(user_id, start_at);");

            st.executeBatch();
            get().commit();

            // เติมคอลัมน์/ดัชนีล็อกอิน หากตาราง User เดิมยังไม่มี
            ensureColumn("User", "email", "ALTER TABLE User ADD COLUMN email TEXT;");
            ensureColumn("User", "password_hash", "ALTER TABLE User ADD COLUMN password_hash TEXT;");
            ensureColumn("User", "password_salt", "ALTER TABLE User ADD COLUMN password_salt TEXT;");
            // UNIQUE INDEX สำหรับอีเมล
            try (Statement ix = get().createStatement()) {
                ix.execute("CREATE UNIQUE INDEX IF NOT EXISTS idx_user_email ON User(email);");
            }

            get().commit();
            System.out.println("✅ Database ready at: " + dbPath());

        } catch (SQLException e) {
            get().rollback();
            throw e;
        }
    }

    /** ปิดคอนเนกชันอย่างเงียบ ๆ */
    public static synchronized void closeQuietly() {
        try { if (conn != null && !conn.isClosed()) conn.close(); }
        catch (Exception ignored) {}
    }

    /** ตรวจว่าคอลัมน์มีหรือยัง ถ้าไม่มีก็ ALTER เพิ่มให้ */
    private static void ensureColumn(String table, String column, String alterSql) throws SQLException {
        if (!columnExists(table, column)) {
            try (Statement s = get().createStatement()) {
                s.execute(alterSql);
            }
        }
    }

    private static boolean columnExists(String table, String column) throws SQLException {
        String sql = "PRAGMA table_info(" + table + ")";
        try (Statement s = get().createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                String name = rs.getString("name");
                if (name != null && name.equalsIgnoreCase(column)) return true;
            }
        }
        return false;
    }

    /** path จริงของไฟล์ .db (ไว้เช็ค/ดีบัก) */
    public static String dbPath() throws SQLException {
        try (Statement s = get().createStatement();
             ResultSet rs = s.executeQuery("PRAGMA database_list;")) {
            while (rs.next()) {
                if ("main".equals(rs.getString("name"))) {
                    return rs.getString("file");
                }
            }
        }
        return DB_FILE;
    }
}
