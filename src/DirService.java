import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Vector;

/*
obtain list of file info in in root directory
 */
public class DirService implements Service {
    @Override
    public void getResponse(String data, Writer writer, RequestHandlerThread t) {
        System.out.println("-------executing DirService-------");
        System.out.println(data);

        String rootDir = t.getRootDir();
        File rootDirF = new File(rootDir);
        if(!rootDirF.exists()){
            //root directory does not exist
            try {
                writer.write("210 cannot find root directory");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            File[] fs = rootDirF.listFiles();
            String flag;
            //using Vector<> to store iterated fileInfo, for it is thread-safe.
            Vector<String> allFileInfo = new Vector<>();
            for(File f : fs){
                if(f.isDirectory()){
                    flag = "d";
                }else{
                    flag = "f";
                }
                String fileInfo = flag+"rw-rw-rw-   1 ftp      ftp            "+f.length()+" Dec 30 17:07 "+f.getName();
                System.out.println(fileInfo);
                allFileInfo.add(fileInfo);
            }

            try {
                writer.write("150 Opening channel for rootDir file info list transfer");
                writer.write("\r\n");
                writer.flush();

                for(String fileInfo : allFileInfo){
                    writer.write(fileInfo);
                    writer.write("\r\n");
                    writer.flush();
                }

                writer.write("end of files\r\n");
                writer.flush();

                writer.write("226 file info list transfer complete\r\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }




    }
}
