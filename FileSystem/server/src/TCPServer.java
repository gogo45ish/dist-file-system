import java.io.IOException;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer {
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(4);

    public static void main(String[] args) throws IOException {

        ServerSocket listener = new ServerSocket(4999);
        boolean i = true;
//        while (i){
//            System.out.println("Waiting for client master");
//            Socket client = listener.accept();
//            System.out.println("Client master connected!");
//            i = false;
//        }
        while (true) {
            if (i) {
                System.out.println("Waiting for client master YOO");
                Socket clientMaster = listener.accept();
                System.out.println("Client master connected!");
                ClientHandler clientThread = new ClientHandler(clientMaster, clients);
                clients.add(clientThread);
                pool.execute(clientThread);
                i = false;
            }
            System.out.println("Server online!");
            Socket client = listener.accept();
            System.out.println("Client connected");

            ClientHandler clientThread = new ClientHandler(client, clients);
            clients.add(clientThread);

            pool.execute(clientThread);

        }


    }
}
