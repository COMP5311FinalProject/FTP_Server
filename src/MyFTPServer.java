import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MyFTPServer {

    private int port;

    ServerSocket serverSocket;

    public MyFTPServer(int port){
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //initialize data required to run handler threads;
        ThreadScopeData.init();
    }

    public void listen() throws IOException{
        Socket socket = null;
        while(true){
            //three way handshakes to establish TCP connection
            socket = serverSocket.accept();
            RequestHandlerThread t = new RequestHandlerThread(socket);
            t.start();
        }
    }

    public static void main(String[] args) {
        System.out.println("========booting up server========");

        //get config file path
        String configPath = System.getProperty("user.dir") + "/config/serverconfig.xml";
        File file = new File(configPath);
        SAXBuilder builder = new SAXBuilder();
        try {
            //get port No. from config file
            Document parse = builder.build(file);
            Element root = parse.getRootElement();
            int port = Integer.parseInt(root.getChildText("port"));

            //boot up server
            MyFTPServer server = new MyFTPServer(port);
            server.listen();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
