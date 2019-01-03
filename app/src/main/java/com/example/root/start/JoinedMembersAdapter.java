package com.example.root.start;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class JoinedMembersAdapter extends RecyclerView.Adapter<JoinedMembersAdapter.JoinedMembersViewHolder> {
    ArrayList<CreateUser> namelist;
    Context c;
    DatabaseReference myidref,friendidref;
    FirebaseAuth auth;
    FirebaseUser user;

    JoinedMembersAdapter(ArrayList<CreateUser> namelist, Context c){
        this.namelist=namelist;
        this.c=c;
    }

    @Override
    public int getItemCount() {
        return namelist.size();
    }

    @NonNull
    @Override
    public JoinedMembersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view,viewGroup,false);
        JoinedMembersViewHolder membersViewHolder = new JoinedMembersViewHolder(v,c,namelist);
        return membersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull JoinedMembersViewHolder joinedMembersViewHolder, int i) {
        final CreateUser currentUserObj = namelist.get(i);

        joinedMembersViewHolder.name_txt.setText(currentUserObj.name);
        Picasso.get().load(currentUserObj.imageUrl).placeholder(R.drawable.ic_person_black_24dp).into(joinedMembersViewHolder.circleImageView);

        if (currentUserObj.issharing.equals("false")) {
           joinedMembersViewHolder.status.setImageResource(R.drawable.reddot);
        } else if (currentUserObj.issharing.equals("true")) {
            joinedMembersViewHolder.status.setImageResource(R.drawable.greendot);
        }


        joinedMembersViewHolder.itemClickListener = new itemClickListener() {
            @Override
            public void onClick(View v, int position) {

                if(currentUserObj.userId!=null){
                    AlertDialog.Builder a_builder = new AlertDialog.Builder(v.getContext());
                    a_builder.setMessage("Are you sure you want to delete this user? ").setCancelable(false).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            auth = FirebaseAuth.getInstance();
                            user = auth.getCurrentUser();
                            friendidref = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserObj.userId).child("CircleMembers").child(user.getUid());
                            friendidref.removeValue();
                            myidref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Joined Circle").child(currentUserObj.userId);
                            myidref.removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(c,currentUserObj.name +" Deleted", Toast.LENGTH_LONG).show();

                                        }
                                    });

                        }
                    });

                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Alert..!!!");
                    alert.show();
                }

            }
        };
    }

    public static class JoinedMembersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {
        TextView name_txt;
        CircleImageView circleImageView;
        ImageView status;
        Context c;
        ArrayList<CreateUser> nameArrayList;
        FirebaseAuth auth;

        FirebaseUser user;
        itemClickListener itemClickListener;
        public JoinedMembersViewHolder(@NonNull View itemView,Context c, ArrayList<CreateUser> nameArrayList) {
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

            return true;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition());
        }
    }
}
