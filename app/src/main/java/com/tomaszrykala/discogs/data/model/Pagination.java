
package com.tomaszrykala.discogs.data.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Pagination {

    @SerializedName("per_page")
    @Expose
    private Integer perPage;
    @SerializedName("items")
    @Expose
    private Integer items;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("urls")
    @Expose
    private Urls urls;
    @SerializedName("pages")
    @Expose
    private Integer pages;

    /**
     * 
     * @return
     *     The perPage
     */
    public Integer getPerPage() {
        return perPage;
    }

    /**
     * 
     * @param perPage
     *     The per_page
     */
    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    /**
     * 
     * @return
     *     The items
     */
    public Integer getItems() {
        return items;
    }

    /**
     * 
     * @param items
     *     The items
     */
    public void setItems(Integer items) {
        this.items = items;
    }

    /**
     * 
     * @return
     *     The page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * 
     * @param page
     *     The page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * 
     * @return
     *     The urls
     */
    public Urls getUrls() {
        return urls;
    }

    /**
     * 
     * @param urls
     *     The urls
     */
    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    /**
     * 
     * @return
     *     The pages
     */
    public Integer getPages() {
        return pages;
    }

    /**
     * 
     * @param pages
     *     The pages
     */
    public void setPages(Integer pages) {
        this.pages = pages;
    }
}
