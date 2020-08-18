package neointernship.chess.game.gameplay.moveaction.commands.allow;

import neointernship.chess.game.gameplay.figureactions.IPossibleActionList;
import neointernship.chess.game.gameplay.kingstate.update.KingIsAttackedComputation;
import neointernship.chess.game.model.answer.IAnswer;
import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.chess.game.model.playmap.field.IField;
import neointernship.web.client.communication.message.TurnStatus;

public class CheckCommand extends AbstractCommand implements IAllowCommand {

    public CheckCommand(IBoard board, IMediator mediator) {
        super(board, mediator);
        turnStatus = TurnStatus.Check;
    }

    @Override
    public void execute(IAnswer answer) {
        final IField startField = board.getField(answer.getStartX(), answer.getStartY());

        mediator.deleteConnection(startField);
    }

    @Override
    public boolean check(IField startField, IField finishField) {
        return false;
    }

    public int getCountChecks(Color colorFigure, IPossibleActionList possibleActionList) {
        IField startFieldKing = mediator.getField(mediator.getKing(colorFigure));

        KingIsAttackedComputation kingIsAttackedComputation = new KingIsAttackedComputation(possibleActionList, mediator);
        return kingIsAttackedComputation.fieldIsAttacked(startFieldKing, colorFigure);
    }
}
