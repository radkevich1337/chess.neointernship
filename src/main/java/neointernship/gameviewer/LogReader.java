package neointernship.gameviewer;

import neointernship.chess.game.model.answer.Answer;
import neointernship.chess.game.model.answer.IAnswer;
import neointernship.chess.game.model.playmap.field.Field;
import neointernship.chess.game.model.playmap.field.IField;
import neointernship.web.client.communication.message.TurnStatus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LogReader {
    private String filePath;
    private String currentLine;

    private BufferedReader reader;

    /*
        TODO сделать logreader так, чтобы он по строке из лога определял:
         - Answer,
         - TurnStatus,
         - имена игроков
     */
    public LogReader(final String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        reader = new BufferedReader(new FileReader(filePath));
    }

    public boolean readNextLine() throws IOException {
        currentLine = reader.readLine();
        return currentLine != null;
    }

    public TurnStatus getTurnStatus() {
        return TurnStatus.ERROR; //TODO
    }

    public IAnswer getAnswer() {
        return new Answer(); //TODO
    }

    public String getFirstPlayerName() {
        return null;  // TODO
    }

    public String getSecondPlayerName() {
        return null; // TODO
    }
}
