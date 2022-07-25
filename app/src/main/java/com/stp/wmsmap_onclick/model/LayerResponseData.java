package com.stp.wmsmap_onclick.model;

import java.util.List;

public class LayerResponseData {
    String column_name;
    String table_name;
    String display_layer_name;
    List<Object> data;

    public LayerResponseData(String column_name, String display_layer_name, String table_name, List<Object> data) {
        this.column_name=column_name;
        this.table_name=table_name;
        this.display_layer_name=display_layer_name;
        this.data=data;

    }

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getDisplay_layer_name() {
        return display_layer_name;
    }

    public void setDisplay_layer_name(String display_layer_name) {
        this.display_layer_name = display_layer_name;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}
