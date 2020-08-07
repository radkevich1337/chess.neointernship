package neointernship.chess.game.gameplay.figureactions;

import neointernship.chess.game.gameplay.figureactions.patterns.potential.IPotentialBasicPatterns;
import neointernship.chess.game.model.figure.piece.*;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.field.IField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Intermediary {
    static HashMap<Figure, List<Figure>> attackMap = new HashMap<>();

    public static ArrayList<IField> getList(final Figure figure, final IPotentialBasicPatterns basicAttackPatterns) {
        ArrayList<IField> list = new ArrayList<>();

        if (Bishop.class.equals(figure.getClass())) {
            basicAttackPatterns.clearAttackList();
            list = basicAttackPatterns.getDiagonalFields(figure);
            attackMap.put(figure, basicAttackPatterns.getAttackList());
            return list;

        } else if (Queen.class.equals(figure.getClass())) {
            basicAttackPatterns.clearAttackList();
            list.addAll(basicAttackPatterns.getDiagonalFields(figure));
            list.addAll(basicAttackPatterns.getHorizonVerticalFields(figure));
            attackMap.put(figure, basicAttackPatterns.getAttackList());
            return list;

        } else if (Pawn.class.equals(figure.getClass())) {
            basicAttackPatterns.clearAttackList();
            list = basicAttackPatterns.getPawnFields(figure);
            attackMap.put(figure, basicAttackPatterns.getAttackList());
            return list;

        } else if (Rook.class.equals(figure.getClass())) {
            basicAttackPatterns.clearAttackList();
            list = basicAttackPatterns.getHorizonVerticalFields(figure);
            attackMap.put(figure, basicAttackPatterns.getAttackList());
            return list;

        } else if (King.class.equals(figure.getClass())) {
            basicAttackPatterns.clearAttackList();
            list = basicAttackPatterns.getKingFields(figure);
            attackMap.put(figure, basicAttackPatterns.getAttackList());
            return list;

        } else if (Knight.class.equals(figure.getClass())) {
            basicAttackPatterns.clearAttackList();
            list = basicAttackPatterns.getKnightFields(figure);
            attackMap.put(figure, basicAttackPatterns.getAttackList());
            return list;
        }
        return list;
    }

    public static HashMap<Figure, List<Figure>> getAttackList() {
        return attackMap;
    }
}
