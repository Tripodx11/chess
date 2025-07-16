package service.requests;

public class RegisterRequest {

    private String username;
    private String password;
    private String email;

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
