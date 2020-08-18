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
        Integer[][] map = {{100, 100, 100, 100, 100, 100, 100, 100},
                {10, 10, 10, 10, 10, 10, 10, 10},
                {-15, -10, 10, 20, 20, 10, -10, -15},
                {-20, -15, 10, 30, 30, 10, -15, -20},
                {-20, -15, 10, 30, 30, 10, -15, -20},
                {-15, -10, 10, 20, 20, 10, -10, -15},
                {10, 10, 10, 10, 10, 10, 10, 10},
                {100, 100, 100, 100, 100, 100, 100, 100}};
        boards.put(Pawn.class, map);
    }

    private void addKnight() {
        Integer[][] map = {{-20, 10, 0, 0, 0, 0, 10, -20},
                {-10, 10, 20, 20, 20, 20, 10, -10},
                {-10, 20, 25, 30, 30, 25, 20, -10},
                {-10, 20, 30, 40, 40, 30, 20, -10},
                {-10, 20, 30, 40, 40, 30, 20, -10},
                {-10, 20, 25, 30, 30, 25, 20, -10},
                {-10, 10, 20, 20, 20, 20, 10, -10},
                {-20, 10, 0, 0, 0, 0, 10, -20}};
        boards.put(Knight.class, map);
    }

    private void addBishop() {
        Integer[][] map = {{-20, -10, -10, -10, -10, -10, -10, -20},
                {-10, 0, 0, 0, 0, 0, 0, -10},
                {-10, 0, 20, 20, 20, 20, 0, -10},
                {-10, 0, 20, 20, 20, 20, 0, -10},
                {-10, 0, 20, 20, 20, 20, 0, -10},
                {-10, 0, 20, 20, 20, 20, 0, -10},
                {-10, 0, 0, 0, 0, 0, 0, -10},
                {-20, -10, -10, -10, -10, -10, -10, -20}};
        boards.put(Bishop.class, map);
    }

    private void addRook() {
        Integer[][] map = {{10, -10, 0, 0, 0, 0, -10, 10},
                {-10, 10, 10, 10, 10, 10, 10, -10},
                {-10, 0, 0, 0, 0, 0, 0, -10},
                {-10, 0, 0, 0, 0, 0, 0, -10},
                {-10, 0, 0, 0, 0, 0, 0, -10},
                {-10, 0, 0, 0, 0, 0, 0, -10},
                {-10, 10, 10, 10, 10, 10, 10, -10},
                {10, -10, 0, 0, 0, 0, -10, 10}};
        boards.put(Rook.class, map);
    }

    private void addQueen() {
        Integer[][] map = {{-20, -10, -10, 0, 0, -10, -10, -20},
                {-10, 0, 0, 0, 0, 0, 0, -10},
                {-10, 0, 20, 20, 20, 20, 0, -10},
                {-10, 0, 20, 20, 20, 20, 0, -10},
                {-10, 0, 20, 20, 20, 20, 0, -10},
                {-10, 0, 20, 20, 20, 20, 0, -10},
                {-10, 0, 0, 0, 0, 0, 0, -10},
                {-20, -10, -10, 0, 0, -10, -10, -20}};
        boards.put(Queen.class, map);
    }

    private void addKing() {
        Integer[][] map = {{10, 10, 30, 0, 0, 0, 30, 10},
                {5, 5, -10, -10, -10, -10, 5, 5},
                {10, 0, -20, -20, -20, -20, 0, 10},
                {0, -10, -20, -20, -20, -20, -10, 0},
                {0, -10, -20, -20, -20, -20, -10, 0},
                {10, 0, -20, -20, -20, -20, 0, 10},
                {5, 5, -10, -10, -10, -10, 5, 5},
                {10, 10, 30, 0, 0, 0, 30, 10}};
        boards.put(King.class, map);
    }

    public Integer[][] getBoard(Figure figure) {
        return boards.get(figure.getClass());
    }
}
