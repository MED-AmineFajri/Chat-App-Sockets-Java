import Server.Accepter_clients;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

class Server
{
    ServerSocket server;
    Socket socket;
    List<Socket> clients = new ArrayList<Socket>();

    public Server()
    {
        try {
        server = new ServerSocket(2011);
        System.out.println("server is ready to accept connection");
        System.out.println("Waiting");
        int nbrclient=0;
        while(true){
            socket = server.accept();
            nbrclient++;
            clients.add(socket);
            System.out.println(clients.toString());
            System.out.println("Le client numéro "+nbrclient+ " est connecté !");
            Thread t = new Thread(new Accepter_clients(socket, nbrclient, clients));
            t.start();
        }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        System.out.println("this is server");
        new Server();
    } 
}