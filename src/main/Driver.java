
package main;

import rouletteSetup.RouletteTable;

/**
 * This class is the main entry point of the program, created because I was interested
 * in knowing what the best/most effective strategy was at winning a truly random
 * game of Roulette. Encompasses strategies found online and some I crafted.
 * 
 * @author Dustin Parker
 * 
 * @version December 2, 2014
 *
 */
public class Driver {

	/**
	 * This is the main entry point into Roulette.
	 * 
	 * @param args
	 *            none
	 */
	public static void main(String[] args) {

		/**
		 * Creates an object of the RouletteTable class and
		 * executes run()
		 */
		rouletteSetup.RouletteTable tui = new rouletteSetup.RouletteTable();
		tui.run();

	}

}
