package org.bean.partition;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import org.bean.MainApp;
import org.bean.model.Business;
import org.bean.widget.FoodBusinessView;
import partitionlistview.bean.org.partition.R;

import java.util.ArrayList;

/**
* Created by liuyulong on 15-2-9.
*/
class FoodPartition extends GridPartition<Business> {

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = createView(view);
        ArrayList<Business> foodBusinesses = getItem(i);
        for (int j = 0; j < ((ViewGroup) view).getChildCount() && j < foodBusinesses.size(); j++) {
            ((FoodBusinessView) ((ViewGroup) view).getChildAt(j)).bind(foodBusinesses.get(j));
        }
        return view;
    }

    public View createView(View view) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(MainApp.getApp());
            view = inflater.inflate(R.layout.food_business_item, null);
            for (int i = 0; i < getColumn(); i++) {
                FoodBusinessView businessView = (FoodBusinessView) inflater.inflate(R.layout.food_buesniess_view,
                        null);
                ((LinearLayout) view).addView(businessView,
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            }
        }
        return view;
    }
}
