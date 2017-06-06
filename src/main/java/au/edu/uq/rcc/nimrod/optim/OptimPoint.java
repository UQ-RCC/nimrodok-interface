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

public final class OptimPoint {

	public final SetOfParams setOfParams;	// the set of parameters used to initially define the point domain
	public final int numParams;

	/**
	 * Is this point defined?
	 */
	public boolean defined;

	/**
	 * The number of numeric variables
	 */
	public final int dimensionality;

	/**
	 * Is the objective evaluated?
	 */
	public boolean evaluated;

	/**
	 * Defines the point, numerical variable values
	 */
	public final double[] coords;
	/**
	 * Defines the upper limit of values
	 */
	public final double[] max;
	/**
	 * Defines the lower limit of values
	 */
	public final double[] min;
	/**
	 * Defines the lower limit of values
	 */
	public final double[] range;

	public final boolean[] wrapping;

	/**
	 * Value of the objective function at coords
	 */
	public double objective;
	/**
	 * "proximity" to satisfying constraints
	 */
	public double howClose;
	/**
	 * Multiplies 'howClose' to produce the penalty
	 */
	public double penaltyFactor;
	/**
	 * Value of the objective function plus penalty there
	 */
	public double cost;

	public OptimPoint(SetOfParams setOfParams) {
		if(setOfParams == null) {
			throw new IllegalArgumentException("setOfParams may not be null");
		}

		coords = new double[setOfParams.size()];
		max = new double[setOfParams.size()];
		min = new double[setOfParams.size()];
		range = new double[setOfParams.size()];
		wrapping = new boolean[setOfParams.size()];

		this.setOfParams = setOfParams;
		int dim = 0;

		numParams = setOfParams.size();

		//mapping = new HashMap<Integer, Integer>();
		//System.out.println("OptimPoint constructor: with "+setParams.numParams+" parameters" );
		for(int i = 0; i < setOfParams.size(); ++i) {
			OptimParameter optimParam = setOfParams.get(i);
			max[dim] = optimParam.max;
			min[dim] = optimParam.min;
			range[dim] = max[dim] - min[dim];
			++dim;
		}

		dimensionality = dim;

		defined = false;
		evaluated = false;
	}

	public OptimPoint(OptimPoint model) {
		if(model == null) {
			throw new IllegalArgumentException("model cannot be null");
		}

		dimensionality = model.dimensionality;

		coords = Arrays.copyOf(model.coords, dimensionality);
		max = Arrays.copyOf(model.max, dimensionality);
		min = Arrays.copyOf(model.min, dimensionality);
		range = Arrays.copyOf(model.range, dimensionality);
		wrapping = Arrays.copyOf(model.wrapping, dimensionality);

		setOfParams = model.setOfParams;
		defined = model.defined;
		evaluated = false;
		numParams = model.numParams;
	}

	@Deprecated
	public void adjustCoordinate(int which, double value) throws IllegalArgumentException {
		if((which >= dimensionality) || (which < 0)) {
			throw new IllegalArgumentException();
		}
		coords[which] = value;
		evaluated = false;

	}

	@Deprecated
	public void confineToDomain() {
		for(int i = 0; i < dimensionality; ++i) {
			if(!wrapping[i]) {
				if(coords[i] > max[i]) {
					coords[i] = max[i];
				} else if(coords[i] < min[i]) {
					coords[i] = min[i];
				}
			}
		}
	}
	
	@Deprecated
	public void copyCoords(double[] newCoords) {
		if(newCoords == null || newCoords.length != dimensionality) {
			throw new IllegalArgumentException();
		}

		for(int i = 0; i < dimensionality; ++i) {
			coords[i] = newCoords[i];
		}

		defined = true;
	}

	@Deprecated
	public void copyPoint(OptimPoint from) {
		if(from.dimensionality != dimensionality) {
			throw new IllegalArgumentException();
		}
		copyCoords(from.coords);
		objective = from.objective;
		howClose = from.howClose;
		penaltyFactor = from.penaltyFactor;
		cost = from.cost;
		defined = from.defined;
		evaluated = from.evaluated;

	}

	public String generateEvalString() {
		StringBuilder sb = new StringBuilder();

		OptimParameter op;

		for(int i = 0; i < numParams; ++i) {
			op = setOfParams.get(i);
			sb.append(op.name);
			sb.append(" = ");
			sb.append(coords[i]);
			sb.append(" ");
		}

		return sb.toString();
	}

	@Deprecated
	public void setObjective(double value) {
		objective = value;
		penaltyFactor = 0.0;
		cost = objective;
		evaluated = true;
	}

	public void describePoint(String str) {
		System.out.printf("%s %s\n", str, this.toString());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");

		for(int i = 0; i < dimensionality - 1; ++i) {
			sb.append(coords[i]);
			sb.append(", ");
		}

		sb.append(coords[dimensionality - 1]);
		sb.append("]");

		return sb.toString();
	}
}
