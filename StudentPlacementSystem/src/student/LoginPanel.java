package student;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends JFrame {
    
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton loginButton;
    private JButton signUpButton;
    
    // Color scheme
    private final Color BLUE = new Color(41, 98, 255);
    private final Color WHITE = Color.WHITE;
    private final Color BORDER_COLOR = new Color(180, 180, 180);
    private final Color TEXT_COLOR = new Color(60, 60, 60);
    
    public LoginPanel() {
        // Frame setup
        setTitle("Login");
        setSize(600, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main layout - BorderLayout
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(WHITE);
        
        // Create components
        createFormPanel();
        createFooterPanel();
        
        setVisible(true);
    }
    
    private void createFormPanel() {
        // Main form panel with GridLayout
        JPanel formPanel = new JPanel(new GridLayout(7, 1, 0, 20));
        formPanel.setBackground(WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(80, 50, 50, 50));
        
        // Email Label
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emailLabel.setForeground(TEXT_COLOR);
        formPanel.add(emailLabel);
        
        // Email TextField
        emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 16));
        emailField.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        emailField.setPreferredSize(new Dimension(0, 40));
        formPanel.add(emailField);
        
        // Password Label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordLabel.setForeground(TEXT_COLOR);
        formPanel.add(passwordLabel);
        
        // Password Field
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        passwordField.setPreferredSize(new Dimension(0, 40));
        formPanel.add(passwordField);
        
        // Role Label
        JLabel roleLabel = new JLabel("Role");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        roleLabel.setForeground(TEXT_COLOR);
        formPanel.add(roleLabel);
        
        // Role ComboBox
        String[] roles = {"Admin", "Student", "Company", "Placement Admin"};
        roleComboBox = new JComboBox<String>(roles);
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        roleComboBox.setBackground(WHITE);
        roleComboBox.setPreferredSize(new Dimension(0, 40));
        formPanel.add(roleComboBox);
        
        // Login Button Panel with FlowLayout
        JPanel loginButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 20));
        loginButtonPanel.setBackground(WHITE);
        
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setForeground(WHITE);
        loginButton.setBackground(BLUE);
        loginButton.setPreferredSize(new Dimension(150, 45));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add action listener
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        loginButtonPanel.add(loginButton);
        formPanel.add(loginButtonPanel);
        
        add(formPanel, BorderLayout.CENTER);
    }
    
    private void createFooterPanel() {
        // Footer with "I don't have an account" and Sign Up button
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 40));
        footerPanel.setBackground(WHITE);
        
        JLabel noAccountLabel = new JLabel("I don't have an account");
        noAccountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        noAccountLabel.setForeground(TEXT_COLOR);
        
        signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Arial", Font.PLAIN, 14));
        signUpButton.setForeground(BLUE);
        signUpButton.setBackground(WHITE);
        signUpButton.setBorder(BorderFactory.createLineBorder(BLUE, 1));
        signUpButton.setFocusPainted(false);
        signUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpButton.setPreferredSize(new Dimension(90, 35));
        
        // Add action listener
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleSignUp();
            }
        });
        
        footerPanel.add(noAccountLabel);
        footerPanel.add(signUpButton);
        
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private void handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();
        
        // Print to console
        System.out.println("Login attempted:");
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        System.out.println("Role: " + role);
    }
    
    private void handleSignUp() {
        // Print to console
        System.out.println("Sign Up button clicked");
    }
    
    public static void main(String[] args) {
        // Run on Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginPanel();
            }
        });
    }
}