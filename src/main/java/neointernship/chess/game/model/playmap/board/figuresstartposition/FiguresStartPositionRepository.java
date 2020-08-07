package neointernship.chess.game.model.playmap.board.figuresstartposition;

import neointernship.chess.game.model.enums.ChessType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class FiguresStartPositionRepository {
    private final HashMap<ChessType, String> figuresPositionMap;
    private final int size = 8;

    private static final String CLASSIC_CHESS_FIGURES_POSITION = "" +
            "RNBQKBNR" +
            "PPPPPPPP" +
            "********" +
            "********" +
            "********" +
            "********" +
            "PPPPPPPP" +
            "RNBKQBNR";

    private static final String FISCHER_CHESS_FIGURES_POSITION = ""; // TODO


    public FiguresStartPositionRepository() {
        figuresPositionMap = new HashMap<ChessType, String>() {
            {
                put(ChessType.CLASSIC, CLASSIC_CHESS_FIGURES_POSITION);
                put(ChessType.FISCHER, FISCHER_CHESS_FIGURES_POSITION);
            }
        };
    }

    public Character[][] getStartPosition(final ChessType chessType, String string) {
        final Character[][] startPosition = new Character[size][size];

        String board = "";
        final String firstLine = chessType == ChessType.CLASSIC ? figuresForClassicChess() : figuresForFischerChess();

        board = firstLine.toLowerCase() + pawns().toLowerCase() + voids() + pawns() + firstLine;
        if (chessType == ChessType.OTHER) board = string;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                startPosition[i][j] = board.charAt(i * size + j);
            }
        }

        return startPosition;
    }

    private String pawns() {
        return "PPPPPPPP";
    }

    private String voids() {
        return "................................";
    }

    private String figuresForClassicChess() {
        return "RNBQKBNR";
    }

    /**
     * Метод генерирует строку с расстановкой фигур для игры шахмат Фишера
     *
     * @return строка расстановки последнего ряда
     */
    private String figuresForFischerChess() {
        final Random random = new Random();
        final char[] figures = new char[size];
        int position = Math.abs(random.nextInt() % (size - 1) + 1);

        final HashSet<Integer> positions = new HashSet<>();
        for (int i = 0; i < size; i++) positions.add(i);

        while (!positions.contains(position)) position = Math.abs(random.nextInt() % (size - 1) + 1);
        figures[(position % (size - 1)) + 1] = 'K';
        int crutch = (position % (size - 1)) + 1;
        positions.remove((position % (size - 1)) + 1);

        while (!positions.contains(position) || !(position < crutch)) position = Math.abs(random.nextInt() % size);
        figures[position] = 'R';
        positions.remove(position);

        while (!positions.contains(position) || !(position > crutch)) position = Math.abs(random.nextInt() % size);
        figures[position] = 'R';
        positions.remove(position);

        while (!positions.contains(position % size)) position = Math.abs(random.nextInt() % size);
        figures[position] = 'B';
        crutch = position;
        positions.remove(position);

        while (!positions.contains(position) || (position - crutch) % 2 == 1)
            position = Math.abs(random.nextInt() % size);
        figures[position % size] = 'B';
        positions.remove(position % size);

        while (!positions.contains(position % size)) position = Math.abs(random.nextInt() % size);
        figures[position % size] = 'Q';
        positions.remove(position % size);

        while (!positions.contains(position % size)) position = Math.abs(random.nextInt() % size);
        figures[position % size] = 'N';
        positions.remove(position % size);

        figures[(int) positions.toArray()[0]] = 'N';
        return String.valueOf(figures);
    }
}
