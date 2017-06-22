package strategies;

import rouletteSetup.BettingStrategies;
import rouletteSetup.RouletteTable;


/**
 * Bets on a COLOR if X consecutive turns pass without landing on COLOR &&
 * bets on GREEN if X consecutive turns pass without landing on GREEN
 * 
 * (PROGRESSIVE AND ADDITIVE)
 * 
 * @author Charlie
 *
 */
public class ColorBetProgWithGreenAdd {

	private double colorBetAmount = 0;
	private BettingStrategies strategies = new BettingStrategies();
	private int turnNumber = 0;
	private int colorDistance = 0;
	private double cash = 0;
	private boolean hasColorBet = false;
	private boolean hasGreenBet = false;
	private double startingBet = 0;
	private double MAX_EVEN = 0;
	private double MAX_OTHER = 0;
	private double greenBetAmount = 0;
	private int greenGap = 0;
	private int greenGapMax = 0;
	private double cashReset = 0;
	private double mostCash = 0;
	private int colorWait = 0;
	private RouletteTable roulette = new RouletteTable();
	private double brokeCashTotal = 0;
	private String broke = "";
	private int colorBetNumber = 0;
	private int greenBetNumber = 0;
	private int highestBettingTurns = 0;
	private int greenWait = 0;
	private int numBroke = 0;
	private int highestGap = 0;

	public ColorBetProgWithGreenAdd(double cash, int turnsToWait,
			double startingBet, double MAX_BET, int greenWait, double MAX_OTHER) {
		this.cash = cash;
		this.greenWait = greenWait;
		this.startingBet = startingBet;
		this.MAX_EVEN = MAX_BET;
		this.MAX_OTHER = MAX_OTHER;
		this.cashReset = cash;
		this.mostCash = 0;
		this.colorWait = turnsToWait;

		this.colorProgWithGreenAdd();
	}

	public void colorProgWithGreenAdd() {

		this.turnNumber = this.colorWait;
		int waiting = this.colorWait;
		int greenWaiting = this.greenWait;

		while (this.turnNumber < this.roulette.getNumberOfTurns()) {

			int nonRedCount = 0;
			int nonGreenCount = 0;
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

			// 1 = RED
			while (beginRedBetPos <= this.turnNumber - 1) {
				if (this.roulette.getColor(beginRedBetPos) != 1) {
					nonRedCount++;
				}
				beginRedBetPos++;
			}

			if (greenWaiting < this.turnNumber) {

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
			if (nonGreenCount >= this.greenWait && this.hasGreenBet == false) {

				this.greenBetNumber++;
				this.greenGap++;
				this.greenBetAmount = this.strategies.Additive_Bets(
						this.startingBet, this.greenBetNumber);

				this.hasGreenBet = true;
				this.cash = this.cash - greenBetAmount;

				// consecutive bets NON-GREEN appears
			} else if (nonGreenCount >= this.greenWait
					&& this.hasGreenBet == true) {

				this.greenGap++;
				this.greenBetNumber++;
				this.greenBetAmount = this.strategies.Additive_Bets(
						this.startingBet, this.greenBetNumber);

				// BET GETS TO MAX BET?
				if (this.greenBetAmount >= this.MAX_OTHER) {
					this.greenBetAmount = this.MAX_OTHER;

				}

				// LAST BET
				if (this.cash > 0 && this.greenBetAmount > this.cash) {
					this.greenBetAmount = this.cash;


				}

				this.cash = this.cash - this.greenBetAmount;

			}

			/**
			 * INITIAL COLOR BETTING BEGINS
			 */
			if (nonRedCount >= this.colorWait && this.hasColorBet == false) {

				this.colorBetNumber++;
				this.colorBetAmount = this.strategies.Progressive(
						this.startingBet, this.colorBetNumber);

				this.hasColorBet = true;
				this.cash = this.cash - colorBetAmount;

				// consecutive bets NON-RED appears
			} else if (nonRedCount >= this.colorWait
					&& this.hasColorBet == true) {

				this.colorBetNumber++;
				this.colorBetAmount = this.strategies.Progressive(
						this.startingBet, this.colorBetNumber);

				// BET GETS TO MAX BET?
				if (this.colorBetAmount >= this.MAX_EVEN) {
					this.colorBetAmount = this.MAX_EVEN;

				}

				// LAST BET CASH
				if (this.colorBetAmount > this.cash) {

					this.colorBetAmount = this.cash;

				}

				this.cash = this.cash - this.colorBetAmount;

			}

			// PLAYER WINS COLOR
			if (this.roulette.getColor(this.turnNumber) == 1
					&& this.hasColorBet == true) {

				this.cash = this.cash + (this.colorBetAmount * 2);

				this.colorBetAmount = this.startingBet;
				this.hasColorBet = false;
				this.colorBetNumber = 0;

				// PLAYER WINS GREEN
			} else if (this.roulette.getColor(this.turnNumber) == 0
					&& this.hasGreenBet == true) {

				this.cash = this.cash + (this.greenBetAmount * 18);

				this.greenBetAmount = this.startingBet;
				this.hasGreenBet = false;
				this.greenBetNumber = 0;

				// SETS HIGHEST GREEN GAP ON WIN
				if (this.greenGapMax < this.greenGap) {
					this.greenGapMax = this.greenGap;
				}

				this.greenGap = 0;
			}
			// PLAYER GOES BROKE
			if (this.cash <= 0) {

				this.greenBetAmount = 0;
				this.colorBetAmount = 0;
				this.hasGreenBet = false;
				this.hasColorBet = false;

				this.brokeCashTotal = this.brokeCashTotal + this.cash;
				this.cash = this.cashReset;

				this.broke = this.broke + " " + this.turnNumber;
				this.greenBetNumber = 0;
				this.colorBetNumber = 0;
				this.numBroke++;

			}

			// get highest cash and turn
			if (this.cash > this.mostCash) {
				this.mostCash = this.cash;
				this.highestBettingTurns = this.turnNumber;
			}

			this.turnNumber++;
		}


	}

	public double getHighestBetTurns() {
		return this.highestGap;
	}

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
