import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static String txtFoSearch;
    private static ExecutorService poolThreads;

    public static void main(String[] args) throws IOException {

        System.out.println("Текст для поиска: ");
        txtFoSearch = getTxtFoSerach();

        poolThreads = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4);

        for (File f : File.listRoots()) {
            System.out.println(f);
            list(f.getPath());
        }
    }

    static void list(String nameDir) {

        File file = new File(nameDir);
        String[] dirList = file.list();

        if (file.list() != null) {
            for (int i = 0; i < dirList.length; i++) {
                File tmpFile = new File(nameDir + File.separator + dirList[i]);

                if (tmpFile.isFile()) {
                    poolThreads.submit(new TaskFind(txtFoSearch, nameDir + File.separator + dirList[i]));
                } else {
                    list(nameDir + File.separator + dirList[i]);
                }
            }
        }
    }

    static public String getTxtFoSerach() throws IOException {

        String txtFoSearch = new BufferedReader(new InputStreamReader(System.in)).readLine();
        return txtFoSearch;
    }
}
class TaskFind implements Runnable {

    String fileName, findTxt;

    public TaskFind( String findTxt, String fileName ) {

        this.fileName = fileName;
        this.findTxt = findTxt;

    }

    public void run() {

        String s;

        try
        {
            LineNumberReader lineNumberReader = new LineNumberReader( new BufferedReader( new FileReader(fileName)));
            while(true)
            {
                s = lineNumberReader.readLine();
                if (s == null) break;

                if(s.indexOf(findTxt) != -1)
                {
                    System.out.println("нашли " + findTxt + " в: " +fileName);
                    break;
                }
            }

            lineNumberReader.close();

        } catch ( Exception e ) {
            System.out.println( e.toString() );
        }
    }
}