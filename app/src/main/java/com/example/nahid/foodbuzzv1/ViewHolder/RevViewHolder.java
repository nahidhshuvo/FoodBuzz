package com.example.nahid.foodbuzzv1.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nahid.foodbuzzv1.Interface.RevClickListener;
import com.example.nahid.foodbuzzv1.R;

import org.w3c.dom.Text;

public class RevViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView tvcaption, tvDescription, tvPrice,tvuname;
    public ImageView ivrev;
    public RevClickListener listnerrev;


    public RevViewHolder(View itemView)
    {
        super(itemView);


        ivrev = (ImageView) itemView.findViewById(R.id.rest_image);
         tvcaption = (TextView) itemView.findViewById(R.id.rev_rest_name);
        tvDescription = (TextView) itemView.findViewById(R.id.rest_description);
        tvPrice= (TextView) itemView.findViewById(R.id.rest_price);
        tvuname= (TextView) itemView.findViewById(R.id.rev_user);

    }

    public void setItemClickListner(RevClickListener listner)
    {
        this.listnerrev = listner;
    }

    @Override
    public void onClick(View view)
    {
        listnerrev.onClick(view, getAdapterPosition(), false);
    }
}
