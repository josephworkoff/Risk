# Risk
Java servlet-based browser Risk game
Currently hosted at: http://jwork967.kutztown.edu:8080/Risk/

Javadoc: https://kuvapcsitrd01.kutztown.edu/~jwork967/Risk/

<h2>Game Rules:</h2><br>
    At the start of the game, territories are automatically randomly assigned to each player or neutral. Afterward, players alternate placing 2 troops between one or two of their territories. Between each player, the neutral party randomly selects a neutral territory to place one troop in. This continues until both players have placed 40 troops. Player 1 (Red) moves first.<br>
Each turn, the player gains a certain amount of troops to place in their territories based on their number of territories, bonuses from continents they control, and cards they turn in. After troops have been placed, the player chooses to enter their battle phase or pass. During the battle phase, the player selects one of their territories to attack with, then an adjacent enemy territory to attack. Territories must have at least 2 troops to attack. <br>
When two proper territories have been selected, the attacking and defending players are prompted to enter a number of dice to roll. The attacker may roll dice equal to their territory's power - 1, to a maximum of 3 dice. The defender may roll dice equal to their territory's power up to 2. The highest attacking and defending dice are compared, and the attacker and defenders take damage equal to the number of higher dice the enemy rolled. Ties go to the defender. <br>
eg.  A - D <br>
    [5] [6]<br>
    [3] [2]<br>
    [1]<br>
Attacker loses 1, defender loses 1.<br>
When the defender is brought to 0 power, the territory is conquered by the attacker. When that happens, the attacker chooses a number of troops to move from the attacking territory into the defender, moving at least the number of troops as their last number of dice rolled. A territory may never have 0 troops in it.<br>
The attacker may keep attacking as long as they have at least 2 troops left in that territory, or they may retreat or pass. <br>
At the end of their turn, if the player conquered a territory during their turn, they are granted a random card from the deck. At the start of their turn, if the player has at least 3 cards and a match in their hand, they will be asked if they want to trade in cards for bonus troops. If the player has 5 or more cards, they must trade in a set. Cards must have 3 of the same or 3 different troop designs to be a match. The number of troops gained by trading in cards increases every time a set is traded in. <br>
A player wins by conquering all non-neutral territories.<br>



<h2>Gameplay:</h2>
Instructions are displayed at the bottom of the screen, just below the map.<br>
Players advance through turn phases automatically when the phase is complete, or by clicking the 4 buttons on the left.<br>
    (Placing all troops is required before the player can move to the battle phase.)<br>
When prompted to select a territory for reinforcement/battle, players do so by clicking the buttons on the right labeled with territory names (the map does nothing). Territories can be deselected by clicking them again. Only the buttons for valid selections are enabled at a time.<br>
    When reinforcing, players will then be prompted to enter and submit a number of troops into the text box at the bottom.<br>
    When battling, players will be prompted to select an attacking and then a defending territory. Then the players, first the attacker then defender, are prompted to select and submit a number of dice to roll. Only the attacker may select 0 to retreat from the battle. The battle phase ends only when the turn player clicks the Pass phase button.<br>
When a player has 3 matching cards in their hand, they have the option to turn in cards before they begin the reinforcement phase. If they have 5 or more cards, this is mandatory.<br>
When prompted to select cards to turn in, players do so by clicking on the buttons on the top-left. Selected cards appear in the bottom panel. Cards may be deselected by clicking them again. When cards are submitted, the reinforcement phase will begin if the cards match, or all cards are deselected if they don't.<br>
<br>
In lieu of a start screen, accessing the game through the default URL begins a default game with two human players, red and blue, with a grey neutral party and randomly assigned territories. The game begins with the two players taking turns placing up to two and proceeds from there with normal two-player rules.<br>
Alternatively, the initial game state can be seeded with a URL Get query string that can define player settings, the initial owners and occupancies of all territories (in which case the game will skip the initial allocation step), and cards in players' hands. Syntax and sample URLs can be found below.<br>


<h2>URL Options:</h2>

?online parameter triggers an internet game. No other URL options will be applied if this option exists.<br>

Local games support the following setup parameters:<br><br>

Bots can be enabled with " ?bots=[1|2] ". <br>
The delay before bots' actions can be changed with " ?bots=[1|2]&delay=[time in ms]] ".<br>

Cards can be given to players at the start of the game with<br>
    [RC|BC]=[Territory ID 1-42][ACIW] (eg. ?RC=2I begins the game with Red having an Alberta/Infantry card).<br>
    
Initial territory ownership and occupancy can be seeded with<br>
    [Territory ID 1-42]=[R|B][Occupancy] (eg. ?1=R3 begins the game with Territory 1 Alaska owned by Red with 3 troops in it.)     <br>
    If any territories are seeded through get parameters, any territories not seeded will be designated neutral. This will also skip the setup phase.<br>




<h3>Sample Query Strings: (All create local games)</h3>

**Half Red/Half Blue**<br>
http://jwork967.kutztown.edu:8080/Risk/?1=B3&2=R3&3=B3&4=R3&5=B3&6=R3&7=B3&8=R3&9=B3&10=R3&11=B3&12=R3&13=B3&14=R3&15=B3&16=R3&17=B3&18=R3&19=B3&20=R3&21=B3&22=R3&23=B3&24=R3&25=B3&26=R3&27=B3&28=R3&29=B3&30=R3&31=B3&32=R3&33=B3&34=R3&35=B3&36=R3&37=B3&38=R3&39=B3&40=R3&41=B3&42=R3

**Half Red/Half Blue, 3 cards each**<br>
http://jwork967.kutztown.edu:8080/Risk/?1=R3&2=R3&3=R3&4=R3&5=R3&6=R3&7=R3&8=R3&9=R3&10=R3&11=R3&12=R3&13=R3&14=R3&15=R3&16=R3&17=R3&18=R3&19=R3&20=R3&21=R3&22=R3&23=B3&24=B3&25=B3&26=B3&27=B3&28=B3&29=B3&30=B3&31=B3&32=B3&33=B3&34=B3&35=B3&36=B3&37=B3&38=B3&39=B3&40=B3&41=B3&42=B3&RC=0W&RC=2C&RC=3A&BC=0W&BC=6I&BC=8C

**41 Red/1 Blue**<br>
http://jwork967.kutztown.edu:8080/Risk/?1=R3&2=R3&3=R3&4=R3&5=R3&6=R3&7=R3&8=R3&9=R3&10=R3&11=R3&12=R3&13=R3&14=R3&15=R3&16=R3&17=R3&18=R3&19=R3&20=R3&21=R3&22=R3&23=R3&24=R3&25=R3&26=R3&27=R3&28=R3&29=R3&30=R3&31=R3&32=R3&33=R3&34=R3&35=R3&36=R3&37=R3&38=R3&39=R3&40=R3&41=R3&42=B1

**3 Cards each, territories unseeded.**<br>
http://jwork967.kutztown.edu:8080/Risk/?RC=0W&RC=2C&RC=3A&BC=0W&BC=6I&BC=8C

**1 Human, 1 Bot, 2 second delay.**<br>
http://jwork967.kutztown.edu:8080/Risk/?players=2&bots=1&delay=2000



<h2>Cheats/Testing:</h2>

In-game cheat options are available through the Cheat button at the top-left. Available cheats are:<br>
**Win next battles:** The player rolls 7s during every battle in this turn.<br>
**Add Card:** Displays an interface at the bottom of the screen where the player can create a card to add to their hand. Disabled if the player has 5 cards in hand.<br>
**Win the game:** Conquers every territory and ends the game.<br>
