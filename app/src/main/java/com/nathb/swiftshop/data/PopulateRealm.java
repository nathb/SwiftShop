package com.nathb.swiftshop.data;

import android.content.res.Resources;

import com.nathb.swiftshop.R;
import com.nathb.swiftshop.model.Category;
import com.nathb.swiftshop.model.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

            JSONArray categories = readResource(R.raw.data);
            for (int i = 0; i < categories.length(); i++) {

                // Create category
                JSONObject jsonCategory = categories.getJSONObject(i);
                Category category = new Category(jsonCategory.getString("name"), i+1);
                category = realm.copyToRealm(category);

                // Add items to category
                JSONArray jsonItemNames = jsonCategory.getJSONArray("items");
                for (int j = 0; j < jsonItemNames.length(); j++) {
                    Item item = new Item(jsonItemNames.getString(j), category);
                    realm.copyToRealm(item);
                }

            }

        } catch (Exception e) {
            Timber.e(e, "Error creating from json file");
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
