package com.tomaszrykala.discogs.data;

public class ReleaseListItem implements ListItem {
    private final String id;
    private final String title;
    private final String artist;
    private final String artUrl;

    public ReleaseListItem(String id, String title, String artist, String artUrl) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.artUrl = artUrl;
    }

    @Override public String getId() {
        return id;
    }

    @Override public String getTitle() {
        return title;
    }

    @Override public String getSubtitle() {
        return artist;
    }

    @Override public String getThumbUrl() {
        return artUrl;
    }
}
