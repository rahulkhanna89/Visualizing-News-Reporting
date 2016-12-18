package com.newsvisualizer.visualization.services;

import com.newsvisualizer.visualization.beans.AccernData;
import com.newsvisualizer.visualization.dataprocessing.RankProcessor;
import com.newsvisualizer.visualization.dboperations.QueryOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by rahulkhanna on 08/12/16.
 */
@RestController
public class QueryService {

    private QueryOperations queryOps;

    private Map<String, Map<Integer, Double>> fetchedData;

    public Map<String, Map<Integer, Double>> getFetchedData() {
        return fetchedData;
    }

    public void setFetchedData(Map<String, Map<Integer, Double>> fetchedData) {
        this.fetchedData = fetchedData;
    }

    public QueryService() {
        try {
            this.queryOps = new QueryOperations();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    @CrossOrigin(origins = "http://localhost:8000")
    @RequestMapping(value = "/databysector")
    public List<AccernData> getDataBySector(@RequestParam(value = "sector") String sector, @RequestParam(value = "threshold") int threshold) {
        System.out.println("sector " + sector + " threshold = " + threshold);
        List<AccernData> storiesByGivenSector = queryOps.getStoriesByGivenSector(sector, threshold, 6);
        Map<String, Object> processedData = RankProcessor.populateRank(storiesByGivenSector);
        setFetchedData((Map<String, Map<Integer, Double>>) processedData.get("auxilaryData"));
        System.out.println("storiesByGivenSector.size() = " + storiesByGivenSector.size());
        List<AccernData> dataToReturn = new ArrayList<>();
        dataToReturn.addAll((Collection<? extends AccernData>) processedData.get("mainViewData"));
        return dataToReturn ;
    }

    @RequestMapping(value = "/getSourceRankData")
    public Map<String, Map<Integer, Double>> getSourceRankData(){
        return getFetchedData();
    }

}
