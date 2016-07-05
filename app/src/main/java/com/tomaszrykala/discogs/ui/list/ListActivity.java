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

import com.tomaszrykala.discogs.DiscogsApp;
import com.tomaszrykala.discogs.R;
import com.tomaszrykala.discogs.data.model.Release;
import com.tomaszrykala.discogs.mvp.ListMvp;
import com.tomaszrykala.discogs.ui.detail.DetailActivity;
import com.tomaszrykala.discogs.util.DividerItemDecoration;
import com.tomaszrykala.discogs.util.ListItem;

import java.util.List;

import javax.inject.Inject;

public class ListActivity extends AppCompatActivity implements ListAdapter.OnListItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, ListMvp.ListView {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject ListMvp.ListPresenter mPresenter;

    private List<ListItem.ChartListItem> mItems;

    @Override protected void onCreate(Bundle savedInstanceState) {
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

    @Override protected void onDestroy() {
        super.onDestroy();
        mPresenter.releaseView();
    }

    @Override public void showLoading(final boolean isLoading) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override public void run() {
                mSwipeRefreshLayout.setRefreshing(isLoading);
            }
        });
    }

    @Override public void onLoadSuccess(List<Release> list) {
        setupListAdapter(list);
    }

    @Override public void onLoadFail(String error) {
        requestRefresh(error, "Retry");
    }

    @Override public void onListItemClick(View itemView, final ListItem.ChartListItem item) {
        final Pair<View, String> pairToolbar =
                new Pair<>(itemView.findViewById(R.id.title), getString(R.string.transition_toolbar));
        final View view = itemView.findViewById(R.id.art);
        final Pair<View, String> pairArt =
                new Pair<>(view, getString(R.string.transition_art));
        final Pair[] pairs = {pairToolbar, pairArt};

        @SuppressWarnings("unchecked")
        ActivityOptionsCompat transition = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
        final Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.ID, item.id);
        ActivityCompat.startActivity(this, intent, transition.toBundle());
    }

    @Override public void onRefresh() {
        requestRefresh("Refresh Chart?", "Yes");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            onRefresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupListAdapter(List<Release> chart) {
        mItems = ListItem.toChartListItems(chart);
        mRecyclerView.setAdapter(new ListAdapter(mItems, this));
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    private void requestRefresh(String message, String action) {
        Snackbar.make(mRecyclerView, message, Snackbar.LENGTH_LONG)
                .setAction(action, new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        mPresenter.onRefresh();
                    }
                })
                .setCallback(new Snackbar.Callback() {
                    @Override public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                        if (event == DISMISS_EVENT_TIMEOUT) {
                            mPresenter.onRequestCancel();
                        }
                    }
                })
                .show();
    }

    @VisibleForTesting
    public List<ListItem.ChartListItem> getItems() {
        return mItems;
    }
}
