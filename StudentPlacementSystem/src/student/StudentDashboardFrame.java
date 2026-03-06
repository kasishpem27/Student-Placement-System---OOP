package student;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import dbconnection.DBConnection;


public class StudentDashboardFrame extends JFrame {

    // Theme colours
    private final Color clr_blue = new Color(0, 102, 153);
    private final Color clr_blue_hover = new Color(0, 122, 183);
    private final Color clr_white = Color.WHITE;
    private final Color clr_bg = new Color(245, 248, 252);
    private final Color clr_cardBorder = new Color(220, 230, 240);
    private final Color clr_textDark = new Color(40, 40, 40);

    private final Color clr_logout_btn = new Color(0, 140, 205);

    private final Color clr_welcome_subtitle = new Color(190, 220, 240);
    private final Color clr_stats_title = new Color(90, 90, 90);
    private final Color clr_quick_title = new Color(50, 50, 50);

    private final Color clr_search_card = new Color(226, 238, 255);
    private final Color clr_applications_card = new Color(220, 245, 230);
    private final Color clr_offcampus_card = new Color(243, 232, 255);
    private final Color clr_offers_card = new Color(255, 243, 220);

    // Header
    private JLabel jl_headerLogo;
    private JLabel jl_headerTitle;
    private JButton jb_logoutButton;

    // ================= STATS LABELS =================
    private JLabel jl_totalApplicationsTitle;
    private JLabel jl_totalApplicationsValue;

    private JLabel jl_pendingApplicationsTitle;
    private JLabel jl_pendingApplicationsValue;

    private JLabel jl_acceptedApplicationsTitle;
    private JLabel jl_acceptedApplicationsValue;

    private JLabel jl_rejectedApplicationsTitle;
    private JLabel jl_rejectedApplicationsValue;

    // Quick Actions
    private JPanel jp_searchPostingsActionCard;
    private JPanel jp_myApplicationsActionCard;
    private JPanel jp_offCampusActionCard;
    
    private LoginFrame login;
    private String studentName, studentUserName;
    private int userId, studentId;
    private int totalApplicationsCount, pendingCount, acceptedCount, rejectedCount;

