package com.hixel.hixel.company;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.View;

import com.hixel.hixel.R;
import com.hixel.hixel.models.Company;

import java.util.ArrayList;

public class CompanyAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private  int resource;

    private static class  viewHolder{
        TextView name;
        TextView value;
    }

    public CompanyAdapter(Context context,int resource,ArrayList<String>objects)
    {
        super(context,resource,objects);
        mContext=context;
        this.resource=resource;

    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String[] ratios={"Current Ratio","Debt-to-Equity","Return-on-Equity","Return-on-Assets","Profit-Margin"};
        String Name=ratios[position];
        String value=getItem(position);
        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView=inflater.inflate(resource,parent,false);
        TextView tvName=(TextView)convertView.findViewById(R.id.textView1);
        TextView tvBirthday=(TextView)convertView.findViewById(R.id.textView2);
        TextView tvSex=(TextView)convertView.findViewById(R.id.textView3);
        tvName.setText(Name);
        tvBirthday.setText("");
        tvSex.setText(value);





        return convertView;

    }



}
