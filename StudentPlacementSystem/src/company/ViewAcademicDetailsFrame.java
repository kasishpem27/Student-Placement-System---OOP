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

public class ViewAcademicDetailsFrame extends JFrame {

    private final Color clr_blue       = new Color(0, 102, 153);
    private final Color clr_blue_hover = new Color(0, 122, 183);
    private final Color clr_white      = Color.WHITE;
    private final Color clr_bg         = new Color(245, 247, 250);
    private final Color clr_cardBorder = new Color(220, 225, 230);
    private final Color clr_textDark   = new Color(40, 40, 40);
    private final Color clr_label      = new Color(90, 90, 90);

    private final String companyName;
    private final String companyId;
    private final CompanyDashboardFrame dashboard;

    private JTextField        jt_search;
    private DefaultTableModel tm_list;
    private JTable            jt_list;

    private JLabel jl_val_name, jl_val_program, jl_val_gpa, jl_val_cpa, jl_val_gradYear;
    private JTextArea jl_val_posting;
    private JTextArea jta_Skills, jta_Modules, jta_Notes;

    public ViewAcademicDetailsFrame(String companyName, String companyId, CompanyDashboardFrame dashboard) {
        super("CareerConnect - Academic Details");
        this.companyName = companyName;
        this.companyId   = companyId == null ? "1" : companyId.replaceAll("[^0-9]", "");
        this.dashboard   = dashboard;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 750));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ── HEADER ──────────────────────────────────────────────────────────
        JPanel jp_header = new JPanel(new BorderLayout());
        jp_header.setBackground(clr_blue);
        jp_header.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel jp_headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        jp_headerLeft.setBackground(clr_blue);
        JLabel jl_title = new JLabel("Academic Details");
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

        // ── ROOT CONTENT ─────────────────────────────────────────────────────
        JPanel jp_root = new JPanel(new BorderLayout());
        jp_root.setBackground(clr_bg);
        jp_root.setBorder(new EmptyBorder(16, 18, 18, 18));

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
        jp_topBar.add(jb_back, BorderLayout.WEST);

        // ── BODY ─────────────────────────────────────────────────────────────
        JPanel jp_body = new JPanel(new BorderLayout(14, 0));
        jp_body.setBackground(clr_bg);

        // LEFT PANEL
        JPanel jp_left = new JPanel(new BorderLayout());
        jp_left.setBackground(clr_white);
        jp_left.setBorder(new LineBorder(clr_cardBorder, 1));

        JPanel jp_leftHead = new JPanel(new BorderLayout());
        jp_leftHead.setBackground(clr_white);
        jp_leftHead.setBorder(new EmptyBorder(14, 14, 10, 14));

        JLabel jl_applicantsTitle = new JLabel("Applicants");
        jl_applicantsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jl_applicantsTitle.setForeground(clr_blue);

        JPanel jp_searchRow = new JPanel(new BorderLayout(6, 0));
        jp_searchRow.setBackground(clr_white);
        jp_searchRow.setBorder(new EmptyBorder(8, 0, 0, 0));

        jt_search = new JTextField();
        jt_search.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jt_search.setPreferredSize(new Dimension(0, 30));
        jt_search.setBorder(new LineBorder(new Color(210, 210, 210), 1));
        jt_search.setToolTipText("Search by Name");

        JButton jb_search = new JButton("Search");
        jb_search.setFocusPainted(false);
        jb_search.setFont(new Font("Segoe UI", Font.BOLD, 13));
        jb_search.setForeground(clr_white);
        jb_search.setBackground(clr_blue);
        jb_search.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jb_search.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        jb_search.setOpaque(true);
        jb_search.setContentAreaFilled(true);
        jb_search.setBorderPainted(false);

        jp_searchRow.add(jt_search, BorderLayout.CENTER);
        jp_searchRow.add(jb_search, BorderLayout.EAST);

        jp_leftHead.add(jl_applicantsTitle, BorderLayout.NORTH);
        jp_leftHead.add(jp_searchRow,       BorderLayout.SOUTH);

        tm_list = new DefaultTableModel(new Object[]{"Name", "Course", "studentId"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        jt_list = new JTable(tm_list);
        jt_list.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jt_list.setRowHeight(28);
        jt_list.setBackground(clr_white);
        jt_list.setForeground(clr_textDark);
        jt_list.setGridColor(new Color(240, 244, 248));
        jt_list.setSelectionBackground(new Color(210, 230, 250));
        jt_list.setSelectionForeground(clr_textDark);
        jt_list.setShowVerticalLines(false);

        JTableHeader th = jt_list.getTableHeader();
        th.setBackground(clr_blue);
        th.setForeground(clr_white);
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setPreferredSize(new Dimension(0, 34));
        th.setReorderingAllowed(false);

        jt_list.getColumnModel().getColumn(2).setMinWidth(0);
        jt_list.getColumnModel().getColumn(2).setMaxWidth(0);
        jt_list.getColumnModel().getColumn(2).setWidth(0);
        jt_list.getColumnModel().getColumn(0).setPreferredWidth(140);
        jt_list.getColumnModel().getColumn(1).setPreferredWidth(200);
        jt_list.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        JScrollPane sp_list = new JScrollPane(jt_list);
        sp_list.setBorder(new LineBorder(clr_cardBorder, 1));
        sp_list.getViewport().setBackground(new Color(245, 248, 252));
        sp_list.getVerticalScrollBar().setUnitIncrement(16);

        jp_left.add(jp_leftHead, BorderLayout.NORTH);
        jp_left.add(sp_list,     BorderLayout.CENTER);

        // RIGHT PANEL
        JPanel jp_right = new JPanel(new BorderLayout());
        jp_right.setBackground(clr_white);
        jp_right.setBorder(new LineBorder(clr_cardBorder, 1));

        JPanel jp_rightHead = new JPanel(new BorderLayout());
        jp_rightHead.setBackground(clr_white);
        jp_rightHead.setBorder(new EmptyBorder(14, 14, 10, 14));
        JLabel jl_detailTitle = new JLabel("Student Details");
        jl_detailTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jl_detailTitle.setForeground(clr_blue);
        jp_rightHead.add(jl_detailTitle, BorderLayout.CENTER);

        JPanel jp_infoGrid = new JPanel(new GridLayout(6, 2, 0, 0));
        jp_infoGrid.setBackground(clr_white);
        jp_infoGrid.setBorder(new EmptyBorder(0, 14, 8, 14));

        JLabel jl_nameLbl = new JLabel("Name");
        jl_nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jl_nameLbl.setForeground(clr_label);
        jl_nameLbl.setBorder(new EmptyBorder(6, 0, 6, 8));
        jl_val_name = new JLabel("-");
        jl_val_name.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_val_name.setForeground(clr_textDark);
        jl_val_name.setBorder(new EmptyBorder(6, 8, 6, 8));
        jp_infoGrid.add(jl_nameLbl);
        jp_infoGrid.add(jl_val_name);

        JLabel jl_progLbl = new JLabel("Program");
        jl_progLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jl_progLbl.setForeground(clr_label);
        jl_progLbl.setBorder(new EmptyBorder(6, 0, 6, 8));
        jl_val_program = new JLabel("-");
        jl_val_program.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_val_program.setForeground(clr_textDark);
        jl_val_program.setBorder(new EmptyBorder(6, 8, 6, 8));
        jp_infoGrid.add(jl_progLbl);
        jp_infoGrid.add(jl_val_program);

        JLabel jl_gpaLbl = new JLabel("GPA");
        jl_gpaLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jl_gpaLbl.setForeground(clr_label);
        jl_gpaLbl.setBorder(new EmptyBorder(6, 0, 6, 8));
        jl_val_gpa = new JLabel("-");
        jl_val_gpa.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_val_gpa.setForeground(clr_textDark);
        jl_val_gpa.setBorder(new EmptyBorder(6, 8, 6, 8));
        jp_infoGrid.add(jl_gpaLbl);
        jp_infoGrid.add(jl_val_gpa);

        JLabel jl_cpaLbl = new JLabel("CPA");
        jl_cpaLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jl_cpaLbl.setForeground(clr_label);
        jl_cpaLbl.setBorder(new EmptyBorder(6, 0, 6, 8));
        jl_val_cpa = new JLabel("-");
        jl_val_cpa.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_val_cpa.setForeground(clr_textDark);
        jl_val_cpa.setBorder(new EmptyBorder(6, 8, 6, 8));
        jp_infoGrid.add(jl_cpaLbl);
        jp_infoGrid.add(jl_val_cpa);

        JLabel jl_gradLbl = new JLabel("Graduation Year");
        jl_gradLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jl_gradLbl.setForeground(clr_label);
        jl_gradLbl.setBorder(new EmptyBorder(6, 0, 6, 8));
        jl_val_gradYear = new JLabel("-");
        jl_val_gradYear.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_val_gradYear.setForeground(clr_textDark);
        jl_val_gradYear.setBorder(new EmptyBorder(6, 8, 6, 8));
        jp_infoGrid.add(jl_gradLbl);
        jp_infoGrid.add(jl_val_gradYear);

        JLabel jl_postLbl = new JLabel("Applied To");
        jl_postLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jl_postLbl.setForeground(clr_label);
        jl_postLbl.setBorder(new EmptyBorder(6, 0, 6, 8));
        jl_val_posting = new JTextArea("-");
        jl_val_posting.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_val_posting.setForeground(clr_textDark);
        jl_val_posting.setBackground(clr_white);
        jl_val_posting.setEditable(false);
        jl_val_posting.setLineWrap(true);
        jl_val_posting.setWrapStyleWord(true);
        jl_val_posting.setBorder(new EmptyBorder(6, 8, 6, 8));
        jp_infoGrid.add(jl_postLbl);
        jp_infoGrid.add(jl_val_posting);

        // Text area sections
        JPanel jp_textSections = new JPanel(new GridLayout(3, 1, 0, 10));
        jp_textSections.setBackground(clr_white);
        jp_textSections.setBorder(new EmptyBorder(8, 14, 14, 14));

        JPanel jp_skillsSection = new JPanel(new BorderLayout());
        jp_skillsSection.setBackground(clr_white);
        jp_skillsSection.setBorder(new LineBorder(clr_cardBorder, 1));
        JLabel jl_skillsTitle = new JLabel("Skills");
        jl_skillsTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jl_skillsTitle.setForeground(clr_label);
        jl_skillsTitle.setBorder(new EmptyBorder(6, 8, 4, 8));
        jta_Skills = new JTextArea("-");
        jta_Skills.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jta_Skills.setEditable(false);
        jta_Skills.setLineWrap(true);
        jta_Skills.setWrapStyleWord(true);
        jta_Skills.setBackground(clr_white);
        jta_Skills.setForeground(clr_textDark);
        jta_Skills.setBorder(new EmptyBorder(4, 6, 4, 6));
        JScrollPane sp_skills = new JScrollPane(jta_Skills);
        sp_skills.setBorder(null);
        sp_skills.getViewport().setBackground(clr_white);
        sp_skills.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp_skills.getVerticalScrollBar().setUnitIncrement(16);
        jp_skillsSection.add(jl_skillsTitle, BorderLayout.NORTH);
        jp_skillsSection.add(sp_skills,      BorderLayout.CENTER);

        JPanel jp_modulesSection = new JPanel(new BorderLayout());
        jp_modulesSection.setBackground(clr_white);
        jp_modulesSection.setBorder(new LineBorder(clr_cardBorder, 1));
        JLabel jl_modulesTitle = new JLabel("Modules & Grades");
        jl_modulesTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jl_modulesTitle.setForeground(clr_label);
        jl_modulesTitle.setBorder(new EmptyBorder(6, 8, 4, 8));
        jta_Modules = new JTextArea("-");
        jta_Modules.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jta_Modules.setEditable(false);
        jta_Modules.setLineWrap(true);
        jta_Modules.setWrapStyleWord(true);
        jta_Modules.setBackground(clr_white);
        jta_Modules.setForeground(clr_textDark);
        jta_Modules.setBorder(new EmptyBorder(4, 6, 4, 6));
        JScrollPane sp_modules = new JScrollPane(jta_Modules);
        sp_modules.setBorder(null);
        sp_modules.getViewport().setBackground(clr_white);
        sp_modules.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp_modules.getVerticalScrollBar().setUnitIncrement(16);
        jp_modulesSection.add(jl_modulesTitle, BorderLayout.NORTH);
        jp_modulesSection.add(sp_modules,      BorderLayout.CENTER);

        JPanel jp_notesSection = new JPanel(new BorderLayout());
        jp_notesSection.setBackground(clr_white);
        jp_notesSection.setBorder(new LineBorder(clr_cardBorder, 1));
        JLabel jl_notesTitle = new JLabel("Notes");
        jl_notesTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jl_notesTitle.setForeground(clr_label);
        jl_notesTitle.setBorder(new EmptyBorder(6, 8, 4, 8));
        jta_Notes = new JTextArea("-");
        jta_Notes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jta_Notes.setEditable(false);
        jta_Notes.setLineWrap(true);
        jta_Notes.setWrapStyleWord(true);
        jta_Notes.setBackground(clr_white);
        jta_Notes.setForeground(clr_textDark);
        jta_Notes.setBorder(new EmptyBorder(4, 6, 4, 6));
        JScrollPane sp_notes = new JScrollPane(jta_Notes);
        sp_notes.setBorder(null);
        sp_notes.getViewport().setBackground(clr_white);
        sp_notes.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp_notes.getVerticalScrollBar().setUnitIncrement(16);
        jp_notesSection.add(jl_notesTitle, BorderLayout.NORTH);
        jp_notesSection.add(sp_notes,      BorderLayout.CENTER);

        jp_textSections.add(jp_skillsSection);
        jp_textSections.add(jp_modulesSection);
        jp_textSections.add(jp_notesSection);

        JPanel jp_rightBody = new JPanel(new BorderLayout());
        jp_rightBody.setBackground(clr_white);
        jp_rightBody.add(jp_infoGrid,     BorderLayout.NORTH);
        jp_rightBody.add(jp_textSections, BorderLayout.CENTER);

        JScrollPane sp_right = new JScrollPane(jp_rightBody);
        sp_right.setBorder(null);
        sp_right.getViewport().setBackground(clr_white);
        sp_right.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp_right.getVerticalScrollBar().setUnitIncrement(16);

        jp_right.add(jp_rightHead, BorderLayout.NORTH);
        jp_right.add(sp_right,     BorderLayout.CENTER);

        jp_left.setPreferredSize(new Dimension(420, 0));
        jp_body.add(jp_left,  BorderLayout.WEST);
        jp_body.add(jp_right, BorderLayout.CENTER);

        jp_root.add(jp_topBar, BorderLayout.NORTH);
        jp_root.add(jp_body,   BorderLayout.CENTER);

        add(jp_root, BorderLayout.CENTER);

        // ── INITIAL LOAD ─────────────────────────────────────────────────────
        tm_list.setRowCount(0);
        String sqlAll =
            "SELECT DISTINCT s.studentId, s.fullName, s.course " +
            "FROM student s " +
            "JOIN application a ON a.studentId = s.studentId " +
            "JOIN recruitmentPosting rp ON a.postingId = rp.postingId " +
            "JOIN company c ON rp.companyId = c.companyId " +
            "WHERE c.companyId = ? ORDER BY s.studentId";
        int initCount = 0;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlAll)) {
            ps.setInt(1, Integer.parseInt(companyId));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String sc = String.valueOf(rs.getInt("studentId"));
                    String fn = rs.getString("fullName");
                    String cr = rs.getString("course");
                    tm_list.addRow(new Object[]{
                        fn != null && !fn.trim().isEmpty() ? fn.trim() : "-",
                        cr != null && !cr.trim().isEmpty() ? cr.trim() : "-",
                        sc != null && !sc.trim().isEmpty() ? sc.trim() : "-"
                    });
                    initCount++;
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "DB error while loading applicants:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        if (initCount == 0) {
            tm_list.addRow(new Object[]{"No applicants found", "-", "-"});
        }

        // ── LISTENERS ────────────────────────────────────────────────────────
        jb_back.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                jb_back.setBackground(new Color(230, 235, 245));
            }
            public void mouseExited(MouseEvent e) {
                jb_back.setBackground(clr_white);
            }
        });
        jb_back.addActionListener(new BackHandler());

        jb_search.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                jb_search.setBackground(clr_blue_hover);
            }
            public void mouseExited(MouseEvent e) {
                jb_search.setBackground(clr_blue);
            }
        });
        jb_search.addActionListener(new SearchHandler());
        jt_search.addActionListener(new SearchHandler());

        jt_list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = jt_list.getSelectedRow();
                if (row < 0) {
                    return;
                }
                String studentId = String.valueOf(tm_list.getValueAt(row, 2));
                if (studentId == null || studentId.trim().isEmpty() || studentId.equals("-")) {
                    jl_val_name.setText("-");
                    jl_val_program.setText("-");
                    jl_val_gpa.setText("-");
                    jl_val_cpa.setText("-");
                    jl_val_gradYear.setText("-");
                    jl_val_posting.setText("-");
                    jta_Skills.setText("-");
                    jta_Modules.setText("-");
                    jta_Notes.setText("-");
                    return;
                }
                String sqlS =
                    "SELECT s.studentId, s.fullName, s.course, " +
                    "ad.gpa, ad.cpa, ad.graduationYear, ad.skills, ad.modules, ad.notes, " +
                    "GROUP_CONCAT(DISTINCT rp.title ORDER BY rp.postingId SEPARATOR ', ') AS postings " +
                    "FROM student s " +
                    "JOIN application a ON a.studentId = s.studentId " +
                    "JOIN recruitmentPosting rp ON a.postingId = rp.postingId " +
                    "JOIN company c ON rp.companyId = c.companyId " +
                    "LEFT JOIN academicDetails ad ON ad.studentId = s.studentId " +
                    "WHERE c.companyId = ? AND s.studentId = ? " +
                    "GROUP BY s.studentId, s.fullName, s.course, ad.gpa, ad.cpa, ad.graduationYear, ad.skills, ad.modules, ad.notes " +
                    "LIMIT 1";
                try (Connection con = DBConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement(sqlS)) {
                    ps.setInt(1, Integer.parseInt(companyId));
                    ps.setInt(2, Integer.parseInt(studentId.trim()));
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            jl_val_name.setText("-");
                            jl_val_program.setText("-");
                            jl_val_gpa.setText("-");
                            jl_val_cpa.setText("-");
                            jl_val_gradYear.setText("-");
                            jl_val_posting.setText("-");
                            jta_Skills.setText("-");
                            jta_Modules.setText("-");
                            jta_Notes.setText("-");
                            return;
                        }
                        String fn   = rs.getString("fullName");
                        jl_val_name.setText(fn != null && !fn.trim().isEmpty() ? fn.trim() : "-");
                        String cr   = rs.getString("course");
                        jl_val_program.setText(cr != null && !cr.trim().isEmpty() ? "<html>" + cr.trim() + "</html>" : "-");
                        String gpa  = rs.getString("gpa");
                        jl_val_gpa.setText(gpa != null && !gpa.trim().isEmpty() ? gpa.trim() : "-");
                        String cpa  = rs.getString("cpa");
                        jl_val_cpa.setText(cpa != null && !cpa.trim().isEmpty() ? cpa.trim() : "-");
                        String gy   = rs.getString("graduationYear");
                        jl_val_gradYear.setText(gy != null && !gy.trim().isEmpty() ? gy.trim() : "-");
                        String post = rs.getString("postings");
                        jl_val_posting.setText(post != null && !post.trim().isEmpty() ? post.trim() : "-");
                        String rawSkills = rs.getString("skills");
                        if (rawSkills == null || rawSkills.trim().isEmpty()) {
                            jta_Skills.setText("-");
                        } else {
                            jta_Skills.setText(rawSkills.trim().replaceAll(",\\s*", "\n"));
                        }
                        String mod = rs.getString("modules");
                        if (mod == null || mod.trim().isEmpty()) {
                            jta_Modules.setText("-");
                        } else {
                            jta_Modules.setText(mod.trim().replaceAll(",\\s*", "\n"));
                        }
                        String notes = rs.getString("notes");
                        jta_Notes.setText(notes != null && !notes.trim().isEmpty() ? notes.trim() : "-");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        ViewAcademicDetailsFrame.this,
                        "DB error while loading academic details:\n" + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }

    // ── BACK LISTENER ────────────────────────────────────────────────────────
    private class BackHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dashboard.refreshStats();
            dashboard.setVisible(true);
            dispose();
        }
    }

    // ── SEARCH LISTENER ──────────────────────────────────────────────────────
    private class SearchHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String q = jt_search == null ? "" : jt_search.getText();
            String f = q == null ? "" : q.trim().toLowerCase();

            tm_list.setRowCount(0);
            jl_val_name.setText("-");
            jl_val_program.setText("-");
            jl_val_gpa.setText("-");
            jl_val_cpa.setText("-");
            jl_val_gradYear.setText("-");
            jl_val_posting.setText("-");
            jta_Skills.setText("-");
            jta_Modules.setText("-");
            jta_Notes.setText("-");

            String sql = f.isEmpty()
                ? "SELECT DISTINCT s.studentId, s.fullName, s.course " +
                  "FROM student s JOIN application a ON a.studentId = s.studentId " +
                  "JOIN recruitmentPosting rp ON a.postingId = rp.postingId " +
                  "JOIN company c ON rp.companyId = c.companyId " +
                  "WHERE c.companyId = ? ORDER BY s.studentId"
                : "SELECT DISTINCT s.studentId, s.fullName, s.course " +
                  "FROM student s JOIN application a ON a.studentId = s.studentId " +
                  "JOIN recruitmentPosting rp ON a.postingId = rp.postingId " +
                  "JOIN company c ON rp.companyId = c.companyId " +
                  "WHERE c.companyId = ? AND LOWER(s.fullName) LIKE ? " +
                  "ORDER BY s.studentId";

            int count = 0;
            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, Integer.parseInt(companyId));
                if (!f.isEmpty()) {
                    ps.setString(2, "%" + f + "%");
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String sc = String.valueOf(rs.getInt("studentId"));
                        String fn = rs.getString("fullName");
                        String cr = rs.getString("course");
                        tm_list.addRow(new Object[]{
                            fn != null && !fn.trim().isEmpty() ? fn.trim() : "-",
                            cr != null && !cr.trim().isEmpty() ? cr.trim() : "-",
                            sc != null && !sc.trim().isEmpty() ? sc.trim() : "-"
                        });
                        count++;
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ViewAcademicDetailsFrame.this, "DB error while searching:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
            if (count == 0) {
                tm_list.addRow(new Object[]{"No applicants found", "-", "-"});
            }
        }
    }
}