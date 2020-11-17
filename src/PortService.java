import java.io.IOException;
import java.io.Writer;
/*
set up IP and port for file transfer
 */
public class PortService implements Service {
    @Override
    public void getResponse(String data, Writer writer, RequestHandlerThread t) {
        System.out.println("--------executing PortService-------");
        System.out.println(data);

        String[] rawData = data.split(",");
        String ip = rawData[0];
        String port = rawData[1];

        t.setIP(ip);
        t.setPort(port);
        System.out.println("set up file transfer ip: "+ip);
        System.out.println("set up file transfer port: "+port);

        try {
            writer.write("200 ip and port for file transfer have been set up\r\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
