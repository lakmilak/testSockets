import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    ;
    private static final int PORT = 1890;
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1488);
            while(true){
                Socket connected = serverSocket.accept();
                System.out.println("Someone has been connected!");
                ServerConnection connection = new ServerConnection(connected);
                new Thread(connection).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}