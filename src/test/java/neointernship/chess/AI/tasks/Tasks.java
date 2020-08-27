package neointernship.chess.AI.tasks;

import neointernship.chess.game.gameplay.activecolorcontroller.ActiveColorController;
import neointernship.chess.game.gameplay.figureactions.IPossibleActionList;
import neointernship.chess.game.gameplay.figureactions.PossibleActionList;
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
import neointernship.web.client.AI.TurnGenerator;
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

public class Tasks {
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
    private static TurnGenerator turnGenerator;

    @Before
    public void before() {
        board = new Board();
        figureFactory = new Factory();
        mediator = new Mediator();
        storyGame = new StoryGame(mediator);
        possibleActionList = new PossibleActionList(board, mediator, storyGame);
        chessTypes = ChessType.OTHER;
        figuresStartPositionRepository = new FiguresStartPositionRepository();
        activeColorController = new ActiveColorController();

        gameLoop = new GameLoop(mediator, possibleActionList, board, activeColorController, storyGame);
        turnGenerator = new TurnGenerator(board, mediator, storyGame, possibleActionList, color, true, 0);
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



        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(turnGenerator.getAnswer(2));

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

        IAnswer answer = turnGenerator.getAnswer(2);

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

        IAnswer answer = turnGenerator.getAnswer(2);

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

        IAnswer answer = turnGenerator.getAnswer(2);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(new AnswerSimbol(0, 5, 1, 6, (char) 0));

        answer = turnGenerator.getAnswer(2);

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

        IAnswer answer = turnGenerator.getAnswer(2);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(new AnswerSimbol(1, 7, 2, 7, (char) 0));

        answer = turnGenerator.getAnswer(2);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(new AnswerSimbol(1, 5, 0, 7, (char) 0));

        answer = turnGenerator.getAnswer(2);

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

        IAnswer answer = turnGenerator.getAnswer(2);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);
        System.out.println(answer);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(new AnswerSimbol(0, 6, 1, 5, (char) 0));

        answer = turnGenerator.getAnswer(2);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);
        System.out.println(answer);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(new AnswerSimbol(1, 5, 2, 5, (char) 0));

        answer = turnGenerator.getAnswer(2);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);
        System.out.println(answer);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(new AnswerSimbol(2, 3, 3, 4, (char) 0));

        answer = turnGenerator.getAnswer(2);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);
        System.out.println(answer);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(new AnswerSimbol(2, 5, 3, 6, (char) 0));

        answer = turnGenerator.getAnswer(2);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);
        System.out.println(answer);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(new AnswerSimbol(3, 6, 4, 7, (char) 0));

        answer = turnGenerator.getAnswer(2);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);
        System.out.println(answer);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(new AnswerSimbol(4, 7, 4, 6, (char) 0));

        answer = turnGenerator.getAnswer(2);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);
        System.out.println(answer);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(new AnswerSimbol(3, 6, 4, 7, (char) 0));

        answer = turnGenerator.getAnswer(2);

        gameLoop.getActiveColorController().update();
        gameLoop.doIteration(answer);
        System.out.println(answer);

        assertEquals(gameLoop.getMatchResult().getValue(), EnumGameState.MATE);
    }
}
