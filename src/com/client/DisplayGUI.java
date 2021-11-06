/** File: DisplayGUI.java 
 * @author Joseph Workoff
 * Major: CS/SW MS
 * Creation Date: 09/24/2020
 * Due Date: 12/09/2020
 * Professor: Dr. Spiegel
 * Course: CSC421
 * Assignment Number: 3
 * Purpose: DisplayGUI Class for creating and manipulating the GUI
*/

package com.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Image;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.ArrayList;


/**
 * GUI Class. Contains all panels and widgets for gameplay, and methods for manipulating them. 
 */
public class DisplayGUI{

    private VerticalPanel northPanel;
    private VerticalPanel westPanel;
    private VerticalPanel eastPanel;
    private VerticalPanel southPanel;
    private ScrollPanel southScrollPanel;
    private ScrollPanel centerPanel;

    private Image mapImage;

    private Label header;
    private Label mapHeader;

    private VerticalPanel cardPanel;
    private Button[] cardButtons;

    private VerticalPanel actionPanel;
    private Button[] actionButtons;

    private TextArea historyBox;

    private HorizontalPanel eastPanelColumns;
    private VerticalPanel continentColumn1;
    private VerticalPanel continentColumn2;
    private VerticalPanel[] continentPanels;
    private HorizontalPanel[] territoryPanels;
    private TextBox[] territoryOccTextBoxes;
    private Button[] territoryButtons;

    private DockLayoutPanel layoutPanel;

    private HorizontalPanel placedPanel;
    private HorizontalPanel battlePanel;

    private TextBox actionDisplay;
    private TextBox placedDisplay;
    private TextBox selectedDisplay;
    private TextBox targetDisplay;


    private VerticalPanel rollPanel;
    private HorizontalPanel diceSelectionPanel;
    private HorizontalPanel dicePanel;

    private TextBox rollDisplay;
    private TextBox diceDisplay;
    private Button diceSubmitButton;
    private RadioButton[] diceRollButtons;


    private HorizontalPanel inputPanel;
    private TextBox inputBox;
    private Button submitButton;

    private HorizontalPanel playerPanel;

    private TextBox playingAsDisplay;
    private TextBox playerDisplay;
    private TextBox debugDisplay;

    private HorizontalPanel cheatPanel;

    private Button cheatMenuButton;

    private Button devButton;

    private Button[] cheatButtons;


    private HorizontalPanel cardTurnInPanel;
    private TextBox[] cardTextBoxes;
    private Button cardTurnInButton;


    private HorizontalPanel cardCheatPanel;
        
    private RadioButton[] cardDesignButtons;

    private TextBox cardCheatInput;
    private Button cardCheatSubmit;

    private Logger logger;


    public DisplayGUI(){}

