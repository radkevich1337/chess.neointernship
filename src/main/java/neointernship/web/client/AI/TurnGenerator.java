package neointernship.web.client.AI;

import neointernship.chess.game.gameplay.activecolorcontroller.ActiveColorController;
import neointernship.chess.game.gameplay.figureactions.IPossibleActionList;
import neointernship.chess.game.gameplay.figureactions.PossibleActionList;
import neointernship.chess.game.gameplay.loop.GameLoop;
import neointernship.chess.game.gameplay.loop.IGameLoop;
import neointernship.chess.game.model.answer.AnswerSimbol;
import neointernship.chess.game.model.answer.IAnswer;
import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.enums.EnumGameState;
import neointernship.chess.game.model.figure.factory.Factory;
import neointernship.chess.game.model.figure.factory.IFactory;
import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.mediator.Mediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.chess.game.model.playmap.field.IField;
import neointernship.chess.game.story.IStoryGame;
import neointernship.chess.game.story.StoryGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class TurnGenerator {
    private final IBoard board;
    private final IFactory figureFactory;
    private final IMediator mediator;
    private final IPossibleActionList possibleActionList;
    private final IStoryGame storyGame;
    private final BoardPrice boardPrice;

    private final ActiveColorController activeColorController;
    private final Color color;

    private final IGameLoop gameLoop;

    public TurnGenerator(IBoard board, IMediator mediator, IStoryGame storyGame,
                         IPossibleActionList possibleActionList, Color color) {
        this.board = board;
        this.figureFactory = new Factory();
        this.mediator = mediator;
        this.storyGame = storyGame;
        this.possibleActionList = possibleActionList;
        this.activeColorController = new ActiveColorController();
        this.color = color;

        this.boardPrice = new BoardPrice();

        if (activeColorController.getCurrentColor() != color) activeColorController.update();
        this.gameLoop = new GameLoop(mediator, possibleActionList, board, activeColorController, storyGame);
    }

    private int H(IMediator mediator) {
        int sum = 0;
        int figureSum;
        int ch = 0;
        for (Figure figure : mediator.getFigures()) {
            figureSum = 0;
            for (Figure figure1 : possibleActionList.getAttackList().getOrDefault(figure, new ArrayList<>())){
                if (figure.getColor() == figure1.getColor()) figureSum += figure1.getPrice() / 15;
                else figureSum += figure1.getPrice() / 20;
            }
            IField field = mediator.getField(figure);
            ch = figure.getColor() == Color.WHITE ? 1 : -1;
            sum += ch * (figureSum + boardPrice.getBoard(figure)[field.getXCoord()][field.getYCoord()] +
                    figure.getPrice() + possibleActionList.getRealFigureActions().get(figure).size() * 5);
        }
        return sum;
    }

    private Pair getPair(Color color, int height, IGameLoop gameLoop) {
        if (gameLoop == null) gameLoop = this.gameLoop;

        HashMap<Integer, Pair> map = new HashMap<>();
        Pair pair;
        GameLoop copyGameLoop;
        IMediator copyMediator;
        IPossibleActionList copyPossibleActionList;
        IStoryGame copyStoryGame;
        IMediator mediator = gameLoop.getMediator();
        IPossibleActionList possibleActionList = new PossibleActionList(board, mediator, gameLoop.getStoryGame());
        possibleActionList.updateRealLists();
        IAnswer newAnswer;

        for (Figure figure : mediator.getFigures(color)) {
            IField iField = mediator.getField(figure);
            ActiveColorController copyActiveColorController = new ActiveColorController(gameLoop.getActiveColorController());
            if (copyActiveColorController.getCurrentColor() != color) copyActiveColorController.update();
            for (IField field : possibleActionList.getRealList(figure)) {
                copyMediator = new Mediator(gameLoop.getMediator());
                copyStoryGame = new StoryGame(gameLoop.getStoryGame());
                copyPossibleActionList = new PossibleActionList(board, copyMediator, copyStoryGame);
                copyPossibleActionList.updateRealLists();

                copyGameLoop = new GameLoop(copyMediator, copyPossibleActionList, board,
                        copyActiveColorController, copyStoryGame);
                newAnswer = new AnswerSimbol(iField.getXCoord(), iField.getYCoord(), field.getXCoord(), field.getYCoord(), (char) 0);
                copyGameLoop.doIteration(newAnswer);

                if (copyGameLoop.getGameStateController().getState().getValue() == EnumGameState.MATE) {
                    int sum = copyGameLoop.getActiveColorController().getCurrentColor() == Color.WHITE ? 1 : -1;
                    return new Pair(100000000 * sum, newAnswer);
                }

                if (height > 0) {
                    pair = getPair(Color.swapColor(color), height - 1, copyGameLoop);
                    map.put(pair.getSum(), new Pair(pair.getSum(), newAnswer));
                } else {
                    map.put(H(copyGameLoop.getMediator()), new Pair(H(copyGameLoop.getMediator()), newAnswer));
                }
            }
        }

        int key = color == Color.WHITE ? Collections.max(map.keySet()) : Collections.min(map.keySet());
        return map.get(key);
    }

    public IAnswer getAnswer(int depth) {
        return getPair(color, depth, gameLoop).getAnswer();
    }

    private class Pair {
        private final int sum;
        private final IAnswer answer;

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
}
