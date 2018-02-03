/**
 * 
 */
package info.iskariot.pgsc.data;

/**
 * @author Pingger Shikkoken
 *
 */
public interface Branch
{
	/** @return the Branches Hashcode */
	public String getHashcode();
	
	/** @return the Label of the Branch */
	public String getLabel();
	
	/** @return the Parent Branch. Should return null if this the master/HEAD Branch */
	public Branch getParent();
	
	/** @return the Project to which the Branch belongs */
	public Project getProject();
}
