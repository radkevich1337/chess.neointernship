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
import java.util.stream.Collectors;

public class TurnGenerator {
    private final IBoard board;
    private final IFactory figureFactory;
    private final IMediator mediator;
    private final IPossibleActionList possibleActionList;
    private final IStoryGame storyGame;
    private final BoardPrice boardPrice;

    private final boolean abFlag;
    private final int dif;

    private final ActiveColorController activeColorController;
    private final Color color;

    private final IGameLoop gameLoop;
    private static int i = 0;
    private int alpha;

    public TurnGenerator(IBoard board, IMediator mediator, IStoryGame storyGame,
                         IPossibleActionList possibleActionList, Color color, boolean abFlag, int dif) {
        this.board = board;
        this.figureFactory = new Factory();
        this.mediator = mediator;
        this.storyGame = storyGame;
        this.possibleActionList = possibleActionList;
        this.activeColorController = new ActiveColorController();
        this.color = color;

        this.abFlag = abFlag;
        this.dif = dif;

        this.boardPrice = new BoardPrice();

        if (activeColorController.getCurrentColor() != color) activeColorController.update();
        this.gameLoop = new GameLoop(this.mediator, this.possibleActionList, this.board, activeColorController, this.storyGame);
    }

    private int H(IMediator mediator, IPossibleActionList possibleActionList, Color activeColor) {
        int sum = 0;
        int figureSum;
        boolean beatenField;
        int ch = 0;
        for (Figure figure : mediator.getFigures()) {
            if (possibleActionList.getPotentialFigureAction().get(figure) == null)
                possibleActionList.updatePotentialLists();
            figureSum = 0;

            IField field = mediator.getField(figure);

            for (Figure figure1 : possibleActionList.getAttackList().getOrDefault(figure, new ArrayList<>())) {
                if (figure1.getClass() != King.class && figure1 != null) {
                    /*beatenField = BeatenBoard.getBeatenFieldCount(mediator.getField(figure1)) > 0;/*figure.getColor() == activeColor ?
                            BeatenBoard.getBeatenFieldCount(mediator.getField(figure1)) > 0 :
                            BeatenBoard.getBeatenFieldCount(mediator.getField(figure1)) < 0;*/
                    float koef = 0.01f;
                    //if (!beatenField) koef *= 0.2;
                    figureSum += figure1.getPrice() * koef;
                }
            }
            /*beatenField = figure.getColor() == activeColor ?
                    BeatenBoard.getBeatenFieldCount(field) > 0 : BeatenBoard.getBeatenFieldCount(field) < 0;*/

            ch = figure.getColor() == activeColor ? 1 : -1;
            //if (beatenField) ch *= 0.99;
            sum += ch * (figureSum + boardPrice.getBoard(figure)[field.getXCoord()][field.getYCoord()] +
                    figure.getPrice() + possibleActionList.getRealList(figure).size() * 2);
        }
        return sum;
    }

