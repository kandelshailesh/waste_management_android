package com.example.fohor_maila.models.subscriptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchSubscription {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("DATA")
    @Expose
    private List<FetchData> data = null;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FetchData> getData() {
        return data;
    }

    public void setData(List<FetchData> data) {
        this.data = data;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
