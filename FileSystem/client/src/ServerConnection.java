import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardOpenOption.*;


public class ServerConnection implements Runnable{
    private static int count = 0;
    private int id=0;
    private Socket server;
    private DataInputStream in;
//    private BufferedReader keyboard;
    private PrintWriter out;
    private Scanner reader;
    private InputStream is;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;

    public ServerConnection(Socket s) throws IOException {
        server = s;

        out = new PrintWriter(server.getOutputStream(), true);
        reader = new Scanner(System.in);
        is = s.getInputStream();
        id=count++;
    }

    @Override
    public void run() {
        String input = null;
            try {
                while (true) {
//                    DataInputStream dIn = new DataInputStream(is);
                    in = new DataInputStream(is);
                    input = in.readUTF();

                    if (input != null) {
                        if (input.contains("UPLD")) {
                            int length = in.readInt();
                            String[] file = input.split(" ");
                            String fileName = file[1];

                            System.out.println(fileName);
                            System.out.println(length);
                            if(length>0) {
                                byte[] message = new byte[length];
                                in.readFully(message, 0, message.length); // read the message
                                System.out.println("read fully");
                                try (FileOutputStream fos = new FileOutputStream(fileName)) {
                                    fos.write(message);

                                }
                                System.out.println("File created");
                            }
                        }
                    }
                    if (input == null) break;

                        if (input.contains("create")){
                            System.out.println(input);
                            String[] file = input.split(" ");
                            String fileName = file[1];
                            Path p1 = Paths.get(fileName);
                            try {
                                Files.createFile(p1);
                                System.out.format("File created:  %s%n", p1.toRealPath());
                                out.println("Done");
                            } catch (FileAlreadyExistsException e) {
                                System.out.format("File %s  already exists.%n", p1.normalize());
                                out.println("File already exists.");
                            } catch (NoSuchFileException e) {
//                            System.out.format("Directory %s  does  not  exists.%n", p1.normalize()
//                                    .getParent());
                                out.println("Directory does  not  exists.");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if (input.contains("delete")){
                            String[] file = input.split(" ");
                            String fileName = file[1];
                            Path p = Paths.get(fileName);
                            File f = new File(fileName);

                            try {

                                Files.delete(p);
                                out.println("Done");
//                        System.out.println(p + "  deleted successfully.");
                            } catch (NoSuchFileException e) {
//                            System.out.println(p + "File does  not  exist.");
                                out.println(p + "File does  not  exist.");
                            } catch (DirectoryNotEmptyException e) {
//                            System.out.println("Directory " + p + "  is not  empty.");
                                out.println("Directory " + p + "  is not  empty.");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else if(input.contains("check")){
                            String[] file = input.split(" ");
                            String fileName = file[1];
//                    Path p1 = Paths.get(fileName);
                            File f = new File(fileName);
                            //TODO Client has to have id to tell which client has the file
                            if (f.exists()){
                                out.println("yes\n");
                            } else {
                                out.println("no\n");
                            }
                        }
                        else if(input.contains("find")){
                            DataOutputStream out = new DataOutputStream(server.getOutputStream());
                            System.out.println("This fired");
                            String[] c = input.split(" ");
                            String fileName = c[1];
                            File dir = new File(".");
                            String firstFile = "";
                            if(!dir.isDirectory()) throw new IllegalStateException("wtf mate?");
                            for(File file : dir.listFiles()) {
                                if(file.getName().contains(fileName)) {
                                    String substring = file.getName().substring(Math.max(file.getName().length() - 2, 0));
                                    if (file.getName().contains(substring)){
                                        firstFile = file.getName();
                                        out.writeUTF("receive "+firstFile+" "+file.getName()+ " " + fileName);
                                    }else{
                                        out.writeUTF("receive "+file.getName()+ " " + fileName);
                                    }
//                                    out.writeUTF("receive "+file.getName());
                                    System.out.println("file found: "+ file.getName());
//                                    FileInputStream fl = new FileInputStream(file);
                                    byte[] bytes = FileUtils.readFileToByteArray(file);
//                                    fl.close();

                                    out.flush();
                                    System.out.println(bytes.length);
                                    out.writeInt(bytes.length); // write length of the message
                                    out.write(bytes);
                                    System.out.println("Video sent");
                                    Path p = Paths.get(file.getName());
                                    Files.delete(p);
//                                    TimeUnit.MILLISECONDS.sleep(2000);
                                }
                            }
                        }
                        else if(input.contains("edit")){
                            String[] file = input.split(" ");
                            String fileName = file[1];
//                        Path dest = Paths.get(fileName);
                            File f = new File(fileName);
//                        List<String> texts = new ArrayList<>();
//                        Charset cs = Charset.forName("US-ASCII");

                            if (f.exists()){
                                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
                                try {
//                                Path p = Files.write(dest, texts, cs, WRITE, APPEND);
                                    for(int i = 2; i< file.length;i++){
                                        writer.append(file[i]);
                                        writer.append(" ");
                                    }
                                    writer.close();
                                    System.out.println("Text was written");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                out.println("There is no such file");
                            }

                        }

                }
            } catch (IOException e) {
                e.printStackTrace();
//                System.out.println("disconnected");
            }
//            catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            finally {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }


    }
}
