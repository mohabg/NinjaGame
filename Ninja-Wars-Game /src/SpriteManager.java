import java.util.*;

import javafx.scene.Group;

public class SpriteManager {
	private ArrayList<ArrayList<Alien>> aliensList;
	private ArrayList<Sprite> sprites; 
	private ArrayList<Sprite> spritesToRemove = new ArrayList<Sprite>();
	
	private Ninja myNinja;
	private AlienBoss alienBoss;
	
	private ArrayList<AlienRock> alienRocks = new ArrayList<AlienRock>();
	private ArrayList<AlienOrb> alienOrbs = new ArrayList<AlienOrb>();
	private ArrayList<FireBall> fireBalls = new ArrayList<FireBall>();
	private ArrayList<NinjaStar> ninjaStars = new ArrayList<NinjaStar>();
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	private Group root;
	
	public SpriteManager(Group root, Ninja myNinja, ArrayList<ArrayList<Alien>> aliensList){
		sprites = new ArrayList<Sprite>();
		this.aliensList = aliensList;
		this.root = root;
		this.myNinja = myNinja;
	}
	
	public void addSprite(Sprite sprite){
		sprites.add(sprite);
		if(sprite instanceof NinjaStar){
			ninjaStars.add((NinjaStar) sprite);
		}
		if(sprite instanceof FireBall){
			fireBalls.add((FireBall) sprite);
		}
		if(sprite instanceof AlienBoss){
			alienBoss = (AlienBoss) sprite;
		}
		if(sprite instanceof AlienRock){
			alienRocks.add((AlienRock) sprite);
		}
		if(sprite instanceof AlienOrb){
			alienOrbs.add((AlienOrb) sprite);
		}
		if(sprite instanceof Bullet){
			bullets.add((Bullet) sprite);
		}
	}
	
	public void updateSprites(int size){
		for(Sprite sprite : sprites){
			checkBounds(sprite, size);
			//Move automatically
			if(sprite instanceof Alien || sprite instanceof NinjaStar 
					|| sprite instanceof Bullet || sprite instanceof FireBall
					|| sprite instanceof AlienBoss || sprite instanceof AlienOrb
					|| sprite instanceof AlienRock){
				sprite.move();
			}
			}
	}
	
	public void checkBounds(Sprite sprite, int size){
		if(sprite.getX() + sprite.getWidth() >= size){
			if(sprite instanceof Alien){
				switchDirection(false);
			}
			if(sprite instanceof AlienBoss){
				sprite.setXVelocity(sprite.getXVelocity() * -1);
			}
		}
		if(sprite.getX() <= 10){
			if(sprite instanceof Alien){
				switchDirection(true);
			}
			if(sprite instanceof AlienBoss){
				sprite.setXVelocity(sprite.getXVelocity() * -1);
			}
		}
		if(sprite instanceof Ninja){
			if(myNinja.isShieldOn){
				myNinja.canMoveLeft = false;
				myNinja.canMoveRight = false;
			}
			else{
				if(myNinja.getX() + myNinja.getWidth() >= size){
				myNinja.canMoveRight = false;
			}
			else{
				myNinja.canMoveRight = true;
			}
			
			if(myNinja.getX() <= 0){
				myNinja.canMoveLeft = false;
			}
			else{
				myNinja.canMoveLeft = true;
			}
			}
		}
	}
	public void switchDirection(Boolean movingLeft){
		for(int i = 0; i < sprites.size(); i++){
			Sprite sprite = sprites.get(i);
			
			if(sprite instanceof Alien){
				//If moving left, move right
				if(movingLeft){
				if(sprite.getXVelocity() < 0){
				sprite.setXVelocity(sprite.getXVelocity() * -1);
			}
				}
				else{
				//If moving right, move left	
				if(sprite.getXVelocity() > 0){
					sprite.setXVelocity(sprite.getXVelocity() * -1);
				}
				}
			}
		}
	}
	public Boolean collidesWith(Sprite one, Sprite two){
		return one.getX() + one.getWidth() >= two.getX() && 
					one.getY() + one.getHeight() >= two.getY() && 
						two.getX() + two.getWidth() >= one.getX() &&
							two.getY() + two.getHeight() >= one.getY();
	}
	public void checkCollisions(){
		fireBallCollision();
		bulletCollision();
		ninjaStarCollision();
		alienBossCollisions();
	}

