package company;

import dbconnection.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CreateOpportunityFrame extends JFrame {

    private final Color clr_blue       = new Color(0, 102, 153);
    private final Color clr_blue_hover = new Color(0, 122, 183);
    private final Color clr_white      = Color.WHITE;
    private final Color clr_bg         = new Color(245, 247, 250);
    private final Color clr_cardBorder = new Color(220, 225, 230);
    private final Color clr_textDark   = new Color(40, 40, 40);
    private final Color clr_bottomBackground = new Color(240, 244, 248);

    private final String companyName;
    private final String companyId;
    private final CompanyDashboardFrame dashboard;

    // TAB TOGGLE BUTTONS
    private JButton jb_tabJob;
    private JButton jb_tabPlacement;
    private JPanel  jp_jobForm;
    private JPanel  jp_placementForm;
    private JPanel  jp_formHolder;

    // JOB FORM FIELDS
    private JTextField  jt_Job_title, jt_Job_location, jt_Job_salary, jt_Job_hours;
    private JTextArea   jta_Job_desc;
    private JRadioButton rbJob_fullTime, rbJob_partTime;
    private ButtonGroup  bgJob_jobType;
    private JRadioButton rbJob_onsite, rbJob_hybrid, rbJob_remote;
    private ButtonGroup  bgJob_mode;
    private JTextField jt_Job_deadline;
    private JCheckBox   jcb_Job_confirm;

    // PLACEMENT FORM FIELDS
    private JTextField  jt_Pl_title, jt_Pl_location, jt_Pl_stipend, jt_Pl_duration;
    private JTextArea   jta_Pl_desc;
    private JRadioButton rbPl_onsite, rbPl_hybrid, rbPl_remote;
    private ButtonGroup  bgPl_mode;
    private JTextField jt_Pl_deadline;
    private JCheckBox   jcb_Pl_confirm;

    public CreateOpportunityFrame(String companyName, String companyId, CompanyDashboardFrame dashboard) {
        super("CareerConnect - Create Opportunity");
        this.companyName = companyName;
        this.companyId   = companyId == null ? "1" : companyId.replaceAll("[^0-9]", "");
        this.dashboard   = dashboard;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 750));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // HEADER
        JPanel jp_header = new JPanel(new BorderLayout());
        jp_header.setBackground(clr_blue);
        jp_header.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel jp_headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        jp_headerLeft.setBackground(clr_blue);
        JLabel jl_headerTitle = new JLabel("Create Opportunity");
        jl_headerTitle.setForeground(clr_white);
        jl_headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jp_headerLeft.add(jl_headerTitle);

        JPanel jp_headerRightInner = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        jp_headerRightInner.setBackground(clr_blue);
        java.net.URL profileUrl = getClass().getResource("/images/profile.png");
        JLabel jl_profileIcon = new JLabel();
        if (profileUrl != null) {
            Image scaled = new ImageIcon(profileUrl).getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
            jl_profileIcon = new JLabel(new ImageIcon(scaled));
        }
        JLabel jl_userInfo = new JLabel(companyName + "  \u00b7  COM" + companyId);
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

        // MAIN WRAPPER
        JPanel jp_wrapper = new JPanel(new BorderLayout());
        jp_wrapper.setBackground(clr_bg);

        JPanel jp_content = new JPanel(new BorderLayout());
        jp_content.setBackground(clr_bg);
        jp_content.setBorder(new EmptyBorder(16, 18, 18, 18));

        // TOP BAR: back button left, tab buttons centred
        JPanel jp_topBar = new JPanel(new BorderLayout());
        jp_topBar.setBackground(clr_bg);
        jp_topBar.setBorder(new EmptyBorder(0, 0, 14, 0));

        JButton jb_back = new JButton("\u2190 Back");
        jb_back.setFocusPainted(false);
        jb_back.setBackground(clr_white);
        jb_back.setForeground(clr_textDark);
        jb_back.setBorder(new LineBorder(clr_cardBorder, 1));
        jb_back.setOpaque(true);
        jb_back.setPreferredSize(new Dimension(90, 32));
        jb_back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jb_back.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jp_topBar.add(jb_back, BorderLayout.WEST);

        // TAB TOGGLE BAR (centred between back button and edge)
        jb_tabJob       = new JButton("Job Opportunity");
        jb_tabPlacement = new JButton("Placement");

        Font tabFont = new Font("Segoe UI", Font.BOLD, 13);
        jb_tabJob.setFont(tabFont);
        jb_tabJob.setFocusPainted(false);
        jb_tabJob.setOpaque(true);
        jb_tabJob.setContentAreaFilled(true);
        jb_tabJob.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jb_tabJob.setPreferredSize(new Dimension(200, 36));
        jb_tabPlacement.setFont(tabFont);
        jb_tabPlacement.setFocusPainted(false);
        jb_tabPlacement.setOpaque(true);
        jb_tabPlacement.setContentAreaFilled(true);
        jb_tabPlacement.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jb_tabPlacement.setPreferredSize(new Dimension(200, 36));

        // Active = blue filled, Inactive = white with border
        jb_tabJob.setBackground(clr_blue);
        jb_tabJob.setForeground(clr_white);
        jb_tabJob.setBorder(new LineBorder(clr_blue, 2));
        jb_tabPlacement.setBackground(clr_white);
        jb_tabPlacement.setForeground(clr_textDark);
        jb_tabPlacement.setBorder(new LineBorder(clr_cardBorder, 2));

        JPanel jp_tabBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        jp_tabBar.setBackground(clr_bg);
        jp_tabBar.add(jb_tabJob);
        jp_tabBar.add(jb_tabPlacement);
        jp_topBar.add(jp_tabBar, BorderLayout.CENTER);

        // CARD
        JPanel jp_card = new JPanel(new BorderLayout());
        jp_card.setBackground(clr_white);
        jp_card.setBorder(new LineBorder(clr_cardBorder, 1));

        JPanel jp_cardInner = new JPanel(new BorderLayout());
        jp_cardInner.setBackground(clr_white);
        jp_cardInner.setBorder(new EmptyBorder(18, 18, 18, 18));

        Font base = new Font("Segoe UI", Font.PLAIN, 13);
        Color lc = new Color(60, 60, 60);

        // JOB OPPORTUNITY FORM
        jt_Job_title    = new JTextField();
        jt_Job_title.setFont(base);
        jt_Job_location = new JTextField();
        jt_Job_location.setFont(base);
        jt_Job_salary   = new JTextField();
        jt_Job_salary.setFont(base);
        jt_Job_hours    = new JTextField();
        jt_Job_hours.setFont(base);

        jta_Job_desc = new JTextArea();
        jta_Job_desc.setFont(base);
        jta_Job_desc.setLineWrap(true);
        jta_Job_desc.setWrapStyleWord(true);
        jta_Job_desc.setBorder(new EmptyBorder(4, 4, 4, 4));

        JScrollPane sp_jobDesc = new JScrollPane(jta_Job_desc);
        sp_jobDesc.setBorder(new LineBorder(clr_cardBorder, 1));
        sp_jobDesc.getViewport().setBackground(clr_white);

        rbJob_fullTime = new JRadioButton("Full-time");
        rbJob_fullTime.setFont(base);
        rbJob_fullTime.setBackground(clr_white);
        rbJob_fullTime.setSelected(true);
        rbJob_partTime = new JRadioButton("Part-time");
        rbJob_partTime.setFont(base);
        rbJob_partTime.setBackground(clr_white);
        bgJob_jobType  = new ButtonGroup();
        bgJob_jobType.add(rbJob_fullTime);
        bgJob_jobType.add(rbJob_partTime);

        JPanel jp_jobTypeRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        jp_jobTypeRadio.setBackground(clr_white);
        jp_jobTypeRadio.add(rbJob_fullTime);
        jp_jobTypeRadio.add(rbJob_partTime);

        rbJob_onsite = new JRadioButton("On-site");
        rbJob_onsite.setFont(base);
        rbJob_onsite.setBackground(clr_white);
        rbJob_onsite.setSelected(true);
        rbJob_hybrid = new JRadioButton("Hybrid");
        rbJob_hybrid.setFont(base);
        rbJob_hybrid.setBackground(clr_white);
        rbJob_remote = new JRadioButton("Remote");
        rbJob_remote.setFont(base);
        rbJob_remote.setBackground(clr_white);
        bgJob_mode   = new ButtonGroup();
        bgJob_mode.add(rbJob_onsite);
        bgJob_mode.add(rbJob_hybrid);
        bgJob_mode.add(rbJob_remote);

        JPanel jp_jobModeRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        jp_jobModeRadio.setBackground(clr_white);
        jp_jobModeRadio.add(rbJob_onsite);
        jp_jobModeRadio.add(rbJob_hybrid);
        jp_jobModeRadio.add(rbJob_remote);

        jt_Job_deadline = new JTextField();
        jt_Job_deadline.setFont(base);

        jcb_Job_confirm = new JCheckBox("I confirm the posting details are correct.");
        jcb_Job_confirm.setFont(base);
        jcb_Job_confirm.setBackground(clr_white);

        JPanel jp_jobRows = new JPanel(new GridLayout(9, 1, 0, 8));
        jp_jobRows.setBackground(clr_white);

        JPanel jp_jRow_title = new JPanel(new BorderLayout());
        jp_jRow_title.setBackground(clr_white);
        JPanel jp_jLbl_title = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_jLbl_title.setBackground(clr_white);
        jp_jLbl_title.setPreferredSize(new Dimension(220, 36));
        JLabel jl_jLbl_title = new JLabel("<html>Title <font color='red'>*</font></html>");
        jl_jLbl_title.setFont(base);
        jl_jLbl_title.setForeground(lc);
        jp_jLbl_title.add(jl_jLbl_title);
        JPanel jp_jFld_title = new JPanel(new GridLayout(1, 1));
        jp_jFld_title.setBackground(clr_white);
        jp_jFld_title.add(jt_Job_title);
        jp_jRow_title.add(jp_jLbl_title, BorderLayout.WEST);
        jp_jRow_title.add(jp_jFld_title, BorderLayout.CENTER);

        JPanel jp_jRow_description = new JPanel(new BorderLayout());
        jp_jRow_description.setBackground(clr_white);
        JPanel jp_jLbl_description = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_jLbl_description.setBackground(clr_white);
        jp_jLbl_description.setPreferredSize(new Dimension(220, 36));
        JLabel jl_jLbl_description = new JLabel("<html>Description <font color='red'>*</font></html>");
        jl_jLbl_description.setFont(base);
        jl_jLbl_description.setForeground(lc);
        jp_jLbl_description.add(jl_jLbl_description);
        JPanel jp_jFld_description = new JPanel(new GridLayout(1, 1));
        jp_jFld_description.setBackground(clr_white);
        jp_jFld_description.add(sp_jobDesc);
        jp_jRow_description.add(jp_jLbl_description, BorderLayout.WEST);
        jp_jRow_description.add(jp_jFld_description, BorderLayout.CENTER);

        JPanel jp_jRow_jobType = new JPanel(new BorderLayout());
        jp_jRow_jobType.setBackground(clr_white);
        JPanel jp_jLbl_jobType = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_jLbl_jobType.setBackground(clr_white);
        jp_jLbl_jobType.setPreferredSize(new Dimension(220, 36));
        JLabel jl_jLbl_jobType = new JLabel("<html>Job Type <font color='red'>*</font></html>");
        jl_jLbl_jobType.setFont(base);
        jl_jLbl_jobType.setForeground(lc);
        jp_jLbl_jobType.add(jl_jLbl_jobType);
        JPanel jp_jFld_jobType = new JPanel(new GridLayout(1, 1));
        jp_jFld_jobType.setBackground(clr_white);
        jp_jFld_jobType.add(jp_jobTypeRadio);
        jp_jRow_jobType.add(jp_jLbl_jobType, BorderLayout.WEST);
        jp_jRow_jobType.add(jp_jFld_jobType, BorderLayout.CENTER);

        JPanel jp_jRow_location = new JPanel(new BorderLayout());
        jp_jRow_location.setBackground(clr_white);
        JPanel jp_jLbl_location = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_jLbl_location.setBackground(clr_white);
        jp_jLbl_location.setPreferredSize(new Dimension(220, 36));
        JLabel jl_jLbl_location = new JLabel("<html>Location <font color='red'>*</font></html>");
        jl_jLbl_location.setFont(base);
        jl_jLbl_location.setForeground(lc);
        jp_jLbl_location.add(jl_jLbl_location);
        JPanel jp_jFld_location = new JPanel(new GridLayout(1, 1));
        jp_jFld_location.setBackground(clr_white);
        jp_jFld_location.add(jt_Job_location);
        jp_jRow_location.add(jp_jLbl_location, BorderLayout.WEST);
        jp_jRow_location.add(jp_jFld_location, BorderLayout.CENTER);

        JPanel jp_jRow_workMode = new JPanel(new BorderLayout());
        jp_jRow_workMode.setBackground(clr_white);
        JPanel jp_jLbl_workMode = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_jLbl_workMode.setBackground(clr_white);
        jp_jLbl_workMode.setPreferredSize(new Dimension(220, 36));
        JLabel jl_jLbl_workMode = new JLabel("Work Mode");
        jl_jLbl_workMode.setFont(base);
        jl_jLbl_workMode.setForeground(lc);
        jp_jLbl_workMode.add(jl_jLbl_workMode);
        JPanel jp_jFld_workMode = new JPanel(new GridLayout(1, 1));
        jp_jFld_workMode.setBackground(clr_white);
        jp_jFld_workMode.add(jp_jobModeRadio);
        jp_jRow_workMode.add(jp_jLbl_workMode, BorderLayout.WEST);
        jp_jRow_workMode.add(jp_jFld_workMode, BorderLayout.CENTER);

        JPanel jp_jRow_salary = new JPanel(new BorderLayout());
        jp_jRow_salary.setBackground(clr_white);
        JPanel jp_jLbl_salary = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_jLbl_salary.setBackground(clr_white);
        jp_jLbl_salary.setPreferredSize(new Dimension(220, 36));
        JLabel jl_jLbl_salary = new JLabel("<html>Salary <font color='red'>*</font></html>");
        jl_jLbl_salary.setFont(base);
        jl_jLbl_salary.setForeground(lc);
        jp_jLbl_salary.add(jl_jLbl_salary);
        JPanel jp_jFld_salary = new JPanel(new GridLayout(1, 1));
        jp_jFld_salary.setBackground(clr_white);
        jp_jFld_salary.add(jt_Job_salary);
        jp_jRow_salary.add(jp_jLbl_salary, BorderLayout.WEST);
        jp_jRow_salary.add(jp_jFld_salary, BorderLayout.CENTER);

        JPanel jp_jRow_workingHours = new JPanel(new BorderLayout());
        jp_jRow_workingHours.setBackground(clr_white);
        JPanel jp_jLbl_workingHours = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_jLbl_workingHours.setBackground(clr_white);
        jp_jLbl_workingHours.setPreferredSize(new Dimension(220, 36));
        JLabel jl_jLbl_workingHours = new JLabel("Working Hours");
        jl_jLbl_workingHours.setFont(base);
        jl_jLbl_workingHours.setForeground(lc);
        jp_jLbl_workingHours.add(jl_jLbl_workingHours);
        JPanel jp_jFld_workingHours = new JPanel(new GridLayout(1, 1));
        jp_jFld_workingHours.setBackground(clr_white);
        jp_jFld_workingHours.add(jt_Job_hours);
        jp_jRow_workingHours.add(jp_jLbl_workingHours, BorderLayout.WEST);
        jp_jRow_workingHours.add(jp_jFld_workingHours, BorderLayout.CENTER);

        JPanel jp_jRow_deadline = new JPanel(new BorderLayout());
        jp_jRow_deadline.setBackground(clr_white);
        JPanel jp_jLbl_deadline = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_jLbl_deadline.setBackground(clr_white);
        jp_jLbl_deadline.setPreferredSize(new Dimension(220, 36));
        JLabel jl_jLbl_deadline = new JLabel("<html>Application Deadline <font color='red'>*</font></html>");
        jl_jLbl_deadline.setFont(base);
        jl_jLbl_deadline.setForeground(lc);
        jp_jLbl_deadline.add(jl_jLbl_deadline);
        JPanel jp_jFld_deadline = new JPanel(new GridLayout(1, 1));
        jp_jFld_deadline.setBackground(clr_white);
        jp_jFld_deadline.add(jt_Job_deadline);
        jp_jRow_deadline.add(jp_jLbl_deadline, BorderLayout.WEST);
        jp_jRow_deadline.add(jp_jFld_deadline, BorderLayout.CENTER);

        JPanel jp_jRow_confirm = new JPanel(new BorderLayout());
        jp_jRow_confirm.setBackground(clr_white);
        jp_jRow_confirm.add(jcb_Job_confirm, BorderLayout.WEST);

        jp_jobRows.add(jp_jRow_title);
        jp_jobRows.add(jp_jRow_description);
        jp_jobRows.add(jp_jRow_jobType);
        jp_jobRows.add(jp_jRow_location);
        jp_jobRows.add(jp_jRow_workMode);
        jp_jobRows.add(jp_jRow_salary);
        jp_jobRows.add(jp_jRow_workingHours);
        jp_jobRows.add(jp_jRow_deadline);
        jp_jobRows.add(jp_jRow_confirm);

        jp_jobForm = new JPanel(new BorderLayout());
        jp_jobForm.setBackground(clr_white);
        jp_jobForm.add(jp_jobRows, BorderLayout.NORTH);

        // PLACEMENT FORM
        jt_Pl_title    = new JTextField();
        jt_Pl_title.setFont(base);
        jt_Pl_location = new JTextField();
        jt_Pl_location.setFont(base);
        jt_Pl_stipend  = new JTextField();
        jt_Pl_stipend.setFont(base);
        jt_Pl_duration = new JTextField();
        jt_Pl_duration.setFont(base);

        jta_Pl_desc = new JTextArea();
        jta_Pl_desc.setFont(base);
        jta_Pl_desc.setLineWrap(true);
        jta_Pl_desc.setWrapStyleWord(true);
        jta_Pl_desc.setBorder(new EmptyBorder(4, 4, 4, 4));

        JScrollPane sp_plDesc = new JScrollPane(jta_Pl_desc);
        sp_plDesc.setBorder(new LineBorder(clr_cardBorder, 1));
        sp_plDesc.getViewport().setBackground(clr_white);

        rbPl_onsite = new JRadioButton("On-site");
        rbPl_onsite.setFont(base);
        rbPl_onsite.setBackground(clr_white);
        rbPl_onsite.setSelected(true);
        rbPl_hybrid = new JRadioButton("Hybrid");
        rbPl_hybrid.setFont(base);
        rbPl_hybrid.setBackground(clr_white);
        rbPl_remote = new JRadioButton("Remote");
        rbPl_remote.setFont(base);
        rbPl_remote.setBackground(clr_white);
        bgPl_mode   = new ButtonGroup();
        bgPl_mode.add(rbPl_onsite);
        bgPl_mode.add(rbPl_hybrid);
        bgPl_mode.add(rbPl_remote);

        JPanel jp_plModeRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        jp_plModeRadio.setBackground(clr_white);
        jp_plModeRadio.add(rbPl_onsite);
        jp_plModeRadio.add(rbPl_hybrid);
        jp_plModeRadio.add(rbPl_remote);

        jt_Pl_deadline = new JTextField();
        jt_Pl_deadline.setFont(base);

        jcb_Pl_confirm = new JCheckBox("I confirm the posting details are correct.");
        jcb_Pl_confirm.setFont(base);
        jcb_Pl_confirm.setBackground(clr_white);

        JPanel jp_plRows = new JPanel(new GridLayout(8, 1, 0, 8));
        jp_plRows.setBackground(clr_white);

        JPanel jp_pRow_title = new JPanel(new BorderLayout());
        jp_pRow_title.setBackground(clr_white);
        JPanel jp_pLbl_title = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_pLbl_title.setBackground(clr_white);
        jp_pLbl_title.setPreferredSize(new Dimension(220, 36));
        JLabel jl_pLbl_title = new JLabel("<html>Title <font color='red'>*</font></html>");
        jl_pLbl_title.setFont(base);
        jl_pLbl_title.setForeground(lc);
        jp_pLbl_title.add(jl_pLbl_title);
        JPanel jp_pFld_title = new JPanel(new GridLayout(1, 1));
        jp_pFld_title.setBackground(clr_white);
        jp_pFld_title.add(jt_Pl_title);
        jp_pRow_title.add(jp_pLbl_title, BorderLayout.WEST);
        jp_pRow_title.add(jp_pFld_title, BorderLayout.CENTER);

        JPanel jp_pRow_description = new JPanel(new BorderLayout());
        jp_pRow_description.setBackground(clr_white);
        JPanel jp_pLbl_description = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_pLbl_description.setBackground(clr_white);
        jp_pLbl_description.setPreferredSize(new Dimension(220, 36));
        JLabel jl_pLbl_description = new JLabel("<html>Description <font color='red'>*</font></html>");
        jl_pLbl_description.setFont(base);
        jl_pLbl_description.setForeground(lc);
        jp_pLbl_description.add(jl_pLbl_description);
        JPanel jp_pFld_description = new JPanel(new GridLayout(1, 1));
        jp_pFld_description.setBackground(clr_white);
        jp_pFld_description.add(sp_plDesc);
        jp_pRow_description.add(jp_pLbl_description, BorderLayout.WEST);
        jp_pRow_description.add(jp_pFld_description, BorderLayout.CENTER);

        JPanel jp_pRow_location = new JPanel(new BorderLayout());
        jp_pRow_location.setBackground(clr_white);
        JPanel jp_pLbl_location = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_pLbl_location.setBackground(clr_white);
        jp_pLbl_location.setPreferredSize(new Dimension(220, 36));
        JLabel jl_pLbl_location = new JLabel("<html>Location <font color='red'>*</font></html>");
        jl_pLbl_location.setFont(base);
        jl_pLbl_location.setForeground(lc);
        jp_pLbl_location.add(jl_pLbl_location);
        JPanel jp_pFld_location = new JPanel(new GridLayout(1, 1));
        jp_pFld_location.setBackground(clr_white);
        jp_pFld_location.add(jt_Pl_location);
        jp_pRow_location.add(jp_pLbl_location, BorderLayout.WEST);
        jp_pRow_location.add(jp_pFld_location, BorderLayout.CENTER);

        JPanel jp_pRow_workMode = new JPanel(new BorderLayout());
        jp_pRow_workMode.setBackground(clr_white);
        JPanel jp_pLbl_workMode = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_pLbl_workMode.setBackground(clr_white);
        jp_pLbl_workMode.setPreferredSize(new Dimension(220, 36));
        JLabel jl_pLbl_workMode = new JLabel("Work Mode");
        jl_pLbl_workMode.setFont(base);
        jl_pLbl_workMode.setForeground(lc);
        jp_pLbl_workMode.add(jl_pLbl_workMode);
        JPanel jp_pFld_workMode = new JPanel(new GridLayout(1, 1));
        jp_pFld_workMode.setBackground(clr_white);
        jp_pFld_workMode.add(jp_plModeRadio);
        jp_pRow_workMode.add(jp_pLbl_workMode, BorderLayout.WEST);
        jp_pRow_workMode.add(jp_pFld_workMode, BorderLayout.CENTER);

        JPanel jp_pRow_stipend = new JPanel(new BorderLayout());
        jp_pRow_stipend.setBackground(clr_white);
        JPanel jp_pLbl_stipend = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_pLbl_stipend.setBackground(clr_white);
        jp_pLbl_stipend.setPreferredSize(new Dimension(220, 36));
        JLabel jl_pLbl_stipend = new JLabel("<html>Stipend <font color='red'>*</font></html>");
        jl_pLbl_stipend.setFont(base);
        jl_pLbl_stipend.setForeground(lc);
        jp_pLbl_stipend.add(jl_pLbl_stipend);
        JPanel jp_pFld_stipend = new JPanel(new GridLayout(1, 1));
        jp_pFld_stipend.setBackground(clr_white);
        jp_pFld_stipend.add(jt_Pl_stipend);
        jp_pRow_stipend.add(jp_pLbl_stipend, BorderLayout.WEST);
        jp_pRow_stipend.add(jp_pFld_stipend, BorderLayout.CENTER);

        JPanel jp_pRow_duration = new JPanel(new BorderLayout());
        jp_pRow_duration.setBackground(clr_white);
        JPanel jp_pLbl_duration = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_pLbl_duration.setBackground(clr_white);
        jp_pLbl_duration.setPreferredSize(new Dimension(220, 36));
        JLabel jl_pLbl_duration = new JLabel("<html>Duration <font color='red'>*</font></html>");
        jl_pLbl_duration.setFont(base);
        jl_pLbl_duration.setForeground(lc);
        jp_pLbl_duration.add(jl_pLbl_duration);
        JPanel jp_pFld_duration = new JPanel(new GridLayout(1, 1));
        jp_pFld_duration.setBackground(clr_white);
        jp_pFld_duration.add(jt_Pl_duration);
        jp_pRow_duration.add(jp_pLbl_duration, BorderLayout.WEST);
        jp_pRow_duration.add(jp_pFld_duration, BorderLayout.CENTER);

        JPanel jp_pRow_deadline = new JPanel(new BorderLayout());
        jp_pRow_deadline.setBackground(clr_white);
        JPanel jp_pLbl_deadline = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_pLbl_deadline.setBackground(clr_white);
        jp_pLbl_deadline.setPreferredSize(new Dimension(220, 36));
        JLabel jl_pLbl_deadline = new JLabel("<html>Application Deadline <font color='red'>*</font></html>");
        jl_pLbl_deadline.setFont(base);
        jl_pLbl_deadline.setForeground(lc);
        jp_pLbl_deadline.add(jl_pLbl_deadline);
        JPanel jp_pFld_deadline = new JPanel(new GridLayout(1, 1));
        jp_pFld_deadline.setBackground(clr_white);
        jp_pFld_deadline.add(jt_Pl_deadline);
        jp_pRow_deadline.add(jp_pLbl_deadline, BorderLayout.WEST);
        jp_pRow_deadline.add(jp_pFld_deadline, BorderLayout.CENTER);

        JPanel jp_pRow_confirm = new JPanel(new BorderLayout());
        jp_pRow_confirm.setBackground(clr_white);
        jp_pRow_confirm.add(jcb_Pl_confirm, BorderLayout.WEST);

        jp_plRows.add(jp_pRow_title);
        jp_plRows.add(jp_pRow_description);
        jp_plRows.add(jp_pRow_location);
        jp_plRows.add(jp_pRow_workMode);
        jp_plRows.add(jp_pRow_stipend);
        jp_plRows.add(jp_pRow_duration);
        jp_plRows.add(jp_pRow_deadline);
        jp_plRows.add(jp_pRow_confirm);

        jp_placementForm = new JPanel(new BorderLayout());
        jp_placementForm.setBackground(clr_white);
        jp_placementForm.add(jp_plRows, BorderLayout.NORTH);

        // FORM HOLDER
        jp_formHolder = new JPanel(new BorderLayout());
        jp_formHolder.setBackground(clr_white);
        jp_formHolder.add(jp_jobForm, BorderLayout.CENTER);

        jp_cardInner.add(jp_formHolder, BorderLayout.CENTER);
        jp_card.add(jp_cardInner, BorderLayout.CENTER);

        jp_content.add(jp_topBar, BorderLayout.NORTH);
        jp_content.add(jp_card, BorderLayout.CENTER);

        JScrollPane sp = new JScrollPane(jp_content);
        sp.setBorder(null);
        sp.getViewport().setBackground(clr_bg);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        jp_wrapper.add(sp, BorderLayout.CENTER);

        // Create Posting button
        JButton jb_create = new JButton("Create Posting");
        jb_create.setFocusPainted(false);
        jb_create.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jb_create.setForeground(Color.WHITE);
        jb_create.setBackground(clr_blue);
        jb_create.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jb_create.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        jb_create.setOpaque(true);
        jb_create.setContentAreaFilled(true);
        jb_create.setBorderPainted(false);

        JPanel jp_bottom = new JPanel();
        jp_bottom.setBackground(clr_bottomBackground);
        jp_bottom.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        jp_bottom.add(jb_create);
        jp_wrapper.add(jp_bottom, BorderLayout.SOUTH);

        add(jp_wrapper, BorderLayout.CENTER);

        // LISTENERS
        jb_back.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                jb_back.setBackground(new Color(230, 235, 245));
            }
            public void mouseExited(MouseEvent e) {
                jb_back.setBackground(clr_white);
            }
        });
        jb_back.addActionListener(new BackHandler());

        jb_create.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                jb_create.setBackground(clr_blue_hover);
            }
            public void mouseExited(MouseEvent e) {
                jb_create.setBackground(clr_blue);
            }
        });
        jb_create.addActionListener(new CreateHandler());

        jb_tabJob.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jb_tabJob.setBackground(clr_blue);
                jb_tabJob.setForeground(clr_white);
                jb_tabJob.setBorder(new LineBorder(clr_blue, 2));
                jb_tabPlacement.setBackground(clr_white);
                jb_tabPlacement.setForeground(clr_textDark);
                jb_tabPlacement.setBorder(new LineBorder(clr_cardBorder, 2));
                jp_formHolder.removeAll();
                jp_formHolder.add(jp_jobForm, BorderLayout.CENTER);
                jp_formHolder.revalidate();
                jp_formHolder.repaint();
            }
        });

        jb_tabPlacement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jb_tabPlacement.setBackground(clr_blue);
                jb_tabPlacement.setForeground(clr_white);
                jb_tabPlacement.setBorder(new LineBorder(clr_blue, 2));
                jb_tabJob.setBackground(clr_white);
                jb_tabJob.setForeground(clr_textDark);
                jb_tabJob.setBorder(new LineBorder(clr_cardBorder, 2));
                jp_formHolder.removeAll();
                jp_formHolder.add(jp_placementForm, BorderLayout.CENTER);
                jp_formHolder.revalidate();
                jp_formHolder.repaint();
            }
        });
    }

    // BACK LISTENER
    private class BackHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dashboard.refreshStats();
            dashboard.setVisible(true);
            dispose();
        }
    }

    // CREATE LISTENER
    private class CreateHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            boolean isJob = jp_formHolder.isAncestorOf(jp_jobForm);

            if (isJob) {
                String title    = jt_Job_title.getText().trim();
                String desc     = jta_Job_desc.getText().trim();
                String location = jt_Job_location.getText().trim();
                String salary   = jt_Job_salary.getText().trim();
                String jobDeadlineText = jt_Job_deadline.getText().trim();

                if (title.isEmpty() || desc.isEmpty() || location.isEmpty() || salary.isEmpty() || jobDeadlineText.isEmpty()) {
                    JOptionPane.showMessageDialog(CreateOpportunityFrame.this, "Please fill all required (*) fields.", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                java.sql.Date jobDeadlineDate;
                try {
                    jobDeadlineDate = java.sql.Date.valueOf(jobDeadlineText.replace("/", "-"));
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(CreateOpportunityFrame.this, "Invalid date format. Please use yyyy/MM/dd.", "Validation", JOptionPane.WARNING_MESSAGE);
                    jt_Job_deadline.requestFocus();
                    return;
                }
                java.sql.Date todayJob = new java.sql.Date(System.currentTimeMillis());
                if (jobDeadlineDate.before(todayJob)) {
                    JOptionPane.showMessageDialog(CreateOpportunityFrame.this, "Deadline cannot be in the past.", "Validation", JOptionPane.WARNING_MESSAGE);
                    jt_Job_deadline.requestFocus();
                    return;
                }
                if (!jcb_Job_confirm.isSelected()) {
                    JOptionPane.showMessageDialog(CreateOpportunityFrame.this, "Please confirm the posting checkbox.", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String jobType = rbJob_fullTime.isSelected() ? "Full-time" : "Part-time";
                String mode    = rbJob_onsite.isSelected() ? "On-site" : (rbJob_hybrid.isSelected() ? "Hybrid" : "Remote");
                String hours   = jt_Job_hours.getText().trim();

                try (Connection con = DBConnection.getConnection()) {
                    Integer companyDbId = null;
                    try (PreparedStatement ps = con.prepareStatement("SELECT companyId FROM company WHERE companyId = ?")) {
                        ps.setInt(1, Integer.parseInt(companyId));
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                companyDbId = rs.getInt("companyId");
                            }
                        }
                    }
                    if (companyDbId == null) {
                        JOptionPane.showMessageDialog(CreateOpportunityFrame.this, "Company not found in DB for ID: " + companyId, "Database", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int postingId;
                    String sqlPosting = "INSERT INTO recruitmentPosting (title, description, applicationDeadline, availabilityStatus, isActive, location, workMode, companyId) VALUES (?, ?, ?, 1, 1, ?, ?, ?)";
                    try (PreparedStatement ps = con.prepareStatement(sqlPosting, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                        ps.setString(1, title);
                        ps.setString(2, desc);
                        ps.setDate(3, jobDeadlineDate);
                        ps.setString(4, location);
                        ps.setString(5, mode);
                        ps.setInt(6, companyDbId);
                        ps.executeUpdate();
                        try (ResultSet keys = ps.getGeneratedKeys()) {
                            if (!keys.next()) {
                                throw new Exception("Failed to get postingId.");
                            }
                            postingId = keys.getInt(1);
                        }
                    }
                    String sqlJob = "INSERT INTO jobOpportunity (postingId, jobType, salary, workingHours) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement ps = con.prepareStatement(sqlJob)) {
                        ps.setInt(1, postingId);
                        ps.setString(2, jobType.equals("Full-time") ? "Full-Time" : "Part-Time");
                        try {
                            ps.setDouble(3, Double.parseDouble(salary));
                        } catch (NumberFormatException nfe) {
                            ps.setNull(3, java.sql.Types.DOUBLE);
                        }
                        if (hours.isEmpty()) {
                            ps.setNull(4, java.sql.Types.VARCHAR);
                        } else {
                            ps.setString(4, hours);
                        }
                        ps.executeUpdate();
                    }
                    JOptionPane.showMessageDialog(CreateOpportunityFrame.this, "Job posting created!\nPosting ID: " + postingId, "Success", JOptionPane.INFORMATION_MESSAGE);
                    dashboard.refreshStats();
                    jt_Job_title.setText("");
                    jta_Job_desc.setText("");
                    jt_Job_location.setText("");
                    jt_Job_salary.setText("");
                    jt_Job_hours.setText("");
                    rbJob_fullTime.setSelected(true);
                    rbJob_onsite.setSelected(true);
                    jt_Job_deadline.setText("");
                    jcb_Job_confirm.setSelected(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(CreateOpportunityFrame.this, "Database Error:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                String title    = jt_Pl_title.getText().trim();
                String desc     = jta_Pl_desc.getText().trim();
                String location = jt_Pl_location.getText().trim();
                String stipend  = jt_Pl_stipend.getText().trim();
                String duration = jt_Pl_duration.getText().trim();
                String plDeadlineText = jt_Pl_deadline.getText().trim();

                if (title.isEmpty() || desc.isEmpty() || location.isEmpty() || stipend.isEmpty() || duration.isEmpty() || plDeadlineText.isEmpty()) {
                    JOptionPane.showMessageDialog(CreateOpportunityFrame.this, "Please fill all required (*) fields.", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                java.sql.Date plDeadlineDate;
                try {
                    plDeadlineDate = java.sql.Date.valueOf(plDeadlineText.replace("/", "-"));
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(CreateOpportunityFrame.this, "Invalid date format. Please use yyyy/MM/dd.", "Validation", JOptionPane.WARNING_MESSAGE);
                    jt_Pl_deadline.requestFocus();
                    return;
                }
                java.sql.Date todayPl = new java.sql.Date(System.currentTimeMillis());
                if (plDeadlineDate.before(todayPl)) {
                    JOptionPane.showMessageDialog(CreateOpportunityFrame.this, "Deadline cannot be in the past.", "Validation", JOptionPane.WARNING_MESSAGE);
                    jt_Pl_deadline.requestFocus();
                    return;
                }
                if (!jcb_Pl_confirm.isSelected()) {
                    JOptionPane.showMessageDialog(CreateOpportunityFrame.this, "Please confirm the posting checkbox.", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String mode = rbPl_onsite.isSelected() ? "On-site" : (rbPl_hybrid.isSelected() ? "Hybrid" : "Remote");

                try (Connection con = DBConnection.getConnection()) {
                    Integer companyDbId = null;
                    try (PreparedStatement ps = con.prepareStatement("SELECT companyId FROM company WHERE companyId = ?")) {
                        ps.setInt(1, Integer.parseInt(companyId));
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                companyDbId = rs.getInt("companyId");
                            }
                        }
                    }
                    if (companyDbId == null) {
                        JOptionPane.showMessageDialog(CreateOpportunityFrame.this, "Company not found in DB for ID: " + companyId, "Database", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int postingId;
                    String sqlPosting = "INSERT INTO recruitmentPosting (title, description, applicationDeadline, availabilityStatus, isActive, location, workMode, companyId) VALUES (?, ?, ?, 1, 1, ?, ?, ?)";
                    try (PreparedStatement ps = con.prepareStatement(sqlPosting, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                        ps.setString(1, title);
                        ps.setString(2, desc);
                        ps.setDate(3, plDeadlineDate);
                        ps.setString(4, location);
                        ps.setString(5, mode);
                        ps.setInt(6, companyDbId);
                        ps.executeUpdate();
                        try (ResultSet keys = ps.getGeneratedKeys()) {
                            if (!keys.next()) {
                                throw new Exception("Failed to get postingId.");
                            }
                            postingId = keys.getInt(1);
                        }
                    }
                    String sqlPl = "INSERT INTO placementOpportunity (postingId, placementDuration, stipend) VALUES (?, ?, ?)";
                    try (PreparedStatement ps = con.prepareStatement(sqlPl)) {
                        ps.setInt(1, postingId);
                        ps.setString(2, duration);
                        try {
                            ps.setDouble(3, Double.parseDouble(stipend));
                        } catch (NumberFormatException nfe) {
                            ps.setNull(3, java.sql.Types.DOUBLE);
                        }
                        ps.executeUpdate();
                    }
                    JOptionPane.showMessageDialog(CreateOpportunityFrame.this, "Placement posting created!\nPosting ID: " + postingId, "Success", JOptionPane.INFORMATION_MESSAGE);
                    dashboard.refreshStats();
                    jt_Pl_title.setText("");
                    jta_Pl_desc.setText("");
                    jt_Pl_location.setText("");
                    jt_Pl_stipend.setText("");
                    jt_Pl_duration.setText("");
                    rbPl_onsite.setSelected(true);
                    jt_Pl_deadline.setText("");
                    jcb_Pl_confirm.setSelected(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(CreateOpportunityFrame.this, "Database Error:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}