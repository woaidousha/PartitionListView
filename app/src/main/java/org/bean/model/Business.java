package org.bean.model;

/**
 * Created by liuyulong on 15-2-9.
 */
public class Business {

    long business_id;
    String name;
    String address;
    String photo_url;

    public long getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(long business_id) {
        this.business_id = business_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    @Override
    public String toString() {
        return "Business{" +
                "business_id=" + business_id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", photo_url='" + photo_url + '\'' +
                '}';
    }
}
