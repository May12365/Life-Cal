package life.cal;

public class TestDb {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC"); // ยืนยันว่าไดรเวอร์ถูกโหลด
            System.out.println(" SQLite driver loaded");

            Db.migrate();
            System.out.println(" DB & tables ready at: " + Db.dbPath());

            Db.closeQuietly();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
