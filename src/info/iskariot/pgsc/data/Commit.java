/**
 * 
 */
package info.iskariot.pgsc.data;

/**
 * @author Pingger Shikkoken
 *
 */
public interface Commit
{
	/** @return the Branch in which this Commit was made */
	public Branch getBranch();
	
	/** @return the Descrption of this Commit */
	public String getDescription();
	
	/** @return the Hashcode of this Commit */
	public String getHashcode();
	
	/** @return the Title of this Commit */
	public String getTitle();
}
