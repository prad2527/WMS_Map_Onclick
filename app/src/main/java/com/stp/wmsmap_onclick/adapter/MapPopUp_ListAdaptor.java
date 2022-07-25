package com.stp.wmsmap_onclick.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.stp.wmsmap_onclick.R;
import com.stp.wmsmap_onclick.Utils.ApplicationConstant;

import java.util.ArrayList;

/**
 * Created by ShettyDev.
 */


public class MapPopUp_ListAdaptor extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> str_key;
    private final ArrayList<String> str_value;

    public MapPopUp_ListAdaptor(Activity context, ArrayList<String> title, ArrayList<String> value) {
        super(context, R.layout.support_simple_spinner_dropdown_item, title);
        this.context = context;
        this.str_key = title;
        this.str_value = value;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.map_pop_up_row, null, true);

        TextView txtTitle = rowView.findViewById(R.id.txt);
        txtTitle.setText(str_key.get(position));
        TextView desc = rowView.findViewById(R.id.txt2);
        if (str_key.get(position).equals("Flag")) {
            txtTitle.setVisibility(View.GONE);
            desc.setVisibility(View.GONE);
        }else{
            txtTitle.setVisibility(View.VISIBLE);
            desc.setVisibility(View.VISIBLE);
        }
            if (str_key.get(position).equals("Commissioning Year")) {
                if (str_value.get(position)==""||str_value.get(position)== null||str_value.get(position).equals("null")) {
                    desc.setText("-");
                    desc.setTextColor(getContext().getResources().getColor(R.color.black));
                }else{
                    try {

                        String val = String.format("%.0f",Double.valueOf(str_value.get(position)));
                        Log.d("@@AK comm",val);
                        desc.setText(val);
                        desc.setTextColor(getContext().getResources().getColor(R.color.black));
                    }catch (Exception e){e.printStackTrace();}
                }


            }else if (str_key.get(position).equals("Diameter")) {
                if (str_value.get(position)==""||str_value.get(position)== null||str_value.get(position).equals("null")) {
                    desc.setText("-");
                    desc.setTextColor(getContext().getResources().getColor(R.color.black));
                }else {
                    try {

                        String val = String.format("%.0f", Double.valueOf(str_value.get(position)));
                        Log.d("@@AK comm", val);
                        desc.setText(val);
                        desc.setTextColor(getContext().getResources().getColor(R.color.black));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else{
                desc.setText(str_value.get(position));
                desc.setTextColor(getContext().getResources().getColor(R.color.black));
            }

            if (str_key.get(position).equals("Photo Link")){
                if (str_value.get(position)==""||str_value.get(position)== null||str_value.get(position).equals("null")){
                    desc.setText("No Image");
                    desc.setTextColor(getContext().getResources().getColor(R.color.black));

                }else {
                    desc.setText("Show Image");
                    desc.setTextColor(getContext().getResources().getColor(R.color.purple_500));


                    desc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialog = new Dialog(getContext());
                            dialog.setContentView(R.layout.dialog_asset_layer_img);
                            ImageView image_data = dialog.findViewById(R.id.image_data);
                            ImageView close = dialog.findViewById(R.id.close);
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            Log.d("@@AK photo", ApplicationConstant.PHOTO +str_value.get(position));
                            Picasso.with(getContext())
                                    .load(ApplicationConstant.PHOTO +str_value.get(position))
                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .networkPolicy(NetworkPolicy.NO_CACHE)
                                    .into(image_data);
                            dialog.show();
                        }
                    });
                }

        }else{



        }
        Log.d("@@AK ", str_key.get(position));
        return rowView;
    }

}