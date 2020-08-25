package neointernship.web.client.AI;

import neointernship.chess.game.model.figure.piece.*;
import neointernship.chess.game.model.playmap.field.IField;

import java.util.HashMap;

public class BoardPrice {
    private final int SIZE = 8;
    private final HashMap<Class, Integer[][]> boards;

    public BoardPrice() {
        boards = new HashMap<>();
        addPawn();
        addBishop();
        addKnight();
        addRook();
        addQueen();
        addKing();
    }

    private void addPawn() {
        Integer[][] map = {{300, 300, 300, 300, 300, 300, 300, 300},
                {10, 10, 10, 10, 10, 10, 10, 10},
                {-20, -20, -20, 20, 20, -20, -20, -20},
                {-20, -20, -20, 30, 30, -20, -20, -20},
                {-20, -20, -20, 30, 30, -20, -20, -20},
                {-20, -20, -20, 20, 20, -20, -20, -20},
                {10, 10, 10, 10, 10, 10, 10, 10},
                {300, 300, 300, 300, 300, 300, 300, 300}};
        boards.put(Pawn.class, map);
    }

    private void addKnight() {
        Integer[][] map = {{-40, 10, 0, 0, 0, 0, 10, -40},
                {-40, 0, 10, 10, 10, 10, 0, -40},
                {-40, 20, 25, 30, 30, 25, 20, -40},
                {-40, 20, 30, 40, 40, 30, 20, -40},
                {-40, 20, 30, 40, 40, 30, 20, -40},
                {-40, 20, 25, 30, 30, 25, 20, -40},
                {-40, 0, 10, 10, 10, 10, 0, -40},
                {-40, 10, 0, 0, 0, 0, 10, -40}};
        boards.put(Knight.class, map);
    }

    private void addBishop() {
        Integer[][] map = {{-20, -20, 10, -20, -20, 10, -20, -20},
                {-20, 0, 0, 0, 0, 0, 0, -20},
                {-10, 0, 20, 20, 20, 20, 0, -10},
                {-10, 0, 20, 20, 20, 20, 0, -10},
                {-10, 0, 20, 20, 20, 20, 0, -10},
                {-10, 0, 20, 20, 20, 20, 0, -10},
                {-20, 0, 0, 0, 0, 0, 0, -20},
                {-20, -20, 10, -20, -20, 10, -20, -20}};
        boards.put(Bishop.class, map);
    }

    private void addRook() {
        Integer[][] map = {{10, -10, 0, 0, 0, 0, -10, 10},
                {-20, 10, 10, 10, 10, 10, 10, -20},
                {-20, 0, 0, 0, 0, 0, 0, -20},
                {-20, 0, 0, 0, 0, 0, 0, -20},
                {-20, 0, 0, 0, 0, 0, 0, -20},
                {-20, 0, 0, 0, 0, 0, 0, -20},
                {-20, 10, 10, 10, 10, 10, 10, -20},
                {10, -10, 0, 0, 0, 0, -10, 10}};
        boards.put(Rook.class, map);
    }

    private void addQueen() {
        Integer[][] map = {{-20, -10, -10, 10, 0, -10, -10, -20},
                {-10, 0, 0, 0, 0, 0, 0, -10},
                {-10, 0, 20, 10, 20, 20, 0, -10},
                {-10, 0, 20, 20, 20, 20, 0, -10},
                {-10, 0, 20, 20, 20, 20, 0, -10},
                {-10, 0, 20, 10, 20, 20, 0, -10},
                {-10, 0, 0, 0, 0, 0, 0, -10},
                {-20, -10, -10, 10, 0, -10, -10, -20}};
        boards.put(Queen.class, map);
    }

    private void addKing() {
        Integer[][] map = {{10, 10, 30, 0, 10, 0, 30, 10},
                {5, 5, -10, -30, -30, -30, 5, 5},
                {10, 0, -20, -20, -20, -20, 0, 10},
                {0, -10, -20, -20, -20, -20, -10, 0},
                {0, -10, -20, -20, -20, -20, -10, 0},
                {10, 0, -20, -20, -20, -20, 0, 10},
                {5, 5, -10, -30, -30, -30, 5, 5},
                {10, 10, 30, 0, 10, 0, 30, 10}};
        boards.put(King.class, map);
    }

    public Integer[][] getBoard(Figure figure) {
        return boards.get(figure.getClass());
    }
}