    /**
     * DisplayGUI constructor. Creates every GUI Panel and Widget.
     */
    public DisplayGUI(Board board){


        this.mapImage = new Image();
        this.mapImage.setUrl("https://i.imgflip.com/16sn4c.jpg");

        this.northPanel = new VerticalPanel();
        this.westPanel = new VerticalPanel();
        this.eastPanel = new VerticalPanel();
        this.southPanel = new VerticalPanel();


        // Top Panel/

        this.playerPanel = new HorizontalPanel();

        this.playingAsDisplay = new TextBox();
        this.playingAsDisplay.setText("");
        this.playingAsDisplay.setEnabled(false);
        this.playingAsDisplay.setVisibleLength(15);

        this.playerPanel.add(playingAsDisplay);

        this.playerDisplay = new TextBox();
        this.playerDisplay.setText("");
        this.playerDisplay.setEnabled(false);
        this.playerDisplay.setVisibleLength(15);
        
        this.playerPanel.add(playerDisplay);

        this.debugDisplay = new TextBox();
        this.debugDisplay.setText("");
        this.debugDisplay.setEnabled(false);
        this.debugDisplay.setVisibleLength(500);
        this.debugDisplay.setVisible(false);

        this.playerPanel.add(debugDisplay);

        this.northPanel.add(playerPanel);

        this.cheatPanel = new HorizontalPanel();

        this.devButton = new Button("Test");
        this.devButton.setVisible(false);
        this.cheatPanel.add(devButton);

        this.cheatMenuButton = new Button("Cheat");
        this.cheatPanel.add(cheatMenuButton);

        this.cheatButtons = new Button[3];

        this.cheatButtons[0] = new Button("Win Next Battles");
        this.cheatButtons[1] = new Button("Add Card");
        this.cheatButtons[2] = new Button("Win the Game");

        for (Button b: this.cheatButtons){
            b.setVisible(false);
            this.cheatPanel.add(b);
        }

        this.northPanel.add(this.cheatPanel);

        // /Top Panel


        // West Panel/

        //Cards UI
        this.cardPanel = new VerticalPanel();
        this.cardButtons = new Button[5];

        this.cardButtons[0] = new Button(new String(" "));
        this.cardButtons[1] = new Button(new String(" "));
        this.cardButtons[2] = new Button(new String(" "));
        this.cardButtons[3] = new Button(new String(" "));
        this.cardButtons[4] = new Button(new String(" "));

        for (Button b: cardButtons){
            b.setEnabled(false);
            b.setWidth("200px");

            cardPanel.add(b);
        }

        cardPanel.getElement().setId("cardPanel");

        westPanel.getElement().setId("westPanel");
        westPanel.add(cardPanel);


        //Actions UI
        this.actionPanel = new VerticalPanel();
        this.actionButtons = new Button[5];

        this.actionButtons[0] = new Button(new String("Start Turn"));
        this.actionButtons[1] = new Button(new String("Reinforce"));
        this.actionButtons[2] = new Button(new String("Battle"));
        this.actionButtons[3] = new Button(new String("Pass"));
        this.actionButtons[4] = new Button(new String("Turn In Cards"));

        for (Button b: actionButtons){
            b.setEnabled(false);
            b.setWidth("200px");
        }
        

        actionPanel.add(this.actionButtons[0]);
        actionPanel.add(this.actionButtons[4]);
        actionPanel.add(this.actionButtons[1]);
        actionPanel.add(this.actionButtons[2]);
        actionPanel.add(this.actionButtons[3]);

        this.actionPanel.getElement().setId("actionPanel");

        this.westPanel.add(actionPanel);


        this.historyBox = new TextArea();
        this.historyBox.setCharacterWidth(40);
        this.historyBox.setVisibleLines(30);
        this.historyBox.setEnabled(false);

        this.historyBox.getElement().setId("history");


        this.westPanel.add(this.historyBox);


        // /West Panel


        // East Panel/

        //Territories UI
        this.eastPanelColumns = new HorizontalPanel();

        this.continentColumn1 = new VerticalPanel();
        this.continentColumn2 = new VerticalPanel();

        this.continentPanels = new VerticalPanel[6];
        this.territoryPanels = new HorizontalPanel[42];
        this.territoryOccTextBoxes = new TextBox[42];
        this.territoryButtons = new Button[42];


        //loop through each continent
        int cID, tID = 0;

        for (Continent cont: board.getContinents()){
            cID = cont.getID() - 1;

            //create continent's panel
            this.continentPanels[cID] = new VerticalPanel();
            this.continentPanels[cID].addStyleName("continentPanel");

            //loop through continent's territories
            for (Territory terr: cont.getTerritories()){
                tID = terr.getID() - 1;

                //create territory's panel
                this.territoryPanels[tID] = new HorizontalPanel();

                //create territory's label
                this.territoryOccTextBoxes[tID] = new TextBox();
                this.territoryOccTextBoxes[tID].setText("000");
                this.territoryOccTextBoxes[tID].setVisibleLength(2);
                this.territoryOccTextBoxes[tID].setEnabled(false);

                terr.setOccTextBox(this.territoryOccTextBoxes[tID]);

                //create territory's button
                this.territoryButtons[tID] = new Button(terr.getID() + "." + terr.getName());
                this.territoryButtons[tID].setEnabled(false);
                this.territoryButtons[tID].setWidth("200px");
                terr.setButton(this.territoryButtons[tID]);

                //add them to territory's panel
                this.territoryPanels[tID].add(territoryOccTextBoxes[tID]);
                this.territoryPanels[tID].add(territoryButtons[tID]);

                //add territory's panel to continent's panel
                this.continentPanels[cID].add(territoryPanels[tID]);
            }
        }

        //Add each Continent Panel to the two columns
        this.continentColumn1.add(continentPanels[0]);
        this.continentColumn1.add(continentPanels[1]);
        this.continentColumn1.add(continentPanels[2]);
        this.continentColumn2.add(continentPanels[3]);
        this.continentColumn2.add(continentPanels[4]);
        this.continentColumn2.add(continentPanels[5]);

        //set continent column html ids
        this.continentColumn1.getElement().setId("continentColumn1");
        this.continentColumn2.getElement().setId("continentColumn2");

        //add columns to columns panel
        this.eastPanelColumns.add(continentColumn1);
        this.eastPanelColumns.add(continentColumn2);

        //add columns panel to east panel
        this.eastPanel.add(eastPanelColumns);

        // /East Panel


        // South Panel/

        //Bottom Text Display

        // this.reinforcePanel = new VerticalPanel();
        // this.selectionPanel = new VerticalPanel();
        // this.battlePanel = new VerticalPanel();


        this.actionDisplay = new TextBox();
        this.placedDisplay = new TextBox();
        this.selectedDisplay = new TextBox();
        this.targetDisplay = new TextBox();

        this.actionDisplay.setEnabled(false);
        this.placedDisplay.setEnabled(false);
        this.selectedDisplay.setEnabled(false);
        this.targetDisplay.setEnabled(false);



        //Roll Panel

        this.rollPanel = new VerticalPanel();

        this.rollDisplay = new TextBox();
        this.rollDisplay.setEnabled(false);
        this.rollDisplay.setVisibleLength(70);

        this.rollPanel.add(rollDisplay);

        this.dicePanel = new HorizontalPanel();

        this.diceDisplay = new TextBox();
        this.diceDisplay.setEnabled(false);
        this.diceDisplay.setVisibleLength(30);

        this.dicePanel.add(diceDisplay);

        this.diceRollButtons = new RadioButton[3];

        this.diceRollButtons[0] = new RadioButton("diceRollButtons", "Retreat");
        this.diceRollButtons[1] = new RadioButton("diceRollButtons", "1");
        this.diceRollButtons[2] = new RadioButton("diceRollButtons", "2");
        this.diceRollButtons[3] = new RadioButton("diceRollButtons", "3");

        this.diceSelectionPanel = new HorizontalPanel();


        for (RadioButton r: this.diceRollButtons){
            this.diceSelectionPanel.add(r);
        }

        this.diceSubmitButton = new Button("Roll");

        this.diceSelectionPanel.add(diceSubmitButton);

        this.dicePanel.add(this.diceSelectionPanel);

        this.rollPanel.add(this.dicePanel);




        this.actionDisplay.getElement().setId("actionDisplay");
        this.placedDisplay.getElement().setId("placedDisplay");
        this.selectedDisplay.getElement().setId("selectedDisplay");
        this.targetDisplay.getElement().setId("targetDisplay");
        this.rollDisplay.getElement().setId("rollDisplay");

        this.actionDisplay.setVisibleLength(79);
        this.placedDisplay.setVisibleLength(90);
        this.selectedDisplay.setVisibleLength(35);
        this.targetDisplay.setVisibleLength(35);


        this.placedDisplay.setText("PLACED DISPLAY");
        this.selectedDisplay.setText("SELECTED DISPLAY");
        this.targetDisplay.setText("TARGET DISPLAY");


        this.placedPanel = new HorizontalPanel();
        this.placedPanel.add(placedDisplay);

        this.battlePanel = new HorizontalPanel();
        this.battlePanel.add(selectedDisplay);
        this.battlePanel.add(targetDisplay);


        this.inputPanel = new HorizontalPanel();
        this.inputBox = new TextBox();
        this.inputBox.setEnabled(true);
        this.inputBox.setVisibleLength(3);

        this.submitButton = new Button("Submit");

        this.inputPanel.add(this.inputBox);
        this.inputPanel.add(this.submitButton);


        //Card Turn-In Display
        this.cardTurnInPanel = new HorizontalPanel();
        
        this.cardTextBoxes = new TextBox[3];
        this.cardTextBoxes[0] = new TextBox();
        this.cardTextBoxes[1] = new TextBox();
        this.cardTextBoxes[2] = new TextBox();

        this.cardTurnInButton = new Button("Turn In");


        for (TextBox t: this.cardTextBoxes){
            t.setVisibleLength(20);
            t.setEnabled(false);
            t.setVisible(false);

            this.cardTurnInPanel.add(t);
        }

        this.cardTurnInPanel.add(cardTurnInButton);
        this.cardTurnInPanel.setVisible(false);




        //Card Cheat Display
        this.cardCheatPanel = new HorizontalPanel();
        
        this.cardDesignButtons = new RadioButton[3];

        this.cardDesignButtons[0] = new RadioButton("cardDesignButtons", "WILD");
        this.cardDesignButtons[1] = new RadioButton("cardDesignButtons", "INFANTRY");
        this.cardDesignButtons[2] = new RadioButton("cardDesignButtons", "CAVALRY");
        this.cardDesignButtons[3] = new RadioButton("cardDesignButtons", "ARTILLERY");

        this.cardCheatInput = new TextBox();
        this.cardCheatInput.setEnabled(true);
        this.cardCheatInput.setVisibleLength(10);

        this.cardCheatSubmit = new Button("Add Card");

        
        this.cardCheatPanel.add(this.cardCheatInput);

        for (RadioButton r: this.cardDesignButtons){
            this.cardCheatPanel.add(r);
            r.setVisible(false);
        }

        this.cardCheatPanel.add(this.cardCheatSubmit);




        this.southPanel.add(this.actionDisplay);
        this.southPanel.add(this.placedDisplay);
        this.southPanel.add(this.battlePanel);
        this.southPanel.add(this.inputPanel);
        this.southPanel.add(this.rollPanel);
        this.southPanel.add(this.cardTurnInPanel);
        this.southPanel.add(this.cardCheatPanel);

        this.southScrollPanel = new ScrollPanel();
        this.southScrollPanel.add(this.southPanel);

        // /South Panel


        //Main Layout Panel
        this.layoutPanel = new DockLayoutPanel(Unit.EM);
        this.layoutPanel.addNorth(northPanel, 5);
        this.layoutPanel.addWest(westPanel, 25);
        this.layoutPanel.addEast(eastPanel, 42);
        this.layoutPanel.addSouth(southScrollPanel, 15);


        this.centerPanel = new ScrollPanel();
        this.centerPanel.add(mapImage);

        this.layoutPanel.add(centerPanel);

        this.logger = Logger.getLogger("");

    }



