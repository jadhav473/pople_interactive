package com.example.myapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;
import com.example.myapp.Volley.MyLocalProvider;
import com.example.myapp.model.ProfileData;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    Context context;
    ArrayList<ProfileData> arrayList;
    RecyclerView recyclerView;

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.new_product_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    public ProductAdapter(Context context, ArrayList arrayList, RecyclerView recyclerView) {
        this.context = context;
        this.arrayList = arrayList;
        this.recyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {

        ProfileData profileData = arrayList.get(position);

        holder.user_name.setText(profileData.getFirst()+" "+profileData.getLast());
        holder.user_data.setText("("+profileData.getGender()+" , "+profileData.getAge()+")");
        holder.user_desc.setText(profileData.getCity() +" - "+profileData.getPostcode());

        MyLocalProvider.glideImageUsingURL(context,profileData.getLarge(),holder.profile_image,R.drawable.ic_menu_gallery);

       /* holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.getLayoutManager().scrollToPosition(position + 1);
            }
        });*/
        //holder.price_tag.setText("                                             100₹");

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public boolean isSwipeable;
        TextView user_name,user_desc,user_data;
        ImageView profile_image;
        Button btn_accept;
        //TextView price_tag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.user_name);
            user_desc = itemView.findViewById(R.id.user_desc);
            user_data = itemView.findViewById(R.id.user_data);
            profile_image = itemView.findViewById(R.id.profile_image);
           /* btn_accept = itemView.findViewById(R.id.btn_accept);*/
            //price_tag = itemView.findViewById(R.id.price_tag);

        }
    }
}
