import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/*
switch to FTP passive mode
 */
public class PasvService implements Service {
    @Override
    public void getResponse(String data, Writer writer, RequestHandlerThread t) {
        System.out.println("------executing PasvService--------");

        String response = "";
        ServerSocket serverSocket = null;
        int sPort = -1;
        try {

            while(serverSocket == null){
                //generate a random int from 1025 to 9999 as port NO.
                sPort = (int) (Math.random() * 100000) % 9999 + 1024;
                serverSocket = new ServerSocket(sPort);
            }

            if(serverSocket != null && sPort != -1){
                //encrypt sPort with into 2 tokens for client to decipher
                t.setDataPort(null);
                int sPortToken1 = sPort/256;
                int sPortToken2 = sPort - sPortToken1*256;

                //encrypt server ip into tokens for client to decipher
                /*
                since client and server are both deployed on the local host for testing,
                for convenience, we simply use loopback address by calling getLocalHost() just for testing cases here
                instead of calling getLocalHost() for real server ip
                 */
                //String sIP = InetAddress.getLocalHost().getHostAddress();
                String sIP = InetAddress.getLoopbackAddress().getHostAddress();
                String sIPTokens = sIP.replace(".",",");

                response = "2271 Switching to passive mode ("+sIPTokens +","+sPortToken1+","+sPortToken2+")\r\n";
            }

            writer.write(response);
            writer.flush();

            //waiting for client to establish connection for file transfer
            t.setDataSocket(serverSocket.accept());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
