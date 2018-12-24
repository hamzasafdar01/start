package com.example.root.start;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MembersViewHolder> {
    ArrayList<CreateUser> namelist;
    Context c;

    MembersAdapter(ArrayList<CreateUser> namelist, Context c){
        this.namelist=namelist;
        this.c=c;
    }


    @Override
    public int getItemCount() {
        return namelist.size();
    }


    @NonNull
    @Override
    public MembersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view,viewGroup,false);
        MembersViewHolder membersViewHolder = new MembersViewHolder(v,c,namelist);



        return membersViewHolder;
    }





    @Override
    public void onBindViewHolder(@NonNull MembersViewHolder membersViewHolder, int i) {

        final CreateUser currentUserObj = namelist.get(i);

            Log.i("Jaleel", namelist.size() + "  " + currentUserObj);
            membersViewHolder.name_txt.setText(currentUserObj.name);
            Picasso.get().load(currentUserObj.imageUrl).placeholder(R.drawable.ic_person_black_24dp).into(membersViewHolder.circleImageView);

            if (currentUserObj.issharing.equals("false")) {
                membersViewHolder.status.setImageResource(R.drawable.reddot);
            } else if (currentUserObj.issharing.equals("true")) {
                membersViewHolder.status.setImageResource(R.drawable.greendot);
            }


            membersViewHolder.itemClickListener = new itemClickListener() {
                @Override
                public void onClick(View v, int position) {
                    Log.d("hamza", "onClick: " + currentUserObj.userId);
                    if(currentUserObj.userId!=null){

                        Intent map = new Intent(v.getContext(),UserLocationMainActivity.class);
                        map.putExtra("userid",currentUserObj.userId);
                        c.startActivity(map);

                    }

                }
            };


    }



static class MembersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {
        public
        TextView name_txt;
        CircleImageView circleImageView;
        ImageView status;
        Context c;
        ArrayList<CreateUser> nameArrayList;
        FirebaseAuth auth;

        FirebaseUser user;
        itemClickListener itemClickListener;
        public MembersViewHolder(@NonNull View itemView,Context c, ArrayList<CreateUser> nameArrayList) {
            super(itemView);

            this.c = c;
            this.nameArrayList = nameArrayList;
            itemView.setOnClickListener(this);
            auth =FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            name_txt = itemView.findViewById(R.id.item_title);
            circleImageView = itemView.findViewById(R.id.circleImageView);
            status = itemView.findViewById(R.id.status);
            itemView.setOnLongClickListener(this);


        }


        public void setItemClickListener(com.example.root.start.itemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public boolean onLongClick(View v) {

            Log.d("hamza", "onLongClick: " );
            return true;
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(c,"You have clicked this user" ,Toast.LENGTH_SHORT).show();
//            Log.d("hamza", "onClick: " );
            itemClickListener.onClick(v,getAdapterPosition());


        }




    }

}
