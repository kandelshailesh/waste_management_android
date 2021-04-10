package com.example.fohor_maila.models.schedules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Schedules {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("DATA")
    @Expose
    private Data data;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
