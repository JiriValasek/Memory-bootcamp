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
            new Card("♣A", R.drawable.img_clubs_ace),
            new Card("♣2", R.drawable.img_clubs_2),
            new Card("♣3", R.drawable.img_clubs_3),
            new Card("♣4", R.drawable.img_clubs_4),
            new Card("♣5", R.drawable.img_clubs_5),
            new Card("♣6", R.drawable.img_clubs_6),
            new Card("♣7", R.drawable.img_clubs_7),
            new Card("♣8", R.drawable.img_clubs_8),
            new Card("♣9", R.drawable.img_clubs_9),
            new Card("♣10", R.drawable.img_clubs_10),
            new Card("♣J", R.drawable.img_clubs_jack),
            new Card("♣Q", R.drawable.img_clubs_queen),
            new Card("♣K", R.drawable.img_clubs_king),
            new Card("♦A", R.drawable.img_diamonds_ace),
            new Card("♦2", R.drawable.img_diamonds_2),
            new Card("♦3", R.drawable.img_diamonds_3),
            new Card("♦4", R.drawable.img_diamonds_4),
            new Card("♦5", R.drawable.img_diamonds_5),
            new Card("♦6", R.drawable.img_diamonds_6),
            new Card("♦7", R.drawable.img_diamonds_7),
            new Card("♦8", R.drawable.img_diamonds_8),
            new Card("♦9", R.drawable.img_diamonds_9),
            new Card("♦10", R.drawable.img_diamonds_10),
            new Card("♦J", R.drawable.img_diamonds_jack),
            new Card("♦Q", R.drawable.img_diamonds_queen),
            new Card("♦K", R.drawable.img_diamonds_king),
            new Card("♥A", R.drawable.img_hearts_ace),
            new Card("♥2", R.drawable.img_hearts_2),
            new Card("♥3", R.drawable.img_hearts_3),
            new Card("♥4", R.drawable.img_hearts_4),
            new Card("♥5", R.drawable.img_hearts_5),
            new Card("♥6", R.drawable.img_hearts_6),
            new Card("♥7", R.drawable.img_hearts_7),
            new Card("♥8", R.drawable.img_hearts_8),
            new Card("♥9", R.drawable.img_hearts_9),
            new Card("♥10", R.drawable.img_hearts_10),
            new Card("♥J", R.drawable.img_hearts_jack),
            new Card("♥Q", R.drawable.img_hearts_queen),
            new Card("♥K", R.drawable.img_hearts_king),
            new Card("♠A", R.drawable.img_spades_ace),
            new Card("♠2", R.drawable.img_spades_2),
            new Card("♠3", R.drawable.img_spades_3),
            new Card("♠4", R.drawable.img_spades_4),
            new Card("♠5", R.drawable.img_spades_5),
            new Card("♠6", R.drawable.img_spades_6),
            new Card("♠7", R.drawable.img_spades_7),
            new Card("♠8", R.drawable.img_spades_8),
            new Card("♠9", R.drawable.img_spades_9),
            new Card("♠10", R.drawable.img_spades_10),
            new Card("♠J", R.drawable.img_spades_jack),
            new Card("♠Q", R.drawable.img_spades_queen),
            new Card("♠K", R.drawable.img_spades_king)};

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
        int id = (int) Math.floor((52 + 1) * Math.random());
        return this.cardDict[id];
    }
}
