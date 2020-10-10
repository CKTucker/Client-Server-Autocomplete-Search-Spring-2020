/*
Author: Chris Tucker
Date: 5/2/2020
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private int port;
    private ServerSocket serverSocket;

    private void init() {
        try{
            serverSocket = new ServerSocket(port);
            System.out.println("Created server socket");
            while(true){
                System.out.println( "Listening for a connection on the local port "+serverSocket.getLocalPort() + "..." );
                Socket socket = serverSocket.accept();
                System.out.println( "\nA connection established with the remote port "+socket.getPort()+" at "+socket.getInetAddress().toString() );
                executeCommand(socket);
            }
            //serverSocket.close();
        }catch (IOException e){ e.printStackTrace(); }
    }

    private void executeCommand(Socket socket) {
        try{
            try {
                Scanner in = new Scanner( socket.getInputStream() );
                PrintWriter out = new PrintWriter( socket.getOutputStream() );
                System.out.println( "I/O setup done" );
                while(true){
                    if(in.hasNext()){
                        String command = in.next();
                        if( command.equals("QUIT") ) {
                            System.out.println("QUIT: Connection being closed.");
                            out.print("QUIT accepted. Connection being closed.\n");
                            out.flush();
                            return;
                        }else
                            accessAutocomplete(command, in, out);
                    }
                }
            }finally {
                socket.close();
                System.out.println("Connection with client closed");
            }
        }catch (Exception e){ e.printStackTrace(); }
    }

    private void accessAutocomplete(String command, Scanner in, PrintWriter out) {
        String prefix = in.next();
        String file = in.next();
        int numTerms = Integer.parseInt(in.next());

        In fileIn = new In(file);
        int n = fileIn.readInt();
        Term[] terms = new Term[n];
        for (int i = 0; i < n; i++) {
            long weight = fileIn.readLong();
            fileIn.readChar();
            String query = fileIn.readLine();
            terms[i] = new Term(query.trim(), weight);
        }
        Autocomplete autocomplete = new Autocomplete(terms);
        Term[] results = autocomplete.allMatches(prefix);
        out.println("Server found the following results:");
        if(results!=null) {
            for (int i = 0; i < Math.min(numTerms, results.length); i++) {
                //System.out.println("DEBUG: " + results[i]);
                out.println(results[i]);
                out.flush();
            }
        }else{
            out.println("No results found!");
            out.flush();
        }
        out.println("END");
        out.flush();
    }

    public static void main(String[] args){
        Server server = new Server();
        int i;
        if(args.length==0 || (i = Integer.parseInt(args[0])) <= 1023){
            throw new IllegalArgumentException("Must pass server a port # greater than 1023");
        }else{
            Runtime.getRuntime().addShutdownHook(server.new ShutdownThread());
            server.port=i;
            server.init();
        }
    }

    //serverSocket.close() is not possible to reach in Hello_Server, this I think is why you couldn't reuse a port without running
    //into issues, there is more in my report, but essentially by using a shutdown hook you can run cleanup code after Ctrl+C is pressed
    //allowing the socket to be closed properly. Ctrl+Z on linux however will bypass this, according to what I have read.
    //https://stackoverflow.com/questions/2541475/capture-sigint-in-java
    private class ShutdownThread extends Thread{
        @Override
        public void run() {
            try {
                System.out.println("Shutting down server...");
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
