package server;



import com.google.gson.Gson;
import db.HandleEntity;
import db.entity.Pokemon;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiClientServer {
    public static List<String> requests = new ArrayList<>();
    public static HandleEntity EH = new HandleEntity(Pokemon.class);


    public static void main(String[] args) {
        //localhost 127.0.0.1
        //178.174.162.51
        int port = 80;
        ExecutorService executor = Executors.newCachedThreadPool();
        try(ServerSocket socket = new ServerSocket(port)) {
            System.out.println("Multi Client server running!");
            while(true) {
                Socket client = socket.accept();

                //STARTA TRÅD
                executor.submit(()-> handleConnection(client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void handleConnection(Socket client){
        try {

            System.out.println("Client:"+client.getInetAddress()+" connected");
            Thread.sleep(500);
            BufferedReader fromClient = new BufferedReader(new InputStreamReader((client.getInputStream())));
            
            readAndSaveMsg(fromClient);



            var toClient = client.getOutputStream();
            String htmlTest = getHtmlFile("start");

            //sendResponse(toClient, requests.get(requests.size()-1));
            sendResponse(toClient, htmlTest, "text/html");
            toClient.close();
            fromClient.close();
            client.close();
        }
    catch (Exception e){
        System.out.println(e);
        }}



    private static void sendResponse(OutputStream toClient, String msg, String type) throws IOException{
        byte[] data = msg.getBytes(StandardCharsets.UTF_8);
        String header = "HTTP/1.1 200 OK\r\nContent-Type: "+type+"\r\nContent-length: "+data.length+"\r\n\r\n";


        toClient.write(header.getBytes());
        toClient.write(data);
        toClient.flush();

    }



    private static void readAndSaveMsg(BufferedReader fromClient) throws IOException {
        List<String> tempList = new ArrayList<>();

        while (true) {
            String line = fromClient.readLine();
            System.out.println(line);
            if(!line.isEmpty() && line != "" && line != "\n" && line.length() > 0){
                tempList.add(line);
            }
            if (line.isEmpty() || line == null) {
                break;
            }


        }
        String[] header = tempList.get(0).split(" ");
        System.out.println("header");
        for (String string : header) {
            System.out.println(string);
        }
        String id = header[1].replace("/", "");
        if(id != "/" && !id.isBlank() && id != ""){
            System.out.println("id is not /");
            int intId = Integer.parseInt(id);
            Pokemon pkmn = (Pokemon)EH.findById(intId);
            synchronized(requests){
                requests.add(pkmn.toString());
                }
        }
        }


        private static String getHtmlFile(String filename){
            StringBuilder contentBuilder = new StringBuilder();
            String file = "\\Server\\src\\main\\resources\\html\\"+filename+".html";

            try {
                BufferedReader in = new BufferedReader(new FileReader(file));
                String str;
                while ((str = in.readLine()) != null){
                    contentBuilder.append(str);
                }
                in.close();
                }
            catch(Exception e){

                System.out.println(e);

            }
            if(contentBuilder.length() > 0) {
                System.out.println("hittade html file");
            }

            return contentBuilder.toString();
        }



        public static void jsontests(){
            List<Pokemon> pkmn = EH.getAll();

            Gson gson = new Gson();

            String json = gson.toJson(pkmn);


        }

}
