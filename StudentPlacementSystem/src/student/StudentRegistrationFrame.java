package student;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import org.mindrot.jbcrypt.BCrypt;

import dbconnection.DBConnection;

public class StudentRegistrationFrame extends JFrame {

    // UI COMPONENTS 
    private JLabel jl_leftLogo, jl_leftCompanyName, jl_leftCopyright;
    private JLabel jl_title;

    private JLabel jl_fullName, jl_dob,jl_section, jl_address, jl_contact, jl_gender, jl_faculty, jl_course,jl_username, jl_email, jl_password, jl_confirmPassword;

    private JTextField jt_fullName, jt_dob, jt_address, jt_contact, jt_course, jt_email, jt_username;
    private JPasswordField jp_password, jp_confirmPassword;

    private JRadioButton jrb_male, jrb_female, jrb_ratherNot;
    private ButtonGroup bg_gender;

    private JComboBox jcb_faculty, jcb_section;

    private JButton jb_signUp;
    private JLabel jl_loginLink;

    // CONSTANTS 
    private final String[] str_faculties = {
            "Faculty of Agriculture",
            "Faculty of Engineering",
            "Faculty of Information,Communication and Digital Technologies",
            "Faculty of Law & Management",
            "Faculty of Ocean Studies",
            "Faculty of Science",
            "Faculty of Social Studies & Humanities"
    };
    
    private final String[] str_sections = { "A", "B" };

    //  COLORS
    private final Color clr_blue = new Color(0, 102, 153);
    private final Color clr_blue_hover = new Color(0, 122, 183);
    private final Color clr_link = new Color(0, 102, 153);
    private final Color clr_link_hover = new Color(120, 180, 230);
    private final Color clr_white = Color.WHITE;
    private final Color clr_leftPanel = new Color(0, 102, 153);
    private final Color clr_fieldBorder = new Color(200, 200, 200);

    //  PLACEHOLDERS 
    private final String ph_fullName = "Enter your full name";
    private final String ph_dob = "YYYY/MM/DD";
    private final String ph_address = "Enter your address";
    private final String ph_contact = "Enter your contact number";
    private final String ph_course = "Enter your course";
    private final String ph_email = "Enter your email address";
    private final String ph_username = "Enter a username";
    
    // Navigation
    private LoginFrame login;

