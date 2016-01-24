import javafx.scene.image.Image;

public class Alien extends Sprite{
	private Image bulletImage = new Image(getClass().getClassLoader().getResourceAsStream("alien_bullet.png"));
	private Image damagedAlien = new Image(getClass().getClassLoader().getResourceAsStream("alien_damaged.png")); 
	
	private boolean damaged = false;
	
	public Alien(double x, double y, double xVel, double yVel,Image image, double health, double damage){
		super(x, y, xVel, yVel, image, health, damage);
}
	public Bullet getBullet(){
		Bullet bullet = new Bullet(this.getX(), this.getY() + this.getHeight(), 0.0, 1.25, bulletImage, 0, 20);
		return bullet;
	}
	public void changeImage(){
		this.setImage(damagedAlien);
	}
	public void setDamaged(){
		damaged = true;
	}
	public Boolean isDamaged(){
		return damaged;
	}
}
