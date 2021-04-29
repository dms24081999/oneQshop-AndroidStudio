package com.dominicsilveira.one_q_shop.utils.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.dominicsilveira.one_q_shop.R;;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Invoice.InvoiceDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.ProductDetails;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;


public class InvoiceListAdapter extends RecyclerView.Adapter<InvoiceListAdapter.MyViewHolder> {

    private List<InvoiceDetails> items = new ArrayList<>();
    private Context context;

    public InvoiceListAdapter(Context context, List<InvoiceDetails> items) {
        this.items = items;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date_time_text;
        public ImageButton share_btn,open_btn;

        MyViewHolder(View itemView) {
            super(itemView);
            date_time_text = (TextView) itemView.findViewById(R.id.date_time_text);
            share_btn = (ImageButton) itemView.findViewById(R.id.share_btn);
            open_btn = (ImageButton) itemView.findViewById(R.id.open_btn);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = (Activity) recyclerView.getContext();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public InvoiceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_invoice_card, parent, false);
        InvoiceListAdapter.MyViewHolder pvh = new InvoiceListAdapter.MyViewHolder(v);
        return pvh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final InvoiceListAdapter.MyViewHolder holder, int position) {
        InvoiceDetails invoiceDetails=items.get(position);
        setDatas(holder,invoiceDetails);
    }

    public void setDatas(InvoiceListAdapter.MyViewHolder holder, final InvoiceDetails invoiceDetails){
        holder.date_time_text.setText(invoiceDetails.getUploadedAt());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}