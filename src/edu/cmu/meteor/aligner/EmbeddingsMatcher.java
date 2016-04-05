/*********************************
 * Copyright 2015, Christophe Servan, GETALP-LIG, University of Grenoble, France
 * Contact: christophe.servan@gmail.com
 *
 * The library is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 2.1 of the licence, or
 * (at your option) any later version.
 *
 * This program and library are distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * **********************************/
 
package edu.cmu.meteor.aligner;

import edu.lig.multivec.util.Lib_distance;

public class EmbeddingsMatcher {

	public static void match(int stage, Alignment a, Stage s, Lib_distance d, double threshold) {

		// Simplest possible matcher: test all word keys for equality

		for (int j = 0; j < s.words2.length; j++) {

			for (int i = 0; i < s.words1.length; i++) {

				// Match
				if (s.words1[i] == s.words2[j]) {

					Match m = new Match();
					m.module = stage;
					m.prob = 1;
					m.start = j;
					m.length = 1;
					m.matchStart = i;
					m.matchLength = 1;

					// Add this match to the list of matches and mark coverage
/*					System.err.println(s.m_wordStrings1.get(i));
					System.err.println(s.m_wordStrings2.get(j));
					System.err.println("1.0");*/
					s.matches.get(j).add(m);
					s.line1Coverage[i]++;
					s.line2Coverage[j]++;
				}
				else
				{
					double l_prob = d.getSimilarityProb(s.m_wordStrings1.get(i),s.m_wordStrings2.get(j));
					if (l_prob >= threshold)
					{
							
							Match m = new Match();
							m.module = stage;
/*							System.err.println(s.m_wordStrings1.get(i));
							System.err.println(s.m_wordStrings2.get(j));
							System.err.println(l_prob);*/
							m.prob = l_prob; 
							m.start = j;
							m.length = 1;
							m.matchStart = i;
							m.matchLength = 1;
		
							// Add this match to the list of matches and mark coverage
							s.matches.get(j).add(m);
							s.line1Coverage[i]++;
							s.line2Coverage[j]++;
					}
				}
			}
		}
	}
}
