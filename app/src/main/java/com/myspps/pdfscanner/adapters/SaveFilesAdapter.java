package com.myspps.pdfscanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.model.FiltersAppliedModel;
import java.util.ArrayList;

public class SaveFilesAdapter extends RecyclerView.Adapter<SaveFilesAdapter.ViewHolder> {
    Context context;
    ArrayList<FiltersAppliedModel> filtersArrayList;

    public SaveFilesAdapter(Context context2, ArrayList<FiltersAppliedModel> arrayList) {
        this.context = context2;
        this.filtersArrayList = arrayList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.view_file_save, viewGroup, false);
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) inflate.getLayoutParams();
        marginLayoutParams.height = ((viewGroup.getHeight() / 2) - marginLayoutParams.topMargin) - marginLayoutParams.bottomMargin;
        inflate.setLayoutParams(marginLayoutParams);
        return new ViewHolder(inflate);
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ((RequestBuilder) Glide.with(this.context).load(this.filtersArrayList.get(i).getBitmap()).centerCrop()).into(viewHolder.imageView);
    }

    public int getItemCount() {
        return this.filtersArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.view_image);
        }
    }
}
