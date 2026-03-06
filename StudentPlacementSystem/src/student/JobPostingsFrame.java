package student;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

import dbconnection.DBConnection;

public class JobPostingsFrame extends JFrame {


    private JButton jb_back;
    private JButton jb_logout;
    private JButton jb_view;
    private JButton jb_apply;

    private DefaultTableModel model;
    private JTable table;

    private JCheckBox jcb_all;
    private JTextField jt_search;

    private final Color PRIMARY_BLUE = new Color(0, 102, 153);
    private final Color CARD_COLOR   = Color.WHITE;
    
    private StudentDashboardFrame dashboard;
    private int studentId;
    private String studentUserName;

    public JobPostingsFrame(StudentDashboardFrame dashboard, int studentId) {
        this.dashboard = dashboard;
        this.studentId = studentId;
        
        
        fetchStudentName();
        setTitle("Job Postings");
        setSize(1500, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        getContentPane().setBackground(CARD_COLOR);

        // HEADER
        JPanel jp_header = new JPanel(new BorderLayout());
        jp_header.setBackground(PRIMARY_BLUE);
        jp_header.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel jp_headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        jp_headerLeft.setBackground(PRIMARY_BLUE);
        ImageIcon icon_logo = new ImageIcon(new ImageIcon("src/images/CareerConnect.png").getImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH));
        JLabel jl_logo = new JLabel(icon_logo);
        jp_headerLeft.add(jl_logo);
        JLabel jl_headerTitle = new JLabel("Job Postings");
        jl_headerTitle.setForeground(Color.WHITE);
        jl_headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        jp_headerLeft.add(jl_headerTitle);

        JPanel jp_headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        jp_headerRight.setBackground(PRIMARY_BLUE);
        JLabel jl_userInfo = new JLabel(studentUserName + "  ·  " + studentId);
        jl_userInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_userInfo.setForeground(Color.WHITE);

        jb_logout = new JButton("Logout");
        jb_logout.setFocusPainted(false);
        jb_logout.setBackground(new Color(0, 140, 205));
        jb_logout.setForeground(Color.WHITE);
        jb_logout.setBorderPainted(false);
        jb_logout.setOpaque(true);
        jb_logout.setPreferredSize(new Dimension(110, 34));
        jb_logout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jb_logout.setFont(new Font("Segoe UI", Font.BOLD, 13));

        ImageIcon icon_avatar = new ImageIcon(new ImageIcon("src/images/profile.png").getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
        JLabel jl_avatar = new JLabel(icon_avatar);
        jp_headerRight.add(jl_avatar);
        jp_headerRight.add(jl_userInfo);
        jp_headerRight.add(jb_logout);
        jp_header.add(jp_headerLeft, BorderLayout.WEST);
        jp_header.add(jp_headerRight, BorderLayout.EAST);
        add(jp_header, BorderLayout.NORTH);

        // MAIN CONTENT
        JPanel jp_main = new JPanel(new BorderLayout());
        jp_main.setBackground(CARD_COLOR);
        jp_main.setBorder(new EmptyBorder(8, 16, 0, 16));

        // TOP BAR (back button)
        JPanel jp_topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_topBar.setBackground(CARD_COLOR);
        jp_topBar.setBorder(new EmptyBorder(0, 0, 8, 0));

        jb_back = new JButton("← Back");
        jb_back.setFocusPainted(false);
        jb_back.setBackground(CARD_COLOR);
        jb_back.setForeground(Color.BLACK);
        jb_back.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        jb_back.setPreferredSize(new Dimension(90, 32));
        jb_back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jb_back.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jp_topBar.add(jb_back);

        // SEARCH PANEL
        JPanel jp_searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        jp_searchPanel.setBackground(CARD_COLOR);

        jt_search = new JTextField(22);
        jt_search.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JButton jb_search = new JButton("Search");
        jb_search.setFocusPainted(false);
        jb_search.setBorderPainted(false);
        jb_search.setFont(new Font("Segoe UI", Font.BOLD, 13));
        jb_search.setForeground(Color.WHITE);
        jb_search.setBackground(PRIMARY_BLUE);
        jb_search.setOpaque(true);
        jb_search.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton jb_clear = new JButton("Clear");
        jb_clear.setFocusPainted(false);
        jb_clear.setBackground(CARD_COLOR);
        jb_clear.setForeground(Color.BLACK);
        jb_clear.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        jb_clear.setPreferredSize(new Dimension(90, 32));
        jb_clear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jb_clear.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        jcb_all = new JCheckBox("Include Closed", false);
        jcb_all.setBackground(CARD_COLOR);
        jcb_all.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        jp_searchPanel.add(new JLabel("Search by Company or Title:"));
        jp_searchPanel.add(jt_search);
        jp_searchPanel.add(jb_search);
        jp_searchPanel.add(jb_clear);
        jp_searchPanel.add(Box.createHorizontalStrut(10));
        jp_searchPanel.add(jcb_all);

        // TABLE
        model = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        model.setColumnIdentifiers(new String[]{
            "Company Name", "Posting Title", "Salary / Rs", "Location", "Deadline", "Status", "_postingId"
        });

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(210, 225, 245));
        table.setSelectionForeground(new Color(40, 40, 40));
        table.getTableHeader().setUI(new javax.swing.plaf.basic.BasicTableHeaderUI());
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(PRIMARY_BLUE);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumnModel().getColumn(2).setMaxWidth(700);
        table.getColumnModel().getColumn(5).setMaxWidth(300);
        table.getColumnModel().getColumn(6).setMaxWidth(300);

        TableColumn idCol = table.getColumnModel().getColumn(6);
        idCol.setMinWidth(0); idCol.setMaxWidth(0); idCol.setWidth(0);

        loadRows("");

        JScrollPane jp_scroll = new JScrollPane(table);
        jp_scroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel jp_topSection = new JPanel(new BorderLayout());
        jp_topSection.setBackground(CARD_COLOR);
        jp_topSection.add(jp_topBar,      BorderLayout.NORTH);
        jp_topSection.add(jp_searchPanel, BorderLayout.CENTER);

        jp_main.add(jp_topSection, BorderLayout.NORTH);
        jp_main.add(jp_scroll,     BorderLayout.CENTER);
        add(jp_main, BorderLayout.CENTER);

        // BOTTOM BUTTON PANEL
        JPanel jp_bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        jp_bottom.setBackground(CARD_COLOR);
        jp_bottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        jb_view = new JButton("View Details");
        jb_view.setFocusPainted(false);
        jb_view.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jb_view.setForeground(Color.WHITE);
        jb_view.setBackground(PRIMARY_BLUE);
        jb_view.setBorderPainted(false);
        jb_view.setOpaque(true);
        jb_view.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jb_view.setPreferredSize(new Dimension(160, 36));

        jb_apply = new JButton("Apply");
        jb_apply.setFocusPainted(false);
        jb_apply.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jb_apply.setForeground(Color.WHITE);
        jb_apply.setBackground(PRIMARY_BLUE);
        jb_apply.setBorderPainted(false);
        jb_apply.setOpaque(true);
        jb_apply.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jb_apply.setPreferredSize(new Dimension(160, 36));

        jp_bottom.add(jb_view);
        jp_bottom.add(jb_apply);
        add(jp_bottom, BorderLayout.SOUTH);

        // LISTENERS
        jb_back.addActionListener(new BackListener());
        jb_logout.addActionListener(new LogoutListener());
        jb_search.addActionListener(new SearchListener());
        jb_clear.addActionListener(new ClearListener());
        jb_view.addActionListener(new ViewListener());
        jb_apply.addActionListener(new ApplyListener());

        jcb_all.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadRows(jt_search.getText().trim());
            }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                int row = table.getSelectedRow();
                if (row < 0) { jb_apply.setEnabled(true); return; }
                int postingId = (int) model.getValueAt(row, 6);
                String status = (String) model.getValueAt(row, 5);
                if (hasApplied(postingId) || "Closed".equals(status)) {
                    jb_apply.setEnabled(false);
                } else {
                    jb_apply.setEnabled(true);
                }
            }
        });
    }

    // BACK LISTENER
    private class BackListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	dashboard.refreshGridStats();
        	dashboard.setVisible(true);
            dispose();
            
        }
    }

    // LOGOUT LISTENER
    private class LogoutListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int r = JOptionPane.showConfirmDialog(JobPostingsFrame.this,
                    "Do you want to log out?", "Logout", JOptionPane.OK_CANCEL_OPTION);
            if (r == JOptionPane.OK_OPTION) System.exit(0);
        }
    }

    // SEARCH LISTENER
    private class SearchListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            loadRows(jt_search.getText().trim());
        }
    }

    // CLEAR LISTENER
    private class ClearListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            jt_search.setText("");
            loadRows("");
        }
    }

    // VIEW LISTENER
    private class ViewListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(JobPostingsFrame.this, "Please select a posting to view.", "No Selection", JOptionPane.INFORMATION_MESSAGE); return; }
            int postingId  = (int)    model.getValueAt(row, 6);
            String company = (String) model.getValueAt(row, 0);
            String role    = (String) model.getValueAt(row, 1);
            String status  = (String) model.getValueAt(row, 5);
            new JobDetailFrame(JobPostingsFrame.this, dashboard, studentId, postingId, company, role, status).setVisible(true);
        }
    }

    // APPLY LISTENER
    private class ApplyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(JobPostingsFrame.this, "Please select a posting to apply.", "No Selection", JOptionPane.INFORMATION_MESSAGE); return; }
            int postingId  = (int)    model.getValueAt(row, 6);
            String company = (String) model.getValueAt(row, 0);
            String role    = (String) model.getValueAt(row, 1);
            setVisible(false);
            new ApplyJobFrame(dashboard, JobPostingsFrame.this, studentId, postingId, company, role).setVisible(true);
        }
    }

    // LOAD ROWS
    private void loadRows(String keyword) {
        model.setRowCount(0);
        boolean includeClosed = jcb_all.isSelected();
        String sql =
            "SELECT rp.postingId, c.companyName, rp.title, " +
            "jo.salary, po.stipend, rp.location, rp.applicationDeadline, rp.isActive " +
            "FROM recruitmentPosting rp " +
            "JOIN company c ON rp.companyId = c.companyId " +
            "LEFT JOIN jobOpportunity jo ON rp.postingId = jo.postingId " +
            "LEFT JOIN placementOpportunity po ON rp.postingId = po.postingId " +
            (!includeClosed ? "WHERE rp.isActive = TRUE " : "") +
            "ORDER BY rp.isActive DESC, rp.applicationDeadline ASC";
        try (Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String company = rs.getString("companyName");
                String role    = rs.getString("title");
                if (!keyword.isEmpty()) {
                    String kw = keyword.toLowerCase();
                    if (!company.toLowerCase().contains(kw) && !role.toLowerCase().contains(kw)) continue;
                }
                double sal = rs.getDouble("salary");
                double sti = rs.getDouble("stipend");
                String money;
                if      (sal > 0) money = String.valueOf(sal);
                else if (sti > 0) money = String.valueOf(sti);
                else              money = "—";
                String location  = rs.getString("location")            != null ? rs.getString("location")            : "—";
                String deadline  = rs.getString("applicationDeadline") != null ? rs.getString("applicationDeadline") : "—";
                String status    = rs.getBoolean("isActive") ? "Open" : "Closed";
                int    postingId = rs.getInt("postingId");
                model.addRow(new Object[]{ company, role, money, location, deadline, status, postingId });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // HAS APPLIED
    private boolean hasApplied(int postingId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT COUNT(*) FROM application WHERE studentId = ? AND postingId = ?")) {
            ps.setInt(1, studentId);
            ps.setInt(2, postingId);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException ex) { return false; }
    }

    private void fetchStudentName() {
    	String sql = "SELECT username from student,user where user.userId = student.userId AND studentId = ?";
      
        
        try (	
	        	Connection con = DBConnection.getConnection();
	            PreparedStatement myStmt = con.prepareStatement(sql)) { 

	           	myStmt.setInt(1, studentId);
	            ResultSet result = myStmt.executeQuery();
	            
	            if (result.next()) {
	           
	            	studentUserName = result.getString("username");
	                	                
	            }
	            
	           
	        } catch (SQLException ex) {
	            JOptionPane.showMessageDialog(JobPostingsFrame.this,
	                    "DB Error: " + ex.getMessage(),
	                    "Error",
	                    JOptionPane.ERROR_MESSAGE);
	        }
    }
}