package com.example.firebase_landis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.icu.text.Edits;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    EditText humidTextView;
    Button insertBtn, inquireBtn, testBtn;
    LineChart lineChart;
    String year, month, day, hour, minute, second;
    String time;
    int count;
    Data data;
    static ArrayList<Entry> dataVals = new ArrayList<Entry>();
    static ArrayList<Entry> dataValsMonth = new ArrayList<Entry>();
    static int count_month;
    // create object of firebase database
    FirebaseDatabase firebaseDatabase;
    // database reference
    DatabaseReference RefToHub_1, RefToYear, RefToMonth, RefToDay, RefToTime, RefToHumid;

    //MPAndroidChart
    LineDataSet lineDataSet = new LineDataSet(null, null);
    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
    LineData lineData;
    ArrayList<String> xAxisValue = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getCurrentTime();

        Log.e("hour", String.valueOf(hour));
        Log.e("minute", String.valueOf(minute));
        Log.e("second", String.valueOf(second));
        Log.e("time", time);

        humidTextView = findViewById(R.id.humidTextView);
        insertBtn = findViewById(R.id.btnInsert);
        inquireBtn = findViewById(R.id.btnInquire);
        testBtn = findViewById(R.id.btnTest);

        lineChart = findViewById(R.id.lineChartView);

        firebaseDatabase = FirebaseDatabase.getInstance();
        RefToHub_1 = firebaseDatabase.getReference("sensorhub_1");
        RefToYear = RefToHub_1.child(String.valueOf(year));
        RefToMonth = RefToYear.child(String.valueOf(month));
        RefToDay = RefToMonth.child(String.valueOf(day));
//        RefToTime = RefToDate.child("17:28:11");
//        RefToHumid = RefToTime.child("humid");

        insertData();
        inquireDataDay();
        inquireTest();
//        retrieveData();
    }

    private void getCurrentTime() {
        Calendar currentTime = new GregorianCalendar(TimeZone.getTimeZone("Asia/Taipei"));
        Log.e("currentTime", String.valueOf(currentTime));
        year = String.format("%04d", currentTime.get(Calendar.YEAR));
        month = String.format("%02d", currentTime.get(Calendar.MONTH) + 1);
        day = String.format("%02d", currentTime.get(Calendar.DAY_OF_MONTH));
        hour = String.format("%02d", currentTime.get(Calendar.HOUR_OF_DAY));
        minute = String.format("%02d", currentTime.get(Calendar.MINUTE));
        second = String.format("%02d", currentTime.get(Calendar.SECOND));
        time = hour + ":" + minute + ":" + second;
        Log.e("time", time);
    }

    private void inquireDataDay() {
        inquireBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveData();
                hideSoftKeyboard(inquireBtn);
            }
        });
    }

    private void inquireTest() {
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveTest();
            }
        });
    }


    private void insertData() {
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentTime();

                int humid = Integer.parseInt(humidTextView.getText().toString());
//                RefToHumid.child()
                Data data = new Data(humid);
                Log.e("time", String.valueOf(data));

                // firebase 在 insert data 時, 需要 getter
                RefToDay.child(time).setValue(data);

                humidTextView.setText("");
                retrieveData();
                hideSoftKeyboard(insertBtn);
            }
        });
    }

    private void retrieveData(){
        RefToDay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ArrayList<Entry> dataVals = new ArrayList<Entry>();

                if(snapshot.hasChildren()){
                    xAxisValue.clear();
                    count = 0;
//                    count_month = 0;
                    dataVals.clear();
//                    dataValsMonth.clear();
                    for(DataSnapshot myDataSnapshot: snapshot.getChildren()){
                        Log.e("myDataSnapshot", String.valueOf(myDataSnapshot));
                        Log.e("myDataSnapshot.getKey()",myDataSnapshot.getKey());
                        Log.e("myDataSnapshot.getValue()", String.valueOf(myDataSnapshot.getValue()));
//                        Entry keyValue = new Entry(1, myDataSnapshot.getValue());
                        data = myDataSnapshot.getValue(Data.class); // wrong
                        xAxisValue.add(myDataSnapshot.getKey());
                        dataVals.add(new Entry(count, data.getHumid()));
                        dataValsMonth.add(new Entry(count_month, data.getHumid()));
                        count += 1;
                        count_month += 1;
                        Log.e("count_month", String.valueOf(count_month));
//                        Log.e("dataValsMonth", String.valueOf(dataValsMonth));
                    }
                    Log.e("dataVals", String.valueOf(dataVals));
                    Log.e("xAxisValue", String.valueOf(xAxisValue));
//                    showChart(dataVals);
////
                }else{
                    lineChart.clear();
                    lineChart.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void retrieveTest(){
        RefToMonth.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ArrayList<Entry> dataValsMonth = new ArrayList<Entry>();
//                dataValsMonth.clear();

                if(snapshot.hasChildren()){
//                    dataValsMonth.clear();
                    xAxisValue.clear();
                    count_month = 0;
                    for(DataSnapshot myDataSnapshot: snapshot.getChildren()){
                        Log.e("test", String.valueOf(myDataSnapshot)); // DataSnapshot { key = 06, value = {18:02:59={humid=1}}
                        Log.e("test.getKey()",myDataSnapshot.getKey());
                        Log.e("test.getValue()", String.valueOf(myDataSnapshot.getValue()));
//                        Entry keyValue = new Entry(1, myDataSnapshot.getValue());
//                        String value = myDataSnapshot.getKey();
                        MonthData MonthDatadata = myDataSnapshot.getValue(MonthData.class); // wrong // 沒有讀到
                        // myDataSnapshot.getValue() 是 HashMap
//                        Log.e("timeKey", String.valueOf(timeKey));
                        retrieveData();
//                        Log.e("test_dataValsMonth", String.valueOf(dataValsMonth));
                        Log.e("test_dataVals", String.valueOf(dataVals));
//                        dataValsMonth.addAll(dataVals);
                        Log.e("data", String.valueOf(data));
                        Log.e("test_data", String.valueOf(MonthDatadata.getKeyvalue()));
//                        break;

//                        xAxisValue.add(myDataSnapshot.getValue().toString());
//                        dataVals.add(new Entry(count, data.getHumid()));
//                        count += 1;

                    }

                    Log.e("test_dataValsMonth", String.valueOf(dataValsMonth));
                    Log.e("test_xAxisValue", String.valueOf(xAxisValue));

                    if(!dataValsMonth.isEmpty()){
                        showChart(dataValsMonth);
                    }
                    dataValsMonth.clear();

////
                }else{
                    lineChart.clear();
                    lineChart.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showChart(ArrayList<Entry> dataVals) {
//        XAxis xAxis = lineChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return xAxisValue.get((int)value);
//            }
//        });
        lineDataSet.setValues(dataVals);
        lineDataSet.setLabel("Humid");
        iLineDataSets.clear();
        iLineDataSets.add(lineDataSet);
        Log.e("iLineDataSets", String.valueOf(iLineDataSets));
        lineData = new LineData(iLineDataSets);
        lineChart.clear();
        lineChart.setData(lineData);

        lineChart.invalidate();
    }

    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}