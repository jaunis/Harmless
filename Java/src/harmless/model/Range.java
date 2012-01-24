/**
 * 
 */
package harmless.model;

/**
 * @author Jean AUNIS
 *
 */
public class Range {
	
	private int from;
	private int to;
	private Slice slice;
	/**
	 * @return the from
	 */
	public int getFrom() {
		return from;
	}
	/**
	 * @param from the from to set
	 */
	public void setFrom(int from) {
		this.from = from;
	}
	/**
	 * @return the to
	 */
	public int getTo() {
		return to;
	}
	/**
	 * @param to the to to set
	 */
	public void setTo(int to) {
		this.to = to;
	}
	/**
	 * @return the slice
	 */
	public Slice getSlice() {
		return slice;
	}
	/**
	 * @param slice the slice to set
	 */
	public void setSlice(Slice slice) {
		this.slice = slice;
	}
	public Range(int from, int to, Slice slice) {
		this.from = from;
		this.to = to;
		this.slice = slice;
	}
	
	

}
