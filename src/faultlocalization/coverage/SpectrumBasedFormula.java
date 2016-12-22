package faultlocalization.coverage;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

//TODO: this class should implement a sprectrum based formula, that given coverage information return a statement ranking
public class SpectrumBasedFormula {
	public static enum Formulas {
		
		TARANTULA {

			@Override
			public String getName() {
				return "TARANTULA";
			}

			@Override
			public String description() {
				return "TARANTULA IS VERY CREEPY";
			}
			
			@Override
			public Float evaluate(int positive, int negative, int totalPositive, int totalNegative) {
				return (negative/(float)totalNegative)/((negative/(float)totalNegative)+(positive/(float)totalPositive));
			}
			
		},
		
		OCHIAI {

			@Override
			public String getName() {
				return "OCHIAI";
			}

			@Override
			public String description() {
				return "OCHIAI sounds like an asian te";
			}
			
			@Override
			public Float evaluate(int positive, int negative, int totalPositive, int totalNegative) {
				return negative / (float) Math.sqrt(totalNegative * (negative + positive));
			}
			
		},
		
		OP2 {

			@Override
			public String getName() {
				return "OP2";
			}

			@Override
			public String description() {
				return "It comes next to OP1";
			}
			
			@Override
			public Float evaluate(int positive, int negative, int totalPositive, int totalNegative) {
				return negative - (positive / (float)(totalPositive+1));
			}
			
		},
		
		BARINEL {

			@Override
			public String getName() {
				return "BARINEL";
			}

			@Override
			public String description() {
				return "BARINEL is another way to write BAR LINE but a wrong way";
			}
			
			@Override
			public Float evaluate(int positive, int negative, int totalPositive, int totalNegative) {
				return 1 - (positive/(float)(positive+negative));
			}
			
		},
		
		DSTAR {

			@Override
			public String getName() {
				return "DSTAR";
			}

			@Override
			public String description() {
				return "The Star!";
			}
			
			@Override
			public Float evaluate(int positive, int negative, int totalPositive, int totalNegative) {
				return (float) (Math.pow(negative, 2) / (float)(positive+(totalNegative - negative))); 
			}
			
		};
		
		public abstract String getName();
		public abstract String description();
		public abstract Float evaluate(int positive, int negative, int totalPositive, int totalNegative);
	}
	
	protected Formulas formula;
	
	public SpectrumBasedFormula(Formulas f) {
		this.formula = f;
	}
	
	public Map<Integer, Float> rankStatements(CoverageInformation coverageInfo) {
		Map<Integer, Float> ranking = new TreeMap<>();
		for (Integer l : coverageInfo.getMarkedLines()) {
			int pos = coverageInfo.getPassedCount(l);
			int neg = coverageInfo.getFailedCount(l);
			int totalPos = coverageInfo.getTotalPassedTests();
			int totalNeg = coverageInfo.getTotalFailedTests();
			ranking.put(l, this.formula.evaluate(pos, neg, totalPos, totalNeg));
		}
		return sortByValue(ranking);
	}
	
	@Override
	public String toString() {
		String res = "Spectrum-Based ranking\n";
		res += "------------------------------\n";
		res += "Formula used : " + this.formula.getName() + "\n";
		res += "Description : " + this.formula.description() + "\n";
		res += "------------------------------\n";
		return res;
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
	    return map.entrySet()
	              .stream()
	              .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
	              .collect(Collectors.toMap(
	                Map.Entry::getKey, 
	                Map.Entry::getValue, 
	                (e1, e2) -> e1, 
	                LinkedHashMap::new
	              ));
	}

}
