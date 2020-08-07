package neointernship.chess.game.gameplay.gamestate.state;

import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.enums.EnumGameState;

public class GameState implements IGameState {
    private EnumGameState value;
    private Color color;

    public GameState(EnumGameState value, Color color) {
        this.value = value;
        this.color = color;
    }

    public GameState(IGameState gameState) {
        this.value = gameState.getValue();
        this.color = gameState.getColor();
    }

    @Override
    public EnumGameState getValue() {
        return value;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void updateValue(EnumGameState value, Color color) {
        this.value = value;
        this.color = color;
    }
}
