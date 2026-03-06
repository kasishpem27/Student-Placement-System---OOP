package student;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

import dbconnection.DBConnection;

public class ApplyJobFrame extends JFrame {

    private final Color clr_blue        = new Color(0, 102, 153);
    private final Color clr_white       = Color.WHITE;
    private final Color clr_bg          = new Color(245, 248, 252);
    private final Color clr_cardBorder  = new Color(220, 230, 240);
    private final Color clr_textDark    = new Color(40, 40, 40);
    private final Color clr_textMuted   = new Color(90, 90, 90);
    private final Color clr_acceptGreen = new Color(34, 139, 34);

    private  StudentDashboardFrame dashboard;
    private JobPostingsFrame      jobPostingsFrame;
    private int                   studentId;
    private int                   postingId;
    private String                company;
    private String                role;
    private String studentUserName;

    private JButton jb_logout;
    private JButton jb_back;
    private JButton jb_submit;

    private JLabel jl_cvFile;
    private JLabel jl_coverLetterFile;
    private JLabel jl_transcriptFile;
    private JLabel jl_additionalFile;

    private File   file_cv;
    private File   file_coverLetter;
    private File   file_transcript;
    private File[] files_additional;

    public ApplyJobFrame(StudentDashboardFrame dashboard, JobPostingsFrame jobPostingsFrame,
                         int studentId, int postingId, String company, String role) {
        super("Apply — " + company + " · " + role);
        this.jobPostingsFrame = jobPostingsFrame;
        this.studentId        = studentId;
        this.postingId        = postingId;
        this.company          = company;
        this.role             = role;
        this.dashboard = dashboard;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);
        getContentPane().setBackground(clr_bg);

        // HEADER
        JPanel jp_header = new JPanel(new BorderLayout());
        jp_header.setBackground(clr_blue);
        jp_header.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel jp_headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        jp_headerLeft.setBackground(clr_blue);
        JLabel jl_headerTitle = new JLabel("Apply for Position");
        jl_headerTitle.setForeground(clr_white);
        jl_headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jp_headerLeft.add(jl_headerTitle);

        JPanel jp_headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        jp_headerRight.setBackground(clr_blue);
        JLabel jl_userInfo = new JLabel(studentUserName + "  ·  " + studentId);
        jl_userInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_userInfo.setForeground(new Color(190, 220, 240));

        jb_logout = new JButton("Logout");
        jb_logout.setFocusPainted(false);
        jb_logout.setBackground(new Color(0, 140, 205));
        jb_logout.setForeground(clr_white);
        jb_logout.setBorderPainted(false);
        jb_logout.setOpaque(true);
        jb_logout.setPreferredSize(new Dimension(110, 34));
        jb_logout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jb_logout.setFont(new Font("Segoe UI", Font.BOLD, 13));

        jp_headerRight.add(jl_userInfo);
        jp_headerRight.add(jb_logout);
        jp_header.add(jp_headerLeft, BorderLayout.WEST);
        jp_header.add(jp_headerRight, BorderLayout.EAST);
        add(jp_header, BorderLayout.NORTH);

        // CONTENT
        JPanel jp_content = new JPanel(new BorderLayout());
        jp_content.setBackground(clr_bg);
        jp_content.setBorder(new EmptyBorder(16, 18, 18, 18));

        // TOP BAR (back button + page title)
        JPanel jp_topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_topBar.setBackground(clr_bg);
        jp_topBar.setBorder(new EmptyBorder(0, 0, 8, 0));

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

        JPanel jp_titlePanel = new JPanel(new BorderLayout());
        jp_titlePanel.setBackground(clr_bg);
        JLabel jl_pageTitle = new JLabel("Apply to " + company + " — " + role);
        jl_pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jl_pageTitle.setForeground(clr_textDark);
        jl_pageTitle.setBorder(new EmptyBorder(4, 0, 10, 0));
        jp_titlePanel.add(jp_topBar,    BorderLayout.NORTH);
        jp_titlePanel.add(jl_pageTitle, BorderLayout.CENTER);

        // CARD
        JPanel jp_card = new JPanel(new BorderLayout());
        jp_card.setBackground(clr_white);
        jp_card.setBorder(new CompoundBorder(
                new LineBorder(clr_cardBorder, 1),
                new EmptyBorder(22, 26, 22, 26)));

        // ELIGIBILITY SECTION
        JPanel jp_eligibility = new JPanel(new BorderLayout());
        jp_eligibility.setBackground(clr_white);
        jp_eligibility.setBorder(new EmptyBorder(0, 0, 18, 0));

