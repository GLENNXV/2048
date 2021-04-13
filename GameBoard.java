/**
 * Author: xus83
 * Revised: April 11, 2021
 *
 * Description: game state module
 */

package src;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @brief class GameBoard simulates the game state
 * @details class GameBoard have properties like Indicators, Learning Outcomes and name.
 */
public class GameBoard{
    protected int score;
    protected int lastScore;
    protected boolean status;
    protected boolean haveWon;
    protected int[][] board;
    protected int[][] lastStep;

    /**
     * @brief the getter
     * @details get the name
     * @return String name
     */
    public int getScore(){
        return score;
    }

    /**
     * @brief the getter
     * @details get the name
     * @return String name
     */
    public boolean getStatus(){
        return status;
    }

    /**
     * @brief the getter
     * @details get the name
     * @return String name
     */
    public boolean getHaveWon(){
        return haveWon;
    }

    /**
     * @brief the getter
     * @details get the name
     * @return String name
     */
    public int[][] getBoard(){
        return board;
    }

    /**
     * @brief the construct method
     * @details initialize the objects
     * @param String courseName
     * @param IndicatorT[] indicators
     * @return CourseT object
     */
    public GameBoard(){
        score = 0;
        status = true;
        haveWon = false;
        board = new int[4][4];
        for(int i = 0; i < 4; i++){
            board[i] = new int[]{0,0,0,0};
        }
        for(int i = 0; i < 2; i++){
            int num = (int) (Math.random() / 0.0625);
            int x = 0;
            int y;
            while (num >= 4) {
                x++;
                num -= 4;
            }
            y = num;
            if(board[x][y]==0){
                board[x][y] = 2;
            }else{
                board[y][x] = 2;
            }
        }
        lastScore = 0;
        lastStep = new int[4][4];
        for(int i = 0; i < 4; i++){
            lastStep[i] = Arrays.copyOf(board[i],4);
        }
    }

    private void haveLost(){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++) {
                if(board[i][j]==0){
                    return;
                }else{
                    try{
                       if(board[i][j]==board[i][j+1] || board[i][j]==board[i+1][j]){
                           return;
                       }else{
                           reverseUp();
                           if(board[i][j]==board[i][j+1] || board[i][j]==board[i+1][j]) {
                               reverseUp();
                               return;
                           }
                           reverseUp();
                       }
                    }catch(IndexOutOfBoundsException e){
                        assert true;
                    }
                }
            }
        }
        status = false;
    }

    private void addRandom(){
        while(true) {
            int num = (int) (Math.random() / 0.0625);
            int x = 0;
            int y;
            while (num >= 4) {
                x++;
                num -= 4;
            }
            y = num;
            if (board[x][y] == 0) {
                board[x][y] = 2;
                break;
            }
        }
    }

    private void storeLast(){
        lastScore = score;
        for(int i = 0; i < 4; i++){
            lastStep[i] = Arrays.copyOf(board[i],4);
        }
    }

    private boolean canLeft(){
        if(!status){
            return false;
        }
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if(board[i][j]==0){
                    for(int k = j + 1; k < 4; k++){
                        if(board[i][k]!=0){
                            return true;
                        }
                    }
                }else{
                    try{
                        if (board[i][j] == board[i][j + 1]){
                            return true;
                        }
                    }catch(IndexOutOfBoundsException e){
                        assert true;
                    }
                }
            }
        }
        return false;
    }

    private void goLeft(){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if(board[i][j]==0){
                    for(int k = j + 1; k < 4; k++){
                        if(board[i][k]!=0) {
                            board[i][j] = board[i][k];
                            board[i][k] = 0;
                            break;
                        }
                    }
                }
            }
        }
    }

    private void toLeft(){
        if(canLeft()) {
            goLeft();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    try {
                        if (board[i][j] == board[i][j + 1] && board[i][j] != 0) {
                            board[i][j] = 2 * board[i][j];
                            board[i][j + 1] = 0;
                            score += board[i][j];
                            if(2 * board[i][j]==2048){
                                haveWon = true;
                                status = false;
                            }
                        }
                    } catch (IndexOutOfBoundsException e) {
                        break;
                    }
                }
            }
            goLeft();
            addRandom();
            haveLost();
        }
    }

    public void left(){
        storeLast();
        toLeft();
    }

    private void reverseRight(){
        int temp;
        for(int i = 0; i < 4; i++){
            temp = board[i][0];
            board[i][0] = board[i][3];
            board[i][3] = temp;
            temp = board[i][1];
            board[i][1] = board[i][2];
            board[i][2] = temp;
        }
    }

    public void right(){
        storeLast();
        reverseRight();
        toLeft();
        reverseRight();
    }

    private void reverseUp(){
        int temp;
        for(int i = 0; i < 4; i++){
            for(int j = i; j < 4; j++){
                if(i!=j){
                    temp = board[i][j];
                    board[i][j] = board[j][i];
                    board[j][i] = temp;
                }
            }
        }
    }

    private void toUp(){
        reverseUp();
        toLeft();
        reverseUp();
    }

    public void up(){
        storeLast();
        toUp();
    }

    private void reverseDown(){
        int[] temp;
        temp = Arrays.copyOf(board[0],4);
        board[0] = Arrays.copyOf(board[3],4);
        board[3] = Arrays.copyOf(temp,4);
        temp = Arrays.copyOf(board[1],4);
        board[1] = Arrays.copyOf(board[2],4);
        board[2] = Arrays.copyOf(temp,4);

    }

    public void down(){
        storeLast();
        reverseDown();
        toUp();
        reverseDown();
    }

    /**
     * @brief the setter
     * @details add more outcomes for corresponding indicator
     * @param LOsT outcome
     * @param IndicatorT[] indicators
     */
    public void undo(){
        score = lastScore;
        for(int i = 0; i < 4; i++){
            board[i] = Arrays.copyOf(lastStep[i],4);
        }
    }

    public static void main(String[] args){
        GameBoard elsb = new GameBoard();
        for(int[] a :elsb.getBoard()){
            System.out.println(Arrays.toString(a));
        }
        try{
            System.out.println(elsb.board[4][5]);
        }catch(IndexOutOfBoundsException e){
            assert true;
        }
        Scanner scan = new Scanner(System.in);
        while(true) {
            if (scan.hasNext()) {
                elsb.up();
                for(int[] a :elsb.getBoard()){
                    System.out.println(Arrays.toString(a));
                }
                elsb.undo();
                System.out.println("");
                for(int[] a :elsb.getBoard()){
                    System.out.println(Arrays.toString(a));
                }
                scan.next();
                //scan.close();
            }
        }
    }
}
