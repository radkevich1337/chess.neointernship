package neointernship.chess.AI.tasks;

import neointernship.chess.game.gameplay.activecolorcontroller.ActiveColorController;
import neointernship.chess.game.gameplay.figureactions.IPossibleActionList;
import neointernship.chess.game.gameplay.figureactions.PossibleActionList;
import neointernship.chess.game.gameplay.gameprocesscontroller.GameProcessController;
import neointernship.chess.game.gameplay.gameprocesscontroller.IGameProcessController;
import neointernship.chess.game.gameplay.gamestate.controller.GameStateController;
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
import neointernship.web.client.player.APlayer;
import neointernship.web.client.player.RandomBot;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;

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

    private static IGameLoop gameLoop;

    @BeforeClass
    public static void before() {
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
    }

    @After
    public void clear() {
        mediator.clear();
        possibleActionList.updateRealLists();
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
        IStoryGame newStoryGame = new StoryGame((StoryGame) storyGame);
        IPossibleActionList newPossibleActionList = new PossibleActionList(board, newMediator, newStoryGame);

        gameLoop.setGameProcessController(new GameProcessController(newMediator, newPossibleActionList, board, storyGame));
        gameLoop.setGameStateController(new GameStateController(newPossibleActionList, newMediator, newStoryGame));
    }

    @Test
    public void one() {
        boolean flag = false;
        String string = "........" +
                "....b..." +
                "....Pk.B" +
                "........" +
                "......Q." +
                "........" +
                "......K." +
                "........";
        activeColorController.update();
        initGameMap(string);

        int i = 0;

        for (Figure figure : mediator.getFigures()) {
            IField iField = mediator.getField(figure);
            for (IField field : possibleActionList.getRealList(figure)) {
                updateGameLoop();
                IAnswer answer = new AnswerSimbol(iField.getXCoord(), iField.getYCoord(), field.getXCoord(), field.getYCoord(), (char) 0);
                gameLoop.doIteration(answer);
                i++;
                if (gameLoop.getMatchResult().getValue() == EnumGameState.MATE) {
                    flag = true;
                    break;
                }
            }
            if (flag) break;
        }
        System.out.println(i);
        assertTrue(flag);
    }

    @Test
    public void two() {
        boolean flag = false;
        String string = "r...k..r" +
                "ppR..npp" +
                "....N.P." +
                "........" +
                ".qN...Q." +
                ".P......" +
                "P....PP." +
                ".....K.R";
        initGameMap(string);
        int i = 0;

        for (Figure figure : mediator.getFigures()) {
            IField iField = mediator.getField(figure);
            for (IField field : possibleActionList.getRealList(figure)) {
                updateGameLoop();
                IAnswer answer = new AnswerSimbol(iField.getXCoord(), iField.getYCoord(), field.getXCoord(), field.getYCoord(), (char) 0);
                gameLoop.doIteration(answer);
                i++;
                if (gameLoop.getMatchResult().getValue() == EnumGameState.MATE) {
                    flag = true;
                    break;
                }
            }
            if (flag) break;
        }
        System.out.println(i);
        assertTrue(flag);
    }

    @Test
    public void three() {
        boolean flag = false;
        String string = "r..R.r.k" +
                "pp.bbN.B" +
                "..p.pn.Q" +
                "........" +
                "........" +
                "........" +
                "PqP..PPP" +
                ".....RK.";
        initGameMap(string);
        int i = 0;

        for (Figure figure : mediator.getFigures()) {
            IField iField = mediator.getField(figure);
            for (IField field : possibleActionList.getRealList(figure)) {
                updateGameLoop();
                IAnswer answer = new AnswerSimbol(iField.getXCoord(), iField.getYCoord(), field.getXCoord(), field.getYCoord(), (char) 0);
                gameLoop.doIteration(answer);
                i++;
                if (gameLoop.getMatchResult().getValue() == EnumGameState.MATE) {
                    flag = true;
                    break;
                }
            }
            if (flag) break;
        }
        System.out.println(i);
        assertTrue(flag);
    }
}