    public DockLayoutPanel getLayoutPanel(){return this.layoutPanel;}



    // Button Getters/

    public Button[] getCardButtons(){return this.cardButtons;}

    public Button getCardTurnInButton(){return this.cardTurnInButton;}
    public Button[] getActionButtons(){return this.actionButtons;}

    public Button getCheatMenuButton(){return this.cheatMenuButton;}
    public Button[] getCheatButtons(){return this.cheatButtons;}

    public Button getCardCheatSubmit(){return this.cardCheatSubmit;}

    public Button getSubmitButton(){return this.submitButton;}
    public Button getDiceSubmitButton(){return this.diceSubmitButton;}

    public Button getDevButton(){return this.devButton;}



    // /Getters


    /**
     * Sets the text for the Main Text Box
     * @param prompt - Prompt String
     */
    public void print(String prompt){
        this.actionDisplay.setText(prompt);
    }

    /**
     * Sets the text for the Allocation Placement Text Box
     * @param prompt - Prompt String
     */
    public void printPlaced(String prompt){
        this.placedDisplay.setText(prompt);
    }

    /**
     * Sets the text for the Selected Territory Text Box
     * @param prompt - Prompt String
     */
    public void printSelected(String prompt){
        this.selectedDisplay.setText(prompt);
    }

