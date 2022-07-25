package com.stp.wmsmap_onclick.WMS;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by pradnya.
 */

public class WMSLayerManager extends WMSLayerDefination
{
    public JSONObject map_def_jobj;
    public String selected_display_label;
    public String selected_layer_id;
    public String selected_geoserver_id;
    public Integer selected_map_group_id;
    public ArrayList<String> layer_style;
    public ArrayList<String> layer_table_name;

    public ArrayList<String> layer_id_arr;
    public ArrayList<String> layer_geoserver_id_arr;
    public ArrayList<String> layer_label_list_arr;
    public ArrayList<Boolean> layer_onload_visible_arr;
    public ArrayList<WMSLayerDefination> WMSLayers = new ArrayList<WMSLayerDefination>() ;

    public ArrayList<String> getLayer_table_name() {
        return layer_table_name;
    }

    public void setLayer_table_name(ArrayList<String> layer_table_name) {
        this.layer_table_name = layer_table_name;
    }

    public ArrayList<String> getLayer_style() {
        return layer_style;
    }

    public void setLayer_style(ArrayList<String> layer_style) {
        this.layer_style = layer_style;
    }

    public WMSLayerManager(JSONObject map_def_jobj) {
        this.map_def_jobj=map_def_jobj;
    }

    public ArrayList<String> getLayer_id_arr() {
        return layer_id_arr;
    }

    public void setLayer_id_arr(ArrayList<String> layer_id_arr) {
        this.layer_id_arr = layer_id_arr;
    }

    public ArrayList<String> getLayer_geoserver_id_arr() {
        return layer_geoserver_id_arr;
    }

    public void setLayer_geoserver_id_arr(ArrayList<String> layer_geoserver_id_arr) {
        this.layer_geoserver_id_arr = layer_geoserver_id_arr;
    }

    public ArrayList<String> getLayer_label_list_arr() {
        return layer_label_list_arr;
    }

    public void setLayer_label_list_arr(ArrayList<String> layer_label_list_arr) {
        this.layer_label_list_arr = layer_label_list_arr;
    }

    public ArrayList<Boolean> getLayer_onload_visible_arr() {
        return layer_onload_visible_arr;
    }

    public void setLayer_onload_visible_arr(ArrayList<Boolean> layer_onload_visible_arr) {
        this.layer_onload_visible_arr = layer_onload_visible_arr;
    }

