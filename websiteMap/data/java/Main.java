import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {

        try (PrintWriter fileListLinks = new PrintWriter("data/fileListLinks.txt")) {
            String nameWebsite = "https://secure-headland-59304.herokuapp.com/";
            Set<String> setLinks = ConcurrentHashMap.newKeySet();
            setLinks.add(nameWebsite);
            List<String> listLinks = new ForkJoinPool().invoke(new ForkJoinMapWebsite(nameWebsite, setLinks, nameWebsite));

            for (String link : listLinks) {
                fileListLinks.write(link + "\n");
            }
            fileListLinks.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
