package OffCampusAdmin;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import dbconnection.DBConnection;

public class AddApplicationFrame extends JFrame {

    // colour initialisation
    private final Color clr_blue       = new Color(0, 102, 153);
    private final Color clr_blue_hover = new Color(0, 122, 183);
    private final Color clr_white      = Color.WHITE;
    private final Color clr_bg         = new Color(245, 248, 252);
    private final Color clr_cardBorder = new Color(220, 230, 240);
    private final Color clr_textDark   = new Color(40, 40, 40);
    private final Color clr_textMid    = new Color(100, 116, 139);

    // JLabel declaration
    private JLabel jl_headerTitle;
    private JLabel jl_profileIcon;
    private JLabel jl_userInfo;
    private JLabel jl_studentId;
    private JLabel jl_company;
    private JLabel jl_role;
    private JLabel jl_date;
    private JLabel jl_status;


    // buttons declaration
    private JButton jb_back;
    private JButton jb_submit;
    private JButton jb_clear;

    //JPanel declaration
    private JPanel jp_header;
    private JPanel jp_headerLeft;
    private JPanel jp_headerRight;
    private JPanel jp_headerRightInner;
    private JPanel jp_content;
    private JPanel jp_backRow;
    private JPanel jp_formCard;
    private JPanel jp_formInner;
    private JPanel jp_bodyStack;
    private JPanel jp_bottomPanel;


    // JScrollPane declaration
    private JScrollPane jp_scrollPane;


    // JTextField and JComboBox Declaration
    private JTextField jtf_studentId;
    private JTextField jtf_company;
    private JTextField jtf_role;
    private JTextField jtf_date;
    private JComboBox<String> jcb_status;


    // image declaration
    private ImageIcon rawProfileIcon;
    private Image     scaledProfileIcon;


    // dashboard reference
    private AdminDashboardFrame dashboard;

