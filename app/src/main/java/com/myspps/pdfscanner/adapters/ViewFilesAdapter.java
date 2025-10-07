package com.myspps.pdfscanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.interfaces.FilesClickInterface;
import com.myspps.pdfscanner.model.ViewFilesModel;
import java.util.ArrayList;

public class ViewFilesAdapter extends RecyclerView.Adapter<ViewFilesAdapter.ViewHolder> {
    Context context;
    FilesClickInterface itemClickInterface;
    ArrayList<ViewFilesModel> viewFilesArray;

    public ViewFilesAdapter(Context context2, ArrayList<ViewFilesModel> arrayList, FilesClickInterface filesClickInterface) {
        this.context = context2;
        this.viewFilesArray = arrayList;
        this.itemClickInterface = filesClickInterface;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.view_single_file, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.tvName.setText(this.viewFilesArray.get(i).getName());
        viewHolder.tvSize.setText(this.viewFilesArray.get(i).getSize());
        viewHolder.tvDate.setText(this.viewFilesArray.get(i).getDate());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            public final  int f$1;

            {
                this.f$1 = i;
            }

            public final void onClick(View view) {
                ViewFilesAdapter.this.lambda$onBindViewHolder$0$ViewFilesAdapter(this.f$1, view);
            }
        });
        viewHolder.share.setOnClickListener(new View.OnClickListener() {
            public final  int f$1;

            {
                this.f$1 = i;
            }

            public final void onClick(View view) {
                ViewFilesAdapter.this.lambda$onBindViewHolder$1$ViewFilesAdapter(this.f$1, view);
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            public final  int f$1;

            {
                this.f$1 = i;
            }

            public final void onClick(View view) {
                ViewFilesAdapter.this.lambda$onBindViewHolder$2$ViewFilesAdapter(this.f$1, view);
            }
        });
        viewHolder.rename.setOnClickListener(new View.OnClickListener() {
            public final  int f$1;

            {
                this.f$1 = i;
            }

            public final void onClick(View view) {
                ViewFilesAdapter.this.lambda$onBindViewHolder$3$ViewFilesAdapter(this.f$1, view);
            }
        });
    }

    public  void lambda$onBindViewHolder$0$ViewFilesAdapter(int i, View view) {
        this.itemClickInterface.onItemClick(i);
    }

    public  void lambda$onBindViewHolder$1$ViewFilesAdapter(int i, View view) {
        this.itemClickInterface.onShareClick(i);
    }

    public  void lambda$onBindViewHolder$2$ViewFilesAdapter(int i, View view) {
        this.itemClickInterface.onDeleteClick(i);
    }

    public  void lambda$onBindViewHolder$3$ViewFilesAdapter(int i, View view) {
        this.itemClickInterface.onRenameClick(i);
    }

    public int getItemCount() {
        return this.viewFilesArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView delete;
        ImageView rename;
        ImageView share;
        TextView tvDate;
        TextView tvName;
        TextView tvSize;

        public ViewHolder(View view) {
            super(view);
            this.tvName = (TextView) view.findViewById(R.id.tvName);
            this.tvSize = (TextView) view.findViewById(R.id.tvSize);
            this.tvDate = (TextView) view.findViewById(R.id.tvDate);
            this.share = (ImageView) view.findViewById(R.id.share);
            this.rename = (ImageView) view.findViewById(R.id.rename);
            this.delete = (ImageView) view.findViewById(R.id.delete);
        }
    }
}
