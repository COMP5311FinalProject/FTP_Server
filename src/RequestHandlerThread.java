import java.io.*;
import java.net.Socket;

/**
 * Thread that handles connection request from clients
 *
 *
 */
public class RequestHandlerThread extends Thread{

    public final ThreadLocal<String> USER = new ThreadLocal<String>();

    private String rootDir = ThreadScopeData.rootDir;

    //server-side socket
    private Socket socket;

    //number of times the client send request
    private int count = 0;

    private String dataIP;

    private String dataPort;

    private String mode = "control";

    //login status
    private boolean isLogin = false;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public RequestHandlerThread(Socket socket){
        this.socket = socket;
    }

    public RequestHandlerThread(Socket socket, String mode){
        this.socket = socket;
        this.mode = mode;
    }

    public void setIsLogin(boolean isLogin){
        this.isLogin = isLogin;
    }

    public boolean getIsLogin() {
        return isLogin;
    }

    public void setDataIP(String dataIP) {
        this.dataIP = dataIP;
    }

    public String getDataIP() {
        return dataIP;
    }

    public String getDataPort() {
        return dataPort;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setDataPort(String dataPort) {
        this.dataPort = dataPort;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void run(){
        System.out.println("=======new client connected======");
        System.out.println("mode: " + this.getMode());

        BufferedReader reader;
        Writer writer;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new OutputStreamWriter(socket.getOutputStream());

            //awaits client request
            while(true){

                if(count == 0){
                    //first request would contain no data
                    writer.write("220 connection success.\r\n");
                    writer.flush();
                    count++;
                }else{
                    if(socket.isClosed()){
                        //socket closed, no more incoming requests. Terminate
                        break;
                    }
                    String request = reader.readLine();
                    if(request == null){
                        throw new IOException("ERROR: Empty Request");
                    }
                    //analyze the requested service type
                    String[] rawData = request.split(" ");
                    String serviceType = rawData[0];
                    Service service = ServiceFactory.createService(serviceType);

                    //verify login status. calling ValiService and LoginService does not require login
                    if (isLogin || service instanceof ValiService || service instanceof LoginService){
                        if(service == null){
                            String response = "502 cannot resolve requested service type";
                        }else{
                            //extract param data if any
                            String data = "";
                            if(rawData.length >= 2){
                                data = rawData[1];
                            }
                            //run requested service
                            service.getResponse(data,writer,this);
                        }
                    }else{
                        String response = "532 please login first";
                        writer.write(response);
                        writer.flush();
                    }
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("Execution completed. Connection terminated");
        }
    }



}
