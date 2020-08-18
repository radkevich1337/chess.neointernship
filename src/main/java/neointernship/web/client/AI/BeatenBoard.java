package neointernship.web.client.AI;

import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.figure.piece.*;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.chess.game.model.playmap.field.IField;

import java.util.Collection;
import java.util.HashMap;

public class BeatenBoard {
    private static HashMap<IField, Float> beatenBoard;
    private static HashMap<Class, Float> figureCost;

    private BeatenBoard() {
        beatenBoard = new HashMap<>();
        figureCost = new HashMap<>();

        figureCost.put(Queen.class, 5 - 3.03f);
        figureCost.put(Rook.class, 5 - 2.68f);
        figureCost.put(Bishop.class, 5 - 2.54f);
        figureCost.put(Knight.class, 5 - 2.46f);
        figureCost.put(Pawn.class, 5 - 2f);
        figureCost.put(King.class, 5 - 4f);
    }

    public static void init(IBoard board){
        if (beatenBoard == null) new BeatenBoard();
        beatenBoard.clear();

        for (int i = 0; i < board.getSize(); i++){
            for (int j = 0; j < board.getSize(); j++){
                beatenBoard.put(board.getField(i, j), 0.f);
            }
        }
    }

    public static void addBeatenFields(Collection<IField> fields, IMediator mediator, Color color) {
        for (IField field : fields) {
            Figure figure = mediator.getFigure(field);
            if (figure != null){
                float count = beatenBoard.get(field);
                //float cost = figureCost.get(figure.getClass());
                //count += color == Color.WHITE ? cost : -1 * cost;
                count += color == Color.WHITE ? 1 : -1;
                beatenBoard.put(field, count);
            }

        }
    }

    public static Float getBeatenFieldCount(IField field) {
        assert (beatenBoard.get(field) != null);
        assert (field != null);
        return beatenBoard.get(field);
    }
}
