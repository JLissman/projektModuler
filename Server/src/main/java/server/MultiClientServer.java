package server;



import com.google.gson.Gson;
import db.EntityHandler;
import db.entity.Pokemon;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static server.HTMLTemplateBuilder.*;
import static server.RequestHandler.*;
import static server.ResponseUnit.*;

public class MultiClientServer {
    public static List<String> requests = new ArrayList<>();
    public static EntityHandler entityHandler = new EntityHandler(Pokemon.class);



    public static void main(String[] args) {
        //localhost 127.0.0.1
        //178.174.162.51
        int port = 80;
        ExecutorService executor = Executors.newCachedThreadPool();
        try(ServerSocket socket = new ServerSocket(port)) {
            System.out.println("Server running");
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
        URLDecoder decode = new URLDecoder();
        try {

            System.out.println("Client:"+client.getInetAddress()+" connected");
            Thread.sleep(500);
            BufferedReader fromClient = new BufferedReader(new InputStreamReader((client.getInputStream())));
            var toClient = client.getOutputStream();
            var url = readRequest(fromClient);
            System.out.println("URL:"+url);
            if(url.split("\\?").length > 1){
            System.out.println("Params:"+url.split("\\?")[1]);}


            if (url.equals("/cat.jpg")){
                sendFileResponse(toClient, "cat.jpg");

            }else if(url.equals("/allPkmn.html")){
                List<Pokemon> pkmn = entityHandler.getAll();

                Gson gson = new Gson();

                String json = gson.toJson(pkmn);
                sendResponse(toClient, json,"application/json");

            }
            else if(url.split("\\?")[0].equals("/pokemon.html")){
                String rawId = url.split("\\?")[1].replace("index=", "");
                int pokemonid = Integer.parseInt(rawId);
                String htmlPokemon = buildPKMNtemplate(pokemonid);
                sendResponse(toClient, htmlPokemon, "text/html");

            }
            else if(url.equals("/") || url.equals("/index.html")){
                String htmlTest = getHtmlFile("start");
                sendResponse(toClient, htmlTest, "text/html");

            }else if(url.split("\\?")[0].equals("/createPkmn")){
                String[] params = url.split("\\?")[1].split("&");
                String name = params[0].replace("pkname=","");
                String evo = params[1].replace("pkevo=","");
                int curLen = entityHandler.getAll().size();
                int id = curLen+1;
                boolean notUnique = true;
                while(notUnique){
                    notUnique = doesIdExist(id);
                    if(notUnique==false){
                        break;
                    }else{
                    id++;}
                }
                Pokemon pkmn = new Pokemon(id,name,evo);
                entityHandler.create(pkmn);
                String success = addString(pkmn);
                sendResponse(toClient, success, "text/html");
            }
            else if(url.split("\\?")[0].equals("/deletePkmn")){
                String[] params = url.split("\\?")[1].split("&");
                String idStr = params[0].replace("pkdelid=","");
                int id = Integer.parseInt(idStr);
                Pokemon pkmn =(Pokemon) entityHandler.findById(id);
                entityHandler.remove(pkmn);
                String success = deleteString(pkmn);
                sendResponse(toClient, success, "text/html");
            }
            else{
                notFound(toClient);
                }



            //String htmlTest = getHtmlFile("start");

            //sendResponse(toClient, requests.get(requests.size()-1));
            //sendResponse(toClient, htmlTest, "text/html");
            toClient.close();
            fromClient.close();
            client.close();
        }
    catch (Exception e){
        System.out.println(e);
        }

    }

    private static boolean doesIdExist(int id) {
        Pokemon pkmn = (Pokemon) entityHandler.findById(id);
        if(pkmn != null){
            return true;
        }else{
            return false;
        }
    }




    private static void notFound(OutputStream toClient) throws IOException{
        String header = "HTTP/1.1 404 Not Found\r\nContent-length: 0\r\n\r\n";


        toClient.write(header.getBytes());
        toClient.flush();

    }




        private static String getHtmlFile(String filename){
            StringBuilder contentBuilder = new StringBuilder();
            String file = "C:\\Users\\Jonathan\\Documents\\ITHS\\Databas\\projektModuler\\Server\\src\\main\\resources\\html\\"+filename+".html";

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



        public static String jsontests(){
            List<Pokemon> pkmn = entityHandler.getAll();

            Gson gson = new Gson();

            String json = gson.toJson(pkmn);

            return json;
        }



    }


