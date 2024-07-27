package main;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class BlackJack {

    //dealer variables
    Card hiddenCards;
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount;

    //player variables
    ArrayList<Card> playerHand;
    ArrayList<Card> splitHand;
    int playerSplitSum;
    int playerSum;
    int playerAceCount;
    int playerSplitAceCount;
    boolean isSplit;
    boolean firstHand;

    //window settings
    int width = 500;
    int height = 715;
    int cardHeight = 154;
    int cardWidth = 110;

    GamePanel gamePanel;

    //create instances for the frame, buttons and button panel
    JFrame frame = new JFrame("BlackJack");
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("hit");
    JButton stayButton = new JButton("stay");
    JButton resetButton = new JButton("reset");
    JButton splitButton = new JButton("split");

    ArrayList<Card> deck;
    Random random = new Random();

    //constructor
    public BlackJack() {

        start();

        frame.setVisible(true);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePanel);

        buttonPanel.add(splitButton);
        buttonPanel.add(resetButton);
        hitButton.setFocusable(false);
        buttonPanel.add(hitButton);
        stayButton.setFocusable(false);
        buttonPanel.add(stayButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        hitButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isSplit){
                    if(firstHand){
                        Card card = deck.remove(deck.size() - 1);
                        playerSum += card.getValue();
                        playerAceCount += card.isAce() ? 1 : 0;
                        playerHand.add(card);
                        gamePanel.repaint();
                        System.out.println("dealing to first hand");

                        if (reducePlayer()>21){
                            firstHand = false;
                            System.out.println("switching hands");
                            if (splitHand.size()>0){
                                hitButton.setEnabled(true);
                            }else hitButton.setEnabled(false);
                        }else hitButton.setEnabled(true);
                    }else{
                        Card card1 = deck.remove(deck.size() - 1);
                        playerSplitSum += card1.getValue();
                        playerSplitAceCount += card1.isAce() ? 1 : 0;
                        splitHand.add(card1);
                        gamePanel.repaint();
                        System.out.println("dealing to second hand");
                        if (reducePlayer2ndHand()>21){
                            hitButton.setEnabled(false);
                        }else hitButton.setEnabled(true);
                    }
                }else {

                    Card card = deck.remove(deck.size() - 1);
                    playerSum += card.getValue();
                    playerAceCount += card.isAce() ? 1 : 0;
                    playerHand.add(card);
                    gamePanel.repaint();
                    if (reducePlayer() > 21) {
                        hitButton.setEnabled(false);
                    }
                }
                gamePanel.repaint();
            }
        });

        stayButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {


                if (firstHand){
                    firstHand = false;
                    System.out.println("Stay first hand ");
                    // Disable hitting for the first hand
                    // Enable hitting for the second hand if it exists
                    if (splitHand.size() > 0) {
                        hitButton.setEnabled(true);
                        System.out.println("hit for second hand ");
                    }

                }else {
                    while (dealerSum < 17) {
                        Card card = deck.remove(deck.size() - 1);
                        dealerSum += card.getValue();
                        dealerAceCount += card.isAce() ? 1 : 0;
                        dealerHand.add(card);
                    }
                    stayButton.setEnabled(false);
                    hitButton.setEnabled(false);
                    gamePanel.repaint();
                }
            }
        });

        splitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                splitCards();
                isSplit = true;
                firstHand = true;
                splitButton.setEnabled(false);
                gamePanel.repaint();
            }
        });

        resetButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                start();

            }
        });

        gamePanel.repaint();
    }

    //methods
    public void start() {

        // Clear existing game state
        if (gamePanel != null) {
            frame.remove(gamePanel); // Remove the old game panel
        }

        stackDeck();
        shuffleTheDeck();

        //dealer
        dealerHand = new ArrayList<Card>();
        dealerSum = 0;
        dealerAceCount = 0;

        hiddenCards = deck.remove(deck.size() - 1); //draw card from top
        dealerSum += hiddenCards.getValue();
        dealerAceCount += hiddenCards.isAce() ? 1 : 0; //javas implementation of ternary operation

        //draw dealers second card
        Card card = deck.remove(deck.size() - 1);
        dealerSum += card.getValue();
        dealerAceCount += card.isAce() ? 1 : 0;
        dealerHand.add(card);

        //initialize player variables
        firstHand = false;
        isSplit=false;
        splitHand = new ArrayList<Card>();
        playerSplitSum = 0;
        playerSplitAceCount = 0;
        playerHand = new ArrayList<Card>();
        playerSum = 0;
        playerAceCount = 0;
        //Deals players hand
        for (int i = 0; i < 2; i++) {

            Card playerCard = deck.remove(deck.size() - 1);
            playerSum += playerCard.getValue();
            playerAceCount += playerCard.isAce() ? 1 : 0;
            playerHand.add(playerCard);
            System.out.println(playerAceCount);
        }
        //check for matching values
        if (playerHand.size() >= 2) {

            Card card1 = playerHand.get(0);
            Card card2 = playerHand.get(1);

            System.out.println("Comparing values: " + card1.getValue() + " and " + card2.getValue());

            if (card1.getValue() == card2.getValue()) {
                splitButton.setEnabled(true);
            } else {
                splitButton.setEnabled(false);
            }
        }
        // Reinitialize game panel
        gamePanel = new GamePanel(this);
        frame.add(gamePanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
        // Reset button state
        hitButton.setEnabled(true);
        stayButton.setEnabled(true);
        gamePanel.repaint();

    }

    public void shuffleTheDeck() {

        for (int i = 0; i < deck.size(); i++) {
            int n = random.nextInt(deck.size());
            Card curr = deck.get(i);
            Card randCard = deck.get(n);
            deck.set(i, randCard);
            deck.set(n, curr);
        }
        System.out.println("Deck has been shuffled!");
        System.out.println(deck);
    }

    public void stackDeck() {

        deck = new ArrayList<>();
        String[] values = {"A", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] types = {"C", "D", "S", "H"};

        for (String type : types) {
            for (String value : values) {
                Card card = new Card(value, type);
                deck.add(card);
            }
        }
        System.out.println("Building deck");
        System.out.println(deck);
    }

    public int reducePlayer() {

        while (playerSum > 21 && playerAceCount > 0) {
            playerSum -= 10;
            playerAceCount--;
        }

        return playerSum;
    }

    public int reduceDealer() {

        while (dealerSum > 21 && dealerAceCount > 0) {
            dealerSum -= 10;
            dealerAceCount--;
        }

        return dealerSum;
    }

    public void splitCards() {

        splitHand.add(playerHand.remove(playerHand.size()-1));
        playerAceCount -= splitHand.get(0).isAce() ? 1 : 0;
        playerSplitSum += splitHand.get(0).getValue();
        playerSplitAceCount += splitHand.get(0).isAce() ? 1 : 0;
        playerSum -= playerSplitSum;

    }

    public int reducePlayer2ndHand() {

        while (playerSplitSum > 21 && playerSplitAceCount > 0) {
            playerSplitSum -= 10;
            playerSplitAceCount--;
        }
        return playerSplitSum;
    }

}
