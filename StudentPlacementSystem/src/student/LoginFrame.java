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

import org.mindrot.jbcrypt.BCrypt;

import OffCampusAdmin.AdminDashboardFrame;
import company.CompanyDashboardFrame;
import dbconnection.DBConnection;
import placement_Admin_ui.PlacementAdminDashboard;


public class LoginFrame extends JFrame {

    private JLabel jl_welcome, jl_title, jl_email, jl_password, jl_role, jl_registerLink;
    private JLabel jl_noAccount, jl_registerStudent, jl_registerCompany, jl_separator;
    private JTextField jt_email;
    private JPasswordField jp_password;
    private JComboBox jcb_role;
    private JButton jb_login;

    private final String[] str_roles = { "Student", "Company", "Placement Admin", "Off-Campus Admin" };

    // blue tones
    private final Color clr_blue = new Color(0, 102, 153);
    private final Color clr_blue_hover = new Color(0, 122, 183);
    private final Color clr_link = new Color(0, 102, 153);
    private final Color clr_link_hover = new Color(120, 180, 230); 
    private final Color clr_white = Color.WHITE;

    private final Color clr_fieldBorder = new Color(200, 200, 200);
    private int userId;

    
    public LoginFrame() {
        super("Login Page");

   
        setLayout(new BorderLayout());
        getContentPane().setBackground(clr_white);
        setResizable(false);


        // Top Panel (Portal + Login)
        JPanel jp_title = new JPanel(new GridLayout(2, 1, 0, 10));
        jp_title.setBackground(clr_white);
        jp_title.setBorder(new EmptyBorder(15, 0, 25, 0));

        // Welcome row with icon
        JPanel jp_welcomeRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jp_welcomeRow.setBackground(clr_white);

        ImageIcon rawLogoIcon = new ImageIcon("src/images/CareerConnect.png");
        Image scaledLogoIcon = rawLogoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        jl_welcome = new JLabel("CareerConnect", new ImageIcon(scaledLogoIcon), SwingConstants.CENTER);
        jl_welcome.setForeground(clr_blue);
        jl_welcome.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        jp_welcomeRow.add(jl_welcome);

        // LOGIN title row
        JPanel jp_loginTitleRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jp_loginTitleRow.setBackground(clr_white);

        jl_title = new JLabel("LOGIN");
        jl_title.setForeground(clr_blue);
        jl_title.setFont(new Font("Arial", Font.BOLD, 30));
        jp_loginTitleRow.add(jl_title);

        jp_title.add(jp_welcomeRow);
        jp_title.add(jp_loginTitleRow);

        add(jp_title, BorderLayout.NORTH);

        // Center Panel (Form)
        JPanel jp_center = new JPanel(new GridLayout(6, 1, 10, 12));
        jp_center.setBackground(clr_white);
        jp_center.setBorder(new EmptyBorder(0, 25, 20, 25));
        
        // Email address
        jl_email = new JLabel("Email Address");
        jl_email.setFont(new Font("Arial", Font.BOLD, 14));

        jt_email = new JTextField();
        jt_email.setPreferredSize(new Dimension(280, 40));
        jt_email.setBorder(new LineBorder(clr_fieldBorder, 1));
        
        // Password
        jl_password = new JLabel("Password");
        jl_password.setFont(new Font("Arial", Font.BOLD, 14));

        jp_password = new JPasswordField();
        jp_password.setPreferredSize(new Dimension(280, 40));
        jp_password.setBorder(new LineBorder(clr_fieldBorder, 1));

        // Role
        jl_role = new JLabel("Role");
        jl_role.setFont(new Font("Arial", Font.BOLD, 14));

        jcb_role = new JComboBox(str_roles);
        jcb_role.setPreferredSize(new Dimension(280, 36));
        jcb_role.setBackground(clr_white);
        jcb_role.setBorder(new LineBorder(clr_fieldBorder, 1));

        jp_center.add(jl_email);
        jp_center.add(jt_email);
        jp_center.add(jl_password);
        jp_center.add(jp_password);
        jp_center.add(jl_role);
        jp_center.add(jcb_role);

        add(jp_center, BorderLayout.CENTER);

        // South Panel
        JPanel jp_south = new JPanel(new GridLayout(2, 1, 10, 10));
        jp_south.setBackground(clr_white);
        jp_south.setBorder(new EmptyBorder(0, 25, 25, 25));

        // Login Button
        JPanel jp_loginRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jp_loginRow.setBackground(clr_white);

        jb_login = new JButton("Login");
        jb_login.setBackground(clr_blue);
        jb_login.setForeground(Color.WHITE);
        jb_login.setFocusPainted(false);
        jb_login.setPreferredSize(new Dimension(180, 45));
        jp_loginRow.add(jb_login);

        // Register Label
        JPanel jp_registerRow = new JPanel(new GridLayout(2, 1, 0, 2));
        jp_registerRow.setBackground(clr_white);

        JPanel jp_noAccountRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        jp_noAccountRow.setBackground(clr_white);

        JPanel jp_linksRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        jp_linksRow.setBackground(clr_white);

        jl_noAccount = new JLabel("No account?");
        jl_noAccount.setFont(new Font("Arial", Font.BOLD, 13));
        jl_noAccount.setForeground(Color.DARK_GRAY);

        jl_registerStudent = new JLabel("Register as Student");
        jl_registerStudent.setFont(new Font("Arial", Font.BOLD, 13));
        jl_registerStudent.setForeground(clr_link);
        jl_registerStudent.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Makes it feel like a link

        jl_separator = new JLabel("|");
        jl_separator.setFont(new Font("Arial", Font.BOLD, 13));
        jl_separator.setForeground(Color.DARK_GRAY);

        jl_registerCompany = new JLabel("Register as Company");
        jl_registerCompany.setFont(new Font("Arial", Font.BOLD, 13));
        jl_registerCompany.setForeground(clr_link);
        jl_registerCompany.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Makes it feel like a link

        jp_noAccountRow.add(jl_noAccount);

        jp_linksRow.add(jl_registerStudent);
        jp_linksRow.add(jl_separator);
        jp_linksRow.add(jl_registerCompany);

        jp_registerRow.add(jp_noAccountRow);
        jp_registerRow.add(jp_linksRow);

        jp_south.add(jp_loginRow);
        jp_south.add(jp_registerRow);

        add(jp_south, BorderLayout.SOUTH);

        // Event Handlers
        jb_login.addActionListener(new LoginHandler());

        // Hover effect for login button
        jb_login.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                jb_login.setBackground(clr_blue_hover);
            }

