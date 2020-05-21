import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static List<Line> lines;
    public static List<String> stations;
    public static String check;
    public static String dataFile = "Date//Line.json";
    public static List<Line> linesAfter;

    public static void main(String[] args) throws IOException {
        lines = new ArrayList<>();
        stations = new ArrayList<>();
        linesAfter = new ArrayList<>();

        Document document = Jsoup.connect("https://ru.wikipedia.org/wiki/Список_станций_Московского_метрополитена").maxBodySize(0).get();
        Elements elements = document.select("table.standard.sortable").first().select("tbody").select("tr");

        check = elements.get(1).select("td").get(0).select("span").get(1).attr("title");
        for (int i = 1; i < elements.size(); i++) {
            String nameLine = (elements.get(i).select("td").get(0).select("span").get(1).attr("title"));
            String station = (elements.get(i).select("td").get(1).select("span").text());
            setLine(nameLine, station);

            check = nameLine;
        }
        joinLine(11, 12);


        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        FileWriter fileWriter = new FileWriter("Date//Line.json");
        gson.toJson(lines, fileWriter);
        fileWriter.close();

        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonData = (JSONArray) parser.parse(getJsonFile());
            for (int i = 0; i < jsonData.size(); i++) {
                JSONObject jsonObject = (JSONObject) jsonData.get(i);
                parseLines(jsonObject);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        System.out.println("Количество станции Московского метро");
        linesAfter.stream().forEach(element -> {
            System.out.println(element.name + ": " + element.station.size());
        });

    }

    private static void parseLines(JSONObject linesArray) {
        Line line = new Line(
                ((String) linesArray.get("name")),
                (List<String>) linesArray.get("station")
        );
        linesAfter.add(line);
    }

    private static String getJsonFile() {
        StringBuilder builder = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(dataFile));
            lines.forEach(builder::append);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return builder.toString();
    }

    public static void joinLine(int line1, int line2) {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(lines.get(line1).station);
        list.addAll(lines.get(line2).station);
        Line line = new Line(lines.get(11).name, list);
        lines.add(line);
        if (line1 < line2) {
            lines.remove(lines.get(line1));
            lines.remove(lines.get(line2 - 1));
        } else {
            lines.remove(lines.get(line2));
            lines.remove(lines.get(line1 - 1));
        }
    }

    public static void setLine(String nameLine, String station) {
        if (station.trim().length() == 0) {
        } else if (check.equals(nameLine)) {
            stations.add(station);
        } else {
            Line line = new Line(check, stations);
            lines.add(line);
            stations = new ArrayList<>();
            stations.add(station);
        }

    }
}


