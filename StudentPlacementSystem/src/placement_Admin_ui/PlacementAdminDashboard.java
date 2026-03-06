package placement_Admin_ui;
import student.LoginFrame;
import dbconnection.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

;
public class PlacementAdminDashboard extends JFrame {

    //THEME COLORS
	private int adminUserId;
    private final Color clr_blue = new Color(0, 102, 153);
    private final Color clr_blue_hover = new Color(0, 122, 183);
    private final Color clr_bg = new Color(245, 248, 252);
    private final Color clr_white = Color.WHITE;
    private final Color clr_cardBorder = new Color(220, 230, 240);
    private final Color clr_textDark = new Color(40, 40, 40);
    private LoginFrame login;

    public PlacementAdminDashboard(LoginFrame login ,int adminUserId) {
    	this.adminUserId = adminUserId;
    	this.login = login;

        setTitle("Placement Admin Dashboard");
        setLayout(new BorderLayout());
        getContentPane().setBackground(clr_bg);

        //  HEADER
        JPanel jp_header = new JPanel(new BorderLayout());
        jp_header.setBackground(clr_blue);
        jp_header.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel jp_headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        jp_headerLeft.setBackground(clr_blue);

        ImageIcon rawLogo = new ImageIcon("src/images/CareerConnect.png");
        Image scaledLogo = rawLogo.getImage().getScaledInstance(38, 34, Image.SCALE_SMOOTH);
        JLabel jl_headerLogo = new JLabel(new ImageIcon(scaledLogo));

        JLabel jl_headerTitle = new JLabel("CareerConnect — Placement Admin Dashboard");
        jl_headerTitle.setForeground(Color.WHITE);
        jl_headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));

        jp_headerLeft.add(jl_headerLogo);
        jp_headerLeft.add(jl_headerTitle);

        JPanel jp_headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        jp_headerRight.setBackground(clr_blue);

        ImageIcon rawProfile = new ImageIcon("src/images/profile.png");
        Image scaledProfile = rawProfile.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        JLabel jl_profileIcon = new JLabel(new ImageIcon(scaledProfile));
        
        String adminName = getAdminName();
        JLabel jl_adminInfo = new JLabel(adminName + " · PLA" + adminUserId);
        jl_adminInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_adminInfo.setForeground(Color.WHITE);

        ImageIcon rawLogout = new ImageIcon("src/images/logout.png");
        Image scaledLogout = rawLogout.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
        JLabel jl_logoutIcon = new JLabel(new ImageIcon(scaledLogout));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFocusPainted(false);
        logoutButton.setBackground(new Color(0, 140, 205));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorderPainted(false);
        logoutButton.setOpaque(true);
        logoutButton.setPreferredSize(new Dimension(110, 34));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        logoutButton.add(jl_logoutIcon);

        jp_headerRight.add(jl_profileIcon);
        jp_headerRight.add(jl_adminInfo);
        jp_headerRight.add(logoutButton);

        jp_header.add(jp_headerLeft, BorderLayout.WEST);
        jp_header.add(jp_headerRight, BorderLayout.EAST);

        add(jp_header, BorderLayout.NORTH);

        //CONTENT 
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(clr_bg);
        content.setBorder(new EmptyBorder(18, 18, 18, 18));

        // WELCOME BANNER 
        JPanel jp_welcomeBanner = new JPanel(new BorderLayout());
        jp_welcomeBanner.setBackground(clr_blue);
        jp_welcomeBanner.setBorder(new EmptyBorder(14, 18, 14, 18));

        JPanel jp_welcomeText = new JPanel(new GridLayout(2, 1, 0, 2));
        jp_welcomeText.setBackground(clr_blue);

        JLabel jl_welcomeTitle = new JLabel("Welcome back, " + adminName);
        jl_welcomeTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jl_welcomeTitle.setForeground(Color.WHITE);

        JLabel jl_welcomeSubTitle = new JLabel("Placement Admin Dashboard");
        jl_welcomeSubTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_welcomeSubTitle.setForeground(new Color(190, 220, 240));

        jp_welcomeText.add(jl_welcomeTitle);
        jp_welcomeText.add(jl_welcomeSubTitle);

        jp_welcomeBanner.add(jp_welcomeText, BorderLayout.WEST);

        // STAT CARDS
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 14, 0));
        statsRow.setBackground(clr_bg);

        statsRow.add(createStatCard("Total Students", getCount("student")));
        statsRow.add(createStatCard("Total Companies", getCount("company")));
        statsRow.add(createStatCard("Total Recruitments", getCount("recruitmentPosting")));
        statsRow.add(createStatCard("Total Placed Students", getPlacedCount()));

        // QUICK ACTION TITLE
        JLabel quickTitle = new JLabel("Quick Actions");
        quickTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        quickTitle.setBorder(new EmptyBorder(20, 0, 14, 0));
        quickTitle.setForeground(new Color(50, 50, 50));

        // QUICK ACTION CARDS
        JPanel quickGrid = new JPanel(new GridLayout(1, 4, 14, 0));
        quickGrid.setBackground(clr_bg);
        quickGrid.setPreferredSize(new Dimension(500, 160));

        quickGrid.add(createActionCard("Student Management",
                "/images/icons8-student-64 (1).png",
                new StudentManagementListener(),
                new Color(226, 238, 255)));

        quickGrid.add(createActionCard("Filter Students",
                "/images/icons8-filter-50 (1).png",
                new FilterListener(),
                new Color(220, 245, 230)));

        quickGrid.add(createActionCard("View Companies",
                "/images/icons8-company-64.png",
                new CompaniesListener(),
                new Color(243, 232, 255)));

        quickGrid.add(createActionCard("View Recruitments",
                "/images/icons8-recruitment-64 (1).png",
                new RecruitmentsListener(),
                new Color(255, 243, 220)));

        JPanel topContent = new JPanel(new BorderLayout());
        topContent.setBackground(clr_bg);

        JPanel welcomeWrapper = new JPanel(new BorderLayout());
        welcomeWrapper.setBackground(clr_bg);
        welcomeWrapper.setBorder(new EmptyBorder(0, 0, 18, 0));
        welcomeWrapper.add(jp_welcomeBanner, BorderLayout.CENTER);

        topContent.add(welcomeWrapper, BorderLayout.NORTH);
        topContent.add(statsRow, BorderLayout.CENTER);

        JPanel centerContent = new JPanel(new BorderLayout());
        centerContent.setBackground(clr_bg);
        centerContent.add(topContent, BorderLayout.NORTH);
        centerContent.add(quickTitle, BorderLayout.CENTER);
        centerContent.add(quickGrid, BorderLayout.SOUTH);

        content.add(centerContent, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);

        logoutButton.addActionListener(new LogoutListener());

        setSize(1100, 700);
        setLocationRelativeTo(null);
    }
    private String getAdminName() {

        String name = "Admin";

        String sql = "SELECT username FROM user WHERE userId = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, adminUserId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                name = rs.getString("username");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return name;
    }

    // STAT CARD 
    private JPanel createStatCard(String title, String value) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(clr_cardBorder, 1));

        JPanel inner = new JPanel(new GridLayout(2, 1, 0, 6));
        inner.setBackground(Color.WHITE);
        inner.setBorder(new EmptyBorder(14, 14, 14, 14));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitle.setForeground(new Color(90, 90, 90));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValue.setForeground(clr_textDark);

        inner.add(lblTitle);
        inner.add(lblValue);
        card.add(inner, BorderLayout.CENTER);

        return card;
    }

    // ACTION CARD 
    private JPanel createActionCard(String text,
                                    String iconPath,
                                    ActionListener listener,
                                    Color bgColor) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(new LineBorder(clr_cardBorder, 1));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel inner = new JPanel(new GridLayout(2, 1, 0, 8));
        inner.setBackground(bgColor);
        inner.setBorder(new EmptyBorder(16, 14, 16, 14));

        ImageIcon rawIcon = new ImageIcon(getClass().getResource(iconPath));
        Image scaledIcon = rawIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledIcon), SwingConstants.CENTER);

        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(clr_textDark);

        inner.add(iconLabel);
        inner.add(lbl);

        card.add(inner, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                listener.actionPerformed(null);
            }
        });

        return card;
    }

    // DATABASE METHODS 
    private String getCount(String tableName) {
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName)) {

            if (rs.next()) return rs.getString(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    private String getPlacedCount() {
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT COUNT(*) FROM student WHERE placementStatus = 1")) {

            if (rs.next()) return rs.getString(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }
    
    

    //LISTENERS 
    private class StudentManagementListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	StudentManagementFrame frame =
        	        new StudentManagementFrame(PlacementAdminDashboard.this, adminUserId);

        	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	frame.setSize(1400,700);
        	frame.setLocationRelativeTo(null);
        	frame.setVisible(true);

        	setVisible(false);;
        }
    }

    private class FilterListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

        	FilterStudentsFrame frame =
        	        new FilterStudentsFrame(PlacementAdminDashboard.this, adminUserId);

        	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	frame.setSize(1400,700);
        	frame.setLocationRelativeTo(null);
        	frame.setVisible(true);

        	setVisible(false);;
        }
    }

    private class CompaniesListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	ViewCompaniesFrame frame =
        	        new ViewCompaniesFrame(PlacementAdminDashboard.this, adminUserId);

        	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	frame.setSize(1400,700);
        	frame.setLocationRelativeTo(null);
        	frame.setVisible(true);

        	setVisible(false);;
        }
    }

    private class RecruitmentsListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	ViewRecruitmentsFrame frame =
        	        new ViewRecruitmentsFrame(PlacementAdminDashboard.this, adminUserId);

        	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	frame.setSize(1400,700);
        	frame.setLocationRelativeTo(null);
        	frame.setVisible(true);

        	setVisible(false);;
        }
    }
    
    
 // LOGOUT LISTENER
    private class LogoutListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            int choice = JOptionPane.showConfirmDialog(
                    PlacementAdminDashboard.this,
                    "Are you sure you want to logout?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
            	login.setVisible(true);
                dispose(); // close dashboard

               
            }
        }
    }

    
}