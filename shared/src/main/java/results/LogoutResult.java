package results;

public class LogoutResult {

    private final String message;

    public LogoutResult() {
        message = null;
    }

    public LogoutResult(String m) {
        message = m;
    }

    public String getMessage() {
        return message;
    }
}
