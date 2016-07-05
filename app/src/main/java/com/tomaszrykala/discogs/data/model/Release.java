
package com.tomaszrykala.discogs.data.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@Generated("org.jsonschema2pojo")
public class Release extends RealmObject {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("format")
    @Expose
    private String format;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("catno")
    @Expose
    private String catno;
    @SerializedName("year")
    @Expose
    private Integer year;
    @SerializedName("resource_url")
    @Expose
    private String resourceUrl;
    @SerializedName("artist")
    @Expose
    private String artist;
    @SerializedName("id")
    @Expose
    @PrimaryKey
    private Integer id;

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The thumb
     */
    public String getThumb() {
        return thumb;
    }

    /**
     * 
     * @param thumb
     *     The thumb
     */
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    /**
     * 
     * @return
     *     The format
     */
    public String getFormat() {
        return format;
    }

    /**
     * 
     * @param format
     *     The format
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The catno
     */
    public String getCatno() {
        return catno;
    }

    /**
     * 
     * @param catno
     *     The catno
     */
    public void setCatno(String catno) {
        this.catno = catno;
    }

    /**
     * 
     * @return
     *     The year
     */
    public Integer getYear() {
        return year;
    }

    /**
     * 
     * @param year
     *     The year
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * 
     * @return
     *     The resourceUrl
     */
    public String getResourceUrl() {
        return resourceUrl;
    }

    /**
     * 
     * @param resourceUrl
     *     The resource_url
     */
    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    /**
     * 
     * @return
     *     The artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * 
     * @param artist
     *     The artist
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }
}
