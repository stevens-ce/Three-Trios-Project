# Three Trios Game
 Two player card game using two windows. 


# Game Setup
 Two players - Either player can be substituted for a machine player, who will automatically play when it is their turn. Both players can be made machines, but this will instantly play through the game; not interesting.
 
 10 Cards - Each card has four number values, one for each of its sides. The color of the card is determined by its current owner.
 
 A Board - The board has valid tiles and hole tiles. Valid tiles are a lighter gray. Cards can only be placed on valid tiles.

 
# Game Rules
 Each player has a hand of 5 cards. On their turn, the player must place their card onto an empty valid tile on the board. If this card is adjacent to an opponent card, and its number value adjacent to the opponent card is higher than the opponent's adjacent number, the opponent card will be dyed the current player's color. This process will repeat for all adjacent cards. 

 The winner of the game is whoever has the most cards dyed their color when all cards have been played to the board.

 Player Red goes first, Blue goes second.

 
# Running Game
 Run game using hw5.jar; command line args required to run game. 

 These arguments will determine whether the Red (argument 1) and Blue (argument 2) players are machines or humans. 
  
  Possible arguments: human, strategy1, strategy2
   
   human: manual play
  
   strategy1: automatically plays a card to the position that will flip the maximum number of cards
  
   strategy2: automatically plays a card to the safest cornermost position

   
# Misc Details
 Alt README contains specific implementation details, produced during the course of creating this project.
 
 All code in src/threetrios/view credited to partner programmer Leah Methratta. 