        JLabel jl_secEligibility = new JLabel("Eligibility & Job Description");
        jl_secEligibility.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jl_secEligibility.setForeground(clr_blue);
        jl_secEligibility.setBorder(new EmptyBorder(0, 0, 7, 0));

        JTextArea jta_desc = new JTextArea(buildEligibilityText());                          //goes to the function buildtextarea - everything constructed from db
        jta_desc.setEditable(false);
        jta_desc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jta_desc.setBackground(new Color(248, 250, 255));
        jta_desc.setBorder(new EmptyBorder(10, 10, 10, 10));
        jta_desc.setRows(9);
        jta_desc.setLineWrap(true);
        jta_desc.setWrapStyleWord(true);

        JScrollPane jp_descScroll = new JScrollPane(jta_desc);
        jp_descScroll.setBorder(new LineBorder(clr_cardBorder, 1));

        jp_eligibility.add(jl_secEligibility, BorderLayout.NORTH);
        jp_eligibility.add(jp_descScroll,     BorderLayout.CENTER);

        // UPLOADS SECTION
        JPanel jp_uploadsSection = new JPanel(new BorderLayout());
        jp_uploadsSection.setBackground(clr_white);

        JLabel jl_secDocs = new JLabel("Upload Documents");
        jl_secDocs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jl_secDocs.setForeground(clr_blue);
        jl_secDocs.setBorder(new EmptyBorder(0, 0, 7, 0));

        jl_cvFile          = new JLabel("No file selected");
        jl_coverLetterFile = new JLabel("No file selected");
        jl_transcriptFile  = new JLabel("No file selected");
        jl_additionalFile  = new JLabel("No file selected");
        styleFileLabel(jl_cvFile);
        styleFileLabel(jl_coverLetterFile);
        styleFileLabel(jl_transcriptFile);
        styleFileLabel(jl_additionalFile);

        JButton jb_uploadCv          = makeUploadButton();
        JButton jb_uploadCoverLetter = makeUploadButton();
        JButton jb_uploadTranscript  = makeUploadButton();
        JButton jb_uploadAdditional  = makeUploadButton();

        JPanel jp_uploadGrid = new JPanel(new GridLayout(4, 1, 0, 4));
        jp_uploadGrid.setBackground(clr_white);
        jp_uploadGrid.add(buildUploadRow("CV / Resume *",   jb_uploadCv,          jl_cvFile));
        jp_uploadGrid.add(buildUploadRow("Cover Letter",    jb_uploadCoverLetter, jl_coverLetterFile));
        jp_uploadGrid.add(buildUploadRow("Transcript *",    jb_uploadTranscript,  jl_transcriptFile));
        jp_uploadGrid.add(buildUploadRow("Additional Docs", jb_uploadAdditional,  jl_additionalFile));

        jb_submit = new JButton("Submit Application");
        jb_submit.setBackground(clr_acceptGreen);
        jb_submit.setForeground(clr_white);
        jb_submit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        jb_submit.setFocusPainted(false);
        jb_submit.setBorderPainted(false);
        jb_submit.setOpaque(true);
        jb_submit.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel jp_btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        jp_btnRow.setBackground(clr_white);
        jp_btnRow.add(jb_submit);

        JPanel jp_uploadsTop = new JPanel(new BorderLayout());
        jp_uploadsTop.setBackground(clr_white);
        jp_uploadsTop.add(jl_secDocs,    BorderLayout.NORTH);
        jp_uploadsTop.add(jp_uploadGrid, BorderLayout.CENTER);

        jp_uploadsSection.add(jp_uploadsTop, BorderLayout.CENTER);
        jp_uploadsSection.add(jp_btnRow,     BorderLayout.SOUTH);

        jp_card.add(jp_eligibility,    BorderLayout.NORTH);
        jp_card.add(jp_uploadsSection, BorderLayout.CENTER);

        JScrollPane jp_outerScroll = new JScrollPane(jp_card);
        jp_outerScroll.setBorder(null);
        jp_outerScroll.getVerticalScrollBar().setUnitIncrement(12);

        jp_content.add(jp_titlePanel,   BorderLayout.NORTH);
        jp_content.add(jp_outerScroll,  BorderLayout.CENTER);
        add(jp_content, BorderLayout.CENTER);

