package com.example.huma.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/*{
        id: 281957,
        page: 1,
        results: [
        {
        id: "568bbeaec3a3680e01007bb7",
        author: "rahuliam",
        content: "The Revenant, a ravishingly violent Western survival yarn from Alejandro González Iñárritu, has a healthy few, scattered like acorns across its two-and-a-half-hour canvas..... no matter how extended, the film’s tense story is under the director’s complete control...DiCaprio’s performance is an astonishing testament to his commitment to a role. cinematographer Emmanuel Lubezki done a great job..as a supporting actor tom hardy is brilliant..must watch...",
        url: "http://j.mp/1O8khtT"
        }
        ],
        total_pages: 1,
        total_results: 1
 }*/
public class Reviews {


    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("results")
    @Expose
    private List<Review> results = new ArrayList<>();
    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    @SerializedName("total_results")
    @Expose
    private int totalResults;

    /**
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The page
     */
    public int getPage() {
        return page;
    }

    /**
     * @param page The page
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * @return The results
     */
    public List<Review> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setResults(List<Review> results) {
        this.results = results;
    }

    /**
     * @return The totalPages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * @param totalPages The total_pages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * @return The totalResults
     */
    public int getTotalResults() {
        return totalResults;
    }

    /**
     * @param totalResults The total_results
     */
    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
