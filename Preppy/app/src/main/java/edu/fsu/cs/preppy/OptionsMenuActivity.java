package edu.fsu.cs.preppy;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
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
