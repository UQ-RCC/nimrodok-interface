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

public final class Configuration {
	public Configuration(int numObjectives, int rngSeed, ConvergenceTest convSettings, Properties props) {
		this.m_NumObjectives = numObjectives;
		this.m_RNGSeed = rngSeed;
		this.m_Convergence = convSettings;
		this.m_Properties = new Properties(props);
	}

	private final int m_NumObjectives;
	private final int m_RNGSeed;
	private final ConvergenceTest m_Convergence;
	private final Properties m_Properties;

	public final int objectiveCount() {
		return m_NumObjectives;
	}
	
	public final int rngSeed() {
		return m_RNGSeed;
	}
	
	public final ConvergenceTest convergenceSettings() {
		return m_Convergence;
	}

	public final Properties customProperties() {
		return m_Properties;
	}
}
