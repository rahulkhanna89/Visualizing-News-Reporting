package com.newsvisualizer.visualization.predataoperations;

import com.mongodb.*;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by rahulkhanna on 04/12/16.
 */
public class DataEnricher {

    private static final String SEPARATOR = ";";
    private static final Map<String, Integer> columnIndex;

    static {
        columnIndex = new HashMap<>();
        columnIndex.put("article_id", 0);
        columnIndex.put("story_id", 1);
        columnIndex.put("harvested_at", 2);
        columnIndex.put("entity_name", 3);
        columnIndex.put("entity_ticker", 4);
        columnIndex.put("entity_sector", 9);
        columnIndex.put("article_sentiment", 36);
        columnIndex.put(("story_name"), 5);
        columnIndex.put("story_sentiment", 29);
        columnIndex.put("story_volume", 33);
        columnIndex.put("event_author_rank", 40);
        columnIndex.put("article_url", 52);
    }

    private final Reader source;

    public DataEnricher(Reader source) {
        this.source = source;
    }

    private List<List<String>> readData() {
        List<List<String>> collect = null;
        try (BufferedReader reader = new BufferedReader(source)) {
            collect = reader.lines().skip(1).filter(line -> {
                List<String> data = Arrays.asList(line.split(","));
                if (data == null || data.size() == 0 || data.get(columnIndex.get("article_id")) == null ||
                        data.get(columnIndex.get("story_id")) == null ||
                        data.get(columnIndex.get("story_id")).isEmpty() ||
                        data.get(columnIndex.get("harvested_at")) == null ||
                        data.get(columnIndex.get("harvested_at")).isEmpty() ||
                        data.get(columnIndex.get("entity_name")) == null ||
                        data.get(columnIndex.get("entity_name")).isEmpty() ||
                        data.get(columnIndex.get("entity_ticker")) == null ||
                        data.get(columnIndex.get("entity_sector")) == null ||
                        data.get(columnIndex.get("entity_sector")).isEmpty() ||
                        data.get(columnIndex.get("article_sentiment")) == null ||
                        data.get(columnIndex.get("story_name")) == null ||
                        data.get(columnIndex.get("story_sentiment")) == null ||
                        data.get(columnIndex.get("story_sentiment")).isEmpty()||
                        data.get(columnIndex.get("story_volume")) == null ||
                        data.get(columnIndex.get("story_volume")).isEmpty() ||
                        data.get(columnIndex.get("event_author_rank")) == null ||
                        data.get(columnIndex.get("article_url")) == null ||
                        data.get(columnIndex.get("article_url")).isEmpty()) {
                    return false;
                } else {
                    return true;
                }
            }).map((line) -> {
                List<String> data = Arrays.asList(line.split(","));
                List<String> selectedData = new ArrayList<String>();
                selectedData.add(data.get(columnIndex.get("article_id")));
                selectedData.add(data.get(columnIndex.get("story_id")));
                selectedData.add(data.get(columnIndex.get("harvested_at")));
                selectedData.add(data.get(columnIndex.get("entity_name")));
                selectedData.add(data.get(columnIndex.get("entity_ticker")));
                selectedData.add(data.get(columnIndex.get("entity_sector")));
                selectedData.add(data.get(columnIndex.get("article_sentiment")));
                selectedData.add(data.get(columnIndex.get("story_name")));
                selectedData.add(data.get(columnIndex.get("story_sentiment")));
                selectedData.add(data.get(columnIndex.get("story_volume")));
                selectedData.add(data.get(columnIndex.get("event_author_rank")));
                String article_url = data.get(columnIndex.get("article_url"));
                String article_hostname = extractSourceNameFromURL(article_url);
                selectedData.add(article_hostname);
                System.out.println("article_hostname = " + article_hostname);
                return selectedData;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return collect;
    }

    private String extractSourceNameFromURL(String article_url) {
        int firstSlashIndex = article_url.indexOf("//") + 2;
        article_url = article_url.substring(firstSlashIndex);
        int indexOf2ndSlash = article_url.indexOf("/");
        String hostName = article_url;
        if (indexOf2ndSlash > 0) {
            hostName = article_url.substring(0, indexOf2ndSlash);
        }
        System.out.println("hostName = " + hostName);
        if (hostName.startsWith("www")) {
            hostName = hostName.substring(4);
        }
        if (hostName.endsWith("com") || hostName.endsWith("org") || hostName.endsWith("net")) {
            System.out.println("ends with com");
            hostName = hostName.substring(0, hostName.length() - 4);
        } else if (hostName.contains("co") && hostName.indexOf("co") > 0) {
            hostName = hostName.substring(0, hostName.indexOf("co") - 1);
        }

        return hostName;
    }

    private void writeData(List<List<String>> inputData, String fileName) {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            String header = "article_id,story_id,harvested_at,entity_name,entity_ticker,entity_sector,article_sentiment,story_name,story_sentiment,story_volume,event_author_rank,article_url\n";
            fileWriter.append(header);
            for (List<String> data : inputData) {
                fileWriter.append(data.toString());
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void saveDataInDB(List<List<String>> dataToSave) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            DBCollection collection = getDbCollection();
            List<DBObject> dbObj = new ArrayList<>();
            dataToSave.stream().forEach(d -> {
                DBObject obj = new BasicDBObject();
                obj.put("article_id", d.get(0));
                obj.put("story_id", d.get(1));
                try {
                    obj.put("harvested_at", format.parse(d.get(2)));
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
                obj.put("entity_name", d.get(3));
                obj.put("entity_ticker", d.get(4));
                obj.put("entity_sector", d.get(5));
                obj.put("article_sentiment", d.get(6));
                obj.put("story_name", d.get(7));
                obj.put("story_sentiment", d.get(8));
                obj.put("story_volume", Integer.parseInt(d.get(9)));
                obj.put("event_author_rank", Integer.parseInt(d.get(10)));
                obj.put("source_name", d.get(11));
//                obj.put("source_name", d.get(12));
                collection.insert(obj);
            });


        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }
    }

    private DBCollection getDbCollection() throws UnknownHostException {
        Mongo client = new MongoClient("127.0.0.1");
        DB db = client.getDB("iv");
        return db.getCollection("data");
    }

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("data2.csv");
        Reader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"));
        DataEnricher enricher = new DataEnricher(reader);
        List<List<String>> cleanData = enricher.readData();
        //enricher.writeData(cleanData, "cleandata.csv");
        enricher.saveDataInDB(cleanData);
//        enricher.getDbCollection().drop();

        System.out.println(enricher.getDbCollection().count());

    }
}
