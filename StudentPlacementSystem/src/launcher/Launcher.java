package launcher;

import javax.swing.JFrame;

import student.LoginFrame;

public class Launcher {
    
    public static void main(String[] args) {
        LoginFrame app = new LoginFrame();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setSize(420, 560);
        app.setLocationRelativeTo(null);
        app.setVisible(true);
    }
}
