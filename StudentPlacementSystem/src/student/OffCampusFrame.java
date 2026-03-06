package student;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import dbconnection.DBConnection;

public class OffCampusFrame extends JFrame {

    private final Color clr_blue       = new Color(0, 102, 153);
    private final Color clr_white      = Color.WHITE;
    private final Color clr_bg         = new Color(245, 248, 252);
    private final Color clr_cardBorder = new Color(220, 230, 240);
    private final Color clr_textDark   = new Color(40, 40, 40);

    private final StudentDashboardFrame dashboard;
    private final int studentId;

    private JButton jb_logout;
    private JButton jb_back;

    private DefaultTableModel jt_model;
    private JTable jt_offCampus;

    public OffCampusFrame(StudentDashboardFrame dashboard, int studentId) {
        super("Off-Campus Applications");
        this.dashboard = dashboard;
        this.studentId = studentId;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);
        getContentPane().setBackground(clr_bg);

        // HEADER
        JPanel jp_header = new JPanel(new BorderLayout());
        jp_header.setBackground(clr_blue);
        jp_header.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel jp_headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        jp_headerLeft.setBackground(clr_blue);
        JLabel jl_headerTitle = new JLabel("Off-Campus Applications");
        jl_headerTitle.setForeground(clr_white);
        jl_headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jp_headerLeft.add(jl_headerTitle);

        JPanel jp_headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        jp_headerRight.setBackground(clr_blue);
        JLabel jl_userInfo = new JLabel(dashboard.studentName + "  ·  " + dashboard.studentIdStr);
        jl_userInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_userInfo.setForeground(new Color(190, 220, 240));

        jb_logout = new JButton("Logout");
        jb_logout.setFocusPainted(false);
        jb_logout.setBackground(new Color(0, 140, 205));
        jb_logout.setForeground(clr_white);
        jb_logout.setBorderPainted(false);
        jb_logout.setOpaque(true);
        jb_logout.setPreferredSize(new Dimension(110, 34));
        jb_logout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jb_logout.setFont(new Font("Segoe UI", Font.BOLD, 13));

        jp_headerRight.add(jl_userInfo);
        jp_headerRight.add(jb_logout);
        jp_header.add(jp_headerLeft, BorderLayout.WEST);
        jp_header.add(jp_headerRight, BorderLayout.EAST);
        add(jp_header, BorderLayout.NORTH);

        // CONTENT
        JPanel jp_content = new JPanel(new BorderLayout());
        jp_content.setBackground(clr_bg);
        jp_content.setBorder(new EmptyBorder(16, 18, 18, 18));

        JPanel jp_topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_topBar.setBackground(clr_bg);
        jp_topBar.setBorder(new EmptyBorder(0, 0, 14, 0));

        jb_back = new JButton("← Back");
        jb_back.setFocusPainted(false);
        jb_back.setBackground(clr_white);
        jb_back.setForeground(clr_textDark);
        jb_back.setBorder(new LineBorder(clr_cardBorder, 1));
        jb_back.setOpaque(true);
        jb_back.setPreferredSize(new Dimension(90, 32));
        jb_back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jb_back.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jp_topBar.add(jb_back);

        jt_model = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        jt_model.setColumnIdentifiers(new String[]{ "Company Name", "Title", "Applied On", "Status" });

        jt_offCampus = new JTable(jt_model);
        jt_offCampus.setRowHeight(30);
        jt_offCampus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jt_offCampus.setGridColor(new Color(230, 230, 230));
        jt_offCampus.setSelectionBackground(new Color(210, 225, 245));
        jt_offCampus.setSelectionForeground(new Color(40, 40, 40));
        jt_offCampus.getTableHeader().setUI(new javax.swing.plaf.basic.BasicTableHeaderUI());
        jt_offCampus.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        jt_offCampus.getTableHeader().setBackground(clr_blue);
        jt_offCampus.getTableHeader().setForeground(clr_white);
        jt_offCampus.getTableHeader().setOpaque(true);
        jt_offCampus.getTableHeader().setReorderingAllowed(false);
        refreshTable();

        JScrollPane jp_scroll = new JScrollPane(jt_offCampus);
        jp_scroll.setBorder(BorderFactory.createEmptyBorder());

        jp_content.add(jp_topBar,  BorderLayout.NORTH);
        jp_content.add(jp_scroll,  BorderLayout.CENTER);
        add(jp_content, BorderLayout.CENTER);

        // LISTENERS
        jb_logout.addActionListener(new LogoutListener());
        jb_back.addActionListener(new BackListener());
    }

    // LOGOUT LISTENER
    private class LogoutListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int r = JOptionPane.showConfirmDialog(OffCampusFrame.this,
                    "Do you want to log out?", "Logout", JOptionPane.OK_CANCEL_OPTION);
            if (r == JOptionPane.OK_OPTION) System.exit(0);
        }
    }

    // BACK LISTENER
    private class BackListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            dashboard.returnToDashboard();
        }
    }

    // REFRESH TABLE
    private void refreshTable() {
        jt_model.setRowCount(0);
        String sql =
            "SELECT companyName, jobTitle, appliedDate, status " +
            "FROM offCampusApplication WHERE studentId = ? " +
            "ORDER BY appliedDate DESC";
        try (Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jt_model.addRow(new Object[]{
                    rs.getString("companyName"),
                    rs.getString("jobTitle"),
                    rs.getString("appliedDate"),
                    rs.getString("status")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}