package client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Boolean firstCon = true;
        while(true){
        try {
            if(firstCon){
            System.out.println("Connected to server...");
            firstCon=false;
            }
            Socket socket = new Socket("localhost",80);
            PrintWriter output = new PrintWriter(socket.getOutputStream());
            
            String toSend = doSomething();
            //tosend "GET /
            output.print(toSend+" HTTP/1.1\r\n");
            output.print("Host: localhost\r\n");
            output.print("\r\n");
            output.flush();
            //do more stuff
            
            
            
            BufferedReader input = new BufferedReader(new InputStreamReader((socket.getInputStream())));
            System.out.println(input.readLine());
            while(true){
                String line = input.readLine();
                if(line == null || line.isEmpty()){
                    break;
                }

                System.out.println(line);
            }
            output.close();
            input.close();
            socket.close();
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }}

    public static String doSomething() {
        Scanner sc = new Scanner(System.in);
        String returnString = "";
        System.out.println("Operation");
        System.out.println("1. Find pokemon");
        System.out.println("2. Add pokemon");
        System.out.println("3. Remove pokemon");
        System.out.println("4. Update pokemon");
        System.out.println("\nMake your choice:");
        int choice = sc.nextInt();
        sc.nextLine();
        switch(choice){
            case 1:
                System.out.println("Pokemon ID to find:");
                int id = sc.nextInt();
                sc.nextLine();
                returnString = "GET /"+id;
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            default:
                System.exit(0);
                
        }
        return returnString;
    }


}
