package com.dominicsilveira.one_q_shop.utils.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.ui.product.ProductCategoriesActivity;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.CategoriesDetails;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static String TAG = CategoryListAdapter.class.getSimpleName();
    private List<CategoriesDetails> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, CategoriesDetails obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public CategoryListAdapter(Context context, List<CategoriesDetails> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView brief;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            title = (TextView) v.findViewById(R.id.title);
            brief = (TextView) v.findViewById(R.id.brief);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_category_card, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            final CategoriesDetails p = items.get(position);
            view.title.setText(p.getName());
            view.brief.setText(p.getDescription());
            Picasso.get().load(p.getImage()).into(view.image);
//            Log.e(TAG,p.getImage());
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(ctx, ProductCategoriesActivity.class);
                    intent.putExtra("CATEGORY_ID",p.getId());
                    intent.putExtra("CATEGORY_NAME",p.getName());
                    ctx.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}