package com.newsvisualizer.visualization.beans;

import java.util.Date;

/**
 * Created by rahulkhanna on 09/12/16.
 */
public class AccernData {

    private final String article_id;
    private final String story_id;
    private final Date harvested_at;
    private final String entity_name;
    private final String entity_ticker;
    private final String entity_sector;
    private final String article_sentiment;
    private final String story_name;
    private final String story_sentiment;
    private final int story_volume;
    private final int event_author_rank;
    private final String source_name;

    public int getOverall_source_rank() {
        return overall_source_rank;
    }

    private final int overall_source_rank;

    public AccernData(String article_id, String story_id, Date harvested_at, String entity_name, String entity_ticker, String entity_sector, String article_sentiment, String story_name, String story_sentiment, int story_volume, int event_author_rank, String source_name, int overall_source_rank) {
        this.article_id = article_id;
        this.story_id = story_id;
        this.harvested_at = harvested_at;
        this.entity_name = entity_name;
        this.entity_ticker = entity_ticker;
        this.entity_sector = entity_sector;
        this.article_sentiment = article_sentiment;
        this.story_name = story_name;
        this.story_sentiment = story_sentiment;
        this.story_volume = story_volume;
        this.event_author_rank = event_author_rank;
        this.source_name = source_name;
        this.overall_source_rank = overall_source_rank;
    }

    public String getArticle_id() {
        return article_id;
    }

    public String getStory_id() {
        return story_id;
    }

    public Date getHarvested_at() {
        return harvested_at;
    }

    public String getEntity_name() {
        return entity_name;
    }

    public String getEntity_ticker() {
        return entity_ticker;
    }

    public String getEntity_sector() {
        return entity_sector;
    }

    public String getArticle_sentiment() {
        return article_sentiment;
    }

    public String getStory_name() {
        return story_name;
    }

    @Override
    public String toString() {
        return "AccernData{" +
                "article_id='" + article_id + '\'' +
                ", story_id='" + story_id + '\'' +
                ", harvested_at=" + harvested_at +
                ", entity_name='" + entity_name + '\'' +
                ", entity_ticker='" + entity_ticker + '\'' +
                ", entity_sector='" + entity_sector + '\'' +
                ", article_sentiment='" + article_sentiment + '\'' +
                ", story_name='" + story_name + '\'' +
                ", story_sentiment='" + story_sentiment + '\'' +
                ", story_volume=" + story_volume +
                ", event_author_rank=" + event_author_rank +
                ", source_name='" + source_name + '\'' +
                ", overall_source_rank=" + overall_source_rank +
                '}';
    }

    public String getStory_sentiment() {
        return story_sentiment;
    }

    public int getStory_volume() {
        return story_volume;
    }

    public int getEvent_author_rank() {
        return event_author_rank;
    }

    public String getSource_name() {
        return source_name;
    }

}
