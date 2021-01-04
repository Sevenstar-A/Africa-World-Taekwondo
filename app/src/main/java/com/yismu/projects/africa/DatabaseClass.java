package com.yismu.projects.africa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class DatabaseClass extends SQLiteOpenHelper{

Context context;
    public DatabaseClass(Context context) {
        super(context, "Student", null, 3);
        this.context=context;
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("Create Table Student( ID INTEGER Primary Key Autoincrement ,Name varchar(30), Phone varchar(10), Registration_Date INTEGER(2),Registration_Month Integer(2),Registration_Full varchar(12),Status varchar(10), Gender varchar(5), Belt varchar(10));");
        sqLiteDatabase.execSQL( "Create Table Month ( ID INTEGER Primary Key Autoincrement ,Name varchar(20), Student_num Integer(3), Income Integer(5), Year varchar(12));" );

        ContentValues contentValues=new ContentValues();
        contentValues.put("Name","Antenyismu Yirsaw");
        contentValues.put("Phone","0924599500");
        contentValues.put("Registration_Date",1);
        contentValues.put("Registration_Month",1);
        contentValues.put("Registration_Full","01/01/2019");
        contentValues.put("Status","Payed");
        contentValues.put("Gender","Male");
        contentValues.put("Belt","Black");

        sqLiteDatabase.insert("Student",null,contentValues);

        contentValues.clear();
        contentValues.put("Name","Sabom Henok");
        contentValues.put("Phone","0901234595");
        contentValues.put("Registration_Date",1);
        contentValues.put("Registration_Month",1);
        contentValues.put("Registration_Full","01/01/2019");
        contentValues.put("Status","Payed");
        contentValues.put("Gender","Male");
        contentValues.put("Belt","Black");

        sqLiteDatabase.insert("Student",null,contentValues);



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Toast.makeText(context, "database upgrader was called ", Toast.LENGTH_SHORT).show();
        sqLiteDatabase.execSQL("Drop Table If Exists Student");
        sqLiteDatabase.execSQL("Drop Table If Exists Month");
        onCreate(sqLiteDatabase);

    }
//    //////////////////////////////////////////////////
    public int check_to_add(String name, String phone)
    {

        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.query("Student",new String[]{"Name","Phone","Status"},"Name = ?, Phone = ?",new String[]{name,phone},null,null,null);
        int num=cursor.getCount();
        cursor.moveToFirst();

        if( num > 0 )
        {
            if( cursor.getString(cursor.getColumnIndex("Status")).equals("Removed"))
                return 1;///The student has been registered, and Removed before so can be re-registered

            else
                return 0;///The student is registered and active
        }
        else
            return 2;

    }

    public void add_student(String name, String phone ,int date,int month, String full_date,String gender, String belt )
    {

        Calendar current_date=Calendar.getInstance();
        String current_full_date= DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(current_date.getTime());
        int current_day=Integer.parseInt((full_date.substring(0,2)));
        int current_month=Integer.parseInt((full_date.substring(3,5)));

        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("Select Name, Phone, Status from Student",null);
        while(cursor.moveToNext())
        {
            if(cursor.getString(0).equals(name) && cursor.getString(1).equals(phone) )
            {
                if(cursor.getString(2).equals("Removed"))
                {
                        new_updater(1,name,phone,date,month,full_date,"Payed",gender,belt);
                         Toast.makeText(context, "Student "+name+" Re-registered", Toast.LENGTH_SHORT).show();

                         update_month_data( 50);//Record month data
                         return;
                }

                else
                {
                    Toast.makeText(context, "Student already registered, Please use another name ", Toast.LENGTH_LONG).show();
                    return;
                }

            }
        }
        cursor.close();

        ContentValues contentValues=new ContentValues();
        contentValues.put("Name",name);
        contentValues.put("Phone",phone);
        contentValues.put("Registration_Date",date);
        contentValues.put("Registration_Month ",month);
        contentValues.put("Registration_Full",full_date);
        contentValues.put("Status","Payed");
        contentValues.put("Gender",gender);
        contentValues.put("Belt",belt);

        long i=db.insert("Student",null,contentValues);
        if(i!=-1)
        {
            String [] month_name={"none","January","February","March","April","May","June","July","August","September","October","November","December"};

              // update_month_data( 60);
           month_data_updater(month_name[month],60);

            Toast.makeText(context, "Student Registered", Toast.LENGTH_SHORT).show();


        }
        else
            Toast.makeText(context, "Student not Registered", Toast.LENGTH_SHORT).show();


        status_changer();

    }

    public void status_changer()
    {
        Calendar current_date=Calendar.getInstance();
        String full_date= DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(current_date.getTime());
        int date=Integer.parseInt( (full_date.substring(0,2) ) );

//        int month=Integer.parseInt( (full_date.substring(3,5)) );
        int month=3;


        SQLiteDatabase db=this.getWritableDatabase();
        String[] columns={"Id","Name","Phone","Registration_Date","Registration_Month","Registration_Full","Status","Gender","Belt"};

        Cursor cursor=db.query("Student", columns,"Status = ?",new String[]{"Payed"},null,null,null);

        while(cursor.moveToNext())
        {

            if(date > cursor.getInt(cursor.getColumnIndex("Registration_Date")) && month > cursor.getInt(cursor.getColumnIndex("Registration_Month"))  )
            {
                if(cursor.getString(cursor.getColumnIndex("Name")).startsWith("Sabom") || cursor.getString(cursor.getColumnIndex("Name")).equals("Antenyismu Yirsaw"))
                {
                    continue;
                }
                else {
                    new_updater(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), "Unpayed", cursor.getString(7), cursor.getString(8));

                }

            }
            else
                continue;

        }

        cursor.close();

    }

    public void new_updater(int id,String name,String phone,int date, int month,String full, String status,String gender,String belt)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put("Name",name);
        contentValues.put("Phone",phone);
        contentValues.put("Registration_Date",date);
        contentValues.put("Registration_Month",month);
        contentValues.put("Registration_Full",full);
        contentValues.put("Status",status);
        contentValues.put("Gender",gender);
        contentValues.put("Belt",belt);

         db.update("Student",contentValues,"Name = ?",new String[]{name});

    }

    public void new_year_month_creater(String year)
    {
        String [] month_name={"none","January","February","March","April","May","June","July","August","September","October","November","December"};

        SQLiteDatabase db=this.getWritableDatabase();
        int i=1;

        while(i<=12)
        {
            ContentValues contentValues=new ContentValues();
            contentValues.put("Name", month_name[i]);
            contentValues.put("Student_num",0);
            contentValues.put("Income",0);
            contentValues.put("Year",year);

            db.insert("Month",null,contentValues);
            i++;
        }
        Toast.makeText(context, "new Year months created", Toast.LENGTH_SHORT).show();

    }

    public void update_month_data(int amount_payed)
    {
        Calendar date=Calendar.getInstance();
        String full_date= DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(date.getTime());

        int current_month = Integer.parseInt((full_date.substring(3,5)));
        String year = full_date.substring(6);


        String [] month_name={"none","January","February","March","April","May","June","July","August","September","October","November","December"};

        SQLiteDatabase db=this.getWritableDatabase();
        String[] col={"Name","Student_num","Income","Year"};
        Cursor cursor=db.query("Month",col,"Name = ?",new String[]{month_name[current_month]},null,null,null);

        cursor.moveToFirst();

        ContentValues contentValues=new ContentValues();
        contentValues.put("Name",cursor.getString(0));
        contentValues.put("Student_num",(cursor.getInt(1)+1) );
        contentValues.put("Income",(cursor.getInt(2)) +amount_payed);
        contentValues.put("Year",year);
        db.update("Month",contentValues,"Name = ?",new String[]{cursor.getString(0)});


        }

        public void month_data_updater(String name,int amount_payed)
        {
            String [] month_name={"none","January","February","March","April","May","June","July","August","September","October","November","December"};

            SQLiteDatabase db=this.getWritableDatabase();
            String[] col={"Name","Student_num","Income","Year"};
            Cursor cursor=db.query("Month",col,"Name = ?",new String[]{name},null,null,null);
            cursor.moveToFirst();
            String year=cursor.getString(cursor.getColumnIndex("Year"));

            ContentValues contentValues=new ContentValues();
            contentValues.put("Name",cursor.getString(0));
            contentValues.put("Student_num",(cursor.getInt(1)+1) );
            contentValues.put("Income",(cursor.getInt(2)) +amount_payed);
            contentValues.put("Year",year);
            db.update("Month",contentValues,"Name = ?",new String[]{cursor.getString(0)});

        }




}




