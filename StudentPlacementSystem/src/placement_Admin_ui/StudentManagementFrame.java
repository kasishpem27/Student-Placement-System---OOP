package placement_Admin_ui;

import service.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentManagementFrame extends JFrame {

    private DefaultTableModel model;
    private JTable table;
    private JTextField searchField;
    private int adminUserId;
    
    public class AppTheme {
        public static final Color CLR_WHITE = Color.WHITE;
        public static final Color CLR_TEXT_DARK = new Color(40, 40, 40);
        public static final Color CLR_CARD_BORDER = new Color(220, 220, 220);
    }

    public StudentManagementFrame(int adminUserId) {
    	 this.adminUserId = adminUserId;

        setTitle("Student Management");
        setSize(1400,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Color PRIMARY_BLUE = new Color(0, 102, 153);
        Color DARK_BLUE = new Color(13, 71, 161);
        Color BACKGROUND = new Color(240, 244, 248);
        Color CARD_COLOR = Color.WHITE;

        getContentPane().setBackground(BACKGROUND);

        //  HEADER
        JPanel jp_header = new JPanel(new BorderLayout());
        jp_header.setBackground(PRIMARY_BLUE);
        jp_header.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel jp_headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        jp_headerLeft.setBackground(PRIMARY_BLUE);

        JLabel jl_headerTitle = new JLabel("Student Management");
        jl_headerTitle.setForeground(Color.WHITE);
        jl_headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));

        jp_headerLeft.add(jl_headerTitle);

        JPanel jp_headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        jp_headerRight.setBackground(PRIMARY_BLUE);

        ImageIcon rawProfile = new ImageIcon("src/images/profile.png");
        Image scaledProfile = rawProfile.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        JLabel jl_profileIcon = new JLabel(new ImageIcon(scaledProfile));

        String adminName = getAdminName();
        JLabel jl_adminInfo = new JLabel(adminName + " · PLA" + adminUserId);
        jl_adminInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_adminInfo.setForeground(Color.WHITE);

        jp_headerRight.add(jl_profileIcon);
        jp_headerRight.add(jl_adminInfo);

        jp_header.add(jp_headerLeft, BorderLayout.WEST);
        jp_header.add(jp_headerRight, BorderLayout.EAST);

        add(jp_header, BorderLayout.NORTH);

        // CONTENT WRAPPER 
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        add(contentPanel, BorderLayout.CENTER);

        // BACK BUTTON PANEL 
        JPanel topActionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topActionPanel.setBackground(Color.WHITE);
        topActionPanel.setBorder(BorderFactory.createEmptyBorder(8, 25, 5, 25));

        JButton btn = new JButton("← Back");
        btn.setFocusPainted(false);
        btn.setBackground(AppTheme.CLR_WHITE);
        btn.setForeground(AppTheme.CLR_TEXT_DARK);
        btn.setBorder(new LineBorder(AppTheme.CLR_CARD_BORDER, 1));
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(90, 32));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(230, 235, 245));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(AppTheme.CLR_WHITE);
            }
        });

        btn.addActionListener(new BackListener());
        topActionPanel.add(btn);

        contentPanel.add(topActionPanel, BorderLayout.NORTH);

        //  MAIN CARD
        JPanel mainCard = new JPanel(new BorderLayout());
        mainCard.setBackground(CARD_COLOR);
        mainCard.setBorder(BorderFactory.createEmptyBorder(0, 25, 20, 25));

        // SEARCH PANEL 
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(CARD_COLOR);

        searchField = new JTextField(18);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JButton searchButton = new JButton("Search");
        JButton viewAllButton = new JButton("View All");

        searchButton.setFocusPainted(false);
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(PRIMARY_BLUE);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));

        searchButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                searchButton.setBackground(DARK_BLUE);
            }
            public void mouseExited(MouseEvent e) {
                searchButton.setBackground(PRIMARY_BLUE);
            }
        });

        viewAllButton.setFocusPainted(false);
        viewAllButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        viewAllButton.setForeground(Color.WHITE);
        viewAllButton.setBackground(PRIMARY_BLUE);
        viewAllButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewAllButton.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));

        viewAllButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                viewAllButton.setBackground(DARK_BLUE);
            }
            public void mouseExited(MouseEvent e) {
                viewAllButton.setBackground(PRIMARY_BLUE);
            }
        });

        topPanel.add(new JLabel("Search by FullName:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(viewAllButton);

        mainCard.add(topPanel, BorderLayout.NORTH);

        // TABLE 
        model = new DefaultTableModel();
        table = new JTable(model);

        model.setColumnIdentifiers(
                new String[]{"FullName","Course","Faculty","Section","Status"}
        );
        
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(210, 225, 245));
        table.getColumnModel().getColumn(2).setPreferredWidth(420);
        
        table.getColumnModel().getColumn(3).setMaxWidth(70);  // Section
        table.getColumnModel().getColumn(4).setMaxWidth(100);  // Status
        

        
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(PRIMARY_BLUE);
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        mainCard.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(mainCard, BorderLayout.CENTER);

        // LISTENERS 
        viewAllButton.addActionListener(new ViewAllListener());
        searchButton.addActionListener(new SearchListener());
        loadAllStudents();
    }

    // LOAD ALL STUDENTS 
    private void loadAllStudents() {

        model.setRowCount(0);

        String sql = "SELECT * FROM student";

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
            	boolean placed = rs.getBoolean("placementStatus");

            	model.addRow(new Object[]{
            	        rs.getString("fullName"),
            	        rs.getString("course"),
            	        rs.getString("faculty"),
            	        rs.getString("section"),
            	        placed ? "Placed" : "Not Placed"
            	});
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

    // VIEW ALL ACTION LISTENER
    private class ViewAllListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadAllStudents();
        }
    }

    // SEARCH ACTION LISTENER
    private class SearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String username = searchField.getText();
            if (username.trim().isEmpty()) return;

            model.setRowCount(0);

            String sql = "SELECT * FROM student WHERE fullName = ?";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString("fullName"),
                            rs.getString("course"),
                            rs.getString("faculty"),
                            rs.getString("section"),
                            rs.getBoolean("placementStatus") ? "Placed" : "Not Placed"
                    });
                } else {
                    JOptionPane.showMessageDialog(
                            StudentManagementFrame.this,
                            "Student Not Found"
                    );
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // BACK ACTION LISTENER
    private class BackListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
        	new PlacementAdminDashboard(adminUserId).setVisible(true);
            dispose();
        }
    }
}