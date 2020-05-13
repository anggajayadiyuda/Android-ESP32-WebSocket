package com.example.androidesp32;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;


public class MainActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    LineChart mChart;
    LineChart mChart1;
    LineChart mChart2;
    String pesanutuh="";
    double nilai, nilai1, nilai2 = 0;
    public String pesan = "";
    public String pesanascii = "";
    private Button start;
    private Button stop;
    private EditText editText;
    private TextView output;
    private InputStream inputStream;
    private OkHttpClient client;
    private Object WebSocket;

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            webSocket.send("1");
//            webSocket.send("What's up ?");
//            webSocket.send(ByteString.decodeHex("deadbeef"));
//            webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
        }
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output(text);
        }
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Receiving bytes : " + bytes.hex());
        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + " / " + reason);
        }
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output("Error : " + t.getMessage());
        }
    }
    private class EchoWebSocketListenerStop extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        @Override
        public void onOpen(WebSocket webSocket, Response responseStop) {
            webSocket.send("0");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button) findViewById(R.id.start);
        output = (TextView) findViewById(R.id.output);
        stop = (Button) findViewById(R.id.stop);
//        output.setMovementMethod(new ScrollingMovementMethod());
//        editText = (EditText) findViewById(R.id.editText);
        client = new OkHttpClient();

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });
    chartmChart();
    chartmChart1();
//    chartmChart2();
//    chartmChart2();

//        long now = 0;
//        now = System.currentTimeMillis();
//                if(now >= 600000){
//                    output.setText(R.string.Default);
//                    now = 0;
//                    mChart.clearvalue();
//                    mChart1.clearvalue();
//                    mChart2.clearvalue();
//                }
    }

    public void chartmChart(){
        mChart = findViewById(R.id.line_chart);
        mChart.setOnChartValueSelectedListener(this);
        // enable description text
        mChart.getDescription().setEnabled(true);
        // enable touch gestures
        mChart.setTouchEnabled(true);
        // enable scaling and dragging
        Description description = new Description();
        description.setText("Tekanan Sensor di Masker");
        description.setTextColor(Color.WHITE);
        description.setTextSize(8);
        mChart.setDescription(description);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        // set an alternative background color
//        mChart.setBackgroundColor(Color.BLACK);
        mChart.setDrawBorders(true);
        mChart.setBorderColor(Color.WHITE);
        mChart.setBorderWidth(1);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        // add empty data
        mChart.setData(data);
        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
//        l.setTypeface(tfLight);
        l.setTextColor(Color.WHITE);
        XAxis xl = mChart.getXAxis();
//        xl.setTypeface(tfLight);
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);
        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(Color.WHITE);
//        leftAxis.setAxisMaximum(100);
//        leftAxis.setAxisMinimum(-50);
        leftAxis.setDrawGridLines(true);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }
    public void chartmChart1(){
        mChart1 = findViewById(R.id.line_chart1);
        mChart1.setOnChartValueSelectedListener(this);
        mChart1.getDescription().setEnabled(false);
        mChart1.setTouchEnabled(true);
        Description description = new Description();
        description.setText("Aliran Udara");
        description.setTextColor(Color.WHITE);
        description.setTextSize(8);
        mChart1.setDescription(description);
        mChart1.setDragEnabled(true);
        mChart1.setScaleEnabled(true);
        mChart1.setDrawGridBackground(false);
        mChart1.setPinchZoom(false);
//        mChart1.setBackgroundColor(Color.BLACK);
        mChart1.setDrawBorders(true);
        mChart1.setBorderColor(Color.WHITE);
        mChart1.setBorderWidth(1);
        LineData data1 = new LineData();
        data1.setValueTextColor(Color.WHITE);
        mChart1.setData(data1);
        Legend l = mChart1.getLegend();
        l.setForm(Legend.LegendForm.LINE);
//        l.setTypeface(tfLight);
        l.setTextColor(Color.WHITE);
        XAxis xl = mChart1.getXAxis();
//        xl.setTypeface(tfLight);
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);
        YAxis leftAxis = mChart1.getAxisLeft();
//        leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(Color.WHITE);
//        leftAxis.setAxisMaximum(100f);
//        leftAxis.setAxisMinimum(-50f);
        leftAxis.setDrawGridLines(true);
        YAxis rightAxis = mChart1.getAxisRight();
        rightAxis.setEnabled(false);
    }
