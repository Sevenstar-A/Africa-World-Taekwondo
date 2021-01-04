package com.yismu.projects.africa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements Add_Student_Dialog.add_student_interface{//implements Add_Student_Dialog.add_student_interface{
    ImageButton add_button, search_button;
    ListView listview;
    AutoCompleteTextView autoCompleteTextView;
    DatabaseClass databaseClass=new DatabaseClass(this);;
    String sortingString="Name", viewString="Active";
     static Cursor static_cursor;///used for populating listview, onListviewItemClicked

    String[] student_name;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.africa_menu_layout,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = item.getTitle().toString();

        if(title.equals("Statistics"))
            to_Statistics();
//        if(item.getItemId()==R.id.statistics_option)
//            to_Statistics();

       else if(title.equals("Notes"))
            to_Statistics();

       else if(title.equals("SortBy"))
        { }

        else if(title.equals("View"))
        { }

        else if(title.equals("Name"))
        {
            sortingString="Name";//use sort_index as 0 means
            populate_autocomplete();
            populate_listview();
            return true;
        }

        else if(title.equals("Date"))
        {
            sortingString="Registration_Date";//use sort_index as 0 means
            populate_autocomplete();
            populate_listview();
            return true;
        }

        else
        {
            viewString = title;//use sort_index as 0 means
            populate_autocomplete();
            populate_listview();
            return true;
        }




        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_button = (ImageButton) findViewById(R.id.add_btn);
        search_button = (ImageButton) findViewById(R.id.search_btn);
        listview = (ListView) findViewById(R.id.listview);
        listview.setDividerHeight(5);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.auto);


        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populate_autocomplete();

            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Add_Student_Dialog add_student_dialog=new Add_Student_Dialog();
                add_student_dialog.show(getSupportFragmentManager(), "add_Student");

