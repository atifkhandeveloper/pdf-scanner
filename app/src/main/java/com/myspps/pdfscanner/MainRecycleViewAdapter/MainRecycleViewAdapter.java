package com.myspps.pdfscanner.MainRecycleViewAdapter;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.myspps.pdfscanner.R;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context ctx;
    private int current_selected_idx = -1;
    public List<File> items = new ArrayList();
    
    public OnItemClickListener mOnItemClickListener;
    private OnLoadMoreListener onLoadMoreListener;
    private SparseBooleanArray selected_items;

    public interface OnItemClickListener {
        void onItemClick(View view, File file, int i);

        void onItemLongClick(View view, File file, int i);
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int i);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public MainRecycleViewAdapter(Context context, List<File> list) {
        this.items = list;
        this.ctx = context;
        this.selected_items = new SparseBooleanArray();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView brief;
        public ImageView image;
        public View lyt_parent;
        public TextView name;
        public TextView size;

        public OriginalViewHolder(View view) {
            super(view);
            this.image = (ImageView) view.findViewById(R.id.largeFileIcon);
            this.name = (TextView) view.findViewById(R.id.fileName);
            this.brief = (TextView) view.findViewById(R.id.tvDate);
            this.size = (TextView) view.findViewById(R.id.tvName);
            this.lyt_parent = view.findViewById(R.id.fileName);
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new OriginalViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_single_file, viewGroup, false));
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        final File file = this.items.get(i);
        if (viewHolder instanceof OriginalViewHolder) {
            OriginalViewHolder originalViewHolder = (OriginalViewHolder) viewHolder;
            originalViewHolder.name.setText(file.getName());
            originalViewHolder.brief.setText(new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(new Date(file.lastModified())));
            originalViewHolder.size.setText(GetSize(file.length()));
            originalViewHolder.lyt_parent.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (MainRecycleViewAdapter.this.mOnItemClickListener != null) {
                        MainRecycleViewAdapter.this.mOnItemClickListener.onItemClick(view, file, i);
                    }
                }
            });
            originalViewHolder.lyt_parent.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View view) {
                    if (MainRecycleViewAdapter.this.mOnItemClickListener == null) {
                        return false;
                    }
                    MainRecycleViewAdapter.this.mOnItemClickListener.onItemLongClick(view, file, i);
                    return true;
                }
            });
            toggleCheckedIcon(viewHolder, i);
            originalViewHolder.image.setImageResource(R.drawable.ic_homeeeeeeeeeeeeeeee);
        }
    }

    public int getItemCount() {
        return this.items.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener2) {
        this.onLoadMoreListener = onLoadMoreListener2;
    }

    private void toggleCheckedIcon(RecyclerView.ViewHolder viewHolder, int i) {
        OriginalViewHolder originalViewHolder = (OriginalViewHolder) viewHolder;
        if (this.selected_items.get(i, false)) {
            originalViewHolder.lyt_parent.setBackgroundColor(Color.parseColor("#4A32740A"));
            if (this.current_selected_idx == i) {
                resetCurrentIndex();
                return;
            }
            return;
        }
        originalViewHolder.lyt_parent.setBackgroundColor(Color.parseColor("#ffffff"));
        if (this.current_selected_idx == i) {
            resetCurrentIndex();
        }
    }

    public String GetSize(long j) {
        String[] strArr = {"bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
        double d = (double) j;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        int i = 0;
        while (i < 9 && d >= 1024.0d) {
            d /= 1024.0d;
            i++;
        }
        String format = decimalFormat.format(d);
        return format.concat(" " + strArr[i]);
    }

    public void toggleSelection(int i) {
        this.current_selected_idx = i;
        if (this.selected_items.get(i, false)) {
            this.selected_items.delete(i);
        } else {
            this.selected_items.put(i, true);
        }
        notifyItemChanged(i);
    }

    public int getSelectedItemCount() {
        return this.selected_items.size();
    }

    public void selectAll() {
        for (int i = 0; i < this.items.size(); i++) {
            this.selected_items.put(i, true);
            notifyItemChanged(i);
        }
    }

    public void clearSelections() {
        this.selected_items.clear();
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedItems() {
        ArrayList arrayList = new ArrayList(this.selected_items.size());
        for (int i = 0; i < this.selected_items.size(); i++) {
            arrayList.add(Integer.valueOf(this.selected_items.keyAt(i)));
        }
        return arrayList;
    }

    public void removeData(int i) {
        this.items.remove(i);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        this.current_selected_idx = -1;
    }
}
