package neointernship.chess.game.gameplay.figureactions;

import neointernship.chess.game.gameplay.figureactions.patterns.potential.IPotentialBasicPatterns;
import neointernship.chess.game.model.figure.piece.*;
import neointernship.chess.game.model.playmap.field.IField;

import java.util.ArrayList;

public class Intermediary {
    public static ArrayList<IField> getList(final Figure figure, final IPotentialBasicPatterns basicAttackPatterns) {
        ArrayList<IField> list = new ArrayList<>();

        if (Bishop.class.equals(figure.getClass())) {
            return basicAttackPatterns.getDiagonalFields(figure);

        } else if (Queen.class.equals(figure.getClass())) {
            list.addAll(basicAttackPatterns.getDiagonalFields(figure));
            list.addAll(basicAttackPatterns.getHorizonVerticalFields(figure));
            return list;

        } else if (Pawn.class.equals(figure.getClass())) {
            return basicAttackPatterns.getPawnFields(figure);

        } else if (Rook.class.equals(figure.getClass())) {
            return basicAttackPatterns.getHorizonVerticalFields(figure);

        } else if (King.class.equals(figure.getClass())) {
            return basicAttackPatterns.getKingFields(figure);

        } else if (Knight.class.equals(figure.getClass())) {
            return basicAttackPatterns.getKnightFields(figure);
        }
        return list;
    }
}
