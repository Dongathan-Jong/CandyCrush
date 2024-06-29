
import java.awt.Color;
import java.io.*;
import java.util.*;

public class Bejeweled 
{

   /* 
	 * Constants
	 */  
   // colours used to mark the selected piece and 
   // the pieces in the chain to be deleted
   final Color COLOUR_DELETE = Color.RED;
   final Color COLOUR_SELECT = Color.YELLOW;

   BejeweledGUI gui;
   
   // declare all constants here
   final int EMPTY = -1;
   final int CHAIN_REQ = 3;
   final int NUMROW = 8, NUMCOL = 8;
   final String GAMEFILEFOLDER = "gamefiles";
   // declare all "global" variables here
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
      // call the start method     
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
      scoreTemp = 0;
      int temp1 = 0;
      int temp2 = 0;
      int temp3 = 0;
      int temp4 = 0;
      
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
            unmarkPiece(row,column);
            unmarkPiece(slot1row, slot1col);
     
         }
         
         temp1 = countLeft(row,column) + countRight(row,column) + 1;
         
         
         if ( temp1 >= CHAIN_REQ) 
         {
            scoreTemp += (countLeft(row,column) + countRight(row,column))+1;
            score += (countLeft(row,column) + countRight(row,column))+1;
            
            markDeletePieceLeft(row,column,(countLeft(row,column)));
            markDeletePieceRight(row,column,(countRight(row,column)));
            
            chainMade = true;
            unmarkPiece(row, column);
            markDeletePiece(row,column);
            updateBoard();
         }

         temp2 = countLeft(slot1row,slot1col) + countRight(slot1row, slot1col) + 1;

         if (temp2 >= CHAIN_REQ) 
         {
            scoreTemp += (countLeft(slot1row, slot1col) + countRight(slot1row, slot1col)) + 1;
            score += (countLeft(slot1row, slot1col) + countRight(slot1row, slot1col)) + 1;
            
            markDeletePieceLeft(slot1row,slot1col, countLeft(slot1row,slot1col));
            markDeletePieceRight(slot1row,slot1col, countRight(slot1row,slot1col));
            adjacentChainMade = true;
            unmarkPiece(slot1row,slot1col);
            markDeletePiece(slot1row,slot1col);
            updateBoard();
         }
         
         temp3 = countUp(slot1row,slot1col) + countDown(slot1row, slot1col) + 1;
                  
         if (temp3 >= CHAIN_REQ) 
         {
            scoreTemp += (countUp(slot1row, slot1col) + countDown(slot1row, slot1col)) + 1;
            score += (countUp(slot1row, slot1col) + countDown(slot1row, slot1col)) + 1;
            
            markDeletePieceUp(slot1row,slot1col, countUp(slot1row,slot1col));
            markDeletePieceDown(slot1row,slot1col, countDown(slot1row,slot1col));
         
            adjacentChainMade = true;
            unmarkPiece(slot1row,slot1col);
            markDeletePiece(slot1row,slot1col);
            updateBoard();
         }
         
         temp4 = countUp(row,column) + countDown(row,column)+ 1;
         
         if (temp4 >= CHAIN_REQ) 
         {
            scoreTemp += (countUp(row,column) + countDown(row,column))+1;
            score += (countUp(row,column) + countDown(row,column))+1;
            

            markDeletePieceUp(row,column,countUp(row,column));
            markDeletePieceDown(row,column,countDown(row,column));
         
            chainMade = true;
            unmarkPiece(row, column);
            markDeletePiece(row,column);
            updateBoard();
            
            
         }
         
         if (chainMade == true) 
         { 
            markDeletePiece(row,column);
            gui.showChainSizeMessage(scoreTemp);
         }
         if(adjacentChainMade == true) 
         {
            markDeletePiece(slot1row, slot1col);
            gui.showChainSizeMessage(scoreTemp);
         }
         if(adjacentChainMade == true || chainMade == true) 
         {
            movesLeft--;
         }
         if (chainMade == false && adjacentChainMade == false) 
         {
            swapPiecesGUI(slot1row, slot1col, row, column);
            swapPieces(slot1row, slot1col, row, column);
            gui.showInvalidMoveMessage();
         }
         
         
         updateBoard();
         updateGameBoard();
         
         firstSelection = true;
      
      }
      else 
      {
         unmarkPiece(row,column);
         unmarkPiece(slot1row, slot1col);
         gui.showInvalidMoveMessage();
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
            board[i][j] = rn.nextInt(8 - 1);
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
      int counter = 0;
      int current = board[row][col];
      boolean same = true;
      
      while (same == true) 
      {
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
      gui.highlightSlot(row, col,COLOUR_DELETE);
      
   }
  
   public void markDeletePieceLeft (int row, int col, int num) 
   {
      for (int i = 1; i <= num; i++) 
      {
         markDeletePiece(row, col-i);
         
      }
   }
  
   public void markDeletePieceRight(int row, int col, int num) 
   {
      
      for (int i = 1; i <= num; i++)
      {
         markDeletePiece(row, col+i);
      }
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
 
   public boolean saveToFile(String fileName) 
   {
      String folder = GAMEFILEFOLDER + "/" + fileName;
      try 
      {
         BufferedWriter writer = new BufferedWriter (new FileWriter (folder, false));
         writer.write(score + "\n" + movesLeft + "\n");
         
         for (int i = 0; i < NUMROW; i++) 
         {
            for (int j = 0; j < NUMCOL; j++) 
            {
               writer.write(board[i][j] + " ");
            }
            writer.write("\n");
         }
         writer.close();
         return true;
         
      }
      catch (IOException e) 
      {
         return false;
      }
   
   }
  
   public boolean loadFromFile (String fileName)
   {
      try {
         Scanner fs = new Scanner (new File (GAMEFILEFOLDER + "/" + fileName));
            score = fs.nextInt();
            movesLeft = fs.nextInt();
         for (int i = 0; i < NUMROW; i++) 
         {
            for (int j = 0; j < NUMCOL; j++) 
            {
               board[i][j] = fs.nextInt();
            }
         }
         return true;
      }
      catch (NumberFormatException e)
      {
         return false;
      }
      catch(IOException e)
      {
         return false;
      }
      
   
   
   }
}
