package com.nathb.swiftshop.ui.adapter;


import android.content.Context;
import android.view.ViewGroup;
import android.widget.*;

import com.nathb.swiftshop.model.Category;

import java.util.List;

public class AllCategoriesAdapter extends android.widget.ArrayAdapter<Category> {

    public AllCategoriesAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

}
