package com.nathb.swiftshop.ui.dialog;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.nathb.swiftshop.R;
import com.nathb.swiftshop.model.Category;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class SelectCategoryDialog extends DialogFragment {

    public interface Callback {
        void onCategorySelected(Category category);
    }

    public static final String TAG = SelectCategoryDialog.class.getSimpleName();

    private Callback callback;
    private String itemName;
    private Realm realm;
    private ArrayAdapter<Category> adapter;
    private RealmResults<Category> categories;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();
        adapter = new ArrayAdapter<>(activity, R.layout.row_all_categories);
        realm = Realm.getDefaultInstance();
        categories = realm.where(Category.class)
                            .findAllAsync()
                            .sort("name");

        categories.addChangeListener(new RealmChangeListener<RealmResults<Category>>() {
            @Override
            public void onChange(RealmResults<Category> element) {
                setCategories(element);
            }
        });
        setCategories(categories);

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onCategorySelected(categories.get(which));
            }
        };

        String title = activity.getResources().getString(R.string.select_category, itemName);

        return new AlertDialog.Builder(activity)
                                .setTitle(title)
                                .setAdapter(adapter, listener)
                                .create();
    }

    private void setCategories(List<Category> categories) {
        adapter.clear();
        adapter.addAll(categories);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        categories.removeChangeListeners();
        realm.close();
    }
}