    private Pair getPair(Color color, int height, IGameLoop gameLoop, Integer alpha, Integer beta, int dif) {
        HashMap<Integer, Pair> map = new HashMap<>();
        Pair pair;
        IMediator mediator = gameLoop.getMediator();
        IPossibleActionList possibleActionList = gameLoop.getPossibleActionList();
        //possibleActionList.updateRealLists(); //todo
        IAnswer newAnswer = null;
        int tmp = 0;
        int h = 0;
        int oldAlpha = alpha;

        int score = Integer.MIN_VALUE;//color == Color.WHITE ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int hParent = H(gameLoop.getMediator(), gameLoop.getPossibleActionList(), color);

        List<Figure> list = (List<Figure>) mediator.getFigures(color).stream().
                sorted(Comparator.comparingInt(Figure::getPrice).reversed()).
                collect(Collectors.toList());

        for (Figure figure : list/*mediator.getFigures(color)*/) {
            IField iField = mediator.getField(figure);
            ActiveColorController copyActiveColorController = new ActiveColorController(gameLoop.getActiveColorController());
            if (copyActiveColorController.getCurrentColor() != color) copyActiveColorController.update();
            for (IField field : possibleActionList.getRealList(figure)) { //todo
                i++;

                IPossibleActionList copyPossibleActionList = new PossibleActionList(possibleActionList);
                IMediator copyMediator = copyPossibleActionList.getMediator();
                IStoryGame copyStoryGame = copyPossibleActionList.getStoryGame();

                GameLoop copyGameLoop = new GameLoop(copyMediator, copyPossibleActionList, board,
                        copyActiveColorController, copyStoryGame);
                newAnswer = new AnswerSimbol(iField.getXCoord(), iField.getYCoord(), field.getXCoord(), field.getYCoord(), 'Q');

                if (copyGameLoop.doIteration(newAnswer) == TurnStatus.ERROR) continue;

                h = H(copyMediator, copyPossibleActionList, color);
                if (h < (hParent + dif) && abFlag) continue;

                if (copyGameLoop.getGameStateController().getState().getValue() == EnumGameState.MATE) {
                    //int sum = this.color == Color.WHITE ? 1 : -1;
                    return new Pair(1000000000, newAnswer);
                } else if (copyGameLoop.getGameStateController().getState().getValue() != EnumGameState.ALIVE) {
                    return new Pair(h, newAnswer);
                }

                /*if (height > 0 && alpha < beta) {
                    pair = getPair(Color.swapColor(color), height - 1, copyGameLoop, -(alpha + 1), -alpha, dif + 10);
                    tmp = -pair.getSum();

                    if (alpha < tmp && beta > tmp) {
                        pair = getPair(Color.swapColor(color), height - 1, copyGameLoop, -beta, -alpha, dif + 10);
                        tmp = -pair.getSum();
                    }

                    if (tmp > alpha) alpha = tmp;

                    /*if (abFlag) {
                        if (tmp > score) score = tmp;
                        if (score > alpha) alpha = score;
                        if (alpha >= beta) return new Pair(alpha, newAnswer);
                    }
                }*/
                if (height > 0 && alpha < beta) {
                    if (oldAlpha == alpha) {
                        pair = getPair(Color.swapColor(color), height - 1, copyGameLoop, -beta, -alpha, dif + 10);
                        tmp = -pair.getSum();
                    } else {
                        pair = getPair(Color.swapColor(color), height - 1, copyGameLoop, -(alpha + 1), -alpha, dif + 10);
                        tmp = -pair.getSum();
                        if (alpha < tmp && beta > tmp){
                            pair = getPair(Color.swapColor(color), height - 1, copyGameLoop, -beta, -alpha, dif + 10);
                            tmp = -pair.getSum();
                        }
                    }

                    if (tmp > alpha) alpha = tmp;

                    map.put(tmp, new Pair(tmp, newAnswer));

                    /*if (abFlag) {
                        if (tmp > score) score = tmp;
                        if (score > alpha) alpha = score;
                        if (alpha >= beta) return new Pair(alpha, newAnswer);
                    }*/
               /* } else if (height > 0) {
                    pair = getPair(Color.swapColor(color), height - 1, copyGameLoop, -beta, -alpha);
                    tmp = -pair.getSum();
                    if (tmp > score) score = tmp;
                    if (score > alpha) alpha = score;
                    if (alpha >= beta) return new Pair(alpha, newAnswer);
                    map.put(score, new Pair(score, newAnswer));*/
                } else {
                    if (color == Color.WHITE) map.put(h, new Pair(h, newAnswer));
                    else map.put(h, new Pair(h, newAnswer));
                }

                //if (alpha >= beta) map.put(tmp, new Pair(tmp, newAnswer));
            }
        }

        if (map.size() == 0) {
            return new Pair(h, newAnswer);
        }

        int key = Collections.max(map.keySet());
        return map.get(key);
    }


    public IAnswer getAnswer(int depth) {
        i = 0;
        possibleActionList.updateRealLists();
        LocalTime startTime = LocalTime.now();
        alpha = H(mediator, possibleActionList, color);
        IAnswer answer = getPair(color, depth, gameLoop, -20000, 20000, dif).getAnswer();
        LocalTime gameTime = LocalTime.ofSecondOfDay(LocalTime.now().toSecondOfDay() - startTime.toSecondOfDay());
        System.out.println(i + " " + gameTime + " " + H(mediator, possibleActionList, color));
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
