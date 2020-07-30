package neointernship.gameviewer;

import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.board.Board;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.gameviewer.gui.Listing;
import neointernship.gameviewer.gui.View;
import neointernship.web.client.GUI.board.view.BoardView;

import java.io.File;
import java.io.FileNotFoundException;

public class LoggerViewer {
    private static final String LOGS_FOLDER_PATH= "zxcqwe123"; // TODO путь к папке с логами
    private static final File[] files = new File(LOGS_FOLDER_PATH).listFiles();
    private static HistoryRepository historyRepository;
    private static GameInfo gameInfo = null;

    private static final Listing listing = new Listing();
    private static BoardView view = null;

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        historyRepository = new HistoryRepository();
        initPathMap();
    }

    public static void initPathMap() throws InterruptedException, FileNotFoundException {
        int index = 0;
        for (File file : files) {
            System.out.println("File: " + file.getName());
            historyRepository.put(index, file.getAbsolutePath());
            index++;
        }
        int i = listing.getLobbyIndex();
        String logPath = historyRepository.get(i);
        gameInfo = new GameInfo(logPath);
        view = new BoardView(gameInfo.getMoveInfo().getMediator(), gameInfo.getMoveInfo().getBoard());
    }

    public static void loop() throws InterruptedException {
        while (true) {
            String answer = listing.getAnswer();
            gameInfo.update(answer);

            view.display();
        }
    }
}
