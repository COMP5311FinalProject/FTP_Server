import java.io.Writer;
interface Service {
    /**
     * @param data      data input from client
     * @param writer    outputstream witer to client
     * @param t         thread to handle the requested command
     *
     */

    public void getResponse(String data,Writer writer,RequestHandlerThread t);
}
