package neointernship.chess.game.gameplay.moveaction.commands.allow;

import neointernship.chess.game.gameplay.figureactions.IPossibleActionList;
import neointernship.chess.game.gameplay.kingstate.update.KingIsAttackedComputation;
import neointernship.chess.game.model.answer.IAnswer;
import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.figure.piece.King;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.chess.game.model.playmap.field.IField;
import neointernship.web.client.communication.message.TurnStatus;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Класс реализующий команду рокировки
 */
public class CastlingCommand extends AbstractCommand implements IAllowCommand {

    private IField startFieldKing;
    private IField finishFieldKing;

    public CastlingCommand(IBoard board, IMediator mediator) {
        super(board, mediator);
        turnStatus = TurnStatus.CASTLING;
    }

    @Override
    public void execute(IAnswer answer) {
        startFieldKing = board.getField(answer.getStartX(), answer.getStartY());
        finishFieldKing = board.getField(answer.getFinalX(), answer.getFinalY());

        Figure king = mediator.getFigure(startFieldKing);

        int yStartCoordRook = 0; // todo корректно только для класических шахмат !!!
        int difCoordRook = 1;

        // если двигаемся вправо
        if (startFieldKing.getYCoord() < finishFieldKing.getYCoord()) {
            yStartCoordRook = 7;
            difCoordRook = -1;
        }
        IField startFieldRook = board.getField(startFieldKing.getXCoord(), yStartCoordRook);

        IField finalFieldRook = board.getField(
                finishFieldKing.getXCoord(),
                finishFieldKing.getYCoord() + difCoordRook);

        Figure rook = mediator.getFigure(startFieldRook);

        mediator.deleteConnection(startFieldKing);
        mediator.deleteConnection(startFieldRook);
        mediator.addNewConnection(finishFieldKing, king);
        mediator.addNewConnection(finalFieldRook, rook);
    }

    @Override
    public boolean check(final IField startField, final IField finishField) {
        final Figure startFigure = mediator.getFigure(startField);

        return startFigure.getClass() == King.class &&
                Math.abs(startField.getYCoord() - finishField.getYCoord()) > 1;
    }

    @Override
    public boolean isCorrect(Color colorFigure, IPossibleActionList possibleActionList) {
        Collection<IField> forCastling = new ArrayList<>();

        int dif = startFieldKing.getYCoord() < finishFieldKing.getYCoord() ? 1 : -1;

        forCastling.add(startFieldKing);
        forCastling.add(board.getField(startFieldKing.getXCoord(), startFieldKing.getYCoord() + dif));
        forCastling.add(finishFieldKing);

        KingIsAttackedComputation kingIsAttackedComputation = new KingIsAttackedComputation(possibleActionList, mediator);
        for (IField tempField : forCastling) {
            if (kingIsAttackedComputation.fieldIsAttacked(tempField, colorFigure)) return false;
        }
        return true;
    }
}
