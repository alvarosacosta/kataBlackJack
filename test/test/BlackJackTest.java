package test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;

import static test.BlackJackTest.Card.*;
import static org.junit.Assert.assertEquals;

public class BlackJackTest{
         
    @Test
    public void test_hand_value_with_one_card() {
        assertEquals(3, createHand("Player1", _3).value());
        assertEquals(10, createHand("Player1", _10).value());
        assertEquals(10, createHand("Player1", Jack).value());
        assertEquals(10, createHand("Player1", Queen).value());
        assertEquals(10, createHand("Player1", King).value());
        assertEquals(11, createHand("Player1", Ace).value());
    }
    
    @Test
    public void test_hand_value_with_two_cards() {
        assertEquals(8, createHand("Player1", _3, _5).value());
        assertEquals(19, createHand("Player1", _8, Ace).value());
    }
    
    @Test
    public void test_hand_value_with_three_cards(){
        assertEquals(20, createHand("Player1", _8, Queen, _2).value());
        assertEquals(18, createHand("Player1", _7, Jack, Ace).value());
    }
    
    @Test
    public void test_hand_is_black_jack() {
        assertEquals(false, createHand("Player1", _5, _2).isBlackJack());
        assertEquals(true, createHand("Player1", Ace, Queen).isBlackJack());
        assertEquals(false, createHand("Player1", Ace, _8, _2).isBlackJack());
    }
    
    @Test
    public void is_bust_with_two_cards(){
        assertEquals(false, createHand("Player1", _9, Queen).isBust());
        
    }
    
    @Test
    public void is_bust_with_three_cards(){
        assertEquals(false, createHand("Player1", _5, _2, _3).isBust());
        assertEquals(true, createHand("Player1", _5, Queen, Jack).isBust());
        assertEquals(false, createHand("Player1", Ace, Jack, King).isBust());
        
    }
    
    @Test
    public void is_bust_with_four_cards(){
        assertEquals(true, createHand("Player1",Ace, Queen, King, _2).isBust());
    }
    
    @Test
    public void who_wins(){
        
        Assert.assertArrayEquals(new String[]{"Player1"}, createGame(createDeck(_5, _4, King, _2), createHand("Player1", Jack, Ace), 
                createHand("Player2", _10, _5, _6), createHand("Player3", _3, _6, Ace, _3, Ace, King), 
                createHand("Croupier", _9, _7)).getWinners());
        
        Assert.assertArrayEquals(new String[]{"Player1", "Player3"}, createGame(createDeck(Ace, _3, King, _2), createHand("Player1", _10, King), 
                createHand("Player2", _10, _2, _6), createHand("Player3", _8, _8, _5), 
                createHand("Croupier", _5, _10)).getWinners());
    }

    private Hand createHand(String name, Card... cards) {
        return new Hand() {

            @Override
            public int value() {
                return canUseExtendAce() ? sum() + 10 : sum();
            }
            
            @Override
            public int sum() {
                int sum = 0;
                for (Card card : cards)
                    sum += card.value();
                return sum;
            }
            
            @Override
            public boolean canUseExtendAce() {
                return isAce() && sum() <= 11 ? true : false;
            }
            
            @Override
            public boolean isAce() {
                for (Card card : cards)
                    if(card == Ace){
                        return true;
                    }
                return false;
            }
            
            @Override
            public boolean isBlackJack() {
                return value() == 21 && cards.length == 2 ? true : false;
            }

            @Override
            public boolean isBust() {
                return value() <= 21 ? false : true;
            }
            
            @Override
            public String name(){
                return name;
            }

        };
    }

    public interface Hand {
        public int value();
        public boolean isBlackJack();
        public boolean isBust();
        public String name();
        public boolean isAce();
        public boolean canUseExtendAce();
        public int sum();

    }
    
    public enum Card {
        Ace, _2, _3, _4, _5, _6, _7, _8, _9, _10, Queen, King, Jack;
        
        private int value() {
            return isFace() ? 10 : ordinal() + 1;
        }
        
        private boolean isFace() {
            return this == King || this == Queen || this == Jack;
        }

    }
    
    private Game createGame(Deck deck, Hand... players){
        return new Game(){
            
            @Override
            public String[] getWinners() {
                ArrayList<String> winners = new ArrayList<>();
                        
                if (croupierHasBlackJack()){
                    return new String[]{"Croupier"};
                }
                
                for (Hand player : players) {
                    if ((player.value() > getCroupierGameValue() && player.value() <= 21) || player.isBlackJack())
                        winners.add(player.name());
                }
                
                String[] result = new String[winners.size()];
                return winners.toArray(result);
                
            }
            
            @Override
            public Hand getCroupier() {
                for (Hand hand : players) {
                    if (hand.name().equals("Croupier")) {
                        return hand;
                    }
                }
                return null;
            }
            
            @Override
            public boolean croupierHasBlackJack() {
                return getCroupier().isBlackJack() ? true : false;
            }
            
            @Override
            public int getCroupierGameValue(){
                int value = getCroupier().value();
                
                
                while (value < 17){
                    Card newc = deck.getOneCard();
                    
                    if (newc == Ace && value <= 10)
                        value = value + newc.value() + 10;
                    else
                        value = value + newc.value();
                        
                }
                
                deck.reload();
                return value;
                
            }
            
        };
    }
    
    public interface Game {
        public String[] getWinners();
        public boolean croupierHasBlackJack();
        public int getCroupierGameValue();
        public Hand getCroupier();
    }
    
    private Deck createDeck(Card... cards){
        return new Deck(){
            int helper = 0;
            
            @Override
            public Card getOneCard() {
                Card result = cards[helper];
                helper++;
                
                return result;
            }

            @Override
            public void reload() {
                helper = 0;
            }
             
        };
    }
    
    public interface Deck {
        public void reload();
        public Card getOneCard();
    }
      
}
