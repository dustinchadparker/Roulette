package strategies;

import rouletteSetup.BettingStrategies;
import rouletteSetup.RouletteTable;


/**
 * Bets on a COLOR if X consecutive turns pass without landing on COLOR &&
 * bets on ODD/EVEN if X consecutive turns pass without landing on ODD/EVEN &&
 * bets on GREEN if X consecutive turns pass without landing on GREEN
 * 
 * (PROGRESSIVE AND PROGRESSIVE AND FLAT)
 * 
 * @author Charlie
 *
 */
public class ColorBetProgOddEvenProgGreenFlat {
	
	private double colorBetAmount = 0;
	private BettingStrategies strategies = new BettingStrategies();
	private int turnNumber = 0;
	private int colorDistance = 0;
	private double cash = 0;
	private boolean hasGreenBet = false;
	private double greenBetAmount = 0;
	private int greenGap = 0;
	private int greenGapMax = 0;
	private int greenBetNumber = 0;
	private boolean hasColorBet = false;
	private boolean hasNumberBet = false;
	private boolean evenBetting = false;
	private boolean oddBetting = false;
	private double startingBet = 0;
	private double MAX_EVEN = 0;
	private double MAX_OTHER = 0;
	private double numberBetAmount = 0;
	private int numberGap = 0;
	private int greenWait = 0;
	private int numberGapMax = 0;
	private double cashReset = 0;
	private double mostCash = 0;
	private int colorWait = 0;
	private RouletteTable roulette = new RouletteTable();
	private double brokeCashTotal = 0;
	private String broke = "";
	private int colorBetNumber = 0;
	private int numberBetNumber = 0;
	private int highestBettingTurns = 0;
	private int numberWait = 0;
	private int numBroke = 0;
	private int highestGap = 0;

