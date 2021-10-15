import java.util.ArrayList;

public class ConnectionStorage {
    private static ArrayList<User> connections = new ArrayList();

    public ConnectionStorage() {
    }

    public static synchronized void addConnection(User u) {
        connections.add(u);
    }

    public static synchronized void sendMessageToAll(String message) {
        new Thread(new Sender((User[])connections.toArray(new User[connections.size()]), message));
    }

    public static void removeFromConnections(User disconnectedUser){
        connections.remove(disconnectedUser);
    }

    public static synchronized void sendMessageToAllWithoutOne(User calledUser, String message) {
        ArrayList<User> copy = new ArrayList(connections);
        copy.remove(calledUser);
        (new Thread(new Sender((User[])copy.toArray(new User[copy.size()]), message))).start();
    }
}