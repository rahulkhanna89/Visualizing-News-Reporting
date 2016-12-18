package com.newsvisualizer.visualization.dboperations;

import com.mongodb.*;
import com.newsvisualizer.visualization.beans.AccernData;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rahulkhanna on 08/12/16.
 */
public class QueryOperations {

    private final DBCollection collection;

    public QueryOperations() {
        Mongo client = null;
        try {
            client = new MongoClient("127.0.0.1");
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }
        DB db = client.getDB("iv");
        this.collection = db.getCollection("data");
    }


    public List<AccernData> getStoriesByGivenSector(String sector, int threshold, int sourceRank) {

        BasicDBObject sourceFetchQuery = new BasicDBObject();
        sourceFetchQuery.put("story_volume", new BasicDBObject("$gte", threshold));


        List<String> story_id = collection.distinct("story_id", sourceFetchQuery);
        BasicDBObject query = new BasicDBObject();
        query.put("entity_sector", sector);
        query.put("story_id", new BasicDBObject("$in", story_id));
        query.put("overall_source_rank", new BasicDBObject("$gt", sourceRank));
        DBCursor cursor = collection.find(query);
        cursor.sort(new BasicDBObject("story_id", 1));
        cursor.sort(new BasicDBObject("harvested_at", 1));
        System.out.println("cursor.hasNext() = " + cursor.hasNext());
        List<AccernData> dataToReturn = new ArrayList<>();
        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            AccernData data = new AccernData((String) object.get("article_id"), (String) object.get("story_id"),
                    (Date) object.get("harvested_at"), (String) object.get("entity_name"),
                    (String) object.get("entity_sector"), (String) object.get("story_name"),
                    (int) object.get("story_volume"),
                    (String) object.get("story_id"), (int) object.get("overall_source_rank"));
//            System.out.println("data.getSource_name() = " + data.getSource_name());
            dataToReturn.add(data);
//            System.out.println("data.toString() = " + data.toString());
        }
        return dataToReturn;
    }

    public static void main(String[] args) {
        QueryOperations ops = new QueryOperations();
        List<AccernData> storiesByGivenSector = ops.getStoriesByGivenSector("Technology", 300, 6);
        System.out.println("storiesByGivenSector = " + storiesByGivenSector.size());
    }

}
