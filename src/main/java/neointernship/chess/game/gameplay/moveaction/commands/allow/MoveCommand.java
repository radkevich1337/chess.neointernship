package neointernship.chess.game.gameplay.moveaction.commands.allow;

import neointernship.chess.game.model.answer.IAnswer;
import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.chess.game.model.playmap.field.IField;
import neointernship.web.client.communication.message.TurnStatus;

public class MoveCommand extends AbstractCommand implements IAllowCommand {

    public MoveCommand(IBoard board, IMediator mediator) {
        super(board, mediator);
        turnStatus = TurnStatus.MOVE;
    }

    @Override
    public void execute(IAnswer answer) {
        final IField startField = board.getField(answer.getStartX(), answer.getStartY());
        final IField finalField = board.getField(answer.getFinalX(), answer.getFinalY());

        final Figure startFigure = mediator.getFigure(startField);

        mediator.deleteConnection(startField);
        mediator.addNewConnection(finalField, startFigure);
    }

    @Override
    public boolean check(final IField startField, final IField finishField) {
        return true;
    }
}
