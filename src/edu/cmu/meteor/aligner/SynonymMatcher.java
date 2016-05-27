/*
 * Carnegie Mellon University
 * Copyright (c) 2004, 2010
 * 
 * This software is distributed under the terms of the GNU Lesser General
 * Public License.  See the included COPYING and COPYING.LESSER files.
 * 
 */

package edu.cmu.meteor.aligner;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import static java.util.Arrays.asList;

public class SynonymMatcher {

	public static void match(String language, int stage, Alignment a, Stage s,
			SynonymDictionary synonyms) {
 
                // Map words to sets of synonym set numbers
                
		Hashtable<Integer, HashSet<Integer>> string1Syn = new Hashtable<Integer, HashSet<Integer>>();
		Hashtable<Integer, HashSet<Integer>> string2Syn = new Hashtable<Integer, HashSet<Integer>>();

		System.err.println(a.words1.toArray(new String[a.words1.size()]));
		System.err.println(a.words2.toArray(new String[a.words2.size()]));
		if (synonyms.ttw != null )
		{
			String out1=synonyms.ttw.tag(new String[] {"This", "is", "a", "test", "."});
			String out2=synonyms.ttw.tag(new String[] {"A", "test", "this", "is", "."});
			System.err.println("OUT1 " + out1);
			System.err.println("OUT2 " + out2);
		}
		else
		{
			System.err.println("synonyms.ttw Null !");
			System.exit(0);
		}
		System.exit(0);
		// Line 1
		for (int i = 0; i < a.words1.size(); i++) {
			HashSet<Integer> set = new HashSet<Integer>(synonyms
					.getSynSets(language, a.words1.get(i)));
			set.addAll(synonyms.getStemSynSets(language,a.words1.get(i)));
			string1Syn.put(i, set);
		}

		// Line 2
		for (int i = 0; i < a.words2.size(); i++) {
			HashSet<Integer> set = new HashSet<Integer>(synonyms
					.getSynSets(language, a.words2.get(i)));
			set.addAll(synonyms.getStemSynSets(language,a.words2.get(i)));
			string2Syn.put(i, set);
		}

		for (int j = 0; j < a.words2.size(); j++) {

			for (int i = 0; i < a.words1.size(); i++) {

				Iterator<Integer> sets1 = string1Syn.get(i).iterator();
				HashSet<Integer> sets2 = string2Syn.get(j);

				boolean syn = false;
				double weight = 0;
				while (sets1.hasNext()) {
					if (sets2.contains(sets1.next())) {
						syn = true;
						weight = 1;
						break;
					}
				}

				// Match if DIFFERENT words with SAME synset
				if (syn && s.words1[i] != s.words2[j]) {

					Match m = new Match();
					m.module = stage;
					m.prob = weight;
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