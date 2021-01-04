package com.yismu.projects.africa;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class Edit_Profile extends AppCompatActivity  implements Alert_dialog.alert_dialog_interface{

    String  Name,Phone,Full_Date,Gender,Belt,Status,student_name;
    int RDate=0,Rmonth=0,id=0;
    EditText name,phone,date,gender,belt;
    ImageButton paying_btn,edit_btn,remove_btn;
    ImageView imageView;
    DatabaseClass databaseobject;
    CheckBox checkBox;
    Button save_btn;
    int alert_dialog_index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile);

       Intent intent=getIntent();

        student_name=intent.getExtras().getString("name");

        databaseobject=new DatabaseClass(this);

        imageView=(ImageView)findViewById(R.id.edit_profile_imageview);
        name=(EditText)findViewById(R.id.edit_profile_name_txt);
        phone=(EditText)findViewById(R.id.edit_profile_phone_txt);
        date=(EditText)findViewById(R.id.edit_profile_date_txt);
        belt=(EditText)findViewById(R.id.edit_profile_belt_txt);
        gender=(EditText)findViewById(R.id.edit_profile_gender_txt);
        paying_btn=(ImageButton)findViewById(R.id.pay_button);
        edit_btn=(ImageButton)findViewById(R.id.edit_button);
        remove_btn=(ImageButton)findViewById(R.id.delete_button);
        checkBox=(CheckBox)findViewById(R.id.edit_profile_checkbox);
        save_btn=(Button)findViewById(R.id.edit_profile_save_button);

       SQLiteDatabase db=databaseobject.getWritableDatabase();
       String[] columns={"id","Name","Phone","Registration_Date","Registration_Month","Registration_Full","Status","Gender","Belt"};
       final Cursor cursor=db.query("Student",columns,"Name = ?",new String[] {student_name},null,null,null);
        cursor.moveToFirst();


        if(student_name.startsWith("Sabom"))
            imageView.setImageResource(R.drawable.sabom_henok);
        else if(student_name.equals("Antenyismu Yirsaw"))
        imageView.setImageResource(R.drawable.yismu);
        else
            imageView.setImageResource(R.drawable.background);

    id=cursor.getInt(0);
  Name=cursor.getString(1);
  Phone=cursor.getString(2);
  RDate=cursor.getInt(cursor.getColumnIndex("Registration_Date"));
  Rmonth=cursor.getInt(cursor.getColumnIndex("Registration_Month"));
  Full_Date=cursor.getString(cursor.getColumnIndex("Registration_Full"));
  Status=cursor.getString(cursor.getColumnIndex("Status"));
  Gender=cursor.getString(cursor.getColumnIndex("Gender"));
  Belt=cursor.getString(cursor.getColumnIndex("Belt"));



  if(Status.equals("Payed"))
      checkBox.setChecked(true);

  else
   checkBox.setChecked(false);

  checkBox.setEnabled(false);

  name.setText(Name);
  phone.setText(Phone);
  date.setText(Full_Date);
  gender.setText(cursor.getString(cursor.getColumnIndex("Gender")));
  belt.setText(cursor.getString(cursor.getColumnIndex("Belt")));

  name.setEnabled(false);
  phone.setEnabled(false);
  date.setEnabled(false);
  gender.setEnabled(false);
  belt.setEnabled(false);
  save_btn.setEnabled(false);

        cursor.close();



        paying_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alert_dialog alert_dialog=new Alert_dialog();
                if( student_name.equals("Antenyismu Yirsaw") )
                {
                    alert_dialog.edittextstring = student_name+" is the Creator of this app, And best Friend of the Instructor." +
                                                                " No need of Payment";
                    alert_dialog.imageindex = 1;//index of R.drawable.yismu on alert_dialog.images[]


                }

                else if (student_name.equals("Sabom Henok"))
                {
                    alert_dialog.edittextstring = student_name + " is the Instructor of Africa World Taekwondo. No need of Payment";
                    alert_dialog.imageindex=2;//index of R.drawable.sabom_henok on alert_dialog.images[]

                }
                else
                    {

                Calendar current_date=Calendar.getInstance();
                String full_date= DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(current_date.getTime());
                int current_month=Integer.parseInt((full_date.substring(3,5)));
                String current_year=full_date.substring(6);
                String [] month_name={"none","January","February","March","April","May","June","July","August","September","October","November","December"};


                alert_dialog.edittextstring=" Comfirm Payment of "+student_name+" for "+month_name[current_month]+ " "+current_year;
                alert_dialog.from_where=1;
                }
                alert_dialog.imageindex=0;
                alert_dialog.show(getSupportFragmentManager(),"");


            }});


            edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(student_name.equals("Antenyismu Yirsaw"))
                {
                    Toast.makeText(Edit_Profile.this, "You cannot Edit This account", Toast.LENGTH_SHORT).show();
                }
                else if (student_name.equals("Sabom Henok"))
                    Toast.makeText(Edit_Profile.this, "You cannot Edit This account!\n He is the Instructor of Africa WTF ", Toast.LENGTH_SHORT).show();

               else {
                    name.setEnabled(true);
                    phone.setEnabled(true);
                    gender.setEnabled(true);
                    belt.setEnabled(true);
                    save_btn.setEnabled(true);
                }
            }
        });

            remove_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Alert_dialog alert_dialog=new Alert_dialog();

                    if( student_name.equals("Antenyismu Yirsaw") )
                    {

                        alert_dialog.edittextstring = student_name+" is the Creator of this app," +
                                " Go fuck your self before deleting this account";
                        alert_dialog.imageindex=3;

                     }

                    else if (student_name.equals("Sabom Henok"))
                    {
                        alert_dialog.edittextstring = "So, You want to Delete Sabom Henok's account, Injoy the pics below\n Touch the image to change ";
                        alert_dialog.imageindex=3;

                    }
                    else{


                    alert_dialog.edittextstring=("Are you sure you want to delete the "+
                                                        "Account of "+student_name+"?");
                    alert_dialog.from_where=2;
                    alert_dialog.imageindex=0;
                    }

                    alert_dialog.show(getSupportFragmentManager(),"");





                }
            });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db=databaseobject.getWritableDatabase();
                String[] columns={"Name","Registration_Date","Registration_Month","Registration_Full","Status"};
                Cursor cursor2=db.query("Student",columns,"Name = ?",new String[] {student_name},null,null,null);
                cursor2.moveToFirst();

                ContentValues contentValues=new ContentValues();
                contentValues.put("Name",name.getText().toString());
                contentValues.put("Phone",phone.getText().toString());
                contentValues.put("Registration_Date",cursor2.getInt(1));
                contentValues.put("Registration_Month",cursor2.getInt(2));
                contentValues.put("Status",cursor2.getString(4));
                contentValues.put("Gender",gender.getText().toString());
                contentValues.put("Belt",belt.getText().toString());

                int result = db.update("Student",contentValues,"Name = ?",new String[]{student_name});

                if(result>0)
                    Toast.makeText(Edit_Profile.this, "Edited successfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Edit_Profile.this, "Not editted", Toast.LENGTH_SHORT).show();

                name.setEnabled(false);
                phone.setEnabled(false);
                date.setEnabled(false);
                gender.setEnabled(false);
                belt.setEnabled(false);
                save_btn.setEnabled(false);
                cursor2.close();

            }
        });



    }

    private void changeToMainActivity() {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }


    @Override
    public void getResult(int index)/// method for doing stuffs based on the alert_dialog confirmation
    {

           if(index==0)
           {
              //means cancle button was clicked on the alert_dialog, for any action button


           }

           else if (index==1)//means ok button was clicked on the alert_dialog for payment
           {
               Calendar current_date=Calendar.getInstance();
               String full_date= DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(current_date.getTime());
               int current_month=Integer.parseInt((full_date.substring(3,5)));

               SQLiteDatabase db=databaseobject.getWritableDatabase();
               String[] columns={"Id","Name","Phone","Registration_Date","Registration_Month","Registration_Full","Status","Gender","Belt"};
               Cursor cursor1=db.query("Student",columns,"Name = ?",new String[]{student_name},null,null,null);
               cursor1.moveToFirst();
               Status=cursor1.getString(cursor1.getColumnIndex("Status"));

               if(!Status.equals("Payed"))
               {

                   databaseobject. new_updater(cursor1.getInt(0),cursor1.getString(1),cursor1.getString(2), cursor1.getInt(3),current_month,cursor1.getString(5),"Payed",cursor1.getString(7),cursor1.getString(8));

                   databaseobject.update_month_data(50);

                   Toast.makeText(Edit_Profile.this, ""+student_name+" has payed successfully", Toast.LENGTH_SHORT).show();
                   checkBox.setChecked(true);
               }

               else
                   Toast.makeText(Edit_Profile.this, "Student already payed", Toast.LENGTH_SHORT).show();
               cursor1.close();

           }

           else if(index==2)
           {
               ///means ok was clicked on the alert dialog for removing

               databaseobject.new_updater(1,Name,Phone,RDate,Rmonth,Full_Date,"Removed",Gender,Belt);
               Toast.makeText(Edit_Profile.this, student_name+" is Removed!", Toast.LENGTH_SHORT).show();
               finish();
               changeToMainActivity();

           }

    }


}
