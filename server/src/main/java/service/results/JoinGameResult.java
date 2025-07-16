package service.results;

public class JoinGameResult {

    private final String message;

    public JoinGameResult() {
        message = null;
    }

    public JoinGameResult(String m) {
        message = m;
    }

    public String getMessage() {
        return message;
    }
}
