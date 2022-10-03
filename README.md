# Risk
Google Web Toolkit-based browser Risk game hosted using Apache Tomcat<br>

Currently unavailable while migrating platforms.

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

Local games are played with two players at one screen, or one player against an AI player.<br>

?online parameter queues for an internet game, which will begin when two clients access the URL at once. No other URL options will be applied if this option exists.<br>

Local games support the following setup parameters:<br>

Bots can be enabled with " ?bots=[1|2] ". <br>
The delay before bots' actions can be changed with " ?bots=[1|2]&delay=[time in ms]] ".<br>

Cards can be given to players at the start of the game with<br>
    [RC|BC]=[Territory ID 1-42][ACIW] (eg. ?RC=2I begins the game with Red having an Alberta/Infantry card).<br>
    
Initial territory ownership and occupancy can be seeded with<br>
    [Territory ID 1-42]=[R|B][Occupancy] (eg. ?1=R3 begins the game with Territory 1 Alaska owned by Red with 3 troops in it.)     <br>
    If any territories are seeded through get parameters, any territories not seeded will be designated neutral. This will also skip the setup phase.<br>


<h2>Cheats/Testing:</h2>

In-game cheat options are available through the Cheat button at the top-left. Available cheats are:<br>
**Win next battles:** The player rolls 7s during every battle in this turn.<br>
**Add Card:** Displays an interface at the bottom of the screen where the player can create a card to add to their hand. Disabled if the player has 5 cards in hand.<br>
**Win the game:** Conquers every territory and ends the game.<br>
