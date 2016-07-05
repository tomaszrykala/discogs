package com.tomaszrykala.discogs.util;

import com.tomaszrykala.discogs.data.model.Release;

import java.util.ArrayList;
import java.util.List;

public class ListItem {

    public static List<ChartListItem> toChartListItems(List<Release> charts) {
        List<ChartListItem> items = new ArrayList<>();
        for (int i = 0, size = charts.size(); i < size; i++) {
            final Release release = charts.get(i);
            final String key = String.valueOf(release.getId());
            final String title = release.getTitle();
            final String artist = release.getArtist();
            // final String aDefault = images.get_default();
            final ChartListItem item = new ChartListItem(key, title, artist, null);
            items.add(item);
        }
        return items;
    }

    public static class ChartListItem {
        public final String id;
        public final String title;
        public final String artist;
        public final String artUrl;

        public ChartListItem(String id, String title, String artist, String artUrl) {
            this.id = id;
            this.title = title;
            this.artist = artist;
            this.artUrl = artUrl;
        }

        @Override
        public String toString() {
            return "ChartListItem{" +
                    "artist='" + artist + '\'' +
                    ", id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", artUrl='" + artUrl + '\'' +
                    '}';
        }
    }
}
