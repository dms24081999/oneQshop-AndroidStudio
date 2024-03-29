package com.dominicsilveira.one_q_shop.utils;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiListener;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiResponse;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Cart.CartDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Cart.CartListDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Invoice.InvoiceListDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.User.User;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class InvoiceGenerator {
    List<CartDetails> cartDetails = new ArrayList<CartDetails>();
    String bookingKey,line1,line2;
    User userObj;
    File file;
    Integer count;
    double total_price;
    Date date;
    ApiListener context;
    RestApiMethods restMethods;

    public InvoiceGenerator(){}

    public InvoiceGenerator(ApiListener context, CartListDetails cartListDetails, User userObj, File file, Date date, String line1, String line2,String bookingKey, RestApiMethods restMethods){
        this.cartDetails=cartListDetails.getResults();
        this.total_price=cartListDetails.getPrice();
        this.count=cartListDetails.getCount();
        this.userObj=userObj;
        this.file=file;
        this.date=date;
        this.context=context;
        this.restMethods=restMethods;
        this.line1=line1;
        this.line2=line2;
        this.bookingKey=bookingKey;
    }

    public void create(){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM, yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
//        dateFormatter.setTimeZone(TimeZone.getTimeZone("IST"));
//        timeFormatter.setTimeZone(TimeZone.getTimeZone("IST"));

        PdfDocument pdfDocument=new PdfDocument();
        Paint paint=new Paint();
        PdfDocument.PageInfo pageInfo=new PdfDocument.PageInfo.Builder(1000,655+(count*45),1).create();
        PdfDocument.Page page=pdfDocument.startPage(pageInfo);
        Canvas canvas=page.getCanvas();

        paint.setTextSize(50);
        canvas.drawText("one-Q-shop",30,60,paint);

        paint.setTextSize(25);
        canvas.drawText("",30,90,paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Invoice no",canvas.getWidth()-40,40,paint);
        canvas.drawText(bookingKey,canvas.getWidth()-40,80,paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.rgb(150,150,150));
        canvas.drawRect(30,120,canvas.getWidth()-30,130,paint);

        paint.setColor(Color.BLACK);
        canvas.drawText("Address: ",50,165,paint);
        canvas.drawText(line1,170,165,paint);
        canvas.drawText(line2,170,195,paint);

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
        canvas.drawText("Product Name",50,450,paint);
        canvas.drawText("Brand Name",470,450,paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Quantity",canvas.getWidth()-230,450,paint);
        canvas.drawText("Price (Rs.)",canvas.getWidth()-60,450,paint);

        int i=0;
        for (CartDetails cartDetail : cartDetails) {
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setColor(Color.BLACK);
            canvas.drawText(cartDetail.getCartDetails().getName(),50,495+i,paint);
            canvas.drawText(cartDetail.getCartDetails().getBrandDetails().getName(),470,495+i,paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(Integer.toString(cartDetail.getCount()),canvas.getWidth()-230,495+i,paint);
            canvas.drawText(Float.toString(Float.parseFloat(cartDetail.getCartDetails().getPrice()) * cartDetail.getCount()),canvas.getWidth()-60,495+i,paint);
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
    public void uploadFile() {
        RequestBody reqFile = RequestBody.create(okhttp3.MediaType.parse("application/pdf"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("pdf_file",
                                file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("application/pdf"), "pdf_file");
        String token=BasicUtils.getSharedPreferencesString((Activity)context,"TokenAuth","token","0");
        Call<InvoiceListDetails> req = restMethods.postInvoiceDetails(userObj.getId(),token,body, name);
        ApiResponse.callRetrofitApi(req, RestApiMethods.postInvoiceDetailsRequest, this.context);
    }
}

