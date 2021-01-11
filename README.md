Prerequisites
You must have JDK 8, MAVEN installed in your device.


Overview.
We have http://deckofcardsapi.com/ API that has features of creating decks of cards and multiple cards can be drawn from the decks.
We are testing creating and drawing cards from decks features of the application.


Running Tests.
Run "mvn verify" in your project level folder.
You may specify what tests to run by placing a @runThis in any of scenarios or features of feature files that are locaed under src/test/resources/com/deckofcardapi/features. 
By default all the tests will run.


Test Result Reports.
You can find html reports under target/pretty-html-report/cucumber-html-reports after running tests through mvn



Test Cases.
Here is the Cucumber feature file contents to quckly glance at test cases:
______________________________________________________________________________________________________________
Feature: Create Deck Test Cases

	Scenario Outline: Create Deck Positive
		Given User creates <deck_count> number of decks of cards shuffled "<isShuffled>"
		Then Decks of Card should have <deck_count> times 52 cards
		And Deck of Cards shuffle status must be "<isShuffled>"
		
		Examples:
		| deck_count 			| isShuffled 	|
		| 1				| true		|
		| 6				| true		|
		| 20				| true		|
		| -3				| true		|
		| 0				| true		|		
		| 2				| false		|
		| 10				| false		|
		| 20				| false		|
		| -10				| false		|
		| 0				| false		|
	
	
	Scenario Outline: Create Deck Negative
		Then If Number of Decks <deck_count> is over twenty and shuffled "<isShuffled>" success should be false and give error message
		
		Examples:		
		| deck_count 	| isShuffled 	|
		| 25		| true		|
		| 30		| false		|
		

	Scenario: Create One Deck with Jokers
		Given User creates one unshuffled deck with jokers
		Then Number of cards is 54
		And Last two drawn cards are jokers
    
_____________________________________________________________________________________________________________________
    
Feature: Draw Cards

	Scenario: Drawn cards are correct
		Given User creates a new deck of cards with random allowed number of decks
		When User draws random number of cards between one and total number of cards
		Then Remaining number of cards are correct
		And All Drawn cards in the right format