	public void alienBossCollisions() {
		if(alienBoss != null){
		for(int i = 0; i < alienRocks.size(); i++){
			AlienRock rock = alienRocks.get(i);
			if(collidesWith(rock, myNinja) && !rock.isDead()){
				takeDamage(rock);
			}
		}
		for(int i = 0; i < alienOrbs.size(); i++){
			AlienOrb orb = alienOrbs.get(i);
			if(collidesWith(orb, myNinja) && !orb.isDead()){
				takeDamage(orb);
			}
		}
		}
	}

	public void takeDamage(Sprite sprite) {
		double leftOverHealth = 0;
		if(myNinja.isShieldOn){
			Sprite shield = myNinja.getShield();
			leftOverHealth = shield.getHealth() - sprite.getDamage();
			shield.setHealth(leftOverHealth);
			
		}
		else{
			myNinja.setHealth(myNinja.getHealth() - sprite.getDamage());
		}
		if(leftOverHealth < 0){
			//Shield was not enough
			myNinja.setHealth(myNinja.getHealth() - leftOverHealth);
		}
		spritesToRemove.add(sprite);
		sprite.kill();
	}

	public void ninjaStarCollision() {
		//Check if a ninja star hit an alien
		for(int i = 0; i < ninjaStars.size(); i++){
			NinjaStar star = ninjaStars.get(i);
			if(alienBoss != null){
			if(collidesWith(star, alienBoss) && !star.isDead()){
				alienBoss.setHealth(alienBoss.getHealth() - star.getDamage());
				spritesToRemove.add(star);
			}
			}
			for(int j = 0; j < aliensList.size(); j++){
				for(int k = 0; k < aliensList.get(j).size(); k++){

					Alien alien = aliensList.get(j).get(k);
					if(alien.isDead() || star.isDead()){
						continue;
					}
					
					if(collidesWith(star, alien)){
						//2 hits to kill alien
						if(!alien.isDamaged()){
							//First hit
							root.getChildren().remove(alien.getImageView());
							alien.setDamaged();
							alien.changeImage();
							root.getChildren().add(alien.getImageView());
						}
						else{
							//Second hit, remove alien
							spritesToRemove.add(alien);
							for(ArrayList<Alien> alienColumn : aliensList){
							if(alienColumn.contains(alien)){
								alienColumn.remove(alien);
							}
							}
						}
						spritesToRemove.add(star);
					}
				}
			}
		}
	}

	public void bulletCollision() {
		//Check if a bullet hit the ninja
		for(int i = 0; i < bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			
			if( collidesWith(bullet, myNinja)){
				spritesToRemove.add(bullet);
				bullets.remove(bullet);
				
				takeDamage(bullet);
			}
		}
	}

	public void fireBallCollision() {
		for(int k = 0; k < fireBalls.size(); k++){
			FireBall fireBall = fireBalls.get(k);
		for(int i = 0; i < aliensList.size(); i++){
			for(int j = 0; j < aliensList.get(i).size(); j++){
				Alien alien = aliensList.get(i).get(j);
				
				if(collidesWith(fireBall, alien)){
				fireBall.kill();
				spritesToRemove.add(fireBall);
				killAliens();
				break;
			}
			}
			}
			if(alienBoss != null){
			if(collidesWith(fireBall, alienBoss)){
				spritesToRemove.add(fireBall);
				alienBoss.setHealth(alienBoss.getHealth() - fireBall.getDamage());
			}
			}
		
			
		}
	}

	private void killAliens() {
		for(int i = 0; i < aliensList.size(); i++){
			for(int j = 0; j < aliensList.get(i).size(); j++){
				Alien alien = aliensList.get(i).get(j);
				spritesToRemove.add(alien);
				root.getChildren().remove(alien.getImageView());
			}
		}
		aliensList.clear();
	}
	
	public void cleanUpSprites(){
		for(int i = 0; i < spritesToRemove.size(); i++){
			spritesToRemove.get(i).kill();
			root.getChildren().remove(spritesToRemove.get(i).getImageView());
			spritesToRemove.remove(i);
		}
		if(myNinja.getHealth() <= 0){
			myNinja.killNinja();
		}
	}
}
