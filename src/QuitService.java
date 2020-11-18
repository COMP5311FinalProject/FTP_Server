import java.io.IOException;
import java.io.Writer;
/*
Terminate connection with client
 */
public class QuitService implements Service{
    @Override
    public void getResponse(String data, Writer writer, RequestHandlerThread t) {
        System.out.println("------executing QuitService------");

        try {
            writer.write("221 goodbye client\r\n");
            writer.flush();
            writer.close();
            t.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
