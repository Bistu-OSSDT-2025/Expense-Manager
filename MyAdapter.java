package com.example.keepb.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keepb.EditActivity;
import com.example.keepb.database.NoteDbOpenHelper;
import com.example.keepb.R;
import com.example.keepb.bean.Note;
import com.example.keepb.bean.Transaction;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Transaction> mBeanList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private NoteDbOpenHelper mNoteDbOpenHelper;
    
    public MyAdapter(Context context, List<Transaction> mBeanList){
        this.mBeanList = mBeanList;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mNoteDbOpenHelper = new NoteDbOpenHelper(mContext);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.list_item_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Transaction transaction = mBeanList.get(position);
        
        // 设置分类作为标题
        holder.mTvTitle.setText(transaction.getCategory());
        
        // 设置描述作为内容
        holder.mTvContent.setText(transaction.getDescription());
        
        // 显示金额和类型
        String amountText = transaction.getTypeString() + ": ¥" + 
                           String.format("%.2f", transaction.getAmount());
        holder.mTvTime.setText(amountText);
        
        // 根据收支类型设置颜色
        try {
            if (transaction.isIncome()) {
                holder.mTvTime.setTextColor(0xFF4CAF50); // 绿色
            } else {
                holder.mTvTime.setTextColor(0xFFE91E63); // 红色
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 如果颜色设置失败，使用默认颜色
            holder.mTvTime.setTextColor(0xFF000000); // 黑色
        }
        
        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditActivity.class);
                intent.putExtra("transaction", transaction);
                mContext.startActivity(intent);
            }
        });

        holder.rlContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Dialog dialog = new Dialog(mContext, android.R.style.ThemeOverlay_Material_Dialog_Alert);
                View dialogView = mLayoutInflater.inflate(R.layout.list_item_dialog_layout, null);
                TextView tvDelete = dialogView.findViewById(R.id.tv_delete);
                TextView tvEdit = dialogView.findViewById(R.id.tv_edit);

                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        showDeleteConfirmDialog(transaction, position);
                    }
                });
                tvEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, EditActivity.class);
                        intent.putExtra("transaction", transaction);
                        mContext.startActivity(intent);
                        dialog.dismiss();
                    }
                });
                dialog.setContentView(dialogView);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mTvTitle;
        TextView mTvContent;
        TextView mTvTime;
        ViewGroup rlContainer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mTvTitle = itemView.findViewById(R.id.tv_title);
            this.mTvContent = itemView.findViewById(R.id.tv_content);
            this.mTvTime = itemView.findViewById(R.id.tv_time);
            this.rlContainer = itemView.findViewById(R.id.rl_item_container);
        }
    }

    public void refreshData(List<Transaction> transactions){
        this.mBeanList = transactions;
        notifyDataSetChanged();
    }

    public void removeData(int pos) {
        mBeanList.remove(pos);
        notifyItemRemoved(pos);
    }
    

    private void showDeleteConfirmDialog(Transaction transaction, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("确认删除")
                .setMessage("确定要删除交易记录《" + transaction.getCategory() + " - " + transaction.getDescription() + "》吗？\n删除后无法恢复。")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("确定删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTransaction(transaction, position);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(true)
                .show();
    }

    private void deleteTransaction(Transaction transaction, int position) {
        try {
            int row = mNoteDbOpenHelper.deleteTransactionById(transaction.getId());
            if (row > 0) {
                // 删除成功
                removeData(position);
                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
            } else {
                // 删除失败
                Toast.makeText(mContext, "删除失败，请重试", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // 异常处理
            Toast.makeText(mContext, "删除失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
