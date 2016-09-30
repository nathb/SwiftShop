package com.nathb.swiftshop.ui.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.nathb.swiftshop.R;

import io.realm.Realm;

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getContentViewId();

    protected Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        setContentView(getContentViewId());
        getSupportActionBar().setTitle(getTitleId());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (showBackButton()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setIcon(R.drawable.logo);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    protected int getTitleId() {
        return R.string.app_name;
    }

    protected boolean showBackButton() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
