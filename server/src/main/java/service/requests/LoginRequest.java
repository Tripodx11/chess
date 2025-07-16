package service.requests;

public class LoginRequest {

    private String username;
    private String password;

    public LoginRequest (String un, String pass) {
        username = un;
        password = pass;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }



}
