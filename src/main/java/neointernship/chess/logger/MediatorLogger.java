package neointernship.chess.logger;

import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.IOException;
import java.util.HashMap;

public class MediatorLogger {
    private static final HashMap<Integer, MediatorLogger> mapLogger = new HashMap<>();
    private final Logger logger;

    private MediatorLogger(final int lobbyId) {
        logger = Logger.getLogger("mediator" + Integer.toString(lobbyId));

        try {
            logger.addAppender(new FileAppender(new PatternLayout(),
                    "logs\\server\\mediator_logs\\mediator_log_" + lobbyId + ".txt", false));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static void addLogger(final int lobbuId) {
        mapLogger.put(lobbuId, new MediatorLogger(lobbuId));
    }

    public static MediatorLogger getLogger(final int lobbyId) {
        return mapLogger.get(lobbyId);
    }

    public void logStartGame(final String firstPlayerName, final String secondPlayerName) {
        logger.info(firstPlayerName + "\n" + secondPlayerName);
    }

    public void logMediatorAction(final IMediator mediator, final IBoard board) {
        logger.info(toSimpleString(mediator, board));
    }


    private String toSimpleString(final IMediator mediator, final IBoard board) {
        final StringBuilder string = new StringBuilder();
        Figure figure;
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if ((figure = mediator.getFigure(board.getField(i, j))) != null) {
                    char figureChar = figure.getColor() == Color.WHITE ? figure.getGameSymbol() : (char) (figure.getGameSymbol() | 32);
                    string.append(figureChar);
                } else {
                    string.append(" ");
                }
            }
        }
        return string.toString();
    }
}
