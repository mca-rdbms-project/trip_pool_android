package com.example.christuniversity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RetroAdapter1 extends BaseAdapter {

    private Context context;
    private ArrayList<RequestListView> dataModelArrayList;

    public RetroAdapter1(Context context, ArrayList<RequestListView> dataModelArrayList) {

        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
    }

    public void remove(int position) {
        dataModelArrayList.remove(position);
        notifyDataSetChanged();
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
            convertView = inflater.inflate(R.layout.retro_lv1, null, true);

            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            holder.tvname = (TextView) convertView.findViewById(R.id.name);
            holder.tvmobile = (TextView) convertView.findViewById(R.id.mobile);
            holder.tvseats = (TextView) convertView.findViewById(R.id.seats);
            holder.tvcollege = (TextView) convertView.findViewById(R.id.college_name);
            holder.tvrequest_id = (TextView) convertView.findViewById(R.id.request_id);
            /*holder.btnaccept = (Button) convertView.findViewById(R.id.accept);
            holder.btndecline = (Button) convertView.findViewById(R.id.decline);
*/
            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        //Picasso.get().load(dataModelArrayList.get(position).getImgURL()).into(holder.iv);
        holder.iv.setImageResource(R.drawable.kyc);
        holder.tvname.setText("Name: "+dataModelArrayList.get(position).getName());
        holder.tvmobile.setText("Mobile: "+dataModelArrayList.get(position).getmobile());
        holder.tvcollege.setText("College: "+dataModelArrayList.get(position).getcollege());
        holder.tvseats.setText("Seats: "+dataModelArrayList.get(position).getseats());
        holder.tvrequest_id.setText("request_id: "+dataModelArrayList.get(position).getrequest_id());

        //holder.btndecline.setOnClickListener(onAcceptListener(position, holder));

        //holder.btndecline.setOnClickListener(onDeclineListener(position, holder));


        return convertView;
    }

    /*private View.OnClickListener onAcceptListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //holder.tvrequest_id.getText().toString();
                Toast.makeText(getContext(),holder.tvrequest_id.getText().toString(),Toast.LENGTH_LONG).show();
            }
        };
    }
*/

    /*private View.OnClickListener onDeclineListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLayoutParent = (LinearLayout) v;

                // Getting the inner Linear Layout
                LinearLayout linearLayoutChild = (LinearLayout ) linearLayoutParent.getChildAt(1);

                // Getting the Country TextView
                TextView tvrequest_id1 = (TextView) linearLayoutChild.getChildAt(4);
                Toast.makeText(getContext(),tvrequest_id1.getText().toString(),Toast.LENGTH_LONG).show();
            }
        };
    }
*/
    private class ViewHolder {

        protected TextView tvname, tvmobile, tvseats, tvcollege, tvrequest_id;
        protected ImageView iv;
        //protected Button btnaccept,btndecline;
    }

}
