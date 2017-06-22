package rouletteSetup;

/**
 * This class creates the actual Roulette table, sets it up, defines everything
 * on the table, prints a desired result, and contains the code to run each strategy.
 * 
 * Numbers and lines are not formatted with Stringf because, well, it's not needed (all
 * bets are a whole number).
 * 
 * @author Chad Parker
 *
 */
import java.util.ArrayList;

import strategies.ColorBetProg;
import strategies.ColorBetProgOddEvenProgGreenFlat;
import strategies.ColorBetProgPercentage;
import strategies.ColorBetProgOddEvenProgGreenAdd;
import strategies.ColorBetProgWithGreenFlat;
import strategies.ColorBetProgWithOddEvenProg;
import strategies.GreenBetAddXTurns;
import strategies.GreenBetFlatXTurns;

public class RouletteTable {

	private double highestTurn = 0;
	private int greenGap = 0;

	private ArrayList<Integer> list = new ArrayList<>(); // instantiates a list
															// of variables to
															// use as the
															// roulette numbers

	private int numberOfTurns = 1000; // change this number to change number of
										// rolls per game/session
	private int numberGap = 0;
	private int runTimes = 0;
	private double percentage = 47; // percentage of color:alternat-color before
									// betting
	private double highestCashout = 0;
	private double averageCashout = 0; // average cash obtained per game/session
	private double numBrokeChance = 0; // chance to go broke per game/session
	private double cashTotal = 0;
	private double startingCash = 5000; // how much cash to start with
	private double minBet = 1; // minimum bet amount
	private double MAX_BET = 2000; // max outside bet amount
	private double MAX_OTHER = 50; // max inside bet amount
	private int colorTurnsToWait = 5; // waits for certain color to come up this
										// many times before betting on opposite
										// one
	private int greenTurnsToWait = 45; // turns to wait before betting on the
										// zeros (green numbers)
	private double brokeTotal = 0;
	private int numRuns = 1; // number of games to run

	/**
	 * Initializes the array, pre-adding each roulette spin (depending on
	 * how many rolls you want) as a number.
	 */
	public RouletteTable() {

		// creates a list of numbers 0-37, acting as the upcoming roulette spins
		while (this.list.size() < this.numberOfTurns) {

			int RANDOM = (int) (Math.random() * 38);
			this.list.add(RANDOM);
		}
		// Uncomment next line if you want to see all of the individual rolls in
		// console (not recommended for large numbers of games!)
		// System.out.println(this.list);
	}

	/**
	 * This method contains all the betting sequences. If you don't want to see
	 * certain ones, comment them out first, as shown.
	 */
	public void run() {

		this.runGreenBetAddXTurns();
		// this.runGreenBetFlatXTurns();
		// this.runColorBetProg();
		// this.runColorBetProgPercentage();
		// this.runColorBetProgWithGreenFlat();
		// this.runColorBetProgWithGreenAdd();
		// this.runColorBetProgWithOddEvenProg();
		// this.runColorBetProgOddEvenProgGreenAdd();
		// this.runColorBetProgOddEvenProgGreenFlat();

	}

