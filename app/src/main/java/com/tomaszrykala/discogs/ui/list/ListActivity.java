package com.tomaszrykala.discogs.ui.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.tomaszrykala.discogs.DiscogsApp;
import com.tomaszrykala.discogs.R;
import com.tomaszrykala.discogs.adapter.list.DividerItemDecoration;
import com.tomaszrykala.discogs.adapter.list.ListAdapter;
import com.tomaszrykala.discogs.data.ListItem;
import com.tomaszrykala.discogs.mvp.ListMvp;
import com.tomaszrykala.discogs.ui.detail.DetailActivity;

import java.util.List;

import javax.inject.Inject;

public class ListActivity extends AppCompatActivity implements ListAdapter.OnListItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, ListMvp.ListView {

    private List<ListItem> mItems;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject ListMvp.ListPresenter mPresenter;
    private ListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }

        DiscogsApp.getInstance().getComponent().inject(this);
        mPresenter.setView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.releaseView();
    }

    @Override
    public void showLoading(final boolean isLoading) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(isLoading);
            }
        });
    }

    @Override
    public void onLoadFail(String error) {
        showSnackBar(error, "Retry");
    }

    @Override
    public void onListItemClick(View itemView, final ListItem item) {
        final Pair<View, String> pairToolbar =
                new Pair<>(itemView.findViewById(R.id.title), getString(R.string.transition_toolbar));
        final ImageView pairThumbnail = (ImageView) itemView.findViewById(R.id.art);
        final Pair<ImageView, String> pairArt = new Pair<>(pairThumbnail, getString(R.string.transition_art));
        final Pair[] pairs = {pairToolbar, pairArt};

        @SuppressWarnings("unchecked")
        ActivityOptionsCompat transition = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
        final Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.ID, item.getId());
        ActivityCompat.startActivity(this, intent, transition.toBundle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            mPresenter.onRefreshRequested();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadSuccess(List<ListItem> items) {
        if (mListAdapter == null) {
            mItems = items;
            mListAdapter = new ListAdapter(mItems, this);
            mRecyclerView.setAdapter(mListAdapter);
        } else {
            mItems.addAll(items);
            mListAdapter.addItems(items);
        }
    }

    @Override
    public void showSnackBar(String message, String action) {
        Snackbar.make(mRecyclerView, message, Snackbar.LENGTH_LONG)
                .setAction(action, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.onRefresh();
                    }
                })
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                        if (event == DISMISS_EVENT_TIMEOUT) {
                            mPresenter.onRequestCancel();
                        }
                    }
                })
                .show();
    }

    @VisibleForTesting
    public List<ListItem> getItems() {
        return mItems;
    }

    @Override public void onRefresh() {
        mPresenter.onRefreshRequested();
    }
}
