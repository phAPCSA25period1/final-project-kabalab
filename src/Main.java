import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
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
            int[] sizes = { 10, 10 };

            buttons.boot(screen, sizes);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(sizes[0], sizes[1], 0, 0));
            panel.setBackground(Color.BLACK);

            for (int y = 0; y < sizes[0]; y++) {
                for (int x = 0; x < sizes[1]; x++) {
                    buttons b = new buttons(new int[] { x, y });

                    Color clr = new Color(
                            (int) (Math.random() * 200) + 30,
                            (int) (Math.random() * 200) + 30,
                            (int) (Math.random() * 200) + 30);

                    JButton btn = b.makeClrButton(clr, "btn_" + y + "_" + x);
                    panel.add(btn);
                }
            }

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
