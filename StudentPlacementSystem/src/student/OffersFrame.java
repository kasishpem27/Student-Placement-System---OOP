package student;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;


import dbconnection.DBConnection;
public class OffersFrame extends JFrame {

    private final Color clr_blue       = new Color(0, 102, 153);
    private final Color clr_white      = Color.WHITE;
    private final Color clr_bg         = new Color(245, 248, 252);
    private final Color clr_cardBorder = new Color(220, 230, 240);
    private final Color clr_textDark   = new Color(40, 40, 40);

    private final StudentDashboardFrame dashboard;
    private final int studentId;

    private JButton jb_logout;
    private JButton jb_back;
    private JButton jb_accept;
    private JButton jb_reject;

    private DefaultTableModel model;
    private JTable table;

    private JRadioButton jrb_job;
    private JRadioButton jrb_placement;

    public OffersFrame(StudentDashboardFrame dashboard, int studentId) {
        super("Offers");
        this.dashboard = dashboard;
        this.studentId = studentId;

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
        JLabel jl_headerTitle = new JLabel("Offers");
        jl_headerTitle.setForeground(clr_white);
        jl_headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jp_headerLeft.add(jl_headerTitle);

        JPanel jp_headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        jp_headerRight.setBackground(clr_blue);
        JLabel jl_userInfo = new JLabel(dashboard.studentName + "  ·  " + dashboard.studentIdStr);
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

        // MAIN CONTENT
        JPanel jp_main = new JPanel(new BorderLayout());
        jp_main.setBackground(clr_white);
        jp_main.setBorder(new EmptyBorder(8, 16, 0, 16));

        // TOP BAR
        JPanel jp_topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp_topBar.setBackground(clr_white);
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

        // FILTER PANEL
        JPanel jp_filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        jp_filterPanel.setBackground(clr_white);

        JLabel jl_filter = new JLabel("Filter :");
        jl_filter.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        jrb_job = new JRadioButton("Job Offers", true);
        jrb_job.setBackground(clr_white);
        jrb_job.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        jrb_placement = new JRadioButton("Placement Offers", false);
        jrb_placement.setBackground(clr_white);
        jrb_placement.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        ButtonGroup bg = new ButtonGroup();
        bg.add(jrb_job);
        bg.add(jrb_placement);

        jp_filterPanel.add(jl_filter);
        jp_filterPanel.add(jrb_job);
        jp_filterPanel.add(jrb_placement);

        // TABLE
        model = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        model.setColumnIdentifiers(new String[]{ "Company Name", "Title", "Offered On", "Expires", "Status", "_offerId" });

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(210, 225, 245));
        table.setSelectionForeground(new Color(40, 40, 40));
        table.getTableHeader().setUI(new javax.swing.plaf.basic.BasicTableHeaderUI());
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(clr_blue);
        table.getTableHeader().setForeground(clr_white);
        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setReorderingAllowed(false);

        TableColumn idCol = table.getColumnModel().getColumn(5);
        idCol.setMinWidth(0); idCol.setMaxWidth(0); idCol.setWidth(0);

        loadOffers();

        JScrollPane jp_scroll = new JScrollPane(table);
        jp_scroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel jp_topSection = new JPanel(new BorderLayout());
        jp_topSection.setBackground(clr_white);
        jp_topSection.add(jp_topBar,      BorderLayout.NORTH);
        jp_topSection.add(jp_filterPanel, BorderLayout.CENTER);

        jp_main.add(jp_topSection, BorderLayout.NORTH);
        jp_main.add(jp_scroll,     BorderLayout.CENTER);
        add(jp_main, BorderLayout.CENTER);

