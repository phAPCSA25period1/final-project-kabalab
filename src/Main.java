import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Button Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            int[] screen = { 800, 800 };
            frame.setSize(screen[0], screen[1]);

            int[] sizes = { 30, 30 };

            buttons.boot(screen, sizes);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(sizes[0], sizes[1], 0, 0));
            panel.setBackground(Color.BLACK);

            buttons[][] btns = new buttons[sizes[0]][sizes[1]];

            for (int y = 0; y < sizes[0]; y++) {
                for (int x = 0; x < sizes[1]; x++) {
                    Color clr = new Color(
                            (int) (Math.random() * 200) + 30,
                            (int) (Math.random() * 200) + 30,
                            (int) (Math.random() * 200) + 30);
                    btns[y][x] = new buttons(new int[] { x, y }, "btn_" + y + "_" + x, clr);
                    panel.add(btns[y][x]);
                }
            }

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
