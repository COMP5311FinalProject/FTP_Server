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
                sPort = (int)(Math.random()*(9999-1025+1))+1025;
                serverSocket = new ServerSocket(sPort);
            }

            if(serverSocket != null && sPort != -1){
                //encrypt sPort with into 2 tokens for client to decipher
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

            //start a new thread to complete data transfer.
            //serverSocket是启动在服务器端的。这里的dataSocket是客户端请求过来的。
            //这个dataSocket就是传输data的socket，与之相对应的是传输command的socket
            Socket dataSocket = serverSocket.accept();
            /**
             * 在当前线程上设置被动模式的socket,在执行RETR等命令的时候，
             * 先检查pasvSocket是否为空，若为空，说明为主动模式.详情看RETRService
             */
            t.setPasvSocket(dataSocket);
//            RequestHandlerThread dataThread = new RequestHandlerThread(dataSocket,"data");
//            dataThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
