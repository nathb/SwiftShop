package com.nathb.swiftshop.data;

import android.content.res.Resources;

import com.nathb.swiftshop.R;
import com.nathb.swiftshop.model.Item;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.realm.Realm;
import timber.log.Timber;

public class PopulateRealm implements Realm.Transaction {

    private Resources resources;

    public PopulateRealm(Resources resources) {
        this.resources = resources;
    }

    @Override
    public void execute(Realm realm) {
        try {
            realm.createAllFromJson(Item.class, readResource(R.raw.items));
        } catch (Exception e) {
            Timber.e("Error creating from json file");
            // Realm handles exception and cancels transaction
            throw new RuntimeException();
        }
    }

    private JSONArray readResource(int resourceId) throws IOException, JSONException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(resources.openRawResource(resourceId)));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return new JSONArray(sb.toString());
    }

}
