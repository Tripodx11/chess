package service.requests;

public class RegisterRequest {

    private final String username;
    private final String password;
    private final String email;

    public RegisterRequest (String un, String pass, String email) {
        username = un;
        password = pass;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
