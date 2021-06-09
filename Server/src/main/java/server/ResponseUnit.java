package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResponseUnit {

    public static void sendFileResponse(OutputStream outputToClient, String filename) throws IOException {
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

    public static void sendResponse(OutputStream toClient, String msg, String type) throws IOException{
        byte[] data = msg.getBytes(StandardCharsets.UTF_8);
        String header = "HTTP/1.1 200 OK\r\nContent-Type: "+type+"\r\nContent-length: "+data.length+"\r\n\r\n";


        toClient.write(header.getBytes());
        toClient.write(data);
        toClient.flush();

    }

}
