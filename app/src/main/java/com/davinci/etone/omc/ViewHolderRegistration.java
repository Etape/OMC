package com.davinci.etone.omc;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewHolderRegistration extends RecyclerView.Adapter<ViewHolderRegistration.viewHolder> {
    Context context;
    ArrayList<Registration_failed> list;
    public ViewHolderRegistration(Context context, ArrayList<Registration_failed> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolderRegistration.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_registration_failed,viewGroup,false);
        return new ViewHolderRegistration.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderRegistration.viewHolder viewHolder, int position) {
        final Registration_failed reg=list.get(position);
        viewHolder.line.setText(reg.getLine());
        viewHolder.error.setText(reg.getError());

    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }
    public static class viewHolder extends RecyclerView.ViewHolder{
        TextView line,error;
        RelativeLayout container;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            line=itemView.findViewById(R.id.line);
            error=itemView.findViewById(R.id.error);
            container=itemView.findViewById(R.id.container);
        }
    }
}
