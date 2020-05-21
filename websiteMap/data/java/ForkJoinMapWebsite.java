import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

public class ForkJoinMapWebsite extends RecursiveTask<List<String>> {
    private String nameWebsite;
    private Set<String> setLinks;
    private String mainLink;


    public ForkJoinMapWebsite(String nameWebsite, Set<String> setLinks, String mainLink) {
        this.nameWebsite = nameWebsite;
        this.setLinks = setLinks;
        this.mainLink = mainLink;
    }

    @Override
    protected List<String> compute() {
        List<String> mapWebsite = new ArrayList<>();
        mapWebsite.add(getNameWebsite());
        try {
            List<ForkJoinMapWebsite> listTask = new ArrayList<>();
            Document doc = Jsoup.connect(nameWebsite).maxBodySize(0).get();
            Thread.sleep(100);
            Elements urls = doc.body().getElementsByTag("a");
            for (Element url : urls) {
                String link = url.absUrl("href");
                if (link.startsWith(mainLink) && setLinks.add(link)) {
                    ForkJoinMapWebsite task = new ForkJoinMapWebsite(link, setLinks, mainLink);
                    task.fork();
                    listTask.add(task);

                }
            }
            for (ForkJoinMapWebsite list : listTask) {
                for (String string : list.join()) {
                    mapWebsite.add("\t" + string);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mapWebsite;
    }

    public String getNameWebsite() {
        return nameWebsite;
    }
}