	/**
	 * Imports and sets all relevant values
	 * 
	 * @param cash
	 * @param turnsToWait
	 * @param startingBet
	 * @param MAX_BET
	 * @param greenWait
	 * @param MAX_OTHER
	 */
	public ColorBetProgOddEvenProgGreenFlat(double cash, int turnsToWait,
			double startingBet, double MAX_BET, int greenWait, double MAX_OTHER) {
		this.cash = cash;
		this.startingBet = startingBet;
		this.MAX_EVEN = MAX_BET;
		this.MAX_OTHER = MAX_OTHER;
		this.cashReset = cash;
		this.greenWait = greenWait;
		this.mostCash = 0;
		this.colorWait = turnsToWait;

		this.ColorWithOddsAndGreenFlat();
	}

	
	private void ColorWithOddsAndGreenFlat() {
		/**
		 * Sets up dummy clones of certain variables so that we don't tamper
		 * with the original ones.
		 */
		this.turnNumber = this.colorWait;
		int waiting = this.colorWait;
		int greenWaiting = this.greenWait;
		/**
		 * This cycles through the AI for every roll of the roulette
		 */
		while (this.turnNumber < this.roulette.getNumberOfTurns()) {
			/**
			 * Initializes these variables needed in the implementation of the
			 * AI
			 */
			int nonRedCount = 0;
			int nonOddCount = 0;
			int nonGreenCount = 0;
			int nonEvenCount = 0;
			int beginRedBetPos = this.turnNumber - waiting;

			// gets distance between same colors
			if (this.roulette.getColor(this.turnNumber - 1) == this.roulette
					.getColor(this.turnNumber)) {
				this.colorDistance++;
			} else {
				this.colorDistance = 0;
			}
			
			// set HIGHEST GAP
			if (this.colorDistance > this.highestGap) {
				this.highestGap = this.colorDistance;
			}
			/**
			 * Counts how many of a certain color there are in the past X rolls
			 */
			while (beginRedBetPos <= this.turnNumber - 1) {
				// 1 = RED
				if (this.roulette.getColor(beginRedBetPos) != 1) {
					nonRedCount++;
				}
				beginRedBetPos++;
			}

			int beginOddBetPos = this.turnNumber;
			/**
			 * This begins the number(odd/even) check at the roll number that we
			 * wait on and counts the number of each in the past X rolls
			 */
			while (beginOddBetPos <= this.turnNumber - 1) {
				// 2=EVEN
				if (this.roulette.getOddEven(beginOddBetPos) == 2) {
					nonOddCount++;
					// 1=ODD
				} else if (this.roulette.getOddEven(beginOddBetPos) == 1) {
					nonEvenCount++;
				}
				beginOddBetPos++;
			}

			if (greenWaiting < this.turnNumber) {
				/**
				 * This begins the green check at the roll number that we wait
				 * on and counts the NON-greens of each in the past X rolls
				 */
				int beginGreenBetPos = this.turnNumber - this.greenWait;
				// 0 = GREEN
				while (beginGreenBetPos <= this.turnNumber - 1) {
					if (this.roulette.getColor(beginGreenBetPos) != 0) {
						nonGreenCount++;
					}
					beginGreenBetPos++;
				}
			}

			/**
			 * INITIAL GREEN (ZEROS) BETTING BEGINS
			 */

			/**
			 * If the required rolls have passed and a bet is not already placed
			 * for green, then it starts the betting for green.
			 */
			if (nonGreenCount >= this.greenWait && this.hasGreenBet == false) {

				this.greenBetNumber++;
				this.greenGap++;
				this.greenBetAmount = this.strategies
						.FlatBets(this.startingBet);

				this.hasGreenBet = true;
				this.cash = this.cash - greenBetAmount;

				/**
				 * Checks to see if a bet has been initialized if the above is
				 * not true
				 */
			} else if (nonGreenCount >= this.greenWait
					&& this.hasGreenBet == true) {
				/**
				 * The following code does this in this: Increments the
				 * number of times we have bet that green will hit next. Adds 1
				 * to the gap between greens Calculates the bet amount from the
				 * BettingStrategies class (notice how we don't subtract from
				 * money yet)
				 */
				this.greenGap++;
				this.greenBetNumber++;
				this.greenBetAmount = this.strategies
						.FlatBets(this.startingBet);

				/**
				 * Checks to see if the bet exceeds the max before subtracting,
				 * if so then the bet is set to the max
				 */
				if (this.greenBetAmount >= this.MAX_EVEN) {
					this.greenBetAmount = this.MAX_EVEN;
				}

				/**
				 * Checks to see if this will be the last bet available to make,
				 * i.e. available cash is less than the required bet, if so then
				 * the bet is set to that amount.
				 */
				if (this.cash > 0 && this.greenBetAmount > this.cash) {

					this.greenBetAmount = this.cash;

				}
				/**
				 * Whatever bet we made is subtracted here from the cash.
				 */
				this.cash = this.cash - this.greenBetAmount;

			}

			/**
			 * INITIAL NUMBER BETTING BEGINS
			 */

			/**
			 * If the required rolls have passed and a bet is not already placed
			 * for odd/even, then it starts the betting for odd/even and
			 * initializes the bets for the numbers.
			 * 
			 * This if/else decides if to bet on even or odd depending on the
			 * past X rolls.
			 */
			if (nonEvenCount >= this.numberWait && this.hasNumberBet == false) {
				this.evenBetting = true;
				this.initialNumberBet();
			} else if (nonOddCount >= this.numberWait
					&& this.hasNumberBet == false) {
				this.oddBetting = true;
				this.initialNumberBet();

				/**
				 * This runs if initial bet has been placed already.
				 */
			} else if (nonEvenCount >= this.numberWait
					|| nonOddCount >= this.numberWait
					&& this.hasNumberBet == true) {

				this.numberBetNumber++;
				this.numberBetAmount = this.strategies.Progressive(
						this.startingBet, this.numberBetNumber);

				/**
				 * If the bet amount is higher than max, bet MAX
				 */
				if (this.numberBetAmount >= this.MAX_EVEN) {
					this.numberBetAmount = this.MAX_EVEN;

				}

				/**
				 * If the bet amount is more than the cash available, bet that
				 * amount.
				 */
				if (this.numberBetAmount > this.cash) {

					this.numberBetAmount = this.cash;

				}

				this.cash = this.cash - this.numberBetAmount;

			}

			/**
			 * INITIAL COLOR BETTING BEGINS
			 */

			/**
			 * If the required rolls have passed and a bet is not already placed
			 * for a color, then it starts the betting for the color and
			 * initializes the bets for the colors.
			 * 
			 * This if/else decides on if to bet on even or odd depending on the
			 * past X rolls.
			 */
			if (nonRedCount >= this.colorWait && this.hasColorBet == false) {

				this.colorBetNumber++;
				this.colorBetAmount = this.strategies.Progressive(
						this.startingBet, this.colorBetNumber);

				this.hasColorBet = true;
				this.cash = this.cash - colorBetAmount;

				/**
				 * This runs if initial bet has been placed already.
				 */
			} else if (nonRedCount >= this.colorWait
					&& this.hasColorBet == true) {
				/**
				 * This code does the following: Adds 1 to the number of times
				 * consecutively bet on a color Gets the amount to be bet
				 */
				this.colorBetNumber++;
				this.colorBetAmount = this.strategies.Progressive(
						this.startingBet, this.colorBetNumber);

				/**
				 * If the bet amount is higher than max, bet MAX
				 */
				if (this.colorBetAmount >= this.MAX_EVEN) {
					this.colorBetAmount = this.MAX_EVEN;

				}

				/**
				 * If the bet amount is more than the cash available, bet that
				 * amount.
				 */
				if (this.colorBetAmount > this.cash) {

					this.colorBetAmount = this.cash;

				}
				/**
				 * Subtracts the determines bet amount from available cash.
				 */
				this.cash = this.cash - this.colorBetAmount;

			}

			/**
			 * Checks to see if the player wins the bet on a COLOR bet at the
			 * current roll, if so it sets required variables to the initial
			 * values and awards the player his money
			 */
			if (this.roulette.getColor(this.turnNumber) == 1
					&& this.hasColorBet == true) {

				this.cash = this.cash + (this.colorBetAmount * 2);

				this.colorBetAmount = this.startingBet;
				this.hasColorBet = false;
				this.colorBetNumber = 0;

				/**
				 * Checks to see if the player wins the bet on a GREEN bet at the
				 * current roll, if so it sets required variables to the initial
				 * values and awards the player his money
				 */
							} else if (this.roulette.getColor(this.turnNumber) == 0
									&& this.hasGreenBet == true) {

								this.cash = this.cash + (this.greenBetAmount * 18);

								this.greenBetAmount = this.startingBet;
								this.hasGreenBet = false;
								this.greenBetNumber = 0;

								/**
								 * Calculates highest green gap upon win and resets gap number
								 * to zero
								 */
								if (this.greenGapMax < this.greenGap) {
									this.greenGapMax = this.greenGap;
								}

								this.greenGap = 0;
							}
			
			/**
			 * Checks to see if the player wins the bet on a number bet at the
			 * current roll, depending on which odd or even number bet on, if so
			 * it sets required variables to the initial values and awards the
			 * player his money
			 */
			
			if (this.hasNumberBet == true && this.evenBetting == true
					&& this.roulette.getOddEven(this.turnNumber) == 1) {

				numberWin();

			} else if (this.hasNumberBet == true && this.oddBetting == true
					&& this.roulette.getOddEven(this.turnNumber) == 2) {

				numberWin();
			}

			// SETS HIGHEST number GAP ON WIN
			if (this.numberGapMax < this.numberGap) {
				this.numberGapMax = this.numberGap;
			}

			this.numberGap = 0;

			/**
			 * Checks to see if the player went broke, if so it resets
			 * everything and gives the player back the starting cash to try
			 * again(testing purposes), and records where he went broke at and
			 * all statistics associated
			 */
			if (this.cash <= 0) {

				this.numberBetAmount = 0;
				this.colorBetAmount = 0;
				this.hasNumberBet = false;
				this.hasColorBet = false;

				this.brokeCashTotal = this.brokeCashTotal + this.cash;
				this.cash = this.cashReset;

				this.broke = this.broke + " " + this.turnNumber;
				this.numberBetNumber = 0;
				this.colorBetNumber = 0;
				this.numBroke++;

			}
			/**
			 * If current cash is higher than the highest, then it makes a new
			 * record.
			 */
			if (this.cash > this.mostCash) { 
				this.mostCash = this.cash;
				this.highestBettingTurns = this.turnNumber;
			}

			this.turnNumber++;
		}

	}

