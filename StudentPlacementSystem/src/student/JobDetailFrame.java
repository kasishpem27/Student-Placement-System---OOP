package student;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

import dbconnection.DBConnection;

public class JobDetailFrame extends JFrame {

    private final Color PRIMARY_BLUE = new Color(0, 102, 153);
    private final Color ACCEPT_GREEN = new Color(34, 139, 34);
    private final Color REJECT_RED   = new Color(211, 47, 47);
    private final Color TEXT_MUTED   = new Color(90, 90, 90);
    private final Color TEXT_DARK    = new Color(40, 40, 40);
    private final Color CARD_BORDER  = new Color(220, 230, 240);

    private final JobPostingsFrame jobPostingsFrame;
    private final StudentDashboardFrame dashboard;
    private final int studentId;
    private final int postingId;
    private final String company;
    private final String role;
    private final String status;

    public JobDetailFrame(JobPostingsFrame jobPostingsFrame, StudentDashboardFrame dashboard,
                          int studentId, int postingId, String company, String role, String status) {
        super(company + " — " + role);
        this.jobPostingsFrame = jobPostingsFrame;
        this.dashboard        = dashboard;
        this.studentId        = studentId;
        this.postingId        = postingId;
        this.company          = company;
        this.role             = role;
        this.status           = status;

        setSize(520, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // LOAD DB DATA
        String[] details = loadPostingDetails();
        if (details == null) return; // DB error shown

        String description  = details[0];
        String location     = details[1];
        String deadline     = details[2];
        String type         = details[3];
        String money        = details[4];
        String workingHours = details[5];
        String duration     = details[6];

        // TOP INFO
        JPanel jp_topInfo = new JPanel(new GridLayout(3, 1, 0, 6));
        jp_topInfo.setBackground(Color.WHITE);
        jp_topInfo.setBorder(new EmptyBorder(20, 24, 12, 24));

        JLabel jl_company = new JLabel(company);
        jl_company.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jl_company.setForeground(PRIMARY_BLUE);

        JLabel jl_role = new JLabel(role + (type.isEmpty() ? "" : "  [" + type + "]"));
        jl_role.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jl_role.setForeground(TEXT_MUTED);

        JPanel jp_badgeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_badgeRow.setBackground(Color.WHITE);
        JLabel jl_badge = new JLabel("  " + status + "  ");
        jl_badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        jl_badge.setForeground(Color.WHITE);
        jl_badge.setBackground("Open".equals(status) ? ACCEPT_GREEN : REJECT_RED);
        jl_badge.setOpaque(true);
        jl_badge.setBorder(new EmptyBorder(3, 6, 3, 6));
        jp_badgeRow.add(jl_badge);

        jp_topInfo.add(jl_company);
        jp_topInfo.add(jl_role);
        jp_topInfo.add(jp_badgeRow);

        // DETAILS GRID
        JPanel jp_grid = new JPanel(new GridLayout(0, 2, 10, 8));
        jp_grid.setBackground(new Color(248, 250, 255));
        jp_grid.setBorder(new EmptyBorder(12, 24, 12, 24));

        JLabel jl_salaryLabel = new JLabel("Salary / Rs:");
        jl_salaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jl_salaryLabel.setForeground(TEXT_MUTED);
        JLabel jl_salaryValue = new JLabel(money);
        jl_salaryValue.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jl_salaryValue.setForeground(TEXT_DARK);
        jp_grid.add(jl_salaryLabel); jp_grid.add(jl_salaryValue);

        JLabel jl_locationLabel = new JLabel("Location:");
        jl_locationLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jl_locationLabel.setForeground(TEXT_MUTED);
        JLabel jl_locationValue = new JLabel(location);
        jl_locationValue.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jl_locationValue.setForeground(TEXT_DARK);
        jp_grid.add(jl_locationLabel); jp_grid.add(jl_locationValue);

        JLabel jl_deadlineLabel = new JLabel("Deadline:");
        jl_deadlineLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jl_deadlineLabel.setForeground(TEXT_MUTED);
        JLabel jl_deadlineValue = new JLabel(deadline);
        jl_deadlineValue.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jl_deadlineValue.setForeground(TEXT_DARK);
        jp_grid.add(jl_deadlineLabel); jp_grid.add(jl_deadlineValue);

        if (!"—".equals(workingHours)) {
            JLabel jl_whLabel = new JLabel("Working Hours:");
            jl_whLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            jl_whLabel.setForeground(TEXT_MUTED);
            JLabel jl_whValue = new JLabel(workingHours);
            jl_whValue.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            jl_whValue.setForeground(TEXT_DARK);
            jp_grid.add(jl_whLabel); jp_grid.add(jl_whValue);
        }

        if ("Placement".equals(type) && !"—".equals(duration)) {
            JLabel jl_durLabel = new JLabel("Duration:");
            jl_durLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            jl_durLabel.setForeground(TEXT_MUTED);
            JLabel jl_durValue = new JLabel(duration);
            jl_durValue.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            jl_durValue.setForeground(TEXT_DARK);
            jp_grid.add(jl_durLabel); jp_grid.add(jl_durValue);
        }

        // DESCRIPTION
        JTextArea jta_desc = new JTextArea("Job Description:\n" + description);
        jta_desc.setEditable(false);
        jta_desc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jta_desc.setBackground(Color.WHITE);
        jta_desc.setLineWrap(true);
        jta_desc.setWrapStyleWord(true);

        JScrollPane jp_descScroll = new JScrollPane(jta_desc);
        jp_descScroll.setBorder(new LineBorder(CARD_BORDER, 1));

        JPanel jp_center = new JPanel(new BorderLayout());
        jp_center.setBackground(Color.WHITE);
        jp_center.setBorder(new EmptyBorder(0, 24, 12, 24));
        jp_center.add(jp_grid,       BorderLayout.NORTH);
        jp_center.add(jp_descScroll, BorderLayout.CENTER);

        JPanel jp_body = new JPanel(new BorderLayout());
        jp_body.setBackground(Color.WHITE);
        jp_body.add(jp_topInfo, BorderLayout.NORTH);
        jp_body.add(jp_center,  BorderLayout.CENTER);

        JScrollPane jp_bodyScroll = new JScrollPane(jp_body);
        jp_bodyScroll.setBorder(null);
        add(jp_bodyScroll, BorderLayout.CENTER);

        // FOOTER
        JPanel jp_footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        jp_footer.setBackground(new Color(245, 247, 250));
        jp_footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, CARD_BORDER));

