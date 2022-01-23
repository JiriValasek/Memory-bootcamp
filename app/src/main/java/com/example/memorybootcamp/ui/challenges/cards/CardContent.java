package com.example.memorybootcamp.ui.challenges.cards;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.Log;
import com.example.memorybootcamp.R;

import com.example.memorybootcamp.ui.challenges.faces.FaceContent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/** Class controlling content of a recycler view. It's still necessary to notify it to changes. */
public class CardContent {

    /** An array of card items. */
    public static final List<CardItem> ITEMS = new ArrayList<>();

    /** Method adding cards into a stack shown in a recycler view. */
    public static void loadCards(Context context, Cards cards, boolean fillInNames, boolean showFronts) {
        ITEMS.clear();
        for (Card card : cards.getCards()) {
            CardContent.CardItem newItem = new CardContent.CardItem();
            newItem.imageId = card.getPicture();
            newItem.cardName = new SpannableStringBuilder("");
            if (fillInNames) {
                newItem.cardName.append(card.getName());
            }
            if (!showFronts) {
                if (ITEMS.size() % 2 == 0) newItem.imageId = R.drawable.img_back_blue;
                else newItem.imageId = R.drawable.img_back_red;
            }
            CardContent.addItem(newItem);
        }
    }

    /** Method adding a single card to a recycler view. */
    private static int addItem(CardItem item) {
        if (!ITEMS.contains(item)) ITEMS.add(item);
        return ITEMS.indexOf(item);
    }

    /** A card item representing a piece of content. */
    public static class CardItem {
        public SpannableStringBuilder cardName;
        public int imageId;
    }
}