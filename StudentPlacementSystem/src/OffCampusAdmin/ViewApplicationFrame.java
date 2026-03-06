package OffCampusAdmin;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import dbconnection.DBConnection;

public class ViewApplicationFrame extends JFrame {

    // Colours declaration and initialisation
    private final Color clr_blue       = new Color(0, 102, 153);
    private final Color clr_blue_hover = new Color(0, 122, 183);
    private final Color clr_white      = Color.WHITE;
    private final Color clr_bg         = new Color(245, 248, 252);
    private final Color clr_cardBorder = new Color(220, 230, 240);
    private final Color clr_textDark   = new Color(40, 40, 40);


    // JLabel declaration
    private JLabel jl_headerTitle;
    private JLabel jl_profileIcon;
    private JLabel jl_userInfo;

    // JButton declaration
    private JButton jb_back;

    // JPanel declaration
    private JPanel jp_header;
    private JPanel jp_headerLeft;
    private JPanel jp_headerRightInner;
    private JPanel jp_headerRight;
    private JPanel jp_content;
    private JPanel jp_topBar;


    // table declaration
    private JTable jt_applicationsTable;
    private JTableHeader tableHeader;
    private DefaultTableModel dtm_applicationsModel;
    private JScrollPane jp_tableScroll;


    // columns declaration
    private String[] columns;


    // header panel declaration
    private ImageIcon rawProfileIcon;
    private Image scaledProfileIcon;


    private AdminDashboardFrame dashboard;

    public ViewApplicationFrame(AdminDashboardFrame dashboard, String adminName) {
        super("All Applications");
        this.dashboard = dashboard;
        setLayout(new BorderLayout());
        getContentPane().setBackground(clr_bg);
        setResizable(false);

        // Header panel
        jp_header = new JPanel(new BorderLayout());
        jp_header.setBackground(clr_blue);
        jp_header.setBorder(new EmptyBorder(12, 16, 12, 16));

        jp_headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        jp_headerLeft.setBackground(clr_blue);

        jl_headerTitle = new JLabel("All Applications");
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

        // Content panel
        jp_content = new JPanel(new BorderLayout());
        jp_content.setBackground(clr_bg);
        jp_content.setBorder(new EmptyBorder(16, 18, 18, 18));

        // Back Button
        jp_topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
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

        // Table initialisation
        columns = new String[]{ "Student ID", "FullName", "Company Name", "Job Title", "Date", "Status" };

        dtm_applicationsModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        jt_applicationsTable = new JTable(dtm_applicationsModel);
        jt_applicationsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jt_applicationsTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        jt_applicationsTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        jt_applicationsTable.setRowHeight(30);
        jt_applicationsTable.setGridColor(new Color(240, 244, 248));
        jt_applicationsTable.setBackground(clr_white);
        jt_applicationsTable.setForeground(clr_textDark);
        jt_applicationsTable.setSelectionBackground(new Color(210, 230, 250));
        jt_applicationsTable.setSelectionForeground(clr_textDark);
        jt_applicationsTable.setShowHorizontalLines(true);
        jt_applicationsTable.setShowVerticalLines(true);
        jt_applicationsTable.setFillsViewportHeight(false);

        tableHeader = jt_applicationsTable.getTableHeader();
        tableHeader.setBackground(clr_blue);
        tableHeader.setForeground(clr_white);
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableHeader.setPreferredSize(new Dimension(0, 36));
        tableHeader.setReorderingAllowed(false);

        jp_tableScroll = new JScrollPane(jt_applicationsTable);
        jp_tableScroll.setBorder(new LineBorder(clr_cardBorder, 1));
        jp_tableScroll.getViewport().setBackground(new Color(240, 244, 248));

        jp_content.add(jp_topBar,      BorderLayout.NORTH);
        jp_content.add(jp_tableScroll, BorderLayout.CENTER);
        add(jp_content, BorderLayout.CENTER);

        // Listeners
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

        // Load data from DB
        loadApplications();
    }

    private void loadApplications() {
        String sql = "SELECT s.studentId, s.fullName, " +
                     "       a.companyName, a.jobTitle, a.appliedDate, a.status " +
                     "FROM offCampusApplication a " +
                     "JOIN student s ON a.studentId = s.studentId " +
                     "ORDER BY a.offCampusAppId";

        try (Connection con = DBConnection.getConnection();
             Statement  st  = con.createStatement();
             ResultSet  rs  = st.executeQuery(sql)) {
            while (rs.next()) {
                dtm_applicationsModel.addRow(new Object[]{
                    rs.getInt   ("studentId"),
                    rs.getString("fullName"),
                    rs.getString("companyName"),
                    rs.getString("jobTitle"),
                    rs.getString("appliedDate"),
                    rs.getString("status")
                });
            }
        } catch (Exception ex) {
            AdminDashboardFrame.showDbError(ex);
        }
    }
}