    public AddApplicationFrame(AdminDashboardFrame dashboard, String adminName) {
        super("Add Application");
        this.dashboard = dashboard;
        setLayout(new BorderLayout());
        getContentPane().setBackground(clr_white);
        setResizable(false);

        // header panel
        jp_header = new JPanel(new BorderLayout());
        jp_header.setBackground(clr_blue);
        jp_header.setBorder(new EmptyBorder(12, 16, 12, 16));

        jp_headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        jp_headerLeft.setBackground(clr_blue);

        jl_headerTitle = new JLabel("Add Application");
        jl_headerTitle.setForeground(clr_white);
        jl_headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jp_headerLeft.add(jl_headerTitle);

        jp_headerRightInner = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        jp_headerRightInner.setBackground(clr_blue);

        rawProfileIcon    = new ImageIcon("src/images/profile.png");
        scaledProfileIcon = rawProfileIcon.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
        jl_profileIcon    = new JLabel(new ImageIcon(scaledProfileIcon));

        jl_userInfo = new JLabel(adminName + "  ·  Administrator");
        jl_userInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_userInfo.setForeground(clr_white);

        jp_headerRightInner.add(jl_profileIcon);
        jp_headerRightInner.add(jl_userInfo);

        jp_headerRight = new JPanel(new BorderLayout());
        jp_headerRight.setBackground(clr_blue);
        jp_headerRight.add(jp_headerRightInner, BorderLayout.CENTER);

        jp_header.add(jp_headerLeft,  BorderLayout.WEST);
        jp_header.add(jp_headerRight, BorderLayout.EAST);
        add(jp_header, BorderLayout.NORTH);

        // content panel
        jp_content = new JPanel(new BorderLayout());
        jp_content.setBackground(clr_white);
        jp_content.setBorder(new EmptyBorder(20, 28, 28, 28));

        // back button
        jb_back = new JButton("← Back");
        jb_back.setFocusPainted(false);
        jb_back.setBackground(clr_white);
        jb_back.setForeground(clr_textDark);
        jb_back.setBorder(new LineBorder(clr_cardBorder, 1));
        jb_back.setOpaque(true);
        jb_back.setPreferredSize(new Dimension(90, 32));
        jb_back.setMaximumSize(new Dimension(90, 32));
        jb_back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jb_back.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        jp_backRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_backRow.setBackground(clr_white);
        jp_backRow.setBorder(new EmptyBorder(0, 0, 16, 0));
        jp_backRow.add(jb_back);

        // form panel
        jp_formCard = new JPanel(new BorderLayout());
        jp_formCard.setBackground(clr_white);
        jp_formCard.setBorder(new LineBorder(clr_cardBorder, 1));

        jp_formInner = new JPanel(new GridLayout(10, 1, 0, 6));
        jp_formInner.setBackground(clr_white);
        jp_formInner.setBorder(new EmptyBorder(28, 32, 28, 32));

        // student id field
        jl_studentId = new JLabel("Student ID *");
        jl_studentId.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jl_studentId.setForeground(clr_textMid);

        jtf_studentId = new JTextField();
        jtf_studentId.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jtf_studentId.setForeground(clr_textDark);
        jtf_studentId.setBackground(clr_white);
        jtf_studentId.setCaretColor(clr_blue);
        jtf_studentId.setBorder(new LineBorder(clr_cardBorder, 1));
        jtf_studentId.setPreferredSize(new Dimension(210, 40));

        jp_formInner.add(jl_studentId);
        jp_formInner.add(jtf_studentId);

        // Company field
        jl_company = new JLabel("Company Name *");
        jl_company.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jl_company.setForeground(clr_textMid);

        jtf_company = new JTextField();
        jtf_company.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jtf_company.setForeground(clr_textDark);
        jtf_company.setBackground(clr_white);
        jtf_company.setCaretColor(clr_blue);
        jtf_company.setBorder(new LineBorder(clr_cardBorder, 1));
        jtf_company.setPreferredSize(new Dimension(210, 40));

        jp_formInner.add(jl_company);
        jp_formInner.add(jtf_company);

        // Role field
        jl_role = new JLabel("Job Title *");
        jl_role.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jl_role.setForeground(clr_textMid);

        jtf_role = new JTextField();
        jtf_role.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jtf_role.setForeground(clr_textDark);
        jtf_role.setBackground(clr_white);
        jtf_role.setCaretColor(clr_blue);
        jtf_role.setBorder(new LineBorder(clr_cardBorder, 1));
        jtf_role.setPreferredSize(new Dimension(210, 40));

        jp_formInner.add(jl_role);
        jp_formInner.add(jtf_role);

        // Date field
        jl_date = new JLabel("Date (YYYY/MM/DD) *");
        jl_date.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jl_date.setForeground(clr_textMid);

        jtf_date = new JTextField();
        jtf_date.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jtf_date.setForeground(clr_textDark);
        jtf_date.setBackground(clr_white);
        jtf_date.setCaretColor(clr_blue);
        jtf_date.setBorder(new LineBorder(clr_cardBorder, 1));
        jtf_date.setPreferredSize(new Dimension(210, 40));
        jtf_date.setToolTipText("Format: YYYY/MM/DD  e.g. 2025/03/15");

        jp_formInner.add(jl_date);
        jp_formInner.add(jtf_date);

        // Status combo
        jl_status = new JLabel("Status *");
        jl_status.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jl_status.setForeground(clr_textMid);

        jcb_status = new JComboBox<>(new String[]{"Pending", "Accepted", "Rejected"});
        jcb_status.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jcb_status.setBackground(clr_white);
        jcb_status.setForeground(clr_textDark);
        jcb_status.setPreferredSize(new Dimension(0, 40));

        jp_formInner.add(jl_status);
        jp_formInner.add(jcb_status);

        jp_formCard.add(jp_formInner, BorderLayout.CENTER);

        jp_scrollPane = new JScrollPane(jp_formCard);
        jp_scrollPane.setBorder(null);
        jp_scrollPane.getViewport().setBackground(clr_white);

        jp_bodyStack = new JPanel(new BorderLayout());
        jp_bodyStack.setBackground(clr_white);
        jp_bodyStack.add(jp_backRow,   BorderLayout.NORTH);
        jp_bodyStack.add(jp_scrollPane, BorderLayout.CENTER);

        jp_content.add(jp_bodyStack, BorderLayout.CENTER);

        // bottom buttons
        jb_submit = new JButton("Submit Application");
        jb_submit.setFocusPainted(false);
        jb_submit.setBackground(clr_blue);
        jb_submit.setForeground(clr_white);
        jb_submit.setBorderPainted(false);
        jb_submit.setOpaque(true);
        jb_submit.setPreferredSize(new Dimension(190, 42));
        jb_submit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jb_submit.setFont(new Font("Segoe UI", Font.BOLD, 14));

        jb_clear = new JButton("Clear Fields");
        jb_clear.setFocusPainted(false);
        jb_clear.setBackground(clr_white);
        jb_clear.setForeground(clr_textMid);
        jb_clear.setBorder(new LineBorder(clr_cardBorder, 1));
        jb_clear.setOpaque(true);
        jb_clear.setPreferredSize(new Dimension(120, 42));
        jb_clear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jb_clear.setFont(new Font("Segoe UI", Font.BOLD, 14));

        jp_bottomPanel = new JPanel();
        jp_bottomPanel.setBackground(clr_bg);
        jp_bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        jp_bottomPanel.add(jb_clear);
        jp_bottomPanel.add(jb_submit);

        add(jp_content,    BorderLayout.CENTER);
        add(jp_bottomPanel, BorderLayout.SOUTH);

        // listeners

        jb_back.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent me) {
                jb_back.setBackground(new Color(230, 235, 245));
            }
            public void mouseExited(MouseEvent me) {
                jb_back.setBackground(clr_white);
            }
        });

        jb_back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                dashboard.setVisible(true);
                dispose();
            }
        });

        jb_submit.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent me) {
                jb_submit.setBackground(clr_blue_hover);
            }
            public void mouseExited(MouseEvent me) {
                jb_submit.setBackground(clr_blue);
            }
        });

        jb_submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                handleSubmit();
            }
        });

        jb_clear.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent me) {
                jb_clear.setBackground(new Color(230, 235, 245));
            }
            public void mouseExited(MouseEvent me) {
                jb_clear.setBackground(clr_white);
            }
        });

        jb_clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                jtf_studentId.setText("");
                jtf_company.setText("");
                jtf_role.setText("");
                jtf_date.setText("");
                jcb_status.setSelectedIndex(0);
            }
        });
    }

    // Submit -> validate -> INSERT into DB
    private void handleSubmit() {

        // 1. Empty field check
        if (jtf_studentId.getText().trim().isEmpty() ||
            jtf_company.getText().trim().isEmpty()   ||
            jtf_role.getText().trim().isEmpty()       ||
            jtf_date.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields before submitting.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String rawDate = jtf_date.getText().trim();
        if (!rawDate.matches("\\d{4}/\\d{2}/\\d{2}")) {
            JOptionPane.showMessageDialog(this,
                "Date must be in YYYY/MM/DD format (e.g. 2025/03/15).",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Parse and check the 30-day window
        java.time.LocalDate appliedDate;
        try {
            appliedDate = java.time.LocalDate.parse(rawDate.replace("/", "-"));
        } catch (java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                "Invalid date. Please enter a real calendar date.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        java.time.LocalDate today    = java.time.LocalDate.now();
        java.time.LocalDate earliest = today.minusDays(30);

        if (appliedDate.isAfter(today)) {
            JOptionPane.showMessageDialog(this,
                "Applied date cannot be in the future.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (appliedDate.isBefore(earliest)) {
            JOptionPane.showMessageDialog(this,
                "Applied date must be within the last 30 days.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dbDate    = rawDate.replace("/", "-");
        String studentId = jtf_studentId.getText().trim();
        

        // 3. Validate that the Student ID exists
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT studentId FROM student WHERE studentId = ?")) {
            ps.setString(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(this,
                    "Student ID \"" + studentId + "\" does not exist in the system.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (SQLException ex) {
            AdminDashboardFrame.showDbError(ex);
            return;
        }

        // 4. All checks passed — insert into offCampusApplication
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "INSERT INTO offCampusApplication " +
                 "(companyName, jobTitle, appliedDate, status, studentId) " +
                 "VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, jtf_company.getText().trim());
            ps.setString(2, jtf_role.getText().trim());
            ps.setDate  (3, java.sql.Date.valueOf(dbDate));
            ps.setString(4, (String) jcb_status.getSelectedItem());
            ps.setInt   (5, Integer.parseInt(studentId));
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Application submitted successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);

            jtf_studentId.setText("");
            jtf_company.setText("");
            jtf_role.setText("");
            jtf_date.setText("");
            jcb_status.setSelectedIndex(0);

            dashboard.refreshStats();
            dashboard.setVisible(true);
            dispose();

        } catch (SQLException ex) {
            AdminDashboardFrame.showDbError(ex);
        }
    }
}