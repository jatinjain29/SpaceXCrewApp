package com.example.spacexcrew;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spacexcrew.Class.CrewMember;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;


class RvAdapter extends RecyclerView.Adapter<RvAdapter.viewholder> {
    Context context;
    List<CrewMember> all_members = new ArrayList<>();

    public RvAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_view, parent, false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        CrewMember current_member = all_members.get(position);
        if (current_member.is_open) {
            holder.info_view.setVisibility(View.VISIBLE);
        } else {
            holder.info_view.setVisibility(View.GONE);
        }
        holder.status.setText(current_member.getStatus().toUpperCase());
        holder.wikipedia.setText(current_member.getWikipedia_url());
        holder.agency.setText("Agency: " + current_member.getAgency());
        holder.name.setText(current_member.getName());
        Bitmap mbitmap = getBitmap(current_member.getMember_image());
        Log.d("UID", current_member.getName() + " " + current_member.getUid() + " ");
        // Log.d("bitmap",current_member.getMember_image()+" ");


        Picasso.get()
                .load(current_member.getImage_url())
                .resize(250, 250)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        holder.member_image.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        holder.member_image.setImageBitmap(mbitmap);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        holder.member_image.setImageDrawable(placeHolderDrawable);
                    }
                });
    }


    public Bitmap getBitmap(String encoded_string) {
        try {
            byte[] encodeByte = Base64.decode(encoded_string, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return all_members.size();
    }

    public void setList(List<CrewMember> updated_list) {
        all_members = updated_list;
        notifyDataSetChanged();
    }

    class viewholder extends RecyclerView.ViewHolder {
        ImageView member_image;
        TextView name, agency, wikipedia, status;
        LinearLayout info_view;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            member_image = itemView.findViewById(R.id.iv_member);
            name = itemView.findViewById(R.id.tv_name);
            agency = itemView.findViewById(R.id.tv_agency);
            wikipedia = itemView.findViewById(R.id.tv_wikipedia);
            status = itemView.findViewById(R.id.tv_status);
            info_view = itemView.findViewById(R.id.info_view);



            wikipedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    String wikipedia_url = all_members.get(getAdapterPosition()).getWikipedia_url();
                    intent.setData(Uri.parse(wikipedia_url));
                    context.startActivity(intent);

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(all_members.get(getAdapterPosition()).is_open){
                        all_members.get(getAdapterPosition()).is_open=false;

                    }
                    else{
                        all_members.get(getAdapterPosition()).is_open=true;

                    }
                    notifyItemChanged(getAdapterPosition());
                }
            });


        }
    }
}