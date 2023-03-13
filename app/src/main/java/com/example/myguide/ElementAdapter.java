package com.example.myguide;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ElementAdapter extends RecyclerView.Adapter<ElementAdapter.ElementViewHolder> {
    private List<Element> ElementList;
    private OnItemClickListener mOnItemClickListener;

    public ElementAdapter(List<Element> ElementList) {
        this.ElementList = ElementList;
    }

    @NonNull
    @Override
    public ElementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item, parent, false);
        return new ElementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ElementViewHolder holder, int position) {
        Element Element = ElementList.get(position);
        holder.nameTextView.setText(Element.getName());
        holder.latitudeTextView.setText("Latitude : "+Element.getLatitude());
        holder.longitudeTextView.setText("Longitutde : "+Element.getLongitude());
        holder.ratingTextView.setText(Element.getRating());
        holder.addressTextView.setText(Element.getAddress());
        Glide.with(holder.element_image.getContext()).load(Element.getUrl()).into(holder.element_image);
        // Set the OnClickListener on the CardView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(Element);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ElementList.size();
    }


    public static class ElementViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView latitudeTextView;
        public TextView longitudeTextView;
        public TextView ratingTextView;
        public TextView addressTextView;
        ImageView element_image;

        public ElementViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            latitudeTextView = itemView.findViewById(R.id.latitudeTextView);
            longitudeTextView = itemView.findViewById(R.id.longitudeTextView);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            element_image=itemView.findViewById(R.id.element_image);
        }
    }
    public List<Element> getElementList() {
        return ElementList;
    }


    public void setElementList(List<Element> ElementList) {
        this.ElementList = ElementList;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }
}
