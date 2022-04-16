
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ClientHandler implements Runnable {

    private Socket client;
    private DataInputStream in;
    private BufferedReader keyboard;
    private DataOutputStream out;
    private ArrayList<ClientHandler> clients;
    public static int fileCounter = 1;
    public static int counter = 1;
    private OutputStream os;
    public static JSONObject metaData = null;
    public static JSONArray arrayJSON = null;
    public static String firstFile = "";


    public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> clients) throws IOException {
        this.client = clientSocket;
        this.clients = clients;
        in = new DataInputStream(client.getInputStream());
        out = new DataOutputStream(client.getOutputStream());
        keyboard = new BufferedReader(new InputStreamReader(System.in));
        os = client.getOutputStream();


    }

    @Override
    public void run() {

        try {




            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\HP\\IdeaProjects\\FileSystem\\server\\src\\metaData.json"));
            String line;
            StringBuilder sbuilderObject = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sbuilderObject.append(line);
            }


            Path filePath = Paths.get("C:\\Users\\HP\\IdeaProjects\\FileSystem\\server\\src\\metaData.json");
            long size = Files.size(filePath);
            if (size != 0) {
                metaData = new JSONObject(sbuilderObject.toString());
                arrayJSON = metaData.getJSONArray("files");
            } else {
                metaData = new JSONObject();
                arrayJSON = new JSONArray();
            }


//            System.out.println(jsonObject.toJSONString());


            while (true) {
                String str = in.readUTF();

                if (str.contains("name")) {
                    out.writeUTF("Some random name");
                } else if (str.contains("check")) {
                    loopClients(str);
                } else if (str.equals("create")) {
                    sendToClient(str);
                } else if (str.contains("delete")) {
                    loopClients(str);
                } else if (str.contains("edit")) {
                    loopClients(str);
                }
                else if (str.contains("UPLD")) {
                    File file = new File("video.mp4");
//                    FileInputStream fl = new FileInputStream(file);
//                    byte[] bytes = new byte[(int) file.length()];
                    byte[] bytes = FileUtils.readFileToByteArray(file);
//                    fl.close();
                    clients.get(1).out.writeUTF("UPLD video.mp4");
                    clients.get(1).out.flush();
                    System.out.println(bytes.length);
                    clients.get(1).out.writeInt(bytes.length); // write length of the message
                    clients.get(1).out.write(bytes);
                    System.out.println("Video sent");
                }
                else if (str.contains("get")) {
                    System.out.println("FIRED");
                    String[] file = str.split(" ");
                    String fileName = file[1];
                    loopClients("find " + fileName);
//                    clients.get(1).out.writeUTF("find "+ fileName);
                }
                else if (str.contains("receive")) {

                    int length = in.readInt();
                    String[] file = str.split(" ");
                    String fileName = "";
                    String fileNamePart = "";
                    if (file.length == 4){
                        firstFile = file[1];
                        fileNamePart = file[2];
                        fileName = file[3];
                    } else {
                        fileNamePart = file[1];
                        fileName = file[2];
                    }
                    int numbFiles = 0;

                    for (int i = 0; i < arrayJSON.length(); i++) {
                        JSONObject o = arrayJSON.getJSONObject(i);
                        String nameFile = o.getString("file");
                        if (nameFile.equals(fileName)) {
                            numbFiles = o.getInt("numberOfFiles");
                            break;
                        }
                    }

                    if (length > 0) {
                        byte[] message = new byte[length];
                        in.readFully(message, 0, message.length); // read the message
                        System.out.println("read fully");
                        try (FileOutputStream fos = new FileOutputStream(fileNamePart)) {
                            fos.write(message);
                            //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
                        }
                        System.out.println("File created");
                    }
                    if (fileCounter != numbFiles){
                        fileCounter++;
                        System.out.println("****************"+fileCounter+"********************");
                        System.out.println("JSDFSDKFJLSKDJFLKSDNKGLNDSKNFKLNDSKLF");
                        System.out.println("JSDFSDKFJLSKDJFLKSDNKGLNDSKNFKLNDSKLF");
                        System.out.println("JSDFSDKFJLSKDJFLKSDNKGLNDSKNFKLNDSKLF");

                    }else{
                        try {

                            byte[] b = new byte[10000];

                            String part_path = "C:\\Users\\HP\\IdeaProjects\\FileSystem\\server\\src\\"+firstFile;
                            System.out.println(part_path);

                            String nameOfFile = part_path.substring(0, part_path.lastIndexOf("."));

                            String part_no = part_path.substring(part_path.lastIndexOf(".") + 1);
                            System.out.println(part_no);

                            String file_extension = nameOfFile.substring(nameOfFile.lastIndexOf("."));

                            String new_path = "C:\\Users\\HP\\IdeaProjects\\FileSystem\\server\\src\\finalvideo.mp4";

                            File combine_file_path = new File(new_path);

                            String path_parent = combine_file_path.getParent();

                            int flag = 0;
                            if (new_path.endsWith(file_extension)) {
                                flag = 1;
                            }

                            File check_part_file_path = new File(part_path);
                            File check_new_file_path = new File(path_parent);

                            if (check_part_file_path.exists() && flag == 1 && check_new_file_path.exists()) {
                                FileOutputStream fos = new FileOutputStream(new_path);

                                int x = 1;
                                int readBytes;
                                String parts_name_path = "";
                                while (true) {
                                    parts_name_path = "";
                                    parts_name_path = nameOfFile + "." + x;
//                                    if (part_no.startsWith("00")) {
//                                        parts_name_path = nameOfFile + ".00" + x;
//                                    } else {
//                                        parts_name_path = nameOfFile + ".0" + x;
//                                    }

                                    System.out.println(parts_name_path);
                                    File f = new File(parts_name_path);
                                    if (f.exists()) {
                                        FileInputStream fis = new FileInputStream(parts_name_path);

                                        while (fis.available() != 0) {
                                            readBytes = fis.read(b, 0, 10000);
                                            fos.write(b, 0, readBytes);
                                        }
                                        System.out.println(" Part " + x + " Joined");
                                        fis.close();
                                        x++;
                                        Path p = Paths.get(parts_name_path);
                                        Files.delete(p);
                                    } else {
                                        System.out.println("File joined successfully");
                                        break;
                                    }
                                }
                                fos.close();
                            } else if (!(check_new_file_path.exists())) {
                                System.out.println("You write wrong Path of New File");
                            } else if (!(flag == 0)) {
                                System.out.println("New file extension doesn't match");
                            } else {
                                System.out.println("File path of first part doesn't exist");
                            }
                            System.out.println("File created");


                        } catch (Exception e) {
                            System.out.println(e);
                            e.printStackTrace();
                        }
                        fileCounter = 1;
                        firstFile = "";
                    }


                    System.out.println(fileName + " HAS " + numbFiles);

                }
                else if (str.contains("append")) {
                    try {
                        byte[] b = new byte[10000];

                        String part_path = "C:\\Users\\HP\\IdeaProjects\\FileSystem\\server\\src\\video.mp4.001";

                        String fileName = part_path.substring(0, part_path.lastIndexOf("."));

                        String part_no = part_path.substring(part_path.lastIndexOf(".") + 1);

                        String file_extension = fileName.substring(fileName.lastIndexOf("."));

                        String new_path = "C:\\Users\\HP\\IdeaProjects\\FileSystem\\server\\src\\finalvideo.mp4";

                        File combine_file_path = new File(new_path);

                        String path_parent = combine_file_path.getParent();

                        int flag = 0;
                        if (new_path.endsWith(file_extension)) {
                            flag = 1;
                        }

                        File check_part_file_path = new File(part_path);
                        File check_new_file_path = new File(path_parent);

                        if (check_part_file_path.exists() && flag == 1 && check_new_file_path.exists()) {
                            FileOutputStream fos = new FileOutputStream(new_path);

                            int x = 1;
                            int readBytes;
                            String parts_name_path = "";
                            while (true) {
                                parts_name_path = "";
                                parts_name_path = fileName + "." + x;
//                                if (part_no.startsWith("00")) {
//                                    parts_name_path = fileName + ".00" + x;
//                                } else {
//                                    parts_name_path = fileName + ".0" + x;
//                                }

                                File f = new File(parts_name_path);
                                if (f.exists()) {
                                    FileInputStream fis = new FileInputStream(parts_name_path);

                                    while (fis.available() != 0) {
                                        readBytes = fis.read(b, 0, 10000);
                                        fos.write(b, 0, readBytes);
                                    }
                                    System.out.println(" Part " + x + " Joined");
                                    fis.close();
                                    x++;
                                } else {
                                    System.out.println("File joined successfully");
                                    break;
                                }
                            }
                        } else if (!(check_new_file_path.exists())) {
                            System.out.println("You write wrong Path of New File");
                        } else if (!(flag == 0)) {
                            System.out.println("New file extension doesn't match");
                        } else {
                            System.out.println("File path of first part doesn't exist");
                        }
                        System.out.println("File created");


                    } catch (Exception e) {
                        System.out.println(e);
                        e.printStackTrace();
                    }
                }
                else if (str.contains("dist")) {
                    String[] command = str.split(" ");
                    String fileName = command[1];
                    try {
                        byte b[] = new byte[1000000];
                        int x = 1, j = 0;
                        String s = "";
                        int numFiles = 0;


                        String path = fileName;
                        FileInputStream fis = new FileInputStream(path);
                        int read_bytes = 0;
                        while (fis.available() != 0) {
                            j = 0;
                            s = "";
                            s = path + "." + x;
//                            if (x <= 9) {
//                                s = path + ".00" + x;
//                            } else {
//                                s = path + ".0" + x;
//                            }

                            FileOutputStream fos = new FileOutputStream(s);
                            while (j < 50000000 && fis.available() != 0) {
                                read_bytes = fis.read(b, 0, 1000000);
                                j = j + read_bytes;
                                fos.write(b, 0, read_bytes);

                            }
                            //new file
                            numFiles++;


                            System.out.println(s);
                            sendFileToClient(s);
                            System.out.println("Part " + x + " Created.");
                            x++;
                            fos.close();
                            Path p = Paths.get(s);
                            Files.delete(p);
                        }

//                        for (int i = 1; i < 5; i++) {
//                            sendFileToClient("video.mp4.00"+i);
//                        }

                        JSONObject obj = new JSONObject();
                        obj.put("numberOfFiles", numFiles);
                        obj.put("file", fileName);

                        arrayJSON.put(obj);

                        metaData.put("files", arrayJSON);
                        try (FileWriter file = new FileWriter("metaData.json")) {
                            file.write(metaData.toString());
                        }

                        System.out.println("File splitted successfully");
                        fis.close();
                    } catch (Exception e) {
                        System.out.println(e);
                        e.printStackTrace();
                    }
                }
                else {
                    System.out.println(str);
                    clients.get(0).out.writeUTF(str);
                }
            }
        } catch (IOException e) {
            System.err.println("Client disconnected");
            e.printStackTrace();
        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendToClient(String msg) throws IOException {
        if (counter == clients.size() + 1) {
            counter = 1;
        }
        System.out.println("Counter: " + counter);

        clients.get(counter).out.writeUTF(msg);
        counter++;
    }

    private void sendFileToClient(String fileName) throws IOException {
        if (counter == clients.size()) {
            counter = 1;
        }
        File file = new File("C:\\Users\\HP\\IdeaProjects\\FileSystem\\server\\src\\"+fileName);
        byte[] bytes = FileUtils.readFileToByteArray(file);
        clients.get(counter).out.writeUTF("UPLD "+fileName);
        clients.get(counter).out.flush();
        System.out.println(bytes.length);
        clients.get(counter).out.writeInt(bytes.length); // write length of the message
        clients.get(counter).out.write(bytes);
        System.out.println("Video sent");

        counter++;
    }

    private void loopClients(String msg) throws IOException {

        if (clients.size() == 2) {
            clients.get(1).out.writeUTF(msg);
        } else {
            for (int i = 1; i < clients.size(); i++) {
                clients.get(i).out.writeUTF(msg);
                System.out.println("SENT");
            }
        }


    }

}
