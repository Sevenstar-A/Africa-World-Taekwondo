package com.yismu.projects.africa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class Add_student_activity extends AppCompatActivity {
    EditText name_txt,phone_txt,belt_txt,date_txt,month_txt;
    Button check_button;
    int date,month,check_btn_index;
    String full_date,Belt;
    DatabaseClass databaseobject;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);

        Calendar calendar=Calendar.getInstance();
        full_date= DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());

        databaseobject=new DatabaseClass(this);


        name_txt=(EditText)findViewById(R.id.name_txt);
        phone_txt=(EditText)findViewById(R.id.phone_txt);

        date_txt=(EditText)findViewById(R.id.date_txt);
        month_txt=(EditText)findViewById(R.id.month_txt);
        check_button=(Button)findViewById(R.id.check_button);

        check_btn_index=0;


        check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_btn_index=0;
                check_button.setText("CHECK VALIDITY");

                name_txt.setEnabled(true);
                phone_txt.setEnabled(true);
                date_txt.setEnabled(true);
                month_txt.setEnabled(true);
                belt_txt.setEnabled(true);

                if(name_txt.getText().toString().equals(""))
                    Toast.makeText(Add_student_activity.this, "Please Fill Name", Toast.LENGTH_SHORT).show();

                else{

                    if(phone_txt.getText().toString().equals("") )
                        Toast.makeText(Add_student_activity.this, "Please Fill Phone", Toast.LENGTH_SHORT).show();
                    else{
                        if(phone_txt.getText().toString().length()<10)
                            Toast.makeText(Add_student_activity.this, "Invalid Phone number", Toast.LENGTH_SHORT).show();
                        else {


                            if (date_txt.getText().toString().equals(""))
                                date = Integer.parseInt((full_date.substring(0, 2)));
                            else
                                date = Integer.parseInt(date_txt.getText().toString());

                            if (month_txt.getText().toString().equals(""))
                                month = Integer.parseInt((full_date.substring(3, 5)));
                            else
                                month = Integer.parseInt(month_txt.getText().toString());

                            if (!month_txt.getText().toString().equals("") && !date_txt.getText().toString().equals(""))
                                full_date = date_txt.getText().toString() + "/" + month_txt.getText().toString() + "/" + "2019";

                            if (belt_txt.getText().toString().equals(""))
                                Belt = "White";
                            else
                                Belt = belt_txt.getText().toString();


                            int check=databaseobject.check_to_add(name_txt.getText().toString(),phone_txt.getText().toString());

                            if(check==0)
                                Toast.makeText(Add_student_activity.this, "Student already registered, Please use another name .", Toast.LENGTH_SHORT).show();
                            else
                            {
                                if(check==1)
                                {
                                    Toast.makeText(Add_student_activity.this, "Student was Removed, Can Re-register now", Toast.LENGTH_SHORT).show();
                                    check_btn_index=1;

                                    check_button.setText("Edit Data");
                                    name_txt.setEnabled(false);
                                    phone_txt.setEnabled(false);
                                    date_txt.setEnabled(false);
                                    month_txt.setEnabled(false);
                                    belt_txt.setEnabled(false);

                                }

                                else if(check==2)
                                {
                                    Toast.makeText(Add_student_activity.this, "Student can valid to be Registered", Toast.LENGTH_SHORT).show();
                                    check_btn_index=1;

                                    check_button.setText("Edit Data");
                                    name_txt.setEnabled(false);
                                    phone_txt.setEnabled(false);
                                    date_txt.setEnabled(false);
                                    month_txt.setEnabled(false);
                                    belt_txt.setEnabled(false);
                                }
                            }

                        }

                    }

                }




            }
        });


    }
}
