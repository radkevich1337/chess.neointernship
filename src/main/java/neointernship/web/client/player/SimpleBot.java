package neointernship.web.client.player;

import neointernship.chess.game.gameplay.figureactions.IPossibleActionList;
import neointernship.chess.game.gameplay.figureactions.PossibleActionList;
import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.enums.EnumGameState;
import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.chess.game.model.playmap.field.IField;
import neointernship.web.client.GUI.Input.IInput;
import neointernship.web.client.communication.message.ClientCodes;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SimpleBot extends APlayer {
    private final IBoard board;
    private final IMediator mediator;
    private final IPossibleActionList possibleActionList;

    private final Random random;

    private final IInput input;

    public SimpleBot(IBoard board, IMediator mediator, PossibleActionList list, Color color, String name,final IInput input) {
        super(color, name);
        this.board = board;
        this.mediator = mediator;
        this.possibleActionList = list;

        this.random = new Random();

        this.input = input;
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

        do {
            index = random.nextInt(figures.size());
            figure = figures.get(index);
            fields = (List<IField>) possibleActionList.getRealList(figure);
        } while (fields.isEmpty());

        index = random.nextInt(fields.size());
        final IField finishField = fields.get(index);

        final IField startField = mediator.getField(figure);

        turn += turn + chars.get(startField.getYCoord()) + integers.get(startField.getXCoord()) + "-" +
                chars.get(finishField.getYCoord()) + integers.get(finishField.getXCoord());

        return turn;
    }

    @Override
    public char getTransformation() throws InterruptedException {
        return 'Q';
    }

    @Override
    public ClientCodes getHandShakeAnswer() throws InterruptedException {
        return ClientCodes.YES;
    }

    @Override
    public void endGame(EnumGameState enumGameState, Color color) {
        try {
            input.endGame(enumGameState,color);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