        // LISTENERS
        jb_logout.addActionListener(new LogoutListener());
        jb_back.addActionListener(new BackListener());
        jb_uploadCv.addActionListener(new UploadListener(jl_cvFile, false));
        jb_uploadCoverLetter.addActionListener(new UploadListener(jl_coverLetterFile, false));
        jb_uploadTranscript.addActionListener(new UploadListener(jl_transcriptFile, false));
        jb_uploadAdditional.addActionListener(new UploadListener(jl_additionalFile, true));
        jb_submit.addActionListener(new SubmitListener());
    }

    // LOGOUT LISTENER
    private class LogoutListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int r = JOptionPane.showConfirmDialog(ApplyJobFrame.this,
                    "Do you want to log out?", "Logout", JOptionPane.OK_CANCEL_OPTION);
            if (r == JOptionPane.OK_OPTION) System.exit(0);
        }
    }

    // BACK LISTENER
    private class BackListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            goBack();
        }
    }

    // UPLOAD LISTENER
    private class UploadListener implements ActionListener {
        private JLabel fileLabel;
        private boolean multiSelect;
        public UploadListener(JLabel fileLabel, boolean multiSelect) {
            this.fileLabel   = fileLabel;
            this.multiSelect = multiSelect;
        }
        public void actionPerformed(ActionEvent e) {
            chooseFile(fileLabel, multiSelect);
        }
    }

    // SUBMIT LISTENER
    private class SubmitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (jl_cvFile.getText().equals("No file selected")
                    || jl_transcriptFile.getText().equals("No file selected")) {
                JOptionPane.showMessageDialog(ApplyJobFrame.this,
                    "Please upload both CV / Resume and Transcript before submitting.",
                    "Missing Documents", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int res = JOptionPane.showConfirmDialog(ApplyJobFrame.this,
                "Submit application to " + company + " for " + role + "?\nThis cannot be undone.",
                "Confirm Submission", JOptionPane.YES_NO_OPTION);

            if (res == JOptionPane.YES_OPTION) {

                // COPY FILES — only runs if student confirmed
                if (file_cv          != null) file_cv          = copyFile(file_cv);
                if (file_coverLetter != null) file_coverLetter = copyFile(file_coverLetter);
                if (file_transcript  != null) file_transcript  = copyFile(file_transcript);
                if (files_additional != null) files_additional = copyFiles(files_additional);

                String sql = "INSERT INTO application (studentId, postingId, appliedOn, status) VALUES (?, ?, CURDATE(), 'Pending')";
                try (Connection con = DBConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, studentId);
                    ps.setInt(2, postingId);
                    ps.executeUpdate();

                    ResultSet keys = ps.getGeneratedKeys();
                    keys.next();
                    int applicationId = keys.getInt(1);

                    PreparedStatement psDoc = con.prepareStatement(
                        "INSERT INTO supportingDocument (applicationId, docType, fileName, filePath) VALUES (?, ?, ?, ?)");

                    if (file_cv != null) {
                        psDoc.setInt(1, applicationId); psDoc.setString(2, "CV");
                        psDoc.setString(3, file_cv.getName()); psDoc.setString(4, "uploads/" + file_cv.getName());
                        psDoc.executeUpdate();
                    }
                    if (file_coverLetter != null) {
                        psDoc.setInt(1, applicationId); psDoc.setString(2, "CoverLetter");
                        psDoc.setString(3, file_coverLetter.getName()); psDoc.setString(4, "uploads/" + file_coverLetter.getName());
                        psDoc.executeUpdate();
                    }
                    if (file_transcript != null) {
                        psDoc.setInt(1, applicationId); psDoc.setString(2, "Transcript");
                        psDoc.setString(3, file_transcript.getName()); psDoc.setString(4, "uploads/" + file_transcript.getName());
                        psDoc.executeUpdate();
                    }
                    if (files_additional != null) {
                        for (File f : files_additional) {
                            psDoc.setInt(1, applicationId); psDoc.setString(2, "Additional");
                            psDoc.setString(3, f.getName()); psDoc.setString(4, "uploads/" + f.getName());
                            psDoc.executeUpdate();
                        }
                    }

                    JOptionPane.showMessageDialog(ApplyJobFrame.this,
                        "Application submitted!\n\nCompany : " + company + "\nRole    : " + role
                        + "\n\nYou will be notified about further rounds via email.",
                        "Submitted", JOptionPane.INFORMATION_MESSAGE);
                    goBack();
                } catch (SQLIntegrityConstraintViolationException ex) {
                    JOptionPane.showMessageDialog(ApplyJobFrame.this,
                        "You have already applied to this posting.",
                        "Duplicate Application", JOptionPane.WARNING_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(ApplyJobFrame.this,
                        "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // GO BACK — called from BackListener and SubmitListener
    private void goBack() {
    	jobPostingsFrame.setVisible(true);
    	dashboard.refreshGridStats();
        dispose();
    }

    // BUILD ELIGIBILITY TEXT — complex DB logic, called once from constructor
    private String buildEligibilityText() {
        StringBuilder sb = new StringBuilder();
        sb.append("Company  : ").append(company).append("\n");
        sb.append("Role     : ").append(role).append("\n\n");
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps1 = con.prepareStatement(
                "SELECT rp.description, rp.location, " +
                "jo.salary, jo.workingHours, " +
                "po.stipend, po.placementDuration " +
                "FROM recruitmentPosting rp " +
                "LEFT JOIN jobOpportunity jo ON rp.postingId = jo.postingId " +
                "LEFT JOIN placementOpportunity po ON rp.postingId = po.postingId " +
                "WHERE rp.postingId = ?");
            ps1.setInt(1, postingId);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                sb.append("Location : ").append(rs1.getString("location") != null ? rs1.getString("location") : "—").append("\n");

                double sal    = rs1.getDouble("salary");
                boolean isJob = !rs1.wasNull();

                if (isJob) {
                    sb.append("Type     : Job\n");
                    if (sal > 0) sb.append("Salary   : Rs ").append(sal).append("\n");
                    if (rs1.getString("workingHours") != null)
                        sb.append("Hours    : ").append(rs1.getString("workingHours")).append("\n");
                } else {
                    sb.append("Type     : Placement\n");
                    double sti = rs1.getDouble("stipend");
                    if (sti > 0) sb.append("Stipend  : Rs ").append(sti).append("/month\n");
                    if (rs1.getString("placementDuration") != null)
                        sb.append("Duration : ").append(rs1.getString("placementDuration")).append("\n");
                }

                sb.append("\nJob Description:\n").append(
                    rs1.getString("description") != null ? rs1.getString("description") : "").append("\n");
            }
        } catch (SQLException ex) {
            sb.append("\n(Could not load details: ").append(ex.getMessage()).append(")");
        }
        return sb.toString();
    }

    // BUILD UPLOAD ROW — called 4 times
    private JPanel buildUploadRow(String labelText, JButton jb_upload, JLabel jl_fileLabel) {
        JPanel jp_row = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        jp_row.setBackground(clr_white);
        JLabel jl_label = new JLabel(labelText + ":");
        jl_label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jl_label.setPreferredSize(new Dimension(155, 22));
        jp_row.add(jl_label);
        jp_row.add(jb_upload);
        jp_row.add(jl_fileLabel);
        return jp_row;
    }

    // MAKE UPLOAD BUTTON — called 4 times
    private JButton makeUploadButton() {
        JButton btn = new JButton("Choose File");
        btn.setBackground(clr_blue);
        btn.setForeground(clr_white);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // STYLE FILE LABEL — called 4 times
    private void styleFileLabel(JLabel lbl) {
        lbl.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lbl.setForeground(clr_textMuted);
    }

    private void chooseFile(JLabel jl_fileLabel, boolean multiSelect) {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(multiSelect);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (multiSelect) {
                files_additional = chooser.getSelectedFiles();
                jl_fileLabel.setText(files_additional.length + " file(s) selected");
            } else {
                File f = chooser.getSelectedFile();
                if      (jl_fileLabel == jl_cvFile)          file_cv          = f;
                else if (jl_fileLabel == jl_coverLetterFile) file_coverLetter = f;
                else if (jl_fileLabel == jl_transcriptFile)  file_transcript  = f;
                jl_fileLabel.setText(f.getName());
            }
            jl_fileLabel.setForeground(clr_acceptGreen);
        }
    }

    private static final String UPLOAD_DIR = "src/uploads/";

        // COPY FILE — called on submit
    private File copyFile(File source) {
        try {
            File destDir = new File(UPLOAD_DIR);
            if (!destDir.exists()) destDir.mkdirs();

            String destName = studentId + "_" + System.currentTimeMillis() + "_" + source.getName();
            File dest = new File(destDir, destName);

            java.nio.file.Files.copy(
                source.toPath(),
                dest.toPath(),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );
            return dest;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "File copy failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return source;
        }
    }

    // COPY FILES — for multi-select additional docs
    private File[] copyFiles(File[] sources) {
        File[] copied = new File[sources.length];
        for (int i = 0; i < sources.length; i++) {
            copied[i] = copyFile(sources[i]);
        }
        return copied;
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
	            JOptionPane.showMessageDialog(ApplyJobFrame.this,
	                    "DB Error: " + ex.getMessage(),
	                    "Error",
	                    JOptionPane.ERROR_MESSAGE);
	        }
    }

}
