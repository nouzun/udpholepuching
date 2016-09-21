import java.awt.List;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server {
	public static void main(String args[]) throws Exception {
		boolean newClient;
    	DatagramSocket socket = new DatagramSocket(5666);
    	ArrayList<ClientInfo> clients = new ArrayList<ClientInfo>();
        System.out.println("Server is started!");
        
        while (true) {
        	newClient = true;
            byte[] receiveData = new byte[1024];

        	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);
            
            System.out.println("Server received a packet!");
            
  	      	for (int i=0; i < clients.size(); i++) {
  	      		ClientInfo client = clients.get(i);
  	      		if(client.address.equals(receivePacket.getAddress()) && client.port == receivePacket.getPort())
  	      		{
  	      			newClient = false;
  	      			break;
  	      		}
  	      	}

  	      	if(newClient)
  	      		clients.add(new ClientInfo(receivePacket.getAddress(), receivePacket.getPort()));
            
		    Thread thread = new Thread(new ServerResponder(socket, receivePacket, clients));
		    thread.start(); 
        }
	}
}

