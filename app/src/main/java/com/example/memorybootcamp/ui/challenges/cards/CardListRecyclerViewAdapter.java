package com.example.memorybootcamp.ui.challenges.cards;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.memorybootcamp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CardListRecyclerViewAdapter extends RecyclerView.Adapter<CardListRecyclerViewAdapter.ViewHolder> {

    private final List<CardContent.CardItem> mValues;
    private final boolean editable;

    public CardListRecyclerViewAdapter(List<CardContent.CardItem> items, boolean editable) {
        mValues = items;
        this.editable = editable;
    }


    @Override
    public long getItemId(int position){return position;}

    public int getItemViewType(int position){return position;}

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.d("TEST B"," " + mValues.get(position).cardName);
        holder.mItem = mValues.get(position);
        Drawable d = holder.itemView.getResources().getDrawable(
                mValues.get(position).imageId, holder.itemView.getContext().getTheme() );
        holder.mImageView.setImageDrawable(d);
        holder.mCardNameView.setText(holder.mItem.cardName);
        prepareTextEdit(holder, editable, position);
        holder.mCardNameView.invalidate();
    }

    private void prepareTextEdit(final ViewHolder holder, boolean editable, int position){
        holder.mCardNameView.setFocusable(editable);
        holder.mCardNameView.setCursorVisible(editable);
        if (editable){
            holder.mCardNameView.setInputType( InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        }
        if(position == 0) {
            holder.mCardNameView.requestFocus();
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public List<CardContent.CardItem> getItems(){
        return mValues;
    }

    public void clearItems(Context context){
        mValues.clear();
        this.notifyItemRangeRemoved(0, getItemCount());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final EditText mCardNameView;
        public CardContent.CardItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.card_picture);
            mCardNameView = (EditText) view.findViewById(R.id.card_name);
            mCardNameView.addTextChangedListener(new TextWatcher(){

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (editable) {
                        Log.d( "MemoryBootcamp", +getAdapterPosition() + " " +
                                getBindingAdapterPosition() + " " + getAbsoluteAdapterPosition());
                        mValues.get(getAdapterPosition()).cardName = new SpannableStringBuilder("");
                        mValues.get(getAdapterPosition()).cardName.append(s.toString());
                    }
                }
            });
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString() + " '" + mCardNameView.getText() + "'";
        }
    }
}