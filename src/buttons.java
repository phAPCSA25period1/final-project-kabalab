import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * Custom button class that represents a grid-based interactive button.
 * <p>
 * Each button maintains its own state (color, clicks) while also interacting
 * with a shared static grid. The class supports animated clicking, color
 * propagation, and pattern-based automation.
 */
public class buttons extends JButton {

    /** Enables click-on-hover behavior. */
    private static boolean movement = false;

    /** Enables color similarity mode. */
    private static boolean sim = true;

    /** Enables repeating automated patterns. */
    private static boolean repeats = true;

    /** Internal flag used to prevent unintended triggers. */
    private static boolean voided = false;

    /** Indicates if an automated sequence is running. */
    private static boolean running = false;

    /** Direction used for color propagation ("N", "L", "R"). */
    private static String direction = "N";

    /** Screen size [width, height]. */
    private static int[] size;

    /** Grid size [columns, rows]. */
    private static int[] switches;

    /** Previously used color for similarity calculations. */
    private static Color prevColor;

    /** Shared 2D grid of buttons indexed as [x][y]. */
    private static buttons[][] buttonsList;

    /** Position of this button in the grid [x, y]. */
    private int[] pos;

    /** Pixel size of each button. */
    private int sizeOfButton;

    /** Whether the button is currently in a clicked state. */
    private boolean clicked = false;

    /** Number of recent clicks registered. */
    private int clicks = 0;

    /** Current color of the button. */
    private Color clr;

    /**
     * Initializes the static grid configuration and button storage.
     *
     * @param psize     screen size as [width, height]
     * @param pswitches grid size as [columns, rows]
     */
    public static void boot(int[] psize, int[] pswitches) {
        size = psize;
        switches = pswitches;
        buttonsList = new buttons[pswitches[0]][pswitches[1]];
    }

    /**
     * Delays execution and triggers a click on a specific button.
     * Ensures the click is executed on the Swing event thread.
     *
     * @param MS    delay in milliseconds
     * @param point grid coordinate [x, y]
     */
    private static void sleepAndClick(int MS, int[] point) {
        try {
            Thread.sleep(MS);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return;
        }

        int x = point[0];
        int y = point[1];

        if (x < 0 || y < 0 || x >= buttonsList.length || y >= buttonsList[0].length) {
            return;
        }

        javax.swing.SwingUtilities.invokeLater(() -> buttonsList[x][y].clicked());
    }

