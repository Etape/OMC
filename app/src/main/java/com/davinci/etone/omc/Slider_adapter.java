package com.davinci.etone.omc;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Slider_adapter extends SliderViewAdapter<Slider_adapter.SliderAdapterVH> {

    private Context context;
    private List<Info> mSliderItems = new ArrayList<>();

    public Slider_adapter(Context context,List<Info> list) {
        this.context = context;
        this.mSliderItems=list;
    }

    public void renewItems(List<Info> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(Info sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
        Info sliderItem = mSliderItems.get(position);
        //Picasso.get().load(sliderItem.getImage()).transform(new Rounded_transform(20,0)).into
         //       (viewHolder.imageViewBackground);
        Picasso.get().load(sliderItem.getImage()).into(viewHolder.imageViewBackground);
        viewHolder.textViewDescription.setText(sliderItem.getTitle());
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=sliderItem.path;
                if (url.equals("path")){
                    Toast.makeText(context, "No path linked to the Info", Toast.LENGTH_SHORT)
                            .show();
                }
                else if (!url.startsWith("http://") && !url.startsWith("https://")){
                    url = "http://" + url;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    v.getContext().startActivity(browserIntent);
                }
                else{
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    v.getContext().startActivity(browserIntent);
                }
            }
        });
    }
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    public static class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        com.github.siyamed.shapeimageview.RoundedImageView imageViewBackground;
        TextView textViewDescription;
        ProgressBar progressBar;
        RelativeLayout container;
        public SliderAdapterVH(View itemView) {
            super(itemView);
            container =itemView.findViewById(R.id.info_contain);
            progressBar=itemView.findViewById(R.id.progressBar);
            imageViewBackground = itemView.findViewById(R.id.image_info);
            textViewDescription = itemView.findViewById(R.id.text_info);
        }
    }
}