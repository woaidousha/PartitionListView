package org.bean.partition;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.bean.MainApp;
import org.bean.model.Business;
import partitionlistview.bean.org.partition.R;

/**
* Created by liuyulong on 15-2-9.
*/
class PlayPartition extends GridPartition<Business> {

    PlayPartition() {
        super(1);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(MainApp.getApp()).inflate(R.layout.play_business_view, null);
            holder = new ViewHolder();
            holder.mBusinessPhoto = (ImageView) view.findViewById(R.id.business_photo);
            holder.mName = (TextView) view.findViewById(R.id.name);
            holder.mAddress = (TextView) view.findViewById(R.id.address);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.bind(getItem(i).get(0));
        return view;
    }

    class ViewHolder {
        ImageView mBusinessPhoto;
        TextView mName;
        TextView mAddress;

        public void bind(Business business) {
            mName.setText(business.getName());
            mAddress.setText(business.getAddress());

            Picasso.with(MainApp.getApp()).load(business.getPhoto_url()).into(mBusinessPhoto);
        }
    }
}
