package strategies;

import rouletteSetup.BettingStrategies;
import rouletteSetup.RouletteTable;

/**
 * Bets on COLOR if X consecutive turns pass without landing on COLOR.
 * 
 * PROGRESSIVE
 * 
 * @author Charlie
 *
 */
public class ColorBetProg {

	private double betAmount = 0;
	private BettingStrategies strategies = new BettingStrategies();
	private int turnNumber = 0;
	private int colorDistance = 0;
	private double cash = 0;
	private boolean hasBet = false;
	private double startingBet = 0;
	private double cashReset = 0;
	private double mostCash = 0;
	private int turnsToWait = 0;
	private RouletteTable roulette = new RouletteTable();
	private double brokeCashTotal = 0;
	private String broke = "";
	private int betNumber = 0;
	private int highestBettingTurns = 0;
	private int numBroke = 0;
	private int highestGap = 0;
	private double MAX_BET = 0;

	public ColorBetProg(double cash, int turnsToWait, double startingBet,
			double MAX_BET) {
		this.cash = cash;
		this.startingBet = startingBet;
		this.cashReset = cash;
		this.mostCash = 0;
		this.MAX_BET = MAX_BET;
		this.turnsToWait = turnsToWait;

		this.colorProgOnlyGAP();
	}

	private void colorProgOnlyGAP() {

		this.turnNumber = this.turnsToWait;
		int waiting = this.turnsToWait;

		while (this.turnNumber < this.roulette.getNumberOfTurns()) {
			int nonRedCount = 0;
			int beginningPos = this.turnNumber - waiting;

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
			while (beginningPos <= this.turnNumber - 1) {
				if (this.roulette.getColor(beginningPos) != 1) {
					nonRedCount++;
				}
				beginningPos++;
			}

			// INITIAL BET
			if (nonRedCount >= this.turnsToWait && this.hasBet == false) {

				this.betNumber++;
				this.betAmount = this.strategies.Progressive(this.startingBet,
						this.betNumber);

				this.hasBet = true;
				this.cash = this.cash - betAmount;

				// CONSECUTIVE BETTING AFTER INITIAL
			} else if (nonRedCount >= this.turnsToWait && this.hasBet == true) {
				this.betNumber++;
				this.betAmount = this.strategies.Progressive(this.startingBet,
						this.betNumber);

				// check BET GETS TO MAX BET?
				if (this.betAmount >= this.MAX_BET) {
					this.betAmount = this.MAX_BET;
				}

				// check PLAYER GOES BROKE
				if (this.cash <= 0) {

					this.hasBet = false;

					this.brokeCashTotal = this.brokeCashTotal + this.cash;
					this.cash = this.cashReset;
					this.broke = this.broke + " " + this.turnNumber;
					this.betNumber = 0;
					this.numBroke++;

					// LAST BET
				} else if (this.cash >= 0 && this.betAmount > this.cash) {
					this.betAmount = this.cash;
					this.cash = 0;
				}

				this.cash = this.cash - this.betAmount;
			}

			// check PLAYER WINS
			if (this.roulette.getColor(this.turnNumber) == 1
					&& this.hasBet == true) {

				this.cash = this.cash + (this.betAmount * 2);
				this.betAmount = this.startingBet;
				this.hasBet = false;
				this.betNumber = 0;

				// get highest cash and turn
				if (this.cash > this.mostCash) {
					this.mostCash = this.cash;
					this.highestBettingTurns = this.turnNumber;
				}

			}

			this.turnNumber++;
		}

	}

	/**
	 * Method that allows the RouletteTable class to extract the highest number
	 * of times a bet is made consecutively for statistical purposes
	 */
	public double getHighestBetTurns() {
		return this.highestGap;
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