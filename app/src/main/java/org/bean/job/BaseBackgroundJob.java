package org.bean.job;

import com.arasthel.asyncjob.AsyncJob;

public abstract class BaseBackgroundJob<T> implements AsyncJob.OnBackgroundJob {

    @Override
    public void doOnBackground() {
        T t = execute();
        MainJob mMainJob = new MainJob(t);
        AsyncJob.doOnMainThread(mMainJob);
    }


    public abstract T execute();
    public abstract void post(T t);

    class MainJob implements AsyncJob.OnMainThreadJob {

        private T t;

        public MainJob(T t) {
            this.t = t;
        }

        @Override
        public void doInUIThread() {
            post(t);
        }
    }

}
