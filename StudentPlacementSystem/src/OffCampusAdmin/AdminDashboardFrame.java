package OffCampusAdmin;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import dbconnection.DBConnection;
import student.LoginFrame;

public class AdminDashboardFrame extends JFrame {

    // colours declaration and initialisation
    private final Color clr_blue             = new Color(0, 102, 153);
    private final Color clr_blue_hover       = new Color(0, 122, 183);
    private final Color clr_white            = Color.WHITE;
    private final Color clr_bg               = new Color(245, 248, 252);
    private final Color clr_cardBorder       = new Color(220, 230, 240);
    private final Color clr_textDark         = new Color(40, 40, 40);
    private final Color clr_logout_btn       = new Color(0, 140, 205);
    private final Color clr_welcome_subtitle = new Color(190, 220, 240);
    private final Color clr_stats_title      = new Color(90, 90, 90);
    private final Color clr_quick_title      = new Color(50, 50, 50);
    private Color viewCardBackground;
    private Color addCardBackground;


    //JLabel Declaration
    private JLabel jl_headerTitle;
    private JLabel jl_profileIcon;
    private JLabel jl_userInfo;
    private JLabel jl_headerLogo;
    private JLabel jl_welcomeTitle;
    private JLabel jl_welcomeSubTitle;
    private JLabel title_total;
    private JLabel title_pending;
    private JLabel title_selected;
    private JLabel title_rejected;
    private JLabel jl_quickActionsTitle;
    private JLabel jl_viewIcon;
    private JLabel jl_viewApplicationsTitle;
    private JLabel jl_addIcon;
    private JLabel jl_addApplicationTitle;
    private JLabel jl_totalApplicationsValue;
    private JLabel jl_pendingApplicationsValue;
    private JLabel jl_selectedApplicationsValue;
    private JLabel jl_rejectedApplicationsValue;


    // JButton declaration
    private JButton jb_logout;

    //JPanel declaration
    private JPanel jp_header;
    private JPanel jp_headerLeft;
    private JPanel jp_headerRightInner;
    private JPanel jp_headerRight;
    private JPanel jp_content;
    private JPanel jp_welcomeBanner;
    private JPanel jp_welcomeText;
    private JPanel jp_welcomeBannerWrapper;
    private JPanel jp_statsRow;
    private JPanel card_total;
    private JPanel inner_total;
    private JPanel card_pending;
    private JPanel inner_pending;
    private JPanel card_selected;
    private JPanel inner_selected;
    private JPanel card_rejected;
    private JPanel inner_rejected;
    private JPanel jp_quickActionsTitlePanel;
    private JPanel jp_quickActionsRow;
    private JPanel jp_quickActionsGrid;
    private JPanel jp_viewApplicationsInner;
    private JPanel jp_addApplicationInner;
    private JPanel jp_topContent;
    private JPanel jp_quickActionsSection;
    private JPanel jp_centerContent;
    private JPanel jp_viewApplicationsActionCard;
    private JPanel jp_addApplicationActionCard;

    // ImageIcon declaration
    private ImageIcon rawLogoIcon;
    private ImageIcon rawProfileIcon;
    private ImageIcon viewIcon;
    private ImageIcon addIcon;

    // Image declaration
    private Image scaledLogoIcon;
    private Image scaledProfileIcon;

    // logged-in admin
    private int adminUserId;
    private String adminName = "Admin";
    private LoginFrame login;