    public StudentRegistrationFrame(LoginFrame login) {
        super("Register Page");
        this.login = login;

        setLayout(new BorderLayout());
        getContentPane().setBackground(clr_white);
        setResizable(false);

        // MAIN CONTAINER 
        JPanel jp_main = new JPanel(new GridLayout(1, 2, 0, 0));
        jp_main.setBackground(clr_white);

        // LEFT PANEL
        JPanel jp_left = new JPanel(new BorderLayout());
        jp_left.setBackground(clr_leftPanel);
        jp_left.setBorder(new EmptyBorder(25, 20, 25, 20));

        JPanel jp_leftCenter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jp_leftCenter.setBackground(clr_leftPanel);
        

        ImageIcon leftIcon = new ImageIcon("src/images/CareerConnect.png");
        Image scaledLeftIcon = leftIcon.getImage().getScaledInstance(96, 96, Image.SCALE_SMOOTH);
        jl_leftLogo = new JLabel(new ImageIcon(scaledLeftIcon), SwingConstants.CENTER);
        jl_leftLogo.setHorizontalAlignment(SwingConstants.CENTER);

        jl_leftCompanyName = new JLabel("CareerConnect");
        jl_leftCompanyName.setHorizontalAlignment(SwingConstants.CENTER);
        jl_leftCompanyName.setForeground(Color.WHITE);
        jl_leftCompanyName.setFont(new Font("Arial", Font.BOLD, 25));

        jp_leftCenter.add(jl_leftLogo);
        jp_leftCenter.add(jl_leftCompanyName);

        JPanel jp_leftMiddle = new JPanel(new GridLayout(3, 1));
        jp_leftMiddle.setBackground(clr_leftPanel);

        JPanel jp_spacerTop = new JPanel();
        jp_spacerTop.setBackground(clr_leftPanel);

        JPanel jp_spacerBottom = new JPanel();
        jp_spacerBottom.setBackground(clr_leftPanel);

        jp_leftMiddle.add(jp_spacerTop);
        jp_leftMiddle.add(jp_leftCenter);
        jp_leftMiddle.add(jp_spacerBottom);

        jl_leftCopyright = new JLabel("Copyright © CareerConnect All rights reserved");
        jl_leftCopyright.setHorizontalAlignment(SwingConstants.CENTER);
        jl_leftCopyright.setForeground(Color.WHITE);
        jl_leftCopyright.setFont(new Font("Arial", Font.PLAIN, 11));

        jp_left.add(jp_leftMiddle, BorderLayout.CENTER);
        jp_left.add(jl_leftCopyright, BorderLayout.SOUTH);

        // RIGHT PANEL 
        JPanel jp_right = new JPanel(new BorderLayout());
        jp_right.setBackground(clr_white);
        jp_right.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Title row
        JPanel jp_title = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jp_title.setBackground(clr_white);

        jl_title = new JLabel("STUDENT REGISTRATION FORM");
        jl_title.setForeground(clr_blue);
        jl_title.setFont(new Font("Arial", Font.BOLD, 28));
        jp_title.add(jl_title);

        // Form holder 
        JPanel jp_formHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jp_formHolder.setBackground(clr_white);

        // Actual form 
        JPanel jp_form = new JPanel(new GridLayout(26, 1, 10, 10));
        jp_form.setBackground(clr_white);

        //  FORM FIELDS

        // Full Name
        jl_fullName = new JLabel("<html>Full Name <font color='red'>*</font></html>");
        jl_fullName.setFont(new Font("Arial", Font.BOLD, 13));

        jt_fullName = new JTextField(ph_fullName);
        jt_fullName.setForeground(Color.GRAY);
        jt_fullName.setPreferredSize(new Dimension(300, 36));
        jt_fullName.setBorder(new LineBorder(clr_fieldBorder, 1));

        // Date of Birth
        jl_dob = new JLabel("<html>Date Of Birth <font color='red'>*</font></html>");
        jl_dob.setFont(new Font("Arial", Font.BOLD, 13));

        jt_dob = new JTextField(ph_dob);
        jt_dob.setForeground(Color.GRAY);
        jt_dob.setPreferredSize(new Dimension(300, 36));
        jt_dob.setBorder(new LineBorder(clr_fieldBorder, 1));

        // Address
        jl_address = new JLabel("Address");
        jl_address.setFont(new Font("Arial", Font.BOLD, 13));

        jt_address = new JTextField(ph_address);
        jt_address.setForeground(Color.GRAY);
        jt_address.setPreferredSize(new Dimension(300, 36));
        jt_address.setBorder(new LineBorder(clr_fieldBorder, 1));

        // Contact
        jl_contact = new JLabel("<html>Contact Number <font color='red'>*</font></html>");
        jl_contact.setFont(new Font("Arial", Font.BOLD, 13));

        jt_contact = new JTextField(ph_contact);
        jt_contact.setForeground(Color.GRAY);
        jt_contact.setPreferredSize(new Dimension(300, 36));
        jt_contact.setBorder(new LineBorder(clr_fieldBorder, 1));

        // Gender
        jl_gender = new JLabel("<html>Gender <font color='red'>*</font></html>");
        jl_gender.setFont(new Font("Arial", Font.BOLD, 13));

        JPanel jp_genderRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jp_genderRow.setBackground(clr_white);

        jrb_male = new JRadioButton("Male");
        jrb_female = new JRadioButton("Female");
        jrb_ratherNot = new JRadioButton("Rather not say");

        jrb_male.setBackground(clr_white);
        jrb_female.setBackground(clr_white);
        jrb_ratherNot.setBackground(clr_white);

        bg_gender = new ButtonGroup();
        bg_gender.add(jrb_male);
        bg_gender.add(jrb_female);
        bg_gender.add(jrb_ratherNot);

        jp_genderRow.add(jrb_male);
        jp_genderRow.add(jrb_female);
        jp_genderRow.add(jrb_ratherNot);

        // Faculty
        jl_faculty = new JLabel("<html>Faculty <font color='red'>*</font></html>");
        jl_faculty.setFont(new Font("Arial", Font.BOLD, 13));

        jcb_faculty = new JComboBox(str_faculties);
        jcb_faculty.setPreferredSize(new Dimension(375, 36));
        jcb_faculty.setBackground(clr_white);
        jcb_faculty.setBorder(new LineBorder(clr_fieldBorder, 1));

        // Course
        jl_course = new JLabel("<html>Course <font color='red'>*</font></html>");
        jl_course.setFont(new Font("Arial", Font.BOLD, 13));

        jt_course = new JTextField(ph_course);
        jt_course.setForeground(Color.GRAY);
        jt_course.setPreferredSize(new Dimension(300, 36));
        jt_course.setBorder(new LineBorder(clr_fieldBorder, 1));
        
        
     // Section
        jl_section = new JLabel("<html>Section <font color='red'>*</font></html>");
        jl_section.setFont(new Font("Arial", Font.BOLD, 13));

        jcb_section = new JComboBox(str_sections);
        jcb_section.setPreferredSize(new Dimension(375, 36));
        jcb_section.setBackground(clr_white);
        jcb_section.setBorder(new LineBorder(clr_fieldBorder, 1));
        
        // Email
        jl_email = new JLabel("<html>Email Address <font color='red'>*</font></html>");
        jl_email.setFont(new Font("Arial", Font.BOLD, 13));

        jt_email = new JTextField(ph_email);
        jt_email.setForeground(Color.GRAY);
        jt_email.setPreferredSize(new Dimension(300, 36));
        jt_email.setBorder(new LineBorder(clr_fieldBorder, 1));
        
        jl_username = new JLabel("<html>Username <font color='red'>*</font></html>");
        jl_username.setFont(new Font("Arial", Font.BOLD, 13));

        jt_username = new JTextField(ph_username);
        jt_username.setForeground(Color.GRAY);
        jt_username.setPreferredSize(new Dimension(300, 36));
        jt_username.setBorder(new LineBorder(clr_fieldBorder, 1));

        // Password
        jl_password = new JLabel("<html>Enter Password <font color='red'>*</font></html>");
        jl_password.setFont(new Font("Arial", Font.BOLD, 13));

        jp_password = new JPasswordField();
        jp_password.setPreferredSize(new Dimension(300, 36));
        jp_password.setBorder(new LineBorder(clr_fieldBorder, 1));

        // Confirm Password
        jl_confirmPassword = new JLabel("<html>Confirm Password <font color='red'>*</font></html>");
        jl_confirmPassword.setFont(new Font("Arial", Font.BOLD, 13));

        jp_confirmPassword = new JPasswordField();
        jp_confirmPassword.setPreferredSize(new Dimension(300, 36));
        jp_confirmPassword.setBorder(new LineBorder(clr_fieldBorder, 1));

        // Add fields to form 
        jp_form.add(jl_fullName);
        jp_form.add(jt_fullName);

        jp_form.add(jl_dob);
        jp_form.add(jt_dob);

        jp_form.add(jl_address);
        jp_form.add(jt_address);

        jp_form.add(jl_contact);
        jp_form.add(jt_contact);

        jp_form.add(jl_gender);
        jp_form.add(jp_genderRow);

        jp_form.add(jl_faculty);
        jp_form.add(jcb_faculty);

        jp_form.add(jl_course);
        jp_form.add(jt_course);
        
        jp_form.add(jl_section);
        jp_form.add(jcb_section);

        jp_form.add(jl_email);
        jp_form.add(jt_email);
        
        jp_form.add(jl_username);
        jp_form.add(jt_username);

        jp_form.add(jl_password);
        jp_form.add(jp_password);

        jp_form.add(jl_confirmPassword);
        jp_form.add(jp_confirmPassword);

        jp_formHolder.add(jp_form);

        // Scroll pane for long form
        JScrollPane sp_form = new JScrollPane(jp_formHolder);
        sp_form.setBorder(null);
        sp_form.getViewport().setBackground(clr_white);
        sp_form.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Bottom area: Register button + Login link
        JPanel jp_bottom = new JPanel(new GridLayout(2, 1, 10, 10));
        jp_bottom.setBackground(clr_white);

        JPanel jp_buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jp_buttonRow.setBackground(clr_white);

        jb_signUp = new JButton("Register");
        jb_signUp.setBackground(clr_blue);
        jb_signUp.setForeground(Color.WHITE);
        jb_signUp.setFocusPainted(false);
        jb_signUp.setPreferredSize(new Dimension(140, 40));

        jp_buttonRow.add(jb_signUp);

        JPanel jp_loginRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jp_loginRow.setBackground(clr_white);

        jl_loginLink = new JLabel("Already have an account? Login");
        jl_loginLink.setForeground(clr_link);
        jl_loginLink.setFont(new Font("Arial", Font.BOLD, 13));
        jl_loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        jp_loginRow.add(jl_loginLink);

        jp_bottom.add(jp_buttonRow);
        jp_bottom.add(jp_loginRow);

        // Assemble right panel
        jp_right.add(jp_title, BorderLayout.NORTH);
        jp_right.add(sp_form, BorderLayout.CENTER);
        jp_right.add(jp_bottom, BorderLayout.SOUTH);

        // Assemble main screen
        jp_main.add(jp_left);
        jp_main.add(jp_right);

        add(jp_main, BorderLayout.CENTER);

        // ================= HANDLERS ===================

        // Hover effect for Register button
        jb_signUp.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                jb_signUp.setBackground(clr_blue_hover);
            }
            public void mouseExited(MouseEvent e) {
                jb_signUp.setBackground(clr_blue);
            }
        });

        // Hover + click effect for Login link
        jl_loginLink.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                jl_loginLink.setForeground(clr_link_hover);
            }
            public void mouseExited(MouseEvent e) {
                jl_loginLink.setForeground(clr_link);
            }
            public void mouseClicked(MouseEvent e) {
                login.setVisible(true);
                dispose();
            }
        });

        // Placeholders 
        jt_fullName.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (jt_fullName.getText().equals(ph_fullName)) {
                    jt_fullName.setText("");
                    jt_fullName.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (jt_fullName.getText().trim().length() == 0) {
                    jt_fullName.setText(ph_fullName);
                    jt_fullName.setForeground(Color.GRAY);
                }
            }
        });

        jt_dob.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (jt_dob.getText().equals(ph_dob)) {
                    jt_dob.setText("");
                    jt_dob.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (jt_dob.getText().trim().length() == 0) {
                    jt_dob.setText(ph_dob);
                    jt_dob.setForeground(Color.GRAY);
                }
            }
        });

        jt_address.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (jt_address.getText().equals(ph_address)) {
                    jt_address.setText("");
                    jt_address.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (jt_address.getText().trim().length() == 0) {
                    jt_address.setText(ph_address);
                    jt_address.setForeground(Color.GRAY);
                }
            }
        });

        jt_contact.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (jt_contact.getText().equals(ph_contact)) {
                    jt_contact.setText("");
                    jt_contact.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (jt_contact.getText().trim().length() == 0) {
                    jt_contact.setText(ph_contact);
                    jt_contact.setForeground(Color.GRAY);
                }
            }
        });

        jt_course.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (jt_course.getText().equals(ph_course)) {
                    jt_course.setText("");
                    jt_course.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (jt_course.getText().trim().length() == 0) {
                    jt_course.setText(ph_course);
                    jt_course.setForeground(Color.GRAY);
                }
            }
        });

        jt_email.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (jt_email.getText().equals(ph_email)) {
                    jt_email.setText("");
                    jt_email.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (jt_email.getText().trim().length() == 0) {
                    jt_email.setText(ph_email);
                    jt_email.setForeground(Color.GRAY);
                }
            }
        });
        
        jt_username.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (jt_username.getText().equals(ph_username)) {
                    jt_username.setText("");
                    jt_username.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (jt_username.getText().trim().length() == 0) {
                    jt_username.setText(ph_username);
                    jt_username.setForeground(Color.GRAY);
                }
            }
        });

        // Register button click: validate fields and show result
        jb_signUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // Read all user inputs (trim spaces)
                String fullName = jt_fullName.getText().trim();
                String dob = jt_dob.getText().trim();
                String address = jt_address.getText().trim();
                String contact = jt_contact.getText().trim();
                String course = jt_course.getText().trim();
                String email = jt_email.getText().trim();
                String username = jt_username.getText().trim();
                
                // Password fields return char[] for security reasons
                char[] pass1 = jp_password.getPassword();
                char[] pass2 = jp_confirmPassword.getPassword();
                
                String normalized = dob.replace('/', '-');
                LocalDate dobDate = LocalDate.parse(normalized);
                LocalDate today = LocalDate.now();

                // Convert placeholder text into empty strings for validation
                if (fullName.equals(ph_fullName)) fullName = "";
                if (dob.equals(ph_dob)) dob = "";
                if (address.equals(ph_address)) address = "";
                if (contact.equals(ph_contact)) contact = "";
                if (course.equals(ph_course)) course = "";
                if (email.equals(ph_email)) email = "";
                if (username.equals(ph_username)) username = "";

                // Validate required fields in order (stop at first invalid)
                if (fullName.length() == 0) {
                    JOptionPane.showMessageDialog(null, "Please enter your full name.", "Invalid Field", JOptionPane.ERROR_MESSAGE);
                    jt_fullName.requestFocus();
                    return;
                }

                if (dob.length() == 0) {
                    JOptionPane.showMessageDialog(null, "Please enter your date of birth (YYYY/MM/DD).", "Invalid Field", JOptionPane.ERROR_MESSAGE);
                    jt_dob.requestFocus();
                    return;
                }
                

                if (dobDate.isAfter(today.minusYears(18))) {
                    JOptionPane.showMessageDialog(null,
                            "You must be at least 18 years old to register.",
                            "Invalid DOB", JOptionPane.ERROR_MESSAGE);
                    jt_dob.requestFocus();
                    return;
                }

                if (contact.length() < 8) {
                    JOptionPane.showMessageDialog(null, "Please enter your contact number.", "Invalid Field", JOptionPane.ERROR_MESSAGE);
                    jt_contact.requestFocus();
                    return;
                }

                if (!jrb_male.isSelected() && !jrb_female.isSelected() && !jrb_ratherNot.isSelected()) {
                    JOptionPane.showMessageDialog(null, "Please select a gender.", "Invalid Field", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (course.length() == 0) {
                    JOptionPane.showMessageDialog(null, "Please enter your course.", "Invalid Field", JOptionPane.ERROR_MESSAGE);
                    jt_course.requestFocus();
                    return;
                }

                if (email.length() == 0 || !email.contains("@")) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid email address.", "Invalid Field", JOptionPane.ERROR_MESSAGE);
                    jt_email.requestFocus();
                    return;
                }
                
                if (username.length() == 0) {
                    JOptionPane.showMessageDialog(null, "Please enter a username.", "Invalid Field", JOptionPane.ERROR_MESSAGE);
                    jt_username.requestFocus();
                    return;
                }

                // Optional: basic rule example
                if (username.length() < 3) {
                    JOptionPane.showMessageDialog(null, "Username must be at least 3 characters.", "Invalid Field", JOptionPane.ERROR_MESSAGE);
                    jt_username.requestFocus();
                    return;
                }

                if (pass1.length == 0) {
                    JOptionPane.showMessageDialog(null, "Please enter a password.", "Invalid Field", JOptionPane.ERROR_MESSAGE);
                    jp_password.requestFocus();
                    return;
                }

                if (pass2.length == 0) {
                    JOptionPane.showMessageDialog(null, "Please confirm your password.", "Invalid Field", JOptionPane.ERROR_MESSAGE);
                    jp_confirmPassword.requestFocus();
                    return;
                }

                // Check if password and confirm password match
                if (!String.valueOf(pass1).equals(String.valueOf(pass2))) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match.", "Invalid Field", JOptionPane.ERROR_MESSAGE);
                    jp_confirmPassword.requestFocus();
                    return;
                }
                
                boolean registered = registerStudent(fullName,dob,address,contact,course,email,username, pass1);
                
                if (registered) {
                    login.setVisible(true);
                    dispose();

                }
                

            }
        });
        

    }
    
    private void resetRegistrationForm() {
        // Text fields back to placeholders
        jt_fullName.setText(ph_fullName);
        jt_fullName.setForeground(Color.GRAY);

        jt_dob.setText(ph_dob);
        jt_dob.setForeground(Color.GRAY);

        jt_address.setText(ph_address);
        jt_address.setForeground(Color.GRAY);

        jt_contact.setText(ph_contact);
        jt_contact.setForeground(Color.GRAY);

        jt_course.setText(ph_course);
        jt_course.setForeground(Color.GRAY);

        jt_email.setText(ph_email);
        jt_email.setForeground(Color.GRAY);
        
        jt_username.setText(ph_username);
        jt_username.setForeground(Color.GRAY);

        // Password fields
        jp_password.setText("");
        jp_confirmPassword.setText("");

        // Radio buttons (gender)
        bg_gender.clearSelection();

        // Combo box
        jcb_faculty.setSelectedIndex(0);
        jcb_section.setSelectedIndex(0);

        // Put cursor on first field
        jt_fullName.requestFocusInWindow();
    }
    
    
    private boolean registerStudent(String fullName, String dob, String address, String contact, String course, String email, String username, char[] pass1) {
        
  
        String faculty = (String) jcb_faculty.getSelectedItem();
        String section = (String) jcb_section.getSelectedItem();
        boolean status = false;

        String gender;
        if (jrb_male.isSelected()) gender = "Male";
        else if (jrb_female.isSelected()) gender = "Female";
        else gender = "Rather not say";

        // Hash password 
        String hashed = BCrypt.hashpw(new String(pass1), BCrypt.gensalt(12));
        java.util.Arrays.fill(pass1, '\0'); 

        // Convert DOB to SQL Date
        java.sql.Date sqlDob;
        try {
            
            String normalized = dob.replace('/', '-');
            sqlDob = java.sql.Date.valueOf(normalized); // yyyy-mm-dd
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null,
                    "Invalid DOB format. Use YYYY/MM/DD.",
                    "Invalid Field", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        String UserSql =
                "INSERT INTO user (username, email, password, role) VALUES (?, ?, ?, ?)";

        String StudentSql =
        		  "INSERT INTO student " +
        		  "(fullName, faculty, course, contactNumber, placementStatus, address, dob, gender, userId, section) " +
        		  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String EmailSql = "SELECT userId from user WHERE email=?";
        
        int userId = 0;
        
        try (	
	        	Connection con = DBConnection.getConnection();
	            PreparedStatement myStmt = con.prepareStatement(UserSql)) {

	            myStmt.setString(1, username);
	            myStmt.setString(2, email);
	            myStmt.setString(3, hashed);
	            myStmt.setString(4, "student");

	            int rows = myStmt.executeUpdate();
	            
	            if (rows != 1) {
	            	
	                JOptionPane.showMessageDialog(null,
	                        "Registration Failed. Try again",
	                        "Registration Process Failed",
	                        JOptionPane.ERROR_MESSAGE);
	                
	                return false;
	            	
	            }
	            	
            	try (PreparedStatement myStmt2 = con.prepareStatement(EmailSql)) {  
	            	myStmt2.setString(1, email);
	            	 
	            	ResultSet result = myStmt2.executeQuery();
	 	            
	 	            if (!result.next()) { 
	 	            // no matching user Id found
	 	                JOptionPane.showMessageDialog(StudentRegistrationFrame.this,
		                        "Registration Failed. Try again",
		                        "Registration Process Failed",
		                        JOptionPane.ERROR_MESSAGE);
	 	               resetRegistrationForm();
		                return false;   
	                } 
	 	            
	 	           userId = result.getInt("userId");
            	
	 	            
	 	           rows = -1;
            	}
 	           try(PreparedStatement myStmt3 = con.prepareStatement(StudentSql)) {
 	        	   
		 	       myStmt3.setString(1, fullName);
		 	       myStmt3.setString(2, faculty);
		 	       myStmt3.setString(3, course);
		 	       myStmt3.setString(4, contact);
		 	      myStmt3.setBoolean(5, status);
		 	       myStmt3.setString(6, (address == null || address.isBlank()) ? null : address);
		 	       myStmt3.setDate(7, sqlDob);
		 	       myStmt3.setString(8, gender);
		 	      myStmt3.setInt(9, userId);
		 	     myStmt3.setString(10, section);
		             
		 	       rows = myStmt3.executeUpdate();
			 	       
		 	       if (rows != 1) {
		                JOptionPane.showMessageDialog(StudentRegistrationFrame.this,
		                        "Registration Failed. Try again ",
		                        "Registration Process Failed",
		                        JOptionPane.ERROR_MESSAGE);
		                resetRegistrationForm();
		                return false;
		 	       }
	 	       
 	           }
 	           
               JOptionPane.showMessageDialog(StudentRegistrationFrame.this,
                       "Registration Successful! Proceed to the login page",
                       "Registration Process Completed",
                       JOptionPane.INFORMATION_MESSAGE);

	 	          
	          
        } catch (SQLIntegrityConstraintViolationException dup) {
            JOptionPane.showMessageDialog(StudentRegistrationFrame.this,
                    "This email or username is already registered.",
                    "Registration Failed", JOptionPane.ERROR_MESSAGE);
            return false;

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(StudentRegistrationFrame.this,
                    "Database error while registering.",
                    "Registration Failed", JOptionPane.ERROR_MESSAGE);
            resetRegistrationForm();
            return false;
            
        }
        return true;
    }

}
