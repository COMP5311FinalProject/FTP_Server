import java.io.IOException;
import java.io.Writer;

/*
  verify username and password
 */

public class LoginService implements Service {
    /**
     *
     * @param data      data input from client
     * @param writer    outputstream witer to client
     * @param t         thread to handle the requested command
     */
    @Override
    public void getResponse(String data, Writer writer, RequestHandlerThread t) {
        System.out.println("------executing LoginService-------");
        System.out.println(data);

        String inputPW = data;

        String response;

        String username = t.USER.get();
        String password = ThreadScopeData.users.get(username);

        if(inputPW.equals(password)){
            ThreadScopeData.userLogined.add(username);
            t.setIsLogin(true);
            response = "230 User(" + username + ") has logged in";
        }else{
            response = "530 wrong password";
        }

        try {
            writer.write(response+"\r\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
