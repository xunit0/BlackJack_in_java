package main;
import java.awt.*;
import javax.swing.*;

public class GamePanel extends JPanel {
    private BlackJack game;

    public GamePanel(BlackJack game) {
        this.game = game;
        setLayout(new BorderLayout());
        setBackground(new Color(53, 101, 77));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {
            // Access the game instance for necessary data
            Image hiddenCardIMG = new ImageIcon(getClass().getResource("/res/cards/BACK.png")).getImage();
            if (!game.stayButton.isEnabled() && !game.firstHand) {
                hiddenCardIMG = new ImageIcon(getClass().getResource(game.hiddenCards.getImagePath())).getImage();
            }
            g.drawImage(hiddenCardIMG, 20, 20, game.cardWidth, game.cardHeight, null);

            // Draw dealer's hand
            for (int i = 0; i < game.dealerHand.size(); i++) {
                Card card = game.dealerHand.get(i);
                Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                g.drawImage(cardImg, game.cardWidth + 25 + (game.cardWidth + 5) * i, 20, game.cardWidth, game.cardHeight, null);
            }

            // Draw player's hand
            for (int i = 0; i < game.playerHand.size(); i++) {
                Card card1 = game.playerHand.get(i);
                Image cardImg = new ImageIcon(getClass().getResource(card1.getImagePath())).getImage();
                g.drawImage(cardImg, 20 + (game.cardWidth + 5) * i, 320, game.cardWidth, game.cardHeight, null);
            }

            // Draw the split hand if applicable
            if (game.isSplit) {
                for (int i = 0; i < game.splitHand.size(); i++) {
                    Card card2 = game.splitHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card2.getImagePath())).getImage();
                    g.drawImage(cardImg, 20 + (game.cardWidth + 5) * i, 480, game.cardWidth, game.cardHeight, null);
                }
            }

            // Draw the player's sum for the split hand if applicable
            if (game.isSplit) {
                g.setFont(new Font("Arial", Font.PLAIN, 15));
                g.setColor(Color.white);
                String playerSecondHand = "split hand: " + game.playerSplitSum;
                g.drawString(playerSecondHand, 375, 500);
            }

            // Draw the sum for the first hand
            g.setFont(new Font("Arial", Font.PLAIN, 15));
            g.setColor(Color.white);
            String sum = "Total: " + game.playerSum;
            g.drawString(sum, 400, 400);

            if (!game.hitButton.isEnabled()) {
                game.dealerSum = game.reduceDealer();
                game.playerSum = game.reducePlayer();
                System.out.println("STAY: ");
                System.out.println(game.dealerSum);
                System.out.println(game.playerSum);
                System.out.println(game.playerSplitSum);
                String message = "";
                if (game.isSplit) {
                    if (game.playerSum > 21 && game.playerSplitSum > 21) {
                        message += "You lose";
                    } else if (game.dealerSum > 21 && game.playerSum <= 21 && game.playerSplitSum <= 21) {
                        message += "You win";
                    } else if (game.dealerSum > game.playerSum && game.dealerSum > game.playerSplitSum) {
                        message += "You lose";
                    } else if (game.playerSum < game.dealerSum || game.playerSplitSum < game.dealerSum) {
                        message += "Break Even";
                    } else if (game.playerSum == game.dealerSum || game.playerSplitSum == game.dealerSum) {
                        message += "You lose half your bet";
                    }
                    else if (game.playerSum > game.dealerSum && game.playerSplitSum > game.dealerSum) {
                        message += "You win";
                    }
                } else {
                    if (game.playerSum > 21) {
                        message += "You lose";
                    } else if (game.dealerSum > 21) {
                        message += "You win";
                    } else if (game.dealerSum == game.playerSum) {
                        message += "Break Even";
                    } else if (game.playerSum > game.dealerSum) {
                        message += "You Win!";
                    } else if (game.playerSum < game.dealerSum) {
                        message += "You Lose";
                    }
                }
                g.setFont(new Font("Arial", Font.PLAIN, 30));
                g.setColor(Color.white);
                g.drawString(message, 220, 250);

                // Draw the dealer's sum
                g.setFont(new Font("Arial", Font.PLAIN, 15));
                g.setColor(Color.white);
                String dealerS = "The Dealer Has: " + game.dealerSum;
                g.drawString(dealerS, 300, 205);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
