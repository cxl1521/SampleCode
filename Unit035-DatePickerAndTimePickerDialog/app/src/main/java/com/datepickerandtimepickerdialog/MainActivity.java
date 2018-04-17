package com.datepickerandtimepickerdialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView mTxtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnTimePickerDlg = (Button) findViewById(R.id.btnTimePickerDlg);
        Button btnDatePickerDlg = (Button) findViewById(R.id.btnDatePickerDlg);
        mTxtResult = (TextView) findViewById(R.id.txtResult);

        btnTimePickerDlg.setOnClickListener(btnTimePickerDlgOnClick);
        btnDatePickerDlg.setOnClickListener(btnDatePickerDlgOnClick);
    }

    private Button.OnClickListener btnDatePickerDlgOnClick = new Button.OnClickListener() {
        public void onClick(View v) {
            mTxtResult.setText("");

            Calendar now = Calendar.getInstance();

            DatePickerDialog datePickerDlg = new DatePickerDialog(MainActivity.this,
                    datePickerDlgOnDateSet,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH));
            datePickerDlg.setTitle("選擇日期");
            datePickerDlg.setMessage("請選擇適合您的日期");
            datePickerDlg.setIcon(android.R.drawable.ic_dialog_info);
            datePickerDlg.setCancelable(false);

            datePickerDlg.show();
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerDlgOnDateSet = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet (DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mTxtResult.setText("您選擇的日期是" +
                    Integer.toString(year) + "年" +
                    Integer.toString(monthOfYear + 1) + "月" +
                    Integer.toString(dayOfMonth) + "日");
        }
    };

    private View.OnClickListener btnTimePickerDlgOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            mTxtResult.setText("");

            Calendar now = Calendar.getInstance();

            TimePickerDialog timePickerDlg = new TimePickerDialog(MainActivity.this,
                    timePickerDlgOnTimeSet,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    true);
            timePickerDlg.setTitle("選擇時間");
            timePickerDlg.setMessage("請選擇適合您的時間");
            timePickerDlg.setIcon(android.R.drawable.ic_dialog_info);
            timePickerDlg.setCancelable(false);

            timePickerDlg.show();
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerDlgOnTimeSet = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet (TimePicker view, int hourOfDay, int minute) {
            mTxtResult.setText("您選擇的時間是" +
                    Integer.toString(hourOfDay) + "時" +
                    Integer.toString(minute) + "分");
        }
    };
}
