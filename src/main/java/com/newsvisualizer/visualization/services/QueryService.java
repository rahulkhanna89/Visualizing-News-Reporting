package com.newsvisualizer.visualization.services;

import com.newsvisualizer.visualization.beans.AccernData;
import com.newsvisualizer.visualization.dataprocessing.RankProcessor;
import com.newsvisualizer.visualization.dboperations.QueryOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by rahulkhanna on 08/12/16.
 */
@RestController
public class QueryService {

    private QueryOperations queryOps;

    private Map<String, Map<Integer, Double>> fetchedData;

    private final Map<String, Map<Integer, Map<String, Object>>> cache;


    public Map<String, Map<Integer, Double>> getFetchedData() {
        return fetchedData;
    }

    public void setFetchedData(Map<String, Map<Integer, Double>> fetchedData) {
        this.fetchedData = fetchedData;
    }

    public QueryService() {
        this.queryOps = new QueryOperations();
        this.cache = new HashMap<>();
    }


    @CrossOrigin(origins = "http://localhost:8000")
    @RequestMapping(value = "/databysector")
    public List<AccernData> getDataBySector(@RequestParam(value = "sector") String sector, @RequestParam(value = "threshold") int threshold) {
        System.out.println("sector " + sector + " threshold = " + threshold);
        List<AccernData> dataToReturn = new ArrayList<>();
        if (cache.containsKey(sector)) {
            Map<Integer, Map<String, Object>> dataOfASector = cache.get(sector);
            if (dataOfASector.containsKey(threshold)) {
                System.out.println("Got data in cache for sector =" + sector + " and threshold = " + threshold);
                dataToReturn.clear();
                dataToReturn.addAll((Collection<? extends AccernData>) cache.get(sector).get(threshold).get("mainViewData"));
                setFetchedData((Map<String, Map<Integer, Double>>) cache.get(sector).get(threshold).get("auxilaryData"));
            } else {
                System.out.println("Got data in cache for sector =" + sector + " but not for threshold = " + threshold);
                dataToReturn.clear();
                Map<String, Object> processedData = fetchData(sector, threshold);
                dataToReturn.addAll((Collection<? extends AccernData>) processedData.get("mainViewData"));
                setFetchedData((Map<String, Map<Integer, Double>>) processedData.get("auxilaryData"));
                dataOfASector.put(threshold, processedData);
            }
        } else {
            dataToReturn.clear();
            Map<Integer, Map<String, Object>> dataByThreshold = new HashMap<>();
            Map<String, Object> processedData = fetchData(sector, threshold);
            dataToReturn.addAll((Collection<? extends AccernData>) processedData.get("mainViewData"));
            setFetchedData((Map<String, Map<Integer, Double>>) processedData.get("auxilaryData"));
            dataByThreshold.put(threshold, processedData);
            cache.put(sector, dataByThreshold);
        }

        return dataToReturn;
    }

    private Map<String, Object> fetchData(@RequestParam(value = "sector") String sector, @RequestParam(value = "threshold") int threshold) {
        List<AccernData> storiesByGivenSector = queryOps.getStoriesByGivenSector(sector, threshold, 6);
        Map<String, Object> processedData = RankProcessor.populateRank(storiesByGivenSector);
        setFetchedData((Map<String, Map<Integer, Double>>) processedData.get("auxilaryData"));
        System.out.println("storiesByGivenSector.size() = " + storiesByGivenSector.size());
        return processedData;
    }

    @CrossOrigin(origins = "http://localhost:8000")
    @RequestMapping(value = "/getSourceRankData")
    public Map<String, Map<Integer, Double>> getSourceRankData() {
        return getFetchedData();
    }

}
