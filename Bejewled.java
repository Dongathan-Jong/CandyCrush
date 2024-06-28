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
