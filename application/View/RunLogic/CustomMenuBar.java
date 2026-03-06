package View.RunLogic;

import javax.swing.*;
import java.awt.*;

/**
 * CustomMenuBar is a custom implementation of a JMenuBar
 * with a maroon background to match the application's theme.
 */
public class CustomMenuBar extends JMenuBar {

    private static final Color MAROON_COLOR = Color.decode("#cc0010");
    private static final Color TEXT_COLOR = Color.WHITE;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(MAROON_COLOR);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public JMenu add(JMenu menu) {
        menu.setForeground(TEXT_COLOR);
        menu.setFont(new Font("Courier New", Font.BOLD, 15));
        return super.add(menu);
    }
}
