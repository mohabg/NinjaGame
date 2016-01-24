import javafx.scene.image.Image;

public class Ninja extends Sprite{
	protected boolean canMoveLeft;
	protected boolean canMoveRight;
	protected boolean isMovingLeft;
	protected boolean isMovingRight;
	protected boolean isShieldOn = false;
	
	private Image shieldImage = new Image(getClass().getClassLoader().getResourceAsStream("ninja_shield.png"));
	private Image ninjaStarImage = new Image(getClass().getClassLoader().getResourceAsStream("ninja_star.png"));
	private Image deadNinjaImage = new Image(getClass().getClassLoader().getResourceAsStream("ninja_dead.png"));
	private Image fireBallImage = new Image(getClass().getClassLoader().getResourceAsStream("ninja_fireball.png"));
	
	private Sprite shield = new Sprite(this.getX(), this.getY(), 0, 0, shieldImage, 100, 0);
	//add animations
	
	public Ninja(double x, double y, double xVel, double yVel,Image image, double health, double damage){
		super(x, y, xVel, yVel, image, health, damage);
		canMoveLeft = true;
		canMoveRight = true;
	}
	public NinjaStar getStar(){
		//Throw ninja star with zero horizontal velocity
		NinjaStar ninjaStar = new NinjaStar(this.getX(), this.getY(), 0.0, -1.5, ninjaStarImage, 0, 2.0);
		return ninjaStar;
	}
	public FireBall getFireBall(){
		FireBall fireball = new FireBall(getX(), getY(), 0, -2, fireBallImage, 0, 1000);
		return fireball;
	}
	public Sprite getShield(){
		return shield;
	}
	public void killNinja(){
		this.setImage(deadNinjaImage);
		this.kill();
	}
}
