package com.myspps.pdfscanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.adapters.ImagePickerAdapter;
import com.myspps.pdfscanner.interfaces.ItemClickInterface;
import com.myspps.pdfscanner.model.ImagePickerModel;
import java.io.File;
import java.util.ArrayList;

public class ImagePickerAdapter extends RecyclerView.Adapter<ImagePickerAdapter.ViewHolder> {
    ItemClickInterface clickInterface;
    Context context;
    int count = 0;
    ArrayList<ImagePickerModel> pickerArray;
    String type;

    public ImagePickerAdapter(Context context2, ArrayList<ImagePickerModel> arrayList, String str, ItemClickInterface itemClickInterface) {
        this.context = context2;
        this.pickerArray = arrayList;
        this.clickInterface = itemClickInterface;
        this.type = str;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_imageview, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ((RequestBuilder) Glide.with(this.context).load(new File(this.pickerArray.get(i).getImagePath())).centerCrop()).into(viewHolder.imageView);
        if (this.pickerArray.get(i).isSelected()) {
            viewHolder.checkBox.setChecked(true);
            this.count++;
            return;
        }
        viewHolder.checkBox.setChecked(false);
        this.count--;
    }

    public int getItemCount() {
        return this.pickerArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.image);
            CheckBox checkBox2 = (CheckBox) view.findViewById(R.id.checkBox);
            this.checkBox = checkBox2;
            checkBox2.setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    ViewHolder.this.lambda$new$0$ImagePickerAdapter$ViewHolder(view);
                }
            });
        }

        public  void lambda$new$0$ImagePickerAdapter$ViewHolder(View view) {
            ImagePickerAdapter.this.clickInterface.onItemClick(getAdapterPosition());
        }
    }
}
