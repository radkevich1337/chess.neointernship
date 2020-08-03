package neointernship.chess.game.story;

import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.field.IField;

import java.util.HashSet;
import java.util.Set;

public class StoryGame implements IStoryGame {

    private final Set<Figure> hoIsMove;

    private final IMediator mediator;

    private Figure lastFigure;
    private IField lastField;

    // todo можешь существовать без Mediator ?
    public StoryGame(final IMediator mediator) {
        this.mediator = mediator;
        this.hoIsMove = new HashSet<>();
    }

    public StoryGame(StoryGame storyGame) {
        this.mediator = storyGame.mediator;
        this.hoIsMove = new HashSet<>();

        this.hoIsMove.addAll(storyGame.hoIsMove);
    }

    @Override
    public boolean isMove(final Figure figure) {
        return hoIsMove.contains(figure);
    }

    @Override
    public Figure getLastFigureMove() {
        return lastFigure;
    }

    @Override
    public IField getLastField() {
        return lastField;
    }

    @Override
    public void update(final Figure figure) {
        hoIsMove.add(figure);
        this.lastFigure = figure;
        lastField = mediator.getField(figure);
    }
}
