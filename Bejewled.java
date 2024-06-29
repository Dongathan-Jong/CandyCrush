
import java.awt.Color;
import java.io.*;
import java.util.*;
/*--------------------------------------------------------------|
|Name: Bejewled                                                 |
|---------------------------------------------------------------|
|Programmer: Jonathan Dong                                      |
|---------------------------------------------------------------|
|Description: This program runs a version of bejewled on the GUI|
|                                                               |
|                                                               |
|--------------------------------------------------------------*/
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
   
   /*--------------------------------------------------------------|
   |void start()                                                   |
   |---------------------------------------------------------------|
   |Returns nothing                                                |
   |---------------------------------------------------------------|
   |                                                               |
   |---------------------------------------------------------------|
   |Description: This method starts the game                       |
   |                                                               |
   |                                                               |
   |--------------------------------------------------------------*/

   public void start() {
      // initiate variables and array
      board = new int[NUMROW][NUMCOL];
      firstSelection = true;
      score = 0;
      scoreTemp = 0;
      movesLeft = 10;
      
      // call methods
      initBoard();
      updateBoard();
      updateGameBoard();
   }

   /*--------------------------------------------------------------|
   |void play (int row, int column)                                |
   |---------------------------------------------------------------|
   |Returns nothing                                                |
   |---------------------------------------------------------------|
   |int row - row of selected piece                                |
   |int column - column of selected piece                          |
   |---------------------------------------------------------------|
   |Description: This method is all the logic of the game          |
   |                                                               |
   |                                                               |
   |--------------------------------------------------------------*/

   public void play (int row, int column) 
   {
      scoreTemp = 0;
      int temp1 = 0;
      int temp2 = 0;
      int temp3 = 0;
      int temp4 = 0;
      
      // check if the selection is the first one
      if (firstSelection) 
      {
         // mark the piece yellow for selection
         markPiece(row, column);
         //set slot1row and slot1col as original points
         slot1row = row;
         slot1col = column;
         // make firstselection and other variables false
         firstSelection = false;
         chainMade = false;
         adjacentChainMade = false;
         
         
      }
      
      // check if both slots are adjacent 
      else if (adjacentSlots(slot1row, slot1col, row, column))
      {
         // check if it is the same piece clicked twice
         if (slot1row == row && slot1col == column) 
         {
            // unmark piece and show invalid message
            unmarkPiece(row,column);
            unmarkPiece(slot1row, slot1col);
            gui.showInvalidMoveMessage();
         }
         
         //check if the two are adjacent
         if (adjacentSlots(slot1row, slot1col, row, column)) 
         {
            // swap the pieces on the gui and the board
            swapPiecesGUI(slot1row, slot1col, row, column);
            swapPieces(slot1row, slot1col, row, column);
            unmarkPiece(row,column);
            unmarkPiece(slot1row, slot1col);
     
         }
         
         temp1 = countLeft(row,column) + countRight(row,column) + 1;
         
         
         // check if there is a horizontal chain with the second selection
         if ( temp1 >= CHAIN_REQ) 
         {
            // set display score and score
            scoreTemp += (countLeft(row,column) + countRight(row,column))+1;
            score += (countLeft(row,column) + countRight(row,column))+1;
            
            // mark the pieces to the left and right of the piece and set them as red outline
            markDeletePieceLeft(row,column,(countLeft(row,column)));
            markDeletePieceRight(row,column,(countRight(row,column)));
            
            // verify a chain made and unmark the selected piece
            chainMade = true;
            unmarkPiece(row, column);
            markDeletePiece(row,column);
            updateBoard();
         }

         temp2 = countLeft(slot1row,slot1col) + countRight(slot1row, slot1col) + 1;

         //check if there is a horizontal chain with the first selection
         if (temp2 >= CHAIN_REQ) 
         {
            // set display score and score
            scoreTemp += (countLeft(slot1row, slot1col) + countRight(slot1row, slot1col)) + 1;
            score += (countLeft(slot1row, slot1col) + countRight(slot1row, slot1col)) + 1;
            
            // mark the pieces to the left and right of the piece and set them as red outline
            markDeletePieceLeft(slot1row,slot1col, countLeft(slot1row,slot1col));
            markDeletePieceRight(slot1row,slot1col, countRight(slot1row,slot1col));
            // verify a chain made and unmark the selecte piece
            adjacentChainMade = true;
            unmarkPiece(slot1row,slot1col);
            markDeletePiece(slot1row,slot1col);
            updateBoard();
         }
         
         temp3 = countUp(slot1row,slot1col) + countDown(slot1row, slot1col) + 1;
                  
         // check for a vertical chain with the first selection
         if (temp3 >= CHAIN_REQ) 
         {
            //set display score and score
            scoreTemp += (countUp(slot1row, slot1col) + countDown(slot1row, slot1col)) + 1;
            score += (countUp(slot1row, slot1col) + countDown(slot1row, slot1col)) + 1;
            
            // mark the pieces up and down to the piece and set them as red outline
            markDeletePieceUp(slot1row,slot1col, countUp(slot1row,slot1col));
            markDeletePieceDown(slot1row,slot1col, countDown(slot1row,slot1col));
         
            // verify a chain made and unmark the selected piece
            adjacentChainMade = true;
            unmarkPiece(slot1row,slot1col);
            markDeletePiece(slot1row,slot1col);
            updateBoard();
         }
         
         temp4 = countUp(row,column) + countDown(row,column)+ 1;
         
         // check for a vertical chain with the second selection
         if (temp4 >= CHAIN_REQ) 
         {
            //set display score and score
            scoreTemp += (countUp(row,column) + countDown(row,column))+1;
            score += (countUp(row,column) + countDown(row,column))+1;
            
            // mark the pieces up and down to the piece and set them as red outline

            markDeletePieceUp(row,column,countUp(row,column));
            markDeletePieceDown(row,column,countDown(row,column));
         
            // verify a chain made and unmark the selected piece
            chainMade = true;
            unmarkPiece(row, column);
            markDeletePiece(row,column);
            updateBoard();
            
            
         }
         
         // if there is a chain made
         if (chainMade == true) 
         { 
            // show chain size message
            markDeletePiece(row,column);
            gui.showChainSizeMessage(scoreTemp);
         }
         if(adjacentChainMade == true) 
         {
            // mark the selected piece as deleted and show chain size message
            markDeletePiece(slot1row, slot1col);
            gui.showChainSizeMessage(scoreTemp);
         }
         // if there are chains made
         if(adjacentChainMade == true || chainMade == true) 
         {
            // minus a move
            movesLeft--;
         }
         // if no chain was made
         if (chainMade == false && adjacentChainMade == false) 
         {
            // swap pieces back 
            swapPiecesGUI(slot1row, slot1col, row, column);
            swapPieces(slot1row, slot1col, row, column);
            // show invalid move
            gui.showInvalidMoveMessage();
         }
         
         // update board to fill empty pieces
         
         updateBoard();
         updateGameBoard();
         
         // revert back to first selection
         firstSelection = true;
      
      }
      else 
      {
         // unmark the pieces
         unmarkPiece(row,column);
         unmarkPiece(slot1row, slot1col);
         gui.showInvalidMoveMessage();
         //revert back to first selection
         firstSelection = true;
      }
   
   }
   /*--------------------------------------------------------------|
   | void updateGameBoard()                                        |
   |---------------------------------------------------------------|
   |Returns nothing                                                |
   |---------------------------------------------------------------|
   |                                                               |
   |---------------------------------------------------------------|
   |Description: updates the visual game board by setting the piece|
   |                                                               |
   |                                                               |
   |--------------------------------------------------------------*/
   public void updateGameBoard() 
   {
      for (int i = 0; i < NUMROW; i++) {
         for (int j = 0; j < NUMCOL; j++) {
            gui.setPiece(i,j, board[i][j]);
         }
      }
   }
   /*--------------------------------------------------------------|
   | void endGame()                                                |
   |---------------------------------------------------------------|
   |Returns nothing                                                |
   |---------------------------------------------------------------|
   |                                                               |
   |---------------------------------------------------------------|
   |Description: ends the game by showing a popup                  |
   |                                                               |
   |                                                               |
   |--------------------------------------------------------------*/
   public void endGame() 
   {
      // call the GUI program to activate the endgame popup
      gui.showGameOverMessage(score, 10 - movesLeft);
   }
   /*--------------------------------------------------------------|
   | void swapPiecesGUI(int row1, int col1, int row2, int col2)    |
   |---------------------------------------------------------------|
   |Returns nothing                                                |
   |---------------------------------------------------------------|
   |int row1 - first row coordinate                                |
   |int col1 - first column coordinate                             |
   |int row2 - second row coordinate                               |
   |int col2 - second column coordinate                            |
   |---------------------------------------------------------------|
   |Description: swaps the pieces on the gui                       |
   |                                                               |
   |                                                               |
   |--------------------------------------------------------------*/  
   public void swapPiecesGUI(int row1, int col1, int row2, int col2) 
   {
      // set the pieces to eachother
      gui.setPiece(row1, col1, board[row2][col2]);
      gui.setPiece(row2, col2, board[row1][col1]);
   }
   /*--------------------------------------------------------------|
   | void initBoard()                                              |
   |---------------------------------------------------------------|
   |Returns nothing                                                |
   |---------------------------------------------------------------|
   |                                                               |
   |---------------------------------------------------------------|
   |Description: updates the board array with random numbers from  |
   |             1-7                                               |
   |                                                               |
   |--------------------------------------------------------------*/
   public void initBoard() 
   {
      // generate random numbers to fill the whole array
      for (int i = 0; i < NUMROW; i++) {
         for (int j = 0; j < NUMCOL; j++) {
            board[i][j] = rn.nextInt(8 - 1);
         }
      }
   }
   /*--------------------------------------------------------------|
   | boolean adjacentSlots(int row1, int col1, int row2, int col2) |
   |---------------------------------------------------------------|
   |Returns boolean - if it is adjacent or not                     |
   |---------------------------------------------------------------|
   |int row1 - first row coordinate                                |
   |int col1 - first column coordinate                             |
   |int row2 - second row coordinate                               |
   |int col2 - second column coordinate                            |
   |---------------------------------------------------------------|
   |Description: checks if the two coordinates are adjacent        |
   |                                                               |
   |                                                               |
   |--------------------------------------------------------------*/ 
   public boolean adjacentSlots(int row1, int col1, int row2, int col2) 
   {
      // check if piece is adjacent
      if ((row2 == row1 - 1 || row2 == row1 + 1) && (col2 == col1) || (col2 == col1 - 1 || col2 == col1 + 1) && (row2 == row1)) 
      {
         // return
         return true;  
      }
      else 
      {
         // return
         return false;
      }
   
   }
   /*--------------------------------------------------------------|
   | int countLeft(int row, int col)                               |
   |---------------------------------------------------------------|
   |Returns int - number of pieces to the left                     |
   |---------------------------------------------------------------|
   |  int row - row of the coordinate                              |
   |  int col - column of the coordinate                           |
   |---------------------------------------------------------------|
   |Description: counts the number of pieces to the left           |
   |                                                               |
   |                                                               |
   |--------------------------------------------------------------*/
   public int countLeft(int row, int col) 
   {
      // initialize temporary variables 
      int counter = 0;
      int current = board[row][col];
      boolean same = true;
      
      // keep running while true
      while (same == true) 
      {
         try
         {
            // if it matches with the last one
            if (board[row][col-1] == current) 
            {
               // increase the counter and go to next piece
               counter++;
               current = board[row][col-1];
               col--;
            
            }
            else 
            {
               // exit loop
               same = false;
            }
         }
         catch (ArrayIndexOutOfBoundsException e) 
         {
            // exit loop
            same = false;
         }
      }
      // return
      return counter;
   }
   /*--------------------------------------------------------------|
   | int countRight(int row, int col)                              |
   |---------------------------------------------------------------|
   |Returns int - number of pieces to the right                    |
   |---------------------------------------------------------------|
   |  int row - row of the coordinate                              |
   |  int col - column of the coordinate                           |
   |---------------------------------------------------------------|
   |Description: counts the number of pieces to the right          |
   |                                                               |
   |                                                               |
   |--------------------------------------------------------------*/
  public int countRight(int row, int col) 
   {
       // initialize temporary variables
      int counter = 0;
      int current = board [row][col];
      boolean same = true;
      // run while true
      while (same == true) 
      {
         try
         {
            // check if pieces are the same
            if (board[row][col+1] == current) 
            {
               // go to next piece and count up
               counter++;
               current = board [row][col+1];
               col++;
               
            
            }
            else 
            {
               // exit loop
               same = false;
            }
         }
         catch (ArrayIndexOutOfBoundsException e) 
         {
            // false
            same = false;
         }
      }
         // return
      return counter;
   }
 
   /*--------------------------------------------------------------|
   | int countUp(int row, int col)                                 |
   |---------------------------------------------------------------|
   |Returns int - number of pieces up                              |
   |---------------------------------------------------------------|
   |  int row - row of the coordinate                              |
   |  int col - column of the coordinate                           |
   |---------------------------------------------------------------|
   |Description: counts the number of pieces up                    |
   |                                                               |
   |                                                               |
   |--------------------------------------------------------------*/
   public int countUp(int row, int col) 
   {
      // create and initialize temporary variables
      int counter = 0;
      int current = board [row][col];
      boolean same = true;
      
      // run while true
      while (same == true) 
      {
         try
         {
            // check if both pieces match
            if (board[row-1][col] == current) 
            {
               // count up and go to next piece
               counter++;
               current = board [row-1][col];
               row--;
            
            }
            else 
            {
               // make false
               same = false;
            }
         }
         catch (ArrayIndexOutOfBoundsException e) 
         {
            // make false
            same = false;
         }
      }
      //return counted value
      return counter;
   }
   /*--------------------------------------------------------------|
   | int countDown(int row, int col)                               |
   |---------------------------------------------------------------|
   |Returns int - number of pieces down                            |
   |---------------------------------------------------------------|
   |  int row - row of the coordinate                              |
   |  int col - column of the coordinate                           |
   |---------------------------------------------------------------|
   |Description: counts the number of pieces down                  |
   |                                                               |
   |                                                               |
   |--------------------------------------------------------------*/
   public int countDown(int row, int col) 
   {
      // create temporary variables and initialize
      int counter = 0;
      int current = board [row][col];
      boolean same = true;
      
      // run while true
      while (same == true) 
      {
         try{
            // check if pieces are the same
            if (board[row + 1][col] == current) 
            {
               // count up and check next piece
               counter++;
               current = board [row+1][col];
               row++;
            
            }
            else 
            {
               // put false
               same = false;
            }
         }
         catch (ArrayIndexOutOfBoundsException e) 
         {
            // put false
            same = false;
         }
      } 
      // return counter
      return counter;
   }
   /*--------------------------------------------------------------|
   | void swapPieces (int row1, int col1, int row2, int col2)      |
   |---------------------------------------------------------------|
   |Returns nothing                                                |
   |---------------------------------------------------------------|
   |int row1 - first row coordinate                                |
   |int col1 - first column coordinate                             |
   |int row2 - second row coordinate                               |
   |int col2 - second column coordinate                            |
   |---------------------------------------------------------------|
   |Description: swaps the pieces on the board array               |
   |                                                               |
   |                                                               |
   |--------------------------------------------------------------*/ 
   public void swapPieces (int row1, int col1, int row2, int col2) 
   {
      // initialize temporary variables and create them
      int temp = row1;
      int temp2 = col1;
      int temp3 = board [row1][col1];
      
      //swap pieces
      board[row1][col1] = board[row2][col2];
      board[row2][col2] = temp3;
   }
   /*--------------------------------------------------------------|
   | void markPiece (int row, int col)                             |
   |---------------------------------------------------------------|
   |Returns nothing                                                |
   |---------------------------------------------------------------|
   |int row - row of coordinate                                    |
   |int col - column of coordinate                                 |
   |---------------------------------------------------------------|
   |Description: mark the piece in the gui slot                    |
   |                                                               |
   |                                                               |
   |--------------------------------------------------------------*/
   public void markPiece (int row, int col) 
   {
      // highlight the slot with the select colour
      gui.highlightSlot(row, col, COLOUR_SELECT);
   }
   /*--------------------------------------------------------------|
   | void unmarkPiece (int row, int col)                           |
   |---------------------------------------------------------------|
   |Returns nothing                                                |
   |---------------------------------------------------------------|
   |int row - row of coordinate                                    |
   |int col - column of coordinate                                 |
   |---------------------------------------------------------------|
   |Description: unmark the piece in the gui slot                  |
   |                                                               |
   |                                                               |
   |--------------------------------------------------------------*/
   public void unmarkPiece (int row, int col) 
   {
      // unhighlight the slot to show no colours
      gui.unhighlightSlot(row, col);
   }
   /*--------------------------------------------------------------|
   | void markDeletePiece (int row, int col)                       |
   |---------------------------------------------------------------|
   |Returns nothing                                                |
   |---------------------------------------------------------------|
   |int row - row of coordinate                                    |
   |int col - column of coordinate                                 |
   |---------------------------------------------------------------|
   |Description: mark the piece with the red outline and set it to |
   |             empty                                             |
   |                                                               |
   |--------------------------------------------------------------*/
   public void markDeletePiece (int row, int col) 
   {
      // highlight the slot with red and make the slot empty
      board[row][col] = EMPTY;
      gui.highlightSlot(row, col,COLOUR_DELETE);
      
   }
   /*--------------------------------------------------------------|
   |void markDeletePieceLeft (int row, int col, int num)           |
   |---------------------------------------------------------------|
   |Returns nothing                                                |
   |---------------------------------------------------------------|
   |int row - row of coordinate                                    |
   |int col - column of coordinate                                 |
   |int num - number to delete to the left                         |
   |---------------------------------------------------------------|
   |Description: delete the pieces to the left and mark them       |
   |                                                               |
   |                                                               |
   |--------------------------------------------------------------*/
   public void markDeletePieceLeft (int row, int col, int num) 
   {
      // run for specified number of times
      for (int i = 1; i <= num; i++) 
      {
         // call the method
         markDeletePiece(row, col-i);
         
      }
   }
   /*--------------------------------------------------------------|
   |void markDeletePieceRight (int row, int col, int num)          |
   |---------------------------------------------------------------|
   |Returns nothing                                                |
   |---------------------------------------------------------------|
   |int row - row of coordinate                                    |
   |int col - column of coordinate                                 |
   |int num - number to delete to the right                        |
   |---------------------------------------------------------------|
   |Description: delete the pieces to the right and mark them      |
   |                                                               |
   |                                                               |
   |--------------------------------------------------------------*/
   public void markDeletePieceRight(int row, int col, int num) 
   {
      
      // run for specified number of times
      for (int i = 1; i <= num; i++)
      {
         // call the method to delete
         markDeletePiece(row, col+i);
      }
   }
   /*--------------------------------------------------------------|
   |void markDeletePieceUp (int row, int col, int num)             |
   |---------------------------------------------------------------|
   |Returns nothing                                                |
   |---------------------------------------------------------------|
   |int row - row of coordinate                                    |
   |int col - column of coordinate                                 |
   |int num - number to delete to the up                           |
   |---------------------------------------------------------------|
   |Description: delete the pieces to the up and mark them         |
   |                                                               |
   |                                                               |
   |--------------------------------------------------------------*/
   public void markDeletePieceUp(int row, int col, int num) 
   {
    
      // run for specified number of times
   
      for (int i = 1; i <= num; i++)
      {  
         // call method to delete
         markDeletePiece(row-i, col);
      }
   }
   /*--------------------------------------------------------------|
   |void markDeletePieceDown (int row, int col, int num)           |
   |---------------------------------------------------------------|
   |Returns nothing                                                |
   |---------------------------------------------------------------|
   |int row - row of coordinate                                    |
   |int col - column of coordinate                                 |
   |int num - number to delete to the down                         |
   |---------------------------------------------------------------|
   |Description: delete the pieces to the down and mark them       |
   |                                                               |
   |                                                               |
   |--------------------------------------------------------------*/
   public void markDeletePieceDown(int row, int col, int num) 
   {
      
   
      // run for specified number of times
      for (int i = 1; i <= num; i++) 
      {
         markDeletePiece(row+i, col);
      }
   }
   /*--------------------------------------------------------------|
   | void updateBoard()                                            |
   |---------------------------------------------------------------|
   |Returns nothing                                                |
   |---------------------------------------------------------------|
   |                                                               |
   |---------------------------------------------------------------|
   |Description: updates the board array to make the pieces fall   |
   |             down                                              |
   |                                                               |
   |--------------------------------------------------------------*/
   public void updateBoard() 
   {  
      // initialize temporary values  
      int counter = 0;
      int lowestValue = 0;
     
      // run for each row and col
      for (int i = 0; i < NUMROW; i++) 
      {
         for (int j = NUMCOL-1; j >= 0; j--) 
         {
            // check how many are empty
            if (board[j][i] == EMPTY) 
            {
               // increase counter
               counter++;
               if (counter == 1) 
               {
                  //set lowest value
                  lowestValue = j;
               }
            }
         }
         // check if counter is a positive number
         if (counter > 0) 
         {
            // send pieces down
            for (int j = lowestValue; j >= counter ; j--)
            {
               board[j][i] = board[j-counter][i];
            }
            for (int j = lowestValue - counter; j >= 0; j--) 
            {
               board[j][i] = EMPTY;
            }
         
         }
      
         // reset values
         counter = 0;
         lowestValue = 0;
        
      }
     
      // reset all empty values to random squares
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
      // set score and moves
      gui.setScore(score);
      gui.setMoveLeft(movesLeft);
     
      if (movesLeft == 0)
      {
         // set end game message if no more moves
         endGame();
      }
   }
   /*--------------------------------------------------------------|
   |boolean saveToFile(String fileName)                            |
   |---------------------------------------------------------------|
   |Returns boolean - if it saves the game                         |
   |---------------------------------------------------------------|
   |String fileName - filename that user specifies                 |
   |---------------------------------------------------------------|
   |Description: saves the game to a file                          |
   |                                                               |
   |                                                               |
   |--------------------------------------------------------------*/
   public boolean saveToFile(String fileName) 
   {
      // create and initialize variable
      String folder = GAMEFILEFOLDER + "/" + fileName;
      try 
      {
         // create bufferedwriter and store score and moves
         BufferedWriter writer = new BufferedWriter (new FileWriter (folder, false));
         writer.write(score + "\n" + movesLeft + "\n");
         
         // store array of all 8 x 8 pieces
         for (int i = 0; i < NUMROW; i++) 
         {
            for (int j = 0; j < NUMCOL; j++) 
            {
               writer.write(board[i][j] + " ");
            }
            writer.write("\n");
         }
         // close writer and return true
         writer.close();
         return true;
         
      }
      // catch exception and return false
      catch (IOException e) 
      {
         return false;
      }
   
   }
   /*--------------------------------------------------------------|
   |boolean loadFromFile (String fileName)                         |
   |---------------------------------------------------------------|
   |Returns boolean - if it loads the game                         |
   |---------------------------------------------------------------|
   |String fileName - filename that user specifies                 |
   |---------------------------------------------------------------|
   |Description: loads the file to the game                        |
   |                                                               |
   |                                                               |
   |--------------------------------------------------------------*/
   public boolean loadFromFile (String fileName)
   {
      try {
         // create scanner and set variable
         Scanner fs = new Scanner (new File (GAMEFILEFOLDER + "/" + fileName));
            score = fs.nextInt();
            movesLeft = fs.nextInt();
         // restore the board pieces
         for (int i = 0; i < NUMROW; i++) 
         {
            for (int j = 0; j < NUMCOL; j++) 
            {
               board[i][j] = fs.nextInt();
            }
         }
         // return true
         return true;
      }
      catch (NumberFormatException e)
      {
         // return false
         return false;
      }
      catch(IOException e)
      {
         return false;
      }
      
   
   
   }
}
