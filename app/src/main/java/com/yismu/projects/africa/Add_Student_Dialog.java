package com.yismu.projects.africa;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class Add_Student_Dialog extends AppCompatDialogFragment {
    EditText name_txt,phone_txt,date_txt,month_txt;
    Button check_button;
    int date=0,month=0,check_btn_index=0;
    String full_date,Belt="White",Full_Date="01/01/2019";
    add_student_interface add_interface;
    DatabaseClass databaseobject;


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.activity_add__student_dialog_activity,null);

        Calendar calendar=Calendar.getInstance();
        full_date= DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());

        Full_Date="01/01/2019";
        date=0;
        month=0;

        builder.setView(view);
        name_txt=(EditText)view.findViewById(R.id.name_txt);
        phone_txt=(EditText)view.findViewById(R.id.phone_txt);
        date_txt=(EditText)view.findViewById(R.id.date_txt);
        month_txt=(EditText)view.findViewById(R.id.month_txt);
        check_button=(Button)view.findViewById(R.id.check_button);




        check_btn_index=0;


        check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             if(check_button.getText().toString().equals("Edit"))
             {
                 name_txt.setEnabled(true);
                 name_txt.setAllCaps(true);
                 phone_txt.setEnabled(true);
                 date_txt.setEnabled(true);
                 month_txt.setEnabled(true);
                 check_btn_index=0;
                 check_button.setText("Check Data");
             }

             else {

                             if (phone_txt.getText().toString().equals(""))
                                 Toast.makeText(getActivity(), "Please fill phone no", Toast.LENGTH_SHORT).show();
                             else {


                                 if (name_txt.getText().toString().equals(""))
                                     Toast.makeText(getActivity(), "Please fill name", Toast.LENGTH_SHORT).show();

                                 else {

                                     if (phone_txt.getText().toString().length() < 10 )/////////////////////////////////////////
                                         Toast.makeText(getActivity(), "Invalid phone length", Toast.LENGTH_SHORT).show();

                                     else {

                                         String year=full_date.substring(6);

                                         if (date_txt.getText().toString().equals("") && month_txt.getText().toString().equals(""))
                                         {

                                             date = Integer.parseInt(full_date.substring(0, 2));
                                             month = Integer.parseInt(full_date.substring(3, 5));
                                             Full_Date=full_date;

                                         }

                                          if (!(date_txt.getText().toString().equals("")) && !(month_txt.getText().toString().equals("")))

                                         {
                                             if( date_txt.getText().toString().length()<2 )
                                             {
                                                 Toast.makeText(getActivity(), "include 0 at the beginning of date", Toast.LENGTH_SHORT).show();
                                                 return;
                                             }
                                             if(month_txt.getText().toString().length()<2)
                                             {
                                                 Toast.makeText(getActivity(), "include 0 at the beginning of month", Toast.LENGTH_SHORT).show();
                                                 return;
                                             }


                                             date = Integer.parseInt(date_txt.getText().toString());
                                             month = Integer.parseInt(month_txt.getText().toString());
                                             if(month <=12 && date<31)
                                             {
                                                 Full_Date=date_txt.getText().toString().concat( "/").concat( month_txt.getText().toString() ).concat("/").concat(year) ;
                                             }
                                             else
                                             {
                                                 Toast.makeText(getActivity(), "Wrong date or month input", Toast.LENGTH_SHORT).show();
                                                 return;
                                             }

                                         }

                                         if ((date_txt.getText().toString().equals("")) && !(month_txt.getText().toString().equals("")))

                                         {
                                             month = Integer.parseInt(month_txt.getText().toString());
                                             if(month_txt.getText().toString().length()<2)
                                             {
                                                 Toast.makeText(getActivity(), "include 0 at the beginning of month", Toast.LENGTH_SHORT).show();
                                                 return;
                                             }

                                             if (month <= 12) {

                                                 date = Integer.parseInt(full_date.substring(0, 2));
                                                 Full_Date = full_date.substring(0, 2).concat("/").concat(month_txt.getText().toString()).concat("/").concat(year);

                                             }
                                             else {
                                                 Toast.makeText(getActivity(), "wrong month input", Toast.LENGTH_SHORT).show();
                                                 return;
                                             }

                                         }


                                         if (!(date_txt.getText().toString().equals("")) && month_txt.getText().toString().equals(""))
                                         {
                                             if( date_txt.getText().toString().length()<2)
                                             {
                                                 Toast.makeText(getActivity(), "invalid date length", Toast.LENGTH_SHORT).show();
                                                 return;
                                             }
                                                          date = Integer.parseInt(date_txt.getText().toString());

                                                          if (date <= 31)
                                                          {
                                                              month = Integer.parseInt(full_date.substring(3, 5));
                                                              Full_Date = date_txt.getText().toString().concat("/").concat(full_date.substring(3, 5)).concat("/").concat(year);
                                                          }
                                                          else {
                                                              Toast.makeText(getActivity(), "wrong date input.", Toast.LENGTH_SHORT).show();
                                                              return;
                                                          }

                                         }

                                         check_btn_index = 1;
                                         check_button.setText("Edit");

                                         name_txt.setEnabled(false);
                                         phone_txt.setEnabled(false);
                                         date_txt.setEnabled(false);
                                         month_txt.setEnabled(false);
                                         Toast.makeText(getActivity(), Full_Date, Toast.LENGTH_SHORT).show();

                                     }

                                 }

                             }

             }

            }
        });

        builder.setTitle("Register Student")
                .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

               if(check_btn_index==1)
               {

                   add_interface.add_student(name_txt.getText().toString(), phone_txt.getText().toString(), date, month,Full_Date.toString() , "Male", "White");
               }


               else
                Toast.makeText(getActivity(), "Student not Verified, not Registered", Toast.LENGTH_LONG).show();
            }
        });



        return builder.create();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        add_interface=(add_student_interface) context;
    }


    public interface add_student_interface{
        public void add_student(String name, String phone, int date, int month, String full, String gender, String belt);
    }
}
