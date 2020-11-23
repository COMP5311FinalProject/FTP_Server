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

        //initialize data required to run handler threads;
        ThreadScopeData.init();

        //get control port NO.
        int port = Integer.parseInt(ThreadScopeData.port);

        //boot up server
        MyFTPServer server = new MyFTPServer(port);

        try {
            server.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
