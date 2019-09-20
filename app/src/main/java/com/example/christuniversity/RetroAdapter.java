package com.example.christuniversity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RetroAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ModelListView> dataModelArrayList;

    public RetroAdapter(Context context, ArrayList<ModelListView> dataModelArrayList) {

        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return dataModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.retro_lv, null, true);

            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            holder.tvname = (TextView) convertView.findViewById(R.id.name);
            holder.tvtime = (TextView) convertView.findViewById(R.id.time);
            holder.tvv_details = (TextView) convertView.findViewById(R.id.v_details);
            holder.tvrules = (TextView) convertView.findViewById(R.id.rules);
            //holder.tvdistance = (TextView) convertView.findViewById(R.id.distance);
            holder.tvmobile = (TextView) convertView.findViewById(R.id.mobile);
            holder.tvtrip_id = (TextView) convertView.findViewById(R.id.trip_id);
            //holder.tvt_id = (TextView) convertView.findViewById(R.id.t_id);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        //Picasso.get().load(dataModelArrayList.get(position).getImgURL()).into(holder.iv);
        holder.iv.setImageResource(R.drawable.kyc);
        holder.tvname.setText("Name: "+dataModelArrayList.get(position).getName());
        holder.tvtime.setText("Time: "+dataModelArrayList.get(position).gettime());
        holder.tvv_details.setText("Vehicle Details: "+dataModelArrayList.get(position).getv_details());
        holder.tvrules.setText("Rules: "+dataModelArrayList.get(position).getrules());
        //holder.tvdistance.setText("Distance: "+dataModelArrayList.get(position).getdistance());
        holder.tvmobile.setText("Mobile: "+dataModelArrayList.get(position).getmobile());
        holder.tvtrip_id.setText("trip_id: "+dataModelArrayList.get(position).gettrip_id());
        //holder.tvt_id.setText("trip_id: "+dataModelArrayList.get(position).gettrip_id());

        return convertView;
    }

    private class ViewHolder {

        protected TextView tvname, tvtime, tvv_details, tvrules, tvdistance, tvmobile;
        public TextView tvtrip_id, tvt_id;
        protected ImageView iv;
    }

}


