package neointernship.chess.game.gameplay.loop;

import neointernship.chess.game.gameplay.activecolorcontroller.ActiveColorController;
import neointernship.chess.game.gameplay.figureactions.IPossibleActionList;
import neointernship.chess.game.gameplay.gameprocesscontroller.GameProcessController;
import neointernship.chess.game.gameplay.gameprocesscontroller.IGameProcessController;
import neointernship.chess.game.gameplay.gamestate.controller.GameStateController;
import neointernship.chess.game.gameplay.gamestate.controller.IGameStateController;
import neointernship.chess.game.gameplay.gamestate.state.IGameState;
import neointernship.chess.game.model.answer.IAnswer;
import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.chess.game.story.IStoryGame;
import neointernship.web.client.communication.message.TurnStatus;


/**
 * Класс, реализующий основное ядро игры (игровой цикл)
 */
public class GameLoop implements IGameLoop {
    private final ActiveColorController activeColorController;

    private IGameStateController gameStateController;

    private IGameProcessController gameProcessController;

    private Color activeColor;

    public GameLoop(final IMediator mediator,
                    final IPossibleActionList possibleActionList,
                    final IBoard board,
                    final ActiveColorController activeColorController,
                    final IStoryGame storyGame) {

        this.activeColorController = activeColorController;

        gameStateController = new GameStateController(possibleActionList, mediator, storyGame);

        gameProcessController = new GameProcessController(mediator, possibleActionList, board, storyGame);
    }

    /**
     * Активация главного игрового цикла.
     */
    @Override
    public TurnStatus doIteration(final IAnswer answer) {
        activeColor = activeColorController.getCurrentColor();

        gameProcessController.makeTurn(activeColor, answer);

        final TurnStatus turnStatus = gameProcessController.getTurnStatus();

        if (turnStatus != TurnStatus.ERROR) {
            gameStateController.update(Color.swapColor(activeColor));
        }
        return turnStatus;
    }

    @Override
    public boolean isAlive() {
        return gameStateController.isMatchAlive();
    }

    @Override
    public IGameState getMatchResult() {
        return gameStateController.getState();
    }

    @Override
    public void setGameStateController(IGameStateController gameStateController) {
        this.gameStateController = gameStateController;
    }

    @Override
    public void setGameProcessController(IGameProcessController gameProcessController) {
        this.gameProcessController = gameProcessController;
    }

    @Override
    public IGameStateController getGameStateController() {
        return gameStateController;
    }

    @Override
    public IGameProcessController getGameProcessController() {
        return gameProcessController;
    }
}
