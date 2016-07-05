package com.tomaszrykala.discogs.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tomaszrykala.discogs.R;
import com.tomaszrykala.discogs.util.ListItem;
import com.tomaszrykala.discogs.util.ListItem.ChartListItem;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final List<ChartListItem> mValues;
    private final OnListItemClickListener mListener;

    public interface OnListItemClickListener {
        void onListItemClick(View itemView, ChartListItem item);
    }

    public ListAdapter(List<ListItem.ChartListItem> items, OnListItemClickListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ListItem.ChartListItem item = mValues.get(position);
        holder.mItem = item;
        holder.title.setText(item.title);
        holder.artist.setText(item.artist);
        Glide.with(holder.art.getContext()).load(item.artUrl).centerCrop().crossFade().into(holder.art);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListItemClick(holder.itemView, holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView art;
        public final TextView title;
        public final TextView artist;
        public ChartListItem mItem;

        public ViewHolder(View view) {
            super(view);
            art = (ImageView) view.findViewById(R.id.art);
            title = (TextView) view.findViewById(R.id.title);
            artist = (TextView) view.findViewById(R.id.artist);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + artist.getText() + "'";
        }
    }
}
