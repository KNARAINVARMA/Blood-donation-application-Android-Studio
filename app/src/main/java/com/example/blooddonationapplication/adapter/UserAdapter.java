package com.example.blooddonationapplication.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blooddonationapplication.R;
import com.example.blooddonationapplication.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context context;
    private List<UserModel> list;
    public UserAdapter(Context context, List<UserModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(context).inflate(R.layout.user_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserModel user = list.get(position);
        holder.name.setText(list.get(position).getName());
        holder.age.setText(list.get(position).getAge());
        holder.blood.setText(list.get(position).getBlood());
        holder.address.setText(list.get(position).getAddress());
        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+list.get(position).getPhone().toString()));
                context.startActivity(intent);
            }

        });
    }
public void FilterList(ArrayList<UserModel> filterList) {
        list = filterList;
        notifyDataSetChanged();

}
    @Override
    public int getItemCount() {
        return list.size();
    }



    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name,email,phone,age,address,blood;
        Button callButton;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.Name);
            age = itemView.findViewById(R.id.age);
            blood = itemView.findViewById(R.id.bloodGroup);
            address = itemView.findViewById(R.id.address);
            callButton = itemView.findViewById(R.id.callButton);
        }
    }
}