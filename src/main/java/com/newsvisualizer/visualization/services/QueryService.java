package com.newsvisualizer.visualization.services;

import com.mongodb.*;
import com.newsvisualizer.visualization.beans.AccernData;
import com.newsvisualizer.visualization.dataprocessing.RankProcessor;
import com.newsvisualizer.visualization.dboperations.QueryOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rahulkhanna on 08/12/16.
 */
@RestController
public class QueryService {

    private QueryOperations queryOps;

    public QueryService() {
        this.queryOps = new QueryOperations();
    }

    @CrossOrigin(origins = "http://localhost:8000")
    @RequestMapping(value = "/databysector")
    public List<AccernData> getDataBySector(@RequestParam(value = "sector") String sector, @RequestParam(value = "threshold") int threshold) {
        System.out.println("sector " + sector + " threshold = " + threshold);
        List<AccernData> storiesByGivenSector = queryOps.getStoriesByGivenSector(sector, threshold,6);
        RankProcessor.populateRank(storiesByGivenSector);
        System.out.println("storiesByGivenSector.size() = " + storiesByGivenSector.size());
        return storiesByGivenSector;
    }

}