    /**
     * Sets the text for the Targeted Territory Text Box
     * @param prompt - Prompt String
     */
    public void printTarget(String prompt){
        this.targetDisplay.setText(prompt);
    }

    /**
     * Sets the text for the Roll Display Text Box
     * @param prompt - Prompt String
     */
    public void printRoll(String prompt){
        this.rollDisplay.setText(prompt);
    }

    /**
     * Sets the text for the Player Display Text Box
     * @param prompt - Prompt String
     */
    public void printPlayer(String prompt){
        this.playerDisplay.setText(prompt);
    }

    /**
     * Sets the text for the Playing-As Display Text Box
     * @param prompt - Prompt String
     */
    public void printPlayAs(String prompt){
        this.playingAsDisplay.setText(prompt);
    }

    /**
     * Sets the text for the Debug Display Text Box
     * @param prompt - Prompt String
     */
    public void printDebug(String prompt){
        this.debugDisplay.setVisible(true);
        // this.debugDisplay.setText(prompt);
        this.debugDisplay.setText(this.debugDisplay.getText() + " " + prompt);
    }

    /**
     * Displays the attacker's and defender's rolls from a battle.
     */
    public void printRolls(ArrayList<Integer> attackDice, ArrayList<Integer> defenseDice){
        String out = new String("");
        for (Integer i : attackDice){
            out += ("[" + i + "]");
        }
        out += " | ";
        for (Integer i : defenseDice){
            out += ("[" + i + "]");
        }

        this.diceDisplay.setText(out);
    }

