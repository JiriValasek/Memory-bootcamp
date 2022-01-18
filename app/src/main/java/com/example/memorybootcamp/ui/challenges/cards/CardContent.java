package com.example.memorybootcamp.ui.challenges.cards;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.SpannableStringBuilder;
import android.util.Log;

import com.example.memorybootcamp.ui.challenges.faces.FaceContent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CardContent {

    /**
     * An array of card items.
     */
    public static final List<CardItem> ITEMS = new ArrayList<>();


    public static void loadCards(Context context, Cards cards, boolean fillInNames) {
        for (Card card : cards.getCards()) {
            CardContent.CardItem newItem = new CardContent.CardItem();
            newItem.imageId = card.getPicture();
            newItem.cardName = new SpannableStringBuilder("");
            if (fillInNames) {
                newItem.cardName.append(card.getName());
            }
            CardContent.addItem(newItem);
        }
    }

    private static int addItem(CardItem item) {
        if (!ITEMS.contains(item)) ITEMS.add(item);
        return ITEMS.indexOf(item);
    }

    /**
     * A card item representing a piece of content.
     */
    public static class CardItem {
        public SpannableStringBuilder cardName;
        public int imageId;
    }
}