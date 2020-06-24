package com.mintusharma.webkultask.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.mintusharma.webkultask.R;
import com.mintusharma.webkultask.activities.MainActivity;


public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    EditText editText;
    BaseActivity dialogUtils;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    public void showDialogForDownload() {
        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.file_download_dialog, null, false);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        Button download_btn=view.findViewById(R.id.download);
        Button cancel_btn=view.findViewById(R.id.cancel);
        editText=view.findViewById(R.id.links);

        TextView clear=view.findViewById(R.id.clear_text);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().isEmpty()){
                    editText.setText("");
                }
            }
        });
        download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new MainActivity().downloadDataFromUrl(editText.getText().toString().trim());
                Toast.makeText(getApplicationContext(),"File downloading..",Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
