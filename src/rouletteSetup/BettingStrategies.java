package rouletteSetup;

/**
 * This class contains all the betting strategies for the Roulette game.
 * 
 * Additive bets: A bet added onto iteself each round.
 * Flat bets: Bets that do not change.
 * Progressive bets: Bets that are multiplied by a predefined variable.
 * 
 * @author Chad
 *
 */
public class BettingStrategies {

	private double betAmount;

	/**
	 * Empty Constructor
	 */
	public BettingStrategies() {

	}

	private int betsMade = 0;
	private double startBet = 0;

	/**
	 * Calculates an ADDITIVE BET based on data it's given
	 * 
	 * @param startBet the starting/base bet
	 * @param betsMade current number of consecutive bets
	 * @return startBet the amount to currently wager
	 */
	public double Additive_Bets(double startBet, int betsMade) {

		this.betsMade = betsMade;
		this.startBet = startBet;

		if (betsMade <= 1) {

		} else {
			this.startBet = this.startBet * betsMade;
		}
		return this.startBet;
	}

	/**
	 * Calculates a FLAT BET based on data it's given
	 * 
	 * @param startBet the starting/base bet
	 * @param betsMade current number of consecutive bets
	 * @return betAmount the amount to currently wager
	 */
	public double FlatBets(double betAmount) {

		this.betAmount = betAmount;

		return betAmount;
	}

	/**
	 * Calculates a PROGRESSIVE BET based on data it's given
	 * 
	 * @param startBet the starting/base bet
	 * @param betsMade current number of consecutive bets
	 * @return startBet the amount to currently wager
	 */
	public double Progressive(double startBet, int betsMade) {

		this.betsMade = betsMade;
		this.startBet = startBet;

		if (betsMade <= 1) {

		} else if (betsMade == 2) {
			this.startBet = this.startBet * 2;
		} else if (betsMade > 2) {

			/**
			 * Took me a while to figure this one out. My formula!
			 */
			this.startBet = this.startBet * 2 * 3
					* (Math.pow(3, (betsMade - 3)));
		}

		return this.startBet;

	}
}