	/**
	 * Runs the corresponding strategy (see in the package strategies for
	 * details on each specific strategy!).
	 */
	private void runGreenBetAddXTurns() {
		int numRunTimes = this.runTimes; // dummy clone of the number of games

		while (numRunTimes < this.numRuns) { // number of rolls per game

			/**
			 * This creates a clone of the GreenBetAddXTurns class and calls it
			 * strat1. Then it calls(runs) that strategy, passing along the data
			 * in parentheses to be used in the corresponding strategy
			 */
			GreenBetAddXTurns strat1 = new GreenBetAddXTurns(this.startingCash,
					this.greenTurnsToWait, this.minBet, this.MAX_OTHER);

			/**
			 * Figures up how much cash you walk away with at the end of the
			 * game by getting the data from the 'getFinalCashout' method in
			 * strat1 (GreenBetAddXTurns strategy)
			 */
			this.averageCashout = this.averageCashout
					+ strat1.getFinalCashOut();

			/**
			 * Determines what the most cash you had at any point of all the
			 * games/rolls.
			 */
			if (strat1.getHighestCashOut() > this.highestCashout) {
				this.highestCashout = strat1.getHighestCashOut();
			}

			/**
			 * Gets the percentage/chance that you went broke during your games
			 */
			this.numBrokeChance = this.numBrokeChance + strat1.getNumBroke();

			/**
			 * Gets the EXTRA cash that you leave with when you finish.
			 */
			this.cashTotal = this.cashTotal + strat1.getFinalCashOut()
					+ strat1.getBrokeTotal();
			/**
			 * Gets the number of times that you went broke during your games
			 */
			this.brokeTotal = this.brokeTotal + strat1.getBrokeTotal();

			/**
			 * Gets the most number of turns that actually pass before the color
			 * you bet on actually comes up again
			 */
			this.highestTurn = strat1.getHighestBetTurns();

			/**
			 * Gets the most number of turns that actually pass before the green
			 * number comes up again
			 */
			this.greenGap = strat1.getHighestBetTurns();

			numRunTimes++; // increments, signaling that one roll has passed
		}

		// Prints all results, along with corresponding strategy.
		System.out.println("\n\n::::: Additive GREEN bet @"
				+ this.greenTurnsToWait + " ::::: " + this.numRuns
				* this.numberOfTurns + " total rolls.");
		this.printResults();
		this.printGreenDistance();

	}

	/**
	 * Runs the corresponding strategy (see in the package strategies)
	 */
	private void runGreenBetFlatXTurns() {
		int numRunTimes = this.runTimes;

		while (numRunTimes < this.numRuns) {

			GreenBetFlatXTurns strat1 = new GreenBetFlatXTurns(
					this.startingCash, this.greenTurnsToWait, this.minBet);

			this.averageCashout = this.averageCashout
					+ strat1.getFinalCashOut();

			if (strat1.getHighestCashOut() > this.highestCashout) {
				this.highestCashout = strat1.getHighestCashOut();
			}

			this.numBrokeChance = this.numBrokeChance + strat1.getNumBroke();

			this.cashTotal = this.cashTotal + strat1.getFinalCashOut()
					+ strat1.getBrokeTotal();
			this.brokeTotal = this.brokeTotal + strat1.getBrokeTotal();
			this.highestTurn = strat1.getHighestBetTurns();
			this.greenGap = strat1.getHighestBetTurns();

			numRunTimes++;
		}
		System.out.println("\n\n::::: Flat GREEN bet @" + this.greenTurnsToWait
				+ " ::::: " + this.numRuns * this.numberOfTurns
				+ " total rolls.");
		this.printResults();
		this.printGreenDistance();

	}

	private void runColorBetProg() {
		int numRunTimes = this.runTimes;

		while (numRunTimes < this.numRuns) {

			ColorBetProg strat1 = new ColorBetProg(this.startingCash,
					this.colorTurnsToWait, this.minBet, this.MAX_BET);

			this.averageCashout = this.averageCashout
					+ strat1.getFinalCashOut();

			if (strat1.getHighestCashOut() > this.highestCashout) {
				this.highestCashout = strat1.getHighestCashOut();
			}

			this.numBrokeChance = this.numBrokeChance + strat1.getNumBroke();

			this.cashTotal = this.cashTotal + strat1.getFinalCashOut()
					+ strat1.getBrokeTotal();
			this.brokeTotal = this.brokeTotal + strat1.getBrokeTotal();
			this.highestTurn = strat1.getHighestBetTurns();

			numRunTimes++;
		}
		System.out.println("\n\n::::: Progressive COLOR bet @"
				+ this.colorTurnsToWait + " ::::: " + this.numRuns
				* this.numberOfTurns + " total rolls.");
		this.printResults();
		this.printSequentialColors();

	}

