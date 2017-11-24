/**
 * 
 */
package info.iskariot.pgsc.data;

/**
 * @author Pingger Shikkoken
 *
 */
public interface Project
{
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
