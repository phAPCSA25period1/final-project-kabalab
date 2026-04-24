import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class buttons {
    private static int[] size; // screen size [width, height]
    private static int[] switches; // grid layout [cols, rows]
    private int[] pos; // button grid position [x, y]
    private int sizeOfButton; // pixel size
    private boolean clicked = false;
    private boolean test = false;

    public static void boot(int[] psize, int[] pswitches) {
        size = psize;
        switches = pswitches;
    }

    public buttons(int[] ppos) {
        pos = ppos;

        // calculate button size based on screen and grid
        int widthPerButton = size[0] / switches[0];
        int heightPerButton = size[1] / switches[1];

        sizeOfButton = Math.min(widthPerButton, heightPerButton);
    }

    /**
     * Returns the pixel position of this button
     */
    public Point getPixelPosition() {
        int x = pos[0] * sizeOfButton;
        int y = pos[1] * sizeOfButton;
        return new Point(x, y);
    }

    public JButton makeClrButton(Color clr, String str) {
        JButton btn = new JButton(" ");
        btn.setName(str);
        btn.setBackground(clr);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension size = new Dimension(sizeOfButton, sizeOfButton);
        btn.setPreferredSize(size);
        btn.setMinimumSize(size);
        btn.setMaximumSize(size);

        btn.setContentAreaFilled(false);
        btn.setOpaque(true);

        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(clr, 0),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        btn.addActionListener(e -> {
            handler(btn.getName());

            // make it darker when clicked
            btn.setBackground(darken(clr, 1));
            clicked = true;
            // after 2 seconds, return to normal
            new javax.swing.Timer(2000, evt -> {
                btn.setBackground(clr);
                clicked = false;
            }) {
                {
                    setRepeats(false);
                    start();
                }
            };
        });

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!clicked) {
                    btn.setBackground(lighten(clr, 0.2));
                    System.out.println(test);
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!clicked)
                    btn.setBackground(clr);
            }
        });

        return btn;
    }

    /**
     * Handles button clicks
     */
    private void handler(String name) {
        if (name.equals("btn_0_0")) {
            test = !test;
            System.out.println(test);

        } else {
            // System.out.println(name + " not found");
        }
    }

    /**
     * Lightens a color
     */
    private Color lighten(Color color, double fraction) {
        int r = (int) Math.min(255, color.getRed() + 255 * fraction);
        int g = (int) Math.min(255, color.getGreen() + 255 * fraction);
        int b = (int) Math.min(255, color.getBlue() + 255 * fraction);
        return new Color(r, g, b);
    }

    /**
     * Darkens a color
     */
    private Color darken(Color color, double fraction) {
        int r = (int) Math.max(0, color.getRed() * (1 - fraction));
        int g = (int) Math.max(0, color.getGreen() * (1 - fraction));
        int b = (int) Math.max(0, color.getBlue() * (1 - fraction));
        return new Color(r, g, b);
    }
}
