
package com.tomaszrykala.discogs.data.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Label {

    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    @SerializedName("releases")
    @Expose
    private List<Release> releases = new ArrayList<Release>();

    /**
     * 
     * @return
     *     The pagination
     */
    public Pagination getPagination() {
        return pagination;
    }

    /**
     * 
     * @param pagination
     *     The pagination
     */
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    /**
     * 
     * @return
     *     The releases
     */
    public List<Release> getReleases() {
        return releases;
    }

    /**
     * 
     * @param releases
     *     The releases
     */
    public void setReleases(List<Release> releases) {
        this.releases = releases;
    }

}
