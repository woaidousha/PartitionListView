package org.bean.partition;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import com.alibaba.fastjson.JSON;
import com.arasthel.asyncjob.AsyncJob;
import org.bean.job.BaseBackgroundJob;
import org.bean.model.FindBusinessRes;
import org.bean.util.APIConstant;
import org.bean.util.DemoApiTool;
import org.bean.widget.AutoLoadListView;
import partitionlistview.bean.org.partition.R;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


public class MyActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener,
        AutoLoadListView.LoadListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AutoLoadListView mListView;
    private BasePartitionAdapter mAdapter;
    private FoodPartition mFoodPartition;
    private PlayPartition mPlayPartition;

    private GetFoodJob mGetFoodJob;
    private GetPlayJob mGetPlayJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mListView = (AutoLoadListView) findViewById(R.id.listview);
        mListView.setLoadListener(this);
        mAdapter = new BasePartitionAdapter();
        mFoodPartition = new FoodPartition();
        mAdapter.addPartition(mFoodPartition);
        mPlayPartition = new PlayPartition();
        mAdapter.addPartition(mPlayPartition);
        mListView.setAdapter(mAdapter);

        mGetFoodJob = new GetFoodJob(this);
        mGetPlayJob = new GetPlayJob(this);

        getData();
    }

    public void getData() {
        AsyncJob.doInBackground(mGetFoodJob);
    }

    private void getFood() {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("city", "上海");
        paramMap.put("latitude", "31.21524");
        paramMap.put("longitude", "121.420033");
        paramMap.put("category", "美食");
        paramMap.put("region", "长宁区");
        paramMap.put("limit", APIConstant.getPageSize());
        paramMap.put("radius", "2000");
        paramMap.put("format", "json");

        String requestResult = DemoApiTool.requestApi(APIConstant.FIND_BUSINESS, APIConstant.APP_KEY,
                APIConstant.APP_SECRET, paramMap);

        FindBusinessRes res = JSON.parseObject(requestResult, FindBusinessRes.class);
        mFoodPartition.appendData(res.getBusinesses());
    }

    private void getPlay() {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("city", "上海");
        paramMap.put("latitude", "31.21524");
        paramMap.put("longitude", "121.420033");
        paramMap.put("category", "休闲娱乐");
        paramMap.put("region", "长宁区");
        paramMap.put("limit", APIConstant.getPageSize());
        paramMap.put("radius", "2000");
        paramMap.put("format", "json");
        paramMap.put("page", (mPlayPartition.getDataCount() / APIConstant.DEFAULT_PAGE_SIZE + 1) + "");

        String requestResult = DemoApiTool.requestApi(APIConstant.FIND_BUSINESS, APIConstant.APP_KEY,
                APIConstant.APP_SECRET, paramMap);

        FindBusinessRes res = JSON.parseObject(requestResult, FindBusinessRes.class);
        mPlayPartition.appendData(res.getBusinesses());
        mPlayPartition.setHasMore(mPlayPartition.getDataCount() < res.getTotal_count());
    }

    public void updateList() {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.notifyDataChanged();
    }

    @Override
    public void onRefresh() {
        mAdapter.clear();
        getData();
    }

    @Override
    public void load() {
        if (mSwipeRefreshLayout.isRefreshing() || !mPlayPartition.hasMore()) {
            return;
        }
        mSwipeRefreshLayout.setRefreshing(true);
        AsyncJob.doInBackground(mGetPlayJob);
    }

    public static class GetFoodJob extends BaseBackgroundJob<Void> {

        private WeakReference<MyActivity> mMyActivity;

        public GetFoodJob(MyActivity myActivity) {
            this.mMyActivity = new WeakReference<MyActivity>(myActivity);
        }

        @Override
        public Void execute() {
            MyActivity activity = mMyActivity.get();
            if (activity != null) {
                activity.getFood();
            }
            return null;
        }

        @Override
        public void post(Void aVoid) {
            MyActivity activity = mMyActivity.get();
            if (activity != null) {
                activity.updateList();;
            }
        }
    }

    public static class GetPlayJob extends BaseBackgroundJob<Void> {

        private WeakReference<MyActivity> mMyActivity;

        public GetPlayJob(MyActivity myActivity) {
            this.mMyActivity = new WeakReference<MyActivity>(myActivity);
        }

        @Override
        public Void execute() {
            MyActivity activity = mMyActivity.get();
            if (activity != null) {
                activity.getPlay();
            }
            return null;
        }

        @Override
        public void post(Void aVoid) {
            MyActivity activity = mMyActivity.get();
            if (activity != null) {
                activity.updateList();;
            }
        }
    }
}
