package com.example.huma.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/*{
        id: 281957,
        results: [
        {
        id: "5693d51bc3a3687b6b000145",
        iso_639_1: "en",
        key: "EIELwayIIT4",
        name: "The Revenant Official Trailer 1 2015 HD",
        site: "YouTube",
        size: 1080,
        type: "Trailer"
        }
        ]
        }*/
public class Trailers {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("results")
    @Expose
    private List<Trailer> results = new ArrayList<>();

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The results
     */
    public List<Trailer> getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<Trailer> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "Trailers{" +
                "id=" + id +
                ", results=" + results +
                '}';
    }
}
