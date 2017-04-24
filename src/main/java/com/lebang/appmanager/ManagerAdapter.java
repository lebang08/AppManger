package com.lebang.appmanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/12
 */
public class ManagerAdapter extends RecyclerView.Adapter<ManagerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<AppBean> mList;

    private OnItemListener mListener;

    public void setOnItemClickListener(OnItemListener listener) {
        this.mListener = listener;
    }


    public ManagerAdapter(Context mContext, ArrayList<AppBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_manager, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.mIcon.setImageDrawable(mList.get(position).img);
        holder.mNamePackage.setText(mList.get(position).package_name);
        holder.mNamePackLabel.setText(mList.get(position).label_name);
        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(position);
                }
            }
        });
        holder.mCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListener != null) {
                    mListener.onUninstallClick(position);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mCard;
        ImageView mIcon;
        TextView mNamePackage, mNamePackLabel;

        public MyViewHolder(View itemView) {
            super(itemView);
            mCard = (RelativeLayout) itemView.findViewById(R.id.card);
            mIcon = (ImageView) itemView.findViewById(R.id.icon_app);
            mNamePackage = (TextView) itemView.findViewById(R.id.name_package);
            mNamePackLabel = (TextView) itemView.findViewById(R.id.name_label);
        }
    }

    public interface OnItemListener {
        void onClick(int pos);

        void onUninstallClick(int pos);
    }
}