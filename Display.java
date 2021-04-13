/**
 * Author: xus83
 * Revised: April 11, 2021
 *
 * Description: view module
 */

package src;

import java.awt.event.*;
import java.awt.Color;
import java.awt.Font;
import java.io.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.JTextField;


public class Display extends JFrame{
    protected JPanel BoardPanel;
    protected JPanel WinLosePanel;
    protected JPanel ScorePanel;
    protected JPanel HighestPanel;
    protected JPanel NamePanel;
    protected JButton UndoButton;
    protected JButton NewButton;
    protected JButton helpButton;
    protected JButton soButton;
    protected JTextField ScoreField;
    protected JTextField HightestField;
    protected JLabel[][] slots;
    protected JLabel scoreLabel;
    protected JLabel highestLabel;
    protected JLabel nameLabel;
    protected JLabel winloseLabel;
    protected Font font;
    protected Font nameFont;
    protected GameBoard theBoard;
    protected int[][] currBoard;

    public Display() throws InterruptedException {
        super();
        setTitle("2048 by xus83");
        setResizable(false);
        getContentPane().setLayout(null);
        setBounds(700, 20, 500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(new Color(0xFAF8EF));

        font = new Font("", Font.BOLD, 30);
        nameFont = new Font("", Font.BOLD, 60);

        BoardPanel = new JPanel();
        BoardPanel.setBounds(17, 142, 450, 450);
        getContentPane().add(BoardPanel);
        BoardPanel.setLayout(null);

        WinLosePanel = new JPanel();
        WinLosePanel.setBounds(0,20,500,650);
        getContentPane().add(WinLosePanel);
        WinLosePanel.setVisible(false);
        winloseLabel = new JLabel("");
        winloseLabel.setFont(new Font("", Font.BOLD, 25));
        WinLosePanel.add(winloseLabel);
        soButton = new JButton("Start Over");
        soButton.setBackground(new Color(0xFAF8EF));
        soButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WinLosePanel.setVisible(false);
                theBoard = new GameBoard();
                resetSlots();
                UndoButton.setEnabled(true);
                NewButton.setEnabled(true);
                ScoreField.setVisible(true);
                HightestField.setVisible(true);
                ScoreField.requestFocus();
            }
        });
        WinLosePanel.add(soButton);

        ScorePanel = new JPanel();
        ScorePanel.setBounds(250, 20, 100, 40);
        getContentPane().add(ScorePanel);
        HighestPanel = new JPanel();
        HighestPanel.setBounds(360, 20, 100, 40);
        getContentPane().add(HighestPanel);
        scoreLabel = new JLabel("score");
        ScorePanel.add(scoreLabel);
        highestLabel = new JLabel("highest");
        HighestPanel.add(highestLabel);
        ScoreField = new JTextField("0");
        ScoreField.setBounds(80, 40, 100, 20);
        ScoreField.setEditable(false);
        ScorePanel.add(ScoreField);
        try {
            BufferedReader in = new BufferedReader(new FileReader("2048.sav"));
            String str;
            if ((str = in.readLine()) != null) {
                HightestField = new JTextField(str);
            }else{
                HightestField = new JTextField("0");
            }
        } catch (IOException e) {
            HightestField = new JTextField("0");
        }
        HightestField.setBounds(80, 40, 100, 20);
        HightestField.setEditable(false);
        HighestPanel.add(HightestField);

        UndoButton = new JButton("undo");
        UndoButton.setBounds(250, 80, 100, 40);
        UndoButton.setBackground(new Color(0xFAF8EF));
        getContentPane().add(UndoButton);
        NewButton = new JButton("new game");
        NewButton.setBounds(360, 80, 100, 40);
        NewButton.setBackground(new Color(0xFAF8EF));
        getContentPane().add(NewButton);
        UndoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theBoard.undo();
                resetSlots();
            }
        });
        NewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theBoard = new GameBoard();
                resetSlots();
            }
        });

        NamePanel = new JPanel();
        NamePanel.setBounds(20, 20, 200, 150);
        getContentPane().add(NamePanel);
        nameLabel = new JLabel("2048");
        nameLabel.setFont(nameFont);
        NamePanel.add(nameLabel);
        helpButton = new JButton("?");
        helpButton.setFont(font);
        helpButton.setEnabled(false);
        helpButton.setBackground(null);
        helpButton.setBorder(null);
        helpButton.setToolTipText("Use wasd or ↑←↓→ to merge same numbers. You win if you got 2048, good luck & have fun!");
        NamePanel.add(helpButton);

        theBoard = new GameBoard();
        currBoard = theBoard.getBoard();

        slots = new JLabel[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (currBoard[j][i] != 0) {
                    slots[i][j] = new JLabel(String.valueOf(currBoard[j][i]));
                } else {
                    slots[i][j] = new JLabel("");
                }
                slots[i][j].setFont(font);
                slots[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                slots[i][j].setBounds(120 * i, 120 * j, 90, 90);
                slots[i][j].setOpaque(true);
                slots[i][j].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
                reColor(i, j, String.valueOf(currBoard[j][i]));
                BoardPanel.add(slots[i][j]);
            }
        }

        class keyboardListener extends KeyAdapter{
            @Override
            public void keyPressed(KeyEvent e) {
                keyboardEvent(e);
                if (theBoard.getHaveWon()) {
                    WinLosePanel.setVisible(true);
                    winloseLabel.setText("Nice Move! Congrats!");
                    UndoButton.setEnabled(false);
                    NewButton.setEnabled(false);
                    ScoreField.setVisible(false);
                    HightestField.setVisible(false);
                } else if (!theBoard.getStatus()) {
                    WinLosePanel.setVisible(true);
                    winloseLabel.setText("Bad Move! Better Luck Next Time!");
                    UndoButton.setEnabled(false);
                    NewButton.setEnabled(false);
                    ScoreField.setVisible(false);
                    HightestField.setVisible(false);
                }
            }
        }
        ScoreField.addKeyListener(new keyboardListener());
        HightestField.addKeyListener(new keyboardListener());
        UndoButton.addKeyListener(new keyboardListener());
        NewButton.addKeyListener(new keyboardListener());
    }

    private void keyboardEvent(KeyEvent e) {
        int input = e.getKeyCode();
        switch(input){
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                theBoard.left();
                resetSlots();
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                theBoard.right();
                resetSlots();
                break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                theBoard.up();
                resetSlots();
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                theBoard.down();
                resetSlots();
                break;
        }
    }

    private void resetSlots() {
        currBoard = theBoard.getBoard();
        for(int i = 0; i < 4 ; i++) {
            for (int j = 0; j < 4; j++) {
                if(currBoard[j][i]!=0) {
                    slots[i][j].setText(String.valueOf(currBoard[j][i]));
                }else{
                    slots[i][j].setText("");
                }
                reColor(i,j, String.valueOf(currBoard[j][i]));
            }
        }
        ScoreField.setText(String.valueOf(theBoard.getScore()));
        if(Integer.parseInt(HightestField.getText())<theBoard.getScore()){
            HightestField.setText(String.valueOf(theBoard.getScore()));
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter("2048.sav"));
                out.write(String.valueOf(theBoard.getScore()));
                out.close();
            }catch(IOException e){
                assert true;
            }
        }
    }

    private void reColor(int i, int j, String s){
        if(s.equals("0")){
            slots[i][j].setBackground(new Color(0xFAF8EF));
        }else{
            Color currColor = new Color(0xFAF8EF);
            for(int k = 0; k < Integer.parseInt(s)/2 - 1; k++){
                currColor=currColor.darker();
            }
            slots[i][j].setBackground(currColor);
        }
    }
}
