import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ServerResponder implements Runnable {

	DatagramSocket socket = null;
    DatagramPacket packet = null;
    byte[] sendData = new byte[1024];
    ArrayList<ClientInfo> clients = null;

    public ServerResponder(DatagramSocket socket, DatagramPacket packet, ArrayList<ClientInfo> clients) {
        this.socket = socket;
        this.packet = packet;
        this.clients = (ArrayList<ClientInfo>) clients.clone();
    }

    public void run() {
    	DatagramPacket response = null;
    	
        String sentence = new String(packet.getData(), 0 , packet.getLength());
        System.out.println("RECEIVED: " + sentence);
    	
        //String capitalizedSentence = sentence.toUpperCase();
        //sendData = capitalizedSentence.getBytes();
        
        	
    	ByteArrayOutputStream bos = null;
    	ObjectOutput out = null;
    	try {
    		if(sentence.startsWith("<Hi!>"))
    		{
    	        String capitalizedSentence = sentence.toUpperCase();
    	        sendData = capitalizedSentence.getBytes();
    	        
                response = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());

                socket.send(response);
    		}
    		else
    		{
      	      	for (int i=0; i < clients.size(); i++) {
      	      		ClientInfo client = clients.get(i);
      	      		if(client.address.equals(packet.getAddress()) && client.port == packet.getPort())
      	      		{
      	      			clients.remove(i);
      	      			break;
      	      		}
      	      	}
    			
	    		bos = new ByteArrayOutputStream();
	        	out = new ObjectOutputStream(bos);   
				out.writeObject(clients);
				
	        	sendData = bos.toByteArray();
	        	
	            response = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());

	            socket.send(response);
	        	
	        	out.close();
	        	bos.close();
    		}
    		

            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        	

        System.out.println(clients.size());

    }
}