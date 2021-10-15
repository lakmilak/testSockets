import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerConnection implements Runnable {
    private Thread t;
    private Socket connectedSocket;
    private User connectedUser;
    private BufferedWriter out;
    private Scanner in;
    private boolean successfulConnection = false;

    public ServerConnection(Socket socket) {
        t = new Thread("Socket " + socket);
        connectedSocket = socket;
    }

    public void run() {
        try {
            in = new Scanner(connectedSocket.getInputStream());
            out = new BufferedWriter(new OutputStreamWriter(connectedSocket.getOutputStream()));
            Hub();
            getInformation();
            ConnectionStorage.removeFromConnections(connectedUser);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();

                if (out != null) out.close();

                connectedSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void getInformation() throws IOException {
        ConnectionStorage.addConnection(connectedUser);

        while(connectedSocket.isConnected()) {
           if(in.hasNextLine()){
               String message = in.nextLine();
               if(message.equals((char) 0))
                   break;
               ConnectionStorage.sendMessageToAllWithoutOne(connectedUser,connectedUser.getLogin()+": "+message);
           }
        }

    }

    private void Hub() throws IOException {
        while(true) {
            out.write("Введите login, чтобы зайти в свой аккаунт\n");
            out.write("Или введите singup чтобы создать учётную запись\n");
            out.flush();
            String req = in.nextLine();
            if (req.equalsIgnoreCase("login")) {
                if (tryToLogin()) {
                    out.write("Вы успешно зашли под пользователем "+connectedUser.getLogin()+"\n");
                    out.flush();
                    return;
                }
            } else if (req.equalsIgnoreCase("singup")) {
                registration();
            } else {
                out.write("Неверно введён запрос, повторите ещё!\n");
                out.flush();
            }
        }
    }

    private boolean tryToLogin() throws IOException {
        out.write("Введите логин\n");
        out.flush();
        String login = in.nextLine();
        out.write("Введите пароль\n");
        out.flush();
        String password = in.nextLine();
        File file = new File("src/main/resources/Users/");
        String[] fileMas= file.list();

        for(String fileName : fileMas) {
            FileInputStream fileInput = new FileInputStream("src/main/resources/Users/" + fileName);
            String line = new String(fileInput.readAllBytes());
            String[] loginComponents = line.split("!");
            if (login.equals(loginComponents[0]) && password.equals(loginComponents[1])) {
                connectedUser = new User(login, connectedSocket);
                return true;
            }
        }

        return false;
    }

    private void registration() throws IOException {
        out.write("Введите логин\n");
        out.flush();
        String login = in.nextLine();
        out.write("Введите пароль\n");
        out.flush();
        String password = in.nextLine();
        File f = new File("src/main/resources/Users/");
        int number = f.list().length;
        String tempLine = "test" + (number+ 1) + ".dat";
        f = new File("src/main/resources/Users/" + tempLine);
        f.createNewFile();
        FileWriter fileWriter = new FileWriter(f);
        fileWriter.write(login + "!" + password);
        fileWriter.flush();
        out.write("Регистрация завершена!\n");
        out.flush();
        fileWriter.close();
    }
}
