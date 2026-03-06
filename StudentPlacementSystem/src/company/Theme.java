package company;

import java.awt.Color;
import java.awt.Font;

public final class Theme {
    private Theme() {}

    public static final Color CLR_BLUE = new Color(0, 102, 153);
    public static final Color CLR_BG = Color.WHITE;
    public static final Color CLR_PANEL = new Color(245, 247, 250);
    public static final Color CLR_BORDER = new Color(220, 225, 230);

    public static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 28);
    public static final Font FONT_H2 = new Font("Arial", Font.BOLD, 18);
    public static final Font FONT_BASE = new Font("Arial", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Arial", Font.PLAIN, 12);
}