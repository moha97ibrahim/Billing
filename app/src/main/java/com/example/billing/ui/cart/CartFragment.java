package com.example.billing.ui.cart;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.billing.R;
import com.example.billing.addFoodDB.BillContract;
import com.example.billing.addFoodDB.BillDbHelper;
import com.example.billing.printingActivity;
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
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private CartCursorAdapter cartCursorAdapter;
    private BillDbHelper dbHelper;
    private TextView gTotal, sGST, cGST, cTotal;
    private float grandToatal;
    private CardView cardViewtot, submitCard , clearCard;
    private  ProgressDialog progressDoalog;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CartViewModel cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_cart, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        cTotal = root.findViewById(R.id.cartTotal);
//        cGST = root.findViewById(R.id.cgstView);
//        sGST = root.findViewById(R.id.sgstView);
//        gTotal = root.findViewById(R.id.grandTotalView);
//        cardViewtot = root.findViewById(R.id.cardViewTotal);
        cartViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        //listview
        ListView cartListView = root.findViewById(R.id.cartFoodListView);
        cartListView.setEmptyView(textView);
        cartCursorAdapter = new CartCursorAdapter(getActivity(), null);
        cartListView.setAdapter(cartCursorAdapter);
        int BILL_LOADER = 0;
        getActivity().getSupportLoaderManager().restartLoader(BILL_LOADER, null, this);


        submitCard = root.findViewById(R.id.submitCardView);


        submitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), printingActivity.class);
                i.putExtra("total",grandToatal);
                startActivity(i);
                progressDoalog = new ProgressDialog(getActivity());
                progressDoalog.setMessage("Loading.....");
                progressDoalog.show();
//                  createPDFFile(Common.getAppPath(getActivity()) + "test_pdf");
            }
        });



        Dexter.withActivity(getActivity())
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


        return root;
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
            document.setPageSize(PageSize.A9);
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
            BillDbHelper dbHelper = new BillDbHelper(getContext());
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
            addNewItemWithLeftAndRight(document, "Total", String.valueOf(dbHelper.getTotalSum()), titlefont, orderNumberValueFont);
            addLineSpace(document);
            addNewItemWithLeftAndRight(document, "CGST", "9%", titlefont, orderNumberValueFont);
            addLineSpace(document);
            addNewItemWithLeftAndRight(document, "SGST", "9%", titlefont, orderNumberValueFont);
            addLineSpace(document);
            addNewItemWithLeftAndRight(document, "Grand Total", String.valueOf(dbHelper.getTotalSum()), titlefont, orderNumberValueFont);
            addLineSpace(document);

//================================================================================================================================================
//            addNewItemWithLeftAndRight(document, "Total", String.valueOf(dbHelper.getTotalSum()), titlefont, orderNumberValueFont);

            document.close();

            Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();

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
        PrintManager printManager = (PrintManager) getContext().getSystemService(Context.PRINT_SERVICE);
        try {
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(getActivity(), Common.getAppPath(getActivity()) + "test_pdf");
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
        lineSeparator.setLineColor(new BaseColor(1, 1, 0, 0));
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

    @Override
    public void onResume() {
        updateValue();
        super.onResume();

    }

    private void refresh(int i) {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateValue();
            }
        };

        handler.postDelayed(runnable, i);
    }

    private void updateValue() {
        DecimalFormat precision = new DecimalFormat("0.00");

        try {
            Log.e("run", "run");
            dbHelper = new BillDbHelper(getContext());
            float cartTotal = dbHelper.getTotalSum();
            float CGST = (float) (cartTotal * (9.0 / 100));
            float SGST = (float) ((9.0 / 100) * cartTotal);
            grandToatal = cartTotal;
            cartTotal = cartTotal - CGST - SGST;
            cTotal.setText(precision.format(cartTotal));
            cGST.setText(precision.format(CGST));
            sGST.setText(precision.format(SGST));
            gTotal.setText(precision.format(grandToatal));
            refresh(1);
        } catch (NullPointerException ignored) {
        }

    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String pro[] = {
                BillContract.addFood._ID_CART,
                BillContract.addFood.COLUMN_FOOD_NAME_CART,
                BillContract.addFood.COLUMN_FOOD_QUANTITY_CART,
                BillContract.addFood.COLUMN_FOOD_PRICE_CART
        };

        return new CursorLoader(getActivity(),
                BillContract.addFood.CONTENT_URI_CART,
                pro,
                null,
                null,
                null);
    }


    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        cartCursorAdapter.swapCursor(data);


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        cartCursorAdapter.swapCursor(null);


    }


    @Override
    public void onStop() {
        super.onStop();
        try {
            progressDoalog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}