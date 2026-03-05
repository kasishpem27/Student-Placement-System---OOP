package student;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import dbconnection.DBConnection;

public class MyApplicationsFrame extends JFrame {

    // Colours
    private final Color clr_blue       = new Color(0, 102, 153);
    private final Color clr_blue_hover = new Color(0, 122, 183);
    private final Color clr_white      = Color.WHITE;
    private final Color clr_bg         = new Color(245, 248, 252);
    private final Color clr_cardBorder = new Color(220, 230, 240);
    private final Color clr_textDark   = new Color(40, 40, 40);

   
    private JLabel jl_headerTitle;
    private JLabel jl_profileIcon;
    private JLabel jl_userInfo;
    private JButton jb_back;
    
    private StudentDashboardFrame dashboard;
    private String studentUserName;
    private int studentId;
    private List<String[]> rows = new ArrayList<>();


    public MyApplicationsFrame(StudentDashboardFrame dashboard, int studentId) {
        super("My Applications");
        this.dashboard = dashboard;
        this.studentId = studentId;
        setLayout(new BorderLayout());
        getContentPane().setBackground(clr_bg);
        setResizable(false);
        
        fetchStudentName();
        // Header
        JPanel jp_header = new JPanel(new BorderLayout());
        jp_header.setBackground(clr_blue);
        jp_header.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel jp_headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        jp_headerLeft.setBackground(clr_blue);

        jl_headerTitle = new JLabel("My Applications");
        jl_headerTitle.setForeground(clr_white);
        jl_headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jp_headerLeft.add(jl_headerTitle);


        JPanel jp_headerRightInner = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        jp_headerRightInner.setBackground(clr_blue);

        ImageIcon rawProfile = new ImageIcon("src/images/profile.png");
        Image scaledProfile = rawProfile.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
        jl_profileIcon = new JLabel(new ImageIcon(scaledProfile));

        jl_userInfo = new JLabel(studentUserName + " ·  STU" + studentId);
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

        // Content
        JPanel jp_content = new JPanel(new BorderLayout());
        jp_content.setBackground(clr_bg);
        jp_content.setBorder(new EmptyBorder(16, 18, 18, 18));

        // Back Button
        JPanel jp_topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
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

        // Table
        String[] columns = { "Company Name", "Job Title", "Applied On", "Status" };
        loadApplicationsTable();
        
        
        String[][] data = rows.toArray(new String[0][]);
        DefaultTableModel tableModel = new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setGridColor(new Color(240,244,248));
        table.setBackground(clr_white);
        //table.setBackground(new Color(240,244,248));
        table.setForeground(clr_textDark);
        table.setSelectionBackground(new Color(210, 230, 250));
        table.setSelectionForeground(clr_textDark);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setFillsViewportHeight(false);
        
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(clr_blue);
        tableHeader.setForeground(clr_white);
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableHeader.setPreferredSize(new Dimension(0, 36));
        tableHeader.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(clr_cardBorder, 1));
        scrollPane.getViewport().setBackground(new Color(240,244,248));

        // Layout
        jp_content.add(jp_topBar, BorderLayout.NORTH);
        jp_content.add(scrollPane, BorderLayout.CENTER);

        add(jp_content, BorderLayout.CENTER);

     // Handler

        jb_back.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { jb_back.setBackground(new Color(230, 235, 245)); }
            @Override public void mouseExited (MouseEvent e) { jb_back.setBackground(clr_white); }
        });

        jb_back.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent ae) {
                dashboard.setVisible(true);
                dispose();
            }
        });
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
	            JOptionPane.showMessageDialog(MyApplicationsFrame.this,
	                    "DB Error: " + ex.getMessage(),
	                    "Error",
	                    JOptionPane.ERROR_MESSAGE);
	        }
    }
    
    private void loadApplicationsTable() {
    	String sql =
    		    "SELECT c.companyName, rp.title AS job_title, a.appliedOn, a.status " +
    		    "FROM application a " +
    		    "JOIN recruitmentPosting rp ON a.postingId = rp.postingId " +
    		    "JOIN company c ON rp.companyId = c.companyId " +
    		    "WHERE a.studentId = ?";
    	
        try (	
	        	Connection con = DBConnection.getConnection();
	            PreparedStatement myStmt = con.prepareStatement(sql)) { 

	            myStmt.setString(1, Integer.toString(studentId));

	            ResultSet result = myStmt.executeQuery();
	            
	            while (result.next()) {
	            	rows.add(new String[] {
	            	        result.getString("companyname"),
	            	        result.getString("job_title"),
	            	        result.getString("appliedOn"),
	            	        result.getString("status")
	            	});

	            }
	            
	           
	        } catch (SQLException ex) {
	            JOptionPane.showMessageDialog(MyApplicationsFrame.this,
	                    "DB Error: " + ex.getMessage(),
	                    "Error",
	                    JOptionPane.ERROR_MESSAGE);
	        }
    }


}