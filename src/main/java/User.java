import java.net.Socket;

public class User {
    private Socket connectedSocked;
    private String login;

    public User(String name, Socket socket) {
        this.connectedSocked = socket;
        this.login = name;
    }

    public Socket getConnectedSocked() {
        return connectedSocked;
    }

    public String getLogin() {
        return login;
    }
}
