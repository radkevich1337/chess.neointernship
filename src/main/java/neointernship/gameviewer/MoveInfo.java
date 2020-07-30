package neointernship.gameviewer;

import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.web.client.communication.message.TurnStatus;

public class MoveInfo {
    public MoveInfo(IMediator mediator, IBoard board, TurnStatus turnStatus) {
        this.mediator = mediator;
        this.board = board;
        this.turnStatus = turnStatus;
    }

    public IMediator getMediator() {
        return mediator;
    }

    public IBoard getBoard() {
        return board;
    }

    public TurnStatus getTurnStatus() {
        return turnStatus;
    }

    private final IMediator mediator;
    private final IBoard board;
    private TurnStatus turnStatus;
}
