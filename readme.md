## Tic Tac Toe with the MiniMax algorithm and alpha-beta pruning

# intention

The original purpose of this project was to be an extention of a [javascript project](http://codepen.io/thodges314/pen/VjYWje) which allows users to play human vs computer tic-tac-toe matches.  I had hoped, upon succesfully creating an unbeatable tic-tac-toe game, to extend the game to squares of larger dimensions.  The algorithm, on a 3-by-3 board, will ultimately fall into the familiar pattern that many of us have learned in grade school.  Such a clear pattern is not known to exist for a 4-by-4, a 5-by-5 or a 10-by-10 board.  Would this become a game that requires the cunning strategy of Go, or would the board quickly become so cluttered as to make winning impossible?

While I had originally intended to build a graphical interface that would allow the user to play games against the computer, I discovered that given the time required for the computer to select single moves early in the game, this would not practical.  This project quickly became a curiosity and a look at big-O runtime.

# implementation

This project is written in Java and consists of several classes.  They are as follows:

__GameBoard__: This class instantiates an object representing a single tic-tac-toe board.  The constructor uses the parameter `size` and constructs a `size`-by-`size` byte array.  Public methods are `getSize()` which returns the board size, `placeX(byte i, byte j)` and `placeO(byte i, byte j)` which return a new GameBoard with X or O added to the given board at position i, j (location is 0 indexed), `checkWin()` which returns 1, -1 or 0 depending on if X, O or neither player has won the board, `countPossibleMoves()` with returns a byte representing the number of moves remaining, `possibleMoves()` which returns a 2-dimensional byte array of possible moves remaining and `displayBoard()` which outputs a representation of the board to the terminal.  
`placeX(byte i, byte j)` and `placeO(byte i, byte j)` return new game boards so that testing a possible move doesn't mutate the current GameBoard.  All primatives were declared byte to save system resources during execution.

__Score__: This class bundles a score with a move.  It has getters and setters for the score (byte) and the accompanying move (byte array).  It implements comparable based on the score value.

__MiniMaxAlphaBeta__: This class does the real work.  The constructor receives a GameBoard and a boolean representing wheather it is X's turn to play.  The public method `getBestScore()` returns a Score object representing the ideal next move.  For a full explanation of the MiniMax algorithm, I recommend [this video](https://www.youtube.com/watch?v=STjW3eH0Cik).

__TicTacToe__: As this is a work in progress, at any time in development the main method in the TicTacToe class has served the purpose of testing all of the classes.  Now, when executed it asks for the size of the board and the number of moves that the computer will make.  If the number of moves is less than the size of the board squared then the board is filled with alternating Xs and Os scattered around the board to make up the difference between the board size and the number of possible moves that the computer is being asked to contemplate.  This lets us test execution time for board sizes other than square numbers.
After cpnstructing a board, the computer loops until the board is full or the game has been won, alternately assigning X or O to take a turn, displaying the move and the resultant board, and displaying the time taken to select a move.

## initial results
All experiments were conducted on a Intel Core i5-2430M System with 6GB of RAM running Xubuntu with minimal background activity.

On a 3-by-3 board I found this program to run significantly faster and more efficiently in Java than in JavaScript.  While JavaScript would take 584 milliseconds to make the first move, Java would take only 51 milliseconds.

When I initially tried running my program on a 4-by-4 board and waited several minutes for a result I thought that I had committed some error in my code.  Finally I decided to let my program run while I got up for some coffee.  On a 4-by-4 board my program took over 7 minutes to make the first move, and nearly 1 minute to make the second move.  
The following are the times taken in nanoseconds in one sample execution to make the 16 moves required to play a 4 by 4 game of tic-tac-toe:  
`438245236720`  
`59147675285`  
`41159916791`  
`2370946394`  
`717716489`  
`174163770`  
`42955012`  
`16490541`  
`5941570`  
`2063885`  
`1179783`  
`583850`  
`368384`  
`323892`  
`327889`  
`522487`  

Intuitively, this appears exponential, but performing a linear fit on the natural logs of those value results in a high R^2 value value but a less than satisfying visual representation:  
![]({{site.baseurl}}//fit1.png)

This experiment was conducted again asking the computer to find 17 moves.  A 5-by-5 board was rendered and 8 moves were randomly generated of alternate X and O before minimax was implemented.  The times to select moves were even more erratic:  
`9336016288430` - `2 hours 35 minutes 36 seconds 16 milliseconds 288 microseconds 430 nanoseconds`  
`8490946512765` - `2 hours 21 minutes 30 seconds 946 milliseconds 512 microseconds 765 nanoseconds`  
`8475921330967` - `2 hours 21 minutes 15 seconds 921 milliseconds 330 microseconds 967 nanoseconds`  
`62478646908` --- `1 minute 2 seconds 478 milliseconds 646 microseconds 908 nanoseconds`  
`60380934206` --- `1 minute 380 milliseconds 934 microseconds 206 nanoseconds`  
`373576695` ----- `373 milliseconds 576 microseconds 695 nanoseconds`  
`425921237` ----- `425 milliseconds 921 microseconds 237 nanoseconds`  
`28910997` ------ `28 milliseconds 910 microseconds 997 nanoseconds`  
`7167043` ------- `1 millisecond 923 microseconds 899 nanoseconds`  
`1923899` ------- `1 millisecond 923 microseconds 899 nanoseconds`  
`999819` -------- `999 microseconds 819 nanoseconds`  
`904596` -------- `904 microseconds 596 nanoseconds`  
`508394` -------- `508 microseconds 394 nanoseconds`  
`425584` -------- `425 microseconds 584 nanoseconds`  
`495731` -------- `495 microseconds 731 nanoseconds`  
`389555` -------- `389 microseconds 555 nanoseconds`  
`358337` -------- `358 microseconds 337 nanoseconds`  

Computation time suddenly drops from approximately 2 and a half hours to 1 minute to 400 milliseconds to 2 milliseconds and then to 1 millisecond where an even regression begins.  
Here is a chart of the natural logs of those values in reverse order and the poorly representative linear best fit equation:  
![]({{site.baseurl}}//fit2.png)
