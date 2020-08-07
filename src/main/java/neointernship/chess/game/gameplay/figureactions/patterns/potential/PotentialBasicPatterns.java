package neointernship.chess.game.gameplay.figureactions.patterns.potential;

import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.figure.piece.Pawn;
import neointernship.chess.game.model.figure.piece.Rook;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.mediator.Mediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.chess.game.model.playmap.field.Field;
import neointernship.chess.game.model.playmap.field.IField;
import neointernship.chess.game.story.IStoryGame;
import neointernship.chess.game.story.StoryGame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PotentialBasicPatterns implements IPotentialBasicPatterns {
    private final int boardSize;
    private final IMediator mediator;
    private final IBoard board;
    private final IStoryGame storyGame;
    private List<Figure> attackList;

    public PotentialBasicPatterns(final IMediator mediator, final IBoard board, final IStoryGame storyGame) {
        this.mediator = mediator;
        this.board = board;
        boardSize = board.getSize();
        this.storyGame = storyGame;
        this.attackList = new ArrayList<>();
    }

    public PotentialBasicPatterns(PotentialBasicPatterns potentialBasicPatterns) {
        this.boardSize = potentialBasicPatterns.boardSize;
        this.mediator = new Mediator(potentialBasicPatterns.mediator);
        this.board = potentialBasicPatterns.board;
        this.storyGame = new StoryGame(potentialBasicPatterns.storyGame);
        this.attackList = potentialBasicPatterns.attackList;
    }

    public ArrayList<IField> getDiagonalFields(final Figure figure) {
        final ArrayList<IField> possibleAttackFields = new ArrayList<>();
        final IField currentField = mediator.getField(figure);
        boolean keepRightDownMove = true;
        boolean keepLeftUpMove = true;
        for (int i = 1; i < boardSize; i++) {
            if (keepRightDownMove && currentField.getXCoord() < boardSize && currentField.getYCoord() < boardSize) {
                keepRightDownMove = actionToAdd(figure.getColor(), currentField.getXCoord() + i, currentField.getYCoord() + i, possibleAttackFields);
            }

            if (keepLeftUpMove && currentField.getXCoord() > 0 && currentField.getYCoord() > 0) {
                keepLeftUpMove = actionToAdd(figure.getColor(), currentField.getXCoord() - i, currentField.getYCoord() - i, possibleAttackFields);
            }
        }

        boolean keepRightUpMove = true;
        boolean keepLeftDownMove = true;
        for (int i = 1; i < boardSize; i++) {
            if (keepRightUpMove && currentField.getXCoord() > 0 && currentField.getYCoord() < boardSize) {
                keepRightUpMove = actionToAdd(figure.getColor(), currentField.getXCoord() - i, currentField.getYCoord() + i, possibleAttackFields);
            }

            if (keepLeftDownMove && currentField.getXCoord() < boardSize && currentField.getYCoord() > 0) {
                keepLeftDownMove = actionToAdd(figure.getColor(), currentField.getXCoord() + i, currentField.getYCoord() - i, possibleAttackFields);
            }
        }

        return possibleAttackFields;
    }

    public ArrayList<IField> getHorizonVerticalFields(final Figure figure) {
        final ArrayList<IField> possibleAttackFields = new ArrayList<>();

        final IField currentField = mediator.getField(figure);

        boolean keepRightMove = true;
        boolean keepLeftMove = true;
        for (int i = 1; i < boardSize; i++) {
            if (keepRightMove) {
                keepRightMove = actionToAdd(figure.getColor(), currentField.getXCoord(), currentField.getYCoord() + i, possibleAttackFields);
            }
            if (keepLeftMove) {
                keepLeftMove = actionToAdd(figure.getColor(), currentField.getXCoord(), currentField.getYCoord() - i, possibleAttackFields);
            }
        }

        boolean keepUpMove = true;
        boolean keepDownMove = true;
        for (int i = 1; i < boardSize; i++) {
            if (keepUpMove) {
                keepUpMove = actionToAdd(figure.getColor(), currentField.getXCoord() - i, currentField.getYCoord(), possibleAttackFields);
            }
            if (keepDownMove) {
                keepDownMove = actionToAdd(figure.getColor(), currentField.getXCoord() + i, currentField.getYCoord(), possibleAttackFields);
            }
        }
        return possibleAttackFields;
    }

    public ArrayList<IField> getKnightFields(final Figure figure) {
        final ArrayList<IField> possibleAttackFields = new ArrayList<>();

        final IField currentField = mediator.getField(figure);

        final int[] onesList = new int[]{1, -1};
        final int[] twosList = new int[]{2, -2};

        for (final int one : onesList) {
            for (final int two : twosList) {
                actionToAdd(figure.getColor(),
                        currentField.getXCoord() + one,
                        currentField.getYCoord() + two,
                        possibleAttackFields);
                actionToAdd(figure.getColor(),
                        currentField.getXCoord() + two,
                        currentField.getYCoord() + one,
                        possibleAttackFields);
            }
        }

        return possibleAttackFields;
    }

    public ArrayList<IField> getPawnFields(final Figure figure) {
        final ArrayList<IField> possibleAttackFields = new ArrayList<>();
        final IField currentField = mediator.getField(figure);

        int offset = (figure.getColor() == Color.BLACK) ? 1 : -1;

        final int[] onesList = new int[]{1, -1};
        for (final int one : onesList) {
            addAttackField(figure.getColor(), currentField.getXCoord() + offset, currentField.getYCoord() + one, possibleAttackFields);
        }

        boolean isFreePath = addMoveField(currentField.getXCoord() + offset,
                currentField.getYCoord(),
                possibleAttackFields);

        addIfAisleTake(figure, possibleAttackFields);

        if (isFreePath) {
            if (currentField.getXCoord() == 1 || currentField.getXCoord() == 6) {
                offset *= 2;
                addMoveField(currentField.getXCoord() + offset, currentField.getYCoord(), possibleAttackFields);
            }
        }

        // для превращения ( только для изменения из пешки в другую фигуру)
        if (currentField.getXCoord() == 0 || currentField.getXCoord() == 7) {
            IField field = board.getField(currentField.getXCoord(), currentField.getYCoord());
            possibleAttackFields.add(field);
        }

        return possibleAttackFields;
    }

    private void addIfAisleTake(final Figure pawn, final Collection<IField> possibleAttackFields) {
        final IField currentField = mediator.getField(pawn);
        final Color color = pawn.getColor();
        final Figure lastFigure = storyGame.getLastFigureMove();

        int startXCoord = 3;
        int move = -1;
        if (color == Color.BLACK) {
            startXCoord = 4;
            move = 1;
        }

        final IField lastFieldLastFigure = storyGame.getLastField();
        final IField realFieldLastFigure = mediator.getField(lastFigure);

        if (realFieldLastFigure != null && lastFieldLastFigure != null) { // может быть null если было превращение
            if (currentField.getXCoord() == startXCoord && lastFigure.getColor() == Color.swapColor(color)) {
                if (lastFigure.getClass() == Pawn.class) {
                    if (Math.abs(realFieldLastFigure.getXCoord() - lastFieldLastFigure.getXCoord()) == 2) {
                        if (Math.abs(lastFieldLastFigure.getYCoord() - currentField.getYCoord()) == 1) {
                            possibleAttackFields.add(new Field(startXCoord + move, lastFieldLastFigure.getYCoord()));
                        }
                    }
                }
            }
        }
    }

    @Override
    public ArrayList<IField> getKingFields(final Figure king) {
        final ArrayList<IField> possibleAttackFields = new ArrayList<>();

        final IField fieldKing = mediator.getField(king);

        final int[] onesList = new int[]{1, -1};

        for (final int one : onesList) {
            for (final int two : onesList) {

                actionToAdd(king.getColor(),
                        fieldKing.getXCoord() + one,
                        fieldKing.getYCoord() + two,
                        possibleAttackFields);

            }
        }

        for (final int one : onesList) {
            actionToAdd(king.getColor(),
                    fieldKing.getXCoord() + one,
                    fieldKing.getYCoord(),
                    possibleAttackFields);

            actionToAdd(king.getColor(),
                    fieldKing.getXCoord(),
                    fieldKing.getYCoord() + one,
                    possibleAttackFields);
        }

        addIfCastling(king, fieldKing, possibleAttackFields);

        return possibleAttackFields;
    }

    private void addIfCastling(final Figure king, final IField fieldKing, final Collection<IField> possibleAttackFields) {
        // если король не ходил todo костыль
        if (!storyGame.isMove(king) && fieldKing.getYCoord() == 4) {

            for (final Figure rook : mediator.getFigures(king.getColor())) {
                // если есть ладья которой не ходил
                if (rook.getClass() == Rook.class && !storyGame.isMove(rook) && mediator.getField(rook).getXCoord() == fieldKing.getXCoord()) {
                    // если между ними нет других фигур
                    boolean haveFigure = false;
                    final IField fieldRook = mediator.getField(rook);
                    final int dif = fieldKing.getYCoord() < fieldRook.getYCoord() ? 1 : -1;

                    IField fieldTemp = board.getField(fieldKing.getXCoord(), fieldKing.getYCoord() + dif);

                    while (fieldTemp.getYCoord() != fieldRook.getYCoord()) {
                        if (mediator.getFigure(fieldTemp) != null) {
                            haveFigure = true;
                        }
                        fieldTemp = board.getField(fieldKing.getXCoord(), fieldTemp.getYCoord() + dif);
                    }
                    //если фигур нет
                    if (!haveFigure) {
                        final IField finalField = board.getField(fieldKing.getXCoord(), fieldKing.getYCoord() + (2 * dif));
                        possibleAttackFields.add(finalField);
                    }
                }
            }
        }
    }

    /**
     * This method checks if it needed to add a field to the piece's attack list.
     * It checks if the field coordinates are within the dimensions of the Board.
     * If the fields contains some piece that has opposite color, the field added to the list.
     *
     * @param newFieldYCoord   {@link Integer} column coordinate of the field being checked
     * @param possibleMoveList a list where we add the field if it needed.
     * @return boolean value if moving through direction is possible (not covered with another piece).
     */
    private boolean actionToAdd(final Color color, final int newFieldXCoord, final int newFieldYCoord, ArrayList<IField> possibleMoveList) {
        if (invalidCoordinates(newFieldXCoord, newFieldYCoord)) {
            return false;
        }
        final IField field = board.getField(newFieldXCoord, newFieldYCoord);
        final Figure figure = mediator.getFigure(field);

        addMoveField(newFieldXCoord, newFieldYCoord, possibleMoveList);
        addAttackField(color, newFieldXCoord, newFieldYCoord, possibleMoveList);

        return figure == null;
    }

    private boolean addMoveField(final int newFieldXCoord, final int newFieldYCoord, ArrayList<IField> possibleMoveList) {
        if (invalidCoordinates(newFieldXCoord, newFieldYCoord)) {
            return false;
        }

        final IField field = board.getField(newFieldXCoord, newFieldYCoord);
        final Figure figure = mediator.getFigure(field);

        if (figure == null) {
            possibleMoveList.add(field);
            return true;
        }
        return false;
    }

    private void addAttackField(final Color color, final int newFieldXCoord, final int newFieldYCoord, ArrayList<IField> possibleMoveList) {
        if (invalidCoordinates(newFieldXCoord, newFieldYCoord)) {
            return;
        }

        final IField field = board.getField(newFieldXCoord, newFieldYCoord);
        final Figure figure = mediator.getFigure(field);

        if (figure != null){
            if (figure.getColor() != color){
                possibleMoveList.add(field);
            }
            attackList.add(figure);
        }
    }

    private boolean invalidCoordinates(final int newFieldXCoord, final int newFieldYCoord) {
        return newFieldXCoord < 0
                || newFieldXCoord >= boardSize
                || newFieldYCoord < 0
                || newFieldYCoord >= boardSize;
    }

    public void clearAttackList(){
        attackList.clear();
    }

    public List<Figure> getAttackList() {
        return attackList;
    }
}
