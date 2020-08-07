package neointernship.chess.game.gameplay.gamestate.controller;

import neointernship.chess.game.gameplay.figureactions.IPossibleActionList;
import neointernship.chess.game.gameplay.gamestate.controller.draw.DrawStateController;
import neointernship.chess.game.gameplay.gamestate.state.IGameState;
import neointernship.chess.game.gameplay.gamestate.update.FiguresHaveMovesComputation;
import neointernship.chess.game.gameplay.gamestate.update.GameStateDefineLogic;
import neointernship.chess.game.gameplay.kingstate.controller.IKingStateController;
import neointernship.chess.game.model.enums.Color;

public interface IGameStateController {
    boolean isMatchAlive();

    IGameState getState();

    void update(final Color color);

    IGameState getCurrentState();

    IPossibleActionList getPossibleActionList();

    FiguresHaveMovesComputation getFiguresHaveMovesComputation();

    GameStateDefineLogic getGameStateDefineLogic();

    DrawStateController getDrawStateController();

    IKingStateController getKingStateController();
}
