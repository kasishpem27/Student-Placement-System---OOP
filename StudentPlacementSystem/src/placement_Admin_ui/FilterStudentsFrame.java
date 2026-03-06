package placement_Admin_ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import service.DBConnection;
import java.sql.*;


public class FilterStudentsFrame extends JFrame {

	private int adminUserId;
    private JComboBox<String> courseBox;
    private JComboBox<String> facultyBox;
    private JComboBox<String> statusBox;
    private JComboBox<String> sectionBox;

    private DefaultTableModel model;

    public FilterStudentsFrame(int adminUserId) {
    	
    	 this.adminUserId = adminUserId;

        setTitle("Filter Students");
        setSize(1400,700);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Color PRIMARY_BLUE = new Color(0, 102, 153);
        Color DARK_BLUE = new Color(13, 71, 161);
        Color BACKGROUND = new Color(240, 244, 248);
        Color CARD_COLOR = Color.WHITE;

       
     // HEADER 
        JPanel jp_header = new JPanel(new BorderLayout());
        jp_header.setBackground(PRIMARY_BLUE);
        jp_header.setBorder(new EmptyBorder(12, 16, 12, 16));  

        // LEFT SIDE (Title only)
        JPanel jp_headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        jp_headerLeft.setBackground(PRIMARY_BLUE);

        JLabel jl_headerTitle = new JLabel("Filter Students");
        jl_headerTitle.setForeground(Color.WHITE);
        jl_headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));

        jp_headerLeft.add(jl_headerTitle);

        // RIGHT SIDE (Profile icon + Admin info) 
        JPanel jp_headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        jp_headerRight.setBackground(PRIMARY_BLUE);

        // Load profile icon 
        ImageIcon rawProfile = new ImageIcon("src/images/profile.png");
        Image scaledProfile = rawProfile.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        JLabel jl_profileIcon = new JLabel(new ImageIcon(scaledProfile));
        
        String adminName = getAdminName();
        JLabel jl_adminInfo = new JLabel(adminName + " · PLA" + adminUserId);
        jl_adminInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_adminInfo.setForeground(Color.WHITE);

        jp_headerRight.add(jl_profileIcon);
        jp_headerRight.add(jl_adminInfo);

        // Add left & right sections
        jp_header.add(jp_headerLeft, BorderLayout.WEST);
        jp_header.add(jp_headerRight, BorderLayout.EAST);

        add(jp_header, BorderLayout.NORTH);
        
     // TOP ACTION PANEL 
        JPanel topActionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topActionPanel.setBackground(Color.WHITE);
        topActionPanel.setBorder(BorderFactory.createEmptyBorder(8, 30, 5, 30));

        JButton backBtn = new JButton("← Back");
        backBtn.setFocusPainted(false);
        backBtn.setBackground(Color.WHITE);
        backBtn.setForeground(Color.BLACK);
        backBtn.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));
        backBtn.setPreferredSize(new Dimension(90, 32));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        backBtn.addActionListener(new BackButtonListener());

        topActionPanel.add(backBtn);
      
        
        //  MAIN CARD 
        JPanel mainCard = new JPanel(new BorderLayout());
        mainCard.setBackground(CARD_COLOR);
        mainCard.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30));
        
   
        
        // FILTER PANEL 
        JPanel filterPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        filterPanel.setBackground(CARD_COLOR);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        courseBox = new JComboBox<>();
        loadCourses();
        facultyBox = new JComboBox<>(new String[]{
        		"Faculty of Agriculture",
        		"Faculty of Engineering",
        		"Faculty of Information,Communication and Digital Technologies",
        		"Faculty of Law & Management",
        		"Faculty of Ocean Studies",
        		"Faculty of Science",
        		"Faculty of Social Studies & Humanities"
        		});
        statusBox = new JComboBox<>(new String[]{"Placed", "Not Placed"});
        sectionBox = new JComboBox<>(new String[]{"A", "B"});

        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        
        courseBox.setFont(fieldFont);
        facultyBox.setFont(fieldFont);
        statusBox.setFont(fieldFont);
        sectionBox.setFont(fieldFont);

        filterPanel.add(new JLabel("Course:"));
        filterPanel.add(courseBox);
        filterPanel.add(new JLabel("Faculty:"));
        filterPanel.add(facultyBox);
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(statusBox);
        filterPanel.add(new JLabel("Section:"));
        filterPanel.add(sectionBox);

        mainCard.add(filterPanel, BorderLayout.NORTH);

        //  TABLE 
        model = new DefaultTableModel();
        JTable table = new JTable(model);

        model.setColumnIdentifiers(
                new String[]{"FullName","Course","Faculty","Section","Status"}
        );
     // Adjust column widths
        table.getColumnModel().getColumn(2).setPreferredWidth(400);
        
        table.getColumnModel().getColumn(3).setMaxWidth(90);  // Section
        table.getColumnModel().getColumn(4).setMaxWidth(100);  // Status
        
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(PRIMARY_BLUE);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setBorder(null);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setViewportBorder(null);

        mainCard.add(scrollPane, BorderLayout.CENTER);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND);

        contentPanel.add(topActionPanel, BorderLayout.NORTH);
        contentPanel.add(mainCard, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        //BUTTON PANEL
        JButton filterButton = new JButton("Apply Filter");
        

        filterButton.setFocusPainted(false);
        

        filterButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
       

        filterButton.setForeground(Color.WHITE);
       

        filterButton.setBackground(PRIMARY_BLUE);
     

        filterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
      

        filterButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
      

        filterButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                filterButton.setBackground(DARK_BLUE);
            }
            public void mouseExited(MouseEvent e) {
                filterButton.setBackground(PRIMARY_BLUE);
            }
        });

      

        filterButton.addActionListener(new FilterButtonListener());
       
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(BACKGROUND);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        bottomPanel.add(filterButton);
    
  
        add(bottomPanel, BorderLayout.SOUTH);
    }
 // LOAD COURSES FROM DATABASE
    private void loadCourses() {

        String sql = "SELECT DISTINCT course FROM student";

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                courseBox.addItem(rs.getString("course"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
    // FILTER BUTTON LISTENER
    private class FilterButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        	model.setRowCount(0);
        	

        	String sql = "SELECT * FROM student WHERE course=? AND faculty=? AND placementStatus=? AND section=?";

        	try (Connection con = DBConnection.getConnection();
        	     PreparedStatement ps = con.prepareStatement(sql)) {

        	   
        		ps.setString(1, (String) courseBox.getSelectedItem());
        		ps.setString(2, (String) facultyBox.getSelectedItem());

        		boolean placed = statusBox.getSelectedItem().equals("Placed");
        		ps.setBoolean(3, placed);

        		ps.setString(4, (String) sectionBox.getSelectedItem());
        	    ResultSet rs = ps.executeQuery();

        	    while (rs.next()) {
        	        model.addRow(new Object[]{
        	                rs.getString("fullName"),
        	                rs.getString("course"),
        	                rs.getString("faculty"),
        	                rs.getString("section"),
        	                rs.getBoolean("placementStatus") ? "Placed" : "Not Placed"
        	        });
        	    }

        	} catch (Exception ex) {
        	    ex.printStackTrace();
        	}
        }
    }

    //  BACK BUTTON LISTENER
    private class BackButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
        	new PlacementAdminDashboard(adminUserId).setVisible(true);
            dispose();
        }
    }
}