    /**
     * Sets the text for the dice display text box
     * @param prompt
     */
    public void printRolls(String prompt){
        this.diceDisplay.setText(prompt);
    }

    
    /**
     * Sets the text for the first card text box.
     * @param prompt
     */
    public void printCards(String prompt) {
        this.cardTextBoxes[0].setText(prompt);
    }

    /**
     * Adds a message to the history log.
     * @param event
     */
    public void logEvent(String event){
        this.historyBox.setText(this.historyBox.getText() + "\n" + event);
    }



    /**
     * Displays the Card Turn-In UI.
     * Enables the full Card Buttons.
     * Disables continuing if the player's hand size >= 5
     * @param playerHand
     */
    public void displayCardTurnInMenu(int playerHandSize, ArrayList<Card> turnInCards) {

        toggleTurnInButton(false);

        this.print("Choose 3 matching cards to turn in.");

        // int playerHandSize = playerHand.size();

        for (int i = 0; i < playerHandSize; i++){
            this.cardButtons[i].setEnabled(true);
        }

        
        //forbid continuing until hand size < 5
        if (playerHandSize >= 5){
            toggleReinforceButton(false);
        }

        this.cardTurnInPanel.setVisible(true);

        for (TextBox t: this.cardTextBoxes){
            t.setVisible(true);
            t.setText("");
        }

        int i = 0;
        for (Card c: turnInCards){
            this.cardTextBoxes[i].setText(c.getCardString());
            i++;
        }
    }



    /**
     * Gets the integer input from the Input textbox.
     * Error checks for non-numeric input.
     * @param source - 0: Allocation Input Box, 1: Card Cheat Input Box
     * @return Input int, or -999 upon error.
     */
    public int getInputInt(int source){
        int num = 0;

        String str = "";

        //Allocation Input
        if (source == 0){
            str = this.inputBox.getText();
            this.inputBox.setText("");
        }
        //Card Cheat Input
        else if (source == 1){
            str = this.cardCheatInput.getText();
            this.cardCheatInput.setText("");
        }

        return Util.parseInt(str);
    }


