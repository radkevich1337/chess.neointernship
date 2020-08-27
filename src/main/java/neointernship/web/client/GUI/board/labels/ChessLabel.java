package neointernship.web.client.GUI.board.labels;

import javax.swing.*;
import java.awt.*;

public class ChessLabel extends JLabel {
    private final String s;

    Font font = new Font("Ariel", Font.PLAIN, 24);
    Color bgLight = new Color(255, 255, 250);
    Color bgDark = new Color(87, 180, 80);
    Color sideLight = new Color(255, 255, 250);

    public ChessLabel(String s) {
        super(s);
        this.s = s;
    }

    public void set(int idx, int row) {
        if (idx == 8 || row == 8) {
            setBackground(sideLight);
            setHorizontalAlignment(SwingConstants.CENTER);
        } else {
            setFont(font);
            setOpaque(true);
            setBackground((idx + row) % 2 == 0 ? bgLight : bgDark);
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    public String getS() {
        return s;
    }
}