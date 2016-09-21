import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class ClientReceiver implements Runnable {

	 DatagramPacket receivePacket = null;
	 
	 public ClientReceiver(DatagramPacket receivePacket)
	 {
		 this.receivePacket = receivePacket;
	 }

	 public void run() {
		 
	      String reply = new String(receivePacket.getData(), 0 ,receivePacket.getLength());
	      System.out.println(receivePacket.getAddress() + ":" + receivePacket.getPort() + ": " + reply);
	 }
}
