package neointernship.chess.game.gameplay.moveaction.commands;

import neointernship.chess.game.gameplay.figureactions.IPossibleActionList;
import neointernship.chess.game.gameplay.figureactions.PossibleActionList;
import neointernship.chess.game.model.answer.IAnswer;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.mediator.Mediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.web.client.communication.message.TurnStatus;

public class RestrictMoveCommand implements IMoveCommand {
    private final IMediator mediator;
    private final IPossibleActionList possibleActionList;
    private final IBoard board;

    public RestrictMoveCommand(final IMediator mediator,
                               final IPossibleActionList possibleActionList,
                               final IBoard board) {
        this.mediator = mediator;
        this.possibleActionList = possibleActionList;
        this.board = board;
    }

    public RestrictMoveCommand(RestrictMoveCommand restrictMoveCommand) {
        this.mediator = new Mediator(restrictMoveCommand.mediator);
        this.board = restrictMoveCommand.board;
        this.possibleActionList = new PossibleActionList((PossibleActionList) restrictMoveCommand.possibleActionList);
    }

    @Override
    public TurnStatus execute(final IAnswer answer) {
        return TurnStatus.ERROR;
    }
}
