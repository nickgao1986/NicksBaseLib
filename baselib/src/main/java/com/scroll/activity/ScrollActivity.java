package com.scroll.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.base.reddots.R;
import com.scroll.view.PedoScrollableRefreshLayout;

public class ScrollActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoll_fragment);
        PedoScrollableRefreshLayout pedo_scrollable_layout = findViewById(R.id.pedo_scrollable_layout);
        pedo_scrollable_layout.getRefreshableView().init(getSupportFragmentManager());
    }
}
