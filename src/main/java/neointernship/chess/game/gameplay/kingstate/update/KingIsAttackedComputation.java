package neointernship.chess.game.gameplay.kingstate.update;

import neointernship.chess.game.gameplay.figureactions.IPossibleActionList;
import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.board.Board;
import neointernship.chess.game.model.playmap.field.IField;

import java.util.Collection;
import java.util.Objects;

public final class KingIsAttackedComputation {
    private final IPossibleActionList possibleActionList;
    private final IMediator mediator;

    public KingIsAttackedComputation(final IPossibleActionList possibleActionList,
                                     final IMediator mediator) {
        this.possibleActionList = possibleActionList;
        this.mediator = mediator;
    }

    public boolean kingIsAttacked(Color color) {
        Figure king = mediator.getKing(color);
        IField kingField = mediator.getField(king);

        Color opponentColor = Color.swapColor(color);
        for (Figure figure : mediator.getFigures(opponentColor)) {
            Collection<IField> collection = possibleActionList.getPotentialList(figure);
            if (collection.contains(kingField)) {
                return true;
            }
        }

        return false;
    }
}
