package neointernship.web.client.AI.treewidth;

import neointernship.chess.game.gameplay.loop.GameLoop;
import neointernship.chess.game.model.answer.AnswerSimbol;
import neointernship.chess.game.model.answer.IAnswer;
import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.playmap.field.IField;

import java.util.ArrayList;
import java.util.List;

public class Tree {
    private final GameLoop gameLoop;
    private int h;
    private final IAnswer answer;
    private final List<Tree> childs;

    public Tree(GameLoop gameLoop, IAnswer answer) {
        this.gameLoop = gameLoop;
        this.answer = answer;
        this.childs = new ArrayList<>();
    }

    public void makeTurn() {
        int hBefore = 0;
        gameLoop.doIteration(answer);
        h = 1;
        if (hBefore < h) {
            for (Figure figure : gameLoop.getMediator().getFigures(gameLoop.getActiveColorController().getCurrentColor())) {
                IField startField = gameLoop.getMediator().getField(figure);
                for (IField field : gameLoop.getPossibleActionList().getRealList(figure)) {
                    IAnswer answer = new AnswerSimbol(startField.getXCoord(), startField.getYCoord(),
                            field.getXCoord(), field.getYCoord(), 'Q');
                    childs.add(new Tree(new GameLoop(gameLoop), answer));
                }
            }
        }
    }

    public int getH() {
        return h;
    }

    public IAnswer getAnswer() {
        return answer;
    }

    public List<Tree> getChilds() {
        return childs;
    }
}
