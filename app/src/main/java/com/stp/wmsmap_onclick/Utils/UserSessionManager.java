package com.stp.wmsmap_onclick.Utils;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;


/**
 * Created by pradnya.
 */

public class UserSessionManager {
    private static final String TAG = UserSessionManager.class.getSimpleName();
    private static final String PREFER_NAME = ApplicationConstant.APPLICATION_PACKAGE_NAME;
    private static final String LATITUDE_VAL ="latitude" ;
    private static final String LONGITUDE_VAL ="longitude" ;


    private static final String SELECTED_COLUMN_NAME ="column_name" ;
    private static final String SELECTED_TABLE_NAME ="table_name" ;
    private static final String SELECTED_DISPLAY_LAYER_NAME = "display_layer_name";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    public UserSessionManager(Context context){
            Context _context = context;
            pref = _context.getSharedPreferences(PREFER_NAME, 0);
            editor = pref.edit();
        }




    public HashMap<String, String> getLastUpdatedSession(){
        HashMap<String, String> addressPicker = new HashMap<>();
        addressPicker.put(LATITUDE_VAL, pref.getString(LATITUDE_VAL, null));
        addressPicker.put(LONGITUDE_VAL, pref.getString(LONGITUDE_VAL, null));
        return addressPicker;
    }

    public void createSelectedLayerClick(String column_name, String table_name ,String display_layer_name ){
        // Storing login value as TRUE
        // Storing name in pref
        editor.putString(SELECTED_COLUMN_NAME,column_name );
        editor.putString(SELECTED_TABLE_NAME, table_name);
        editor.putString(SELECTED_DISPLAY_LAYER_NAME,display_layer_name );
        // commit changes
        editor.commit();
    }
    public HashMap<String, String> getSelectedLayerClick(){

        HashMap<String, String> selectedList = new HashMap<String, String>();
        selectedList.put(SELECTED_COLUMN_NAME, pref.getString(SELECTED_COLUMN_NAME, null));
        selectedList.put(SELECTED_TABLE_NAME, pref.getString(SELECTED_TABLE_NAME, null));
        selectedList.put(SELECTED_DISPLAY_LAYER_NAME, pref.getString(SELECTED_DISPLAY_LAYER_NAME, null));


        // return user
        return selectedList;
    }

}
