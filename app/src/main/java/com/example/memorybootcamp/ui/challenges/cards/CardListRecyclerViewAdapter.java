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

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorybootcamp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/** Recycler view adapter holding cards. */
public class CardListRecyclerViewAdapter
        extends RecyclerView.Adapter<CardListRecyclerViewAdapter.ViewHolder> {

    /** List of card items to be shown in view holders. */
    private final List<CardContent.CardItem> mValues;
    /** Should editText be editable. */
    private final boolean editable;

    /** Constructor */
    public CardListRecyclerViewAdapter(List<CardContent.CardItem> items, boolean editable) {
        mValues = items;
        this.editable = editable;
    }

    /** Method preparing view holder. */
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);
        return new ViewHolder(view);
    }

    /** Method setting up content of a view holder. */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Drawable d = ResourcesCompat.getDrawable(holder.itemView.getResources(),
                mValues.get(position).imageId, holder.itemView.getContext().getTheme());
        holder.mImageView.setImageDrawable(d);
        holder.mCardNameView.setText(holder.mItem.cardName);
        prepareTextEdit(holder, editable, position);
        holder.mCardNameView.invalidate();
    }

    /** Method preparing text edit according to a mode. */
    private void prepareTextEdit(final ViewHolder holder, boolean editable, int position){
        holder.mCardNameView.setFocusable(editable);
        holder.mCardNameView.setCursorVisible(editable);
        if (editable){
            holder.mCardNameView.setInputType( InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        }
        if(position == 0) { holder.mCardNameView.requestFocus(); }
    }

    /** Method returning id of an element on a given position. */
    @Override
    public long getItemId(int position){return position;}
    /** Method returning number of items in a recycler view. */
    @Override
    public int getItemCount() { return mValues.size(); }
    /** Method returning type of a view on a given position. */
    public int getItemViewType(int position){return position;}

    /** Getter for items in a recycler view. */
    public List<CardContent.CardItem> getItems(){ return mValues; }

    /** Method for clearing items in a recycler view. */
    public void clearItems(Context context){
        mValues.clear();
        this.notifyItemRangeRemoved(0, getItemCount());
    }

    /** View holder for cards with listener for text changes. */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /** View to be held. */
        public final View mView;
        /** Image view with a card image. */
        public final ImageView mImageView;
        /** Edit text with name of a card. */
        public final EditText mCardNameView;
        /** Card item held in the holder. */
        public CardContent.CardItem mItem;

        /** Constructor */
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

                /** Listener recording changed text into the item. */
                @Override
                public void afterTextChanged(Editable s) {
                    if (editable) {
                        mValues.get(getAbsoluteAdapterPosition()).cardName =
                                new SpannableStringBuilder("");
                        mValues.get(getAbsoluteAdapterPosition()).cardName.append(s.toString());
                    }
                }
            });
        }

        /** Method retrieving name of a card. */
        @NotNull
        @Override
        public String toString() {
            return super.toString() + " '" + mCardNameView.getText() + "'";
        }
    }
}