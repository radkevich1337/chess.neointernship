package neointernship.chess.game.gameplay.moveaction;

import neointernship.chess.game.gameplay.figureactions.IPossibleActionList;
import neointernship.chess.game.model.answer.IAnswer;
import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.enums.MoveState;
import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.chess.game.model.playmap.field.IField;

import java.util.Collection;

public class MoveCorrectnessValidator {
    private final IMediator mediator;
    private final IPossibleActionList possibleActionList;
    private final IBoard board;

    public MoveCorrectnessValidator(final IMediator mediator,
                                    IPossibleActionList possibleActionList,
                                    final IBoard board) {
        this.mediator = mediator;
        this.possibleActionList = possibleActionList;
        this.board = board;
    }

    public MoveState check(final Color color, final IAnswer answer) {

        IField startField = board.getField(answer.getStartX(), answer.getStartY());
        IField finalField = board.getField(answer.getFinalX(), answer.getFinalY());
        Figure startFigure = mediator.getFigure(startField);

        if (startFigure == null || startFigure.getColor() != color) {
            return MoveState.RESTRICT;
        }

        for (Figure currentFigure : mediator.getFigures()) {
            if (currentFigure == startFigure) {

                if (possibleActionList.getRealList(currentFigure) == null) possibleActionList.updateRealLists();
                Collection<IField> fields = possibleActionList.getRealList(currentFigure);

                if (fields.contains(finalField)) {
                    return MoveState.ALLOWED;
                }
            }
        }

        return MoveState.RESTRICT;
    }
}
