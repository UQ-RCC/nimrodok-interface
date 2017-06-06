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

/* This is a relic from the past - it needs to go - Zane */
public class ArrayOfPoints {

	public final int numPoints;
	public final int dimensionality;
	public final OptimPoint[] pointArray;
	public final SetOfParams setOfParams;

	/**
	 * Construct a new ArrayOfPoints object.
	 *
	 * @param modelPoint The model point. May not be null.
	 * @param howMany The number of points to create. Must be greater than 0.
	 */
	public ArrayOfPoints(OptimPoint modelPoint, int howMany) {
		if (modelPoint == null) {
			throw new IllegalArgumentException("modelPoint cannot be null");
		}

		if ((numPoints = howMany) <= 0) {
			throw new IllegalArgumentException("Cannot create an ArrayOfPoints with 0 or negative count.");
		}

		pointArray = new OptimPoint[howMany];

		for (int i = 0; i < numPoints; ++i) {
			pointArray[i] = new OptimPoint(modelPoint);
		}

		dimensionality = modelPoint.dimensionality;
		setOfParams = modelPoint.setOfParams;
	}

	/**
	 * Construct a new ArrayOfPoints object using the given set of parameters.
	 *
	 * @param sop The SetOfParams object to be passed to each point for
	 * creation. May not be null.
	 * @param howMany The number of points to create. Must be greater than 0.
	 */
	public ArrayOfPoints(SetOfParams sop, int howMany) {
		this(new OptimPoint(sop), howMany);
	}

	public ArrayOfPoints(OptimPoint[] points) {
		if (points == null || points.length == 0) {
			throw new IllegalArgumentException("points");
		}

		int len = points[0].dimensionality;

		for (int i = 1; i < points.length; ++i) {
			if (points[i].dimensionality != len) {
				throw new IllegalArgumentException("Mismatching points");
			}
		}

		numPoints = points.length;
		pointArray = points;
		dimensionality = points[0].dimensionality;
		setOfParams = points[0].setOfParams;
	}

	public boolean allPointsEvaluated() {
		for (int i = 0; i < numPoints; ++i) {
			if (!pointArray[i].evaluated) {
				return false;
			}
		}
		return true;
	}

	public String describePoints() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < pointArray.length; ++i) {
			sb.append(pointArray[i].toString());
			sb.append("\n\t");
			if (pointArray[i].evaluated) {
				sb.append("cost: ");
				sb.append(pointArray[i].cost);
			} else {
				sb.append("not evaluated");
			}

			sb.append("\n");
		}

		return sb.toString();
	}

	public void setObjective(int index, double objective) throws IllegalArgumentException {
		if (index < 0 || index >= numPoints) {
			throw new IllegalArgumentException("Setting objective for index out of range of point array");
		}
		pointArray[index].setObjective(objective);
	}
}
