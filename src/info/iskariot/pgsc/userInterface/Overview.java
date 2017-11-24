/**
 * 
 */
package info.iskariot.pgsc.userInterface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * This class describes the MainOverview Window
 * 
 * @author Pingger Shikkoken
 *
 */
public class Overview extends Application
{
	private ProjectOverview	pov;
	
	private BorderPane		root;
	
	private Scene			scene;
	
	private Stage			stage;
	
	private TabPane			tabs;
	
	/**
	 * Request the Exit of the Overview
	 */
	public void requestExit()
	{
		if(root.isDisable()) { return; }
		stage.close();
	}
	
	private void setupInterface()
	{
		setupMenu();
		setupMain();
	}
	
	/**
	 * 
	 */
	private void setupMain()
	{
		tabs = new TabPane();
		root.setCenter(tabs);
		tabs.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
		setupProjectsOverview();
	}
	
	/** Setup the Top MenuBar */
	private void setupMenu()
	{
		MenuBar bar = new MenuBar();
		root.setTop(bar);
		
		Menu File = new Menu("File");
		{
			MenuItem miExit = new MenuItem("Exit");
			miExit.setOnAction(event -> requestExit());
			File.getItems().addAll(miExit);
		}
		
		Menu Help = new Menu("Help");
		{
			MenuItem License = new MenuItem("License");
			License.setOnAction(
					event -> new Alert(AlertType.INFORMATION, "This Project is licensed under The Unlicense.")
							.showAndWait());
			MenuItem About = new MenuItem("About");
			About.setOnAction(
					event -> new Alert(AlertType.INFORMATION, "Developed and maintained by Pingger Shikkoken.")
							.showAndWait());
			Help.getItems().addAll(License, About);
		}
		
		bar.getMenus().addAll(File, Help);
	}
	
	private void setupProjectsOverview()
	{
		Tab projectsOverview = new Tab("Overview", pov = new ProjectOverview());
		projectsOverview.setClosable(false);
		tabs.getTabs().add(projectsOverview);
	}
	
	/** Creates the Scene and the root and sets the Properties for the Scene */
	private void setupScene()
	{
		root = new BorderPane();
		scene = new Scene(root);
		stage.setScene(scene);
	}
	
	private void setupStage(Stage s)
	{
		stage = s;
		s.setTitle("Pinggers Git/SVN Client");
		s.setHeight(480);
		s.setWidth(800);
		s.setMinHeight(480);
		s.setMinWidth(480);
		s.centerOnScreen();
		// Prevent Closing of the Main Window when it is disabled
		s.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
			if(root.isDisable())
			{
				event.consume();
			}
		});
		// Use own Exit handling (double check for isDisable ...)
		s.setOnCloseRequest(event -> {
			requestExit();
			event.consume();
		});
	}
	
	/**
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		setupStage(primaryStage);
		setupScene();
		setupInterface();
		stage.requestFocus();
		root.requestLayout();
		primaryStage.show();
	}
}