    /**
     * Constructs a button with a specified position, name, and base color.
     *
     * @param ppos grid position [x, y]
     * @param name identifier name of the button
     * @param clr1 initial color (randomized if black)
     */
    public buttons(int[] ppos, String name, Color clr1) {
        super();
        setText("");
        this.pos = ppos;
        clr = (clr1.getRed() == 0) ? randomColor() : clr1;
        prevColor = new Color(255, 0, 0);

        int widthPerButton = size[0] / switches[0];
        int heightPerButton = size[1] / switches[1];
        sizeOfButton = Math.min(widthPerButton, heightPerButton);

        setName(name);
        setBackground(clr);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(true);

        Dimension d = new Dimension(sizeOfButton, sizeOfButton);
        setPreferredSize(d);
        setMinimumSize(d);
        setMaximumSize(d);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(clr, 0),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        addActionListener(e -> clicked());

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (movement)
                    clicked();
                if (!clicked)
                    setBackground(lighten(clr, 0.2));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!clicked)
                    setBackground(clr);
            }
        });

        buttonsList[pos[0]][pos[1]] = this;
    }

    /**
     * Converts the grid position into pixel coordinates.
     *
     * @return pixel position of the button
     */
    public Point getPixelPosition() {
        return new Point(pos[0] * sizeOfButton, pos[1] * sizeOfButton);
    }

    /**
     * Handles click behavior, including visual effects, click counting,
     * and delayed color reset.
     */
    public void clicked() {
        clicks++;
        handler();

        setBackground(darken(clr, 1));
        clicked = true;

        new javax.swing.Timer(2000, evt -> {
            clicks = Math.max(0, clicks - 1);

            if (clicks == 0) {
                if (sim) {
                    clr = direction.equals("N")
                            ? simliarColor(prevColor)
                            : betterSimColor(direction, pos[0], pos[1]);
                } else {
                    clr = randomColor();
                }

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

    /**
     * Executes an automated sweeping pattern across the grid.
     * Alternates direction per row and optionally repeats.
     */
    private void btn0_0Clicked() {
        if (running) {
            repeats = !repeats;
            System.out.println("repeats: " + repeats);
            return;
        }

        running = true;

        new Thread(() -> {
            int cols = buttonsList.length;
            int rows = buttonsList[0].length;

            for (int y = 0; y < rows; y += 2) {
                direction = "L";

                for (int x = (y == 0) ? 1 : 0; x < cols; x++) {
                    sleepAndClick(2, new int[] { x, y });
                }

                if (y == 0)
                    voided = false;

                if (y + 1 < rows) {
                    direction = "R";
                    for (int x = cols - 1; x >= 0; x--) {
                        sleepAndClick(2, new int[] { x, y + 1 });
                    }
                }
            }

            direction = "N";
            running = false;

            if (repeats) {
                voided = true;
                for (int i = 0; i < 5; i++)
                    clicked();
            } else {
                voided = false;
            }

        }).start();
    }

    /**
     * Handles special multi-click triggers based on button identity.
     */
    private void handler() {
        if (clicks >= 5) {
            if (getName().equals("btn_0_0")) {
                btn0_0Clicked();
                clicks = 3;
            } else if (getName().equals("btn_0_1") && !voided) {
                movement = !movement;
                clicks = 3;
                System.out.println("Drag: " + movement);
            } else if (getName().equals("btn_0_2") && !voided) {
                sim = !sim;
                clicks = 3;
                System.out.println("Similar: " + sim);
            }
        }
    }

    /** @return a random soft color */
    private static Color randomColor() {
        return new Color(
                (int) (Math.random() * 200) + 30,
                (int) (Math.random() * 200) + 30,
                (int) (Math.random() * 200) + 30);
    }

    /**
     * Generates a color similar to the provided color.
     *
     * @param c base color
     * @return slightly varied color
     */
    private static Color simliarColor(Color c) {
        return new Color(
                clamp(c.getRed() + randOffset()),
                clamp(c.getGreen() + randOffset()),
                clamp(c.getBlue() + randOffset()));
    }

    /**
     * Blends colors from neighboring buttons based on direction.
     *
     * @param dir direction ("L" or "R")
     * @param x   grid x
     * @param y   grid y
     * @return blended color
     */
    private static Color betterSimColor(String dir, int x, int y) {
        int nx = x + (dir.equals("L") ? -1 : 1);
        if (nx < 0 || nx >= switches[0])
            nx = x;

        int r = avg(spectrum("r", x, y - 1), spectrum("r", nx, y));
        int g = avg(spectrum("g", x, y - 1), spectrum("g", nx, y));
        int b = avg(spectrum("b", x, y - 1), spectrum("b", nx, y));

        return new Color(
                clamp(r + randOffset()),
                clamp(g + randOffset()),
                clamp(b + randOffset()));
    }

    /**
     * Retrieves a specific RGB component from a grid position.
     *
     * @param val component ("r", "g", "b")
     * @param x   column index
     * @param y   row index
     * @return color component value
     */
    private static int spectrum(String val, int x, int y) {
        if (x < 0 || y < 0 || x >= buttonsList.length || y >= buttonsList[0].length) {
            return 0;
        }

        Color c = buttonsList[x][y].clr;

        switch (val) {
            case "r":
                return c.getRed();
            case "g":
                return c.getGreen();
            case "b":
                return c.getBlue();
        }
        return 0;
    }

    /** @return average of two integers */
    private static int avg(int a, int b) {
        return (a + b) / 2;
    }

    /** @return random offset for color variation */
    private static int randOffset() {
        return (int) (Math.random() * 30 - 15);
    }

    /** Clamps value between 0 and 255 */
    private static int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }

    /**
     * Lightens a color.
     *
     * @param c base color
     * @param f fraction (0–1)
     * @return lightened color
     */
    private Color lighten(Color c, double f) {
        return new Color(
                clamp((int) (c.getRed() + 255 * f)),
                clamp((int) (c.getGreen() + 255 * f)),
                clamp((int) (c.getBlue() + 255 * f)));
    }

    /**
     * Darkens a color.
     *
     * @param c base color
     * @param f fraction (0–1)
     * @return darkened color
     */
    private Color darken(Color c, double f) {
        return new Color(
                clamp((int) (c.getRed() * (1 - f))),
                clamp((int) (c.getGreen() * (1 - f))),
                clamp((int) (c.getBlue() * (1 - f))));
    }
}
