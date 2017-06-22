package strategies;

import rouletteSetup.BettingStrategies;
import rouletteSetup.RouletteTable;

/**
 * Bets on a COLOR if X% of the last X rolls pass without landing on COLOR
 * 
 * PROGRESSIVE
 * 
 * @author Charlie
 *
 */

public class ColorBetProgPercentage {

	private double highestBettingTurns = 0;

	private RouletteTable newRoulette = new RouletteTable();

	private double cash, cashReset, mostCash;
	private double setPercentage = 0;
	private int numBroke = 0;
	private double brokeCashTotal = 0;
	private String broke = "";
	private double winnings = 0;
	private double startingBet = 0;
	private int betCounter = 0;
	private int turnNumber = 0;
	private int turnsToWait = 5;
	private double MAX_BET = 0;
	private int recordedTurnNumberForHighest = 0;
	private BettingStrategies strategies = new BettingStrategies();

	public ColorBetProgPercentage(double cash, int turnsToWait,
			double startingBet, double percentage, double MAX_BET) {
		this.setPercentage = percentage;
		this.MAX_BET = MAX_BET;
		this.cash = cash;
		this.startingBet = startingBet;
		this.cashReset = cash;
		this.mostCash = 0;
		this.turnsToWait = turnsToWait;

		this.colorFlatPercentBased();
	}

	public void colorFlatPercentBased() {

		this.turnNumber = (this.turnsToWait + 1);

		while (this.turnNumber < this.newRoulette.getNumberOfTurns()) {

			double redCount = 0;
			double totalCount = 0;
			int start = this.turnNumber - this.turnsToWait;

			while (start < this.turnNumber) {

				// 1 = RED
				if (this.newRoulette.getColor(start) == 1) {
					redCount++;
					totalCount++;
				} else {
					totalCount++;
				}
				start++;
			}

			// CHECK TO SEE IF RED IS LESS THAN REQ PERC
			double percentage = (redCount / totalCount) * 100;

			if (percentage < this.setPercentage) {

				this.betCounter++;
				this.winnings = this.strategies.Progressive(this.startingBet,
						this.betCounter);

				// BET GETS TO MAX BET?
				if (this.winnings >= this.MAX_BET) {
					this.winnings = this.MAX_BET;

					// LAST BET
				} else if (this.cash >= 0 && this.winnings > this.cash) {
					this.winnings = this.cash;
					this.cash = 0;
				}

				this.cash = this.cash - this.winnings;
			}

			if (this.betCounter > 0
					&& this.newRoulette.getColor(this.turnNumber) == 1) {

				this.cash = this.cash + (this.winnings * 2);

				if (this.highestBettingTurns < this.betCounter) {
					this.highestBettingTurns = this.betCounter;
				}

				this.betCounter = 0;

				if (this.cash > this.mostCash) {
					this.mostCash = this.cash;
					this.recordedTurnNumberForHighest = this.turnNumber;
				}

			}

			// PLAYER GOES BROKE
			if (this.cash <= 0) {

				this.brokeCashTotal = this.brokeCashTotal + this.cash;
				this.cash = this.cashReset;
				this.broke = this.broke + " " + this.turnNumber;

				if (this.highestBettingTurns < this.betCounter) {
					this.highestBettingTurns = this.betCounter;
				}

				this.betCounter = 0;
				this.numBroke++;

			}

			this.turnNumber++;

		}

	}

	public double getHighestBetTurns() {
		return this.highestBettingTurns;
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
