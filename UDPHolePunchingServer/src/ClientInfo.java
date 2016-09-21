import java.io.Serializable;
import java.net.InetAddress;


public class ClientInfo implements Serializable {

    public InetAddress address;
    public Integer port;

    public ClientInfo(InetAddress address, Integer port){
        this.address = address;
        this.port = port;
    }
	
}
