package com.satyajit.attendance.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.satyajit.attendance.R;
import com.satyajit.attendance.database.AppDatabase;
import com.satyajit.attendance.models.DbModel;

import java.util.List;

public class MarksAdapter extends RecyclerView.Adapter<MarksAdapter.MyViewHolder> {
    private Context context;
    private List<DbModel> mMarksList;

    OnShareClickedListener mCallback;

    public MarksAdapter(Context context, OnShareClickedListener mCallback) {
        this.context = context;
        this.mCallback = mCallback;
    }


    public interface OnShareClickedListener {
         void onClick(String value);
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_last_marked_items, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarksAdapter.MyViewHolder myViewHolder, final int i) {

        myViewHolder.name.setText(mMarksList.get(i).getName());
        myViewHolder.YvNumber.setText(mMarksList.get(i).getYvNumber());

        myViewHolder.cardClicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onClick(mMarksList.get(i).getHash());
            }
        });



    }

    @Override
    public int getItemCount() {
        if (mMarksList == null) {
            return 0;
        }
        return mMarksList.size();

    }

    public void setTasks(List<DbModel> personList) {
        mMarksList = personList;
        notifyDataSetChanged();
    }

    public List<DbModel> getTasks() {

        return mMarksList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, YvNumber;
        AppDatabase mDb;
        CardView cardClicker;

        MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            mDb = AppDatabase.getInstance(context);
            name = itemView.findViewById(R.id.name);
            YvNumber = itemView.findViewById(R.id.YvNumber);
            cardClicker = itemView.findViewById(R.id.cardClicker);

        }



    }


}
