package com.example.billing.ui.insight;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.billing.MainActivity;
import com.example.billing.R;
import com.example.billing.SettingActivity;
import com.example.billing.addFoodDB.BillContract;
import com.example.billing.addFoodDB.BillDbHelper;
import com.example.billing.utills.Common;
import com.example.billing.utills.PdfDocumentAdapter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    ArrayList<ArrayList<String>> arrayList1 = new ArrayList<>();
    ArrayList<String> arrayList2 = new ArrayList<>();

    private TextView TodayExpance;
    public static final String DATE_FORMAT_1 = "dd";
    public static final String DATE_FORMAT_2 = "MMM";
    public static final String DATE_FORMAT_3 = "yyyy";

    private ListView insightListView;
    private InsightCursorAdapter insightCursorAdapter;
    private int BILL_LOADER = 0;

    private TextView total_revenue,revenue_per_day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insight);

        insightListView = findViewById(R.id.totalListView);
        insightCursorAdapter = new InsightCursorAdapter(getApplicationContext(),null);
        insightListView.setAdapter(insightCursorAdapter);
        InsightActivity.this.getSupportLoaderManager().restartLoader(BILL_LOADER, null, this);

        dbHelper = new BillDbHelper(getApplicationContext());
        dbHelper.getDataByMonth();
        // pieChart();
        CartesianChart();

        total_revenue = findViewById(R.id.totalRevenue);
        String tot_rev = String.valueOf(dbHelper.totalRevenue());
        total_revenue.setText(tot_rev);


        try{
            revenue_per_day = findViewById(R.id.revenueToday);
            String today_rev = String.valueOf(dbHelper.getTodayExpance(getCurrentDate(),getCurrentMonth(),getCurrentYear()));
            revenue_per_day.setText(today_rev);
        } catch (Exception e) {
            e.printStackTrace();
        }

        externalWritePermission();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    private void externalWritePermission() {
        Dexter.withActivity(InsightActivity.this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                });

    }

    private void createPDFFile(String path) {
        if (new File(path).exists()) {
            new File(path).delete();
        }
        try {
            Document document = new Document();
            //save
            PdfWriter.getInstance(document, new FileOutputStream(path));
            //open to write
            document.open();

            //setting
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("AUTHOR");
            document.addCreator("AUTHOR");


            //font setting
            BaseColor colorAssent = new BaseColor(0, 0, 0, 1);
            float fontSize = 13.0f;
            float valueFontSize = 10.0f;

            //custom font
            BaseFont fontname = BaseFont.createFont("res/font/helvetica.ttf", "UTF-8", BaseFont.EMBEDDED);

            //title  font
            Font titlefont = new Font(fontname, 16.0f, Font.NORMAL, BaseColor.BLACK);
            addNewItem(document, "Order Details", Element.ALIGN_CENTER, titlefont);

            //add more
            Font orderNumberFont = new Font(fontname, fontSize, Font.NORMAL, colorAssent);
            addNewItem(document, "order No", Element.ALIGN_LEFT, titlefont);

            Font orderNumberValueFont = new Font(fontname, valueFontSize, Font.NORMAL, BaseColor.BLACK);
            addNewItem(document, "#717171", Element.ALIGN_LEFT, titlefont);

            addLineSeparator(document);

            addNewItem(document, "Order Date", Element.ALIGN_LEFT, titlefont);
            addNewItem(document, "01.01.2020", Element.ALIGN_LEFT, titlefont);

            addLineSeparator(document);

//            addNewItem(document,"Account Name",Element.ALIGN_LEFT,titlefont);
//            addNewItem(document,"IBRAHIM",Element.ALIGN_LEFT,titlefont);

            addLineSeparator(document);

            //addproduct

            addLineSpace(document);
            addNewItem(document, "Product detail", Element.ALIGN_CENTER, titlefont);
            addLineSeparator(document);

//            //addnewItemWith Left and Right product details
//            addNewItemWithLeftAndRight(document, "Cake", "(0.0%)", titlefont, orderNumberValueFont);
//            addNewItemWithLeftAndRight(document, "12.0*1000", "12000.0", titlefont, orderNumberValueFont);
//
//            addLineSeparator(document);
//
//            addNewItemWithLeftAndRight(document, "lava", "(0.0%)", titlefont, orderNumberValueFont);
//            addNewItemWithLeftAndRight(document, "12.0*1000", "12000.0", titlefont, orderNumberValueFont);
//
//            addLineSeparator(document);
//
//            //total
//            addLineSpace(document);
//            addLineSpace(document);


            //===============================================================================================================================================
            //database
            BillDbHelper dbHelper = new BillDbHelper(InsightActivity.this);
            ArrayList<ArrayList<String>> arrayList1 = new ArrayList<>();
            ArrayList<String> arrayList2 = new ArrayList<>();
            arrayList1 = dbHelper.getCart();
            for (int i = 0; i < arrayList1.size(); i++) {
                arrayList2 = arrayList1.get(i);
                addNewItemWithLeftAndRight(document, arrayList2.get(0), arrayList2.get(1) + " * " + arrayList2.get(2), titlefont, orderNumberValueFont);
                addNewItemWithLeftAndRight(document, arrayList2.get(1) + " * " + arrayList2.get(2), String.valueOf(Integer.parseInt(arrayList2.get(1)) * Integer.parseInt(arrayList2.get(2))), titlefont, orderNumberValueFont);
                addLineSeparator(document);
            }
            addLineSpace(document);
            addLineSpace(document);
//            addNewItemWithLeftAndRight(document, "Total", String.valueOf(dbHelper.getTotalSum()), titlefont, orderNumberValueFont);
//            addLineSpace(document);
//            addNewItemWithLeftAndRight(document, "CGST", "9%", titlefont, orderNumberValueFont);
//            addLineSpace(document);
//            addNewItemWithLeftAndRight(document, "SGST", "9%", titlefont, orderNumberValueFont);
            addLineSpace(document);
            addNewItemWithLeftAndRight(document, "Grand Total", String.valueOf(dbHelper.getTotalSum()), titlefont, orderNumberValueFont);
            addLineSpace(document);

//================================================================================================================================================
//            addNewItemWithLeftAndRight(document, "Total", String.valueOf(dbHelper.getTotalSum()), titlefont, orderNumberValueFont);

            document.close();

            Toast.makeText(InsightActivity.this, "success", Toast.LENGTH_SHORT).show();

            printPdf();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printPdf() {
        PrintManager printManager = (PrintManager) InsightActivity.this.getSystemService(Context.PRINT_SERVICE);
        try {
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(InsightActivity.this, Common.getAppPath(InsightActivity.this) + "test_pdf");
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());
        } catch (Exception ex) {
            Log.d("edm", "" + ex.getMessage());
        }

    }

    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight, Font textLeftFont, Font textRightFont) throws DocumentException {
        Chunk chunkTextLeft = new Chunk(textLeft, textLeftFont);
        Chunk chunkTextRight = new Chunk(textRight, textRightFont);
        Paragraph p = new Paragraph(chunkTextLeft);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(chunkTextRight);
        document.add(p);
    }

    private void addLineSeparator(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 0));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);

    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);
    }


    private void CartesianChart() {
        Cartesian cartesian = AnyChart.column();
        AnyChartView anyChartView = findViewById(R.id.any_chart_view1);
        List<DataEntry> data = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        map = getMonth();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
        }
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

    private int getTodayExpance() {
        int sum=0, val = 0;
        arrayList1 = dbHelper.getDataByMonth();
        ArrayList<String> arrayList4 = new ArrayList<>();
        for (int i = 0; i < arrayList1.size(); i++) {
            arrayList4 = arrayList1.get(i);
            if (arrayList4.get(0).equals(getCurrentDate()) && arrayList4.get(1).equals(getCurrentMonth()) && arrayList4.get(2).equals(getCurrentYear())) {
                val = Integer.parseInt(arrayList4.get(3));
                sum = sum +val;
            }
        }

        return val;
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_1);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        Log.e("date", dateFormat.format(today));
        return dateFormat.format(today);
    }

    public static String getCurrentMonth() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_2);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        Log.e("date", dateFormat.format(today));
        return dateFormat.format(today);
    }

    public static String getCurrentYear() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_3);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        Log.e("date", dateFormat.format(today));
        return dateFormat.format(today);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.insight_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.printData:
                createPDFFile(Common.getAppPath(InsightActivity.this) + "test_pdf");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
