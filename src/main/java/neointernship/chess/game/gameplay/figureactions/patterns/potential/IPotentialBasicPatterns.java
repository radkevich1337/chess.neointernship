package neointernship.chess.game.gameplay.figureactions.patterns.potential;

import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.chess.game.model.playmap.field.IField;
import neointernship.chess.game.story.IStoryGame;

import java.util.ArrayList;
import java.util.List;

public interface IPotentialBasicPatterns {
    ArrayList<IField> getDiagonalFields(final Figure figure);

    ArrayList<IField> getHorizonVerticalFields(final Figure figure);

    ArrayList<IField> getKnightFields(final Figure knight);

    ArrayList<IField> getPawnFields(final Figure pawn);

    ArrayList<IField> getKingFields(final Figure king);

    void clearAttackList();

    List<Figure> getAttackList();

     int getBoardSize();

    IMediator getMediator();

    IBoard getBoard();

    IStoryGame getStoryGame();
}
