package org.bean.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.bean.model.Business;
import partitionlistview.bean.org.partition.R;

public class FoodBusinessView extends LinearLayout {

    private Business mBusiness;

    private ImageView mBusinessPhoto;
    private TextView mName;
    private TextView mAddress;

    public FoodBusinessView(Context context) {
        super(context);
    }

    public FoodBusinessView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FoodBusinessView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mBusinessPhoto = (ImageView) findViewById(R.id.business_photo);
        mName = (TextView) findViewById(R.id.name);
        mAddress = (TextView) findViewById(R.id.address);
    }

    public void bind(Business business) {
        mBusiness = business;

        mName.setText(mBusiness.getName());
        mAddress.setText(mBusiness.getAddress());

        Picasso.with(getContext()).load(mBusiness.getPhoto_url()).into(mBusinessPhoto);
    }
}
