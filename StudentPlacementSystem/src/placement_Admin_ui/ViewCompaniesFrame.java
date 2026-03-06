package placement_Admin_ui;

import service.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewCompaniesFrame extends JFrame {

    private DefaultTableModel model;
    private JTable table;
    private int adminUserId;
    public ViewCompaniesFrame(int adminUserId) {
    	 this.adminUserId = adminUserId;
    	 
        setTitle("All Companies");
        setSize(1400,700);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Color PRIMARY_BLUE = new Color(0, 102, 153);
        Color BACKGROUND = new Color(240, 244, 248);
        Color CARD_COLOR = Color.WHITE;

        getContentPane().setBackground(BACKGROUND);

        // HEADER 
        JPanel jp_header = new JPanel(new BorderLayout());
        jp_header.setBackground(PRIMARY_BLUE);
        jp_header.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel jp_headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        jp_headerLeft.setBackground(PRIMARY_BLUE);

        JLabel jl_headerTitle = new JLabel("All Companies");
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

        //  TOP ACTION PANEL
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

        backBtn.addActionListener(new BackListener());
        topActionPanel.add(backBtn);

        //  MAIN CARD
        JPanel mainCard = new JPanel(new BorderLayout());
        mainCard.setBackground(CARD_COLOR);
        mainCard.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30));

        // TABLE
        model = new DefaultTableModel();
        table = new JTable(model);

        model.setColumnIdentifiers(
                new String[]{"Company Name","Industry","Address","Contact Number"}
        );

        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(PRIMARY_BLUE);
        table.getTableHeader().setForeground(Color.WHITE);

        // LOAD COMPANIES DIRECTLY  DATABASE
        loadCompaniesFromDatabase();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        mainCard.add(scrollPane, BorderLayout.CENTER);

        //  CONTENT WRAPPER 
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND);

        contentPanel.add(topActionPanel, BorderLayout.NORTH);
        contentPanel.add(mainCard, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

    }

    //  FUNCTION TO LOAD COMPANIES FROM DATABASE  
    private void loadCompaniesFromDatabase() {

        String sql = "SELECT * FROM company";

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
            	model.addRow(new Object[]{
            	        rs.getString("companyName"),
            	        rs.getString("industry"),
            	        rs.getString("address"),
            	        rs.getString("contactNumber")
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
    //  BACK LISTENER 
    private class BackListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
        	new PlacementAdminDashboard(adminUserId).setVisible(true);
            dispose();
        }
    }
}