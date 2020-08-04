package neointernship.chess.game.model.figure.factory;

import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.figure.piece.Figure;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Factory implements IFactory {
    private PieceClassesRepository pieceClassesRepository;

    /**
     * Constructor of the class, where we declare the PieceRepository instance.
     */
    public Factory() {
        pieceClassesRepository = new PieceClassesRepository();
    }

    /**
     * This method creates and transfers an Figure instances in according to the input piece game symbol.
     *
     * @param pieceName {@link Character} input piece name.
     * @return IPiece instance.
     */
    @Override
    public Figure createFigure(final Character pieceName, final Color color) {
        Class<? extends Figure> pieceClass = pieceClassesRepository.getPieceClass((char) (pieceName & 223));
        try {
            Constructor<? extends Figure> constructor = pieceClass.getConstructor(Color.class);
            return constructor.newInstance(color);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
