import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class buttons extends JButton {
    private static boolean movement = false;
    private static boolean sim = true;
    private static boolean repeats = true;
    private static boolean voided = false;
    private static boolean running = false;
    private static int[] size; // screen size [width, height]
    private static int[] switches; // grid layout [cols, rows]
    private static Color prevColor;
    private static buttons[][] buttonsList;
    private int[] pos; // grid position
    private int sizeOfButton;
    private boolean clicked = false;
    private int clicks = 0;
    private Color clr;

    public static void boot(int[] psize, int[] pswitches) {
        size = psize;
        switches = pswitches;
        buttonsList = new buttons[pswitches[0]][pswitches[1]];
    }

    private static void sleepAndClick(int MS, int[] point) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return;
        }
        final int fx = point[0];
        final int fy = point[1];
        javax.swing.SwingUtilities.invokeLater(() -> buttonsList[fx][fy].clicked());
    }

    public buttons(int[] ppos, String name, Color clr1) {
        super(" "); // button text

        this.pos = ppos;

        clr = (clr1.getRed() == 0) ? randomColor() : clr1;
        prevColor = randomColor();

        int widthPerButton = size[0] / switches[0];
        int heightPerButton = size[1] / switches[1];
        sizeOfButton = Math.min(widthPerButton, heightPerButton);

        // set properties
        setName(name);
        setBackground(clr);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(true);

        Dimension size = new Dimension(sizeOfButton, sizeOfButton);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(clr, 0),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        // click action
        addActionListener(e -> clicked());

        // hover + movement behavior
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (movement)
                    clicked();

                if (!clicked) {
                    setBackground(lighten(clr, 0.2));
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!clicked)
                    setBackground(clr);
            }
        });
        buttonsList[pos[0]][pos[1]] = this;
    }

    public Point getPixelPosition() {
        int x = pos[0] * sizeOfButton;
        int y = pos[1] * sizeOfButton;
        return new Point(x, y);
    }

    public void clicked() {
        clicks++;
        handler();

        setBackground(darken(clr, 1));
        clicked = true;

        new javax.swing.Timer(2000, evt -> {
            clicks = (clicks - 1 < 0) ? 0 : clicks - 1;
            if (clicks == 0) {
                clr = (sim) ? simliarColor(prevColor) : randomColor();
                if (sim)
                    prevColor = clr;
                setBackground(clr);
                clicked = false;
            }
        }) {
            {
                setRepeats(false);
                start();
            }
        };
    }

    private void btn0_0Clicked() {
        if (running) {
            repeats = !repeats;
            System.out.println("repeats:" + repeats);
        } else {
            running = true;
            new Thread(() -> {
                for (int y = 0; y < buttonsList[0].length; y += 2) {
                    for (int x = (y == 0) ? 1 : 0; x < buttonsList.length; x++) {
                        sleepAndClick(100, new int[] { x, y });
                    }
                    if (y == 0) {
                        voided = false;
                    }
                    for (int x = buttonsList.length - 1; x >= 0; x--) {
                        sleepAndClick(100, new int[] { x, y + 1 });
                    }
                }
                running = false;
                if (repeats) {
                    voided = true;
                    clicked();
                    clicked();
                    clicked();
                    clicked();
                    clicked();
                } else {
                    voided = false;
                }
            }).start();
        }
    }

    private void handler() {
        if (clicks >= 5) {
            if (getName().equals("btn_0_0")) {
                btn0_0Clicked();
            } else if (getName().equals("btn_0_1") && !voided) {
                movement = !movement;
                clicks = 3;
                System.out.println("Drag:" + movement);
            } else if (getName().equals("btn_0_2") && !voided) {
                sim = !sim;
                clicks = 3;
                System.out.println("Simliar:" + sim);
            }
        }
    }

    private static Color randomColor() {
        return new Color(
                (int) (Math.random() * 200) + 30,
                (int) (Math.random() * 200) + 30,
                (int) (Math.random() * 200) + 30);
    }

    private static Color simliarColor(Color clr) {
        int r = Math.abs((clr.getRed() + (int) (Math.random() * 30 - 15)) % 255);
        int g = Math.abs((clr.getGreen() + (int) (Math.random() * 30 - 15)) % 255);
        int b = Math.abs((clr.getBlue() + (int) (Math.random() * 30 - 15)) % 255);
        return new Color(r, g, b);
    }
    
    private static Color betterSimColorUR(int x,y){
        int r = sqeezed((spectrum()));
    }
    
    private static int spectrum(String value, int x,int y){
        int returned;
        if (value.equals("r")) {
            returned = buttonsList[pos[0]][pos[1]].clr.getRed();
        }  else if (value.equals("g")) {
            returned = buttonsList[pos[0]][pos[1]].clr.getGreen();
        }  else if (value.equals("b")) {
             returned = buttonsList[pos[0]][pos[1]].clr.getBlue();
        } else {
            returned = 0;
        }
        return returned;
    }
    
    private static int sqeezed(int value){
        return Math.min((Math.max((value),0)),255);
    }

    private Color lighten(Color color, double fraction) {
        int r = (int) Math.min(255, color.getRed() + 255 * fraction);
        int g = (int) Math.min(255, color.getGreen() + 255 * fraction);
        int b = (int) Math.min(255, color.getBlue() + 255 * fraction);
        return new Color(r, g, b);
    }

    private Color darken(Color color, double fraction) {
        int r = (int) Math.max(0, color.getRed() * (1 - fraction));
        int g = (int) Math.max(0, color.getGreen() * (1 - fraction));
        int b = (int) Math.max(0, color.getBlue() * (1 - fraction));
        return new Color(r, g, b);
    }
}
