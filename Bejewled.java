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


  
