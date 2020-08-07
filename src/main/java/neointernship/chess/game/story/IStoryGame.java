package neointernship.chess.game.story;

import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.field.IField;

import java.util.Set;

public interface IStoryGame {
    /**
     * Возвращает true если данной фигурой совершали ход
     *
     * @param figure
     * @return
     */
    boolean isMove(final Figure figure);

    /**
     * Возвращает фигуру, которой последний раз был совершён ход
     *
     * @return
     */
    Figure getLastFigureMove();

    /**
     * Возвращает поле, на котором стояла фигура перед последним её ходом.
     *
     * @return
     */
    IField getLastField();

    /**
     *
     */
    void update(final Figure figure);

    Set<Figure> getHoIsMove();

    IMediator getMediator();

    Figure getLastFigure();
}
