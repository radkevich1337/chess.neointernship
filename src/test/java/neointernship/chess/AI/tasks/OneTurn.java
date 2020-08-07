package neointernship.chess.AI.tasks;

import neointernship.chess.game.gameplay.activecolorcontroller.ActiveColorController;
import neointernship.chess.game.gameplay.activecolorcontroller.IActiveColorController;
import neointernship.chess.game.gameplay.figureactions.IPossibleActionList;
import neointernship.chess.game.gameplay.figureactions.PossibleActionList;
import neointernship.chess.game.gameplay.gameprocesscontroller.GameProcessController;
import neointernship.chess.game.gameplay.gameprocesscontroller.IGameProcessController;
import neointernship.chess.game.gameplay.gamestate.controller.GameStateController;
import neointernship.chess.game.gameplay.gamestate.controller.IGameStateController;
import neointernship.chess.game.gameplay.loop.GameLoop;
import neointernship.chess.game.gameplay.loop.IGameLoop;
import neointernship.chess.game.model.answer.AnswerSimbol;
import neointernship.chess.game.model.answer.IAnswer;
import neointernship.chess.game.model.enums.ChessType;
import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.enums.EnumGameState;
import neointernship.chess.game.model.figure.factory.Factory;
import neointernship.chess.game.model.figure.factory.IFactory;
import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.mediator.Mediator;
import neointernship.chess.game.model.playmap.board.Board;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.chess.game.model.playmap.board.figuresstartposition.FiguresStartPositionRepository;
import neointernship.chess.game.model.playmap.field.IField;
import neointernship.chess.game.story.IStoryGame;
import neointernship.chess.game.story.StoryGame;
import neointernship.web.client.communication.message.TurnStatus;
import neointernship.web.client.player.APlayer;
import neointernship.web.client.player.RandomBot;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OneTurn {
    private static APlayer firstPlayer;
    private static APlayer secondPlayer;

    private static IBoard board;
    private static IFactory figureFactory;
    private static IMediator mediator;
    private static IPossibleActionList possibleActionList;
    private static IStoryGame storyGame;

    private static ChessType chessTypes;
    private static FiguresStartPositionRepository figuresStartPositionRepository;
    private static Character FIELD_CHAR_EMPTY = '.';
    private static ActiveColorController activeColorController;
    private static Color color = Color.WHITE;
    private static Random random = new Random();

    private static IGameLoop gameLoop;

    private static int i = 0;

    @Before
    public void before() {
        firstPlayer = new RandomBot(Color.WHITE, "bot1");
        secondPlayer = new RandomBot(Color.BLACK, "bot2");

        board = new Board();
        figureFactory = new Factory();
        mediator = new Mediator();
        storyGame = new StoryGame(mediator);
        possibleActionList = new PossibleActionList(board, mediator, storyGame);
        chessTypes = ChessType.OTHER;
        figuresStartPositionRepository = new FiguresStartPositionRepository();
        activeColorController = new ActiveColorController();

        gameLoop = new GameLoop(mediator, possibleActionList, board, activeColorController, storyGame);
        System.out.println("!");
        i = 0;
    }

    private int H(IMediator mediator) {
        int sum = 0;
        for (Figure figure : mediator.getFigures()) {
            sum += figure.getColor() == Color.WHITE ? figure.getPrice() : -1 * figure.getPrice();
        }
        return sum;
    }

    private static void initGameMap(String string) {
        final Character[][] figuresRepository = figuresStartPositionRepository.getStartPosition(chessTypes, string);
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                final IField field = board.getField(i, j);

                final Character currentChar = figuresRepository[i][j];
                if (currentChar != FIELD_CHAR_EMPTY) {
                    final Color color = currentChar < 'a' ? Color.WHITE : Color.BLACK;
                    final Figure figure = figureFactory.createFigure(currentChar, color);

                    mediator.addNewConnection(field, figure);
                }
            }
        }
        possibleActionList.updateRealLists();
    }

    public void updateGameLoop() {
        IMediator newMediator = new Mediator(mediator);
        IStoryGame newStoryGame = new StoryGame(storyGame);
        IPossibleActionList newPossibleActionList = new PossibleActionList(board, newMediator, newStoryGame);

        gameLoop.setGameProcessController(new GameProcessController(newMediator, newPossibleActionList, board, storyGame));
        gameLoop.setGameStateController(new GameStateController(newPossibleActionList, newMediator, newStoryGame));
    }

    private class Pair{
        private int sum;
        private IAnswer answer;

        public Pair(int sum, IAnswer answer) {
            this.sum = sum;
            this.answer = answer;
        }

        public int getSum() {
            return sum;
        }

        public IAnswer getAnswer() {
            return answer;
        }
    }

    private Pair getAnswer(Color color, int height, IGameLoop gameLoop) {
        //List<GameLoop> gameLoops = new ArrayList<>();
        HashMap<Integer, Pair> map = new HashMap<>();
        //List<Pair> list = new ArrayList<>();
        /**/
        Pair pair;
        GameLoop copyGameLoop = null;
        IMediator copyMediator;
        IPossibleActionList copyPossibleActionList;
        IStoryGame copyStoryGame;
        IMediator mediator = gameLoop.getMediator();
        IPossibleActionList possibleActionList = new PossibleActionList(board, mediator, new StoryGame(gameLoop.getStoryGame()));//gameLoop.getPossibleActionList();
        possibleActionList.updateRealLists();
        IAnswer newAnswer = null;
        if (OneTurn.color == color) {
            for (Figure figure : mediator.getFigures(color)) {
                IField iField = mediator.getField(figure);
                ActiveColorController copyActiveColorController = new ActiveColorController(gameLoop.getActiveColorController());
                copyActiveColorController.update();
                for (IField field : possibleActionList.getRealList(figure)) {
                    i++;
                    copyMediator = new Mediator(gameLoop.getMediator());
                    copyStoryGame = new StoryGame(gameLoop.getStoryGame());
                    copyPossibleActionList = new PossibleActionList(board, copyMediator, copyStoryGame);
                    copyPossibleActionList.updateRealLists();

                    copyGameLoop = new GameLoop(copyMediator, copyPossibleActionList, board,
                            copyActiveColorController, copyStoryGame);
                    newAnswer = new AnswerSimbol(iField.getXCoord(), iField.getYCoord(), field.getXCoord(), field.getYCoord(), (char) 0);
                    copyGameLoop.doIteration(newAnswer);

                    if (copyGameLoop.getGameStateController().getState().getValue() == EnumGameState.MATE){
                        System.out.println(i);
                        return new Pair(100000000, newAnswer);
                    }

                    if (height > 0) {
                        pair = getAnswer(Color.swapColor(color), height - 1, copyGameLoop);
                        map.put(pair.getSum(), new Pair(pair.getSum(), newAnswer));
                    }else {
                        map.put(H(copyGameLoop.getMediator()), new Pair(H(copyGameLoop.getMediator()), newAnswer));
                    }
                }
            }
        } else {
            final List<Figure> figures = (List<Figure>) gameLoop.getMediator().getFigures(color);
            List<IField> fields;
            Figure figure;
            int index;

            gameLoop.getPossibleActionList().updateRealLists();

            do {
                index = random.nextInt(figures.size());
                figure = figures.get(index);
                fields = (List<IField>) gameLoop.getPossibleActionList().getRealList(figure);
            } while (fields.isEmpty());

            index = random.nextInt(fields.size());
            final IField finalField = fields.get(index);

            final IField startField = gameLoop.getMediator().getField(figure);

            newAnswer = new AnswerSimbol(startField.getXCoord(), startField.getYCoord(),
                    finalField.getXCoord(), finalField.getYCoord(), (char) 0);
            if (height > 0) {
                return getAnswer(Color.swapColor(color), height - 1, gameLoop);
            }
            return new Pair(H(gameLoop.getMediator()), newAnswer);
        }

        System.out.println(i);
        int key = OneTurn.color == color ? Collections.max(map.keySet()) : Collections.min(map.keySet());
        return map.get(key);
    }

    @Test
    public void one() {
        String string = "........" +
                "....b..." +
                "....Pk.B" +
                "........" +
                "......Q." +
                "........" +
                "......K." +
                "........";

        initGameMap(string);

        IAnswer answer = getAnswer(Color.WHITE,2, gameLoop).getAnswer();

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);

        assertEquals(gameLoop.getMatchResult().getValue(), EnumGameState.MATE);
    }

    @Test
    public void two() {
        String string = "r...k..r" +
                "ppR..npp" +
                "....N.P." +
                "........" +
                ".qN...Q." +
                ".P......" +
                "P....PP." +
                ".....K.R";
        initGameMap(string);

        IAnswer answer = getAnswer(Color.WHITE, 2, gameLoop).getAnswer();

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);

        assertEquals(gameLoop.getMatchResult().getValue(), EnumGameState.MATE);
    }

    @Test
    public void three() {
        String string = "r..R.r.k" +
                "pp.bbN.B" +
                "..p.pn.Q" +
                "........" +
                "........" +
                "........" +
                "PqP..PPP" +
                ".....RK.";
        initGameMap(string);

        IAnswer answer = getAnswer(Color.WHITE, 2, gameLoop).getAnswer();

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);

        assertEquals(gameLoop.getMatchResult().getValue(), EnumGameState.MATE);
    }

    @Test
    public void four() {
        String string = "....rbnk" +
                "p......p" +
                ".....P.n" +
                "..p.p..N" +
                "....p..." +
                "...q..Q." +
                "Pr.....P" +
                "R.B...RK";
        initGameMap(string);

        IAnswer answer = getAnswer(Color.WHITE, 2, gameLoop).getAnswer();

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(new AnswerSimbol(0, 5, 1, 6, (char) 0));

        answer = getAnswer(Color.WHITE, 2, gameLoop).getAnswer();

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);

        assertEquals(gameLoop.getMatchResult().getValue(), EnumGameState.MATE);
    }

    @Test
    public void five() {
        String string = "...R.Q.." +
                "rq...nnk" +
                "..p..p.." +
                ".....Pr." +
                ".P....p." +
                "P...N.P." +
                ".....PB." +
                "...R..K.";
        initGameMap(string);

        IAnswer answer = getAnswer(Color.WHITE, 2, gameLoop).getAnswer();

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(new AnswerSimbol(1, 7, 2, 7, (char) 0));

        answer = getAnswer(Color.WHITE, 2, gameLoop).getAnswer();

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(new AnswerSimbol(1, 5, 0, 7, (char) 0));

        answer = getAnswer(Color.WHITE, 2, gameLoop).getAnswer();

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);

        assertEquals(gameLoop.getMatchResult().getValue(), EnumGameState.MATE);
    }

    @Test
    public void six() {
        String string = "r.q...k." +
                "...b.n.p" +
                "p..pp.p." +
                ".p......" +
                "....P..Q" +
                "...BB..." +
                "PPP...PP" +
                ".K...R..";
        initGameMap(string);

        IAnswer answer = getAnswer(Color.WHITE, 4, gameLoop).getAnswer();

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);
        System.out.println(answer);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(new AnswerSimbol(0, 6, 1, 5, (char) 0));

        answer = getAnswer(Color.WHITE, 4, gameLoop).getAnswer();

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);
        System.out.println(answer);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(new AnswerSimbol(1, 5, 2, 5, (char) 0));

        answer = getAnswer(Color.WHITE, 4, gameLoop).getAnswer();

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);
        System.out.println(answer);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(new AnswerSimbol(2, 3, 3, 4, (char) 0));

        answer = getAnswer(Color.WHITE, 4, gameLoop).getAnswer();

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);
        System.out.println(answer);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(new AnswerSimbol(2, 5, 3, 6, (char) 0));

        answer = getAnswer(Color.WHITE, 4, gameLoop).getAnswer();

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);
        System.out.println(answer);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(new AnswerSimbol(3, 6, 4, 7, (char) 0));

        answer = getAnswer(Color.WHITE, 4, gameLoop).getAnswer();

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);
        System.out.println(answer);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(new AnswerSimbol(4, 7, 4, 6, (char) 0));

        answer = getAnswer(Color.WHITE, 4, gameLoop).getAnswer();

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);
        System.out.println(answer);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(new AnswerSimbol(3, 6, 4, 7, (char) 0));

        answer = getAnswer(Color.WHITE, 2, gameLoop).getAnswer();

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);
        System.out.println(answer);

        assertEquals(gameLoop.getMatchResult().getValue(), EnumGameState.MATE);
    }
}
