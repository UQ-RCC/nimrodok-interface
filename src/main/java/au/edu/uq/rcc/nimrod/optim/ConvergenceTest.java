/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2017 University of Queensland
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package au.edu.uq.rcc.nimrod.optim;

import java.util.*;

//////////////////////////////////////////////////////////////////////////
//// ConvergenceTest
/**
 * <I>SelectSearchSpacePoints</I>Reads a set of parameter lines, which use the Nimrod/O syntax to define the parameter
 * names, data types and domains. Uses the Nimrod Java API class <I>SetOfParams</I> to handle the parsing. This
 * information is then output in the form of a reference to the java class.
 */
/* This is a relic from the past - it needs to go - Zane */
public class ConvergenceTest {

	public ConvergenceTest(String inString) {
		userString = inString;
		parseConvergenceTest(inString);
	}
	private static final double EPSILON = 1.2e-7;

	public boolean parsingValid;

	public boolean checkConvergence(ArrayOfPoints designatedPointSet) {
		//ArrayOfPoints designatedPointSet;
		OptimPoint[] pointArray;
		double max, min;
		double criterion = 0.0;

		//Debug.write("ConvergenceTest: checkConvergence, tolerance is "+tolerance);
		//designatedPointSet = simplexPoints;
		pointArray = designatedPointSet.pointArray;

		if(!designatedPointSet.allPointsEvaluated()) {
			return (false);
		}

		max = pointArray[0].cost;
		min = pointArray[0].cost;
		for(int i = 0; i < designatedPointSet.numPoints; ++i) {
			if(pointArray[i].cost > max) {
				max = pointArray[i].cost;
			} else if(pointArray[i].cost < min) {
				min = pointArray[i].cost;
			}
		}

		if(!proportional) {
			criterion = max - min;
		} else if(proportional) {
			criterion = 2.0 * (max - min) / (Math.abs(max) + Math.abs(min) + EPSILON);
		}
		//Debug.write("ConvergenceTest: checkConvergence, criterion is "+criterion);
		if(criterion <= tolerance) {
			return (true);
		} else {
			return (false);
		}
	}

	public boolean checkConvergence(double value) {
		if(value < tolerance) {
			return (true);
		} else {
			return (false);
		}
	}

	////////////////// Public ports and parameters ///////////////////////
	public enum TestMethod {
		SPATIAL_RANGE,
		SPATIAL_RANGE_PROPORTION,
		TEMPORAL_RANGE,
		TEMPORAL_RANGE_PROPORTION,
		NOT_DEFINED
	}
	TestMethod testMethod = TestMethod.NOT_DEFINED;
	boolean proportional = false;
	double tolerance;
	int steps;

	// class that stores information
	String userString;

	/*	public SetOfParams sop;

	
	public int numParams;
	private int startPointSteps[];
	public int pointCount;
	public int dimensionality;
	//public int numStarts;
	

	public ArrayOfPoints arrayOfPoints;
	public OptimPoint[] pointArray;
	public OptimPoint modelPoint;
	
	public int pointIndex;
	 */

	//int numPoints;
	//OptimPoint[] pointArray;
	//boolean pointsEvaluated;
	///////////////////////////////////////////////////////////////////
	////                        public methods                     ////
	private int parseConvergenceTest(String parseString) {

		loadSyntax();
		testMethod = TestMethod.NOT_DEFINED;
		proportional = false;
		tolerance = 0.0;
		parsingValid = false;

		lines = parseString.split("\n");
		numLines = lines.length;
		lineIndex = -1;
		while(true) {
			if(getNextLine() < 0) {
				break;
			}
			if(parseLine() != 0) {
				showParsingError();
				return (-1);
			}
		}

		if(testMethod == TestMethod.NOT_DEFINED) {
			System.out.println("Warning, no convergence test selected");
			return (-1);
		}
		parsingValid = true;
		return (0);

	}