            public void mouseExited(MouseEvent e) {
                jb_login.setBackground(clr_blue);
            }
        });

        // Hover and click effect for register link label
        jl_registerStudent.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                jl_registerStudent.setForeground(clr_link_hover);
            }

            public void mouseExited(MouseEvent e) {
                jl_registerStudent.setForeground(clr_link);
            }

            public void mouseClicked(MouseEvent e) {
                
                StudentRegistrationFrame frame = new StudentRegistrationFrame(LoginFrame.this);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1000, 900);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                setVisible(false);
                
            }
        });

        jl_registerCompany.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                jl_registerCompany.setForeground(clr_link_hover);
            }

            public void mouseExited(MouseEvent e) {
                jl_registerCompany.setForeground(clr_link);
            }

            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null,
                        "Register as Company clicked (open Company Register frame here).",
                        "Register",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    // InnerClass of LoginHandler
    private class LoginHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String email = jt_email.getText().trim();
            char[] pass = jp_password.getPassword();
       
            String role = (String) jcb_role.getSelectedItem();

            if (email.length() == 0 || !email.contains("@")) {
                JOptionPane.showMessageDialog(null,
                        "Please enter a valid email address. (It should contain a @)",
                        "Invalid Field",
                        JOptionPane.ERROR_MESSAGE);
                jt_email.requestFocus(); // Put typing cursor in that field
                return ;
            }

            if (pass.length == 0 || pass.length <= 8) {
                JOptionPane.showMessageDialog(null,
                        "Please enter a valid password. (It should be greater than 8 characters)",
                        "Invalid Field",
                        JOptionPane.ERROR_MESSAGE);
                jp_password.requestFocus(); // Put typing cursor in that field
                return;
            }
            
            boolean authenticated = authenticateUser(email, new String(pass),role);
            
            if (role.equals("student")){
	            StudentDashboardFrame frame = new StudentDashboardFrame(LoginFrame.this, userId);
	            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	            frame.setSize(1100,700);
	            frame.setLocationRelativeTo(null);
	            frame.setVisible(true);
	            setVisible(false);
	            
		    } else if (role.equals("Off-Campus Admin")) {
			            AdminDashboardFrame frame = new AdminDashboardFrame(LoginFrame.this, userId);
			            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			            frame.setSize(1100,650);
			            frame.setLocationRelativeTo(null);
			            frame.setVisible(true);
			            setVisible(false);
			            
		   
		    
		    } else if (role.equals("Company")) {
	            CompanyDashboardFrame frame = new CompanyDashboardFrame(LoginFrame.this, userId);
	            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	            frame.setSize(1100,700);
	            frame.setLocationRelativeTo(null);
	            frame.setVisible(true);
	            setVisible(false);
		    }
            
		        
		        jt_email.setText("");
		        jp_password.setText("");
		        }
            
            
        }
        
        

    
    private boolean authenticateUser(String email, String password, String role) {
        String sql = "SELECT * FROM user WHERE email=? AND role=?; ";
        
        try (	
	        	Connection con = DBConnection.getConnection();
	            PreparedStatement myStmt = con.prepareStatement(sql)) {

	            myStmt.setString(1, email);
	            myStmt.setString(2,role);

	            ResultSet result = myStmt.executeQuery();
	            
	            if (!result.next()) {
	            	
	                JOptionPane.showMessageDialog(null,
	                        "Incorrect Credentials. Access Denied",
	                        "Login Failed",
	                        JOptionPane.ERROR_MESSAGE);
	                jt_email.setText("");
	                jp_password.setText("");
	                return false;
	            }
	            
	            userId = result.getInt("userId");
	            String storedHash = result.getString("password");
	            String accountStatus = result.getString("accountStatus");
	            
	            boolean passwordMatch = BCrypt.checkpw(password, storedHash);
	            
	            if (accountStatus.equals("deactivated")) {
	                JOptionPane.showMessageDialog(null,
	                        "Access Denied. Your account is not active",
	                        "Account Deactivated",
	                        JOptionPane.ERROR_MESSAGE);
	                return false;
	            }
	            
	               
	            if (!passwordMatch ) {
	            	
	                JOptionPane.showMessageDialog(null,
	                        "Incorrect Credentials. Access Denied",
	                        "Login Failed",
	                        JOptionPane.ERROR_MESSAGE);
	                jt_email.setText("");
	                jp_password.setText("");
	                return false;
	        
	            }
	            
            	JOptionPane.showMessageDialog(null,
                        "Login Successful. Press OK to redirect to Dashboard",
                        "Login Sucessful",
                        JOptionPane.INFORMATION_MESSAGE);
            	                	           	          
            	

	        } catch (SQLException ex) {
	            JOptionPane.showMessageDialog(LoginFrame.this,
	                    "DB Error: " + ex.getMessage(),
	                    "Error",
	                    JOptionPane.ERROR_MESSAGE);
	        }
            return true;
    }
    
}
