import java.io.IOException;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

/*
set up IP and port for file transfer
 */
public class PortService implements Service {
    @Override
    public void getResponse(String data, Writer writer, RequestHandlerThread t) {
        System.out.println("--------executing PortService-------");
        System.out.println(data);

        String[] rawData = data.split(",");
        String dataIp = rawData[0];
        String dataPort = rawData[1];


        t.setDataIP(dataIp);
        t.setDataPort(dataPort);

        System.out.println("set up file transfer ip: "+dataIp);
        System.out.println("set up file transfer port: "+dataPort);

        try {
            writer.write("200 ip and port for file transfer have been set up\r\n");
            writer.flush();
            System.out.println(t.getDataPort());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
