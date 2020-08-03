package neointernship.web.client.player;

import neointernship.chess.game.gameplay.figureactions.IPossibleActionList;
import neointernship.chess.game.gameplay.figureactions.PossibleActionList;
import neointernship.chess.game.model.answer.IAnswer;
import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.enums.EnumGameState;
import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.chess.game.model.playmap.field.IField;
import neointernship.web.client.GUI.Input.IInput;
import neointernship.web.client.GUI.board.view.BoardView;
import neointernship.web.client.communication.message.ClientCodes;
import neointernship.web.client.communication.message.TurnStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomBot extends APlayer {

    private BoardView boardView;
    private IPossibleActionList possibleActionList;
    private final Random random;
    private final IInput input;

    public RandomBot(final Color color, final String name, final IInput input) {
        super(color, name);
        this.random = new Random();
        this.input = input;
    }

    public void init(final IMediator mediator, final IBoard board, final Color color) {
        super.init(mediator, board, color);
        this.possibleActionList = new PossibleActionList(board, mediator, storyGame);

        this.boardView = new BoardView(mediator, board);
        if (!input.isVoid()) boardView.display();
    }

    @Override
    public void updateMediator(final IAnswer answer, final TurnStatus turnStatus) throws InterruptedException {
        super.updateMediator(answer, turnStatus);
        if (!input.isVoid()) {
            boardView.update();
            boardView.display();
        }
    }

    @Override
    public String getAnswer() {

        final List<Figure> figures = (List<Figure>) mediator.getFigures(getColor());
        List<IField> fields;
        Figure figure;
        int index;
        String turn = "";

        final List<Character> integers = Arrays.asList('8', '7', '6', '5', '4', '3', '2', '1');
        final List<Character> chars = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');

        possibleActionList.updateRealLists();

        do {
            index = random.nextInt(figures.size());
            figure = figures.get(index);
            fields = (List<IField>) possibleActionList.getRealList(figure);
        } while (fields.isEmpty());

        index = random.nextInt(fields.size());
        final IField finalField = fields.get(index);

        final IField startField = mediator.getField(figure);

        turn += turn + chars.get(startField.getYCoord()) + integers.get(startField.getXCoord()) + "-" +
                chars.get(finalField.getYCoord()) + integers.get(finalField.getXCoord());

        return turn;
    }

    @Override
    public char getTransformation() {
        return 'Q';
    }

    @Override
    public ClientCodes getHandShakeAnswer() throws InterruptedException {
        input.getHandShakeAnswer();
        return ClientCodes.YES;
    }

    @Override
    public void endGame(final EnumGameState enumGameState, final Color color) throws InterruptedException {
        input.endGame(enumGameState, color);
        boardView.dispose();
    }

}
