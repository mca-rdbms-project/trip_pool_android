package com.example.christuniversity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RetroAdapter2 extends BaseAdapter {

    private Context context;
    private ArrayList<ModelListView1> dataModelArrayList;

    public RetroAdapter2(Context context, ArrayList<ModelListView1> dataModelArrayList) {

        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
    }

    public void remove(int position) {
        dataModelArrayList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 1;
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
        RetroAdapter2.ViewHolder holder;

        if (convertView == null) {
            holder = new RetroAdapter2.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.retro_lv2, null, true);

            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            holder.tvrequest_id = (TextView) convertView.findViewById(R.id.request_id);
            holder.tvorigin = (TextView) convertView.findViewById(R.id.origin);
            holder.tvdestination = (TextView) convertView.findViewById(R.id.destination);
            holder.tvtime = (TextView) convertView.findViewById(R.id.time);
            holder.tvtrip_id = (TextView) convertView.findViewById(R.id.trip_id);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (RetroAdapter2.ViewHolder)convertView.getTag();
        }

        //Picasso.get().load(dataModelArrayList.get(position).getImgURL()).into(holder.iv);
        holder.iv.setImageResource(R.drawable.kyc);
        holder.tvrequest_id.setText("Request Id: "+dataModelArrayList.get(position).getrequest_id());
        holder.tvtime.setText("Time: "+dataModelArrayList.get(position).gettime());
        holder.tvorigin.setText("Origin: "+dataModelArrayList.get(position).getorigin());
        holder.tvdestination.setText("Destination: "+dataModelArrayList.get(position).getdestination());
        holder.tvtrip_id.setText("Destination: "+dataModelArrayList.get(position).gettrip_id());


        return convertView;
    }

    private class ViewHolder {

        protected TextView tvrequest_id, tvtime, tvorigin, tvdestination, tvtrip_id;
        protected ImageView iv;
    }

}

