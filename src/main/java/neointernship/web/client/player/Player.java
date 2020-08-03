package neointernship.web.client.player;

import neointernship.chess.game.model.answer.IAnswer;
import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.enums.EnumGameState;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.web.client.GUI.Input.IInput;
import neointernship.web.client.GUI.Input.Input;
import neointernship.web.client.GUI.board.view.BoardView;
import neointernship.web.client.communication.message.ClientCodes;
import neointernship.web.client.communication.message.TurnStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Player extends APlayer {
    private BoardView boardView;
    final Scanner scanner;
    private final IInput input;

    public Player(final Color color, final String name, final Input input) {
        super(color, name);
        scanner = new Scanner(System.in);

        this.input = input;
    }

    @Override
    public void init(final IMediator mediator, final IBoard board, final Color color) {
        super.init(mediator, board, color);

        this.boardView = new BoardView(mediator, board);
        boardView.display();
    }

    @Override
    public String getAnswer() throws InterruptedException {
        String answer;

        do {
            answer = input.getMoveAnswer().trim().toLowerCase();
        } while (!Pattern.matches("[a-h]+[1-8]+[-|–|—]+[a-h]+[1-8]", answer) && !answer.equals("gg"));

        return answer;
    }

    @Override
    public void updateMediator(final IAnswer answer, final TurnStatus turnStatus) throws InterruptedException {
        super.updateMediator(answer, turnStatus);

        boardView.update();
        boardView.display();
    }

    @Override
    public char getTransformation() throws InterruptedException {
        final List<Character> figureList = Arrays.asList('Q', 'N', 'B', 'R');
        char figure;

        do {
            final String string = input.getTransformAnswer().trim().toUpperCase();
            figure = string.charAt(0);
        } while (!figureList.contains(figure));

        return figure;
    }

    @Override
    public ClientCodes getHandShakeAnswer() throws InterruptedException {
        String answer = "";

        for (int i = 0; i < 3 && !answer.equals("да") && !answer.equals("нет"); i++) {
            answer = input.getHandShakeAnswer().trim().toLowerCase();
        }

        if (answer.equals("да")) {
            return ClientCodes.YES;
        } else {
            input.invise();
            return ClientCodes.NO;
        }
    }

    @Override
    public void endGame(final EnumGameState enumGameState, final Color color) throws InterruptedException {
        input.endGame(enumGameState, color);
        boardView.dispose();
    }
}
