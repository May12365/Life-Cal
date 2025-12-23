package life.cal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserListView extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField tfSearch;
    private JButton btnSearch, btnRefresh, btnDelete, btnClose;

    public UserListView() {
        setTitle("รายชื่อสมาชิก");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // top: search bar
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("ค้นหา (ชื่อ/อีเมล):"));
        tfSearch = new JTextField(20);
        top.add(tfSearch);

        btnSearch = new JButton("ค้นหา");
        btnSearch.addActionListener(e -> loadUsers(tfSearch.getText().trim()));
        top.add(btnSearch);

        btnRefresh = new JButton("รีเฟรช");
        btnRefresh.addActionListener(e -> { tfSearch.setText(""); loadUsers(""); });
        top.add(btnRefresh);

        btnDelete = new JButton("ลบที่เลือก");
        btnDelete.addActionListener(e -> deleteSelectedUsers());
        top.add(btnDelete);

        add(top, BorderLayout.NORTH);

        // center: table  (เอาวันเกิดออก -> เหลือ 8 คอลัมน์)
        model = new DefaultTableModel(
            new Object[]{"ID","อีเมล","ชื่อ","เพศ","ส่วนสูง(cm)","น้ำหนัก(kg)","เป้าหมาย(kg)","กิจกรรม"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // ตั้งความกว้างให้พอดีกับ 8 คอลัมน์ (index 0..7)
        table.getColumnModel().getColumn(0).setPreferredWidth(60);   // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(180);  // อีเมล
        table.getColumnModel().getColumn(2).setPreferredWidth(120);  // ชื่อ
        table.getColumnModel().getColumn(3).setPreferredWidth(60);   // เพศ
        table.getColumnModel().getColumn(4).setPreferredWidth(100);  // ส่วนสูง
        table.getColumnModel().getColumn(5).setPreferredWidth(100);  // น้ำหนัก
        table.getColumnModel().getColumn(6).setPreferredWidth(110);  // เป้าหมาย
        table.getColumnModel().getColumn(7).setPreferredWidth(100);  // กิจกรรม

        // ลบด้วยปุ่ม Delete บนคีย์บอร์ด
        table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
             .put(KeyStroke.getKeyStroke("DELETE"), "deleteRows");
        table.getActionMap().put("deleteRows", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { deleteSelectedUsers(); }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // bottom: close
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnClose = new JButton("ปิด");
        btnClose.addActionListener(e -> dispose());
        bottom.add(btnClose);
        add(bottom, BorderLayout.SOUTH);

        setSize(900, 420);
        setLocationRelativeTo(null);

        // load initial
        loadUsers("");
    }

    private String thGender(String g) {
        if (g == null) return "";
        return "M".equalsIgnoreCase(g) ? "ชาย" : "หญิง";
    }

    private void loadUsers(String keyword) {
        model.setRowCount(0);
        String base =
            "SELECT id, email, name, gender, height_cm, weight_kg, goal_weight_kg, activity_level " +
            "FROM User ";
        String where = "";
        boolean hasKw = keyword != null && !keyword.isEmpty();
        if (hasKw) where = "WHERE (LOWER(email) LIKE ? OR LOWER(name) LIKE ?) ";
        String order = "ORDER BY id DESC";

        try (PreparedStatement ps = Db.get().prepareStatement(base + where + order)) {
            if (hasKw) {
                String p = "%" + keyword.toLowerCase() + "%";
                ps.setString(1, p);
                ps.setString(2, p);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object id   = rs.getInt("id");
                    Object mail = rs.getString("email");
                    Object name = rs.getString("name");
                    Object gen  = thGender(rs.getString("gender"));
                    Object h    = rs.getObject("height_cm");
                    Object w    = rs.getObject("weight_kg");
                    Object gw   = rs.getObject("goal_weight_kg"); // อาจเป็น null
                    Object act  = rs.getString("activity_level");
                    model.addRow(new Object[]{ id, mail, name, gen, h, w, gw==null? "-" : gw, act });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "โหลดข้อมูลไม่สำเร็จ: " + e.getMessage());
        }
    }

    /** ลบแถวที่เลือก (รองรับหลายแถว) */
    private void deleteSelectedUsers() {
        int[] viewRows = table.getSelectedRows();
        if (viewRows.length == 0) {
            JOptionPane.showMessageDialog(this, "กรุณาเลือกสมาชิกที่ต้องการลบ");
            return;
        }

        // เก็บ id ที่เลือก
        List<Integer> ids = new ArrayList<>();
        for (int r : viewRows) {
            int modelRow = table.convertRowIndexToModel(r);
            Object val = model.getValueAt(modelRow, 0); // ID
            if (val instanceof Number) ids.add(((Number) val).intValue());
        }
        if (ids.isEmpty()) return;

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "ยืนยันลบสมาชิกที่เลือกจำนวน " + ids.size() + " รายการ?\n(ข้อมูลบันทึกอาหาร/ออกกำลังกายของผู้ใช้นี้จะถูกลบด้วย)",
                "ยืนยันการลบ",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        String delMeal = "DELETE FROM MealLog WHERE user_id=?";
        String delEx   = "DELETE FROM ExerciseLog WHERE user_id=?";
        String delUser = "DELETE FROM User WHERE id=?";

        try {
            Connection c = Db.get();
            try (PreparedStatement psMeal = c.prepareStatement(delMeal);
                 PreparedStatement psEx   = c.prepareStatement(delEx);
                 PreparedStatement psUser = c.prepareStatement(delUser)) {

                for (int id : ids) {
                    psMeal.setInt(1, id);  psMeal.executeUpdate();
                    psEx.setInt(1, id);    psEx.executeUpdate();
                    psUser.setInt(1, id);  psUser.executeUpdate();
                }
                c.commit();
            }
            JOptionPane.showMessageDialog(this, "ลบสำเร็จ " + ids.size() + " รายการ");
            loadUsers(tfSearch.getText().trim());
        } catch (SQLException e) {
            try { Db.get().rollback(); } catch (SQLException ignored) {}
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "ลบไม่สำเร็จ: " + e.getMessage());
        }
    }

    // ใช้รันเดี่ยว ๆ ได้
    public static void main(String[] args) {
        try { Db.migrate(); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new UserListView().setVisible(true));
    }
}