    public void AddWMSLayer(String map_group_id_column, String group_name)
    {
        try {
            JSONArray jsonArrLayers= null;
            jsonArrLayers=map_def_jobj.getJSONArray("layers");
            for(int n = 0; n < jsonArrLayers.length(); n++)
            {
                WMSLayerDefination LDef = new WMSLayerDefination();
                JSONObject EachLayer = jsonArrLayers.getJSONObject(n);
                if (group_name.equalsIgnoreCase(EachLayer.getString(map_group_id_column))) // groups filter
                {
                    LDef.setLayer_id(EachLayer.getString("layer_id"));
                    LDef.setLayer_label(EachLayer.getString("layer_label"));
                    LDef.setGeoserver_id(EachLayer.getString("geoserver_id"));
                    LDef.setTable_name(EachLayer.getString("table_name"));

                    LDef.setStyle(EachLayer.getString("style"));
                    LDef.setLayer_index(EachLayer.getInt("layer_index"));
                    LDef.setOnload_visible(EachLayer.getBoolean("onload_visible"));
                    LDef.setMap_group_id(EachLayer.getInt("map_group_id"));
                    LDef.setMap_group_label(EachLayer.getString("map_group_label"));
                    LDef.setLayer_transparency(EachLayer.getDouble("layer_transparency"));
                    LDef.setLayer_description(EachLayer.getString("layer_description"));

                    JSONObject jsonArr_att=EachLayer.getJSONObject("attribute_list");
                    Log.e("", "JSON Att key ------>>>>>>"+jsonArr_att.toString());

                    Log.e("JSON obj length ", "  "+jsonArr_att.length());
                    LDef.attibutes_db_name = new ArrayList<String>();
                    LDef.attibutes_display_name = new ArrayList<String>();

                    for(int i = 0; i<jsonArr_att.length(); i++)
                    {
                        LDef.attibutes_display_name.add(jsonArr_att.get(jsonArr_att.names().getString(i)).toString());
                        LDef.attibutes_db_name.add(jsonArr_att.names().getString(i));
                    }
                    Log.e("db name "," #####################"+LDef.attibutes_db_name.toString());
                    Log.e("label "," #####################"+LDef.attibutes_display_name.toString());

                    for(Iterator<String> iter = jsonArr_att.keys(); iter.hasNext();)
                    {
                        String key = iter.next();
                        Object value = jsonArr_att.get(key);
                    }
                    this.WMSLayers.add(LDef);
                }
            }
        } catch (JSONException e) {
            Log.e("Json Exception:"," "+e);
        }
    }
    public void SetLayerArrays()
    {
        this.layer_id_arr = new ArrayList<String>();
        this.layer_geoserver_id_arr = new ArrayList<String>();
        this.layer_label_list_arr = new ArrayList<String>();
        this.layer_onload_visible_arr = new ArrayList<Boolean>();
        this.layer_style=new ArrayList<>();
        this.layer_table_name=new ArrayList<>();


        for (int i=0;i<this.WMSLayers.size();i++)
        {
            Log.e("Layer Manager","==========---------------->>>>>>>>>>>>>>>>>>"+WMSLayers.get(i).getGeoserver_id());
            this.layer_id_arr.add(WMSLayers.get(i).getLayer_id());
            this.layer_style.add(WMSLayers.get(i).getStyle());
            this.layer_table_name.add(WMSLayers.get(i).getTable_name());
            this.layer_geoserver_id_arr.add(WMSLayers.get(i).getGeoserver_id());
            this.layer_label_list_arr.add(WMSLayers.get(i).getLayer_label());
            this.layer_onload_visible_arr.add(WMSLayers.get(i).isOnload_visible());
        }
    }
    public ArrayList<String> getAttributeDbName(String geoserver_id)
    {
        for (WMSLayerDefination g: this.WMSLayers)
        {
            if(g.getTable_name().equals(geoserver_id))
            {
                return g.getAttibutes_db_name();
            }
        }
        return null;
    }
    public ArrayList<String> getAttributeLabel(String geoserver_id)
    {
        for (WMSLayerDefination g: this.WMSLayers)
        {
            if(g.getTable_name().equals(geoserver_id))
            {
                return g.getAttibutes_display_name();
            }
        }
        return null;
    }
    public Boolean CrossCheckAttributes()
    {
        String msg="";
        Boolean result=true;
        for (WMSLayerDefination g: this.WMSLayers)
        {
            if(g.getAttibutes_db_name().size()== g.getAttibutes_display_name().size())
            {
            }
            else
            {
                msg="Error: "+g.getGeoserver_id()+" Number of label and column name does not match !!";
                Log.e("CrossCheckAttributes"," "+msg);
                result=false;
            }
        }
        return result;
    }
    public void  SetSelection(String Layer_Id)
    {
        for (WMSLayerDefination g: this.WMSLayers)
        {
            if (Layer_Id.equals(g.getLayer_id()))
            {
                Log.e("Layer Manager ", "matched  id ----------------> " + g.getLayer_id());
                this.setSelected_geoserver_id(g.getGeoserver_id());
                this.setSelected_layer_id(g.getLayer_id());
            }
        }
    }

    public String getSelected_display_label() {
        return selected_display_label;
    }

    public void setSelected_display_label(String selected_display_label) {
        this.selected_display_label = selected_display_label;
    }

    public String getSelected_geoserver_id() {
        return selected_geoserver_id;
    }

    public void setSelected_geoserver_id(String selected_geoserver_id) {
        this.selected_geoserver_id = selected_geoserver_id;
    }

    public Integer getSelected_map_group_id() {
        return selected_map_group_id;
    }

    public void setSelected_map_group_id(Integer selected_map_group_id) {
        this.selected_map_group_id = selected_map_group_id;
    }

    public String getSelected_layer_id() {
        return selected_layer_id;
    }

    public void setSelected_layer_id(String selected_layer_id) {
        this.selected_layer_id = selected_layer_id;
    }
}
