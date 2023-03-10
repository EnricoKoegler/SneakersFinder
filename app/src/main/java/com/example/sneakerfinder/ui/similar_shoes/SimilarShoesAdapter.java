package com.example.sneakerfinder.ui.similar_shoes;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sneakerfinder.R;
import com.example.sneakerfinder.db.entity.Shoe;
import com.example.sneakerfinder.db.entity.ShoeScanResult;
import com.example.sneakerfinder.db.entity.ShoeScanResultWithShoe;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SimilarShoesAdapter extends RecyclerView.Adapter<SimilarShoesAdapter.ItemViewHolder> {
    public interface ItemClickListener {
        void onItemClicked(ShoeScanResultWithShoe shoe);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView titleText;
        private final TextView descText;
        private final ImageView img;
        private final TextView priceText;

        public ItemViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.shoe_name);
            descText = itemView.findViewById(R.id.shoe_desc);
            img = itemView.findViewById(R.id.shoe_iv);
            priceText = itemView.findViewById(R.id.shoe_price);
            itemView.findViewById(R.id.item_root).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            SimilarShoesAdapter.this.onItemClicked(this.getLayoutPosition());
        }

    }

    private final LayoutInflater inflater;
    private final SimilarShoesActivity.ActivityStyle activityStyle;
    private List<ShoeScanResultWithShoe> shoeScans;
    private ItemClickListener listener;

    public SimilarShoesAdapter(Context context, SimilarShoesActivity.ActivityStyle activityStyle) {
        inflater = LayoutInflater.from(context);
        this.activityStyle = activityStyle;
    }

    @NonNull
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_shoe, parent, false);
        return new ItemViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (shoeScans != null){
            ShoeScanResultWithShoe shoeScanResultWithShoe = shoeScans.get(position);
            ShoeScanResult shoeScanResult = shoeScanResultWithShoe.shoeScanResult;
            Shoe shoe = shoeScanResultWithShoe.shoe;

            if (shoeScanResult != null) {
                if (activityStyle == SimilarShoesActivity.ActivityStyle.SIMILAR_SHOES)
                    holder.descText.setText(String.format(Locale.getDefault(), "Accuracy: %.0f%%", shoeScanResult.confidence * 100));
                else
                    holder.descText.setText(String.format(Locale.getDefault(), "%.0f%% match", shoeScanResult.confidence * 100));
                holder.descText.setTypeface(null, Typeface.BOLD);
                if(shoeScanResult.confidence > 0.0) holder.descText.setTextColor(Color.RED);
                if(shoeScanResult.confidence > 0.1) holder.descText.setTextColor(Color.rgb(255, 165, 0));
                if(shoeScanResult.confidence > 0.5) holder.descText.setTextColor(Color.GREEN);

            }

            if (shoe != null) {
                if (shoe.name != null) holder.titleText.setText(shoe.model);
                if (shoe.price != null) holder.priceText.setText(shoe.price);
                if (shoe.thumbnailUrl != null) Picasso.get().load(shoe.thumbnailUrl).into(holder.img);
            }
       }
    }

    public void setItems(List<ShoeScanResultWithShoe> items){
        this.shoeScans = items;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        if (shoeScans != null)
            return shoeScans.size();
        else return 0;
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    private void onItemClicked(int index) {
        if(this.listener != null){
            this.listener.onItemClicked(shoeScans.get(index));
        }
    }
}