#@temp
Feature: Draw Cards

	Scenario: Number of drawn cards are correct
		Given User creates a new deck of cards with random allowed number of decks
		When User draws random number of cards between one and total number of cards
		Then Remaining number of cards are correct
		And All Drawn cards in the right format