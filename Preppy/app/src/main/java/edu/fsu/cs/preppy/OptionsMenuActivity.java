package edu.fsu.cs.preppy;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class OptionsMenuActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    int day,month,year,hour,minute;
    int finalday,finalmonth,finalyear,finalhour,finalminute;
    Calendar cal = Calendar.getInstance();



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu,menu);




        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String date = "";
        Dialog dialog = null;

        TimePickerDialog timePickerDialog;
        cal.setTimeInMillis(System.currentTimeMillis());

        switch(item.getItemId()){

            case R.id.suggestion:
                Toast.makeText(this,"Suggestions selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.calendar:
                Calendar calendarEvent = Calendar.getInstance();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("vnd.android.cursor.item/event");
                startActivity(intent);
                return true;

            case R.id.mon:
                cal.set(cal.DAY_OF_WEEK,cal.MONDAY);


                hour = cal.get(Calendar.HOUR_OF_DAY);
                minute = cal.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(OptionsMenuActivity.this, OptionsMenuActivity.this,
                        hour,minute, DateFormat.is24HourFormat(OptionsMenuActivity.this));

                timePickerDialog.show();



                return true;
            case R.id.tue:
                cal.set(cal.DAY_OF_WEEK,cal.TUESDAY);
                hour = cal.get(Calendar.HOUR_OF_DAY);
                minute = cal.get(Calendar.MINUTE);


                timePickerDialog = new TimePickerDialog(OptionsMenuActivity.this, OptionsMenuActivity.this,
                        hour,minute, DateFormat.is24HourFormat(OptionsMenuActivity.this));

                timePickerDialog.show();

                return true;
            case R.id.wed:
                cal.set(cal.DAY_OF_WEEK,cal.WEDNESDAY);
                hour = cal.get(Calendar.HOUR_OF_DAY);
                minute = cal.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(OptionsMenuActivity.this, OptionsMenuActivity.this,
                        hour,minute, DateFormat.is24HourFormat(OptionsMenuActivity.this));

                timePickerDialog.show();

                return true;
            case R.id.thur:
                cal.set(cal.DAY_OF_WEEK,cal.THURSDAY);
                hour = cal.get(Calendar.HOUR_OF_DAY);
                minute = cal.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(OptionsMenuActivity.this, OptionsMenuActivity.this,
                        hour,minute, DateFormat.is24HourFormat(OptionsMenuActivity.this));

                timePickerDialog.show();

                return true;
            case R.id.fri:
                cal.set(cal.DAY_OF_WEEK,cal.FRIDAY);
                hour = cal.get(Calendar.HOUR_OF_DAY);
                minute = cal.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(OptionsMenuActivity.this, OptionsMenuActivity.this,
                        hour,minute, DateFormat.is24HourFormat(OptionsMenuActivity.this));

                timePickerDialog.show();

                return true;
            case R.id.sat:
                cal.set(cal.DAY_OF_WEEK,cal.SATURDAY);
                hour = cal.get(Calendar.HOUR_OF_DAY);
                minute = cal.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(OptionsMenuActivity.this, OptionsMenuActivity.this,
                        hour,minute, DateFormat.is24HourFormat(OptionsMenuActivity.this));

                timePickerDialog.show();

                return true;
            case R.id.sun:
                cal.set(cal.DAY_OF_WEEK,cal.SUNDAY);
                hour = cal.get(Calendar.HOUR_OF_DAY);
                minute = cal.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(OptionsMenuActivity.this, OptionsMenuActivity.this,
                        hour,minute, DateFormat.is24HourFormat(OptionsMenuActivity.this));

                timePickerDialog.show();

                return true;
<<<<<<< HEAD
            case R.id.suggestion:
                Toast.makeText(this,"Suggestions selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.calendar:
                Calendar calendarEvent = Calendar.getInstance();
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("beginTime", calendarEvent.getTimeInMillis());
                intent.putExtra("endTime", calendarEvent.getTimeInMillis() + 60 * 60 * 1000);
                intent.putExtra("title", "My Food");
                intent.putExtra("allDay", true);
                intent.putExtra("rule", "FREQ=YEARLY");
                intent.putExtra("save", true);
                startActivity(intent);
                return true;
            case R.id.import_meals:
                dialog = new Dialog(this);
                final Dialog importDialog = dialog;
                dialog.setContentView(R.layout.menu_option_import_export_dialog);
                dialog.setTitle("Enter Filename");
                dialog.setCancelable(true);
                dialog.findViewById(R.id.dei_action).setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {

                        EditText et_filename = importDialog.findViewById(R.id.dei_filename);
                        String filename = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + et_filename.getText().toString().trim();

                        Bundle b = getContentResolver().call(PreppyProvider.CONTENT_URI, "load", filename, null);

                        Toast.makeText(getApplicationContext(), b.getBoolean("succeeded") ? "Meal information stored in \"" + filename + "\" has" + " been " + "saved." : "There was an issue reading the meal " + "information" + " from the" + " " + "specified file.", Toast.LENGTH_LONG).show();
                    }
                });
                dialog.show();
                return true;
            case R.id.export_meals:
                dialog = new Dialog(this);
                final Dialog exportDialog = dialog;
                dialog.setContentView(R.layout.menu_option_import_export_dialog);
                dialog.setTitle("Enter Filename");
                dialog.setCancelable(true);
                dialog.findViewById(R.id.dei_action).setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {

                        EditText et_filename = exportDialog.findViewById(R.id.dei_filename);
                        String filename = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + et_filename.getText().toString().trim();

                        Bundle b = getContentResolver().call(PreppyProvider.CONTENT_URI, "dump", filename, null);
                        Toast.makeText(getApplicationContext(), b.getBoolean("succeeded") ? "Your stored meals have been " + "exported to " + "\"" + filename + "\"" : "There was an issue storing your meals in the specified file.", Toast.LENGTH_LONG).show();
                    }
                });

                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        finalyear = year;
        finalmonth = month + 1;
        finalday = dayOfMonth;

        Calendar c = Calendar.getInstance();

        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(OptionsMenuActivity.this, OptionsMenuActivity.this,
                hour,minute, DateFormat.is24HourFormat(OptionsMenuActivity.this));

        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        finalhour = hourOfDay;
        Intent myintent = new Intent(getBaseContext(), Alarm.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0, myintent,0);

        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);

        finalminute = minute;
        cal.set(cal.HOUR_OF_DAY,finalhour);
        cal.set(cal.MINUTE,finalminute);
        cal.set(cal.SECOND, cal.SECOND + 3);

        Toast.makeText(this,"Time set for: " + finalhour + " " + finalminute,Toast.LENGTH_LONG).show();

        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

        am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),604800000,pendingIntent);

    }


    public void dothis(){

    }

}