//

            }
        });



        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searched_name=autoCompleteTextView.getText().toString();

                if(!searched_name.equals(""))
                {


                   ArrayList<String> name_arraylist=populate_autocomplete();

                    int j=0;
                    for(int i=0;i<name_arraylist.size();i++)
                    {
                        if( name_arraylist.get(i).equals(searched_name))
                        {
                            j=1;
                            break;
                        }

                    }


                    if(j==1)
                        to_edit(searched_name);
                    else
                        Toast.makeText(MainActivity.this, "No student with this name, Retype.", Toast.LENGTH_LONG).show();

                }
                else
                    Toast.makeText(MainActivity.this, "Enter Name!", Toast.LENGTH_SHORT).show();

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

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

       //to populate listview and autocomplete views
        DatabaseClass databaseobject=new DatabaseClass(this);
        databaseobject.status_changer();

        //to Create a new month data for new year

        String last_checked_year=savedInstanceState.getString("last_checked_year","");
        Calendar current_date=Calendar.getInstance();
        String full_date= DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(current_date.getTime());
        String current_year=full_date.substring(6);


        SQLiteDatabase db=databaseobject.getWritableDatabase();
        Cursor cursor=db.rawQuery("Select Name From Month",null);


        if( cursor.getCount()==0 || !current_year.equals(last_checked_year)  )
        {
            databaseobject.new_year_month_creater(current_year);

        }



    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseClass databaseobject=new DatabaseClass(this);
        databaseobject.status_changer();

        populate_listview();
        populate_autocomplete();






    }

    private void to_Statistics()
    {
        Intent intent=new Intent(this,Statistics_page.class);
        startActivity(intent);
    }

    private void to_edit(String name)
    {

        Intent intent=new Intent(this, Edit_Profile.class);
        intent.putExtra("name",name);
        startActivity(intent);
    }



    private ArrayList<String> populate_autocomplete()
    {
        DatabaseClass databaseobject=new DatabaseClass(this);
        SQLiteDatabase db=databaseobject.getWritableDatabase();
        Cursor cursor1=db.query("Student",new String[] {"Name","Phone"},"Status != ?",new String[]{"Removed"},null,null,sortingString);

        ArrayList<String> autocomplete_name_arrayList=new ArrayList<>();

        while(cursor1.moveToNext())
        {
            autocomplete_name_arrayList.add(cursor1.getString(0));
        }


        ArrayAdapter<String> autocomplete_adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,autocomplete_name_arrayList);
        autoCompleteTextView.setAdapter(autocomplete_adapter);
        return autocomplete_name_arrayList;


    }




    public void populate_listview(){//uses the static cursor to populate the listview , the listview is changed by the change_static_cursor() method

        SQLiteDatabase db=databaseClass.getWritableDatabase();
        String[] columns={"Name","Phone","Registration_Date","Registration_Full","Status"};

        if (viewString.equals("All"))
            static_cursor = db.query("Student",columns,null,null,null,null,sortingString );

        else if(viewString.equals("Active"))
            static_cursor = db.query("Student",columns,"Status != ?",new String[] {"Removed"},null,null,sortingString );

        else
            static_cursor = db.query("Student",columns,"Status = ?",new String[] {viewString},null,null,sortingString );

        Toast.makeText(this, static_cursor.getCount()+" Students", Toast.LENGTH_SHORT).show();

       String[] name=new String[static_cursor.getCount()];
        //this name array is needed because the getView method in the listview ArrayAdapter class uses it to konw the number of count the getView method should be called.
        //if you set it's size 1, then your getView method will be called once and get one list item, if it's size is 2, two list items will be displayed
        // So you should make the size of this name array equal to the number of list items you want to view
        // don't forget to pass a data[] parameter whenever you make a custom listview and make it the last parameter element of the custom listview constructors
        //super() method   E.x super(context, R.layout.single_row_Layout, R.id.textview, data);


        listview_adapter adapter=new listview_adapter(this,static_cursor,name);
        listview.setAdapter(adapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                static_cursor.moveToPosition(i);
                to_edit(static_cursor.getString(0));
                }
        });






    }



    @Override//implementation of the Add_studnet_Dialog.add_Student() method; The method is called in th Add_Student_Dialog class
    public void add_student(String name, String phone, int date, int month, String full, String gender, String belt) {
        DatabaseClass databaseobject=new DatabaseClass(this);

        databaseobject.add_student(name,phone,date,month,full,gender,belt);
        populate_autocomplete();
       populate_listview();

    }


    class ViewHolder
    {
        ImageView listview_imageView;
        TextView listview_textview;
        CheckBox listview_checkbox;

        ViewHolder(View view)
        {
            listview_imageView =(ImageView) view.findViewById(R.id.imageview_listview_layout);
            listview_textview = (TextView)view.findViewById(R.id.listview_layout_textview);
            listview_checkbox = (CheckBox)view.findViewById(R.id.listview_layout_checkbox);
        }


    }

    class listview_adapter extends ArrayAdapter<String>
    {
        Context context;
        Cursor cursor;

        int[] date;

        listview_adapter(Context context, Cursor cursor,String[] data){
            // super(context, R.layout.listview_layout, R.id.listview_layout_textview ,data);



            super(context, R.layout.listview_layout,R.id.listview_layout_textview,data);//default constructor parameters( context, singlel row layout to be inflated, parentview, data);
            this.context=context;
            this.cursor=cursor;


        }



        @SuppressLint("ResourceAsColor")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            View row = convertView;
            ViewHolder holder = null;


            if( row == null)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.listview_layout, parent, false);
                holder = new ViewHolder(row);
                row.setTag(holder);//storing holder object for future recycling

            }

            else
            {
                holder = (ViewHolder) row.getTag();
            }


//            TextView listview_textview = (TextView) row.findViewById(R.id.listview_layout_textview);
//
//            ImageView listview_imageView = (ImageView) row.findViewById(R.id.imageview_listview_layout);
//            CheckBox listview_checkbox = (CheckBox) row.findViewById(R.id.listview_layout_checkbox);
//


            if (cursor.moveToPosition(position))
            {

                String data;
                String status = cursor.getString(4);
                if(status.equals("Removed"))
                    data=cursor.getString(0) + "\n" + cursor.getString(1) + "\n" +"Removed"+"\n\n\n";

                else
                data=cursor.getString(0) + "\n" + cursor.getString(1) + "\n" +cursor.getString(3)+"\n\n\n";

                holder.listview_textview.setText(data);




                if (status.equals("Payed"))
                {
                    holder.listview_checkbox.setChecked(true);
                    holder.listview_checkbox.setBackgroundColor(R.color.Green);
                    holder.listview_checkbox.setClickable(false);


                }

                else
                {

                    holder.listview_checkbox.setChecked(false);
                    holder.listview_checkbox.setBackgroundColor(R.color.Red);
                    holder.listview_checkbox.setClickable(false);
                }


                if (cursor.getString(0).startsWith("Sabom"))
                    holder.listview_imageView.setImageResource(R.drawable.sabom_henok);

                else if (cursor.getString(0).equals("Antenyismu Yirsaw"))
                    holder.listview_imageView.setImageResource(R.drawable.yismu);

                else
                    holder.listview_imageView.setImageResource(R.drawable.background);

            }

            return row;
        }


    }










}


