package strategies;

import rouletteSetup.BettingStrategies;
import rouletteSetup.RouletteTable;


/**
 *  Bets on a GREEN if the last X rolls pass without landing on GREEN
 * 
 * PROGRESSIVE
 * 
 * @author Charlie
 *
 */

public class GreenBetFlatXTurns {

	private int highestBettingTurns = 0;

	private RouletteTable newRoulette = new RouletteTable();

	private double cash, cashReset, mostCash;
	private int numBroke = 0;
	private double brokeCashTotal = 0;
	private String broke = "";
	private double winnings = 0;
	private double startingBet = 10;
	private BettingStrategies strategies = new BettingStrategies();
	private int turnNumber = 0;
	private int turnsToWait = 5;
	private int recordedTurnNumberForHighest = 0;
	private boolean hasBet = false;
	private int timeToBetCount = 0;

	public GreenBetFlatXTurns(double cash, int turnsToWait,
			double startingBet) {
		this.cash = cash;
		this.startingBet = startingBet;
		this.cashReset = cash;
		this.mostCash = 0;
		this.turnsToWait = turnsToWait;

		this.greenFlatPercentBased();
	}

	private void greenFlatPercentBased() {

		while (this.turnNumber < this.newRoulette.getNumberOfTurns()) {

			int getRoulette = this.newRoulette.getColor(this.turnNumber);

			if (this.timeToBetCount >= this.turnsToWait && getRoulette != 0) {
				this.winnings = this.strategies.FlatBets(this.startingBet);
				this.hasBet = true;
				this.cash = this.cash - this.winnings;
			}

			// PLAYER LOSES
			if (getRoulette != 0) {
				this.timeToBetCount++;

				// PLAYER WIN
			} else if (getRoulette == 0
					&& (this.timeToBetCount - this.turnsToWait) >= 0
					&& this.hasBet == true) {

				this.cash = this.cash + (this.winnings / 2) * 36;

				if (this.highestBettingTurns < this.timeToBetCount) {
					this.highestBettingTurns = this.timeToBetCount;
				}

				this.timeToBetCount = 0;

				// GET MOST CASH AND TURN @ MOST
				if (this.cash > this.mostCash) {
					this.mostCash = this.cash;
					this.recordedTurnNumberForHighest = this.turnNumber;
				}

			} else if (getRoulette == 0 && this.hasBet == false) {
				this.timeToBetCount = 0;

			}

			// PLAYER WENT BROKE
			if (this.cash <= 0) {
				
				this.brokeCashTotal = this.brokeCashTotal + this.cash;
				this.cash = this.cashReset;
				this.broke = this.broke + " " + this.turnNumber;

				if (this.highestBettingTurns < this.timeToBetCount) {
					this.highestBettingTurns = this.timeToBetCount;
				}

				this.timeToBetCount = 0;
				this.numBroke++;

			}

			this.turnNumber++;

		}

		
	}

	public int getHighestBetTurns() {
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
