package instrumentator.utils;

public class Pair<L,R> {
	private L left;
	private R right;
	
	public Pair(L left, R right){
		setLeft(left);
		setRight(right);
	}
	
	public L getLeft() {
		return left;
	}
	public void setLeft(L left) {
		this.left = left;
	}
	public R getRight() {
		return right;
	}
	public void setRight(R right) {
		this.right = right;
	}
	
	@Override
	public int hashCode(){
		return getLeft().hashCode() ^ getRight().hashCode();
	}
	
	@Override 
	public String toString(){
		return "[Passed: "+getLeft()+", Failed: "+getRight()+"]";
	}
	
	@Override
	public boolean equals(Object o){
		if (!(o instanceof Pair)) return false;
		Pair<L,R> other = (Pair<L, R>) o;
		return getLeft() == other.getLeft() && getRight() == other.getRight();
	}
	
	
}
