package test;

import org.junit.Test;

import static test.BlackJackTest.Card.*;
import static org.junit.Assert.assertEquals;

public class BlackJackTest{
         
    @Test
    public void test_hand_value_with_one_card() {
        assertEquals(3, createHand(_3).value());
        assertEquals(10, createHand(_10).value());
        assertEquals(10, createHand(Jack).value());
        assertEquals(10, createHand(Queen).value());
        assertEquals(10, createHand(King).value());
        assertEquals(11, createHand(Ace).value());
    }
    
    @Test
    public void test_hand_value_with_two_cards() {
        assertEquals(8, createHand(_3, _5).value());
        assertEquals(19, createHand(_8, Ace).value());
    }
    
    @Test
    public void test_hand_value_with_three_cards(){
        assertEquals(20, createHand(_8, Queen, _2).value());
        assertEquals(18, createHand(_7, Jack, Ace).value());
    }
    
    @Test
    public void test_hand_is_black_jack() {
        assertEquals(false, createHand(_5, _2).isBlackJack());
        assertEquals(true, createHand(Ace, Queen).isBlackJack());
        assertEquals(false, createHand(Ace, _8, _2).isBlackJack());
    }
    
    @Test
    public void is_bust_with_two_cards(){
        assertEquals(false, createHand(_9, Queen).isBust());
        
    }
    
    @Test
    public void is_bust_with_three_cards(){
        assertEquals(false, createHand(_5, _2, _3).isBust());
        assertEquals(true, createHand(_5, Queen, Jack).isBust());
        assertEquals(false, createHand(Ace, Jack, King).isBust());
        
    }
    
    @Test
    public void is_bust_with_threen_cards(){
        assertEquals(true, createHand(Ace, Queen, King, _2).isBust());
    }

    private Hand createHand(Card... cards) {
        return new Hand() {

            @Override
            public int value() {
                return isStupid() ? sum() - 10 : sum();
            }

            private int sum() {
                int sum = 0;
                for (Card card : cards)
                    sum += card.value();
                return sum;
            }

            private boolean isStupid() {
                return isAce() && sum() > 21 ? true : false;
            }

            private boolean isAce() {
                for (Card card : cards)
                    if(card == Ace){
                        return true;
                    }
                return false;
            }
            
            @Override
            public boolean isBlackJack() {
                return sum() == 21 && cards.length == 2 ? true : false;
            }

            @Override
            public boolean isBust() {
                return value() <= 21 ? false : true;
            }

        };
    }

    public interface Hand {
        public int value();
        public boolean isBlackJack();
        public boolean isBust();

    }
    
    public enum Card {
        _2, _3, _4, _5, _6, _7, _8, _9, _10, Ace, Queen, King, Jack;
        
        private int value() {
            return isFace() ? 10 : ordinal() + 2;
        }
        
        private boolean isFace() {
            return this == King || this == Queen || this == Jack;
        }

    }
      
}
