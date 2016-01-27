import java.util.*;
import javafx.scene.Group;
//This entire file is my masterpiece
//Mohab Gabal
/**
 * This class is responsible for the most critical functionality in the game. It keeps track of all the sprites, updates them, manages collisions, and 
 * removes the dead objects. It does things such as moving all of the sprites around, making sure they don't move off the screen, and detects collisions and deals 
 * damage, accordingly. It does all of this efficiently and without duplicating any code. It is created and used in the Game class, from which it is passed three
 * key parameters that are used throughout. All of the variable and method names are descriptive, the code is easy to read, and the methods are concise.    
 * @author mohabgabal
 *
 */
public class SpriteManagerMasterPiece {
	private ArrayList<ArrayList<Alien>> aliensList = new ArrayList<ArrayList<Alien>>();
	private ArrayList<Sprite> sprites = new ArrayList<Sprite>(); 
	private ArrayList<Sprite> spritesToRemove = new ArrayList<Sprite>();
	
	private Ninja myNinja;
	private AlienBoss alienBoss;
	
	private ArrayList<FireBall> fireBalls = new ArrayList<FireBall>();
	private ArrayList<NinjaStar> ninjaStars = new ArrayList<NinjaStar>();
	
	private Group root;
	
	public SpriteManagerMasterPiece(Group root, Ninja myNinja, ArrayList<ArrayList<Alien>> aliensList){
		
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
	}
	
	public void updateSprites(int size){
		for(Sprite sprite : sprites){
			checkBounds(sprite, size);
			//Move automatically
			if(!(sprite instanceof Ninja)){
				sprite.move();
			}
			}
	}
	
	public void checkBounds(Sprite sprite, int size){
		if(sprite.getX() + sprite.getWidth() >= size){
			if(sprite instanceof Alien){
				switchAlienDirection(false);
			}
			if(sprite instanceof AlienBoss){
				sprite.setXVelocity(sprite.getXVelocity() * -1);
			}
		}
		if(sprite.getX() <= 10){
			if(sprite instanceof Alien){
				switchAlienDirection(true);
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
	public void switchAlienDirection(Boolean movingLeft){
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
		for(int i = 0; i < sprites.size(); i++){
			Sprite sprite = sprites.get(i);
			if(sprite instanceof NinjaStar ||
					sprite instanceof FireBall){
				ninjaWeaponCollision(sprite);
			}
			else{
				if(sprite instanceof Bullet ||
						sprite instanceof AlienRock ||
							sprite instanceof AlienOrb){
					if(collidesWith(sprite, myNinja) && !sprite.isDead()){
					ninjaTakeDamage(sprite);
				}
					}
			}
		}
	}
	public void ninjaTakeDamage(Sprite sprite) {
		spritesToRemove.add(sprite);
		sprite.kill();
		
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

	public void ninjaWeaponCollision(Sprite sprite) {
		
			if(alienBoss != null){
			if(collidesWith(sprite, alienBoss) && !sprite.isDead()){
				alienBoss.setHealth(alienBoss.getHealth() - sprite.getDamage());
				spritesToRemove.add(sprite);
				sprite.kill();
			}
			}
			
			for(int j = 0; j < aliensList.size(); j++){
				for(int k = 0; k < aliensList.get(j).size(); k++){

					Alien alien = aliensList.get(j).get(k);
					if(collidesWith(sprite, alien) && !sprite.isDead()){
						if(sprite instanceof NinjaStar){
							
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
						}
						if(sprite instanceof FireBall){
							killAliens();
						}
						spritesToRemove.add(sprite);
						sprite.kill();
					}
				}
			}
		}
	
	private void killAliens() {
		for(int i = 0; i < aliensList.size(); i++){
			for(int j = 0; j < aliensList.get(i).size(); j++){
				Alien alien = aliensList.get(i).get(j);
				spritesToRemove.add(alien);
			}
		}
		aliensList.clear();
	}
	
	public void cleanUpSprites(){
		for(int i = 0; i < spritesToRemove.size(); i++){
			root.getChildren().remove(spritesToRemove.get(i).getImageView());
			spritesToRemove.remove(i);
		}
		if(myNinja.getHealth() <= 0){
			myNinja.killNinja();
		}
	}
}
