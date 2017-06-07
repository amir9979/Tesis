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
    public int median( int x, int y, int z ) {
        int m = z;
        // read ("Enter 3 numbers:",x,y,z);
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
		if (x > y) {
			if (y > z){
				m = y;			
			} else if (y <= z){
				if (x > z){
					m = z;				
				} else if (x <= z){
					m = x;
				}
			} 
		} else if (x <= y) {
			if (y > z){
				if (x > z){
					m = x;				
				} else if (x <= z){
					m = z;
				}
			} else if (y <= z) {
				m = y;
			}
		}
        return m;
    }
	
}
