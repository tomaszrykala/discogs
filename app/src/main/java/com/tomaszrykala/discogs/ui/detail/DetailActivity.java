package com.tomaszrykala.discogs.ui.detail;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;
import com.tomaszrykala.discogs.DiscogsApp;
import com.tomaszrykala.discogs.R;
import com.tomaszrykala.discogs.data.model.Release;
import com.tomaszrykala.discogs.mvp.DetailMvp;

import java.util.List;

import javax.inject.Inject;

public class DetailActivity extends AppCompatActivity implements DetailMvp.DetailView {

    public static final String ID = "ID";

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private NestedScrollView mBackground;
    private FloatingActionButton mFab;
    private TextView mBody;
    private ImageView mArt;
    private String mId;

    @Inject DetailMvp.DetailPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mArt = (ImageView) findViewById(R.id.toolbar_art);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mBody = (TextView) findViewById(R.id.detail_body);
        mBackground = (NestedScrollView) findViewById(R.id.detail_body_background);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mPresenter.onFabClick();
            }
        });

        setupAppBarHeight();
        mId = getIntent().getExtras().getString(ID, null);

        ActivityCompat.setEnterSharedElementCallback(this,
                new SharedElementCallback() {
                    @Override
                    public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                        super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                        ViewCompat.animate(mFab).setStartDelay(300).scaleX(1f).scaleY(1f);
                    }

                    @Override
                    public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                        super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
                        mFab.setScaleX(0f);
                        mFab.setScaleY(0f);
                    }
                });

        DiscogsApp.getInstance().getComponent().inject(this);
        mPresenter.setView(this);
        mPresenter.load(mId);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mPresenter.releaseView();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void setupAppBarHeight() {
        View appBar = findViewById(R.id.app_bar);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final ViewGroup.LayoutParams params = appBar.getLayoutParams();
        final int widthPixels = displayMetrics.widthPixels;
        final int heightPixels = displayMetrics.heightPixels;
        final int orientation = getResources().getConfiguration().orientation;
        final boolean isPortrait = orientation == Configuration.ORIENTATION_PORTRAIT;
        params.height = isPortrait ? widthPixels : heightPixels / 2;
        appBar.setLayoutParams(params);
    }

    @Override public void showLoading(boolean isLoading) {
        // no-op
    }

    @Override public void onLoadFail(String error) {
        Snackbar.make(mCollapsingToolbarLayout, error, Snackbar.LENGTH_LONG).setAction("retry?", new View.OnClickListener() {
            @Override public void onClick(View v) {
                mPresenter.load(mId);
            }
        }).show();
    }

    @Override public void onLoadSuccess(Release release) {
        // art
        final String url = release.getThumb();
        final GlidePalette requestListener = getRequestListener(url);

        //noinspection unchecked
        Glide.with(this).load(url).centerCrop().listener(requestListener).crossFade().error(R.drawable.discogs_logo)
                .into(mArt);

        // title
        final String artist = release.getArtist();
        getSupportActionBar().setTitle(artist);

        // body
        mBody.setText(release.toString());
    }

    private GlidePalette getRequestListener(String url) {
        return GlidePalette.with(url)
                .intoCallBack(new BitmapPalette.CallBack() {
                    @Override public void onPaletteLoaded(@Nullable Palette palette) {
                        if (palette != null) {
                            final Palette.Swatch darkVibrantSwatch = palette.getDarkVibrantSwatch();
                            final Palette.Swatch lightVibrantSwatch = palette.getLightVibrantSwatch();
                            if (darkVibrantSwatch != null && lightVibrantSwatch != null) {

                                // primary color
                                mBackground.setBackgroundColor(darkVibrantSwatch.getRgb());

                                // accent color
                                mFab.setBackgroundTintList(ColorStateList.valueOf(lightVibrantSwatch.getRgb()));
                            }
                        }
                    }
                });
    }

    @Override public void share(String message) {
        Toast.makeText(DetailActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
