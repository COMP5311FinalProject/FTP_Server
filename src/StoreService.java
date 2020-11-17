

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.net.Socket;

/*
Store files from clients into work directory
 */
public class StoreService implements Service{
    @Override
    public void getResponse(String data, Writer writer, RequestHandlerThread t) {
        System.out.println("------executing StoreService-------");
        System.out.println(data);

        try {
            writer.write("150 Binary mode transfer\r\n");
            writer.flush();

            //set up RandomAccessFile object to write in input stream
            String inputFileDir = t.getRootDir() + "/" + data;
            RandomAccessFile inputFile = new RandomAccessFile(inputFileDir,"rw");

            Socket fSocket = new Socket(t.getIP(),Integer.parseInt(t.getPort()));
            InputStream input = fSocket.getInputStream();
            byte[] buffer = new byte[1024];
            //number of bytes read into buffer
            int bytesRead = 0;
            while ((bytesRead = input.read(buffer)) != -1){
                //write input data into work directory
                inputFile.write(buffer,0,bytesRead);
            }

            inputFile.close();
            fSocket.close();
            input.close();

            writer.write("226 file transfer to server complete\r\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}