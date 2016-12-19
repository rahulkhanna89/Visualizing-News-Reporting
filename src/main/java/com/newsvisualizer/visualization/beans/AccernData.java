package com.newsvisualizer.visualization.beans;

import java.util.Date;

/**
 * This is the pojo class representing Accern's data.
 * <p>
 * Created by rahulkhanna on 09/12/16.
 */
public class AccernData implements Comparable<AccernData> {

    private final String article_id;
    private final String story_id;
    private final Date harvested_at;
    private final String entity_name;
    private final String entity_sector;
    private final String story_name;
    private final int story_volume;
    private final String source_name;
    private String shapeAssigned = "Triangle";
    private final int overall_source_rank;
    private int sourceScore = 1;
    private int sourceRank = 0;

    /**
     * Constructor
     *
     * @param article_id          unique for an article
     * @param story_id            id for a story which can be same for group of articles.
     * @param harvested_at        time at which the article was reported.
     * @param entity_name         name of the entity involved in the article/story.
     * @param entity_sector       name of the sector in which entity belongs.
     * @param story_name
     * @param story_volume        number of articles reported in a story.
     * @param source_name         name of the source which reported the article.
     * @param overall_source_rank
     */
    public AccernData(String article_id, String story_id, Date harvested_at, String entity_name, String entity_sector, String story_name, int story_volume, String source_name, int overall_source_rank) {
        this.article_id = article_id;
        this.story_id = story_id;
        this.harvested_at = harvested_at;
        this.entity_name = entity_name;
        this.entity_sector = entity_sector;
        this.story_name = story_name;
        this.story_volume = story_volume;
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


    public String getEntity_sector() {
        return entity_sector;
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
                ", entity_sector='" + entity_sector + '\'' +
                ", story_name='" + story_name + '\'' +
                ", story_volume=" + story_volume +
                ", source_name='" + source_name + '\'' +
                ", shapeAssigned='" + shapeAssigned + '\'' +
                ", overall_source_rank=" + overall_source_rank +
                ", sourceScore=" + sourceScore +
                ", sourceRank=" + sourceRank +
                '}';
    }

    public int getStory_volume() {
        return story_volume;
    }


    public String getSource_name() {
        return source_name;
    }

    public String getShapeAssigned() {
        return shapeAssigned;
    }

    public void setShapeAssigned(String shapeAssigned) {
        this.shapeAssigned = shapeAssigned;
    }

    public int getOverall_source_rank() {
        return overall_source_rank;
    }

    public int getSourceScore() {
        return sourceScore;
    }

    public void setSourceScore(int sourceScore) {
        this.sourceScore = sourceScore;
    }

    public int getSourceRank() {
        return sourceRank;
    }

    public void setSourceRank(int sourceRank) {
        this.sourceRank = sourceRank;
    }

    @Override
    public int compareTo(AccernData o) {
        if (o == null || o.getHarvested_at() == null || this.getHarvested_at() == null) {
            return 0;
        }
        return this.getHarvested_at().compareTo(o.getHarvested_at());
    }
}