    public AdminDashboardFrame(LoginFrame login,int adminUserId) {
        super("CareerConnect — Admin");
        this.login = login;
        this.adminUserId = adminUserId;
        this.adminName   = fetchAdminName(adminUserId);
        setLayout(new BorderLayout());
        getContentPane().setBackground(clr_bg);
        setResizable(false);

        // header panel
        jp_header = new JPanel(new BorderLayout());
        jp_header.setBackground(clr_blue);
        jp_header.setBorder(new EmptyBorder(12, 16, 12, 16));

        jp_headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        jp_headerLeft.setBackground(clr_blue);

        rawLogoIcon    = new ImageIcon("src/images/CareerConnect.png");
        scaledLogoIcon = rawLogoIcon.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
        jl_headerLogo  = new JLabel(new ImageIcon(scaledLogoIcon));
        jl_headerLogo.setHorizontalAlignment(SwingConstants.LEFT);

        jl_headerTitle = new JLabel("CareerConnect — Off-Campus Admin Dashboard");
        jl_headerTitle.setForeground(clr_white);
        jl_headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        jp_headerLeft.add(jl_headerLogo);
        jp_headerLeft.add(jl_headerTitle);

        jp_headerRightInner = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        jp_headerRightInner.setBackground(clr_blue);

        rawProfileIcon    = new ImageIcon("src/images/profile.png");
        scaledProfileIcon = rawProfileIcon.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
        jl_profileIcon    = new JLabel(new ImageIcon(scaledProfileIcon));

        jl_userInfo = new JLabel(adminName + "  ·  Administrator");
        jl_userInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_userInfo.setForeground(clr_white);

        jb_logout = new JButton("Logout");
        jb_logout.setFocusPainted(false);
        jb_logout.setBackground(clr_logout_btn);
        jb_logout.setForeground(clr_white);
        jb_logout.setBorderPainted(false);
        jb_logout.setOpaque(true);
        jb_logout.setPreferredSize(new Dimension(110, 34));
        jb_logout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        jp_headerRightInner.add(jl_profileIcon);
        jp_headerRightInner.add(jl_userInfo);
        jp_headerRightInner.add(jb_logout);

        jp_headerRight = new JPanel(new BorderLayout());
        jp_headerRight.setBackground(clr_blue);
        jp_headerRight.add(jp_headerRightInner, BorderLayout.CENTER);

        jp_header.add(jp_headerLeft,  BorderLayout.WEST);
        jp_header.add(jp_headerRight, BorderLayout.EAST);
        add(jp_header, BorderLayout.NORTH);

        // content panel
        jp_content = new JPanel(new BorderLayout());
        jp_content.setBackground(clr_bg);
        jp_content.setBorder(new EmptyBorder(18, 18, 18, 18));

        // welcome panel
        jp_welcomeBanner = new JPanel(new BorderLayout());
        jp_welcomeBanner.setBackground(clr_blue);
        jp_welcomeBanner.setBorder(new EmptyBorder(14, 18, 14, 18));

        jp_welcomeText = new JPanel(new GridLayout(2, 1, 0, 2));
        jp_welcomeText.setBackground(clr_blue);

        jl_welcomeTitle = new JLabel("Welcome back, " + adminName);
        jl_welcomeTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jl_welcomeTitle.setForeground(clr_white);

        jl_welcomeSubTitle = new JLabel("Off-Campus Dashboard");
        jl_welcomeSubTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_welcomeSubTitle.setForeground(clr_welcome_subtitle);

        jp_welcomeText.add(jl_welcomeTitle);
        jp_welcomeText.add(jl_welcomeSubTitle);
        jp_welcomeBanner.add(jp_welcomeText, BorderLayout.WEST);

        jp_welcomeBannerWrapper = new JPanel(new BorderLayout());
        jp_welcomeBannerWrapper.setBackground(clr_bg);
        jp_welcomeBannerWrapper.setBorder(new EmptyBorder(0, 0, 16, 0));
        jp_welcomeBannerWrapper.add(jp_welcomeBanner, BorderLayout.CENTER);

        // stats panel
        jp_statsRow = new JPanel(new GridLayout(1, 4, 14, 0));
        jp_statsRow.setBackground(clr_bg);

        // Total Applications label
        jl_totalApplicationsValue = new JLabel("—");
        jl_totalApplicationsValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        jl_totalApplicationsValue.setForeground(clr_textDark);

        card_total = new JPanel(new BorderLayout());
        card_total.setBackground(clr_white);
        card_total.setBorder(new LineBorder(clr_cardBorder, 1));
        card_total.setPreferredSize(new Dimension(0, 110));
        inner_total = new JPanel(new GridLayout(2, 1, 0, 6));
        inner_total.setBackground(clr_white);
        inner_total.setBorder(new EmptyBorder(14, 14, 14, 14));
        title_total = new JLabel("Total Applications");
        title_total.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        title_total.setForeground(clr_stats_title);
        inner_total.add(title_total);
        inner_total.add(jl_totalApplicationsValue);
        card_total.add(inner_total, BorderLayout.CENTER);

        // Pending label
        jl_pendingApplicationsValue = new JLabel("—");
        jl_pendingApplicationsValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        jl_pendingApplicationsValue.setForeground(clr_textDark);

        card_pending = new JPanel(new BorderLayout());
        card_pending.setBackground(clr_white);
        card_pending.setBorder(new LineBorder(clr_cardBorder, 1));
        card_pending.setPreferredSize(new Dimension(0, 110));
        inner_pending = new JPanel(new GridLayout(2, 1, 0, 6));
        inner_pending.setBackground(clr_white);
        inner_pending.setBorder(new EmptyBorder(14, 14, 14, 14));
        title_pending = new JLabel("Pending");
        title_pending.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        title_pending.setForeground(clr_stats_title);
        inner_pending.add(title_pending);
        inner_pending.add(jl_pendingApplicationsValue);
        card_pending.add(inner_pending, BorderLayout.CENTER);

        // Selected label
        jl_selectedApplicationsValue = new JLabel("—");
        jl_selectedApplicationsValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        jl_selectedApplicationsValue.setForeground(clr_textDark);

        card_selected = new JPanel(new BorderLayout());
        card_selected.setBackground(clr_white);
        card_selected.setBorder(new LineBorder(clr_cardBorder, 1));
        card_selected.setPreferredSize(new Dimension(0, 110));
        inner_selected = new JPanel(new GridLayout(2, 1, 0, 6));
        inner_selected.setBackground(clr_white);
        inner_selected.setBorder(new EmptyBorder(14, 14, 14, 14));
        title_selected = new JLabel("Selected");
        title_selected.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        title_selected.setForeground(clr_stats_title);
        inner_selected.add(title_selected);
        inner_selected.add(jl_selectedApplicationsValue);
        card_selected.add(inner_selected, BorderLayout.CENTER);

        // Rejected label
        jl_rejectedApplicationsValue = new JLabel("—");
        jl_rejectedApplicationsValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        jl_rejectedApplicationsValue.setForeground(clr_textDark);

        card_rejected = new JPanel(new BorderLayout());
        card_rejected.setBackground(clr_white);
        card_rejected.setBorder(new LineBorder(clr_cardBorder, 1));
        card_rejected.setPreferredSize(new Dimension(0, 110));
        inner_rejected = new JPanel(new GridLayout(2, 1, 0, 6));
        inner_rejected.setBackground(clr_white);
        inner_rejected.setBorder(new EmptyBorder(14, 14, 14, 14));
        title_rejected = new JLabel("Rejected");
        title_rejected.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        title_rejected.setForeground(clr_stats_title);
        inner_rejected.add(title_rejected);
        inner_rejected.add(jl_rejectedApplicationsValue);
        card_rejected.add(inner_rejected, BorderLayout.CENTER);

        jp_statsRow.add(card_total);
        jp_statsRow.add(card_pending);
        jp_statsRow.add(card_selected);
        jp_statsRow.add(card_rejected);

        // quick actions panel
        jp_quickActionsTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_quickActionsTitlePanel.setBackground(clr_bg);
        jp_quickActionsTitlePanel.setBorder(new EmptyBorder(20, 0, 14, 0));

        jl_quickActionsTitle = new JLabel("Quick Actions");
        jl_quickActionsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jl_quickActionsTitle.setForeground(clr_quick_title);
        jp_quickActionsTitlePanel.add(jl_quickActionsTitle);

        // quick actions cards
        jp_quickActionsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        jp_quickActionsRow.setBackground(clr_bg);

        jp_quickActionsGrid = new JPanel(new GridLayout(1, 2, 14, 0));
        jp_quickActionsGrid.setBackground(clr_bg);
        jp_quickActionsGrid.setPreferredSize(new Dimension(900, 160));

        viewCardBackground = new Color(226, 238, 255);
        addCardBackground  = new Color(220, 245, 230);

        viewIcon = new ImageIcon(new ImageIcon("src/images/view_app.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        addIcon  = new ImageIcon(new ImageIcon("src/images/add.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));

        // View Applications card
        jp_viewApplicationsActionCard = new JPanel(new BorderLayout());
        jp_viewApplicationsActionCard.setBackground(viewCardBackground);
        jp_viewApplicationsActionCard.setBorder(new LineBorder(clr_cardBorder, 1));
        jp_viewApplicationsActionCard.setCursor(new Cursor(Cursor.HAND_CURSOR));

        jp_viewApplicationsInner = new JPanel(new GridLayout(2, 1, 0, 8));
        jp_viewApplicationsInner.setBackground(viewCardBackground);
        jp_viewApplicationsInner.setBorder(new EmptyBorder(16, 14, 16, 14));

        jl_viewIcon              = new JLabel(viewIcon, SwingConstants.CENTER);
        jl_viewApplicationsTitle = new JLabel("View Applications", SwingConstants.CENTER);
        jl_viewApplicationsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jl_viewApplicationsTitle.setForeground(clr_textDark);

        jp_viewApplicationsInner.add(jl_viewIcon);
        jp_viewApplicationsInner.add(jl_viewApplicationsTitle);
        jp_viewApplicationsActionCard.add(jp_viewApplicationsInner, BorderLayout.CENTER);

        // Add Application card
        jp_addApplicationActionCard = new JPanel(new BorderLayout());
        jp_addApplicationActionCard.setBackground(addCardBackground);
        jp_addApplicationActionCard.setBorder(new LineBorder(clr_cardBorder, 1));
        jp_addApplicationActionCard.setCursor(new Cursor(Cursor.HAND_CURSOR));

        jp_addApplicationInner = new JPanel(new GridLayout(2, 1, 0, 8));
        jp_addApplicationInner.setBackground(addCardBackground);
        jp_addApplicationInner.setBorder(new EmptyBorder(16, 14, 16, 14));

        jl_addIcon             = new JLabel(addIcon, SwingConstants.CENTER);
        jl_addApplicationTitle = new JLabel("Add Application", SwingConstants.CENTER);
        jl_addApplicationTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jl_addApplicationTitle.setForeground(clr_textDark);

        jp_addApplicationInner.add(jl_addIcon);
        jp_addApplicationInner.add(jl_addApplicationTitle);
        jp_addApplicationActionCard.add(jp_addApplicationInner, BorderLayout.CENTER);

        jp_quickActionsGrid.add(jp_viewApplicationsActionCard);
        jp_quickActionsGrid.add(jp_addApplicationActionCard);
        jp_quickActionsRow.add(jp_quickActionsGrid);

        // Main content panel
        jp_topContent = new JPanel(new BorderLayout());
        jp_topContent.setBackground(clr_bg);
        jp_topContent.add(jp_welcomeBannerWrapper, BorderLayout.NORTH);
        jp_topContent.add(jp_statsRow,             BorderLayout.CENTER);

        jp_quickActionsSection = new JPanel(new BorderLayout());
        jp_quickActionsSection.setBackground(clr_bg);
        jp_quickActionsSection.add(jp_quickActionsTitlePanel, BorderLayout.NORTH);
        jp_quickActionsSection.add(jp_quickActionsRow,        BorderLayout.CENTER);

        jp_centerContent = new JPanel(new BorderLayout());
        jp_centerContent.setBackground(clr_bg);
        jp_centerContent.add(jp_topContent,          BorderLayout.NORTH);
        jp_centerContent.add(jp_quickActionsSection, BorderLayout.CENTER);

        jp_content.add(jp_centerContent, BorderLayout.NORTH);
        add(jp_content, BorderLayout.CENTER);

        // Listeners

        jb_logout.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent me) {
            	jb_logout.setBackground(clr_blue_hover);
            	}
            public void mouseExited (MouseEvent me) {
            	jb_logout.setBackground(clr_logout_btn);
            	}
        });

        jb_logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int r = JOptionPane.showConfirmDialog(null, "Do you want to log out?", "Logout",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (r == JOptionPane.OK_OPTION) {
                    JOptionPane.showMessageDialog(null, "Logged out successfully.", "Logout", JOptionPane.INFORMATION_MESSAGE);
                    login.setVisible(true);
                    dispose();
                }
            }
        });

        jp_viewApplicationsActionCard.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent me) {
            	jp_viewApplicationsActionCard.setBorder(new LineBorder(clr_blue, 2));
            	}
            public void mouseExited (MouseEvent me) {
            	jp_viewApplicationsActionCard.setBorder(new LineBorder(clr_cardBorder, 1));
}
            public void mouseClicked(MouseEvent me) {
                ViewApplicationFrame viewFrame = new ViewApplicationFrame(AdminDashboardFrame.this, adminName);
                viewFrame.setSize(1100, 650);
                viewFrame.setLocationRelativeTo(null);
                viewFrame.setVisible(true);
                setVisible(false);
            }
        });

        jp_addApplicationActionCard.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent me) {
                jp_addApplicationActionCard.setBorder(new LineBorder(clr_blue, 2));
            }
            public void mouseExited(MouseEvent me) {
                jp_addApplicationActionCard.setBorder(new LineBorder(clr_cardBorder, 1));
            }
            public void mouseClicked(MouseEvent me) {
                AddApplicationFrame addFrame = new AddApplicationFrame(AdminDashboardFrame.this, adminName);
                addFrame.setSize(1100, 650);
                addFrame.setLocationRelativeTo(null);
                addFrame.setVisible(true);
                setVisible(false);
            }
        });

        // Load stats from DB on startup
        loadStats();
    }

    // refreshStats() — public so AddApplicationFrame can call it after INSERT
    public void refreshStats() {
        loadStats();
    }

    private void loadStats() {
        int total = 0, pending = 0, selected = 0, rejected = 0;
        String sql = "SELECT status, COUNT(*) AS cnt FROM application GROUP BY status";
        try (Connection con = DBConnection.getConnection();
             Statement  st  = con.createStatement();
             ResultSet  rs  = st.executeQuery(sql)) {
            while (rs.next()) {
                int cnt = rs.getInt("cnt");
                String status = rs.getString("status");
                total += cnt;
                if (status.equals("Pending")) {
                    pending = cnt;
                } else if (status.equals("Accepted")) {
                    selected = cnt;
                } else if (status.equals("Rejected")) {
                    rejected = cnt;
                }
            }
        } catch (Exception ex) {
            showDbError(ex);
        }
        jl_totalApplicationsValue  .setText(String.valueOf(total));
        jl_pendingApplicationsValue .setText(String.valueOf(pending));
        jl_selectedApplicationsValue.setText(String.valueOf(selected));
        jl_rejectedApplicationsValue.setText(String.valueOf(rejected));
    }

    static void showDbError(Exception ex) {
        JOptionPane.showMessageDialog(null,
            "Database error:\n" + ex.getMessage(),
            "DB Error", JOptionPane.ERROR_MESSAGE);
    }

    private String fetchAdminName(int userId) {
        String sql = "SELECT fullName FROM admin WHERE userId = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("fullName");
            }
        } catch (Exception ex) {
            showDbError(ex);
        }
        return "Admin";
    }
}
