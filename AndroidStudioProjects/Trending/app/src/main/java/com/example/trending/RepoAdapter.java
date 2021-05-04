package com.example.trending;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.viewholder> {

    List<Repo> mlist=new ArrayList<>();
ClickListener mlistener;


public void OnitemClicked(ClickListener listener){
    mlistener=listener;
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

        Repo currentRepo = mlist.get(position);
        holder.repo_name.setText(currentRepo.getRepo_name());
        holder.desc.setText(currentRepo.getDescription());
        holder.author.setText(currentRepo.getAuthor());
        holder.repo_url.setText(currentRepo.getRepo_url());
        holder.lang.setText(currentRepo.getLang());
        holder.stars.setText(currentRepo.getStars()+ "");
        holder.forks.setText(currentRepo.getForks()+"");
//        Picasso.get()
//                .load(Uri.parse(currentRepo.getAvatar()))
//                .resize(500,500)
//                .centerCrop()
//                .into(holder.avatar);
        if (currentRepo.getAvatar_bitmap()!=null) {
            Bitmap imgBitmap=decodeString(currentRepo.getAvatar_bitmap());
            holder.avatar.setImageBitmap(imgBitmap);
        }
        else{
            Log.e("BITMAP","not found");
        }
        holder.infoview.setVisibility(mlist.get(position).is_active ? View.VISIBLE :View.GONE);
    }

public void setList(List<Repo> newlist){
        mlist=newlist;
        notifyDataSetChanged();
}

public List<Repo> getList(){
        return mlist;
}

public Bitmap decodeString(String encode){
    try {
        byte[] encodeByte = Base64.decode(encode, Base64.DEFAULT);
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
        return mlist.size();
    }

    class viewholder extends RecyclerView.ViewHolder {
        TextView repo_name, author, desc, repo_url,lang,stars,forks;
        ImageView avatar;
        LinearLayout infoview;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            repo_name = itemView.findViewById(R.id.tv_name);
            author = itemView.findViewById(R.id.tv_author);
            desc = itemView.findViewById(R.id.tv_desc);
            repo_url = itemView.findViewById(R.id.tv_url);
            avatar = itemView.findViewById(R.id.iv_avatar);
            infoview = itemView.findViewById(R.id.info_view);
            stars=itemView.findViewById(R.id.tv_stars);
            forks=itemView.findViewById(R.id.tv_forks);
            lang=itemView.findViewById(R.id.tv_lang);

            repo_url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   mlistener.UrlClicked(getAdapterPosition());
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mlist.get(getAdapterPosition()).is_active=(!mlist.get(getAdapterPosition()).is_active);
                    for(int i=0;i<mlist.size();i++){
                        if(i!=getAdapterPosition()){
                            mlist.get(i).is_active=false;
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }


    interface ClickListener{
        void UrlClicked(int position);
    }
}
