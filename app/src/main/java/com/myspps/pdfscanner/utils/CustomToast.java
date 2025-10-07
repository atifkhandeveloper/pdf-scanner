package com.myspps.pdfscanner.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.myspps.pdfscanner.R;

public class CustomToast extends Toast {
    Context context;
    String message;
    TextView textView;

    public CustomToast(Context context2, String str) {
        super(context2);
        this.context = context2;
        this.message = str;
        View inflate = LayoutInflater.from(context2).inflate(R.layout.custom_toast, (ViewGroup) null);
        TextView textView2 = (TextView) inflate.findViewById(R.id.txt_message);
        this.textView = textView2;
        textView2.setText(str);
        setView(inflate);
        setGravity(17, 0, 0);
        setDuration(0);
        show();
    }
}
