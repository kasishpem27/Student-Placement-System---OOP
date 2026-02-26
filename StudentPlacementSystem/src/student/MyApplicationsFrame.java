package student;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

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
    private JButton jb_headerLogout;
    private JButton jb_back;
    
    private StudentDashboardFrame dashboard;

    public MyApplicationsFrame(StudentDashboardFrame dashboard) {
        super("My Applications");
        this.dashboard = dashboard;
        setLayout(new BorderLayout());
        getContentPane().setBackground(clr_bg);
        setResizable(false);

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

        jb_headerLogout = new JButton("Logout");
        jb_headerLogout.setFocusPainted(false);
        jb_headerLogout.setBackground(new Color(0, 140, 205));
        jb_headerLogout.setForeground(Color.WHITE);
        jb_headerLogout.setBorderPainted(false);
        jb_headerLogout.setOpaque(true);
        jb_headerLogout.setPreferredSize(new Dimension(110, 34));
        jb_headerLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel jp_headerRightInner = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        jp_headerRightInner.setBackground(clr_blue);

        ImageIcon rawProfile = new ImageIcon("src/images/profile.png");
        Image scaledProfile = rawProfile.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
        jl_profileIcon = new JLabel(new ImageIcon(scaledProfile));

        jl_userInfo = new JLabel("Shreeyash  ·  STU2024001");
        jl_userInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_userInfo.setForeground(clr_white);

        jp_headerRightInner.add(jl_profileIcon);
        jp_headerRightInner.add(jl_userInfo);
        jp_headerRightInner.add(jb_headerLogout);

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

        jb_back = new JButton("Back");
        jb_back.setFocusPainted(false);
        jb_back.setBackground(clr_white);
        jb_back.setForeground(clr_textDark);
        jb_back.setBorder(new LineBorder(clr_cardBorder, 1));
        jb_back.setOpaque(true);
        jb_back.setPreferredSize(new Dimension(80, 32));
        jb_back.setCursor(new Cursor(Cursor.HAND_CURSOR));

        jp_topBar.add(jb_back);

        // Table
        String[] columns = { "Company", "Job Title", "Applied On", "Status" };
        String[][] data = {
        	    { "ABC Ltd",      "Software Intern",   "2026-02-10", "Pending"      },
        	    { "TechNova",     "Web Intern",        "2026-02-11", "Shortlisted"  },
        	    { "Oceanic Co",   "IT Support Intern", "2026-02-12", "Rejected"     },
        	    { "FinCore",      "Data Intern",       "2026-02-13", "Accepted"     },
        	    { "ByteWorks",    "QA Intern",         "2026-02-14", "Pending"      },
        	    { "Skyline Tech", "UI/UX Intern",      "2026-02-15", "Under Review" },
        	};

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
        jb_headerLogout.addMouseListener(new MouseAdapter() {
        	
            @Override 
            public void mouseEntered(MouseEvent me) { 
            	jb_headerLogout.setBackground(clr_blue_hover); 
            	}
            
            @Override 
            public void mouseExited(MouseEvent me)  { 
            	jb_headerLogout.setBackground(new Color(0, 140, 205)); 
            	}
        });

        jb_headerLogout.addActionListener(new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent ae) {
                int result = JOptionPane.showConfirmDialog(
                    null, "Do you want to log out?", "Logout",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE
                );
                
                if (result == JOptionPane.OK_OPTION) {
                    JOptionPane.showMessageDialog(
                        null, "Logged out successfully.", "Logout", JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });

        jb_back.addMouseListener(new MouseAdapter() {
            @Override 
            public void mouseEntered(MouseEvent me) { 
            	jb_back.setBackground(new Color(230, 235, 245)); 
            	}
            @Override 
            public void mouseExited(MouseEvent me)  { 
            	jb_back.setBackground(clr_white); 
            	}
        });

        jb_back.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent ae) {
                dashboard.setVisible(true);
                dispose();
            }
        });
    }


}