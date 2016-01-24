import java.util.*;

import javafx.scene.image.*;

public class Sprite{
	
	private double xPosition;
	private double yPosition;
	private double xVelocity;
	private double yVelocity;
	
	private double width;
	private double height;

	private double health;
	private double damage;
	
	private Image image;
	private ImageView imageView;
	
	private Boolean isDead = false;
	
	public Sprite(double x, double y, double xVel, double yVel,Image image, double health, double damage){
		this.xPosition = x;
		this.yPosition = y;
		this.xVelocity = xVel;
		this.yVelocity = yVel;
		this.health = health;
		this.damage = damage;
		this.image = image;
		imageView = new ImageView();
		imageView.setImage(image);
		this.imageView.relocate(xPosition, yPosition);
		this.width = image.getWidth();
		this.height = image.getHeight();
	}
	
	public void move(){
		this.xPosition += this.xVelocity;
		this.yPosition += this.yVelocity;
		this.imageView.relocate(this.xPosition, this.yPosition);
	}
	public void resize(double width, double height){
		this.width = width;
		this.height = height;
		this.imageView.setFitHeight(height);
		this.imageView.setFitWidth(width);
		this.imageView.preserveRatioProperty();
	}
	public Boolean isDead(){
		return isDead;
	}
	public void kill(){
		this.isDead = true;
	}
	public double getWidth(){
		return this.width;
	}
	public double getHeight(){
		return this.height;
	}
	public void setX(double x){
		this.xPosition = x;
	}
	public double getX(){
		return xPosition;
	}
	public void setY(double y){
		this.yPosition = y;
	}
	public double getY(){
		return yPosition;
	}
	public void setXVelocity(double xVel){
		this.xVelocity = xVel;
	}
	public void setYVelocity(double yVel){
		this.yVelocity = yVel;
	}
	public double getXVelocity(){
		return this.xVelocity;
	}
	public double getYVelocity(){
		return this.yVelocity;
	}
	public double getHealth(){
		return this.health;
	}
	public void setHealth(Double health){
		this.health = health;
	}
	public double getDamage(){
		return damage;
	}
	public void setDamage(Double damage){
		this.damage = damage;
	}
	public ImageView getImageView(){
		return this.imageView;
	}
	public Image getImage(){
		return this.image;
	}
	public void setImage(Image image){
		this.image = image;
		this.imageView.setImage(this.image);
		this.width = image.getWidth();
		this.height = image.getHeight();
	}
}
