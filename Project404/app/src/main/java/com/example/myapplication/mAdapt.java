package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class mAdapt extends RecyclerView.Adapter<mAdapt.myViewHolder>
{
    List<Datum> data;
    Context context;


    public mAdapt(List<Datum> data, Context context)
    {
        this.data = data;
        this.context = context;

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.singlerow,parent,false);
        return  new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position)
    {
        Datum datum= data.get(position);
        holder.tv.setText(datum.getName());
        holder.tev.setText(datum.getRole());
        Glide.with((holder.img).getContext()).load(datum.getImage()).placeholder(R.drawable.flclogo).circleCrop().into(holder.img);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView img;
        TextView tv;
        TextView tev;
        CardView cardView;
        public myViewHolder(@NonNull View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            img=itemView.findViewById(R.id.imageholder);
            tv=itemView.findViewById(R.id.theader);
            tev=itemView.findViewById(R.id.colBranch);
            cardView = itemView.findViewById(R.id.cv);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAbsoluteAdapterPosition();
            Datum datum=data.get(position);
            profile p=new profile();
            p.next(datum,context);
        }

    }
}