    public StudentDashboardFrame(LoginFrame login, int userId) {
        super("Student Dashboard");

        setLayout(new BorderLayout());
        getContentPane().setBackground(clr_bg);
        setResizable(false);
        this.login = login;
        this.userId = userId;
        fetchStudentNameAndId();
        
        // Header Panel
        JPanel jp_header = new JPanel(new BorderLayout());
        jp_header.setBackground(clr_blue);
        jp_header.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel jp_headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        jp_headerLeft.setBackground(clr_blue);
        
        
        ImageIcon rawLogoIcon = new ImageIcon("src/images/CareerConnect.png");
        Image scaledLogoIcon = rawLogoIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        jl_headerLogo = new JLabel(new ImageIcon(scaledLogoIcon));
        
        jl_headerLogo.setHorizontalAlignment(SwingConstants.LEFT);

        jl_headerTitle = new JLabel("CareerConnect — Student Dashboard");
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

        ImageIcon rawProfileIcon = new ImageIcon("src/images/profile.png");
        Image scaledProfileIcon = rawProfileIcon.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
        JLabel jl_profileIcon = new JLabel(new ImageIcon(scaledProfileIcon));

        JLabel jl_userInfo = new JLabel(studentUserName + " · STU" + studentId);
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

        // Content Panel
        JPanel jp_content = new JPanel(new BorderLayout());
        jp_content.setBackground(clr_bg);
        jp_content.setBorder(new EmptyBorder(18, 18, 18, 18));

        // Welcome Banner + Text
        JPanel jp_welcomeBanner = new JPanel(new BorderLayout());
        jp_welcomeBanner.setBackground(clr_blue);
        jp_welcomeBanner.setBorder(new EmptyBorder(14, 18, 14, 18));

        JPanel jp_welcomeText = new JPanel(new GridLayout(2, 1, 0, 2));
        jp_welcomeText.setBackground(clr_blue);

        JLabel jl_welcomeTitle = new JLabel("Welcome back, " + studentName);
        jl_welcomeTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jl_welcomeTitle.setForeground(clr_white);

        JLabel jl_welcomeSubTitle = new JLabel("Student Dashboard");
        jl_welcomeSubTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_welcomeSubTitle.setForeground(clr_welcome_subtitle);

        jp_welcomeText.add(jl_welcomeTitle);
        jp_welcomeText.add(jl_welcomeSubTitle);
        jp_welcomeBanner.add(jp_welcomeText, BorderLayout.WEST);

        JPanel jp_welcomeBannerWrapper = new JPanel(new BorderLayout());
        jp_welcomeBannerWrapper.setBackground(clr_bg);
        jp_welcomeBannerWrapper.setBorder(new EmptyBorder(0, 0, 16, 0));
        jp_welcomeBannerWrapper.add(jp_welcomeBanner, BorderLayout.CENTER);

        // Stat Cards
        JPanel jp_statsRow = new JPanel(new GridLayout(1, 4, 14, 0));
        jp_statsRow.setBackground(clr_bg);
        
        calculateGridStats();

        JPanel jp_totalApplicationsCard = new JPanel(new BorderLayout());
        jp_totalApplicationsCard.setBackground(clr_white);
        jp_totalApplicationsCard.setBorder(new LineBorder(clr_cardBorder, 1));
        jp_totalApplicationsCard.setPreferredSize(new Dimension(0, 110));

        JPanel jp_totalApplicationsInner = new JPanel(new GridLayout(2, 1, 0, 6));
        jp_totalApplicationsInner.setBackground(clr_white);
        jp_totalApplicationsInner.setBorder(new EmptyBorder(14, 14, 14, 14));

        jl_totalApplicationsTitle = new JLabel("Total Applications");
        jl_totalApplicationsTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_totalApplicationsTitle.setForeground(clr_stats_title);

        jl_totalApplicationsValue = new JLabel(Integer.toString(totalApplicationsCount));
        jl_totalApplicationsValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        jl_totalApplicationsValue.setForeground(clr_textDark);

        jp_totalApplicationsInner.add(jl_totalApplicationsTitle);
        jp_totalApplicationsInner.add(jl_totalApplicationsValue);
        jp_totalApplicationsCard.add(jp_totalApplicationsInner, BorderLayout.CENTER);

        JPanel jp_pendingApplicationsCard = new JPanel(new BorderLayout());
        jp_pendingApplicationsCard.setBackground(clr_white);
        jp_pendingApplicationsCard.setBorder(new LineBorder(clr_cardBorder, 1));

        JPanel jp_pendingApplicationsInner = new JPanel(new GridLayout(2, 1, 0, 6));
        jp_pendingApplicationsInner.setBackground(clr_white);
        jp_pendingApplicationsInner.setBorder(new EmptyBorder(14, 14, 14, 14));

        jl_pendingApplicationsTitle = new JLabel("Pending Applications");
        jl_pendingApplicationsTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_pendingApplicationsTitle.setForeground(clr_stats_title);

        jl_pendingApplicationsValue = new JLabel(Integer.toString(pendingCount));
        jl_pendingApplicationsValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        jl_pendingApplicationsValue.setForeground(clr_textDark);

        jp_pendingApplicationsInner.add(jl_pendingApplicationsTitle);
        jp_pendingApplicationsInner.add(jl_pendingApplicationsValue);
        jp_pendingApplicationsCard.add(jp_pendingApplicationsInner, BorderLayout.CENTER);

        JPanel jp_acceptedApplicationsCard = new JPanel(new BorderLayout());
        jp_acceptedApplicationsCard.setBackground(clr_white);
        jp_acceptedApplicationsCard.setBorder(new LineBorder(clr_cardBorder, 1));

        JPanel jp_acceptedApplicationsInner = new JPanel(new GridLayout(2, 1, 0, 6));
        jp_acceptedApplicationsInner.setBackground(clr_white);
        jp_acceptedApplicationsInner.setBorder(new EmptyBorder(14, 14, 14, 14));

        jl_acceptedApplicationsTitle = new JLabel("Accepted");
        jl_acceptedApplicationsTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_acceptedApplicationsTitle.setForeground(clr_stats_title);

        jl_acceptedApplicationsValue = new JLabel(Integer.toString(acceptedCount));
        jl_acceptedApplicationsValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        jl_acceptedApplicationsValue.setForeground(clr_textDark);

        jp_acceptedApplicationsInner.add(jl_acceptedApplicationsTitle);
        jp_acceptedApplicationsInner.add(jl_acceptedApplicationsValue);
        jp_acceptedApplicationsCard.add(jp_acceptedApplicationsInner, BorderLayout.CENTER);

        JPanel jp_rejectedApplicationsCard = new JPanel(new BorderLayout());
        jp_rejectedApplicationsCard.setBackground(clr_white);
        jp_rejectedApplicationsCard.setBorder(new LineBorder(clr_cardBorder, 1));

        JPanel jp_rejectedApplicationsInner = new JPanel(new GridLayout(2, 1, 0, 6));
        jp_rejectedApplicationsInner.setBackground(clr_white);
        jp_rejectedApplicationsInner.setBorder(new EmptyBorder(14, 14, 14, 14));

        jl_rejectedApplicationsTitle = new JLabel("Rejected");
        jl_rejectedApplicationsTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_rejectedApplicationsTitle.setForeground(clr_stats_title);

        jl_rejectedApplicationsValue = new JLabel(Integer.toString(rejectedCount));
        jl_rejectedApplicationsValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        jl_rejectedApplicationsValue.setForeground(clr_textDark);

        jp_rejectedApplicationsInner.add(jl_rejectedApplicationsTitle);
        jp_rejectedApplicationsInner.add(jl_rejectedApplicationsValue);
        jp_rejectedApplicationsCard.add(jp_rejectedApplicationsInner, BorderLayout.CENTER);

        jp_statsRow.add(jp_totalApplicationsCard);
        jp_statsRow.add(jp_pendingApplicationsCard);
        jp_statsRow.add(jp_acceptedApplicationsCard);
        jp_statsRow.add(jp_rejectedApplicationsCard);

        // Quick Actions
        JPanel jp_quickActionsTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_quickActionsTitlePanel.setBackground(clr_bg);
        jp_quickActionsTitlePanel.setBorder(new EmptyBorder(20, 0, 14, 0));

        JLabel jl_quickActionsTitle = new JLabel("Quick Actions");
        jl_quickActionsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jl_quickActionsTitle.setForeground(clr_quick_title);
        jp_quickActionsTitlePanel.add(jl_quickActionsTitle);

        // Quick Actions Card
        JPanel jp_quickActionsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        jp_quickActionsRow.setBackground(clr_bg);

        JPanel jp_quickActionsGrid = new JPanel(new GridLayout(1, 4, 14, 0));
        jp_quickActionsGrid.setBackground(clr_bg);
        jp_quickActionsGrid.setPreferredSize(new Dimension(900, 160));

        ImageIcon searchIcon = new ImageIcon(new ImageIcon("src/images/Search.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        ImageIcon applicationsIcon = new ImageIcon(new ImageIcon("src/images/Applications.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        ImageIcon offCampusIcon = new ImageIcon(new ImageIcon("src/images/OffCampus.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        ImageIcon offersIcon = new ImageIcon(new ImageIcon("src/images/Offers.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));

        JPanel jp_searchPostingsActionCard = new JPanel(new BorderLayout());
        jp_searchPostingsActionCard.setBackground(clr_search_card);
        jp_searchPostingsActionCard.setBorder(new LineBorder(clr_cardBorder, 1));
        jp_searchPostingsActionCard.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel jp_searchPostingsInner = new JPanel(new GridLayout(2, 1, 0, 8));
        jp_searchPostingsInner.setBackground(clr_search_card);
        jp_searchPostingsInner.setBorder(new EmptyBorder(16, 14, 16, 14));

        JLabel jl_searchIcon = new JLabel(searchIcon, SwingConstants.CENTER);
        JLabel jl_searchPostingsTitle = new JLabel("Search Postings", SwingConstants.CENTER);
        jl_searchPostingsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jl_searchPostingsTitle.setForeground(clr_textDark);

        jp_searchPostingsInner.add(jl_searchIcon);
        jp_searchPostingsInner.add(jl_searchPostingsTitle);
        jp_searchPostingsActionCard.add(jp_searchPostingsInner, BorderLayout.CENTER);

        JPanel jp_myApplicationsActionCard = new JPanel(new BorderLayout());
        jp_myApplicationsActionCard.setBackground(clr_applications_card);
        jp_myApplicationsActionCard.setBorder(new LineBorder(clr_cardBorder, 1));
        jp_myApplicationsActionCard.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel jp_myApplicationsInner = new JPanel(new GridLayout(2, 1, 0, 8));
        jp_myApplicationsInner.setBackground(clr_applications_card);
        jp_myApplicationsInner.setBorder(new EmptyBorder(16, 14, 16, 14));

        JLabel jl_myApplicationsIcon = new JLabel(applicationsIcon, SwingConstants.CENTER);
        JLabel jl_myApplicationsTitle = new JLabel("My Applications", SwingConstants.CENTER);
        jl_myApplicationsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jl_myApplicationsTitle.setForeground(clr_textDark);

        jp_myApplicationsInner.add(jl_myApplicationsIcon);
        jp_myApplicationsInner.add(jl_myApplicationsTitle);
        jp_myApplicationsActionCard.add(jp_myApplicationsInner, BorderLayout.CENTER);

        JPanel jp_offCampusActionCard = new JPanel(new BorderLayout());
        jp_offCampusActionCard.setBackground(clr_offcampus_card);
        jp_offCampusActionCard.setBorder(new LineBorder(clr_cardBorder, 1));
        jp_offCampusActionCard.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel jp_offCampusInner = new JPanel(new GridLayout(2, 1, 0, 8));
        jp_offCampusInner.setBackground(clr_offcampus_card);
        jp_offCampusInner.setBorder(new EmptyBorder(16, 14, 16, 14));

        JLabel jl_offCampusIcon = new JLabel(offCampusIcon, SwingConstants.CENTER);
        JLabel jl_offCampusTitle = new JLabel("Off-Campus Application", SwingConstants.CENTER);
        jl_offCampusTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jl_offCampusTitle.setForeground(clr_textDark);

        jp_offCampusInner.add(jl_offCampusIcon);
        jp_offCampusInner.add(jl_offCampusTitle);
        jp_offCampusActionCard.add(jp_offCampusInner, BorderLayout.CENTER);

        JPanel jp_myOffersActionCard = new JPanel(new BorderLayout());
        jp_myOffersActionCard.setBackground(clr_offers_card);
        jp_myOffersActionCard.setBorder(new LineBorder(clr_cardBorder, 1));
        jp_myOffersActionCard.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel jp_myOffersInner = new JPanel(new GridLayout(2, 1, 0, 8));
        jp_myOffersInner.setBackground(clr_offers_card);
        jp_myOffersInner.setBorder(new EmptyBorder(16, 14, 16, 14));

        JLabel jl_myOffersIcon = new JLabel(offersIcon, SwingConstants.CENTER);
        JLabel jl_myOffersTitle = new JLabel("My Offers", SwingConstants.CENTER);
        jl_myOffersTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jl_myOffersTitle.setForeground(clr_textDark);

        jp_myOffersInner.add(jl_myOffersIcon);
        jp_myOffersInner.add(jl_myOffersTitle);
        jp_myOffersActionCard.add(jp_myOffersInner, BorderLayout.CENTER);

        jp_quickActionsGrid.add(jp_searchPostingsActionCard);
        jp_quickActionsGrid.add(jp_myApplicationsActionCard);
        jp_quickActionsGrid.add(jp_offCampusActionCard);
        jp_quickActionsGrid.add(jp_myOffersActionCard);

        jp_quickActionsRow.add(jp_quickActionsGrid);

        // Main Panel
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

        // Handlers and Listeners

        jb_logoutButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                jb_logoutButton.setBackground(clr_blue_hover);
            }

            public void mouseExited(MouseEvent e) {
                jb_logoutButton.setBackground(clr_logout_btn);
            }
        });

