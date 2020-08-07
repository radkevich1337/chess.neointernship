package neointernship.chess.game.model.figure.piece;

import neointernship.chess.game.model.enums.Color;

public class King extends Figure {
    public King(Color color) {
        super("King", 'K', color, 100000);
    }
}
