package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Accepter_clients implements Runnable {
	private Socket socket;
	 private int nbrclient;
         BufferedReader entreeDepuisClient;
         PrintWriter sortieVersClient;
         List<Socket> cls = new ArrayList<Socket>();
	 public Accepter_clients(Socket s, int nucl, List<Socket> clients){
            socket = s;
            nbrclient=nucl;
            cls = clients;
	}
	 public void run() {
		 try {
			entreeDepuisClient=new BufferedReader( new
						InputStreamReader(socket.getInputStream()) );
			sortieVersClient =new PrintWriter(socket.getOutputStream());
			 while(true) {
                                treat();
			 }
	
		 } catch (IOException e) {
		 e.printStackTrace();
		 }
	 }
         
    public void treat()
    {
        Runnable r1 = () -> {
            System.out.println("reader start");
            try {
            while(true)
            {
                
                    String msg = entreeDepuisClient.readLine();
                    for(Socket s: cls){
                        if(s.equals(socket)){}
                        else{
                            PrintWriter sortieVersClients =new PrintWriter(s.getOutputStream());
                            sortieVersClients.println("Client " + nbrclient + ": " + msg);
                            sortieVersClients.flush();
                        }
                    }
                    if (msg.equals("exit"))  
                    {
                        System.out.println("client exit the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("client:" + msg);
            }
        } catch (IOException e) {
            System.out.println("connection closed 1");
            }
        };
        new Thread(r1).start();
    }
}
