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

/**
 * A wrapper around {@link OptimPoint} that supports multiple objectives.
 */
public class PointContainer {

	public final Double[] objectives;
	public final OptimPoint point;

	/**
	 * Construct a new point container.
	 *
	 * @param point The point to wrap.
	 * @param numObjectives The number of objectives.
	 *
	 * @throws IllegalArgumentException - if <code>point == null</code> or
	 * <code>numObjectives &lt;= 0</code>
	 */
	public PointContainer(OptimPoint point, int numObjectives) {

		if(point == null || numObjectives <= 0) {
			throw new IllegalArgumentException();
		}

		this.point = point;
		this.objectives = new Double[numObjectives];

		/* Just to be safe. Not sure what Java's policy on the default
		 * value of the integral wrappers. */
		for(int i = 0; i < numObjectives; ++i) {
			this.objectives[i] = null;
		}
		//Arrays.setAll(m_Objectives, null); // <-- Only in JDK8
	}

	/**
	 * Does this point have all its objective results calculated?
	 *
	 * @return If this point has all objective results calculated, this function
	 * returns true. Otherwise false.
	 */
	public boolean hasAllObjectives() {
		for(int i = 0; i < objectives.length; ++i) {
			if(objectives[i] == null) {
				return false;
			}
		}

		return true;
	}

}
