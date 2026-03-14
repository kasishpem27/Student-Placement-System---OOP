package company;

import dbconnection.DBConnection;
import student.LoginFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CompanyRegisterFrame extends JFrame {

    // ── UI COMPONENTS ──
    private JLabel jl_leftLogo, jl_leftCompanyName, jl_leftCopyright;
    private JLabel jl_title;
    private JLabel jl_companyName, jl_address, jl_contact, jl_industry, jl_email, jl_website, jl_brn, jl_desc, jl_password, jl_confirmPassword;
    private JTextField jt_companyName, jt_address, jt_contact, jt_industry, jt_email, jt_website, jt_brn;
    private JTextArea jta_desc;
    private JPasswordField jp_password, jp_confirmPassword;
    private JButton jb_register;
    private JLabel jl_loginLink;

    // ── COLORS ──
    private final Color clr_blue        = new Color(0, 102, 153);
    private final Color clr_blue_hover  = new Color(0, 122, 183);
    private final Color clr_link        = new Color(0, 102, 153);
    private final Color clr_link_hover  = new Color(120, 180, 230);
    private final Color clr_white       = Color.WHITE;
    private final Color clr_leftPanel   = new Color(0, 102, 153);
    private final Color clr_fieldBorder = new Color(200, 200, 200);

    // ── PLACEHOLDERS ──
    private final String ph_companyName = "Enter your company name";
    private final String ph_address     = "Enter your address";
    private final String ph_contact     = "Enter your contact number";
    private final String ph_industry    = "Enter your industry";
    private final String ph_email       = "Enter your email";
    private final String ph_website     = "Enter your company website";
    private final String ph_brn         = "Enter your BRN";
    private final String ph_desc        = "Enter company description";

    private LoginFrame login;

    public CompanyRegisterFrame(LoginFrame login) {
        super("CareerConnect - Company Registration");
        this.login = login;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 750));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(clr_white);

        // ── MAIN CONTAINER ──
        JPanel jp_main = new JPanel(new GridLayout(1, 2, 0, 0));
        jp_main.setBackground(clr_white);

        // ── LEFT PANEL ──
        JPanel jp_left = new JPanel(new BorderLayout());
        jp_left.setBackground(clr_leftPanel);
        jp_left.setBorder(new EmptyBorder(25, 20, 25, 20));

        JPanel jp_leftCenter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jp_leftCenter.setBackground(clr_leftPanel);

        URL logoUrl = getClass().getResource("/images/CareerConnect.png");
        jl_leftLogo = new JLabel();
        if (logoUrl != null) {
            Image scaled = new ImageIcon(logoUrl).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            jl_leftLogo = new JLabel(new ImageIcon(scaled));
        }
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

        jl_leftCopyright = new JLabel("Copyright \u00a9 CareerConnect All rights reserved");
        jl_leftCopyright.setHorizontalAlignment(SwingConstants.CENTER);
        jl_leftCopyright.setForeground(Color.WHITE);
        jl_leftCopyright.setFont(new Font("Arial", Font.PLAIN, 11));

        jp_left.add(jp_leftMiddle, BorderLayout.CENTER);
        jp_left.add(jl_leftCopyright, BorderLayout.SOUTH);

        // ── RIGHT PANEL ──
        JPanel jp_right = new JPanel(new BorderLayout());
        jp_right.setBackground(clr_white);
        jp_right.setBorder(new EmptyBorder(25, 40, 25, 40));

        // Title
        jl_title = new JLabel("COMPANY REGISTRATION FORM");
        jl_title.setForeground(clr_blue);
        jl_title.setFont(new Font("Arial", Font.BOLD, 28));
        jl_title.setBorder(new EmptyBorder(0, 0, 15, 0));

        // ── FORM FIELDS ──

        // Company Name
        jl_companyName = new JLabel("<html>Company Name <font color='red'>*</font></html>");
        jl_companyName.setFont(new Font("Arial", Font.BOLD, 13));
        jt_companyName = new JTextField(ph_companyName);
        jt_companyName.setForeground(Color.GRAY);
        jt_companyName.setBorder(new LineBorder(clr_fieldBorder, 1));
        jt_companyName.setPreferredSize(new Dimension(0, 30));

        // Address
        jl_address = new JLabel("<html>Address <font color='red'>*</font></html>");
        jl_address.setFont(new Font("Arial", Font.BOLD, 13));
        jt_address = new JTextField(ph_address);
        jt_address.setForeground(Color.GRAY);
        jt_address.setBorder(new LineBorder(clr_fieldBorder, 1));
        jt_address.setPreferredSize(new Dimension(0, 30));

        // Contact
        jl_contact = new JLabel("<html>Contact Number <font color='red'>*</font></html>");
        jl_contact.setFont(new Font("Arial", Font.BOLD, 13));
        jt_contact = new JTextField(ph_contact);
        jt_contact.setForeground(Color.GRAY);
        jt_contact.setBorder(new LineBorder(clr_fieldBorder, 1));
        jt_contact.setPreferredSize(new Dimension(0, 30));

        // Industry
        jl_industry = new JLabel("<html>Industry <font color='red'>*</font></html>");
        jl_industry.setFont(new Font("Arial", Font.BOLD, 13));
        jt_industry = new JTextField(ph_industry);
        jt_industry.setForeground(Color.GRAY);
        jt_industry.setBorder(new LineBorder(clr_fieldBorder, 1));
        jt_industry.setPreferredSize(new Dimension(0, 30));

        // Email
        jl_email = new JLabel("<html>Email <font color='red'>*</font></html>");
        jl_email.setFont(new Font("Arial", Font.BOLD, 13));
        jt_email = new JTextField(ph_email);
        jt_email.setForeground(Color.GRAY);
        jt_email.setBorder(new LineBorder(clr_fieldBorder, 1));
        jt_email.setPreferredSize(new Dimension(0, 30));

        // Website
        jl_website = new JLabel("Company Website");
        jl_website.setFont(new Font("Arial", Font.BOLD, 13));
        jt_website = new JTextField(ph_website);
        jt_website.setForeground(Color.GRAY);
        jt_website.setBorder(new LineBorder(clr_fieldBorder, 1));
        jt_website.setPreferredSize(new Dimension(0, 30));

        // BRN
        jl_brn = new JLabel("<html>BRN <font color='red'>*</font></html>");
        jl_brn.setFont(new Font("Arial", Font.BOLD, 13));
        jt_brn = new JTextField(ph_brn);
        jt_brn.setForeground(Color.GRAY);
        jt_brn.setBorder(new LineBorder(clr_fieldBorder, 1));
        jt_brn.setPreferredSize(new Dimension(0, 30));

        // Description
        jl_desc = new JLabel("Company Description");
        jl_desc.setFont(new Font("Arial", Font.BOLD, 13));
        jta_desc = new JTextArea(ph_desc);
        jta_desc.setForeground(Color.GRAY);
        jta_desc.setFont(new Font("Arial", Font.PLAIN, 13));
        jta_desc.setLineWrap(true);
        jta_desc.setWrapStyleWord(true);
        jta_desc.setBorder(new LineBorder(clr_fieldBorder, 1));
        jta_desc.setRows(3);

        // Password
        jl_password = new JLabel("<html>Password <font color='red'>*</font></html>");
        jl_password.setFont(new Font("Arial", Font.BOLD, 13));
        jp_password = new JPasswordField();
        jp_password.setBorder(new LineBorder(clr_fieldBorder, 1));
        jp_password.setPreferredSize(new Dimension(0, 30));

        // Confirm Password
        jl_confirmPassword = new JLabel("<html>Confirm Password <font color='red'>*</font></html>");
        jl_confirmPassword.setFont(new Font("Arial", Font.BOLD, 13));
        jp_confirmPassword = new JPasswordField();
        jp_confirmPassword.setBorder(new LineBorder(clr_fieldBorder, 1));
        jp_confirmPassword.setPreferredSize(new Dimension(0, 30));

        // ── BUILD FORM using nested BorderLayout stacking ──

        // Row: Confirm Password (now innermost)
        JPanel jp_row_confirmPassword = new JPanel(new BorderLayout(0, 10));
        jp_row_confirmPassword.setBackground(clr_white);
        jp_row_confirmPassword.setBorder(new EmptyBorder(0, 0, 4, 0));
        jp_row_confirmPassword.add(jl_confirmPassword, BorderLayout.NORTH);
        jp_row_confirmPassword.add(jp_confirmPassword, BorderLayout.CENTER);
        JPanel jp_wrap_confirmPassword = new JPanel(new BorderLayout());
        jp_wrap_confirmPassword.setBackground(clr_white);
        jp_wrap_confirmPassword.add(jp_row_confirmPassword, BorderLayout.NORTH);

        // Row: Password
        JPanel jp_row_password = new JPanel(new BorderLayout(0, 10));
        jp_row_password.setBackground(clr_white);
        jp_row_password.setBorder(new EmptyBorder(0, 0, 12, 0));
        jp_row_password.add(jl_password, BorderLayout.NORTH);
        jp_row_password.add(jp_password, BorderLayout.CENTER);
        JPanel jp_wrap_password = new JPanel(new BorderLayout());
        jp_wrap_password.setBackground(clr_white);
        jp_wrap_password.add(jp_row_password, BorderLayout.NORTH);
        jp_wrap_password.add(jp_wrap_confirmPassword, BorderLayout.CENTER);

        // Row: Description
        JPanel jp_row_description = new JPanel(new BorderLayout(0, 10));
        jp_row_description.setBackground(clr_white);
        jp_row_description.setBorder(new EmptyBorder(0, 0, 12, 0));
        jp_row_description.add(jl_desc, BorderLayout.NORTH);
        jp_row_description.add(jta_desc, BorderLayout.CENTER);
        JPanel jp_wrap_description = new JPanel(new BorderLayout());
        jp_wrap_description.setBackground(clr_white);
        jp_wrap_description.add(jp_row_description, BorderLayout.NORTH);
        jp_wrap_description.add(jp_wrap_password, BorderLayout.CENTER);

        // Row: BRN
        JPanel jp_row_brn = new JPanel(new BorderLayout(0, 10));
        jp_row_brn.setBackground(clr_white);
        jp_row_brn.setBorder(new EmptyBorder(0, 0, 12, 0));
        jp_row_brn.add(jl_brn, BorderLayout.NORTH);
        jp_row_brn.add(jt_brn, BorderLayout.CENTER);
        JPanel jp_wrap_brn = new JPanel(new BorderLayout());
        jp_wrap_brn.setBackground(clr_white);
        jp_wrap_brn.add(jp_row_brn, BorderLayout.NORTH);
        jp_wrap_brn.add(jp_wrap_description, BorderLayout.CENTER);

        // Row: Website
        JPanel jp_row_website = new JPanel(new BorderLayout(0, 10));
        jp_row_website.setBackground(clr_white);
        jp_row_website.setBorder(new EmptyBorder(0, 0, 12, 0));
        jp_row_website.add(jl_website, BorderLayout.NORTH);
        jp_row_website.add(jt_website, BorderLayout.CENTER);
        JPanel jp_wrap_website = new JPanel(new BorderLayout());
        jp_wrap_website.setBackground(clr_white);
        jp_wrap_website.add(jp_row_website, BorderLayout.NORTH);
        jp_wrap_website.add(jp_wrap_brn, BorderLayout.CENTER);

        // Row: Email
        JPanel jp_row_email = new JPanel(new BorderLayout(0, 10));
        jp_row_email.setBackground(clr_white);
        jp_row_email.setBorder(new EmptyBorder(0, 0, 12, 0));
        jp_row_email.add(jl_email, BorderLayout.NORTH);
        jp_row_email.add(jt_email, BorderLayout.CENTER);
        JPanel jp_wrap_email = new JPanel(new BorderLayout());
        jp_wrap_email.setBackground(clr_white);
        jp_wrap_email.add(jp_row_email, BorderLayout.NORTH);
        jp_wrap_email.add(jp_wrap_website, BorderLayout.CENTER);

        // Row: Industry
        JPanel jp_row_industry = new JPanel(new BorderLayout(0, 10));
        jp_row_industry.setBackground(clr_white);
        jp_row_industry.setBorder(new EmptyBorder(0, 0, 12, 0));
        jp_row_industry.add(jl_industry, BorderLayout.NORTH);
        jp_row_industry.add(jt_industry, BorderLayout.CENTER);
        JPanel jp_wrap_industry = new JPanel(new BorderLayout());
        jp_wrap_industry.setBackground(clr_white);
        jp_wrap_industry.add(jp_row_industry, BorderLayout.NORTH);
        jp_wrap_industry.add(jp_wrap_email, BorderLayout.CENTER);

        // Row: Contact
        JPanel jp_row_contact = new JPanel(new BorderLayout(0, 10));
        jp_row_contact.setBackground(clr_white);
        jp_row_contact.setBorder(new EmptyBorder(0, 0, 12, 0));
        jp_row_contact.add(jl_contact, BorderLayout.NORTH);
        jp_row_contact.add(jt_contact, BorderLayout.CENTER);
        JPanel jp_wrap_contact = new JPanel(new BorderLayout());
        jp_wrap_contact.setBackground(clr_white);
        jp_wrap_contact.add(jp_row_contact, BorderLayout.NORTH);
        jp_wrap_contact.add(jp_wrap_industry, BorderLayout.CENTER);

        // Row: Address
        JPanel jp_row_address = new JPanel(new BorderLayout(0, 10));
        jp_row_address.setBackground(clr_white);
        jp_row_address.setBorder(new EmptyBorder(0, 0, 12, 0));
        jp_row_address.add(jl_address, BorderLayout.NORTH);
        jp_row_address.add(jt_address, BorderLayout.CENTER);
        JPanel jp_wrap_address = new JPanel(new BorderLayout());
        jp_wrap_address.setBackground(clr_white);
        jp_wrap_address.add(jp_row_address, BorderLayout.NORTH);
        jp_wrap_address.add(jp_wrap_contact, BorderLayout.CENTER);

        // Row: Company Name
        JPanel jp_row_companyName = new JPanel(new BorderLayout(0, 10));
        jp_row_companyName.setBackground(clr_white);
        jp_row_companyName.setBorder(new EmptyBorder(0, 0, 12, 0));
        jp_row_companyName.add(jl_companyName, BorderLayout.NORTH);
        jp_row_companyName.add(jt_companyName, BorderLayout.CENTER);
        JPanel jp_form = new JPanel(new BorderLayout());
        jp_form.setBackground(clr_white);
        jp_form.add(jp_row_companyName, BorderLayout.NORTH);
        jp_form.add(jp_wrap_address, BorderLayout.CENTER);

        // Wrapper keeps the form pinned to the top inside the scroll pane
        JPanel jp_formWrapper = new JPanel(new BorderLayout());
        jp_formWrapper.setBackground(clr_white);
        jp_formWrapper.add(jp_form, BorderLayout.NORTH);

        // Scroll pane wraps the form
        JScrollPane sp_form = new JScrollPane(jp_formWrapper);
        sp_form.setBorder(null);
        sp_form.getViewport().setBackground(clr_white);
        sp_form.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp_form.getVerticalScrollBar().setUnitIncrement(16);

        // ── BOTTOM ──
        JPanel jp_bottom = new JPanel(new GridLayout(2, 1, 0, 10));
        jp_bottom.setBackground(clr_white);
        jp_bottom.setBorder(new EmptyBorder(8, 0, 0, 0));

        JPanel jp_buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jp_buttonRow.setBackground(clr_white);
        jb_register = new JButton("Register");
        jb_register.setBackground(clr_blue);
        jb_register.setForeground(Color.WHITE);
        jb_register.setFocusPainted(false);
        jb_register.setOpaque(true);
        jb_register.setContentAreaFilled(true);
        jb_register.setBorderPainted(false);
        jb_register.setPreferredSize(new Dimension(140, 40));
        jp_buttonRow.add(jb_register);

        JPanel jp_loginRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jp_loginRow.setBackground(clr_white);
        jl_loginLink = new JLabel("Already have an account? Login");
        jl_loginLink.setForeground(clr_link);
        jl_loginLink.setFont(new Font("Arial", Font.BOLD, 13));
        jl_loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jp_loginRow.add(jl_loginLink);

        jp_bottom.add(jp_buttonRow);
        jp_bottom.add(jp_loginRow);

        // ── ASSEMBLE RIGHT ──
        jp_right.add(jl_title, BorderLayout.NORTH);
        jp_right.add(sp_form, BorderLayout.CENTER);
        jp_right.add(jp_bottom, BorderLayout.SOUTH);

        // ── ASSEMBLE MAIN ──
        jp_main.add(jp_left);
        jp_main.add(jp_right);
        add(jp_main, BorderLayout.CENTER);

        // ── HANDLERS ──
        jb_register.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                jb_register.setBackground(clr_blue_hover);
            }
            public void mouseExited(MouseEvent e) {
                jb_register.setBackground(clr_blue);
            }
        });

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

        // ── PLACEHOLDERS ──
        jt_companyName.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (jt_companyName.getText().equals(ph_companyName)) {
                    jt_companyName.setText("");
                    jt_companyName.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (jt_companyName.getText().trim().isEmpty()) {
                    jt_companyName.setText(ph_companyName);
                    jt_companyName.setForeground(Color.GRAY);
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
                if (jt_address.getText().trim().isEmpty()) {
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
                if (jt_contact.getText().trim().isEmpty()) {
                    jt_contact.setText(ph_contact);
                    jt_contact.setForeground(Color.GRAY);
                }
            }
        });
        jt_industry.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (jt_industry.getText().equals(ph_industry)) {
                    jt_industry.setText("");
                    jt_industry.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (jt_industry.getText().trim().isEmpty()) {
                    jt_industry.setText(ph_industry);
                    jt_industry.setForeground(Color.GRAY);
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
                if (jt_email.getText().trim().isEmpty()) {
                    jt_email.setText(ph_email);
                    jt_email.setForeground(Color.GRAY);
                }
            }
        });
        jt_website.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (jt_website.getText().equals(ph_website)) {
                    jt_website.setText("");
                    jt_website.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (jt_website.getText().trim().isEmpty()) {
                    jt_website.setText(ph_website);
                    jt_website.setForeground(Color.GRAY);
                }
            }
        });
        jt_brn.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (jt_brn.getText().equals(ph_brn)) {
                    jt_brn.setText("");
                    jt_brn.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (jt_brn.getText().trim().isEmpty()) {
                    jt_brn.setText(ph_brn);
                    jt_brn.setForeground(Color.GRAY);
                }
            }
        });
        jta_desc.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (jta_desc.getText().equals(ph_desc)) {
                    jta_desc.setText("");
                    jta_desc.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (jta_desc.getText().trim().isEmpty()) {
                    jta_desc.setText(ph_desc);
                    jta_desc.setForeground(Color.GRAY);
                }
            }
        });

        // ── REGISTER ACTION ──
        jb_register.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String name     = jt_companyName.getText().trim();
                String address  = jt_address.getText().trim();
                String contact  = jt_contact.getText().trim();
                String industry = jt_industry.getText().trim();
                String email    = jt_email.getText().trim();
                String website  = jt_website.getText().trim();
                String brn      = jt_brn.getText().trim();
                String desc     = jta_desc.getText().trim();
                char[] pass1    = jp_password.getPassword();
                char[] pass2    = jp_confirmPassword.getPassword();

                if (name.equals(ph_companyName)) {
                    name = "";
                }
                if (address.equals(ph_address)) {
                    address = "";
                }
                if (contact.equals(ph_contact)) {
                    contact = "";
                }
                if (industry.equals(ph_industry)) {
                    industry = "";
                }
                if (email.equals(ph_email)) {
                    email = "";
                }
                if (website.equals(ph_website)) {
                    website = "";
                }
                if (brn.equals(ph_brn)) {
                    brn = "";
                }
                if (desc.equals(ph_desc)) {
                    desc = "";
                }

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(CompanyRegisterFrame.this, "Please enter your company name.", "Invalid Field", JOptionPane.ERROR_MESSAGE);
                    jt_companyName.requestFocus();
                    return;
                }
                if (address.isEmpty()) {
                    JOptionPane.showMessageDialog(CompanyRegisterFrame.this, "Please enter your address.", "Invalid Field", JOptionPane.ERROR_MESSAGE);
                    jt_address.requestFocus();
                    return;
                }
                if (contact.length() < 7) {
                    JOptionPane.showMessageDialog(CompanyRegisterFrame.this, "Please enter your contact number.", "Invalid Field", JOptionPane.ERROR_MESSAGE);
                    jt_contact.requestFocus();
                    return;
                }
                if (industry.isEmpty()) {
                    JOptionPane.showMessageDialog(CompanyRegisterFrame.this, "Please enter your industry.", "Invalid Field", JOptionPane.ERROR_MESSAGE);
                    jt_industry.requestFocus();
                    return;
                }
                if (email.isEmpty() || !email.contains("@")) {
                    JOptionPane.showMessageDialog(CompanyRegisterFrame.this, "Please enter a valid email.", "Invalid Field", JOptionPane.ERROR_MESSAGE);
                    jt_email.requestFocus();
                    return;
                }
                if (brn.isEmpty()) {
                    JOptionPane.showMessageDialog(CompanyRegisterFrame.this, "Please enter your BRN.", "Invalid Field", JOptionPane.ERROR_MESSAGE);
                    jt_brn.requestFocus();
                    return;
                }
                if (!brn.toUpperCase().matches("[A-Z0-9]{10}")) {
                    JOptionPane.showMessageDialog(CompanyRegisterFrame.this, "BRN must be exactly 10 alphanumeric characters.", "Invalid Field", JOptionPane.ERROR_MESSAGE);
                    jt_brn.requestFocus();
                    return;
                }
                if (pass1.length == 0) {
                    JOptionPane.showMessageDialog(CompanyRegisterFrame.this, "Please enter a password.", "Invalid Field", JOptionPane.ERROR_MESSAGE);
                    jp_password.requestFocus();
                    return;
                }
                if (pass2.length == 0) {
                    JOptionPane.showMessageDialog(CompanyRegisterFrame.this, "Please confirm your password.", "Invalid Field", JOptionPane.ERROR_MESSAGE);
                    jp_confirmPassword.requestFocus();
                    return;
                }
                if (!String.valueOf(pass1).equals(String.valueOf(pass2))) {
                    JOptionPane.showMessageDialog(CompanyRegisterFrame.this, "Passwords do not match.", "Invalid Field", JOptionPane.ERROR_MESSAGE);
                    jp_confirmPassword.requestFocus();
                    return;
                }

                registerCompany(name, address, contact, industry, email, website, brn, desc, pass1);
            }
        });
    }

    // ── DATABASE LOGIC ──

    private boolean registerCompany(String name, String address, String contact,
                                    String industry, String email, String website,
                                    String brn, String desc, char[] pass) {
        String hashed = BCrypt.hashpw(new String(pass), BCrypt.gensalt(12));
        java.util.Arrays.fill(pass, '\0');

        String username = name.replaceAll("\\s+", "").toLowerCase();
        if (username.length() > 20) {
            username = username.substring(0, 20);
        }

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                int userId;
                String sqlUser = "INSERT INTO `user` (username, email, `password`, role, accountStatus) VALUES (?, ?, ?, 'company', 'activated')";
                try (PreparedStatement ps = con.prepareStatement(sqlUser, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, username);
                    ps.setString(2, email);
                    ps.setString(3, hashed);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new Exception("Failed to get generated userId.");
                        }
                        userId = keys.getInt(1);
                    }
                }

                String sqlCompany = "INSERT INTO company (companyName, address, contactNumber, industry, companyDescription, companyWebsite, BRN, userId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                int companyId;
                try (PreparedStatement ps = con.prepareStatement(sqlCompany, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, name);
                    ps.setString(2, address);
                    ps.setString(3, contact);
                    ps.setString(4, industry);
                    if (desc.isEmpty()) {
                        ps.setNull(5, java.sql.Types.VARCHAR);
                    } else {
                        ps.setString(5, desc);
                    }
                    if (website.isEmpty()) {
                        ps.setNull(6, java.sql.Types.VARCHAR);
                    } else {
                        ps.setString(6, website);
                    }
                    ps.setString(7, brn.toUpperCase());
                    ps.setInt(8, userId);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new Exception("Failed to get generated companyId.");
                        }
                        companyId = keys.getInt(1);
                    }
                }

                con.commit();
                JOptionPane.showMessageDialog(CompanyRegisterFrame.this,
                        "Company registered successfully!\nCompany ID: COM" + companyId,
                        "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
                login.setVisible(true);
                dispose();
                return true;

            } catch (Exception ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (Exception ex) {
            String msg = ex.getMessage() == null ? "" : ex.getMessage().toLowerCase();
            if (msg.contains("email") || msg.contains("uk_") || msg.contains("unique")) {
                JOptionPane.showMessageDialog(CompanyRegisterFrame.this,
                        "Email or username already exists.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(CompanyRegisterFrame.this,
                        "Database error:\n" + ex.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
    }
}