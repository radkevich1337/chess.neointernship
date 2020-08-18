package neointernship.chess.game.gameplay.figureactions.patterns.real;

import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.chess.game.model.playmap.field.IField;
import neointernship.chess.game.story.IStoryGame;

import java.util.Collection;

public interface IRealBasicPatterns {
    Collection<IField> getRealMoveList(final Figure figure, final Collection<IField> potentialMoveList);

    IMediator getMediator();

    IBoard getBoard();

    IStoryGame getStoryGame();
}
