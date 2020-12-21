package com.example.to_do;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Data").child(userUid);
    static ArrayList<Data> DataList;
    static Context context;
    private static ClickListener clickListener;
    private static LayoutInflater inflater;


    public Adapter(Context context, ArrayList<Data> DataList) {

        this.DataList = DataList;
        this.context = context;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        TextView title, desc, date, time;
        Button btnUpdate, btnDelete;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            this.itemView = itemView;
            title = itemView.findViewById(R.id.tasktitle);
            desc = itemView.findViewById(R.id.taskdesc);
            date = itemView.findViewById(R.id.taskdate);
            time = itemView.findViewById(R.id.tasktime);
            btnUpdate =itemView.findViewById(R.id.btn_Update);
            btnDelete = itemView.findViewById(R.id.btn_Delete);
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("RecycleView", "onClick:" +getAdapterPosition());
                    showUpdateDialog(DataList);

                }
            });

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    clickListener.onItemClick(getAdapterPosition(),v);
//                    Log.d("RecycleView", "onClick:" +getAdapterPosition());
//
//                }
//            });
        }

        private void showUpdateDialog(ArrayList<Data> DataList) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            final View dialogView = inflater.inflate(R.layout.update_layout, null);
            dialogBuilder.setView(dialogView);
            final EditText editNewTitle = (EditText) dialogView.findViewById(R.id.editNewTitle);
            final EditText editNewDescription = (EditText) dialogView.findViewById(R.id.editNewDescription);
            final EditText editTime = (EditText) dialogView.findViewById(R.id.editTime);
            final EditText editDate = (EditText) dialogView.findViewById(R.id.editDate);

            final Button btnUpdate = (Button) dialogView.findViewById(R.id.btn_Update);
            final Button btnDelete = (Button) dialogView.findViewById(R.id.btn_Delete);
//            databaseReference.push().setValue(DataList);
            dialogBuilder.setTitle("Updating Title" +title);
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String editTitle = editNewTitle.getText().toString().trim();
                    String editDesc = editNewDescription.getText().toString().trim();
                    String edittime = editTime.getText().toString().trim();
                    String editdate = editDate.getText().toString().trim();
                    if (TextUtils.isEmpty(editTitle)) {

                        editNewTitle.setError("Title required");
                        return;
                    }
                    btnUpdate(editTitle,editDesc,editdate,edittime);
                    alertDialog.dismiss();
                }

                private void btnUpdate(String Title, String Desc, String selecttime, String selectdate) {
                    final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Data").child(userUid);
                    Data data = new Data(Title,Desc,selecttime,selectdate);
                    databaseReference.setValue(data);
//        Toast.makeText(this,"Task Updated Successfully",Toast.LENGTH_LONG).show();
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    btnDelete(userUid);
                }
            });
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);

        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return DataList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Data data = DataList.get(position);
        holder.title.setText(data.getTitle());
        holder.desc.setText(data.getDesc());
        holder.date.setText(data.getSelectdate());
        holder.time.setText(data.getSelecttime());
    }

//    public void onClick(final View view) {
//        RecyclerView mRecyclerView = null;
//        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
//        String item = String.valueOf(DataList.get(itemPosition));
//        Toast.makeText(, item, Toast.LENGTH_LONG).show();
//    }


    private void showUpdateDialog(String Title, String Desc, String Selecttime, String Selectdate) {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.update_layout, null);
//        dialogBuilder.setView(dialogView);
//        final EditText editNewTitle = (EditText) dialogView.findViewById(R.id.editNewTitle);
//        final EditText editNewDescription = (EditText) dialogView.findViewById(R.id.editNewDescription);
//        final EditText editTime = (EditText) dialogView.findViewById(R.id.editTime);
//        final EditText editDate = (EditText) dialogView.findViewById(R.id.editDate);
//
//        final Button btnUpdate = (Button) dialogView.findViewById(R.id.btn_Update);
//        final Button btnDelete = (Button) dialogView.findViewById(R.id.btn_Delete);
//        dialogBuilder.setTitle("Updating Title" +Title);
//        AlertDialog alertDialog = dialogBuilder.create();
//        alertDialog.show();
//
//        btnUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String editTitle = editNewTitle.getText().toString().trim();
//                String editDesc = editNewDescription.getText().toString().trim();
//                String edittime = editTime.getText().toString().trim();
//                String editdate = editDate.getText().toString().trim();
//                if (TextUtils.isEmpty(editTitle)) {
//
//                    editNewTitle.setError("Title required");
//                    return;
//                }
//                updateTitle(Title,Desc,Selecttime,Selectdate);
//                alertDialog.dismiss();
//            }
//        });
//
//        btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deleteTitle(userUid);
//            }
//        });
    }

    private void btnDelete(String Title) {
        DatabaseReference drTitle = FirebaseDatabase.getInstance().getReference("Data").child(userUid);
        drTitle.removeValue();

    }

    private boolean btnUpdate(String Title, String Desc, String selecttime, String selectdate ) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Data").child(userUid);
        Data data = new Data(Title,Desc,selecttime,selectdate);
        databaseReference.setValue(data);
//        Toast.makeText(this,"Task Updated Successfully",Toast.LENGTH_LONG).show();
        return true;

    }


}