	int getNextLine() {
		int index;
		while(true) {
			++lineIndex;
			if(lineIndex >= numLines) {
				return (-1);
			}
			fileLine = lines[lineIndex];
			index = fileLine.indexOf('#');
			if(index >= 0) {
				fileLine = fileLine.substring(0, index);
			}
			fileLine = fileLine.trim();
			if(fileLine.length() > 0) {
				length = fileLine.length();
				startPos = 0;
				endPos = 0;
				//state = 0;

				return (0);
			}
		}

	}

	private int parseLine() {

		/* STARTING POINTS RANDOM
		 * 	 "	" centerspaced <> by <> ...
		 * "	"	widespaced <> by <>  ...
		 * "	"	specified
		 * 				( <>  <>  <> )
		 * 				(<>  <>  <> )
		 * "	"	in file "<>"
		 * randomize reset
		 * randomize timer
		 * randomize seed <> <> <>
		 */
		token = nextToken();
		switch(token) {
			case END_LINE:
				return (0);

			case TOLERANCE:
				token = nextToken();
				switch(token) {
					case NUMBER_FLOAT:
					case NUMBER_INT:

						tolerance = currentFloat;
						break;
					default:
						errorMessage = "Expecting an floating point number after 'tolerance'";
						return (-11);
				}
				break;
			case STEPS:
				token = nextToken();
				switch(token) {
					case NUMBER_INT:

						steps = currentInteger;
						break;
					default:
						errorMessage = "Expecting an integer after 'steps'";
						return (-11);
				}
				break;
			case SPATIAL:
				token = nextToken();
				switch(token) {
					case RANGE:
						token = nextToken();
						switch(token) {
							case END_LINE:
								testMethod = TestMethod.SPATIAL_RANGE;
								proportional = false;
								return (0);
							case PROPORTION:
								token = nextToken();
								switch(token) {
									case END_LINE:
										testMethod = TestMethod.SPATIAL_RANGE_PROPORTION;
										proportional = true;
										return (0);
									default:
										errorMessage = "Unexpected word at end of line: " + fileLine.substring(startPos);
										return (-11);
								}
							default:
								errorMessage = "Unexpected word after 'range'";
								return (-11);
						}
				}
			case TEMPORAL:
				token = nextToken();
				switch(token) {
					case RANGE:
						token = nextToken();
						switch(token) {
							case END_LINE:
								testMethod = TestMethod.TEMPORAL_RANGE;
								proportional = false;
								return (0);
							case PROPORTION:
								token = nextToken();
								switch(token) {
									case END_LINE:
										testMethod = TestMethod.TEMPORAL_RANGE_PROPORTION;
										proportional = true;
										return (0);
									default:
										errorMessage = "Unexpected word at end of line: " + fileLine.substring(startPos);
										return (-11);
								}
							default:
								errorMessage = "Unexpected word after 'range'";
								return (-11);
						}
				}
			default:
				errorMessage = "Unexpected word: " + fileLine.substring(startPos);
				return (-11);

		}
		return (0);
	}

	private enum Toke {
		NOMORELINES,
		UNFINISHED,
		NUMBER_FLOAT,
		NUMBER_INT,
		STRINGG,
		UNFINISHED_STG,
		FORMULA,
		END_LINE,
		NO_MORE_WORDS,
		ILLEGAL_NUMBER_FORMAT,
		CONTROL_CHAR,
		UNFINISHED_VAR_NAME,
		VAR_NAME,
		DUNNO,
		// single char tokens 
		LEFT_PAREN,
		RIGHT_PAREN,
		EQUALS,
		TOLERANCE,
		STEPS,
		SPATIAL,
		RANGE,
		PROPORTION,
		TEMPORAL

	}