    /**
     * Returns the currently selected card design
     * @return Card Design [INFANTRY|ARTILLERY|CAVALRY]
     */
    public String getCardRadioButtons(){
        String str = "";
        for (RadioButton r: this.cardDesignButtons){
            if (r.getValue()){
                r.setValue(false);
                str = r.getText();
            }
        }
        return str;
    }

    

    /**
     * Gets the selected option from the dice radio buttons, then deselects that button.
     * @return Selected roll option
     */
    public String getRoll(){
        String str = "";
        for (RadioButton r: this.diceRollButtons){
            if (r.getValue()){
                r.setValue(false);
                str = r.getText();
            }
        }
        return str;
    }



    /**
     * Toggles display of the cheat buttons.
     * @param toggle
     */ 
    public void toggleCheatMenu(){
        boolean display = !this.cheatButtons[0].isVisible();
        for (Button button : this.cheatButtons){
            button.setVisible(display);
        }
        
    }


    /**
     * Toggles display of the Territory Selection Text Box
     * @param toggle
     */
    public void displaySelectionDisplay(boolean toggle) {
        this.placedPanel.setVisible(toggle);
        this.placedDisplay.setVisible(toggle);
    }


    /**
     * Toggles display of the Allocation Input Panel
     * @param toggle
     */ 
    public void displayInputPanel(boolean toggle) {
        this.inputPanel.setVisible(toggle);
        this.inputBox.setFocus(toggle);
    }

    
    /**
     * Toggles display of the Battle UI.
     * @param toggle
     */ 
    public void displayBattlePanel(boolean toggle) {
        this.battlePanel.setVisible(toggle);
    }

    /**
     * Toggles display of the Roll UI
     * @param toggle
     */ 
    public void displayRollPanel(boolean toggle) {
        this.rollPanel.setVisible(toggle);
    }

    public void displayDiceMenu(boolean toggle) {
        this.diceSelectionPanel.setVisible(toggle);
    }

    /**
     * Toggles display of the Card Cheat UI
     * @param toggle
     */ 
    public void displayCardCheatPanel(boolean toggle) {
        this.cardCheatPanel.setVisible(toggle);
    }

    /**
     * Displays a popup announcing victory for the player.
     * @param player
     */
    public void displayVictory(Player player, String timestamp){
        Window.alert(player.getColor() + " has won the game!\nDuration: " + timestamp);
    }


    /**
     * Displays options for dice to roll during battle based on territory occupancy and attacking or defending.
     * @param attacking - True if attacking.
     */
    public void displayRollOptions(Territory terr, boolean attacking, int maxDice){
        
        this.displayDiceMenu(true);

        this.diceRollButtons[0].setVisible(false);
        this.diceRollButtons[1].setVisible(false);
        this.diceRollButtons[2].setVisible(false);
        this.diceRollButtons[3].setVisible(false);

        if (attacking){
            this.diceRollButtons[0].setVisible(true); //allow retreat
            // maxDice = (terr.getOccupancy() >= 4) ? 3 : terr.getOccupancy() - 1;
            this.printRoll(terr.getOwner().getColor() + ": Select a number of dice to attack with");
        }
        else{
            // maxDice = (terr.getOccupancy() >= 2) ? 2 : 1;
            this.printRoll(terr.getOwner().getColor() + ": Select a number of dice to defend with");
        }

        for (int i = 1; i <= maxDice; i++){
            this.diceRollButtons[i].setVisible(true);
        }

    }

    /**
     * Enables the corresponding buttons for all of the turnPlayer's owned territories
     * @param player
     */
    public void activateOwnedButtons(Player player){
        cleanTerritoryButtons();
        for (Territory terr: player.getTerritories()){
            terr.getButton().setEnabled(true);
        }
    }



    /**
     * Enables buttons for territories that the player can attack with.
     * @param player
     */
    public void activateSelectableButtons(Player player) {
        cleanTerritoryButtons();
        for (Territory terr: player.getTerritories()){
            if (terr.getOccupancy() > 1 && terr.enemyAdjacent()){
                terr.getButton().setEnabled(true);
            }
        }
    }

