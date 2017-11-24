/**
 * 
 */
package info.iskariot.pgsc.data;

import java.util.Collection;

/**
 * @author Pingger Shikkoken
 *
 */
public interface Project
{
	/** @return a list of the Branches within this Project */
	public Collection<Branch> getBranches();
	
	/**
	 * @return the Description of this Project
	 */
	public String getDescription();
	
	/**
	 * @return the Title of this Project
	 */
	public String getTitle();
	
	/**
	 * @return the Type of this Project
	 */
	public Type getType();
	
	/**
	 * Called when the UI or User would like this Project to fetch the latest information (e.g. from the Server).<br>
	 * Should return immediately and add a Job for the Update to the general JobList. That Job should call a View Update
	 * after it finishes.
	 */
	public void requestUpdate();
}
