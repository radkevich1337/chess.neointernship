package neointernship.chess.game.gameplay.kingstate.update;

import neointernship.chess.game.gameplay.figureactions.IPossibleActionList;
import neointernship.chess.game.gameplay.figureactions.PossibleActionList;
import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.mediator.Mediator;
import neointernship.chess.game.model.playmap.field.IField;

import java.util.Collection;

public final class KingIsAttackedComputation {
    private final IPossibleActionList possibleActionList;
    private final IMediator mediator;

    public KingIsAttackedComputation(final IPossibleActionList possibleActionList,
                                     final IMediator mediator) {
        this.possibleActionList = possibleActionList;
        this.mediator = mediator;
    }

    public KingIsAttackedComputation(KingIsAttackedComputation kingIsAttackedComputation) {
        this.mediator = new Mediator(kingIsAttackedComputation.mediator);
        this.possibleActionList = new PossibleActionList(kingIsAttackedComputation.possibleActionList);
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

    public int fieldIsAttacked(IField field, Color color) {
        Color opponentColor = Color.swapColor(color);
        int count = 0;

        for (Figure figure : mediator.getFigures(opponentColor)) {
            Collection<IField> collection = possibleActionList.getPotentialList(figure);
            if (collection.contains(field)) {
                count++;
            }
        }
        return count;
    }
}
