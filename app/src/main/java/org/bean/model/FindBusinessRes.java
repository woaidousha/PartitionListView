package org.bean.model;

import java.util.ArrayList;

/**
 * Created by liuyulong on 15-2-9.
 */
public class FindBusinessRes extends Response {

    int total_count;
    ArrayList<Business> businesses;

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public ArrayList<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(ArrayList<Business> businesses) {
        this.businesses = businesses;
    }

    @Override
    public String toString() {
        return "FindBusinessRes{" +
                "total_count=" + total_count +
                ", businesses=" + businesses +
                '}';
    }
}
