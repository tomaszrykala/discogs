package com.tomaszrykala.discogs.adapter.list;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tomaszrykala.discogs.R;
import com.tomaszrykala.discogs.data.ListItem;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private boolean mAnimateItems;
    private int mLastItemAnimated = -1;
    private boolean mAnimationDone = false;

    @NonNull private final List<ListItem> mValues;
    @Nullable private final OnListItemClickListener mListener;

    public interface OnListItemClickListener {
        void onListItemClick(View itemView, ListItem item);
    }

    public ListAdapter(@NonNull List<ListItem> items, @Nullable OnListItemClickListener listener) {
        this(items, listener, true);
    }

    public ListAdapter(@NonNull List<ListItem> items, @Nullable OnListItemClickListener listener,
                       boolean animateItems) {
        mValues = items;
        mListener = listener;
        mAnimateItems = animateItems;
        notifyItemRangeInserted(0, mValues.size());
    }

    public void addItems(@NonNull List<ListItem> items) {
        final int itemCount = getItemCount();
        mValues.addAll(items);
        notifyItemRangeInserted(itemCount, itemCount + items.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list_item, parent, false));
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

        if (mAnimateItems) runEnterAnimation(holder.itemView, position);
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private void runEnterAnimation(@NonNull View view, int position) {
        if (mAnimationDone) return;

        if (position > mLastItemAnimated) {
            mLastItemAnimated = position;
            view.setTranslationY(300);
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(20 * position)
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mAnimationDone = true;
                        }
                    })
                    .start();
        }
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
