package com.example.billing.ui.insight;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.enums.VAlign;
import com.anychart.graphics.vector.Anchor;
import com.example.billing.R;
import com.example.billing.addFoodDB.BillContract;
import com.example.billing.addFoodDB.BillDbHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class InsightActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    BillDbHelper dbHelper;

    ArrayList<ArrayList<String>> arrayList1 = new ArrayList<>();

    private ListView insightListView;
    private InsightCursorAdapter insightCursorAdapter;
    private int BILL_LOADER = 0;

    private TextView total_revenue, revenue_per_day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insight);

        insightListView = findViewById(R.id.totalListView);
        insightCursorAdapter = new InsightCursorAdapter(getApplicationContext(), null);
        insightListView.setAdapter(insightCursorAdapter);
        InsightActivity.this.getSupportLoaderManager().restartLoader(BILL_LOADER, null, this);

        dbHelper = new BillDbHelper(getApplicationContext());
        dbHelper.getDataByMonth();
        // pieChart();
        CartesianChart();

        Calendar calendar;
        SimpleDateFormat dateFormat;
        String date;
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        Log.e("soduficjnalkdjscma;sdkl", date);

        total_revenue = findViewById(R.id.totalRevenue);
        String tot_rev = String.valueOf(dbHelper.totalRevenue());
        total_revenue.setText(tot_rev);


        try {
            revenue_per_day = findViewById(R.id.revenueToday);
            String today_rev = String.valueOf(dbHelper.getTodayExpance(getCurrentDate(), getCurrentMonth(), getCurrentYear()));
            revenue_per_day.setText(today_rev);
        } catch (Exception e) {
            e.printStackTrace();
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }


    private void CartesianChart() {
        Cartesian cartesian = AnyChart.column();
        AnyChartView anyChartView = findViewById(R.id.any_chart_view1);
        List<DataEntry> data = new ArrayList<>();


        data.add(new ValueDataEntry("JAN", dbHelper.getMonthlyExpance("01", getCurrentYear())));
        data.add(new ValueDataEntry("FEB", dbHelper.getMonthlyExpance("02", getCurrentYear())));
        data.add(new ValueDataEntry("MAR", dbHelper.getMonthlyExpance("03", getCurrentYear())));
        data.add(new ValueDataEntry("APR", dbHelper.getMonthlyExpance("04", getCurrentYear())));
        data.add(new ValueDataEntry("MAY", dbHelper.getMonthlyExpance("05", getCurrentYear())));
        data.add(new ValueDataEntry("JUN", dbHelper.getMonthlyExpance("06", getCurrentYear())));
        data.add(new ValueDataEntry("JUL", dbHelper.getMonthlyExpance("07", getCurrentYear())));
        data.add(new ValueDataEntry("AUG", dbHelper.getMonthlyExpance("08", getCurrentYear())));
        data.add(new ValueDataEntry("SEP", dbHelper.getMonthlyExpance("09", getCurrentYear())));
        data.add(new ValueDataEntry("OCT", dbHelper.getMonthlyExpance("10", getCurrentYear())));
        data.add(new ValueDataEntry("NOV", dbHelper.getMonthlyExpance("11", getCurrentYear())));
        data.add(new ValueDataEntry("Dec", dbHelper.getMonthlyExpance("12", getCurrentYear())));


        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(String.valueOf(Anchor.CENTER_BOTTOM))
                .offsetX(0d)
                .offsetY(2d)
                .format("${%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Revenue by Month");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Month");
        cartesian.yAxis(0).title("Revenue");

        anyChartView.setChart(cartesian);
    }
    
//    private void pieChart() {
//        Pie pie = AnyChart.pie();
//        List<DataEntry> data = new ArrayList<>();
//        Map<String, Integer> map = new HashMap<>();
//        map = getMonth();
//
//        for (Map.Entry<String, Integer> entry : map.entrySet()) {
//            data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
//        }
//        pie.data(data);
//        // AnyChartView anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);
//        // anyChartView.setChart(pie);
//    }

    private Map<String, Integer> getMonth() {
        int oldvalue = 0;
        arrayList1 = dbHelper.getDataByMonth();
        ArrayList<String> arrayList4 = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < arrayList1.size(); i++) {
            arrayList4 = arrayList1.get(i);
            String tempMonth = arrayList4.get(1);
            int newvalue = Integer.parseInt(arrayList4.get(3));
            if (map.containsKey(tempMonth)) {
                oldvalue = Integer.valueOf(arrayList4.get(3));
                newvalue = newvalue + oldvalue;
                map.put(arrayList4.get(1), newvalue);
            } else {
                map.put(arrayList4.get(1), Integer.valueOf(arrayList4.get(3)));
            }
        }
        return map;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static String getCurrentDate() {
        Calendar calendar;
        SimpleDateFormat dateFormat;
        String date;
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd");
        date = dateFormat.format(calendar.getTime());
        return date;
    }

    public static String getCurrentMonth() {
        Calendar calendar;
        SimpleDateFormat dateFormat;
        String date;
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM");
        date = dateFormat.format(calendar.getTime());
        return date;
    }

    public static String getCurrentYear() {
        Calendar calendar;
        SimpleDateFormat dateFormat;
        String date;
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy");
        date = dateFormat.format(calendar.getTime());
        return date;
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String projection[] = {
                BillContract.addFood._ID_DATA,
                BillContract.addFood.COLUMN_DATA_ORDER_DATE,
                BillContract.addFood.COLUMN_DATA_ORDER_MONTH,
                BillContract.addFood.COLUMN_DATA_ORDER_YEAR,
                BillContract.addFood.COLUMN_DATA_ORDER_VALUE
        };
        return new CursorLoader(this,
                BillContract.addFood.CONTENT_URI_DATA,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        insightCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        insightCursorAdapter.swapCursor(null);
    }
}
