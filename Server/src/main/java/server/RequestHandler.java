package server;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestHandler {
    public static String readRequest(BufferedReader inputFromClient) throws IOException {
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

}