        if (hasApplied()) {
            JLabel jl_already = new JLabel("Already Applied");
            jl_already.setForeground(ACCEPT_GREEN);
            jl_already.setFont(new Font("Segoe UI", Font.BOLD, 13));
            jp_footer.add(jl_already);
        } else if ("Closed".equals(status)) {
            JLabel jl_closed = new JLabel("Recruitment Closed");
            jl_closed.setForeground(TEXT_MUTED);
            jl_closed.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            jp_footer.add(jl_closed);
        } else {
            JButton jb_applyNow = new JButton("Apply Now");
            jb_applyNow.setBackground(ACCEPT_GREEN);
            jb_applyNow.setForeground(Color.WHITE);
            jb_applyNow.setFont(new Font("Segoe UI", Font.BOLD, 13));
            jb_applyNow.setFocusPainted(false);
            jb_applyNow.setBorderPainted(false);
            jb_applyNow.setOpaque(true);
            jb_applyNow.setCursor(new Cursor(Cursor.HAND_CURSOR));
            jb_applyNow.addActionListener(new ApplyNowListener());
            jp_footer.add(jb_applyNow);
        }

        JButton jb_close = new JButton("Close");
        jb_close.setBackground(new Color(120, 130, 150));
        jb_close.setForeground(Color.WHITE);
        jb_close.setFont(new Font("Segoe UI", Font.BOLD, 13));
        jb_close.setFocusPainted(false);
        jb_close.setBorderPainted(false);
        jb_close.setOpaque(true);
        jb_close.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jb_close.addActionListener(new CloseListener());

        jp_footer.add(jb_close);
        add(jp_footer, BorderLayout.SOUTH);
    }

    // APPLY NOW LISTENER
    private class ApplyNowListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            jobPostingsFrame.setVisible(false);
            ApplyJobFrame frame = new ApplyJobFrame(dashboard, jobPostingsFrame, studentId, postingId, company, role);
            frame.setVisible(true);
        }
    }

    // CLOSE LISTENER
    private class CloseListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }

    // HAS APPLIED
    private boolean hasApplied() {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT COUNT(*) FROM application WHERE studentId = ? AND postingId = ?")) {
            ps.setInt(1, studentId);
            ps.setInt(2, postingId);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException ex) { return false; }
    }

    // LOAD POSTING DETAILS 
    private String[] loadPostingDetails() {
        String[] d = { "", "—", "—", "", "—", "—", "—" };
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "SELECT rp.description, rp.applicationDeadline, rp.location, " +
                "jo.salary, jo.workingHours, " +
                "po.stipend, po.placementDuration " +
                "FROM recruitmentPosting rp " +
                "LEFT JOIN jobOpportunity jo ON rp.postingId = jo.postingId " +
                "LEFT JOIN placementOpportunity po ON rp.postingId = po.postingId " +
                "WHERE rp.postingId = ?");
            ps.setInt(1, postingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                d[0] = rs.getString("description")         != null ? rs.getString("description")         : "";
                d[1] = rs.getString("location")            != null ? rs.getString("location")            : "—";
                d[2] = rs.getString("applicationDeadline") != null ? rs.getString("applicationDeadline") : "—";

                double sal    = rs.getDouble("salary");
                boolean isJob = !rs.wasNull();

                if (isJob) {
                    d[3] = "Job";
                    d[4] = sal > 0 ? String.valueOf(sal) : "—";
                    d[5] = rs.getString("workingHours") != null ? rs.getString("workingHours") : "—";
                } else {
                    d[3] = "Placement";
                    double sti = rs.getDouble("stipend");
                    d[4] = sti > 0 ? String.valueOf(sti) + "/month" : "—";
                    d[6] = rs.getString("placementDuration") != null ? rs.getString("placementDuration") : "—";
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return d;
    }
}