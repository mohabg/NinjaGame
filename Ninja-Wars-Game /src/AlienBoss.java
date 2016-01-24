import javafx.scene.image.Image;

public class AlienBoss extends Sprite{
	private Image alienOrbImage = new Image(getClass().getClassLoader().getResourceAsStream("alien_orb.png"));
	private Image alienRockImage = new Image(getClass().getClassLoader().getResourceAsStream("alien_rock.png"));
	
	public AlienBoss(double x, double y, double xVel, double yVel, Image image, double health, double damage) {
		super(x, y, xVel, yVel, image, health, damage);
	}
	public AlienOrb getAlienOrb(){
		AlienOrb alienOrb = new AlienOrb(this.getX(), this.getY() + this.getHeight(), 0, 1, alienOrbImage, 0, 100);
		alienOrb.resize(65, 65);
		return alienOrb;
	}
	public AlienRock getAlienRock(){
		AlienRock alienRock = new AlienRock(this.getX(), this.getY() + this.getHeight(), 0, 1.7, alienRockImage, 0, 25);
		alienRock.resize(25, 25);
		return alienRock;
	}
}
