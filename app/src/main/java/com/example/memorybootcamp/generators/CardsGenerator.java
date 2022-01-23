package com.example.memorybootcamp.generators;

import com.example.memorybootcamp.ui.challenges.cards.Card;
import com.example.memorybootcamp.R;
import com.example.memorybootcamp.ui.challenges.cards.Cards;

import java.util.ArrayList;
import java.util.List;

/** Generator for simplified cards from https://tekeye.uk/playing_cards/svg-playing-cards. */
public class CardsGenerator {

    /** Array of all available cards working as a card stack to randomly draw from. */
    private final Card[] cardDict = {
            new Card("clubs A", R.drawable.img_clubs_ace),
            new Card("clubs 2", R.drawable.img_clubs_2),
            new Card("clubs 3", R.drawable.img_clubs_3),
            new Card("clubs 4", R.drawable.img_clubs_4),
            new Card("clubs 5", R.drawable.img_clubs_5),
            new Card("clubs 6", R.drawable.img_clubs_6),
            new Card("clubs 7", R.drawable.img_clubs_7),
            new Card("clubs 8", R.drawable.img_clubs_8),
            new Card("clubs 9", R.drawable.img_clubs_9),
            new Card("clubs 10", R.drawable.img_clubs_10),
            new Card("clubs J", R.drawable.img_clubs_jack),
            new Card("clubs Q", R.drawable.img_clubs_queen),
            new Card("clubs K", R.drawable.img_clubs_king),
            new Card("diamonds A", R.drawable.img_diamonds_ace),
            new Card("diamonds 2", R.drawable.img_diamonds_2),
            new Card("diamonds 3", R.drawable.img_diamonds_3),
            new Card("diamonds 4", R.drawable.img_diamonds_4),
            new Card("diamonds 5", R.drawable.img_diamonds_5),
            new Card("diamonds 6", R.drawable.img_diamonds_6),
            new Card("diamonds 7", R.drawable.img_diamonds_7),
            new Card("diamonds 8", R.drawable.img_diamonds_8),
            new Card("diamonds 9", R.drawable.img_diamonds_9),
            new Card("diamonds 10", R.drawable.img_diamonds_10),
            new Card("diamonds J", R.drawable.img_diamonds_jack),
            new Card("diamonds Q", R.drawable.img_diamonds_queen),
            new Card("diamonds K", R.drawable.img_diamonds_king),
            new Card("hearts A", R.drawable.img_hearts_ace),
            new Card("hearts 2", R.drawable.img_hearts_2),
            new Card("hearts 3", R.drawable.img_hearts_3),
            new Card("hearts 4", R.drawable.img_hearts_4),
            new Card("hearts 5", R.drawable.img_hearts_5),
            new Card("hearts 6", R.drawable.img_hearts_6),
            new Card("hearts 7", R.drawable.img_hearts_7),
            new Card("hearts 8", R.drawable.img_hearts_8),
            new Card("hearts 9", R.drawable.img_hearts_9),
            new Card("hearts 10", R.drawable.img_hearts_10),
            new Card("hearts J", R.drawable.img_hearts_jack),
            new Card("hearts Q", R.drawable.img_hearts_queen),
            new Card("hearts K", R.drawable.img_hearts_king),
            new Card("spades A", R.drawable.img_spades_ace),
            new Card("spades 2", R.drawable.img_spades_2),
            new Card("spades 3", R.drawable.img_spades_3),
            new Card("spades 4", R.drawable.img_spades_4),
            new Card("spades 5", R.drawable.img_spades_5),
            new Card("spades 6", R.drawable.img_spades_6),
            new Card("spades 7", R.drawable.img_spades_7),
            new Card("spades 8", R.drawable.img_spades_8),
            new Card("spades 9", R.drawable.img_spades_9),
            new Card("spades 10", R.drawable.img_spades_10),
            new Card("spades J", R.drawable.img_spades_jack),
            new Card("spades Q", R.drawable.img_spades_queen),
            new Card("spades K", R.drawable.img_spades_king)};

    /** Method generating sequence of random cards from the stack. */
    public Cards generateSequence(int length) {
        Cards sequence = new Cards();
        List<Card> cards = new ArrayList<>();
        for(int i=0; i<length; i++){
            cards.add(this.generateCard());
        }
        sequence.setCards(cards);
        return sequence;
    }

    /** Method generating one random card from the stack. */
    public Card generateCard() {
        int id = (int) Math.floor(52 * Math.random());
        return this.cardDict[id];
    }
}