        jb_logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(
                        null,
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

        jp_searchPostingsActionCard.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                jp_searchPostingsActionCard.setBorder(new LineBorder(clr_blue, 2));
            }

            public void mouseExited(MouseEvent e) {
                jp_searchPostingsActionCard.setBorder(new LineBorder(clr_cardBorder, 1));
            }

            public void mouseClicked(MouseEvent e) {
                JobPostingsFrame frame = new JobPostingsFrame(StudentDashboardFrame.this, studentId);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1500, 700);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                setVisible(false);
            }
        });

        jp_myApplicationsActionCard.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                jp_myApplicationsActionCard.setBorder(new LineBorder(clr_blue, 2));
            }

            public void mouseExited(MouseEvent e) {
                jp_myApplicationsActionCard.setBorder(new LineBorder(clr_cardBorder, 1));
            }

            public void mouseClicked(MouseEvent e) {
                MyApplicationsFrame frame = new MyApplicationsFrame(StudentDashboardFrame.this, studentId);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1100, 700);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                setVisible(false);
            }
        });

        jp_offCampusActionCard.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                jp_offCampusActionCard.setBorder(new LineBorder(clr_blue, 2));
            }

            public void mouseExited(MouseEvent e) {
                jp_offCampusActionCard.setBorder(new LineBorder(clr_cardBorder, 1));
            }

            public void mouseClicked(MouseEvent e) {
                OffCampusFrame frame = new OffCampusFrame(StudentDashboardFrame.this, studentId);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1100, 700);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                setVisible(false);
            	
            }
        });

        jp_myOffersActionCard.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                jp_myOffersActionCard.setBorder(new LineBorder(clr_blue, 2));
            }

            public void mouseExited(MouseEvent e) {
                jp_myOffersActionCard.setBorder(new LineBorder(clr_cardBorder, 1));
            }

            public void mouseClicked(MouseEvent e) {
                OffersFrame frame = new OffersFrame(StudentDashboardFrame.this, studentId);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1100, 700);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                setVisible(false);
            		
            }
        });
        
    }
    

    
    private void fetchStudentNameAndId() {
    	String sql = "SELECT student.fullName,student.studentId, user.username from student, user where student.userId= user.userId and student.userId = ?";
      
        
        try (	
	        	Connection con = DBConnection.getConnection();
	            PreparedStatement myStmt = con.prepareStatement(sql)) { 

	            myStmt.setString(1, Integer.toString(userId));

	            ResultSet result = myStmt.executeQuery();
	            
	            if (result.next()) {
	           
	            	studentName = result.getString("fullName");
	            	studentId = result.getInt("studentId");
	            	studentUserName = result.getString("username");
	                	                
	            }
	            
	           
	        } 
        
        	catch (SQLException ex) {
	            JOptionPane.showMessageDialog(StudentDashboardFrame.this,
	                    "DB Error: " + ex.getMessage(),
	                    "Error",
	                    JOptionPane.ERROR_MESSAGE);
	        }
    }
    
    private void calculateGridStats() {
    	String sql = "SELECT COUNT(*) AS total_applications, " +
                "SUM(CASE WHEN status = 'pending' THEN 1 ELSE 0 END) AS pending_applications, " +
                "SUM(CASE WHEN status = 'accepted' THEN 1 ELSE 0 END) AS accepted_applications, " +
                "SUM(CASE WHEN status = 'rejected' THEN 1 ELSE 0 END) AS rejected_applications " +
                "FROM application WHERE studentId = ?";
    	try (	
	        	Connection con = DBConnection.getConnection();
	            PreparedStatement myStmt = con.prepareStatement(sql)) { 

	            myStmt.setString(1, Integer.toString(studentId));

	            ResultSet result = myStmt.executeQuery();
	            
	            if (result.next()) {
	           
	            	pendingCount =  result.getInt("pending_applications");
	            	acceptedCount =  result.getInt("accepted_applications");
	            	rejectedCount =  result.getInt("rejected_applications");
	            	totalApplicationsCount =  pendingCount + acceptedCount + rejectedCount;
	                	                
	            }
	            
	           
	        } catch (SQLException ex) {
	            JOptionPane.showMessageDialog(StudentDashboardFrame.this,
	                    "DB Error: " + ex.getMessage(),
	                    "Error",
	                    JOptionPane.ERROR_MESSAGE);
	        }
    }


}