	private void runColorBetProgPercentage() {
		int numRunTimes = this.runTimes;

		while (numRunTimes < this.numRuns) {

			ColorBetProgPercentage strat1 = new ColorBetProgPercentage(
					this.startingCash, this.colorTurnsToWait, this.minBet,
					this.percentage, this.MAX_BET);

			this.averageCashout = this.averageCashout
					+ strat1.getFinalCashOut();

			if (strat1.getHighestCashOut() > this.highestCashout) {
				this.highestCashout = strat1.getHighestCashOut();
			}

			this.numBrokeChance = this.numBrokeChance + strat1.getNumBroke();

			this.cashTotal = this.cashTotal + strat1.getFinalCashOut()
					+ strat1.getBrokeTotal();
			this.brokeTotal = this.brokeTotal + strat1.getBrokeTotal();
			this.highestTurn = strat1.getHighestBetTurns();

			numRunTimes++;
		}
		System.out.println("\n\n::::: Percentage COLOR bet @"
				+ this.colorTurnsToWait + " ::::: " + this.numRuns
				* this.numberOfTurns + " total rolls.");
		this.printResults();
		this.printSequentialColors();
	}

	/**
	 * RUNS THE CORRESPONDING STRAT
	 * 
	 * @param runTimes
	 *            = the number of games to run the strat
	 * @param numRolls
	 *            = the number of rolls per game
	 */
	private void runColorBetProgWithGreenFlat() {

		int numRunTimes = this.runTimes;

		while (numRunTimes < this.numRuns) {

			ColorBetProgWithGreenFlat strat1 = new ColorBetProgWithGreenFlat(
					this.startingCash, this.colorTurnsToWait, this.minBet,
					this.MAX_BET, this.greenTurnsToWait, this.MAX_OTHER);

			this.averageCashout = this.averageCashout
					+ strat1.getFinalCashOut();

			if (strat1.getHighestCashOut() > this.highestCashout) {
				this.highestCashout = strat1.getHighestCashOut();
			}

			this.numBrokeChance = this.numBrokeChance + strat1.getNumBroke();

			this.cashTotal = this.cashTotal + strat1.getFinalCashOut()
					+ strat1.getBrokeTotal();
			this.brokeTotal = this.brokeTotal + strat1.getBrokeTotal();
			this.highestTurn = strat1.getHighestBetTurns();
			this.greenGap = strat1.getHighestGreenGap();

			numRunTimes++;
		}
		System.out.println("\n\n::::: Progressive COLOR bet @"
				+ this.colorTurnsToWait + " with flat GREEN bet @"
				+ this.greenTurnsToWait + " ::::: " + this.numRuns
				* this.numberOfTurns + " total rolls.");
		this.printResults();
		this.printGreenDistance();
		this.printSequentialColors();
	}

	private void runColorBetProgWithGreenAdd() {
		int numRunTimes = this.runTimes;

		while (numRunTimes < this.numRuns) {

			ColorBetProgWithGreenFlat strat1 = new ColorBetProgWithGreenFlat(
					this.startingCash, this.colorTurnsToWait, this.minBet,
					this.MAX_BET, this.greenTurnsToWait, this.MAX_OTHER);

			this.averageCashout = this.averageCashout
					+ strat1.getFinalCashOut();

			if (strat1.getHighestCashOut() > this.highestCashout) {
				this.highestCashout = strat1.getHighestCashOut();
			}

			this.numBrokeChance = this.numBrokeChance + strat1.getNumBroke();

			this.cashTotal = this.cashTotal + strat1.getFinalCashOut()
					+ strat1.getBrokeTotal();
			this.brokeTotal = this.brokeTotal + strat1.getBrokeTotal();
			this.highestTurn = strat1.getHighestBetTurns();
			this.greenGap = strat1.getHighestGreenGap();

			numRunTimes++;
		}
		System.out.println("\n\n::::: Progressive COLOR bet @"
				+ this.colorTurnsToWait + " with additive GREEN bet @"
				+ this.greenTurnsToWait + " ::::: " + this.numRuns
				* this.numberOfTurns + " total rolls.");
		this.printResults();
		this.printGreenDistance();
		this.printSequentialColors();

	}

