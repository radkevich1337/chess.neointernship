package neointernship.gameviewer;

import java.util.HashMap;

public class HistoryRepository {
    private final HashMap<Integer, String> pathRepository;

    public HistoryRepository() {
        pathRepository = new HashMap<>();
    }

    public void put(final Integer index, final String path) {
        pathRepository.put(index, path);
    }

    public String get(final Integer index) {
        return pathRepository.get(index);
    }
}
