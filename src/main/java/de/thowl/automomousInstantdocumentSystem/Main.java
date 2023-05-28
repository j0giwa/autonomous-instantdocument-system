/*
 * Autonomous Instantdocument System -- Automatically generate LaTeX Documents
 * Copyright (C) 2023 Jonas Schwind, Marvin Boschmann
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package de.thowl.automomousInstantdocumentSystem;

import de.thowl.automomousInstantdocumentSystem.control.Latex;
import de.thowl.automomousInstantdocumentSystem.view.Gui;
import javafx.application.Application;

/**
 * This is the Main Class of the Program
 * 
 * @version 0.2.5
 * @author Jonas Schwind
 */
public class Main {

	private static final int EXIT_SUCCESS = 0;
	private static final int EXIT_ERROR = 1;

	private static final String version = "version: 0.8.5";

	private static String type = null;
	private static String destination = null;
	private static int amount = 0;
	private static int chapters = 0;
	private static boolean shuffle = true;

	public static void main(String[] args) {
		if (args.length == 0) {
			Application.launch(Gui.class, args);
			System.exit(EXIT_SUCCESS);
		}
		handleArgs(args);
		if (type == null || destination == null || amount <= 0) {
			System.out.println("Not enough arguments");
			System.exit(EXIT_ERROR);
		}
		Latex latex = new Latex();
		latex.generate(type, destination, amount, chapters, shuffle);
		System.exit(EXIT_SUCCESS);
	}

	/**
	 * This method handles the command line arguments
	 * 
	 * @param args
	 */
	private static void handleArgs(String[] args) {
		int argc = args.length;
		String arg = null;
		String previousArg = null;
		for (int i = 0; i < argc; i++) {
			arg = args[i];
			if (arg.startsWith("--")) {
				arg = arg.substring(2);
			} else if (arg.startsWith("-")) {
				arg = arg.substring(1);
			}
			// Parameterized arguments
			if (previousArg != null) {
				handleParameterisedArgs(arg, previousArg);
				previousArg = null;
			}
			// Standard arguments
			switch (arg) {
				case "version":
				case "v":
					printVersion();
					System.exit(EXIT_SUCCESS);
					break;
				case "help":
				case "h":
					printHelp();
					System.exit(EXIT_SUCCESS);
					break;
				case "noshuffle":
				case "ns":
					shuffle = false;
					break;
				default:
					previousArg = arg;
					break;
			}
		}
	}

	/**
	 * This method handles the command line arguments with additional parameters
	 * 
	 * @param args
	 */
	private static void handleParameterisedArgs(String arg, String previousArg) {
		switch (previousArg) {
			case "type":
			case "t":
				type = arg;
				break;
			case "destination":
			case "d":
				destination = arg;
				break;
			case "amount":
			case "a":
				try {
					amount = Integer.parseInt(arg);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				break;
			case "chapters":
			case "c":
				try {
					chapters = Integer.parseInt(arg);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				break;
		}
	}

	/**
	 * This method print the version information to console
	 */
	private static void printVersion() {
		System.out.println(version);
	}

	/**
	 * This Method prints a simple help document to console
	 */
	private static void printHelp() {
		// TODO Add help once all features are implemented
		System.out.println("NO HELP (yet...)");
	}
}
