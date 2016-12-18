package com.newsvisualizer.visualization.dataprocessing;

import com.newsvisualizer.visualization.beans.AccernData;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by rahulkhanna on 17/12/16.
 */
public class RankProcessor {


    public static Map<Integer, Map<String, Double>> populateRank(List<AccernData> data) {
        Map<String, List<AccernData>> articleByStoriesId = new HashMap<>();
        data.stream().forEach(article -> {
            if (articleByStoriesId.containsKey(article.getStory_id())) {
                articleByStoriesId.get(article.getStory_id()).add(article);
            } else {
                List<AccernData> articles = new ArrayList<>();
                articles.add(article);
                articleByStoriesId.put(article.getStory_id(), articles);
            }
        });
        Map<String, Map<Integer, List<AccernData>>> articlesByRankForAStory = new HashMap<>();
        List<AccernData> processedData = new ArrayList<AccernData>();
        articleByStoriesId.forEach((s, datas) -> {
            Collections.sort(datas);
            AccernData fastestArticle = datas.get(0);
            fastestArticle.setShapeAssigned("Circle");
            fastestArticle.setSourceScore(100);
            System.out.println("datas = " + datas.size());
//            if (datas.size() > 1) {
                long timeRangeForThisStory = datas.get(datas.size() - 1).getHarvested_at().getTime() - fastestArticle.getHarvested_at().getTime();
                Map<Integer, Map<String, Date>> timeRangePerRank = generateTimeCutOffPerRank(timeRangeForThisStory, fastestArticle.getHarvested_at().getTime());
                Map<Integer, List<AccernData>> articlesPerRankForAStory = new HashMap<>();
                timeRangePerRank.forEach((rank, range) -> {
                    System.out.println("Filtering articles for rank = " + rank);
                    List<AccernData> dataForGivenRank = datas.stream().filter((article) -> article.getHarvested_at().after(range.get("startTime")) && article.getHarvested_at().before(range.get("endTime"))
                    ).map(article -> {
                        article.setSourceRank(rank);
                        return article;
                    }).collect(Collectors.toList());
                    articlesPerRankForAStory.put(rank, dataForGivenRank);
                    processedData.addAll(dataForGivenRank);
                    System.out.println("Number of articles for rank "+rank+" = "+dataForGivenRank.size());
                });
                articlesByRankForAStory.put(s, articlesPerRankForAStory);
//            }
        });

        Map<Integer, Map<String, Double>> ranksByMonth = sourceRankByMonth(processedData);
        return ranksByMonth;
    }

    private static Map<Integer, Map<String, Double>> sourceRankByMonth(List<AccernData> articlesByRankForAStory) {
        Map<Integer, Map<String, Double>> sourceRanksByMonth = new HashMap<>();
        for (int i = 0; i < 12; i++) {
            int monthNeeded = i;
            List<AccernData> articlesByMonth = articlesByRankForAStory.stream().filter(article -> {
                Calendar cal = Calendar.getInstance();
                cal.setTime(article.getHarvested_at());
                return cal.get(Calendar.MONTH) == monthNeeded;
            }).collect(Collectors.toList());
            Map<String, List<AccernData>> articlesByMonthAndSource = new HashMap<>();
            articlesByMonth.stream().forEach(article -> {
                if (articlesByMonthAndSource.containsKey(article.getSource_name())) {
                    articlesByMonthAndSource.get(article.getSource_name()).add(article);
                } else {
                    List<AccernData> articlesBySource = new ArrayList<>();
                    articlesBySource.add(article);
                    articlesByMonthAndSource.put(article.getSource_name(), articlesBySource);
                }
            });
            Map<String, Double> sourceRankForAMonth = new HashMap<>();

            articlesByMonthAndSource.forEach((sourceName, articles) -> {
                int sourceRank = 0;
                for (AccernData article : articles) {
                    sourceRank += article.getSourceRank();
                }
                System.out.println("month = " + monthNeeded);
                System.out.print("sourceName = " + sourceName);
                System.out.println(" sourceRank = " + sourceRank);
                System.out.println("articles.size() = " + articles.size());
                sourceRankForAMonth.put(sourceName, (double) (sourceRank / articles.size()));
            });
            sourceRanksByMonth.put(monthNeeded, sourceRankForAMonth);
        }
        return sourceRanksByMonth;
    }

    private static Map<Integer, Map<String, Date>> generateTimeCutOffPerRank(long timeRangeForThisStory, long startTime) {
        Map<Integer, Map<String, Date>> timeRangePerRank = new HashMap<>(8);
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(startTime);
        for (int i = 8; i >= 1; i--) {
            Map<String, Date> dateRange = new HashMap<>();
            long currentStartTime = startTime;
            dateRange.put("startTime", start.getTime());
            double factor = (float) (1 / Math.pow(2.0, i));
            System.out.println("factor = " + factor);
            long cutoffTime = (long) (startTime + timeRangeForThisStory * factor);
            Calendar endTime = Calendar.getInstance();
            endTime.setTimeInMillis(cutoffTime);
            dateRange.put("endTime", endTime.getTime());
            startTime = endTime.getTimeInMillis();
            timeRangePerRank.put(i, dateRange);
        }
        return timeRangePerRank;
    }

}
