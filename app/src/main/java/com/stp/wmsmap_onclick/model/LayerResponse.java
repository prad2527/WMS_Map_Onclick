package com.stp.wmsmap_onclick.model;

import java.util.List;

public class LayerResponse {
    String success;
    String message;
    List<LayerResponseData> data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LayerResponseData> getData() {
        return data;
    }

    public void setData(List<LayerResponseData> data) {
        this.data = data;
    }
}
