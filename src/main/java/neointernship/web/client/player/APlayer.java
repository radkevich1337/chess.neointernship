package neointernship.web.client.player;

import neointernship.chess.game.model.answer.IAnswer;
import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.enums.EnumGameState;
import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.chess.game.model.playmap.field.IField;
import neointernship.chess.game.story.IStoryGame;
import neointernship.chess.game.story.StoryGame;
import neointernship.web.client.communication.message.ChessCodesReaction;
import neointernship.web.client.communication.message.ClientCodes;
import neointernship.web.client.communication.message.TurnStatus;

public abstract class APlayer {
    protected IMediator mediator;
    private IBoard board;
    private Color color;
    protected IStoryGame storyGame;
    private final String name;
    private ChessCodesReaction chessCodesReaction;

    public APlayer(final Color color, final String name) {
        this.color = color;
        this.name = name;
    }

    public void init(final IMediator mediator, final IBoard board, final Color color) {
        this.mediator = mediator;
        this.mediator.updateMediator();

        this.board = board;
        this.color = color;
        this.storyGame = new StoryGame(mediator);
        this.chessCodesReaction = new ChessCodesReaction(board, mediator);
    }

    public abstract String getAnswer() throws InterruptedException;

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public void updateMediator(final IAnswer answer, final TurnStatus turnStatus) throws InterruptedException {
        final IField startField = board.getField(answer.getStartX(), answer.getStartY());
        final Figure startFigure = mediator.getFigure(startField);
        storyGame.update(startFigure);

        chessCodesReaction.get(turnStatus).execute(answer);
    }

    public IMediator getMediator() {
        return mediator;
    }

    public IBoard getBoard() {
        return board;
    }

    public abstract char getTransformation() throws InterruptedException;

    public abstract ClientCodes getHandShakeAnswer() throws InterruptedException;

    public abstract void endGame(final EnumGameState enumGameState, final Color color) throws InterruptedException;
}
