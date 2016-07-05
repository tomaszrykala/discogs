
package com.tomaszrykala.discogs.data.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Urls {

    @SerializedName("last")
    @Expose
    private String last;
    @SerializedName("next")
    @Expose
    private String next;

    /**
     * 
     * @return
     *     The last
     */
    public String getLast() {
        return last;
    }

    /**
     * 
     * @param last
     *     The last
     */
    public void setLast(String last) {
        this.last = last;
    }

    /**
     * 
     * @return
     *     The next
     */
    public String getNext() {
        return next;
    }

    /**
     * 
     * @param next
     *     The next
     */
    public void setNext(String next) {
        this.next = next;
    }

}
