package com.tomaszrykala.discogs.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tomaszrykala.discogs.R;
import com.tomaszrykala.discogs.data.ListItem;

import java.util.List;

class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final List<ListItem> mValues;
    private final OnListItemClickListener mListener;

    interface OnListItemClickListener {
        void onListItemClick(View itemView, ListItem item);
    }

    ListAdapter(List<ListItem> items, OnListItemClickListener listener) {
        mValues = items;
        mListener = listener;
        notifyItemRangeInserted(0, mValues.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ListItem item = mValues.get(position);
        holder.title.setText(item.getTitle());
        holder.artist.setText(item.getSubtitle());
        Glide.with(holder.art.getContext()).load(item.getThumbUrl()).centerCrop().crossFade()
                .error(R.mipmap.ic_launcher).into(holder.art);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListItemClick(holder.itemView, mValues.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView art;
        final TextView artist;
        final TextView title;

        ViewHolder(View view) {
            super(view);
            art = (ImageView) view.findViewById(R.id.art);
            title = (TextView) view.findViewById(R.id.title);
            artist = (TextView) view.findViewById(R.id.artist);
        }
    }
}
