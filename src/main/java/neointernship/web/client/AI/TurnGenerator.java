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
import neointernship.chess.game.model.figure.piece.King;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.mediator.Mediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.chess.game.model.playmap.field.IField;
import neointernship.chess.game.story.IStoryGame;
import neointernship.chess.game.story.StoryGame;
import neointernship.web.client.communication.message.TurnStatus;

import java.time.LocalTime;
import java.util.*;

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
    private static int i = 0;

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
        this.gameLoop = new GameLoop(this.mediator, this.possibleActionList, this.board, activeColorController, this.storyGame);
    }

    private int H(IMediator mediator, IPossibleActionList possibleActionList) {
        int sum = 0;
        int figureSum;
        boolean beatenField;
        int ch = 0;
        for (Figure figure : mediator.getFigures()) {
            if (possibleActionList.getPotentialFigureAction().get(figure) == null) possibleActionList.updatePotentialLists();
            figureSum = 0;

            IField field = mediator.getField(figure);

            for (Figure figure1 : possibleActionList.getAttackList().getOrDefault(figure, new ArrayList<>())) {
                if (figure1.getClass() != King.class) {
                    beatenField = figure.getColor() == Color.WHITE ?
                            BeatenBoard.getBeatenFieldCount(mediator.getField(figure1)) > 0 :
                            BeatenBoard.getBeatenFieldCount(mediator.getField(figure1)) < 0;
                    float koef = 0.05f;
                    if (!beatenField) koef *= 0.2;
                    figureSum += figure1.getPrice() * koef;
                }
            }
            beatenField = figure.getColor() == Color.WHITE ?
                    BeatenBoard.getBeatenFieldCount(field) > 0 : BeatenBoard.getBeatenFieldCount(field) < 0;

            ch = figure.getColor() == Color.WHITE ? 1 : -1;
            if (beatenField) ch *= 0.99;
            sum += ch * (figureSum + boardPrice.getBoard(figure)[field.getXCoord()][field.getYCoord()] +
                    figure.getPrice() + possibleActionList.getRealList(figure).size() * 2);
        }
        return sum;
    }

    private Pair getPair(Color color, int height, IGameLoop gameLoop, Integer ab) {
        HashMap<Integer, Pair> map = new HashMap<>();
        Pair pair;
        IMediator mediator = gameLoop.getMediator();
        IPossibleActionList possibleActionList = gameLoop.getPossibleActionList();
        //possibleActionList.updateRealLists(); //todo
        IAnswer newAnswer = null;

        Integer abInteger = null;

        for (Figure figure : mediator.getFigures(color)) {
            IField iField = mediator.getField(figure);
            ActiveColorController copyActiveColorController = new ActiveColorController(gameLoop.getActiveColorController());
            if (copyActiveColorController.getCurrentColor() != color) copyActiveColorController.update();
            for (IField field : possibleActionList.getRealList(figure)) { //todo
                i++;
                int hParent = H(gameLoop.getMediator(), gameLoop.getPossibleActionList());
                IPossibleActionList copyPossibleActionList = new PossibleActionList(possibleActionList);
                IMediator copyMediator = copyPossibleActionList.getMediator();
                IStoryGame copyStoryGame = copyPossibleActionList.getStoryGame();
                //copyPossibleActionList.updateRealLists();
                //copyPossibleActionList.updatePotentialLists();

                GameLoop copyGameLoop = new GameLoop(copyMediator, copyPossibleActionList, board,
                        copyActiveColorController, copyStoryGame);
                newAnswer = new AnswerSimbol(iField.getXCoord(), iField.getYCoord(), field.getXCoord(), field.getYCoord(), 'Q');

                if (copyGameLoop.doIteration(newAnswer) == TurnStatus.ERROR) continue;

                int h = H(copyMediator, copyPossibleActionList);
                if (color == Color.WHITE ? h <= hParent - 10 : h >= hParent + 10) continue;

                if (copyGameLoop.getGameStateController().getState().getValue() == EnumGameState.MATE) {
                    int sum = copyGameLoop.getActiveColorController().getCurrentColor() == Color.WHITE ? 1 : -1;
                    return new Pair(100000000 * sum, newAnswer);
                } else if (copyGameLoop.getGameStateController().getState().getValue() != EnumGameState.ALIVE) {
                    return new Pair(0, newAnswer);
                }

                if (height > 0) {
                    pair = getPair(Color.swapColor(color), height - 1, copyGameLoop, abInteger);
                    map.put(pair.getSum(), new Pair(pair.getSum(), newAnswer));

                    if (color == Color.WHITE) abInteger = Collections.max(map.keySet());
                    else abInteger = Collections.min(map.keySet());

                    if (ab != null) {
                        int key = color == Color.WHITE ? ab - abInteger : abInteger - ab;
                        if (key <= 0) break;
                    }
                } else {
                    map.put(h, new Pair(h, newAnswer));
                }
            }
        }

        if (map.size() == 0) {
            return new Pair(0, newAnswer);
        }

        int key = color == Color.WHITE ? Collections.max(map.keySet()) : Collections.min(map.keySet());
        return map.get(key);
    }

    public IAnswer getAnswer(int depth) {
        //i = 0;
        possibleActionList.updateRealLists();
        LocalTime startTime = LocalTime.now();
        IAnswer answer = getPair(color, depth, gameLoop, null).getAnswer();
        LocalTime gameTime = LocalTime.ofSecondOfDay(LocalTime.now().toSecondOfDay() - startTime.toSecondOfDay());
        System.out.println(i + " " + gameTime + " " + H(mediator, possibleActionList));
        return answer;
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
