package com.newsvisualizer.visualization.dataprocessing;

import com.newsvisualizer.visualization.beans.AccernData;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Created by rahulkhanna on 17/12/16.
 */
public class RankProcessor {


    public static void populateRank(List<AccernData> data) {
        Map<String, List<AccernData>> articleByStoriesId = new HashMap<>();
        data.stream().forEach(article -> {
            if (articleByStoriesId.containsKey(article.getStory_id())) {
                articleByStoriesId.get(article.getStory_id()).add(article);
            }else{
                List<AccernData> articles = new ArrayList<>();
                articles.add(article);
                articleByStoriesId.put(article.getStory_id(), articles);
            }
        });
        articleByStoriesId.forEach((s, datas) -> {
            Collections.sort(datas);
            datas.get(0).setShapeAssigned("Circle");
        });
    }

}
