package com.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    // 儲存要顯示的資料。
    private List<String> mListString;

    // ViewHolder 是把項目中所有的 View 物件包起來。
    // 它在 onCreateViewHolder() 中使用。
    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public ImageView mImgView;
        public TextView mTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            mImgView = (ImageView) itemView.findViewById(R.id.imgView);
            mTxt = (TextView) itemView.findViewById(R.id.txt);

            // 處理按下的事件。
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // 按下後執行的程式碼。
            Toast.makeText(view.getContext(),
                    mListString.get(getAdapterPosition()), Toast.LENGTH_LONG)
                    .show();
        }
    }

    // 建構式，用來接收外部程式傳入的項目資料。
    public ItemAdapter(List<String> listString) {
        mListString = listString;
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 建立一個 view。
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recycler_view_item, parent, false);

        // 建立這個 view 的 ViewHolder。
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position) {
        // 把資料設定給 ViewHolder。
        holder.mImgView.setImageResource(android.R.drawable.star_on);
        holder.mTxt.setText(mListString.get(position));
    }

    @Override
    public int getItemCount() {
        return mListString.size();
    }
}
