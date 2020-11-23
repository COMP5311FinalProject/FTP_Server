import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


/*
 * Data shared by all threads that handle client request
 * data are extracted from xml configuration files
 *
 */
public class ThreadScopeData {

    //root directory
    public static String rootDir = new File("").getAbsolutePath();

    //users authorized to login
    public static Map<String,String> users = new HashMap<>();

    //users who have logged in
    public static HashSet<String> userLogined = new HashSet<>();

    //server port NO
    public static String port = null;

    //initialize
    public static void init(){

        System.out.println("==========initializing ThreadScopeData========");
        //concatenate project path with config file's path
        String path = System.getProperty("user.dir") + "/config/serverconfig.xml";

        File file = new File(path);
        SAXBuilder builder = new SAXBuilder();

        try{
            Document parse = builder.build(file);
            Element root = parse.getRootElement();

            //initialize root directory for threads
            rootDir = root.getChildText("rootDir");

            //initialize server port info
            port = root.getChildText("port");

            //initialize authorized users' info
            List<Element> usersEleList = root.getChild("users").getChildren();
            for(Element each : usersEleList){
                users.put(each.getChildText("username"),each.getChildText("password"));
            }

        }catch (IOException | JDOMException e){
            e.printStackTrace();
        }

    }

}
