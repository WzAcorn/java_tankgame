package ball;

import java.awt.Image;

//소스를 입력하고 Ctrl+Shift+O를 눌러서 필요한 파일을 포함한다. 

public class Block extends Sprite {
	private GalagaGame game;

	//충돌은 총알과 탱크에서 하기때문에 별다른 함수를 추가하지 않음.
	public Block(GalagaGame game, Image image, int x, int y) {
		super(image, x, y);		//Sprite클래스 상속
		this.game = game;
		this.type = 3;
		hp = 1;
	}
	
}
	
