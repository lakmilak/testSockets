import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;

public class Sender implements Runnable{

    private User[] connections;
    private String message;
    private BufferedWriter out;

    public Sender(User[] connections, String message){
        this.connections = connections;
        this.message = message;
    }

    public Sender(User u, String message){
        connections = new User[1];
        connections[0] = u;
        this.message = message;
    }

    @Override
    public void run(){
        try{
            for(User u : connections){
                out = new BufferedWriter(new OutputStreamWriter(u.getConnectedSocked().getOutputStream()));
                out.write(message+"\n");
                out.flush();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