    /**
     * Enables the buttons for all of a Territory's neighbors owned by the enemy.
     * @param terr
     */
    public void activateTargetableEnemyButtons(Territory terr){
        cleanTerritoryButtons();
        terr.getButton().setEnabled(true);
        for (final Territory t: terr.getAdjacencies()){
            if (t.getOwner() != terr.getOwner()){
                t.getButton().setEnabled(true);
            }
        }
    }

    /**
     * Disables all Territory buttons
     */
    public void cleanTerritoryButtons(){
        for (Button b: this.territoryButtons){
            b.setEnabled(false);
        }
    }

    /**
     * Disables all Territory buttons except for 1.
     */
    public void cleanTerritoryButtons(Territory terr){

        Button skip = terr.getButton();

        for (Button b: this.territoryButtons){
            if (b != skip){
                b.setEnabled(false);
            }
        }
    }



    /**
     * Disables all Card Buttons.
     */
    public void cleanCardButtons(){
        for (Button b: this.cardButtons){
            b.setEnabled(false);
        }
    }



    /**
     * Resets the UI.
     * Clears all text displays.
     * Removes all phase UIs
     */
    public void cleanDisplay(){
        this.print("");
        this.printPlaced("");
        this.printSelected("");
        this.printTarget("");
        this.printRoll("");
        this.printRolls("");

        this.inputBox.setText("");
        this.inputBox.setFocus(false);

        this.cardCheatInput.setText("");

        this.displayBattlePanel(false);
        this.displayCardCheatPanel(false);
        this.displayInputPanel(false);
        this.displayRollPanel(false);
        this.displaySelectionDisplay(false);

        this.cardTurnInPanel.setVisible(false);
        this.placedDisplay.setVisible(false);
        this.battlePanel.setVisible(false);
        this.rollPanel.setVisible(false);
        this.diceSelectionPanel.setVisible(false);
        this.inputPanel.setVisible(false);
        this.cardCheatPanel.setVisible(false);

        this.actionButtons[4].setEnabled(false);
        this.actionButtons[4].setVisible(false);

        cleanCardButtons();

    }



    /**
     * Toggles the use of the Start Turn button.
     * @param toggle
     */
    public void toggleStartButton(boolean toggle){
        this.actionButtons[0].setEnabled(toggle);
    }

    /**
     * Toggles the use of the Reinforce button.
     * @param toggle
     */
    public void toggleReinforceButton(boolean toggle){
        this.actionButtons[1].setEnabled(toggle);
    }

    /**
     * Toggles the use of the Battle button.
     * @param toggle
     */
    public void toggleBattleButton(boolean toggle){
        this.actionButtons[2].setEnabled(toggle);
    }

    /**
     * Toggles the use of the Pass button.
     * @param toggle
     */
    public void togglePassButton(boolean toggle){
        this.actionButtons[3].setEnabled(toggle);
    }

    /**
     * Toggles the use and visibility of the Turn In button.
     * @param toggle
     */
    public void toggleTurnInButton(boolean toggle){
        this.actionButtons[4].setEnabled(toggle);
        this.actionButtons[4].setVisible(toggle);
    }


    
    /**
     * Click handler for Add Card Cheat Button
     * Displays the Card Cheat UI
     */
    public void displayCardCheatMenu(int size){

        if (size < 5){
            this.cardCheatPanel.setVisible(!this.cardCheatPanel.isVisible());
            for (RadioButton r: this.cardDesignButtons){
                r.setVisible(true);
            }
        }
    }



    /**
     * Displays the player's cards in the Card Buttons.
     * @param cards
     */
    public void displayCards(ArrayList<Card> cards){
        int i = 0;

        for (Button b: this.cardButtons){
            b.setText("");
        }

        for (Card c: cards){
            this.cardButtons[i].setText(c.getCardString());
            i++;
        }
    }

    
    /**
     * Disables the action buttons
     */
    public void cleanActionButtons(){
        for (Button b: this.actionButtons){
            b.setEnabled(false);
        }
    }
}