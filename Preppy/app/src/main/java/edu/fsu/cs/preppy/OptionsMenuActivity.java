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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu,menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String date = "";

        switch(item.getItemId()){

            case R.id.mon:
                date = "monday";
                return true;
            case R.id.tue:
                date = "tuesday";

                return true;
            case R.id.wed:
                date = "wednesday";

                return true;
            case R.id.thur:
                date = "thursday";

                return true;
            case R.id.fri:
                date = "friday";

                return true;
            case R.id.sat:
                date = "saturday";

                return true;
            case R.id.sun:
                date = "sunday";

                return true;
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
                getContentResolver().call(PreppyProvider.CONTENT_URI, "load", "MealData.csv", null);
                return true;
            case R.id.export_meals:
                getContentResolver().call(PreppyProvider.CONTENT_URI, "dump", "MealData.csv", null);
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
        finalminute = minute;

        if(finalhour > 12)
            finalhour = finalhour - 12;

      //  Calendar cal = Calendar.getInstance();
      //  cal.setTimeInMillis(System.currentTimeMillis());




       // cal.set(cal.DAY_OF_WEEK,cal.MONDAY);
        //cal.set(cal.SECOND, cal.SECOND + 1);

        //Intent myintent = new Intent(getBaseContext(), Alarm.class);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0, myintent,0);
       // AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
      //  am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

       // am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7,pendingIntent); //repeats every week
        Toast.makeText(this,"Time set for: "+ finalyear + " " + finalmonth + " " + finalday + " " + finalhour + " " + finalminute,Toast.LENGTH_LONG).show();

    }

}