        // BOTTOM BUTTON PANEL
        JPanel jp_bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        jp_bottom.setBackground(clr_white);
        jp_bottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, clr_cardBorder));

        jb_accept = new JButton("Accept Offer");
        jb_accept.setFocusPainted(false);
        jb_accept.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jb_accept.setForeground(clr_white);
        jb_accept.setBackground(clr_blue);
        jb_accept.setBorderPainted(false);
        jb_accept.setOpaque(true);
        jb_accept.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jb_accept.setPreferredSize(new Dimension(160, 36));

        jb_reject = new JButton("Reject Offer");
        jb_reject.setFocusPainted(false);
        jb_reject.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jb_reject.setForeground(clr_white);
        jb_reject.setBackground(clr_blue);
        jb_reject.setBorderPainted(false);
        jb_reject.setOpaque(true);
        jb_reject.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jb_reject.setPreferredSize(new Dimension(160, 36));

        jp_bottom.add(jb_accept);
        jp_bottom.add(jb_reject);
        add(jp_bottom, BorderLayout.SOUTH);

        // disable both buttons if student has already accepted any offer
        if (hasAcceptedOffer()) {
            jb_accept.setEnabled(false);
            jb_reject.setEnabled(false);
        }

        // LISTENERS
        jb_logout.addActionListener(new LogoutListener());
        jb_back.addActionListener(new BackListener());
        jb_accept.addActionListener(new AcceptListener());
        jb_reject.addActionListener(new RejectListener());

        jrb_job.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { loadOffers(); }
        });

        jrb_placement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { loadOffers(); }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                if (hasAcceptedOffer()) { jb_accept.setEnabled(false); jb_reject.setEnabled(false); return; }
                int row = table.getSelectedRow();
                if (row < 0) { jb_accept.setEnabled(true); jb_reject.setEnabled(true); return; }
                String status = (String) model.getValueAt(row, 4);
                boolean canAct = !("Accepted".equals(status) || "Rejected".equals(status));
                jb_accept.setEnabled(canAct);
                jb_reject.setEnabled(canAct);
            }
        });
    }

    // LOGOUT LISTENER
    private class LogoutListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int r = JOptionPane.showConfirmDialog(OffersFrame.this,
                    "Do you want to log out?", "Logout", JOptionPane.OK_CANCEL_OPTION);
            if (r == JOptionPane.OK_OPTION) System.exit(0);
        }
    }

    // BACK LISTENER
    private class BackListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            dashboard.returnToDashboard();
        }
    }

    // ACCEPT LISTENER
    private class AcceptListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(OffersFrame.this, "Please select an offer first.", "No Selection", JOptionPane.INFORMATION_MESSAGE); return; }
            String company = (String) model.getValueAt(row, 0);
            String role    = (String) model.getValueAt(row, 1);
            int offerId    = (int)    model.getValueAt(row, 5);
            int res = JOptionPane.showConfirmDialog(OffersFrame.this,
                "Accept the offer from " + company + "?\n\nRole : " + role +
                "\n\nThe placement office will be notified." +
                "\n\nPlease note: once an offer is accepted, you cannot accept any other offers.",
                "Accept Offer", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                updateOfferStatus(offerId, "Accepted");
                model.setValueAt("Accepted", row, 4);
                jb_accept.setEnabled(false);
                jb_reject.setEnabled(false);
                dashboard.refreshStats();
                JOptionPane.showMessageDialog(OffersFrame.this,
                    "Offer accepted!\n\nCompany : " + company + "\nRole    : " + role + "\n\nCongratulations!",
                    "Accepted", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // REJECT LISTENER
    private class RejectListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(OffersFrame.this, "Please select an offer first.", "No Selection", JOptionPane.INFORMATION_MESSAGE); return; }
            String company = (String) model.getValueAt(row, 0);
            int offerId    = (int)    model.getValueAt(row, 5);
            int res = JOptionPane.showConfirmDialog(OffersFrame.this,
                "Decline the offer from " + company + "?\nThis cannot be undone.",
                "Reject Offer", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (res == JOptionPane.YES_OPTION) {
                updateOfferStatus(offerId, "Rejected");
                model.setValueAt("Rejected", row, 4);
                jb_accept.setEnabled(false);
                jb_reject.setEnabled(false);
                dashboard.refreshStats();
                JOptionPane.showMessageDialog(OffersFrame.this,
                    "Offer from " + company + " declined. Placement office notified.",
                    "Declined", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // LOAD OFFERS
    private void loadOffers() {
        model.setRowCount(0);
        String offerType = jrb_job.isSelected() ? "Job" : "Placement";
        String sql =
            "SELECT o.offerId, " +
            "c.companyName, " +
            "rp.title AS roleTitle, " +
            "o.offeredAt, o.expiryAt, o.status " +
            "FROM offer o " +
            "JOIN application a ON o.applicationId = a.applicationId " +
            "JOIN recruitmentPosting rp ON a.postingId = rp.postingId " +
            "JOIN company c ON rp.companyId = c.companyId " +
            "WHERE a.studentId = ? AND o.offerType = ? " +
            "ORDER BY o.offeredAt DESC";
        try (Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setString(2, offerType);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("companyName"),
                    rs.getString("roleTitle"),
                    rs.getString("offeredAt"),
                    rs.getString("expiryAt") != null ? rs.getString("expiryAt") : "—",
                    rs.getString("status"),
                    rs.getInt("offerId")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // UPDATE OFFER STATUS — called from AcceptListener and RejectListener
    private void updateOfferStatus(int offerId, String newStatus) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "UPDATE offer SET status = ? WHERE offerId = ?")) {
            ps.setString(1, newStatus);
            ps.setInt(2, offerId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // HAS ACCEPTED OFFER — checks if student has already accepted any offer
    private boolean hasAcceptedOffer() {
        try (Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT COUNT(*) FROM offer o " +
                "JOIN application a ON o.applicationId = a.applicationId " +
                "WHERE a.studentId = ? AND o.status = 'Accepted'")) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException ex) { return false; }
    }
}