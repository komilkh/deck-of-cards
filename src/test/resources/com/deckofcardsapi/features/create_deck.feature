@runThis
Feature: Create Deck Test Cases


	Scenario Outline: Create Deck Positive
		Given User creates <deck_count> number of decks of cards shuffled "<isShuffled>"
		Then Decks of Card should have <deck_count> times 52 cards
		And Deck of Cards shuffle status must be "<isShuffled>"
		
		Examples:
		| deck_count 	| isShuffled 	|
		| 1						| true				|
		| 6						| true				|
		| 20					| true				|
		| -3					| true				|
		| 0						| true				|		
		| 2						| false				|
		| 10					| false				|
		| 20					| false				|
		| -10					| false				|
		| 0						| false				|
	
	
	Scenario Outline: Create Deck Negative
		Then If Number of Decks <deck_count> is over twenty and shuffled "<isShuffled>" success should be false and give error message
		
		Examples:
		
		| deck_count 	| isShuffled 	|
		| 25					| true				|
		| 30					| false				|
		

	Scenario: Create One Deck with Jokers
		Given User creates one unshuffled deck with jokers
		Then Number of cards is 54
		And Last two drawn cards are jokers
		
		
		