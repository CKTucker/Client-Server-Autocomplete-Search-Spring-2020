/*
Author: Chris Tucker
Date: 5/2/2020
 */


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private int port;
    private String query;
    private String file;
    private int numOutput;
    private Socket socket;
    private Scanner in;
    private PrintWriter out;


    private void init() {
        try{
            try{
                socket = new Socket("localhost", port);
                System.out.println("Socket created on the local port " + socket.getLocalPort());
                System.out.println("A connection established with the remote port " + socket.getPort() + " at " + socket.getInetAddress().toString());
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream());
                System.out.println("I/O setup done.");
                sendQuery();
            }finally {
                shutdown();
            }
        }catch (IOException e){e.printStackTrace();}
    }

    private void sendQuery() {
        System.out.println("Sending Query");
        out.printf("QUERY %s %s %d\n", query, file,numOutput);
        out.flush();
        System.out.println("Waiting for Server...");
        while(in.hasNext()){
            String message = in.nextLine();
            if(!message.equals("END"))
                System.out.println(message);
            else break;
        }
    }

    private void shutdown() {
        System.out.println("Shutting Down...");
        out.println("QUIT");
        out.flush();
        try{
            in.close();
            out.close();
            socket.close();
            System.out.println( "Closed Connection" );
        }catch (IOException e){ e.printStackTrace(); }
    }

    public static void main(String[] args){
        int p,num;
        Client client;
        if(args.length<3 || (p = Integer.parseInt(args[0])) <= 1023){
            throw new IllegalArgumentException("Must pass client a port # greater than 1023, a search query, and a file");
        }else if(args.length==4 && (num = Integer.parseInt(args[3]))>0){
            //Optional Parameter to allow client to specify the number of items matching the query to receive from the server
            //Default: 8
            client = new Client(p, args[1], args[2], num);
        }else{
            client = new Client(p, args[1], args[2]);
        }
        client.init();
    }
    private Client(int port, String query, String file){
        this(port,query,file,8);
    }
    private Client(int port, String query, String file, int numOutput){
        this.port=port;
        this.query=query;
        this.file=file;
        this.numOutput=numOutput;
    }
}
