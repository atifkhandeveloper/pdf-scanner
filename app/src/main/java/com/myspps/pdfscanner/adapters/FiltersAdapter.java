package com.myspps.pdfscanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.interfaces.FilterClickInterface;
import com.myspps.pdfscanner.model.FilterModel;
import java.util.ArrayList;

public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.ViewHolder> {
    Context context;
    FilterClickInterface filterClickInterface;
    ArrayList<FilterModel> imageFiltersArray;

    public FiltersAdapter(Context context2, ArrayList<FilterModel> arrayList, FilterClickInterface filterClickInterface2) {
        this.context = context2;
        this.imageFiltersArray = arrayList;
        this.filterClickInterface = filterClickInterface2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.single_filtered_image, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ((RequestBuilder) Glide.with(this.context).load(this.imageFiltersArray.get(i).getBitmapPath()).centerCrop()).into(viewHolder.filteredImageView);
        viewHolder.filterName.setText(this.imageFiltersArray.get(i).getFilterName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            public final  int f$1;

            {
                this.f$1 = i;
            }

            public final void onClick(View view) {
                FiltersAdapter.this.lambda$onBindViewHolder$0$FiltersAdapter(this.f$1, view);
            }
        });
    }

    public  void lambda$onBindViewHolder$0$FiltersAdapter(int i, View view) {
        this.filterClickInterface.onFilterClick(i, this.imageFiltersArray.get(i).getBitmapPath());
    }

    public int getItemCount() {
        return this.imageFiltersArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView filterName;
        ImageView filteredImageView;

        public ViewHolder(View view) {
            super(view);
            this.filterName = (TextView) view.findViewById(R.id.filter_name);
            this.filteredImageView = (ImageView) view.findViewById(R.id.iv_setFilters);
        }
    }
}
