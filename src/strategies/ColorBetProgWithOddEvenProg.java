package strategies;

import rouletteSetup.BettingStrategies;
import rouletteSetup.RouletteTable;


/**
 * Bets on a COLOR if X consecutive turns pass without landing on COLOR &&
 * bets on ODD/EVEN if X consecutive turns pass without landing on ODD/EVEN
 * 
 * (PROGRESSIVE AND PROGRESSIVE)
 * 
 * @author Charlie
 *
 */
public class ColorBetProgWithOddEvenProg {
	
	private double colorBetAmount = 0;
	private BettingStrategies strategies = new BettingStrategies();
	private int turnNumber = 0;
	private int colorDistance = 0;
	private double cash = 0;
	private boolean hasColorBet = false;
	private boolean hasNumberBet = false;
	private boolean evenBetting = false;
	private boolean oddBetting = false;
	private double startingBet = 0;
	private double MAX_EVEN = 0;
	private double numberBetAmount = 0;
	private int numberGap = 0;
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

	public ColorBetProgWithOddEvenProg(double cash, int turnsToWait,
			double startingBet, double MAX_BET) {
		this.cash = cash;
		this.startingBet = startingBet;
		this.MAX_EVEN = MAX_BET;
		this.cashReset = cash;
		this.mostCash = 0;
		this.colorWait = turnsToWait;

		this.ColorWithOdds();
	}

	private void ColorWithOdds() {

		this.turnNumber = this.colorWait;
		int waiting = this.colorWait;

		while (this.turnNumber < this.roulette.getNumberOfTurns()) {

			int nonRedCount = 0;
			int nonOddCount = 0;
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

			while (beginRedBetPos <= this.turnNumber - 1) {
				// 1 = RED
				if (this.roulette.getColor(beginRedBetPos) != 1) {
					nonRedCount++;
				}
				beginRedBetPos++;
			}

			int beginOddBetPos = this.turnNumber;

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

			/**
			 * INITIAL number BETTING BEGINS
			 */
			if (nonEvenCount >= this.numberWait && this.hasNumberBet == false) {
				this.evenBetting = true;
				initialNumberBet();
			} else if (nonOddCount >= this.numberWait
					&& this.hasNumberBet == false) {
				this.oddBetting = true;
				initialNumberBet();

				// CONSECUTIVE BETS NON-NUMBER APPEARS
			} else if (nonEvenCount >= this.numberWait
					|| nonOddCount >= this.numberWait
					&& this.hasNumberBet == true) {

				this.numberGap++;
				this.numberBetNumber++;
				this.numberBetAmount = this.strategies.Progressive(
						this.startingBet, this.numberBetNumber);

				// MAX BET IS REACHED
				if (this.numberBetAmount >= this.MAX_EVEN) {
					this.numberBetAmount = this.MAX_EVEN;

				}

				// LAST OF CASH BET
				if (this.numberBetAmount > this.cash) {

					this.numberBetAmount = this.cash;

				}

				this.cash = this.cash - this.numberBetAmount;
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

				// CONSECUTIVE NON-REDS APPEAR(CONTINUOUS BETTING)
			} else if (nonRedCount >= this.colorWait
					&& this.hasColorBet == true) {

				this.colorBetNumber++;
				this.colorBetAmount = this.strategies.Progressive(
						this.startingBet, this.colorBetNumber);

				// BET GETS TO MAX
				if (this.colorBetAmount >= this.MAX_EVEN) {
					this.colorBetAmount = this.MAX_EVEN;

				}

				// LAST OF CASH BET
				if (this.colorBetAmount > this.cash) {

					this.colorBetAmount = this.cash;

				}

				this.cash = this.cash - this.colorBetAmount;


			}

			// PLAYER WINS COLOR BET
			if (this.roulette.getColor(this.turnNumber) == 1
					&& this.hasColorBet == true) {

				this.cash = this.cash + (this.colorBetAmount * 2);

				this.colorBetAmount = this.startingBet;
				this.hasColorBet = false;
				this.colorBetNumber = 0;

				// PLAYER WINS NUMBER BET
			} else if (this.hasNumberBet == true && this.evenBetting == true
					&& this.roulette.getOddEven(this.turnNumber) == 1) {
				// SETS HIGHEST number GAP ON WIN
				if (this.numberGapMax < this.numberGap) {
					this.numberGapMax = this.numberGap;
				}
				this.highestNumGap();
				this.numberWin();

			} else if (this.hasNumberBet == true && this.oddBetting == true
					&& this.roulette.getOddEven(this.turnNumber) == 2) {

				this.highestNumGap();
				this.numberWin();

			}

			

			// PLAYER GOES BROKE
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

			if (this.cash > this.mostCash) { // get highest cash and
				// turn
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
	 * ODD/EVEN WIN
	 */
	public void numberWin() {

		this.cash = this.cash + (this.numberBetAmount * 2);

		this.numberBetAmount = this.startingBet;
		this.hasNumberBet = false;
		this.evenBetting = false;
		this.oddBetting = false;
		this.numberGap = 0;
		this.numberBetNumber = 0;

	}

	/**
	 * 
	 */
	private void highestNumGap() {
		if (this.numberGapMax < this.numberGap) {
			this.numberGapMax = this.numberGap;
		}
	}
	
	public double getHighestBetTurns() {
		return this.highestGap;
	}

	public int getHighestOddGap() {
		return this.numberGapMax;
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