	/**
	 * 
	 */
	private void initialNumberBet() {
		this.numberBetNumber++;
		this.numberBetAmount = this.strategies.Progressive(this.startingBet,
				this.numberBetNumber);

		this.hasNumberBet = true;
		this.cash = this.cash - numberBetAmount;
	}

	/**
	 * This method is called when an even or an odd number wins to calculate win
	 * amount (the only reason this is here is because I would have had to just
	 * copy and paste it up above in more than one spot i.e. redundant code.
	 */
	public void numberWin() {

		this.cash = this.cash + (this.numberBetAmount * 2);

		this.numberBetAmount = this.startingBet;
		this.hasNumberBet = false;
		this.evenBetting = false;
		this.oddBetting = false;
		this.numberBetNumber = 0;

	}
	/**
	 * Method that allows the RouletteTable class to extract the highest number
	 * of times a bet is made consecutively for statistical purposes
	 */
	public double getHighestBetTurns() {
		return this.highestGap;
	}
	/**
	 * Method that allows the RouletteTable class to extract the highest gap
	 * between odd/even for statistical purposes
	 */
	public int getHighestOddGap() {
		return this.numberGapMax;
	}
	/**
	 * Method that allows the RouletteTable class to extract the highest gap
	 * between greens for statistical purposes
	 */
	public int getHighestGreenGap() {
		return this.greenGapMax;
	}
	/**
	 * Method that allows the RouletteTable class to extract the total number of
	 * times the player went broke for statistical purposes
	 */
	public int getNumBroke() {
		return this.numBroke;
	}
	/**
	 * Method that allows the RouletteTable class to extract the final cash
	 * value at the end of the round for statistical purposes
	 */
	public double getFinalCashOut() {
		return this.cash - this.cashReset;
	}
	/**
	 * Method that allows the RouletteTable class to extract the final cash
	 * value at the end of ALL the rounds for statistical purposes
	 */
	public double getHighestCashOut() {
		return this.mostCash;
	}
	/**
	 * Method that allows the RouletteTable class to extract the total cash
	 * value of the losses in going broke for statistical purposes
	 */
	public double getBrokeTotal() {
		return this.brokeCashTotal;
	}

}