	private Toke nextToken() {
		int pos;
		char ch;
		String str;
		// just to quieten the compiler
		currentString = "";
		str = currentString;

		pos = endPos;

		while(pos < length) {
			if(Character.isWhitespace(fileLine.charAt(pos))) // skip prelim whitespace
			{
				++pos;
			} else {
				break;
			}

		}
		if(pos >= length) {
			//endPos = pos-1;

			return (Toke.END_LINE);
		}

		startPos = pos;
		ch = fileLine.charAt(pos);

		if(ch == '"') {
			while(true) {
				++pos;
				if(pos >= length) {
					endPos = pos;
					return (Toke.UNFINISHED_STG);
				}
				ch = fileLine.charAt(pos);
				if(Character.isISOControl(ch)) {
					endPos = pos;
					return (Toke.CONTROL_CHAR);
				}
				if(ch == '\"') {
					++pos;
					break;
				}
			}
			endPos = pos;
			currentString = fileLine.substring(startPos, endPos);
			return (Toke.STRINGG);
		} else if((Character.isDigit(ch)) || (ch == '.') || (ch == '-')) {
			while((Character.isDigit(ch)) || (ch == '.') || (ch == '-') || (ch == 'E') || (ch == 'e')) {
				++pos;
				if(pos >= length) {
					//--pos;
					break;
				}
				ch = fileLine.charAt(pos);
			}
			endPos = pos;
			str = fileLine.substring(startPos, endPos);

			try {
				currentInteger = Integer.valueOf(str);
				currentFloat = (double)currentInteger;

			} catch(NumberFormatException e1) {
				try {
					currentFloat = Double.valueOf(str);
				} catch(NumberFormatException e2) {
					// not a number really
					return (Toke.ILLEGAL_NUMBER_FORMAT);
				}
				return (Toke.NUMBER_FLOAT);
			}
			return (Toke.NUMBER_INT);
		} // single char tokenns
		else if((ch == ';') || (ch == '#')) {
			endPos = pos + 1;
			return (Toke.END_LINE);
		} else if(ch == '{') {
			endPos = pos + 1;
			return (Toke.LEFT_PAREN);
		} else if(ch == '}') {
			endPos = pos + 1;
			return (Toke.RIGHT_PAREN);
		} else if(ch == '=') {
			endPos = pos + 1;
			return (Toke.EQUALS);
		} else // keyword case maybe
		{
			while(pos < length) {
				++pos;
				if(pos == length) {
					break;
				}
				ch = fileLine.charAt(pos);
				if((Character.isWhitespace(ch)) || (ch == ';') || (ch == '#')) {
					break;
				}
			}

			endPos = pos;

			currentWord = fileLine.substring(startPos, endPos);

			Toke thistoken = hmp.get(currentWord.toLowerCase());
			if(thistoken == null) {
				return (Toke.DUNNO);
			} else {
				return (thistoken);
			}

		}
	}

	public void showParsingError() {
		int i;
		String indicatorLine;
		System.out.println("**Parsing error: " + errorMessage);
		System.out.println("  " + fileLine);

		indicatorLine = "";
		for(i = 0; i < startPos; ++i) {
			indicatorLine = indicatorLine + " ";

		}
		indicatorLine = indicatorLine + "|";
		if(endPos > startPos + 1) {
			for(; i < endPos - 2; ++i) {
				indicatorLine = indicatorLine + " ";
			}

			indicatorLine = indicatorLine + "|";
		}
		System.out.println("  " + indicatorLine);

	}

// for parsing
	private String fileLine = null;
	int lineIndex;
	String[] lines;
	int numLines;

	private int length;
	private int startPos;
	private int endPos;

	private Map<String, Toke> hmp;
	//private static int state;

	String errorMessage;
	private String currentWord;
	private String currentString;
	private int currentInteger;
	private double currentFloat;

	private Toke token;

	private int loadSyntax() {
		currentString = "";
		String str = currentString;
		currentString = str;

		hmp = new HashMap<String, Toke>();

		hmp.put("tolerance", Toke.TOLERANCE);
		hmp.put("steps", Toke.STEPS);
		hmp.put("spatial", Toke.SPATIAL);
		hmp.put("range", Toke.RANGE);
		hmp.put("proportion", Toke.PROPORTION);
		hmp.put("temporal", Toke.TEMPORAL);

		return (0);
	}

}

//////////////////////////////////////////////////////////////////////
////                         private variables                    ////

