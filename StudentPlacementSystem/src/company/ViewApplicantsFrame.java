package company;

import dbconnection.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewApplicantsFrame extends JFrame {

    private final Color clr_blue       = new Color(0, 102, 153);
    private final Color clr_white      = Color.WHITE;
    private final Color clr_bg         = new Color(245, 247, 250);
    private final Color clr_cardBorder = new Color(220, 225, 230);
    private final Color clr_textDark   = new Color(40, 40, 40);

    private final String companyName;
    private final String companyId;
    private final CompanyDashboardFrame dashboard;

    private DefaultTableModel tm_applicants;

    public ViewApplicantsFrame(String companyName, String companyId, CompanyDashboardFrame dashboard) {
        super("CareerConnect - View Applicants");
        this.companyName = companyName;
        this.companyId   = companyId == null ? "1" : companyId.replaceAll("[^0-9]", "");
        this.dashboard   = dashboard;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 750));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // HEADER
        JPanel jp_header = new JPanel(new BorderLayout());
        jp_header.setBackground(clr_blue);
        jp_header.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel jp_headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        jp_headerLeft.setBackground(clr_blue);
        JLabel jl_title = new JLabel("View Applicants");
        jl_title.setForeground(clr_white);
        jl_title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jp_headerLeft.add(jl_title);

        JPanel jp_headerRightInner = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        jp_headerRightInner.setBackground(clr_blue);

        java.net.URL profileUrl = getClass().getResource("/images/profile.png");
        JLabel jl_profileIcon = new JLabel();
        if (profileUrl != null) {
            Image scaled = new ImageIcon(profileUrl).getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
            jl_profileIcon = new JLabel(new ImageIcon(scaled));
        }
        JLabel jl_userInfo = new JLabel(companyName + "  ·  COM" + companyId);
        jl_userInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_userInfo.setForeground(clr_white);
        jp_headerRightInner.add(jl_profileIcon);
        jp_headerRightInner.add(jl_userInfo);

        JPanel jp_headerRight = new JPanel(new BorderLayout());
        jp_headerRight.setBackground(clr_blue);
        jp_headerRight.add(jp_headerRightInner, BorderLayout.CENTER);

        jp_header.add(jp_headerLeft, BorderLayout.WEST);
        jp_header.add(jp_headerRight, BorderLayout.EAST);
        add(jp_header, BorderLayout.NORTH);

        // CONTENT
        JPanel jp_content = new JPanel(new BorderLayout());
        jp_content.setBackground(clr_bg);
        jp_content.setBorder(new EmptyBorder(16, 18, 18, 18));

        JPanel jp_topBar = new JPanel(new BorderLayout());
        jp_topBar.setBackground(clr_bg);
        jp_topBar.setBorder(new EmptyBorder(0, 0, 14, 0));

        JButton jb_back = new JButton("← Back");
        jb_back.setFocusPainted(false);
        jb_back.setBackground(clr_white);
        jb_back.setForeground(clr_textDark);
        jb_back.setBorder(new LineBorder(clr_cardBorder, 1));
        jb_back.setOpaque(true);
        jb_back.setPreferredSize(new Dimension(90, 32));
        jb_back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jb_back.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jb_back.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                jb_back.setBackground(new Color(230, 235, 245));
            }
            public void mouseExited(MouseEvent e) {
                jb_back.setBackground(clr_white);
            }
        });
        jb_back.addActionListener(new BackHandler());
        jp_topBar.add(jb_back, BorderLayout.WEST);

        String[] cols = {"Posting Title", "Name", "Course", "Status"};
        tm_applicants = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable jt_applicants = new JTable(tm_applicants);
        jt_applicants.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jt_applicants.setRowHeight(30);
        jt_applicants.setBackground(clr_white);
        jt_applicants.setForeground(clr_textDark);
        jt_applicants.setGridColor(new Color(240, 244, 248));
        jt_applicants.setSelectionBackground(new Color(210, 230, 250));
        jt_applicants.setSelectionForeground(clr_textDark);

        JTableHeader th = jt_applicants.getTableHeader();
        th.setBackground(clr_blue);
        th.setForeground(clr_white);
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setPreferredSize(new Dimension(0, 36));
        th.setReorderingAllowed(false);

        JScrollPane sp = new JScrollPane(jt_applicants);
        sp.setBorder(new LineBorder(clr_cardBorder, 1));
        sp.getViewport().setBackground(new Color(240, 244, 248));

        jp_content.add(jp_topBar, BorderLayout.NORTH);
        jp_content.add(sp, BorderLayout.CENTER);
        add(jp_content, BorderLayout.CENTER);

        // Load data
        tm_applicants.setRowCount(0);
        String sql =
                "SELECT rp.title AS posting, " +
                "s.fullName AS name, s.course AS course, a.status AS status " +
                "FROM application a " +
                "JOIN recruitmentPosting rp ON a.postingId = rp.postingId " +
                "JOIN company c ON rp.companyId = c.companyId " +
                "JOIN student s ON a.studentId = s.studentId " +
                "WHERE c.companyId = ? ORDER BY rp.postingId, a.appliedOn DESC";

        int count = 0;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(companyId));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tm_applicants.addRow(new Object[]{
                            rs.getString("posting"),
                            rs.getString("name"),
                            rs.getString("course"),
                            rs.getString("status")
                    });
                    count++;
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "DB error while loading applicants:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        if (count == 0) {
            tm_applicants.addRow(new Object[]{"-", "No applicants found", "-", "-"});
        }
    }

    // BACK ACTION LISTENER
    private class BackHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dashboard.refreshStats();
            dashboard.setVisible(true);
            dispose();
        }
    }
}