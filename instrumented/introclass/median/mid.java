package introclass.median;

public class mid {

    public mid() {
    }

    /*@
    @ requires true;
    @ ensures ((\result == a) || (\result == b) || (\result == c));
    @ ensures ( (a == b) ==> ((\result == a) || (\result == b) ) );
    @ ensures ( (b == c) ==> ((\result == b) || (\result == c) ) );
    @ ensures ( (a == c) ==> ((\result == a) || (\result == c) ) );
    @ ensures ((a!=b && a!=c && b!=c) ==> (\exists int n; (n == a) || (n == b) || (n == c); \result>n));
    @ ensures ((a!=b && a!=c && b!=c) ==> (\exists int n; (n == a) || (n == b) || (n == c); \result<n));
    @ signals (RuntimeException e) false;
    @
    @*/
    public int median(int x, int y, int z) {
        int m = z;
        faultlocalization.coverage.CoverageInformationHolder.getInstance().getCoverageInformation("/home/joako/workspace/FaultLocalization/faulty_code/introclass/median/mid.java").mark(20);
        /*if ( y < z ) {
            if ( x < y ){
            	m = y;
            }else if ( x < z )
            	m = z;
        }else{
        	if ( x > y )
        		m = y;
        	else if ( x > z )
        		m = x;
        }*/
        if (faultlocalization.coverage.CoverageInformationHolder.getInstance().getCoverageInformation("/home/joako/workspace/FaultLocalization/faulty_code/introclass/median/mid.java").mark(33, x > y)) {
            if (faultlocalization.coverage.CoverageInformationHolder.getInstance().getCoverageInformation("/home/joako/workspace/FaultLocalization/faulty_code/introclass/median/mid.java").mark(34, y > z)) {
                m = y;
                faultlocalization.coverage.CoverageInformationHolder.getInstance().getCoverageInformation("/home/joako/workspace/FaultLocalization/faulty_code/introclass/median/mid.java").mark(35);
            } else {
                if (faultlocalization.coverage.CoverageInformationHolder.getInstance().getCoverageInformation("/home/joako/workspace/FaultLocalization/faulty_code/introclass/median/mid.java").mark(36, y <= z)) {
                    if (faultlocalization.coverage.CoverageInformationHolder.getInstance().getCoverageInformation("/home/joako/workspace/FaultLocalization/faulty_code/introclass/median/mid.java").mark(37, x > z)) {
                        m = z;
                        faultlocalization.coverage.CoverageInformationHolder.getInstance().getCoverageInformation("/home/joako/workspace/FaultLocalization/faulty_code/introclass/median/mid.java").mark(38);
                    } else {
                        if (faultlocalization.coverage.CoverageInformationHolder.getInstance().getCoverageInformation("/home/joako/workspace/FaultLocalization/faulty_code/introclass/median/mid.java").mark(39, x <= z)) {
                            m = x;
                            faultlocalization.coverage.CoverageInformationHolder.getInstance().getCoverageInformation("/home/joako/workspace/FaultLocalization/faulty_code/introclass/median/mid.java").mark(40);
                        }
                    }
                }
            }
        } else {
            if (faultlocalization.coverage.CoverageInformationHolder.getInstance().getCoverageInformation("/home/joako/workspace/FaultLocalization/faulty_code/introclass/median/mid.java").mark(43, x <= y)) {
                if (faultlocalization.coverage.CoverageInformationHolder.getInstance().getCoverageInformation("/home/joako/workspace/FaultLocalization/faulty_code/introclass/median/mid.java").mark(44, y > z)) {
                    if (faultlocalization.coverage.CoverageInformationHolder.getInstance().getCoverageInformation("/home/joako/workspace/FaultLocalization/faulty_code/introclass/median/mid.java").mark(45, x > z)) {
                        m = x;
                        faultlocalization.coverage.CoverageInformationHolder.getInstance().getCoverageInformation("/home/joako/workspace/FaultLocalization/faulty_code/introclass/median/mid.java").mark(46);
                    } else {
                        if (faultlocalization.coverage.CoverageInformationHolder.getInstance().getCoverageInformation("/home/joako/workspace/FaultLocalization/faulty_code/introclass/median/mid.java").mark(47, x <= z)) {
                            m = z;
                            faultlocalization.coverage.CoverageInformationHolder.getInstance().getCoverageInformation("/home/joako/workspace/FaultLocalization/faulty_code/introclass/median/mid.java").mark(48);
                        }
                    }
                } else {
                    if (faultlocalization.coverage.CoverageInformationHolder.getInstance().getCoverageInformation("/home/joako/workspace/FaultLocalization/faulty_code/introclass/median/mid.java").mark(50, y <= z)) {
                        m = y;
                        faultlocalization.coverage.CoverageInformationHolder.getInstance().getCoverageInformation("/home/joako/workspace/FaultLocalization/faulty_code/introclass/median/mid.java").mark(51);
                    }
                }
            }
        }
        return faultlocalization.coverage.CoverageInformationHolder.getInstance().getCoverageInformation("/home/joako/workspace/FaultLocalization/faulty_code/introclass/median/mid.java").mark(54, m);
    }
}
