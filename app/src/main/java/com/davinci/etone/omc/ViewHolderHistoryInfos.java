package com.davinci.etone.omc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class ViewHolderHistoryInfos extends RecyclerView.Adapter<ViewHolderHistoryInfos.viewHolder> {
    Context context;
    ArrayList<Info> list;
    private FirebaseDatabase Db=FirebaseDatabase.getInstance();

    public ViewHolderHistoryInfos(Context context, ArrayList<Info> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolderHistoryInfos.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View v= LayoutInflater.from(context).inflate(R.layout.item_info_history,viewGroup,false);
        return new ViewHolderHistoryInfos.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderHistoryInfos.viewHolder viewHolder, int position) {
        final Info info=list.get(position);
        Log.i("path","info path :"+info.getImage());
        viewHolder.date.setText(getDate(info.getTime()));
        viewHolder.deletebox.setVisibility(View.GONE);
        Picasso.get().load(info.getImage()).into(viewHolder.imageViewBackground);
        viewHolder.textViewDescription.setText(info.getTitle());
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(info.getPath()));
                view.getContext().startActivity(browserIntent);
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.deletebox.setVisibility(View.VISIBLE);
            }
        });
        viewHolder.delete_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.deletebox.setVisibility(View.GONE);
            }
        });

        viewHolder.delete_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref=Db.getReference().child("Info");
                ref.child(info.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        viewHolder.deletebox.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        com.github.siyamed.shapeimageview.RoundedImageView imageViewBackground;
        TextView textViewDescription,date,delete_yes,delete_no;
        ProgressBar progressBar;
        RelativeLayout container;
        LinearLayout deletebox;
        ImageView delete;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            container =itemView.findViewById(R.id.info_contain);
            progressBar=itemView.findViewById(R.id.progressBar);
            date=itemView.findViewById(R.id.date);
            delete=itemView.findViewById(R.id.delete);
            delete_yes=itemView.findViewById(R.id.delete_yes);
            deletebox=itemView.findViewById(R.id.deletebox);
            delete_no=itemView.findViewById(R.id.delete_no);
            imageViewBackground = itemView.findViewById(R.id.image_info);
            textViewDescription = itemView.findViewById(R.id.text_info);
        }
    }

    public String getDate(long time) {
        java.util.Calendar cal = java.util.Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }
}
