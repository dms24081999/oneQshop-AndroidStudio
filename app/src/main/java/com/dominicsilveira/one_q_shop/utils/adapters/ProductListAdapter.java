package com.dominicsilveira.one_q_shop.utils.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product.ProductDetails;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder>{
    Context context;
    List<ProductDetails> productDetailsArrayList = new ArrayList<ProductDetails>();


    public ProductListAdapter(List<ProductDetails> productDetailsArrayList){
        this.productDetailsArrayList = productDetailsArrayList;
        Log.d("ProductList Value", String.valueOf(productDetailsArrayList));
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parent;
        TextView productName,brandName,priceText;
        ImageView productImage;

        MyViewHolder(View itemView) {
            super(itemView);
            parent = (LinearLayout)itemView.findViewById(R.id.parent);
            productName = (TextView)itemView.findViewById(R.id.productName);
            brandName = (TextView)itemView.findViewById(R.id.brandName);
            priceText = (TextView)itemView.findViewById(R.id.priceText);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProductListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_product_card, parent, false);
        ProductListAdapter.MyViewHolder pvh = new ProductListAdapter.MyViewHolder(v);
        return pvh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ProductListAdapter.MyViewHolder holder, int position) {
        ProductDetails productDetails=productDetailsArrayList.get(position);
        setDatas(holder,productDetails);
    }

    public void setDatas(ProductListAdapter.MyViewHolder holder, final ProductDetails productDetails){
        holder.productName.setText(productDetails.getName());
        if(productDetails.getBrandDetails()!=null){
            holder.brandName.setText(productDetails.getBrandDetails().getName());
        }else{
            holder.brandName.setText("one-Q-shop");
        }
        holder.priceText.setText("₹ ".concat(productDetails.getPrice()));
        Picasso.get().load(AppConstants.BACKEND_URL.concat(productDetails.getImagesDetails().get(0).getImage())).into(holder.productImage);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return productDetailsArrayList.size();
    }
}
