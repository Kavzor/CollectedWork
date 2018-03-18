package configuration;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class View {
	/**
	 * View object relates to the current active instance
	 */
	private static View view = new View();
	
	//Keeps track of current active view
	private String viewResource;
	
	//Enables configuration of the view
	private static ViewConfiguration configuration;
	
	private static final String W_SEPERATE_VIEWS = "Warning: It's recommended that you move fxml files to a source folder \n"
			+ "\tUse View.getConfiguration().setViewSourceFolder(String path) to initalize the path";
	private static final String E_SETUP_MAINSTAGE = "Main stage has not been initalized \n"
			+ " \t\t use View.getConfiguration().setMainStage(Stage primaryStage) to initalize";
	private static final String FOLDER_SEPERATOR = "/";
	
	/**
	 * Prevent instansitation from outside
	 */
	private View() {}
	
	/**
	 * Enables the use of Viewconfiguration to maniuplate view paths
	 * Mandatory to setup mainstage
	 * @return View Configurations object
	 */
	public static ViewConfiguration getConfiguration() {
		if(configuration == null) {
			configuration = new ViewConfiguration();
		}
		return configuration;
	}
	
	/**
	 * Instantly refreshes the current active View page
	 */
	public static void refreshPage() {
		view.invokePage();
	}
	
	/**
	 * Sets the current active View page
	 * @param viewPath path to view
	 */
	public static void switchTo(String viewPath) {
		view.setViewPath(viewPath);
		view.invokePage();
	}
	
	/**
	 * Used when you want to access a view but not active it
	 * Example, dialogs
	 * @param view path, will use internal source pathing
	 * @return the parent object
	 */
	public static Parent getPageResource(String view) {
		try {
			String sourcePath = getConfiguration().getSourcePath();
			if(sourcePath == null) {
				return View.getFXMLLoader(view).load();
			}
			else {
				return View.getFXMLLoader(sourcePath + FOLDER_SEPERATOR + view).load();	
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * If you only need the loader
	 * @param resource used by the FXMLLoader
	 * @return the loader
	 */
	public static FXMLLoader getFXMLLoader(String resource) {
		return new FXMLLoader(View.class.getResource(resource));
	}
	
	/**
	 * Exists the current View
	 */
	public static void closeView() {
		System.exit(0);
	}
	
	private void setViewPath(String viewPath) {
		this.viewResource = viewPath;
	}
	
	private void invokePage() {
		Parent page = getPageResource(this.viewResource);
		Stage mainStage = getConfiguration().getMainStage();
		if(pageExist()) {
			mainStage.getScene().setRoot(page);
		}
		else {
			this.setNewPage(page);
		}
		mainStage.sizeToScene();
		mainStage.show();
	}
	
	private void setNewPage(Parent page) {
		int width = getConfiguration().mWidth > 0 ? getConfiguration().mWidth : 800;
		int height = getConfiguration().mHeight > 0 ? getConfiguration().mHeight : 600;
		Scene scene = new Scene(page, width, heights);
		getConfiguration().getMainStage().setScene(scene);
	}

	private boolean pageExist() {
		return getConfiguration().getMainStage().getScene() != null;
	}
	
	public static class ViewConfiguration {
		private Stage mainStage;
		private String viewSourceFolder;
		private int mHeight;
		private int mWidth;
		
		/**
		 * To prevent instansiation from outside
		 */
		private ViewConfiguration() {
			
		}
		
		/**
		 * Path to source folder, usually that is a seperated source folder containting views
		 * @param viewSourceFolder the path
		 */
		public void setViewSourceFolder(String viewSourceFolder) {
			this.viewSourceFolder = viewSourceFolder;
		}
		
		/**
		 * Primary stage, usually the stage received by JavaFX Application at startup
		 * @param mainStage
		 */
		public void setMainStage(Stage mainStage) {
			this.mainStage = mainStage;
		}
		
		/**
		 * Optional dimension, defaults to 800x600
		 * @param width to set
		 * @param height to set
		 */
		public void setDimension(int width, int height) {
			mWidth = width;
			mHeight = height;
		}
		
		Stage getMainStage() {
			if(mainStage == null) {
				throw new NullPointerException(E_SETUP_MAINSTAGE);
			}
			return this.mainStage;
		}
		
		String getSourcePath() {
			if(this.viewSourceFolder == null) {
				
				System.out.println(W_SEPERATE_VIEWS);
			}
			return this.viewSourceFolder;
		}
	}
}
