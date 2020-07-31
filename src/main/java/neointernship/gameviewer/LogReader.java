package neointernship.gameviewer;

import neointernship.chess.game.model.answer.AnswerSimbol;
import neointernship.chess.game.model.answer.IAnswer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LogReader {
    private String filePath;
    private String currentLine;
    private String firstPlayerName;
    private String secondPlayerName;

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

    public IAnswer getAnswer() throws Exception {
        if (readNextLine()){
            final String[] answer = currentLine.split("\\s*((\\()|\\))");
            return new AnswerSimbol(answer[1].charAt(0), answer[1].charAt(1), answer[3].charAt(0), answer[3].charAt(1));
        }
        throw new Exception(); //TODO
    }

    public void setNames() throws Exception {
        if (readNextLine()) {
            final String[] names = currentLine.split("\\s*+(игроком|и|началась)");
            firstPlayerName = names[1].trim();
            secondPlayerName = names[3].trim();
            return;
        }
        throw new Exception();
    }

    public String getFirstPlayerName() {
        return firstPlayerName; // TODO
    }

    public String getSecondPlayerName() {
        return secondPlayerName; // TODO
    }
}
