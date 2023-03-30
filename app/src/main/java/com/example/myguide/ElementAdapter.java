package com.example.myguide;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ElementAdapter extends RecyclerView.Adapter<ElementAdapter.ElementViewHolder> {
    private final List<Element> listFull;
    private List<Element> ElementList;
    private OnItemClickListener mOnItemClickListener;

    public ElementAdapter(List<Element> ElementList) {
        this.ElementList = ElementList;
           listFull=new ArrayList<>(ElementList);
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
    public void filter(String text,ArrayList<Element> fullElemntAdapter) {
        List<Element> filteredList = new ArrayList<>();

        if (text.isEmpty()) {
            filteredList.addAll(fullElemntAdapter);
        } else {
            String searchText = text.toLowerCase();

            for (Element element : fullElemntAdapter) {
                if (element.getName().toLowerCase().contains(searchText) || element.getAddress().toLowerCase().contains(searchText)
                        || element.getRating().toLowerCase().contains(searchText) || Double.toString(element.getLatitude()).toLowerCase().contains(searchText)
                        || Double.toString(element.getLongitude()).toLowerCase().contains(searchText)) {
                    filteredList.add(element);
                }
            }
        }
        ElementList.clear();
        ElementList.addAll(filteredList);
        notifyDataSetChanged();
    }

    public void sortData(String sortBy,ArrayList<Element> fullElemntAdapter) {
        switch (sortBy.toLowerCase()) {
            case "name":
                Collections.sort(ElementList, new Comparator<Element>() {
                    @Override
                    public int compare(Element o1, Element o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                break;
            case "rating":
                Collections.sort(ElementList, new Comparator<Element>() {
                    @Override
                    public int compare(Element o1, Element o2) {
                        return o2.getRating().compareTo(o1.getRating());
                    }
                });
                break;
            case "none":
                ElementList.clear();
                ElementList.addAll(fullElemntAdapter);
                break;
            default:
                throw new IllegalArgumentException("Invalid sortBy argument: " + sortBy);
        }
        notifyDataSetChanged();
    }

}
