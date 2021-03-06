package com.raj.scorpion.calendar;


import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



import com.molo17.customizablecalendar.library.components.CustomizableCalendar;
import com.molo17.customizablecalendar.library.interactors.AUCalendar;
import com.molo17.customizablecalendar.library.model.Calendar;

import org.joda.time.DateTime;

import java.util.regex.Pattern;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

import static android.support.v4.app.DialogFragment.STYLE_NO_FRAME;

public class MainActivity extends AppCompatActivity {

    CompositeDisposable subscriptions;
    CalendarViewInteractor calendarViewInteractor;
    TextView toDate, fromDate;
    Button click;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        click = findViewById(R.id.button);
        toDate = findViewById(R.id.toDate);
        fromDate = findViewById(R.id.fromDate);




        subscriptions = new CompositeDisposable();
        calendarViewInteractor = new CalendarViewInteractor(this, fromDate, toDate);



        click.setOnClickListener((View v)->{
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setContentView(R.layout.calendar_fragment);

            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            CustomizableCalendar customizableCalendar = dialog.findViewById(R.id.customizable_calendar);
            ImageView cross = dialog.findViewById(R.id.cross);


            DateTime today = new DateTime();

            DateTime firstMonth = today.withDayOfMonth(1);
            DateTime lastMonth = today.plusMonths(3).withDayOfMonth(1);

            final Calendar calendar = new Calendar(firstMonth, lastMonth);
            calendar.setFirstSelectedDay(today.plusDays(5));
            calendar.setLastSelectedDay(today.plusDays(7));
            calendar.setMultipleSelection(true);

            AUCalendar auCalendar = AUCalendar.getInstance(calendar);
            calendarViewInteractor.updateCalendar(calendar);
            subscriptions.add(
                    auCalendar.observeChangesOnCalendar()
                            .subscribe(changeSet -> calendarViewInteractor.updateCalendar(calendar))
            );

            customizableCalendar.injectViewInteractor(calendarViewInteractor);


           cross.setOnClickListener((View v1)->{

               dialog.dismiss();



            });

        });
    }
}