	/**
	 * RUNS THE CORRESPONDING STRAT
	 * 
	 * @param runTimes
	 *            = the number of games to run the strat
	 * @param numRolls
	 *            = the number of rolls per game
	 */
	private void runColorBetProgWithOddEvenProg() {

		int numRunTimes = this.runTimes;

		while (numRunTimes < this.numRuns) {

			ColorBetProgWithOddEvenProg strat1 = new ColorBetProgWithOddEvenProg(
					this.startingCash, this.colorTurnsToWait, this.minBet,
					this.MAX_BET);

			this.averageCashout = this.averageCashout
					+ strat1.getFinalCashOut();

			if (strat1.getHighestCashOut() > this.highestCashout) {
				this.highestCashout = strat1.getHighestCashOut();
			}

			this.numBrokeChance = this.numBrokeChance + strat1.getNumBroke();

			this.cashTotal = this.cashTotal + strat1.getFinalCashOut()
					+ strat1.getBrokeTotal();
			this.brokeTotal = this.brokeTotal + strat1.getBrokeTotal();
			this.highestTurn = strat1.getHighestBetTurns();
			this.numberGap = strat1.getHighestOddGap();

			numRunTimes++;
		}

		System.out.println("\n\n::::: Progressive COLOR bet @"
				+ this.colorTurnsToWait + " with progressive ODD/EVEN bet @"
				+ this.colorTurnsToWait + " ::::: " + this.numRuns
				* this.numberOfTurns + " total rolls.");
		this.printResults();
		printOddEvenDistance();
		this.printSequentialColors();
	}

	/**
	 * RUNS THE CORRESPONDING STRAT
	 * 
	 * @param runTimes
	 *            = the number of games to run the strat
	 * @param numRolls
	 *            = the number of rolls per game
	 */
	private void runColorBetProgOddEvenProgGreenAdd() {

		int numRunTimes = this.runTimes;

		while (numRunTimes < this.numRuns) {

			ColorBetProgOddEvenProgGreenAdd strat1 = new ColorBetProgOddEvenProgGreenAdd(
					this.startingCash, this.colorTurnsToWait, this.minBet,
					this.MAX_BET, this.greenTurnsToWait, this.MAX_OTHER);

			this.averageCashout = this.averageCashout
					+ strat1.getFinalCashOut();

			if (strat1.getHighestCashOut() > this.highestCashout) {
				this.highestCashout = strat1.getHighestCashOut();
			}

			this.numBrokeChance = this.numBrokeChance + strat1.getNumBroke();

			this.cashTotal = this.cashTotal + strat1.getFinalCashOut()
					+ strat1.getBrokeTotal();
			this.brokeTotal = this.brokeTotal + strat1.getBrokeTotal();
			this.highestTurn = strat1.getHighestBetTurns();
			this.greenGap = strat1.getHighestGreenGap();
			this.numberGap = strat1.getHighestOddGap();

			numRunTimes++;
		}
		System.out.println("\n\n::::: Progressive COLOR & ODD/EVEN bet @"
				+ this.colorTurnsToWait + " with additive GREEN bet @"
				+ this.greenTurnsToWait + " ::::: " + this.numRuns
				* this.numberOfTurns + " total rolls.");
		this.printResults();
		this.printGreenDistance();
		this.printOddEvenDistance();
		this.printSequentialColors();
	}

	private void runColorBetProgOddEvenProgGreenFlat() {
		int numRunTimes = this.runTimes;

		while (numRunTimes < this.numRuns) {

			ColorBetProgOddEvenProgGreenFlat strat1 = new ColorBetProgOddEvenProgGreenFlat(
					this.startingCash, this.colorTurnsToWait, this.minBet,
					this.MAX_BET, this.greenTurnsToWait, this.MAX_OTHER);

			this.averageCashout = this.averageCashout
					+ strat1.getFinalCashOut();

			if (strat1.getHighestCashOut() > this.highestCashout) {
				this.highestCashout = strat1.getHighestCashOut();
			}

			this.numBrokeChance = this.numBrokeChance + strat1.getNumBroke();

			this.cashTotal = this.cashTotal + strat1.getFinalCashOut()
					+ strat1.getBrokeTotal();
			this.brokeTotal = this.brokeTotal + strat1.getBrokeTotal();
			this.highestTurn = strat1.getHighestBetTurns();
			this.greenGap = strat1.getHighestGreenGap();
			this.numberGap = strat1.getHighestOddGap();

			numRunTimes++;
		}
		System.out.println("\n\n::::: Progressive COLOR & ODD/EVEN bet @"
				+ this.colorTurnsToWait + " with flat GREEN bet @"
				+ this.greenTurnsToWait + " ::::: " + this.numRuns
				* this.numberOfTurns + " total rolls.");
		this.printResults();
		this.printGreenDistance();
		this.printOddEvenDistance();
		this.printSequentialColors();

	}

