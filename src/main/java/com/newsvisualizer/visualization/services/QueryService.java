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
 * This class represents the service which will be called by the frontend UI.
 * <p>
 * Created by rahulkhanna on 08/12/16.
 */
@RestController
public class QueryService {

    private QueryOperations queryOps;

    private Map<String, Map<Integer, Double>> fetchedData;
    /**
     * This is the cache object which keeps the processed data for main and auxiliary view.
     */
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

    /**
     * This service is called by the main view of the UI.
     *
     * @param sector    of the data
     * @param threshold number of articles which a story must have.
     * @return
     */
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

    /**
     * If the data is not present in the cache, this function will be used to fetch it from MongoDB.
     *
     * @param sector
     * @param threshold
     * @return data corresponding to a sector passing the threshold.
     */
    private Map<String, Object> fetchData(@RequestParam(value = "sector") String sector, @RequestParam(value = "threshold") int threshold) {
        List<AccernData> storiesByGivenSector = queryOps.getStoriesByGivenSector(sector, threshold, 6);
        Map<String, Object> processedData = RankProcessor.populateRank(storiesByGivenSector);
        setFetchedData((Map<String, Map<Integer, Double>>) processedData.get("auxilaryData"));
        System.out.println("storiesByGivenSector.size() = " + storiesByGivenSector.size());
        return processedData;
    }

    /**
     * This service is called for getting the data for the auxiliary view. This service only returns the processed data.
     *
     * @return
     */
    @CrossOrigin(origins = "http://localhost:8000")
    @RequestMapping(value = "/getSourceRankData")
    public Map<String, Map<Integer, Double>> getSourceRankData() {
        return getFetchedData();
    }

}
