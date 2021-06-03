package server;



import com.google.gson.Gson;
import db.EntityHandler;
import db.entity.Pokemon;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiClientServer {
    public static List<String> requests = new ArrayList<>();
    public static EntityHandler EH = new EntityHandler(Pokemon.class);


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
                List<Pokemon> pkmn = EH.getAll();

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

    private static void sendFileResponse(OutputStream outputToClient, String filename) throws IOException {
        String header = "";
        System.out.println("sending cat img");
        byte[] data = new byte[0];
        File f = Path.of(filename).toFile();
        var contentType = Files.probeContentType(f.toPath());
        if (!(f.exists() && !f.isDirectory())){
            header = "HTTP/1.1 404 Not Found\r\nContent-length: 0\r\n\r\n";
            System.out.println("filenotfound");
            return;
        }
        else{
            try(FileInputStream fileInputStream = new FileInputStream(f))
            {
                data = new byte[(int) f.length()];
                fileInputStream.read(data);
                header = "HTTP/1.1 200 OK\r\nContent-Type : "+contentType+" "+data.length +"\r\n\r\n";

            }
            catch (IOException e){
                e.printStackTrace();
            }

        }


        outputToClient.write(header.getBytes());
        outputToClient.write(data);

        outputToClient.flush();

    }

    private static void sendResponse(OutputStream toClient, String msg, String type) throws IOException{
        byte[] data = msg.getBytes(StandardCharsets.UTF_8);
        String header = "HTTP/1.1 200 OK\r\nContent-Type: "+type+"\r\nContent-length: "+data.length+"\r\n\r\n";


        toClient.write(header.getBytes());
        toClient.write(data);
        toClient.flush();

    }
    private static void notFound(OutputStream toClient) throws IOException{
        String header = "HTTP/1.1 404 Not Found\r\nContent-length: 0\r\n\r\n";


        toClient.write(header.getBytes());
        toClient.flush();

    }


  /* private static void readAndSaveMsg(BufferedReader fromClient) throws IOException {
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
        String id = header[1];
        if(id != "/" && !id.isBlank() && id != ""){
            System.out.println("id is not /");
            int intId = Integer.parseInt(id);
            Pokemon pkmn = (Pokemon)EH.findById(intId);
            synchronized(requests){
                requests.add(pkmn.toString());
                }
        }
        }*/


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
            List<Pokemon> pkmn = EH.getAll();

            Gson gson = new Gson();

            String json = gson.toJson(pkmn);

            return json;
        }

        public static String readRequest(BufferedReader inputFromClient) throws IOException{
        var url = "";
        while(true){
            var line = inputFromClient.readLine();
            if(line.startsWith("GET")){
                System.out.println("GET REQUEST");
                url = line.split(" ")[1];}
            if(line.startsWith("POST")){
                System.out.println("POST REQUEST");

                url = line.split(" ")[1];
                var splitted = line.split(" ");
                for (String str:splitted
                     ) {
                    System.out.println("POSTLINE");
                    System.out.println(str);
                }
            }
            if(line==null || line.isEmpty()){
                break;
            }
            //System.out.println(line);
            }
            return url;
        }

        public static String buildPKMNtemplate(int id){
            Pokemon pkmn = (Pokemon)EH.findById(id);
            Pokemon nextPkmn = null;
            Pokemon prevPkmn = null;
            if(id < 150 && id > 1){
            prevPkmn = (Pokemon)EH.findById(id-1);
            nextPkmn = (Pokemon)EH.findById(id+1);
            }

            String finalString = "<!DOCTYPE html>"+
                                 "<html lang=\"en\">"+
                                 "<head>\n"+
                                 "<meta charset=\"UTF-8\">"+
                                 "<title>POKEMON BITCHES</title>"+
                                 "</head>"+
                                 "<center><body>"+
                                 "<div id=\"pkmnid\">Pokedex index:"+pkmn.getId()+"</div>"+
                                 "<div id =\"pkmnname\">Pokemon name:"+pkmn.getName()+"</div>"+
                                 "<div id =\"pkmnevo\">Pokemon evolution:"+pkmn.getEvolution()+" </div>"+
                                 "<img src=\"https://img.pokemondb.net/artwork/large/"+pkmn.getName().toLowerCase()+".jpg\" style=\"width:250px;height:250px\"></img>"+
                                 "</body>";
            if(prevPkmn != null){
                finalString = finalString +"<form method=\"POST\" action=\"/pokemon.html?index="+prevPkmn.getId()+"\">" +
                    "<button type=\"submit\" VALUE=\"Refresh\">Previous Pokemon </button></form>";
            }

            finalString = finalString + "<form method=\"POST\" action=\"/index.html\">" +
                                        "<button type=\"submit\">New Search</button></form>";
            if(nextPkmn != null){
                finalString = finalString +"<form method=\"POST\" action=\"/pokemon.html?index="+nextPkmn.getId()+"\">" +
                        "<button type=\"submit\">Next pokemon</button></form>";
            }
            finalString = finalString +  "</center>"+
                                                "</html>";
            return finalString;


        }
    }


