package com.example.memorybootcamp.ui.challenges.faces.faceslist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.memorybootcamp.ui.challenges.faces.faceslist.face.FaceContent;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.example.memorybootcamp.ui.challenges.faces.faceslist.face.FaceContent.deleteSavedImages;

public class FaceListRecyclerViewAdapter extends RecyclerView.Adapter<FaceListRecyclerViewAdapter.ViewHolder> {

    private final List<FaceContent.FaceItem> mValues;
    private boolean editable;

    public FaceListRecyclerViewAdapter(List<FaceContent.FaceItem> items, boolean editable) {
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
                .inflate(R.layout.face, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.d("TEST B"," " + mValues.get(position).faceName);
        holder.mItem = mValues.get(position);
        Bitmap bm = BitmapFactory.decodeFile(holder.mItem.uri.getPath());
        holder.mImageView.setImageBitmap(bm);
        holder.mFaceNameView.setText(holder.mItem.faceName);
        prepareTextEdit(holder, editable, position);
        holder.mFaceNameView.invalidate();
    }

    private void prepareTextEdit(final ViewHolder holder, boolean editable, int position){
        holder.mFaceNameView.setFocusable(editable);
        holder.mFaceNameView.setCursorVisible(editable);
        if (editable){
            holder.mFaceNameView.setInputType( InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        }
        if(position == 0) {
            holder.mFaceNameView.requestFocus();
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public List<FaceContent.FaceItem> getItems(){
        return mValues;
    }

    public void clearItems(Context context){
        deleteSavedImages(context);
        mValues.clear();
        this.notifyItemRangeRemoved(0, getItemCount());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final EditText mFaceNameView;
        public FaceContent.FaceItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.face_picture);
            mFaceNameView = (EditText) view.findViewById(R.id.face_name);
            mFaceNameView.addTextChangedListener(new TextWatcher(){

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (editable) {
                        mValues.get(getAdapterPosition()).faceName = new SpannableStringBuilder("");
                        mValues.get(getAdapterPosition()).faceName.append(s.toString());
                    }
                }
            });
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString() + " '" + mFaceNameView.getText() + "'";
        }
    }
}