package com.dominicsilveira.one_q_shop.utils.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.ui.product.ProductDetailsActivity;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Cart.CartDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Cart.CartListDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder>{
    Context context;
    List<CartDetails> cartDetails, filteredList,arrayListFiltered;

    public CartListAdapter(List<CartDetails> cartDetails){
        this.cartDetails = cartDetails;
        this.arrayListFiltered = new ArrayList<>(cartDetails);
        this.filteredList = new ArrayList<>(cartDetails);
//        Log.d("ProductList Value", String.valueOf(cartDetailsList));
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parent,productCardBtn;
        TextView productName,brandName,priceText;
        ImageView productImage;

        MyViewHolder(View itemView) {
            super(itemView);
            parent = (LinearLayout)itemView.findViewById(R.id.parent);
            productCardBtn = (LinearLayout)itemView.findViewById(R.id.productCardBtn);
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
    public CartListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_product_card, parent, false);
        CartListAdapter.MyViewHolder pvh = new CartListAdapter.MyViewHolder(v);
        return pvh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final CartListAdapter.MyViewHolder holder, int position) {
        CartDetails productDetails= cartDetails.get(position);
        setDatas(holder,productDetails);
    }

    public void setDatas(CartListAdapter.MyViewHolder holder, final CartDetails cartDetails){
        holder.productName.setText(cartDetails.getCartDetails().getName());
        final String brandName;
        brandName= cartDetails.getCartDetails().getBrandDetails().getName();
        holder.brandName.setText(brandName);
        holder.productCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("PRODUCT_DETAILS", cartDetails.getCartDetails());
                intent.putExtra("BARCODE_VALUE", cartDetails.getCartDetails().getBarcode());
                context.startActivity(intent);
            }
        });
        holder.priceText.setText("₹ ".concat(cartDetails.getCartDetails().getPrice()));
        Picasso.get().load(AppConstants.BACKEND_URL.concat(cartDetails.getCartDetails().getImagesDetails().get(0).getImage())).into(holder.productImage);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return cartDetails.size();
    }

    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(arrayListFiltered);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (CartDetails item : arrayListFiltered) {
                    if (item.getCartDetails().getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            cartDetails.clear();
            try{cartDetails.addAll((Collection<? extends CartDetails>) results.values);}
            catch (Exception e){e.printStackTrace();}
            notifyDataSetChanged();
        }
    };
}

