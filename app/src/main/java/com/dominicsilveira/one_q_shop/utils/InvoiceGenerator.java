package com.dominicsilveira.one_q_shop.utils;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.dominicsilveira.oneqshoprestapi.pojo_classes.Cart.CartDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Cart.CartListDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.User.User;
import com.google.android.gms.tasks.OnFailureListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class InvoiceGenerator {
    List<CartDetails> cartDetails = new ArrayList<CartDetails>();
    String bookingKey;
    User userObj;
    File file;
    Integer count;
    double total_price;

    public InvoiceGenerator(){}

    public InvoiceGenerator(CartListDetails cartListDetails, User userObj, File file){
        this.cartDetails=cartListDetails.getResults();
        this.total_price=cartListDetails.getPrice();
        this.count=cartListDetails.getCount();
        this.userObj=userObj;
        this.file=file;
    }

    public void create(){
        Date date = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM, yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd-MMM-yyyy, hh:mm a");
        dateFormatter.setTimeZone(TimeZone.getTimeZone("IST"));
        timeFormatter.setTimeZone(TimeZone.getTimeZone("IST"));

        PdfDocument pdfDocument=new PdfDocument();
        Paint paint=new Paint();
        PdfDocument.PageInfo pageInfo=new PdfDocument.PageInfo.Builder(1000,655+(count*45),1).create();
        PdfDocument.Page page=pdfDocument.startPage(pageInfo);
        Canvas canvas=page.getCanvas();

        paint.setTextSize(50);
        canvas.drawText("one-Q-shop",30,60,paint);

        paint.setTextSize(25);
        canvas.drawText("name",30,90,paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Invoice no",canvas.getWidth()-40,40,paint);
        canvas.drawText("bookingKey",canvas.getWidth()-40,80,paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.rgb(150,150,150));
        canvas.drawRect(30,120,canvas.getWidth()-30,130,paint);

        paint.setColor(Color.BLACK);
        canvas.drawText(": ",50,170,paint);
        canvas.drawText(dateFormatter.format(date),250,170,paint);
        canvas.drawText(": ",620,170,paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(timeFormatter.format(date),canvas.getWidth()-50,170,paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.rgb(150,150,150));
        canvas.drawRect(30,220,canvas.getWidth()-30,270,paint);
//
        paint.setColor(Color.WHITE);
        canvas.drawText("Bill To: ",50,255,paint);

        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Customer Name: ",50,320,paint);
        canvas.drawText(userObj.getFirstName().concat(" ").concat(userObj.getLastName()),250,320,paint);
        canvas.drawText("Date: ",700,320,paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(dateFormatter.format(date),canvas.getWidth()-50,320,paint);

        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Email: ",50,365,paint);
        canvas.drawText(userObj.getEmail(),250,365,paint);
        canvas.drawText("Time: ",700,365,paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(timeFormatter.format(date),canvas.getWidth()-50,365,paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.rgb(150,150,150));
        canvas.drawRect(30,415,canvas.getWidth()-30,465,paint);

        paint.setColor(Color.WHITE);
        canvas.drawText("Plate-Number",50,450,paint);
        canvas.drawText("Wheeler-Type",240,450,paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Start-Time",canvas.getWidth()-320,450,paint);
        canvas.drawText("End-Time",canvas.getWidth()-50,450,paint);

        int i=0;
        for (CartDetails cartDetail : cartDetails) {
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setColor(Color.BLACK);
            canvas.drawText(cartDetail.getCartDetails().getName(),50,495+i,paint);
            canvas.drawText(cartDetail.getCartDetails().getBrandDetails().getName(),240,495+i,paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(Integer.toString(cartDetail.getCount()),canvas.getWidth()-320,495+i,paint);
            canvas.drawText(Float.toString(Float.parseFloat(cartDetail.getCartDetails().getPrice()) * cartDetail.getCount()),canvas.getWidth()-50,495+i,paint);
            paint.setTextAlign(Paint.Align.LEFT);
            i += 45;
        }
        i-=45;

        paint.setColor(Color.rgb(150,150,150));
        canvas.drawRect(30,565+i,canvas.getWidth()-40,575+i,paint);

        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
//        canvas.drawText("",550,615,paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Total Cost (Rs.):- "+Double.toString(total_price),canvas.getWidth()-50,615+i,paint);
        canvas.drawText("Paid:- YES",canvas.getWidth()-50,660+i,paint);

        pdfDocument.finishPage(page);
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();
    }


//    //this method will upload the file
//    public void uploadFile(final Context context,Application application) {
////        if(utils.isNetworkAvailable(application)) {
//            //if there is a file to upload
//            Uri filePath = Uri.fromFile(file);
//            if (filePath != null) {
//                //displaying a progress dialog while upload is going on
//                final ProgressDialog progressDialog = new ProgressDialog(context);
//                progressDialog.setTitle("Uploading");
//                progressDialog.show();
//
//            //if there is not any file
//            else {
//                //you can display an error toast
//                Toast.makeText(context, "No file Available!", Toast.LENGTH_SHORT).show();
//            }
////        }else{
////            Toast.makeText(context, "No Network Available!", Toast.LENGTH_SHORT).show();
////        }
//    }
//
//    public void downloadFile(String userID, String bookingKey, Context context, Application application) {
//        if(utils.isNetworkAvailable(application)){
//            FirebaseStorage storage = FirebaseStorage.getInstance();
//            StorageReference invoiceRef = storage.getReference().child("invoice/".concat(userID).concat("/").concat(bookingKey).concat(".pdf"));
//
//            final File localFile = new File(context.getExternalCacheDir(), File.separator + "invoice.pdf");
//            invoiceRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    Log.e("firebase ",";local tem file created  created " +localFile.toString());
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    Log.e("firebase ",";local tem file not created  created " +exception.toString());
//                }
//            });
//        }else{
//            Toast.makeText(context, "No Network Available!", Toast.LENGTH_SHORT).show();
//        }
//
//    }

    public void openFile(Context context) {
        final File localFile = new File(context.getExternalCacheDir(), File.separator + "invoice.pdf");
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(localFile),"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Intent intent = Intent.createChooser(target, "Open File");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }
    }

    public void shareFile(Context context) {
        final File localFile = new File(context.getExternalCacheDir(), File.separator + "invoice.pdf");
        Intent share = new Intent(Intent.ACTION_SEND);
        if(localFile.exists()) {
            share.setType("application/pdf");
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(localFile));
            share.putExtra(Intent.EXTRA_SUBJECT, "Sharing File...");
            share.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
            context.startActivity(Intent.createChooser(share, "Share File"));
        }
    }
}

