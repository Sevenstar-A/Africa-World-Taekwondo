package com.yismu.projects.africa;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Alert_dialog extends AppCompatDialogFragment {

    String edittextstring="";
    int  from_where=0;
    int imageindex=0, imagecounter=1;
    TextView editText;
    ImageView alert_dialog_imageview;
    alert_dialog_interface alert_dialog_interface;

    int[] images={R.drawable.background, R.drawable.yismu, R.drawable.sabom_henok, R.drawable.cant_doit, R.drawable.monkey,R.drawable.just_for_you};
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.alert_dialog,null);


        builder.setView(view);
        editText=(TextView) view.findViewById(R.id.alert_dialog_edittext);
        alert_dialog_imageview=(ImageView)view.findViewById(R.id.alert_dialog_imageview);


        imagecounter = imageindex;
        editText.setText(edittextstring);
        alert_dialog_imageview.setImageResource( images[imageindex] );

        alert_dialog_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(imageindex>2)//means the user is doing bad things
                {

                    if(imagecounter < images.length)
                    alert_dialog_imageview.setImageResource( images[imagecounter++] );
                    else
                    {
                    imagecounter=imageindex;
                    alert_dialog_imageview.setImageResource(images[imagecounter++]);
                    }

                }

            }
        });


               builder.setTitle("       Comfirm Action")
                       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      //  Toast.makeText(getActivity(), "ok was clicked", Toast.LENGTH_SHORT).show();
                      alert_dialog_interface.getResult(from_where);

                    }
                })
                .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alert_dialog_interface.getResult(0);
                    }
                });
        return builder.create();


    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        alert_dialog_interface=(alert_dialog_interface)context;

    }

    public interface alert_dialog_interface
    {
        void getResult(int index);

    }

}
