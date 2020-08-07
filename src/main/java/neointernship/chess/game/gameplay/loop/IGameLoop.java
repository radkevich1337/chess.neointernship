package neointernship.chess.game.gameplay.loop;

import neointernship.chess.game.gameplay.activecolorcontroller.ActiveColorController;
import neointernship.chess.game.gameplay.activecolorcontroller.IActiveColorController;
import neointernship.chess.game.gameplay.figureactions.IPossibleActionList;
import neointernship.chess.game.gameplay.gameprocesscontroller.GameProcessController;
import neointernship.chess.game.gameplay.gameprocesscontroller.IGameProcessController;
import neointernship.chess.game.gameplay.gamestate.controller.GameStateController;
import neointernship.chess.game.gameplay.gamestate.controller.IGameStateController;
import neointernship.chess.game.gameplay.gamestate.state.IGameState;
import neointernship.chess.game.model.answer.IAnswer;
import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.story.IStoryGame;
import neointernship.web.client.communication.message.TurnStatus;

public interface IGameLoop {
    TurnStatus doIteration(final IAnswer answer);

    boolean isAlive();

    IGameState getMatchResult();

    void setGameStateController(IGameStateController gameStateController);

    void setGameProcessController(IGameProcessController gameProcessController);

    IGameStateController getGameStateController();

    IGameProcessController getGameProcessController();

    ActiveColorController getActiveColorController();

    Color getActiveColor();

    IMediator getMediator();

    IPossibleActionList getPossibleActionList();

    IStoryGame getStoryGame();
}
