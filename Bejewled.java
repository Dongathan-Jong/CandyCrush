import java.awt.Color;
import java.io.*;
import java.util.*;

public class Bejeweled 
{
   final Color COLOUR_DELETE = Color.RED;
   final Color COLOUR_SELECT = Color.YELLOW;

   BejeweledGUI gui;
   
   final int EMPTY = -1;
   final int CHAIN_REQ = 3;
   final int NUMROW = 8, NUMCOL = 8;
   final String GAMEFILEFOLDER = "gamefiles";
   boolean firstSelection;
   boolean chainMade, adjacentChainMade;
   int[][] board;
   int slot1row = -1, slot1col = -1;
   int score;     
   int scoreTemp;
   int movesLeft;
   Random rn = new Random();
 
 
	
      public Bejeweled(BejeweledGUI gui) {
      this.gui = gui;  
      start();   
   }

   public void start() {
      board = new int[NUMROW][NUMCOL];
      firstSelection = true;
      score = 0;
      scoreTemp = 0;
      movesLeft = 10;
      
      initBoard();
      updateBoard();
      updateGameBoard();
   }


  public void play (int row, int column) 
   {
      
         
      if (firstSelection) 
      {
         markPiece(row, column);
         slot1row = row;
         slot1col = column;
         firstSelection = false;
         chainMade = false;
         adjacentChainMade = false;
      }
      else if (adjacentSlots(slot1row, slot1col, row, column))
      {
         
         if (slot1row == row && slot1col == column) 
         {
            unmarkPiece(row,column);
            unmarkPiece(slot1row, slot1col);
            gui.showInvalidMoveMessage();
         }
         
         if (adjacentSlots(slot1row, slot1col, row, column)) 
         {
            swapPiecesGUI(slot1row, slot1col, row, column);
            swapPieces(slot1row, slot1col, row, column);
         }
         if (countLeft(row,column) + countRight(row,column) > CHAIN_REQ) 
         {
            scoreTemp = score;
            score += (countLeft(row,column) + countRight(row,column))-1;
            
            unmarkPiece(row,column);
            unmarkPiece(slot1row, slot1col);
            markDeletePieceLeft(row,column,(countLeft(row,column)));
            markDeletePieceRight(row,column,(countRight(row,column)));
            
            chainMade = true;
         }

         
         if (countLeft(slot1row,slot1col) + countRight(slot1row, slot1col) > CHAIN_REQ) 
         {
            scoreTemp = score;
            score += (countLeft(slot1row, slot1col) + countRight(slot1row, slot1col)) - 1;
            //scoreTemp = scoreTemp + (countLeft(slot1row, slot1col) + countRight(slot1row, slot1col)) - 1;
            unmarkPiece(row,column);
            unmarkPiece(slot1row, slot1col);
            markDeletePieceLeft(slot1row,slot1col, countLeft(slot1row,slot1col));
            markDeletePieceRight(slot1row,slot1col, countRight(slot1row,slot1col));
            
            adjacentChainMade = true;
         }
                  
         
         if (countUp(slot1row,slot1col) + countDown(slot1row, slot1col) > CHAIN_REQ) 
         {
            scoreTemp = score;
            score += (countUp(slot1row, slot1col) + countDown(slot1row, slot1col)) - 1;
            //scoreTemp = scoreTemp + (countUp(slot1row, slot1col) + countDown(slot1row, slot1col)) - 1;
            unmarkPiece(row,column);
            unmarkPiece(slot1row, slot1col);
            markDeletePieceUp(slot1row,slot1col, countUp(slot1row,slot1col));
            markDeletePieceDown(slot1row,slot1col, countDown(slot1row,slot1col));
         
            adjacentChainMade = true;
         }
         
         
         if (countUp(row,column) + countDown(row,column) > CHAIN_REQ) 
         {
            scoreTemp = score;
            score += (countUp(row,column) + countDown(row,column))-1;
            //scoreTemp = scoreTemp + (countUp(row, column) + countDown(row, column)) - 1;
            
            markDeletePieceUp(row,column,countUp(row,column));
            markDeletePieceDown(row,column,countDown(row,column));
         
            chainMade = true;
            
         }
         
         if (chainMade == true) 
         {
            unmarkPiece(row,column);
            unmarkPiece(slot1row, slot1col);
            markDeletePiece(row,column);
            gui.showChainSizeMessage(score - scoreTemp);
         }
         if(adjacentChainMade == true) 
         {
            unmarkPiece(row,column);
            unmarkPiece(slot1row, slot1col);
            markDeletePiece(slot1row, slot1column);
            gui.showChainSizeMessage(score - scoreTemp);
         }
         if(adjacentChainMade == true || chainMade == true) 
         {
            movesLeft--;
         }
         
         if (chainMade == false && adjacentChainMade == false) 
         {
            swapPiecesGUI(slot1row, slot1col, row, column);
            swapPieces(slot1row, slot1col, row, column);
         }
         
         updateBoard();
         updateGameBoard();
         
         
         firstSelection = true;
      
      }
      else 
      {
         
         unmarkPiece(row,column);
         unmarkPiece(slot1row, slot1col);
         
         
         firstSelection = true;
      }
   
   }
public void updateGameBoard() 
   {
      for (int i = 0; i < NUMROW; i++) {
         for (int j = 0; j < NUMCOL; j++) {
            gui.setPiece(i,j, board[i][j]);
         }
      }
   }

