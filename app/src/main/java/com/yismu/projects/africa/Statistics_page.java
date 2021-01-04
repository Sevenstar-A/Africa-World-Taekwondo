package com.yismu.projects.africa;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class Statistics_page extends AppCompatActivity {

    TextView editText;
    ImageButton student_image_button, statistics_image_button;
    DatabaseClass databaseClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_page);

        databaseClass=new DatabaseClass(this);
        editText=(TextView) findViewById(R.id.dataviewer);
        student_image_button=(ImageButton)findViewById(R.id.student_button);
        statistics_image_button=(ImageButton)findViewById(R.id.money_button);
//        editText.setEnabled(false);
//

        statistics_information();



        student_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                student_information();
            }
        });

        statistics_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statistics_information();
            }
        });

    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Calendar current_date=Calendar.getInstance();
        String full_date= DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(current_date.getTime());

        String last_checked_year=full_date.substring(6);
       outState.putString("last_checked_year",last_checked_year);
        Toast.makeText(this, "onSaveInstace method called"+last_checked_year, Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String last_checked_year=savedInstanceState.getString("last_checked_year","");

        Calendar current_date=Calendar.getInstance();
        String full_date= DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(current_date.getTime());
        String current_year=full_date.substring(6);
        Toast.makeText(this, "onRestore method was called"+current_year, Toast.LENGTH_SHORT).show();


        SQLiteDatabase db=databaseClass.getWritableDatabase();
        Cursor cursor=db.rawQuery("Select Name From Month",null);


       if( cursor.getCount()==0 || !current_year.equals(last_checked_year)  )
       {
          databaseClass.new_year_month_creater(current_year);

       }


    }
    //    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//
////        Calendar date=Calendar.getInstance();
////        String full_date= DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(date.getTime());
////
////        int current_date=Integer.parseInt((full_date.substring(0,2)));
////        int last_date=savedInstanceState.getInt("last_checked_date",1);
////
////             int current_month = Integer.parseInt((full_date.substring(3,5)));
////             int last_month = savedInstanceState.getInt("last_checked_month",1);
////
////            String last_checked_year=savedInstanceState.getString("last_checked_year","2019");
////            String current_year=full_date.substring(6,10);
////             Toast.makeText(this, "onRestoreInstance "+current_month+" "+last_month, Toast.LENGTH_SHORT).show();
////            if(current_month!=last_month)
////            {
//
//                    String [] month_name={"none","January","February","March","April","May","June","July","August","September","October","November","December"};
//                    SQLiteDatabase db=databaseClass.getWritableDatabase();
//
//
//                    Cursor cursor=db.query("Student",new String[]{"Status","Registration_Month"},"Status = ?",new String[]{"Payed"},null,null,null);
//                    int Student_num=0;
//
//                    while(cursor.moveToNext())
//
//                    {
//                    if(month_name[last_month].equals( month_name[cursor.getInt(1)]) )
//                        Student_num++;
//                    }
//
//                     int total_month_income = Student_num * 50;
//
//                    cursor.close();
//
//                //  Name varchar(20), Student_num Integer(3), Income Integer(5), Year varchar(12)
//
//                    Cursor cursor1=db.query("Month",new String[]{"Name","Year"},"Name = ?, Year = ?", new String[]{month_name[last_month], current_year}, null,null,null );
//
//
//
//                        ContentValues contentValues=new ContentValues();
//
//                        contentValues.put("Name",month_name[last_month]);
//                        contentValues.put("Student_num",Student_num);
//                        contentValues.put("Income",total_month_income);
//                        contentValues.put("Year",last_checked_year);
//
//                        if( db.insert("Month",null, contentValues) > 0 )
//                    Toast.makeText(this, "Data saved for "+ month_name[last_month], Toast.LENGTH_SHORT).show();
//
//                        else
//                            Toast.makeText(this, "Data not for "+month_name[last_month], Toast.LENGTH_SHORT).show();
//
//
//
//
//
//            }
//
//    }

    private void student_information()
    {
        SQLiteDatabase db=databaseClass.getWritableDatabase();
        Cursor data=db.rawQuery("Select Name From Student",null);
        int total=data.getCount();


        data=db.query("Student",new String[]{"Status"},"Status != ?",new String[] {"Removed"},null,null,null);
        int Active=data.getCount();

        data=db.query("Student",new String[]{"Status"},"Status = ?",new String[]{"Payed"},null,null,null);
        int payed=data.getCount();

        data=db.query("Student",new String[]{"Status"},"Status = ?",new String[]{"Unpayed"},null,null,null);
        int unpayed=data.getCount();

        data=db.query("Student",new String[]{"Status"},"Status = ?",new String[]{"Removed"},null,null,null);
        int removed=data.getCount();

        data=db.query("Student",new String[]{"Gender"},"Gender = ?",new String[]{"Male"},null,null,null);
        int male=data.getCount();

        data=db.query("Student",new String[]{"Gender"},"Gender = ?",new String[]{"Female"},null,null,null);
        int female=data.getCount();

        editText.setText("     Student Related Information "+"\n\n"+
                "1. Number of all Registered Students: "+total+"\n\n"+
                "2. Number of Active Students: "+Active+"\n"+
                "              Payed Students: "+payed+" \n"+
                "           Unpayed Students: "+unpayed+"\n\n"+
                "3. Number of Removed Students: "+removed+"\n\n"+
                "4. Male Students: "+male+"\n\n"+
                "5. Female Students: "+female

        );

    }

    private void statistics_information()
    {
        SQLiteDatabase db=databaseClass.getWritableDatabase();
        Calendar current_date=Calendar.getInstance();


        String full_date= DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(current_date.getTime());
        int current_month=Integer.parseInt((full_date.substring(3,5)));
        Cursor cursor=db.rawQuery("Select * From Month",null);

        String [] month_name={"none","January","February","March","April","May","June","July","August","September","October","November","December"};

        int Student_num=0, month_income=0;
        String name="";
        String year="";
        year=full_date.substring(6);

        int i=1;
        StringBuilder data=new StringBuilder();

        while(cursor.moveToNext())
        {
            Student_num = cursor.getInt(cursor.getColumnIndex("Student_num") );
            name=cursor.getString(cursor.getColumnIndex("Name"));

            if( Student_num >0)
            {
                year=cursor.getString(cursor.getColumnIndex("Year"));
                month_income=cursor.getInt(cursor.getColumnIndex("Income"));

                if(name.equals("January"))
                {
                        data.append("\t\t\t\t Statistical Data of year "+year+"\n\n");

                        if (name.equals(month_name[current_month]))
                            data.append(i + ". " + name + " " + year + ":-" + Student_num + " Students " + month_income + " Birr (Current month)\n");
                        else
                            data.append(i + ". " + name + " " + year + ":-" + Student_num + " Students " + month_income + " Birr\n");

                        if(i%3==0)
                            data.append("\n");



                }

                else
                 {

                        if (name.equals(month_name[current_month]))
                            data.append(i + ". " + name + " " + year + ":-" + Student_num + " Students " + month_income + "Birr (Current month)\n");
                        else
                            data.append(i + ". " + name + " " + year + ":-" + Student_num + " Students " + month_income + " Birr\n");

                        if(i%3==0)//to create a gap between every 3 month data
                            data.append("\n");

                 }


                i++;

            }

            else
                {
                    if(name.equals("January"))
                    {
                        data.append("    Statistical data of "+year+"\n");
                    }

                    data.append(i+". " + name+"\n");
                    i++;
                continue;


            }



        }

        editText.setText(data.toString());


    }


}
