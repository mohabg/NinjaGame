import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RunGame extends Application{
		private final int SIZE = 550;
	    private static final int FRAMES_PER_SECOND = 60;
	    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
		private Game myGame;
		private Input input;
		
		public static void main(String[] args){
			launch(args);
		}
		
	@Override
	public void start(Stage primaryStage) throws Exception {
		Image splashImage = new Image(getClass().getClassLoader().getResourceAsStream("splash_screen.jpeg"));
		myGame = new Game();
		primaryStage.setTitle(myGame.getTitle());
		Group root = new Group();
		
		Scene splash = showSplashScreen(primaryStage, splashImage, root);
		
		Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount( Timeline.INDEFINITE );
        
        KeyFrame kf = new KeyFrame(
            Duration.millis(MILLISECOND_DELAY),                
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent ae)
                { 
              	  //Hide splash screen
                  if(primaryStage.getScene() == splash){
                	  //Press space to play game
                	  if(input.spacePressed()){
                		  Scene scene = myGame.init(SIZE, SIZE);
                			input = new Input(scene); 
                			input.addHandlers();
                			primaryStage.setScene(scene);
                			primaryStage.show();
                	  }
                  }
                  else{
                	  //Main loop
                	  myGame.respondToInput();
                  myGame.updateAndRemoveSprites(SIZE);
                  }
                }
            });
        
        gameLoop.getKeyFrames().add( kf );
        gameLoop.play();
    }

	private Scene showSplashScreen(Stage primaryStage, Image splashImage, Group root) {
		//Display splash screen
		Scene splash = new Scene(root,SIZE, SIZE);
		ImageView splashScreen = new ImageView(splashImage);
		splashScreen.setFitHeight(SIZE);
		splashScreen.setFitWidth(SIZE);
		root.getChildren().add(splashScreen);
		primaryStage.setScene(splash);
		input = new Input(splash);
		input.addHandlers();
		primaryStage.show();
		return splash;
	}
	}


