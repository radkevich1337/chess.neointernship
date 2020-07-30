package neointernship.gameviewer;

import neointernship.chess.game.model.answer.IAnswer;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.mediator.Mediator;
import neointernship.chess.game.model.playmap.board.Board;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.web.client.communication.message.TurnStatus;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

public class GameInfo {
    private final String firstPlayerName;
    private final String secondPlayerName;
    private MoveInfo currentMove;

    private int historyIndex;
    private final LinkedList<MoveInfo> history;

    private final LogReader logReader;

    private MoveCommands moveCommands;

    public GameInfo(final String historyFilePath) throws FileNotFoundException {
        currentMove = null;
        this.history = new LinkedList<>();

        historyIndex = 0;

        logReader = new LogReader(historyFilePath);
        this.firstPlayerName = logReader.getFirstPlayerName();
        this.secondPlayerName = logReader.getSecondPlayerName();
    }

    public LinkedList<MoveInfo> getHistory() {
        return history;
    }

    private void createHistory() throws IOException {
        while (logReader.readNextLine()) {
            final IBoard board = createNextBoard();
            final IMediator mediator = createNextMediator(board);
            final TurnStatus turnStatus = logReader.getTurnStatus();
            MoveInfo newMove = new MoveInfo(mediator, board, turnStatus);
            history.add(newMove);
        }
    }

    private IMediator createNextMediator(final IBoard board) {
        IMediator pastMediator = history.get(historyIndex-1).getMediator();
        IMediator newMediator = null;

        if (pastMediator != null) {
            newMediator = new Mediator(pastMediator);
            IAnswer answer = logReader.getAnswer();

            moveCommands = new MoveCommands(newMediator, board);
            moveCommands.execute(answer);
        }

        return newMediator;
    }

    public void update(final String answer) {
        if (answer.equals("Forward")) {
            historyIndex++;
        } else {
            historyIndex--;
        }
        currentMove = history.get(historyIndex);
    }

    public void decrementIndex() {
        historyIndex--;
    }

    public MoveInfo getMoveInfo() {
        return history.get(historyIndex);
    }

    public IBoard createNextBoard() {
        return new Board();
    }
}
