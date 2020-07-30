package neointernship.gameviewer.gui;

import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.web.client.GUI.board.labels.ChessLabel;
import neointernship.web.client.GUI.board.labels.labelsgetter.LabelsRepository;

import javax.swing.*;
import java.awt.*;

/**
 * отображение шахмат
 */
public class View extends JFrame {
    private final LabelsRepository labelsRepository;

    private final GridLayout gridLayout;
    Container contentPane = getContentPane();


    public View() {
        labelsRepository = new LabelsRepository();

        gridLayout = new GridLayout(9, 9);
        setTitle("Chess board");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void display(final IMediator mediator, final IBoard board) {
        ChessLabel[][] labels = new ChessLabel[board.getSize()][board.getSize()];
        ChessLabel lastLabel = new ChessLabel(" ");
        ChessLabel[] sideLetters = new ChessLabel[board.getSize()];
        ChessLabel[] sideNumbers = new ChessLabel[board.getSize()];

        contentPane.setLayout(gridLayout);
        for (int i = 0; i < board.getSize(); i++) {
            contentPane.add(sideNumbers[i]);
            for (int j = 0; j < board.getSize(); j++) {
                contentPane.add(labels[i][j]);
            }
        }
        contentPane.add(lastLabel);
        for (int i = 0; i < board.getSize(); i++) {
            contentPane.add(sideLetters[i]);
        }


        setBounds(650,50,700, 700);
        setVisible(true);
    }
}
