import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerConnectionMaster implements Runnable{
    private Socket server;
    private BufferedReader in;
//    private BufferedReader keyboard;
    private PrintWriter out;
    private Scanner reader;

    public ServerConnectionMaster(Socket s) throws IOException {
        server = s;
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        out = new PrintWriter(server.getOutputStream(), true);
        reader = new Scanner(System.in);
    }

    @Override
    public void run() {

            String serverResponse = null;
            try {
                while (true) {
                    serverResponse = in.readLine();
                    if (serverResponse == null) break;

                    if (serverResponse.contains("work")){
                        System.out.println(serverResponse);
                        String[] numbers = serverResponse.replaceAll("^\\D+","").split("\\D+");
                        System.out.println("I am working");

                        int first = Integer.parseInt(numbers[0]);
                        int second = Integer.parseInt(numbers[1]);

                        System.out.println("*************" + first + "***" + second);
//                        findPrime();
                        findPrime(first,second);
                    } else if (serverResponse.equals("done")){
                        System.out.print(serverResponse + "\n");
                    }
                    else {
                        System.out.print(serverResponse+ "\n");
                    }
                }
            } catch (IOException e) {
//                e.printStackTrace();
                System.out.println("disconnected");
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


    }

    private void findPrime(int first, int second) {
//        int lower = 1, upper = (int)(Math.random() * 20 + 1);
//        int lower = first, upper = (int)(Math.random() * second + 1);
        int lower = first, upper = second;
        System.out.println("Range from "+ lower + " to "+ upper);
        for (int i = lower; i <= upper; i++)
            if (isPrime (i))
                System.out.print (i + " ");
    }

    private boolean isPrime(int n) {
        int count = 0;

        // 0, 1 negative numbers are not prime
        if (n < 2)
            return false;

        // checking the number of divisors b/w 1 and the number n-1
        for (int i = 2; i < n; i++)
        {
            if (n % i == 0)
                return false;
        }

        // if reached here then must be true
        return true;
    }
}
