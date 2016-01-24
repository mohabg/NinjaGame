import java.util.BitSet;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.*;

public class Input {

	private Scene scene;
	
	private BitSet keyboardBitSet = new BitSet();
	
	private KeyCode upKey = KeyCode.UP;
	private KeyCode downKey = KeyCode.DOWN;
	private KeyCode leftKey = KeyCode.LEFT;
	private KeyCode rightKey = KeyCode.RIGHT;
	private KeyCode spaceBar = KeyCode.SPACE;
	private KeyCode shiftKey = KeyCode.SHIFT;
	
	private EventHandler<KeyEvent> keyPressedEventHandler = new EventHandler<KeyEvent>(){
		@Override
		public void handle(KeyEvent event){
			keyboardBitSet.set(event.getCode().ordinal(), true);
		}
	};
	
	private EventHandler<KeyEvent> keyReleasedEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            keyboardBitSet.set(event.getCode().ordinal(), false);          
        }
    };
    
    public Input(Scene scene){
    	this.scene = scene;
    }
    public void addHandlers(){
    	scene.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
    	scene.addEventFilter(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);
    }
    public void removeHandlers(){
    	scene.removeEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
    	scene.removeEventFilter(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);
    }
    public boolean upPressed() {
        return keyboardBitSet.get( upKey.ordinal());
    }

    public boolean downPressed() {
        return keyboardBitSet.get( downKey.ordinal());
    }

    public boolean leftPressed() {
        return keyboardBitSet.get( leftKey.ordinal());  
    }

    public boolean rightPressed() {
        return keyboardBitSet.get( rightKey.ordinal());
    }
    public boolean spacePressed(){
    	return keyboardBitSet.get(spaceBar.ordinal());
    }
    public boolean shiftPressed(){
    	return keyboardBitSet.get(shiftKey.ordinal());
    }
}