   public void endGame() 
   {
      gui.showGameOverMessage(score, 10 - movesLeft);
   }
   
   public void swapPiecesGUI(int row1, int col1, int row2, int col2) 
   {
      gui.setPiece(row1, col1, board[row2][col2]);
      gui.setPiece(row2, col2, board[row1][col1]);
   }
   
   public void initBoard() 
   {
      for (int i = 0; i < NUMROW; i++) {
         for (int j = 0; j < NUMCOL; j++) {
            board[i][j] = rn.nextInt(7 - 1);
         }
      }
   }
   public boolean adjacentSlots(int row1, int col1, int row2, int col2) 
   {
      if ((row2 == row1 - 1 || row2 == row1 + 1) && (col2 == col1) || (col2 == col1 - 1 || col2 == col1 + 1) && (row2 == row1)) 
      {
         return true;  
      }
      else 
      {
         return false;
      }
   
   }
  
   public int countLeft(int row, int col) 
   {
      int counter = 1;
      int current = board[row][col];
      boolean same = true;
      while (same == true) {
         try
         {
            if (board[row][col-1] == current) 
            {
            
               counter++;
               current = board[row][col-1];
               col--;
            
            }
            else 
            {
               same = false;
            }
         }
         catch (ArrayIndexOutOfBoundsException e) 
         {
            same = false;
         }
      }
      return counter;
   }

	public int countRight(int row, int col) 
   {
      int counter = 0;
      int current = board [row][col];
      boolean same = true;
      while (same == true) 
      {
         try
         {
            if (board[row][col+1] == current) 
            {
               counter++;
               current = board [row][col+1];
               col++;
               
            
            }
            else 
            {
               same = false;
            }
         }
         catch (ArrayIndexOutOfBoundsException e) 
         {
            same = false;
         }
      }
      return counter;
   }
 

   public int countUp(int row, int col) 
   {
      int counter = 0;
      int current = board [row][col];
      boolean same = true;
      
      while (same == true) 
      {
         try
         {
            if (board[row-1][col] == current) 
            {
               counter++;
               current = board [row-1][col];
               row--;
            
            }
            else 
            
               same = false;
            }
         }
         catch (ArrayIndexOutOfBoundsException e) 
         {
            same = false;
         }
      }
      return counter;
   }

   public int countDown(int row, int col) 
   {
      int counter = 0;
      int current = board [row][col];
      boolean same = true;
      
      while (same == true) 
      {
         try{
            if (board[row + 1][col] == current) 
            {
               counter++;
               current = board [row+1][col];
               row++;
            
            }
            else 
            {
               same = false;
            }
         }
         catch (ArrayIndexOutOfBoundsException e) 
         {
            same = false;
         }
      } 
      return counter;
   }

public void swapPieces (int row1, int col1, int row2, int col2) 
   {
      int temp = row1;
      int temp2 = col1;
      int temp3 = board [row1][col1];
      board[row1][col1] = board[row2][col2];
      board[row2][col2] = temp3;
   }
 
   public void markPiece (int row, int col) 
   {
      gui.highlightSlot(row, col, COLOUR_SELECT);
   }
   
   public void unmarkPiece (int row, int col) 
   {
      gui.unhighlightSlot(row, col);
   }
   public void markDeletePiece (int row, int col) 
   {
      board[row][col] = EMPTY;
   }
   public void markDeletePieceLeft (int row, int col, int num) {
      
      for (int i = 1; i < num; i++) 
      {
         gui.highlightSlot(row, col,COLOUR_DELETE);
         
      }
   }
 public void markDeletePieceRight(int row, int col, int num) 
   {
      
      for (int i = 1; i <= num; i++)
      {
         markDeletePiece(row, col+i);
      }
   public void markDeletePieceUp(int row, int col, int num) 
   {
    
   
      for (int i = 1; i <= num; i++)
      {  
         markDeletePiece(row-i, col);
      }
   }
   public void markDeletePieceDown(int row, int col, int num) 
   {
      
   
      for (int i = 1; i <= num; i++) 
      {
         markDeletePiece(row+i, col);
      }
   }
   public void updateBoard() 
   {  
      int counter = 0;
      int lowestValue = 0;
     
      for (int i = 0; i < NUMROW; i++) 
      {
         for (int j = NUMCOL-1; j >= 0; j--) 
         {
            if (board[j][i] == EMPTY) 
            {
               counter++;
               if (counter == 1) 
               {
                  lowestValue = j;
               }
            }
         }
         if (counter > 0) 
         {
            for (int j = lowestValue; j >= counter ; j--)
            {
               board[j][i] = board[j-counter][i];
            }
            for (int j = lowestValue - counter; j >= 0; j--) 
            {
               board[j][i] = EMPTY;
            }
         
         }
      
         counter = 0;
         lowestValue = 0;
        
      }
     
      for (int i = 0; i < NUMROW; i++) 
      {
         for (int j = 0; j < NUMCOL; j++) 
         {
            if (board[i][j] == EMPTY) 
            {
               board[i][j] = rn.nextInt(8 - 1);
            }
            unmarkPiece(i, j);
         }
      }
      gui.setScore(score);
      gui.setMoveLeft(movesLeft);
     
      if (movesLeft == 0)
      {
         endGame();
      }
   }
