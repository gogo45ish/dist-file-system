import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 4999);

        ServerConnection serverConn = new ServerConnection(socket);

//        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        new Thread(serverConn).start();

        while(true) {
            System.out.print("> ");
            String message = keyboard.readLine();

            if (message.equals("quit")) break;
            out.println(message);
//        pr.println("Hey server docker!");

        }

        socket.close();
    }
}