	/**
	 * Prints all of the main stats for each game.
	 */
	public void printResults() {

		System.out.print("You walk away with         : $"
				+ ((this.cashTotal - this.numBrokeChance * this.startingCash)
						- (this.numRuns * this.minBet) + this.startingCash)
				+ " from the initial $" + this.startingCash);
		System.out.print("\nHighest cash ever          : $"
				+ (this.highestCashout - this.minBet));
		System.out.print("\nChance to go broke         : "
				+ (this.numBrokeChance / this.numRuns) * 100
				+ "% chance per 'game' :: (" + this.numRuns + " games and "
				+ this.numBrokeChance + " times gone broke.)");
		System.out
				.print("\nYou end up with            : $"
						+ ((this.cashTotal - this.numBrokeChance
								* this.startingCash)
								- (this.numRuns * this.minBet)
								+ " after losing $"
								+ this.brokeTotal
								+ " in losses and $"
								+ this.numBrokeChance
								* this.startingCash + " to re-play"));
	}

	/**
	 * This is a separate print of results because green bets aren't in all of
	 * the strategies. This prints the longest distance between two green
	 * numbers and the number of rolls to wait after betting on the first green.
	 */
	private void printGreenDistance() {
		System.out.print("\nMost distance from green   : " + this.greenGap
				+ " after waiting " + this.greenTurnsToWait + " rolls.");
	}

	/**
	 * This is a separate print of results because number bets aren't in all of
	 * the strategies. This prints the longest distance between two odd or even
	 * numbers.
	 */
	private void printOddEvenDistance() {
		System.out.print("\nMost distance from odd/even: " + this.numberGap
				+ " after waiting " + this.colorTurnsToWait + " rolls.");
	}

	/**
	 * This is a separate print of results because color bets aren't in all of
	 * the strategies. This prints the longest distance between any two of the
	 * same colors.
	 */
	private void printSequentialColors() {
		System.out.print("\nMost sequential colors     : " + this.highestTurn);
	}

	/**
	 * A method to get the color of a certain number (refer to a Roulette table)
	 */
	public int getColor(int rollNumber) {
		int land = this.list.get(rollNumber);
		int getColor = 0;

		if (land == 0 || land == 37) {
			getColor = 0;// GREEN
		} else if (land == 1 || land == 3 || land == 5 || land == 7
				|| land == 9 || land == 12 || land == 14 || land == 16
				|| land == 18 || land == 21 || land == 23 || land == 25
				|| land == 27 || land == 28 || land == 30 || land == 32
				|| land == 34 || land == 36) {
			getColor = 1;// RED
		} else {
			getColor = 2;// BLACK
		}

		return getColor;

	}

	/**
	 * Calculates even or odd number based on the remainder when divided by two.
	 * Excludes greens, which are numbers 0 and 37
	 */
	public int getOddEven(int rollNumber) {

		int land = this.list.get(rollNumber);

		if (land == 0 || land == 37) {
			return 0;// GREEN
		}
		if (land % 2 == 0) {
			return 1; // EVEN
		} else {
			return 2; // ODD
		}
	}

	/**
	 * Returns the number of rolls total that the Roulette generates
	 * 
	 * @return numberOfTurns
	 */
	public int getNumberOfTurns() {
		return this.numberOfTurns;
	}
}
