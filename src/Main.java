import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Main window that creates and displays a grid of buttons.
 */
public class Main extends JFrame {

    /**
     * Creates the button grid window.
     *
     * @param cols number of columns (x)
     * @param rows number of rows (y)
     */
    public Main(int cols, int rows) {
        super("Button Test");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int[] screen = { 800, 800 };
        setSize(screen[0], screen[1]);

        int[] sizes = { cols, rows };

        // initialize button system
        buttons.boot(screen, sizes);

        JPanel panel = new JPanel();

        // GridLayout = (rows, cols)
        panel.setLayout(new GridLayout(rows, cols, 0, 0));
        panel.setBackground(Color.BLACK);

        buttons[][] btns = new buttons[cols][rows];

        // loop rows first (y), then columns (x)
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {

                Color clr = new Color(
                        (int) (Math.random() * 200) + 30,
                        (int) (Math.random() * 200) + 30,
                        (int) (Math.random() * 200) + 30);

                btns[x][y] = new buttons(
                        new int[] { x, y },
                        "btn_" + y + "_" + x,
                        clr);

                panel.add(btns[x][y]);
            }
        }

        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Entry point.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main(5, 50);
        });
    }
}
