package neointernship.chess.game.gameplay.figureactions;

import neointernship.chess.game.gameplay.figureactions.patterns.potential.IPotentialBasicPatterns;
import neointernship.chess.game.gameplay.figureactions.patterns.real.IRealBasicPatterns;
import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.chess.game.model.playmap.field.IField;
import neointernship.chess.game.story.IStoryGame;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IPossibleActionList {
    Collection<IField> getPotentialList(final Figure figure);

    void updatePotentialLists();

    /**
     * обновляет список потенциальных ходов для фигур цвета color
     *
     * @param color
     */
    void updatePotentialLists(final Color color);

    Collection<IField> getRealList(final Figure figure);

    void updateRealLists();

    IMediator getMediator();

    IBoard getBoard();

    IPotentialBasicPatterns getPotentialPatterns();

    IRealBasicPatterns getRealPatterns();

    Map<Figure, Collection<IField>> getRealFigureActions();

    Map<Figure, Collection<IField>> getPotentialFigureAction();

    Map<Figure, List<Figure>> getAttackList();

    IStoryGame getStoryGame();
}

