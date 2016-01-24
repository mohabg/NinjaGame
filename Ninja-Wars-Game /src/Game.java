import java.util.ArrayList;
import java.util.Random;

import javafx.scene.Group;

import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.*;
import javafx.scene.text.*;

public class Game {
	private final String TITLE = "Ninja Wars";
	private final int ninjaWidth = 30;
	private final int ninjaHeight = 40;
	private final int alienWidth = 25;
	private final int alienHeight = 35;
	private double ninjaSpeed = 1.7;
	private double alienSpeed = 1.25;
	private Boolean bossCreated = false;
	private long lastPressTime;
	private long lastBulletTime;
	private long lastOrbTime;
	private Scene myScene;
	private Ninja myNinja;
	private AlienBoss alienBoss;
	private Image ninjaImage = new Image(getClass().getClassLoader().getResourceAsStream("ninja_standing.png"));
	private Image slideImage = new Image(getClass().getClassLoader().getResourceAsStream("ninja_shield.png"));
	private Image alienBossImage = new Image(getClass().getClassLoader().getResourceAsStream("alien_boss.png"));
	private ArrayList<ArrayList<Alien>> aliensList;
	private Input input;
	private Group root;
	private SpriteManager spriteManager;
	private ProgressBar healthBar;
	private ProgressBar shieldBar;
	private ProgressBar alienBossBar;
	
	public String getTitle(){
		return TITLE;
	}
	
	public Scene init(int width, int height){
		Image alienImage = new Image(getClass().getClassLoader().getResourceAsStream("alien.png"));
		
		//Create background
		Image background = new Image(getClass().getClassLoader().getResourceAsStream("ninja_background.png"));
		ImageView backgroundView = new ImageView(background);
		backgroundView.setFitHeight(height);
		backgroundView.setFitWidth(width);
		root = new Group();
		root.getChildren().add(backgroundView);
		
		aliensList = new ArrayList<ArrayList<Alien>>();
		lastBulletTime = System.currentTimeMillis();
		
		//Create and center ninja
		myNinja = new Ninja(width / 2 - ninjaWidth / 2, height / 1.2, ninjaSpeed, 0, ninjaImage, 60, 0);
		myNinja.resize(ninjaWidth, ninjaHeight);
		root.getChildren().add(myNinja.getImageView());
		
		spriteManager = new SpriteManager(root, myNinja, aliensList);
		createAliens(width, height, alienImage);
		spriteManager.addSprite(myNinja);
		
		addProgressBars(width);
		myScene = new Scene(root,width, height);
		input = new Input(myScene);
		input.addHandlers();
		return myScene;
	}

	private void addProgressBars(int width) {
		healthBar = new ProgressBar(1.0);
		healthBar.setStyle("-fx-accent: red");
		shieldBar = new ProgressBar(1.0);
		
		root.getChildren().add(healthBar);
		root.getChildren().add(shieldBar);
		
		healthBar.relocate(width - 220, 5);
		shieldBar.relocate(width - 115, 5);
		alienBossBar = new ProgressBar(1.0);
	}

	private void createAliens(int width, int height, Image alienImage) {
		int spaceBetweenAliens = 10;
		//Center the rows of aliens
		double startingXPosition = width / 2 - ninjaWidth / 2 - (alienWidth * 3) - (spaceBetweenAliens * 3);
		double xPosition = startingXPosition;
		double startingYPosition = height / 10;
		double yPosition = startingYPosition;
		
		//Create aliens in square formation
		for(int i = 0; i < 7; i++){
			ArrayList<Alien> aliensInSameCol = new ArrayList<Alien>();
		    xPosition += alienWidth + spaceBetweenAliens;
			yPosition = startingYPosition;
			for(int j = 0; j < 7; j++){
			Alien alien = new Alien(xPosition, yPosition, alienSpeed, 0, alienImage, 10, 0);
			alien.resize(alienWidth, alienHeight);
			yPosition += alienHeight;
			aliensInSameCol.add(alien);
			spriteManager.addSprite(alien);
			root.getChildren().add(alien.getImageView());
		}
			aliensList.add(aliensInSameCol);
		}
	}
	public void updateAndRemoveSprites(int size){
		spriteManager.updateSprites(size);
		shouldAlienShoot();
		spriteManager.checkCollisions();
		updateProgressBar();
		spriteManager.cleanUpSprites();
		nextLevel(size);
		regenerateShield();
		
		if(alienBoss != null){
			shouldAlienBossShoot();
			}
		
		endGame(size);
	}
	public void shouldAlienBossShoot(){
		Random rand = new Random();
		int randomInt = rand.nextInt(10); 
		if(System.currentTimeMillis() - lastBulletTime >= 1000){
			//Throw Rock
			if(randomInt >= 5){
			AlienRock alienRock = alienBoss.getAlienRock();
			spriteManager.addSprite(alienRock);
			root.getChildren().add(alienRock.getImageView());
			lastBulletTime = System.currentTimeMillis();
			}
		}
		if(System.currentTimeMillis() - lastOrbTime >= 2500){
			//Throw orb
			if(randomInt >= 5){
				AlienOrb alienOrb = alienBoss.getAlienOrb();
				spriteManager.addSprite(alienOrb);
				root.getChildren().add(alienOrb.getImageView());
				lastOrbTime = System.currentTimeMillis();
			}
		}
	}
	private void nextLevel(int size) {
		//Check if all aliens have been killed
				int numberOfAliens = 0;
				for(ArrayList<Alien> col : aliensList){
					for(Alien alien : col){
						numberOfAliens++;
					}
				}
				if(numberOfAliens == 0 && !bossCreated){
					bossCreated = true;
					alienBoss = new AlienBoss(size / 3, size / 12, 2, 0, alienBossImage, 100, 0);
					alienBoss.resize(90, 140);
					spriteManager.addSprite(alienBoss);
					root.getChildren().add(alienBoss.getImageView());
					root.getChildren().add(alienBossBar);
					alienBossBar.relocate(15, 5);
				}			
	}

