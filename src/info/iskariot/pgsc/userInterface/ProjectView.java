/**
 * 
 */
package info.iskariot.pgsc.userInterface;

import info.iskariot.pgsc.data.Project;
import javafx.scene.layout.BorderPane;

/**
 * @author Pingger Shikkoken
 *
 */
public class ProjectView extends BorderPane
{
	private final Project project;
	
	/**
	 * Constructs a new ProjectView with the given Project
	 * 
	 * @param project
	 *            the Project to View
	 */
	public ProjectView(Project project)
	{
		this.project = project;
	}
	
	/**
	 * @return the associated Project
	 */
	public Project getProject()
	{
		return project;
	}
}
