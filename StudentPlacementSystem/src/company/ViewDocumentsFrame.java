package company;

import dbconnection.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewDocumentsFrame extends JFrame {

    private final Color clr_blue       = Theme.CLR_BLUE;
    private final Color clr_blue_hover = new Color(0, 122, 183);
    private final Color clr_white      = Color.WHITE;
    private final Color clr_bg         = Theme.CLR_PANEL;
    private final Color clr_cardBorder = Theme.CLR_BORDER;
    private final Color clr_textDark   = new Color(40, 40, 40);
    private final Color clr_bottomBackground = new Color(240, 244, 248);

    private final String companyName;
    private final String companyId;
    private final CompanyDashboardFrame dashboard;

    private DefaultTableModel tm_docs;
    private JTable jt_docs;

    public ViewDocumentsFrame(String companyName, String companyId, CompanyDashboardFrame dashboard) {
        super("CareerConnect - View Documents");
        this.companyName = companyName;
        this.companyId   = companyId == null ? "1" : companyId.replaceAll("[^0-9]", "");
        this.dashboard   = dashboard;

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 750));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // HEADER
        JPanel jp_header = new JPanel(new BorderLayout());
        jp_header.setBackground(clr_blue);
        jp_header.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel jp_headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        jp_headerLeft.setBackground(clr_blue);
        JLabel jl_title = new JLabel("View Documents");
        jl_title.setForeground(clr_white);
        jl_title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jp_headerLeft.add(jl_title);

        JPanel jp_headerRightInner = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        jp_headerRightInner.setBackground(clr_blue);

        java.net.URL profileUrl = getClass().getResource("/images/profile.png");
        JLabel jl_profileIcon = new JLabel();
        if (profileUrl != null) {
            Image scaled = new ImageIcon(profileUrl).getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
            jl_profileIcon = new JLabel(new ImageIcon(scaled));
        }
        JLabel jl_userInfo = new JLabel(companyName + "  ·  COM" + companyId);
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

        // CONTENT WRAPPER
        JPanel jp_wrapper = new JPanel(new BorderLayout());
        jp_wrapper.setBackground(clr_bg);

        JPanel jp_root = new JPanel(new BorderLayout());
        jp_root.setBackground(clr_bg);
        jp_root.setBorder(new EmptyBorder(16, 18, 18, 18));

        JPanel jp_topBar = new JPanel(new BorderLayout());
        jp_topBar.setBackground(clr_bg);
        jp_topBar.setBorder(new EmptyBorder(0, 0, 14, 0));

        JButton jb_back = new JButton("← Back");
        jb_back.setFocusPainted(false);
        jb_back.setBackground(clr_white);
        jb_back.setForeground(clr_textDark);
        jb_back.setBorder(new LineBorder(clr_cardBorder, 1));
        jb_back.setOpaque(true);
        jb_back.setPreferredSize(new Dimension(90, 32));
        jb_back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jb_back.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jp_topBar.add(jb_back, BorderLayout.WEST);

        tm_docs = new DefaultTableModel(
                new Object[]{"Student Name", "Document", " Posting Title", "doc_id", "file_type", "stored_path"}, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        jt_docs = new JTable(tm_docs);
        jt_docs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jt_docs.setRowHeight(30);
        jt_docs.setGridColor(new Color(240, 244, 248));
        jt_docs.setBackground(clr_white);
        jt_docs.setForeground(clr_textDark);
        jt_docs.setSelectionBackground(new Color(210, 230, 250));
        jt_docs.setSelectionForeground(clr_textDark);

        // Hide internal columns
        for (int col : new int[]{3, 4, 5}) {
            jt_docs.getColumnModel().getColumn(col).setMinWidth(0);
            jt_docs.getColumnModel().getColumn(col).setMaxWidth(0);
            jt_docs.getColumnModel().getColumn(col).setWidth(0);
        }

        JTableHeader th = jt_docs.getTableHeader();
        th.setBackground(clr_blue);
        th.setForeground(clr_white);
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setPreferredSize(new Dimension(0, 36));
        th.setReorderingAllowed(false);

        JScrollPane sp_table = new JScrollPane(jt_docs);
        sp_table.setBorder(new LineBorder(clr_cardBorder, 1));
        sp_table.getViewport().setBackground(new Color(240, 244, 248));

        jp_root.add(jp_topBar, BorderLayout.NORTH);
        jp_root.add(sp_table, BorderLayout.CENTER);

        JScrollPane sp = new JScrollPane(jp_root);
        sp.setBorder(null);
        sp.getViewport().setBackground(clr_bg);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        jp_wrapper.add(sp, BorderLayout.CENTER);

        JButton jb_download = new JButton("Download Selected");
        jb_download.setFocusPainted(false);
        jb_download.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jb_download.setForeground(Color.WHITE);
        jb_download.setBackground(clr_blue);
        jb_download.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jb_download.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        jb_download.setOpaque(true);
        jb_download.setContentAreaFilled(true);
        jb_download.setBorderPainted(false);

        JPanel jp_bottomPanel = new JPanel();
        jp_bottomPanel.setBackground(clr_bottomBackground);
        jp_bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        jp_bottomPanel.add(jb_download);
        jp_wrapper.add(jp_bottomPanel, BorderLayout.SOUTH);

        add(jp_wrapper, BorderLayout.CENTER);

        // Load data
        tm_docs.setRowCount(0);
        String sql =
                "SELECT DISTINCT " +
                "       s.studentId      AS student_id, " +
                "       s.fullName       AS student_name, " +
                "       sd.fileName      AS document, " +
                "       rp.title         AS job_title, " +
                "       sd.documentId    AS doc_id, " +
                "       sd.docType       AS file_type, " +
                "       sd.filePath      AS stored_path " +
                "FROM supportingDocument sd " +
                "JOIN application a   ON sd.applicationId = a.applicationId " +
                "JOIN student s       ON a.studentId = s.studentId " +
                "JOIN recruitmentPosting rp ON a.postingId = rp.postingId " +
                "JOIN company c       ON rp.companyId = c.companyId " +
                "WHERE c.companyId = ? " +
                "ORDER BY s.studentId, sd.docType";

        int count = 0;
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(companyId));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tm_docs.addRow(new Object[]{
                            rs.getString("student_name"),
                            rs.getString("document"),
                            rs.getString("job_title"),
                            rs.getInt("doc_id"),
                            rs.getString("file_type"),
                            rs.getString("stored_path")
                    });
                    count++;
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "DB error while loading documents:" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        if (count == 0) {
            tm_docs.addRow(new Object[]{"-", "No documents found", "-", -1, "-", null});
        }

        // ═══════════════════════════════════════════════
        // ALL LISTENERS
        // ═══════════════════════════════════════════════
        jb_back.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                jb_back.setBackground(new Color(230, 235, 245));
            }
            public void mouseExited(MouseEvent e) {
                jb_back.setBackground(clr_white);
            }
        });
        jb_back.addActionListener(new BackHandler());

        jb_download.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                jb_download.setBackground(clr_blue_hover);
            }
            public void mouseExited(MouseEvent e) {
                jb_download.setBackground(clr_blue);
            }
        });
        jb_download.addActionListener(new DownloadHandler());
    }

    // BACK LISTENER
    private class BackHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            dashboard.returnToDashboard();
        }
    }

    // DOWNLOAD LISTENER
    private class DownloadHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int selectedRow = jt_docs.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(ViewDocumentsFrame.this, "Select a document first.", "Download", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String studentId  = String.valueOf(tm_docs.getValueAt(selectedRow, 0));
            String documentName    = String.valueOf(tm_docs.getValueAt(selectedRow, 1));
            String storedPath = (String) tm_docs.getValueAt(selectedRow, 5);

            File sourceFile = null;
            if (storedPath != null && !storedPath.trim().isEmpty()) {
                sourceFile = new File(storedPath.trim());
                if (!sourceFile.exists()) {
                    sourceFile = null;
                }
            }

            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Choose where to save");
            fc.setCurrentDirectory(new File(System.getProperty("user.home") + File.separator + "Desktop"));
            fc.setSelectedFile(new File(System.getProperty("user.home") + File.separator + "Desktop" + File.separator + documentName));
            int choice = fc.showSaveDialog(ViewDocumentsFrame.this);
            if (choice != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File destFile = fc.getSelectedFile();

            try {
                if (sourceFile != null) {
                    Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    JOptionPane.showMessageDialog(ViewDocumentsFrame.this,
                        "File not found on disk. The stored path does not exist.",
                        "Download Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(
                        ViewDocumentsFrame.this,
                        "Document downloaded!\n\n"
                                + "Student: " + studentId + "\n"
                                + "Document: " + documentName + "\n"
                                + "Saved to: " + destFile.getAbsolutePath(),
                        "Download",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (IOException ioEx) {
                JOptionPane.showMessageDialog(
                    ViewDocumentsFrame.this,
                    "Failed to save file: " + ioEx.getMessage(),
                    "Download Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}