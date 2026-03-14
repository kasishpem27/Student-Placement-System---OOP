package company;

import dbconnection.DBConnection;
import student.LoginFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CompanyDashboardFrame extends JFrame {

    // Theme colours
    private final Color clr_blue         = new Color(0, 102, 153);
    private final Color clr_blue_hover   = new Color(0, 122, 183);
    private final Color clr_white        = Color.WHITE;
    private final Color clr_bg           = new Color(245, 247, 250);
    private final Color clr_cardBorder   = new Color(220, 225, 230);
    private final Color clr_textDark     = new Color(40, 40, 40);

    private final Color clr_logout_btn = new Color(0, 140, 205);

    private final Color clr_welcome_subtitle = new Color(190, 220, 240);
    private final Color clr_stats_title      = new Color(90, 90, 90);
    private final Color clr_quick_title      = new Color(50, 50, 50);

    private final Color clr_create_card     = new Color(226, 238, 255);
    private final Color clr_applicants_card = new Color(220, 245, 230);
    private final Color clr_docs_card       = new Color(243, 232, 255);
    private final Color clr_academic_card   = new Color(255, 243, 220);

    // Header
    private JLabel jl_headerLogo;
    private JLabel jl_headerTitle;
    private JButton jb_logoutButton;

    // Stats labels
    private JLabel jl_totalPostingsTitle;
    private JLabel jl_totalPostingsValue;

    private JLabel jl_activePostingsTitle;
    private JLabel jl_activePostingsValue;

    private JLabel jl_totalApplicantsTitle;
    private JLabel jl_totalApplicantsValue;

    private JLabel jl_acceptedTitle;
    private JLabel jl_acceptedValue;

    // Quick action cards
    private JPanel jp_createOpportunityActionCard;
    private JPanel jp_viewApplicantsActionCard;
    private JPanel jp_viewDocumentsActionCard;
    private JPanel jp_academicDetailsActionCard;

    private int companyUserId;
    private int companyId;
    private String companyName = "Company";
    private int totalPostings, activePostings, totalApplicants, acceptedOffers;

    private LoginFrame login;

    public CompanyDashboardFrame(LoginFrame login, int companyUserId) {
        super("CareerConnect - Company Dashboard");

        this.login         = login;
        this.companyUserId = companyUserId;

        fetchCompanyInfo(companyUserId);

        setLayout(new BorderLayout());
        getContentPane().setBackground(clr_bg);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 750));

        loadStats();

        // ── Header Panel ──────────────────────────────────────────────────────
        JPanel jp_header = new JPanel(new BorderLayout());
        jp_header.setBackground(clr_blue);
        jp_header.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel jp_headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        jp_headerLeft.setBackground(clr_blue);

        URL logoUrl = getClass().getResource("/images/CareerConnect.png");
        jl_headerLogo = new JLabel();
        if (logoUrl != null) {
            Image scaled = new ImageIcon(logoUrl).getImage().getScaledInstance(38, 34, Image.SCALE_SMOOTH);
            jl_headerLogo = new JLabel(new ImageIcon(scaled));
        }
        jl_headerLogo.setHorizontalAlignment(SwingConstants.LEFT);

        jl_headerTitle = new JLabel("CareerConnect — Company Dashboard");
        jl_headerTitle.setForeground(clr_white);
        jl_headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        jp_headerLeft.add(jl_headerLogo);
        jp_headerLeft.add(jl_headerTitle);

        jb_logoutButton = new JButton("Logout");
        jb_logoutButton.setFocusPainted(false);
        jb_logoutButton.setBackground(clr_logout_btn);
        jb_logoutButton.setForeground(clr_white);
        jb_logoutButton.setBorderPainted(false);
        jb_logoutButton.setOpaque(true);
        jb_logoutButton.setPreferredSize(new Dimension(110, 34));
        jb_logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel jp_headerRightInner = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        jp_headerRightInner.setBackground(clr_blue);

        URL profileUrl = getClass().getResource("/images/profile.png");
        JLabel jl_profileIcon = new JLabel();
        if (profileUrl != null) {
            Image scaled = new ImageIcon(profileUrl).getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
            jl_profileIcon = new JLabel(new ImageIcon(scaled));
        }

        JLabel jl_userInfo = new JLabel(this.companyName + "  ·  COM" + this.companyId);
        jl_userInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_userInfo.setForeground(clr_white);

        jp_headerRightInner.add(jl_profileIcon);
        jp_headerRightInner.add(jl_userInfo);
        jp_headerRightInner.add(jb_logoutButton);

        JPanel jp_headerRight = new JPanel(new BorderLayout());
        jp_headerRight.setBackground(clr_blue);
        jp_headerRight.add(jp_headerRightInner, BorderLayout.CENTER);

        jp_header.add(jp_headerLeft, BorderLayout.WEST);
        jp_header.add(jp_headerRight, BorderLayout.EAST);

        add(jp_header, BorderLayout.NORTH);

        // ── Content Panel ─────────────────────────────────────────────────────
        JPanel jp_content = new JPanel(new BorderLayout());
        jp_content.setBackground(clr_bg);
        jp_content.setBorder(new EmptyBorder(18, 18, 18, 18));

        // Welcome Banner
        JPanel jp_welcomeBanner = new JPanel(new BorderLayout());
        jp_welcomeBanner.setBackground(clr_blue);
        jp_welcomeBanner.setBorder(new EmptyBorder(14, 18, 14, 18));

        JPanel jp_welcomeText = new JPanel(new GridLayout(2, 1, 0, 2));
        jp_welcomeText.setBackground(clr_blue);

        JLabel jl_welcomeTitle = new JLabel("Welcome back, " + this.companyName);
        jl_welcomeTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jl_welcomeTitle.setForeground(clr_white);

        JLabel jl_welcomeSubTitle = new JLabel("Company Dashboard");
        jl_welcomeSubTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_welcomeSubTitle.setForeground(clr_welcome_subtitle);

        jp_welcomeText.add(jl_welcomeTitle);
        jp_welcomeText.add(jl_welcomeSubTitle);
        jp_welcomeBanner.add(jp_welcomeText, BorderLayout.WEST);

        JPanel jp_welcomeBannerWrapper = new JPanel(new BorderLayout());
        jp_welcomeBannerWrapper.setBackground(clr_bg);
        jp_welcomeBannerWrapper.setBorder(new EmptyBorder(0, 0, 16, 0));
        jp_welcomeBannerWrapper.add(jp_welcomeBanner, BorderLayout.CENTER);

        // ── Stat Cards ────────────────────────────────────────────────────────
        JPanel jp_statsRow = new JPanel(new GridLayout(1, 4, 14, 0));
        jp_statsRow.setBackground(clr_bg);

        JPanel jp_totalPostingsCard = new JPanel(new BorderLayout());
        jp_totalPostingsCard.setBackground(clr_white);
        jp_totalPostingsCard.setBorder(new LineBorder(clr_cardBorder, 1));
        jp_totalPostingsCard.setPreferredSize(new Dimension(0, 110));

        JPanel jp_totalPostingsInner = new JPanel(new GridLayout(2, 1, 0, 6));
        jp_totalPostingsInner.setBackground(clr_white);
        jp_totalPostingsInner.setBorder(new EmptyBorder(14, 14, 14, 14));

        jl_totalPostingsTitle = new JLabel("Total Postings");
        jl_totalPostingsTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_totalPostingsTitle.setForeground(clr_stats_title);

        jl_totalPostingsValue = new JLabel(Integer.toString(totalPostings));
        jl_totalPostingsValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        jl_totalPostingsValue.setForeground(clr_textDark);

        jp_totalPostingsInner.add(jl_totalPostingsTitle);
        jp_totalPostingsInner.add(jl_totalPostingsValue);
        jp_totalPostingsCard.add(jp_totalPostingsInner, BorderLayout.CENTER);

        JPanel jp_activePostingsCard = new JPanel(new BorderLayout());
        jp_activePostingsCard.setBackground(clr_white);
        jp_activePostingsCard.setBorder(new LineBorder(clr_cardBorder, 1));

        JPanel jp_activePostingsInner = new JPanel(new GridLayout(2, 1, 0, 6));
        jp_activePostingsInner.setBackground(clr_white);
        jp_activePostingsInner.setBorder(new EmptyBorder(14, 14, 14, 14));

        jl_activePostingsTitle = new JLabel("Active Postings");
        jl_activePostingsTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_activePostingsTitle.setForeground(clr_stats_title);

        jl_activePostingsValue = new JLabel(Integer.toString(activePostings));
        jl_activePostingsValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        jl_activePostingsValue.setForeground(clr_textDark);

        jp_activePostingsInner.add(jl_activePostingsTitle);
        jp_activePostingsInner.add(jl_activePostingsValue);
        jp_activePostingsCard.add(jp_activePostingsInner, BorderLayout.CENTER);

        JPanel jp_totalApplicantsCard = new JPanel(new BorderLayout());
        jp_totalApplicantsCard.setBackground(clr_white);
        jp_totalApplicantsCard.setBorder(new LineBorder(clr_cardBorder, 1));

        JPanel jp_totalApplicantsInner = new JPanel(new GridLayout(2, 1, 0, 6));
        jp_totalApplicantsInner.setBackground(clr_white);
        jp_totalApplicantsInner.setBorder(new EmptyBorder(14, 14, 14, 14));

        jl_totalApplicantsTitle = new JLabel("Total Applicants");
        jl_totalApplicantsTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_totalApplicantsTitle.setForeground(clr_stats_title);

        jl_totalApplicantsValue = new JLabel(Integer.toString(totalApplicants));
        jl_totalApplicantsValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        jl_totalApplicantsValue.setForeground(clr_textDark);

        jp_totalApplicantsInner.add(jl_totalApplicantsTitle);
        jp_totalApplicantsInner.add(jl_totalApplicantsValue);
        jp_totalApplicantsCard.add(jp_totalApplicantsInner, BorderLayout.CENTER);

        JPanel jp_acceptedCard = new JPanel(new BorderLayout());
        jp_acceptedCard.setBackground(clr_white);
        jp_acceptedCard.setBorder(new LineBorder(clr_cardBorder, 1));

        JPanel jp_acceptedInner = new JPanel(new GridLayout(2, 1, 0, 6));
        jp_acceptedInner.setBackground(clr_white);
        jp_acceptedInner.setBorder(new EmptyBorder(14, 14, 14, 14));

        jl_acceptedTitle = new JLabel("Accepted Offers");
        jl_acceptedTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_acceptedTitle.setForeground(clr_stats_title);

        jl_acceptedValue = new JLabel(Integer.toString(acceptedOffers));
        jl_acceptedValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        jl_acceptedValue.setForeground(clr_textDark);

        jp_acceptedInner.add(jl_acceptedTitle);
        jp_acceptedInner.add(jl_acceptedValue);
        jp_acceptedCard.add(jp_acceptedInner, BorderLayout.CENTER);

        jp_statsRow.add(jp_totalPostingsCard);
        jp_statsRow.add(jp_activePostingsCard);
        jp_statsRow.add(jp_totalApplicantsCard);
        jp_statsRow.add(jp_acceptedCard);

        // ── Quick Actions ─────────────────────────────────────────────────────
        JPanel jp_quickActionsTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_quickActionsTitlePanel.setBackground(clr_bg);
        jp_quickActionsTitlePanel.setBorder(new EmptyBorder(20, 0, 14, 0));

        JLabel jl_quickActionsTitle = new JLabel("Quick Actions");
        jl_quickActionsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jl_quickActionsTitle.setForeground(clr_quick_title);
        jp_quickActionsTitlePanel.add(jl_quickActionsTitle);

        JPanel jp_quickActionsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        jp_quickActionsRow.setBackground(clr_bg);

        JPanel jp_quickActionsGrid = new JPanel(new GridLayout(1, 4, 14, 0));
        jp_quickActionsGrid.setBackground(clr_bg);
        jp_quickActionsGrid.setPreferredSize(new Dimension(900, 160));

        ImageIcon createIcon     = new ImageIcon(new ImageIcon(getClass().getResource("/images/icon_create.png")).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        ImageIcon applicantsIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/icon_applicants.png")).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        ImageIcon docsIcon       = new ImageIcon(new ImageIcon(getClass().getResource("/images/icon_documents.png")).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        ImageIcon academicIcon   = new ImageIcon(new ImageIcon(getClass().getResource("/images/icon_academic.png")).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));

        jp_createOpportunityActionCard = new JPanel(new BorderLayout());
        jp_createOpportunityActionCard.setBackground(clr_create_card);
        jp_createOpportunityActionCard.setBorder(new LineBorder(clr_cardBorder, 1));
        jp_createOpportunityActionCard.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel jp_createOpportunityInner = new JPanel(new GridLayout(2, 1, 0, 8));
        jp_createOpportunityInner.setBackground(clr_create_card);
        jp_createOpportunityInner.setBorder(new EmptyBorder(16, 14, 16, 14));

        JLabel jl_createIcon  = new JLabel(createIcon, SwingConstants.CENTER);
        JLabel jl_createTitle = new JLabel("Create Opportunity", SwingConstants.CENTER);
        jl_createTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jl_createTitle.setForeground(clr_textDark);

        jp_createOpportunityInner.add(jl_createIcon);
        jp_createOpportunityInner.add(jl_createTitle);
        jp_createOpportunityActionCard.add(jp_createOpportunityInner, BorderLayout.CENTER);

        jp_viewApplicantsActionCard = new JPanel(new BorderLayout());
        jp_viewApplicantsActionCard.setBackground(clr_applicants_card);
        jp_viewApplicantsActionCard.setBorder(new LineBorder(clr_cardBorder, 1));
        jp_viewApplicantsActionCard.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel jp_viewApplicantsInner = new JPanel(new GridLayout(2, 1, 0, 8));
        jp_viewApplicantsInner.setBackground(clr_applicants_card);
        jp_viewApplicantsInner.setBorder(new EmptyBorder(16, 14, 16, 14));

        JLabel jl_applicantsIcon  = new JLabel(applicantsIcon, SwingConstants.CENTER);
        JLabel jl_applicantsTitle = new JLabel("View Applicants", SwingConstants.CENTER);
        jl_applicantsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jl_applicantsTitle.setForeground(clr_textDark);

        jp_viewApplicantsInner.add(jl_applicantsIcon);
        jp_viewApplicantsInner.add(jl_applicantsTitle);
        jp_viewApplicantsActionCard.add(jp_viewApplicantsInner, BorderLayout.CENTER);

        jp_viewDocumentsActionCard = new JPanel(new BorderLayout());
        jp_viewDocumentsActionCard.setBackground(clr_docs_card);
        jp_viewDocumentsActionCard.setBorder(new LineBorder(clr_cardBorder, 1));
        jp_viewDocumentsActionCard.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel jp_viewDocumentsInner = new JPanel(new GridLayout(2, 1, 0, 8));
        jp_viewDocumentsInner.setBackground(clr_docs_card);
        jp_viewDocumentsInner.setBorder(new EmptyBorder(16, 14, 16, 14));

        JLabel jl_docsIcon  = new JLabel(docsIcon, SwingConstants.CENTER);
        JLabel jl_docsTitle = new JLabel("View Documents", SwingConstants.CENTER);
        jl_docsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jl_docsTitle.setForeground(clr_textDark);

        jp_viewDocumentsInner.add(jl_docsIcon);
        jp_viewDocumentsInner.add(jl_docsTitle);
        jp_viewDocumentsActionCard.add(jp_viewDocumentsInner, BorderLayout.CENTER);

        jp_academicDetailsActionCard = new JPanel(new BorderLayout());
        jp_academicDetailsActionCard.setBackground(clr_academic_card);
        jp_academicDetailsActionCard.setBorder(new LineBorder(clr_cardBorder, 1));
        jp_academicDetailsActionCard.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel jp_academicDetailsInner = new JPanel(new GridLayout(2, 1, 0, 8));
        jp_academicDetailsInner.setBackground(clr_academic_card);
        jp_academicDetailsInner.setBorder(new EmptyBorder(16, 14, 16, 14));

        JLabel jl_academicIcon  = new JLabel(academicIcon, SwingConstants.CENTER);
        JLabel jl_academicTitle = new JLabel("Academic Details", SwingConstants.CENTER);
        jl_academicTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jl_academicTitle.setForeground(clr_textDark);

        jp_academicDetailsInner.add(jl_academicIcon);
        jp_academicDetailsInner.add(jl_academicTitle);
        jp_academicDetailsActionCard.add(jp_academicDetailsInner, BorderLayout.CENTER);

        jp_quickActionsGrid.add(jp_createOpportunityActionCard);
        jp_quickActionsGrid.add(jp_viewApplicantsActionCard);
        jp_quickActionsGrid.add(jp_viewDocumentsActionCard);
        jp_quickActionsGrid.add(jp_academicDetailsActionCard);

        jp_quickActionsRow.add(jp_quickActionsGrid);

        // ── Main Layout Assembly ──────────────────────────────────────────────
        JPanel jp_topContent = new JPanel(new BorderLayout());
        jp_topContent.setBackground(clr_bg);
        jp_topContent.add(jp_welcomeBannerWrapper, BorderLayout.NORTH);
        jp_topContent.add(jp_statsRow, BorderLayout.CENTER);

        JPanel jp_quickActionsSection = new JPanel(new BorderLayout());
        jp_quickActionsSection.setBackground(clr_bg);
        jp_quickActionsSection.add(jp_quickActionsTitlePanel, BorderLayout.NORTH);
        jp_quickActionsSection.add(jp_quickActionsRow, BorderLayout.CENTER);

        JPanel jp_centerContent = new JPanel(new BorderLayout());
        jp_centerContent.setBackground(clr_bg);
        jp_centerContent.add(jp_topContent, BorderLayout.NORTH);
        jp_centerContent.add(jp_quickActionsSection, BorderLayout.CENTER);

        jp_content.add(jp_centerContent, BorderLayout.NORTH);
        add(jp_content, BorderLayout.CENTER);

        // ── Listeners ─────────────────────────────────────────────────────────
        jb_logoutButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { jb_logoutButton.setBackground(clr_blue_hover); }
            public void mouseExited(MouseEvent e)  { jb_logoutButton.setBackground(clr_logout_btn); }
        });

        jb_logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(
                        CompanyDashboardFrame.this,
                        "Do you want to log out?",
                        "Logout",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (result == JOptionPane.OK_OPTION) {
                    login.setVisible(true);
                    dispose();
                }
            }
        });

        jp_createOpportunityActionCard.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { jp_createOpportunityActionCard.setBorder(new LineBorder(clr_blue, 2)); }
            public void mouseExited(MouseEvent e)  { jp_createOpportunityActionCard.setBorder(new LineBorder(clr_cardBorder, 1)); }
            public void mouseClicked(MouseEvent e) {
                CreateOpportunityFrame frame = new CreateOpportunityFrame(CompanyDashboardFrame.this.companyName, String.valueOf(CompanyDashboardFrame.this.companyId), CompanyDashboardFrame.this);
                frame.setVisible(true);
                setVisible(false);
            }
        });

        jp_viewApplicantsActionCard.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { jp_viewApplicantsActionCard.setBorder(new LineBorder(clr_blue, 2)); }
            public void mouseExited(MouseEvent e)  { jp_viewApplicantsActionCard.setBorder(new LineBorder(clr_cardBorder, 1)); }
            public void mouseClicked(MouseEvent e) {
                ViewApplicantsFrame frame = new ViewApplicantsFrame(CompanyDashboardFrame.this.companyName, String.valueOf(CompanyDashboardFrame.this.companyId), CompanyDashboardFrame.this);
                frame.setVisible(true);
                setVisible(false);
            }
        });

        jp_viewDocumentsActionCard.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { jp_viewDocumentsActionCard.setBorder(new LineBorder(clr_blue, 2)); }
            public void mouseExited(MouseEvent e)  { jp_viewDocumentsActionCard.setBorder(new LineBorder(clr_cardBorder, 1)); }
            public void mouseClicked(MouseEvent e) {
                ViewDocumentsFrame frame = new ViewDocumentsFrame(CompanyDashboardFrame.this.companyName, String.valueOf(CompanyDashboardFrame.this.companyId), CompanyDashboardFrame.this);
                frame.setVisible(true);
                setVisible(false);
            }
        });

        jp_academicDetailsActionCard.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { jp_academicDetailsActionCard.setBorder(new LineBorder(clr_blue, 2)); }
            public void mouseExited(MouseEvent e)  { jp_academicDetailsActionCard.setBorder(new LineBorder(clr_cardBorder, 1)); }
            public void mouseClicked(MouseEvent e) {
                ViewAcademicDetailsFrame frame = new ViewAcademicDetailsFrame(CompanyDashboardFrame.this.companyName, String.valueOf(CompanyDashboardFrame.this.companyId), CompanyDashboardFrame.this);
                frame.setVisible(true);
                setVisible(false);
            }
        });

        setLocationRelativeTo(null);
    }

    public void refreshStats() {
        loadStats();
        jl_totalPostingsValue.setText(Integer.toString(totalPostings));
        jl_activePostingsValue.setText(Integer.toString(activePostings));
        jl_totalApplicantsValue.setText(Integer.toString(totalApplicants));
        jl_acceptedValue.setText(Integer.toString(acceptedOffers));
    }

    private void loadStats() {
        try (Connection con = DBConnection.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM recruitmentPosting WHERE companyId = ?")) {
                ps.setInt(1, companyId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        totalPostings = rs.getInt(1);
                    }
                }
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM recruitmentPosting WHERE companyId = ? AND applicationDeadline >= CURDATE() AND isActive = 1")) {
                ps.setInt(1, companyId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        activePostings = rs.getInt(1);
                    }
                }
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM application a JOIN recruitmentPosting rp ON a.postingId = rp.postingId WHERE rp.companyId = ?")) {
                ps.setInt(1, companyId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        totalApplicants = rs.getInt(1);
                    }
                }
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM application a JOIN recruitmentPosting rp ON a.postingId = rp.postingId WHERE rp.companyId = ? AND LOWER(a.status) = 'accepted'")) {
                ps.setInt(1, companyId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        acceptedOffers = rs.getInt(1);
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "DB error while loading dashboard stats:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void fetchCompanyInfo(int userId) {
        String sql = "SELECT companyId, companyName FROM company WHERE userId = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    this.companyId   = rs.getInt("companyId");
                    this.companyName = rs.getString("companyName");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "DB error while fetching company info:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}