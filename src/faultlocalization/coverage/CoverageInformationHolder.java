package faultlocalization.coverage;

import java.util.Map;
import java.util.TreeMap;

public class CoverageInformationHolder {
	
	private Map<String, CoverageInformation> coverageHolder;
	private static CoverageInformationHolder instance;
	
	public static CoverageInformationHolder getInstance() {
		if (instance == null)
			instance = new CoverageInformationHolder();
		return instance;
	}
	
	private CoverageInformationHolder() {
		this.coverageHolder = new TreeMap<>();
	}
	
	public void instantiateCoverageInformation(String file) {
		this.coverageHolder.put(file, new CoverageInformation());
	}
	
	public CoverageInformation getCoverageInformation(String file) {
		return coverageHolder.get(file);
	}

}
