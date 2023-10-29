package com.example.nahid.foodbuzzv1.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nahid.foodbuzzv1.R;
import com.example.nahid.foodbuzzv1.Interface.RestClickListener;

/**
 * Created by Nahid on 4/23/2019.
 */
public class RestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtrestName, txtrestDescription, txtrestPrice;
    public ImageView imageView;
    public RestClickListener listner;


    public RestViewHolder(View itemView)
    {
        super(itemView);


        imageView = (ImageView) itemView.findViewById(R.id.rest_image);
        txtrestName = (TextView) itemView.findViewById(R.id.rev_rest_name);
        txtrestDescription = (TextView) itemView.findViewById(R.id.rest_description);
        txtrestPrice = (TextView) itemView.findViewById(R.id.rest_price);
    }

    public void setRestClickListner(RestClickListener listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}