//    public void chartmChart2(){
//        mChart2 = findViewById(R.id.line_chart2);
//        mChart2.setOnChartValueSelectedListener(this);
//        mChart2.getDescription().setEnabled(false);
//        mChart2.setTouchEnabled(true);
//        Description description = new Description();
//        description.setText("Data Tekanan Sensor 3");
//        description.setTextColor(Color.WHITE);
//        description.setTextSize(8);
//        mChart2.setDescription(description);
//        mChart2.setDragEnabled(true);
//        mChart2.setScaleEnabled(true);
//        mChart2.setDrawGridBackground(false);
//        mChart2.setPinchZoom(false);
////        mChart2.setBackgroundColor(Color.BLACK);
//        mChart2.setDrawBorders(true);
//        mChart2.setBorderColor(Color.WHITE);
//        mChart2.setBorderWidth(1);
//        LineData data2 = new LineData();
//        data2.setValueTextColor(Color.WHITE);
//        mChart2.setData(data2);
//        Legend l = mChart2.getLegend();
//
//        l.setForm(Legend.LegendForm.LINE);
////        l.setTypeface(tfLight);
//        l.setTextColor(Color.WHITE);
//        XAxis xl = mChart2.getXAxis();
////        xl.setAxisMaximum(200);
////        xl.setAxisMinimum(0);
////        xl.setAxisMaxValue(200);
////        xl.setTypeface(tfLight);
//        xl.setTextColor(Color.WHITE);
//        xl.setDrawGridLines(false);
//        xl.setAvoidFirstLastClipping(true);
//        xl.setEnabled(true);
//        YAxis leftAxis = mChart2.getAxisLeft();
////        leftAxis.setTypeface(tfLight);
//        leftAxis.setTextColor(Color.WHITE);
////        leftAxis.setAxisMaximum(100f);
////        leftAxis.setAxisMinimum(-50f);
//        leftAxis.setDrawGridLines(true);
//        YAxis rightAxis = mChart2.getAxisRight();
//        rightAxis.setEnabled(false);
//    }
    public LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Tekanan Masker");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(ColorTemplate.getHoloBlue());
        set.setLineWidth(1f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        return set;
    }
    public LineDataSet createSet1() {
        LineDataSet set1 = new LineDataSet(null, "Aliran Udara");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(Color.rgb(0, 255, 0));
        set1.setCircleColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(1f);
        set1.setCircleRadius(4f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setValueTextColor(Color.WHITE);
        set1.setValueTextSize(9f);
        set1.setDrawValues(false);
        set1.setDrawCircles(false);
        return set1;
    }
    public LineDataSet createSet2() {
        LineDataSet set2 = new LineDataSet(null, "Tekanan 3");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setColor(Color.MAGENTA);
        set2.setCircleColor(ColorTemplate.getHoloBlue());
        set2.setLineWidth(1f);
        set2.setCircleRadius(4f);
        set2.setFillAlpha(65);
        set2.setFillColor(ColorTemplate.getHoloBlue());
        set2.setHighLightColor(Color.rgb(244, 117, 117));
        set2.setValueTextColor(Color.WHITE);
        set2.setValueTextSize(9f);
        set2.setDrawValues(false);
        set2.setDrawCircles(false);
        return set2;
    }
    public void addEntry(float value) {
        LineData data = mChart.getData();
        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well
            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            data.addEntry(new Entry(set.getEntryCount(), value), 0);
//            data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 76) + 30f), 0);
//            System.out.println(set.getEntryCount());
            data.notifyDataChanged();
            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();
            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(400);
            // move to the latest entry
            mChart.moveViewToX(data.getEntryCount());
            mChart.invalidate();
            // this automatically refreshes the chart (calls invalidate())
            // chart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }
    public void addEntry1(float value) {
        LineData data1 = mChart1.getData();
        if (data1 != null) {
            ILineDataSet set = data1.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well
            if (set == null) {
                set = createSet1();
                data1.addDataSet(set);
            }
            data1.addEntry(new Entry(set.getEntryCount(), (float) (value)), 0);
//            data1.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 45) + 21f), 0);
//            System.out.println(set.getEntryCount());
            data1.notifyDataChanged();
            // let the chart know it's data has changed
            mChart1.notifyDataSetChanged();
            // limit the number of visible entries
            mChart1.setVisibleXRangeMaximum(400);
            // move to the latest entry
            mChart1.moveViewToX(data1.getEntryCount());
            mChart1.invalidate();
            // this automatically refreshes the chart (calls invalidate())
            // chart.moveViewTo(data1.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }
    public void addEntry2(float value) {
        LineData data2 = mChart2.getData();
        if (data2 != null) {
            ILineDataSet set = data2.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well
            if (set == null) {
                set = createSet2();
                data2.addDataSet(set);
            }
            data2.addEntry(new Entry(set.getEntryCount(), (float) (value)), 0);
//            data2.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 70) + 30f), 0);
//            System.out.println(set.getEntryCount());
            data2.notifyDataChanged();
            // let the chart know it's data has changed
            mChart2.notifyDataSetChanged();
            // limit the number of visible entries
            mChart2.setVisibleXRangeMaximum(400);
            // move to the latest entry
            mChart2.moveViewToX(data2.getEntryCount());

            mChart2.invalidate();
            // this automatically refreshes the chart (calls invalidate())
//            mChart2.moveViewTo(data2.getXValCount()-7, 55f,);
            // AxisDependency.LEFT);
        }
    }

    public void start() {
        Request request = new Request.Builder().url("ws://192.168.137.171:80/test").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    public void stop(){
        Request requestStop = new Request.Builder().url("ws://192.168.137.171:80/test").build();
        EchoWebSocketListenerStop listenerStop = new EchoWebSocketListenerStop();
        WebSocket ws = client.newWebSocket(requestStop, listenerStop);
        client.dispatcher().executorService().shutdown();
    }

    public void output(String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                output.setText(txt);
//                String[] isisemua = output.getText().toString().split("\\}");
//                String isi = isisemua[isisemua.length - 1] + "}";
                try {
                    String isi = output.getText().toString();
                    JSONObject OBJ = new JSONObject(isi);
                        nilai = OBJ.optDouble("maskP", 0.0);
                        nilai1 = OBJ.optDouble("flow", 0.0);
                        nilai2 = OBJ.optDouble("tekanan3", 0.0);
                        addEntry((float) nilai);
                        addEntry1((float) nilai1);
                        addEntry2((float) nilai2);
//                        Thread.sleep(500);

                } catch (JSONException e) {
                    e.printStackTrace();
//                    ((EditText) MainActivity.this.findViewById(R.id.editText)).setText("\n---Pesan---\n" + e.getMessage() + "\n---Errornya---\n" + e.toString());
                }
            }
        });

    }

}