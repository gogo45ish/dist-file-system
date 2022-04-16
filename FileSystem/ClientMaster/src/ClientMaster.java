import java.io.*;
import java.net.Socket;

public class ClientMaster {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 4999);

        ServerConnectionMaster serverConn = new ServerConnectionMaster(socket);

//        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        new Thread(serverConn).start();

        while(true) {
            System.out.print("> ");
            String message = keyboard.readLine();

            if (message.equals("quit")) break;
            out.writeUTF(message);
//        pr.println("Hey server docker!");

        }

        socket.close();
    }
}
