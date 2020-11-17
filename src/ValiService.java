import java.io.IOException;
import java.io.Writer;

/*
 validate the existence of the user based on username
 */
public class ValiService implements Service{
    /**
     *
     * @param data      data input from client
     * @param writer    outputstream witer to client
     * @param t         thread to handle the requested command
     */
    @Override
    public void getResponse(String data, Writer writer, RequestHandlerThread t){
        System.out.println("------executing ValiService--------");
        System.out.println(data);

        String username = data;

        String response;
        if(ThreadScopeData.users.containsKey(username)){

            //claim the ownership of the thread t for the username
            t.USER.set(username);

            response = "331 please input your password";
        }else{
            response = "501 this username does not exist";
        }

        //set up response
        try {
            writer.write(response + "\r\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
