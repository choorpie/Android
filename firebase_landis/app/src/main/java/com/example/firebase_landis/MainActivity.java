package com.example.firebase_landis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.Edits;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText humidTextView, CO2TextView, O2TextView, KTextView, NTextView, PTextView, SolidHumidTextView, SolidTempTextView, TempTextView;
    Spinner spnData;
    Button insertBtn, inquireBtn, testBtn, nowBtn;
    LineChart lineChart;
    String year, month, day, hour, minute, second;
    String time;
    int count;
    DataAlltypes data;
    String type = "humid";
    String csTime;
    Integer csMess;
    final ArrayList<Entry> dataVals = new ArrayList<Entry>();
    final ArrayList<Entry> dataValsMonth = new ArrayList<Entry>();
    final ArrayList<String> xAxisValue = new ArrayList<>();
    int count_month;
    // create object of firebase database
    FirebaseDatabase firebaseDatabase;
    // database reference
    DatabaseReference RefToHub_1, RefToYear, RefToMonth, RefToDay;

    //MPAndroidChart
    LineDataSet lineDataSet = new LineDataSet(null, null);
    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
    LineData lineData;


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
        TempTextView = findViewById(R.id.TempTextView);
        CO2TextView = findViewById(R.id.CO2TextView);
        O2TextView = findViewById(R.id.O2TextView);
        KTextView = findViewById(R.id.KTextView);
        NTextView = findViewById(R.id.NTextView);
        PTextView = findViewById(R.id.PTextView);
        SolidHumidTextView = findViewById(R.id.SolidHumidTextView);
        SolidTempTextView = findViewById(R.id.SolidTempTextView);


        insertBtn = findViewById(R.id.btnInsert);
        inquireBtn = findViewById(R.id.btnInquire);
        testBtn = findViewById(R.id.btnTest);
        nowBtn = findViewById(R.id.btnNow);


        lineChart = findViewById(R.id.lineChartView);

        firebaseDatabase = FirebaseDatabase.getInstance();
        RefToHub_1 = firebaseDatabase.getReference("sensorhub_1");
        RefToYear = RefToHub_1.child(String.valueOf(year));
        RefToMonth = RefToYear.child(String.valueOf(month));
        RefToDay = RefToMonth.child(String.valueOf(day));
        buildSpinner();

        insertData();
        inquireDataDay();
        inquireTest();
        inquireNow();
    }

    private void buildSpinner() {
        spnData = findViewById(R.id.spnData);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spnDataList, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnData.setAdapter(adapter);
        spnData.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                retrieveData(type);
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

    private void inquireNow() {
        nowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RefToDay.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot myDataSnapshot: snapshot.getChildren()){
                            Log.e("myDataSnapshot..", String.valueOf(myDataSnapshot));
                            Log.e("myDataSnapshot...getKey()",myDataSnapshot.getKey());
                            Log.e("myDataSnapshot...getValue()", String.valueOf(myDataSnapshot.getValue()));
                            data = myDataSnapshot.getValue(DataAlltypes.class);
                            csTime = myDataSnapshot.getKey();
                            csMess = data.getHumid();
//                            Log.e("csTime[0]", String.valueOf(csTime[0]));
//                            Log.e("csMess[0]", String.valueOf(csMess));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                Log.e("data", String.valueOf(data));
                Log.e("csTime", String.valueOf(csTime));

                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("目前濕度：")
                        .setMessage(csTime + "           " + csMess)
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                TextView textView = (TextView)dialog.findViewById(android.R.id.message);
                textView.setTextSize(40);
            }
        });
    }


    private void insertData() {
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentTime();

                if(humidTextView.getText().toString().matches("")){
                    Toast toast = Toast.makeText(MainActivity.this, "欄位不能為空", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                int humid = Integer.parseInt(humidTextView.getText().toString());
                int K = Integer.parseInt(KTextView.getText().toString());
                int P = Integer.parseInt(PTextView.getText().toString());
                int N = Integer.parseInt(NTextView.getText().toString());
                int Temp = Integer.parseInt(TempTextView.getText().toString());
                int SolidHumid = Integer.parseInt(SolidHumidTextView.getText().toString());
                int SolidTemp = Integer.parseInt(SolidTempTextView.getText().toString());
                int O2 = Integer.parseInt(O2TextView.getText().toString());
                int CO2 = Integer.parseInt(CO2TextView.getText().toString());

                Log.e("humid", String.valueOf(humid));


//                RefToHumid.child()
                DataAlltypes data_insert = new DataAlltypes(humid, CO2, K, N, O2, P, SolidHumid, SolidTemp, Temp);
//                Log.e("time", String.valueOf(data_insert));

                // firebase 在 insert data 時, 需要 getter
                RefToDay.child(time).setValue(data_insert);

                humidTextView.setText("");
                TempTextView.setText("");
                SolidTempTextView.setText("");
                SolidHumidTextView.setText("");
                CO2TextView.setText("");
                O2TextView.setText("");
                NTextView.setText("");
                PTextView.setText("");
                KTextView.setText("");

//                retrieveData();
                hideSoftKeyboard(insertBtn);
            }
        });
    }

    private void retrieveData(String type){
        RefToDay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    dataVals.clear();
                    xAxisValue.clear();
                    count = 0;
                    for(DataSnapshot myDataSnapshot: snapshot.getChildren()){
                        Log.e("myDataSnapshot", String.valueOf(myDataSnapshot));
                        Log.e("myDataSnapshot.getKey()",myDataSnapshot.getKey());
                        Log.e("myDataSnapshot.getValue()", String.valueOf(myDataSnapshot.getValue()));
                        data = myDataSnapshot.getValue(DataAlltypes.class); // wrong
                        xAxisValue.add(myDataSnapshot.getKey());
                        switch (type){
                            case "humid":
                                dataVals.add(new Entry(count, data.getHumid()));
                                break;
                            case "CO2":
                                dataVals.add(new Entry(count, data.getCO2()));
                                break;
                            case "O2":
                                dataVals.add(new Entry(count, data.getO2()));
                                break;
                            case "temp":
                                dataVals.add(new Entry(count, data.getTemp()));
                                break;
                            case "K":
                                dataVals.add(new Entry(count, data.getK()));
                                break;
                            case "N":
                                dataVals.add(new Entry(count, data.getN()));
                                break;
                            case "P":
                                dataVals.add(new Entry(count, data.getP()));
                                break;
                            case "solid_temp":
                                dataVals.add(new Entry(count, data.getSoil_temp()));
                                break;
                            case "solid_humid":
                                dataVals.add(new Entry(count, data.getSoil_humid()));
                                break;
                        }
//                        dataVals.add(new Entry(count, data.getHumid()));
                        count += 1;
                    }
                    showChart(dataVals);
//                    Log.e("dataVals", String.valueOf(dataVals));
//                    Log.e("xAxisValue", String.valueOf(xAxisValue));

//                    if(!dataVals.isEmpty()){
//                        Log.e("dataVals", String.valueOf(dataVals));
//                        Log.e("xAxisValue", String.valueOf(xAxisValue));
//                        showChart(dataVals);
//                    }

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

    private void retrieveData(DatabaseReference Ref){
        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                xAxisValue.clear();
                if(snapshot.hasChildren()){
                    for(DataSnapshot myDataSnapshot: snapshot.getChildren()){
//                        Log.e("myDataSnapshot1", String.valueOf(myDataSnapshot));
//                        Log.e("myDataSnapshot1.getKey()",myDataSnapshot.getKey());
//                        Log.e("myDataSnapshot1.getValue()", String.valueOf(myDataSnapshot.getValue()));
                        data = myDataSnapshot.getValue(DataAlltypes.class); // wrong
                        dataValsMonth.add(new Entry(count_month, data.getHumid()));
                        count_month += 1;
//                        Log.e("Ref.getParent();", String.valueOf(Ref.getKey()));
                        xAxisValue.add(Ref.getKey());

                        Log.e("count_month", String.valueOf(count_month));
                    }
//                    Log.e("dataVals", String.valueOf(dataVals));
                    Log.e("xAxisValue", String.valueOf(xAxisValue));
                }else{
                    lineChart.clear();
                    lineChart.invalidate();
                }
                showChart(dataValsMonth);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void retrieveTest(){
        RefToMonth.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){

                if(snapshot.hasChildren()){
                    count_month = 0;

                    for(DataSnapshot myDataSnapshot: snapshot.getChildren()){
//                        Log.e("test", String.valueOf(myDataSnapshot)); // DataSnapshot { key = 06, value = {18:02:59={humid=1}}
//                        Log.e("test.getKey()",myDataSnapshot.getKey());
//                        Log.e("test.getValue()", String.valueOf(myDataSnapshot.getValue()));

//                        xAxisValue.add(myDataSnapshot.getKey());
//                        Log.e("xAxisValue1", String.valueOf(xAxisValue));

                        String key = myDataSnapshot.getKey();
                        DatabaseReference ref = RefToMonth.child(key);
                        retrieveData(ref);
                        Log.e("xAxisValue1", String.valueOf(xAxisValue));
                    }
//                    Log.e("test_dataValsMonth", String.valueOf(dataValsMonth.size()));
//                    Log.e("test_xAxisValue", String.valueOf(xAxisValue.size()));

//                    Log.e("xAxisValue_size", String.valueOf(xAxisValue.size()));

                    if(!dataValsMonth.isEmpty()) {
                        Log.e("test_dataValsMonth", String.valueOf(dataValsMonth.size()));
                        Log.e("test_xAxisValue", String.valueOf(xAxisValue.size()));
                    }
                    dataValsMonth.clear();
                    xAxisValue.clear();

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
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (value < xAxisValue.size()) {
                    return xAxisValue.get((int)value);
                } else {
                    return "0";
                }
//                return xAxisValue.get((int)value);
            }
        });
        Log.e("show_xAxisValue", String.valueOf(xAxisValue.size()));
        Log.e("show_dataVals", String.valueOf(dataVals.size()));
        lineDataSet.setValues(dataVals);
        lineDataSet.setLabel(type);
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