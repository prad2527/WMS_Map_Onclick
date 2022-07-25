package com.stp.wmsmap_onclick.WMS;

import java.util.ArrayList;

/**
 * Created by pradnya.
 */


public class WMSLayerDefination {

    String layer_id;
    String layer_label;
    String style;
    String geoserver_id;
    String table_name;

    Integer layer_index;            // not functional right now
    boolean onload_visible;
    Integer map_group_id;           // set of layers which can be used for different maps
    String map_group_label;
    Double layer_transparency;
    String layer_description;
    ArrayList<String> sld_id_list;
    ArrayList<String> sld_label_list;
    ArrayList<String> attibutes_db_name;
    ArrayList<String> attibutes_display_name;

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getLayer_id() {
        return layer_id;
    }

    public void setLayer_id(String layer_id) {
        this.layer_id = layer_id;
    }

    public String getLayer_label() {
        return layer_label;
    }

    public void setLayer_label(String layer_label) {
        this.layer_label = layer_label;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getGeoserver_id() {
        return geoserver_id;
    }

    public void setGeoserver_id(String geoserver_id) {
        this.geoserver_id = geoserver_id;
    }

    public Integer getLayer_index() {
        return layer_index;
    }

    public void setLayer_index(Integer layer_index) {
        this.layer_index = layer_index;
    }

    public boolean isOnload_visible() {
        return onload_visible;
    }

    public void setOnload_visible(boolean onload_visible) {
        this.onload_visible = onload_visible;
    }

    public Integer getMap_group_id() {
        return map_group_id;
    }

    public void setMap_group_id(Integer map_group_id) {
        this.map_group_id = map_group_id;
    }

    public String getMap_group_label() {
        return map_group_label;
    }

    public void setMap_group_label(String map_group_label) {
        this.map_group_label = map_group_label;
    }

    public Double getLayer_transparency() {
        return layer_transparency;
    }

    public void setLayer_transparency(Double layer_transparency) {
        this.layer_transparency = layer_transparency;
    }

    public String getLayer_description() {
        return layer_description;
    }

    public void setLayer_description(String layer_description) {
        this.layer_description = layer_description;
    }

    public ArrayList<String> getSld_id_list() {
        return sld_id_list;
    }

    public void setSld_id_list(ArrayList<String> sld_id_list) {
        this.sld_id_list = sld_id_list;
    }

    public ArrayList<String> getAttibutes_display_name() {
        return attibutes_display_name;
    }

    public void setAttibutes_display_name(ArrayList<String> attibutes_display_name) {
        this.attibutes_display_name = attibutes_display_name;
    }

    public ArrayList<String> getSld_label_list() {
        return sld_label_list;
    }

    public void setSld_label_list(ArrayList<String> sld_label_list) {
        this.sld_label_list = sld_label_list;
    }

    public ArrayList<String> getAttibutes_db_name() {
        return attibutes_db_name;
    }

    public void setAttibutes_db_name(ArrayList<String> attibutes_db_name) {
        this.attibutes_db_name = attibutes_db_name;
    }
}
