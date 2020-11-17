import java.io.*;
import java.net.Socket;

/*
Transfer file from work directory to client
 */
public class RetrService implements Service {
    @Override
    public void getResponse(String data, Writer writer, RequestHandlerThread t) {
        System.out.println("-------executing RetrService-------");
        System.out.println(data);

        String scDir = t.getRootDir() + File.separator + data;
        File scDirF= new File(scDir);

        try {
            if(scDirF.exists()){
                //utilizing ASCII mode of FTP
                writer.write("150 ASCII mode transfer\r\n");
                writer.flush();

                //set up socket for file transfer
                Socket fSocket = new Socket(t.getIP(),Integer.parseInt(t.getPort()));

                //declare input and output stream, and stream buffer for file transfer
                BufferedOutputStream output = new BufferedOutputStream(fSocket.getOutputStream());
                InputStream input = new FileInputStream(scDirF);
                byte[] buffer = new byte[1024];
                //number of bytes read into the buffer
                int bytesRead = 0;
                while((bytesRead = input.read(buffer)) != -1){
                    output.write(buffer,0,bytesRead);
                }
                output.flush();
                input.close();
                fSocket.close();

                writer.write("220 file transfer to client complete\r\n");
                writer.flush();
            } else{
                writer.write("220 requested file does not exist\r\n");
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