	private void endGame(int size) {
		if(myNinja.isDead()){
			Text gameOver = new Text();
			gameOver.setText("Game Over");
			gameOver.setX(size / 3);
			gameOver.setY(size / 2);
			gameOver.setCache(true);
		    gameOver.setStyle("-fx-font: 48 arial;");
		    
			root.getChildren().add(gameOver);
		}
		if(alienBoss != null){
		if(alienBoss.getHealth() <= 0){
			alienBoss.kill();
			Text youWin = new Text();
			youWin.setText("You Win");
			youWin.setX(size / 3);
			youWin.setY(size / 2);
			youWin.setCache(true);
		    youWin.setStyle("-fx-font: 48 arial;");
		    
			root.getChildren().add(youWin);
		}
	}
	}
	public void regenerateShield(){
		Sprite shield = myNinja.getShield();
		if(shield.getHealth() < 100){
		shield.setHealth(shield.getHealth() + 0.05 );
	}
	}
	public void updateProgressBar(){
		double healthPercentage = myNinja.getHealth() / 50;
		double shieldPercentage = myNinja.getShield().getHealth() / 100;
		
		healthBar.setProgress(healthPercentage);
		shieldBar.setProgress(shieldPercentage);
		if(alienBoss != null){
			double alienBossPercentage = alienBoss.getHealth() / 100;
			alienBossBar.setProgress(alienBossPercentage);
			}
	}
	public void shouldAlienShoot(){
		//Wait 1200 millis before shooting again
		if(System.currentTimeMillis() - lastBulletTime <= 1200){
			return;
		}
		lastBulletTime = System.currentTimeMillis();
		double ninjaX = myNinja.getX();
		Random random = new Random();
		for(int i = 0; i < aliensList.size(); i++){
			boolean shot = false;
			int randomNum = random.nextInt(10);
			ArrayList<Alien> alienCol = aliensList.get(i);
			if(alienCol.size() == 0){
				continue;
			}
			Alien firstInCol = alienCol.get(alienCol.size() - 1);
			double alienX = firstInCol.getX();

			//Ninja moving right + alien is to the right
			if(alienX > ninjaX){
				if(myNinja.isMovingRight){
					if(randomNum >= 6){
						shot = true;
					shoot(firstInCol);
					}
				}
			}
			//Ninja moving left + alien is to the left
			if(alienX <= ninjaX){
				if(myNinja.isMovingLeft){
					if(randomNum >= 6){
						shot = true;
					shoot(firstInCol);
					}
				}
			}
			//Regardless of alien position
			if(randomNum >= 6){
				//If this alien did not shoot already
				if(!shot){
					shot = true;
				shoot(firstInCol);
			}
			}
		}
		myNinja.isMovingLeft = false;
		myNinja.isMovingRight = false;
	}
	public void shoot(Alien alien){
		Bullet bullet = alien.getBullet();
		root.getChildren().add(bullet.getImageView());
		bullet.move();
		spriteManager.addSprite(bullet);
	}
	
	public void respondToInput(){
		//Cheat key: Throw FireBall
		if(input.shiftPressed() && !myNinja.isDead()){
			FireBall fireball = myNinja.getFireBall();
			spriteManager.addSprite(fireball);
			root.getChildren().add(fireball.getImageView());
		}
		if(input.leftPressed()){
			if(myNinja.canMoveLeft && !myNinja.isDead()){
			myNinja.isMovingLeft = true;
			myNinja.setXVelocity(ninjaSpeed * -1);
			myNinja.move();
		}
		}
		if(input.rightPressed() && !myNinja.isDead()){
			if(myNinja.canMoveRight){
			myNinja.isMovingRight = true;
			myNinja.setXVelocity(ninjaSpeed);
			myNinja.move();
		}
		}
		if(input.spacePressed() && !myNinja.isDead()){
			if(lastPressTime == 0){
			lastPressTime = System.currentTimeMillis();
			throwStar();
			}
			//Wait 300 millis before throwing another star
			else if(System.currentTimeMillis() - lastPressTime >= 300){
				throwStar();
			}
		}
		if(input.downPressed() && !myNinja.isDead() && myNinja.getShield().getHealth() > 0){
			myNinja.isShieldOn = true;
			myNinja.setImage(slideImage);
			myNinja.resize(ninjaWidth, ninjaHeight);
		}
		else{
			myNinja.isShieldOn = false;
			myNinja.setImage(ninjaImage);
			myNinja.resize(ninjaWidth, ninjaHeight);
		}
	}
	
	private void throwStar() {
		//If shield is not on, throw star
		if(!myNinja.isShieldOn){
			NinjaStar star = myNinja.getStar();
			root.getChildren().add(star.getImageView());
			spriteManager.addSprite(star);
			lastPressTime = System.currentTimeMillis();
}
	}
}
