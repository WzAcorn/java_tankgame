package ball;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

//소스를 입력하고 Ctrl+Shift+O를 눌러서 필요한 파일을 포함한다. 

public class Enemy_tank extends Sprite {
	private GalagaGame game;
	
	public Enemy_tank(GalagaGame game, Image image, int x, int y) {
		super(image, x, y);		//Sprite클래스 상속
		this.game = game;
		dx = 0;
		dy = 0;
		this.type = 1;
		hp = 3;
	}

	@Override
	public void move(boolean temp) {	//맵 바깥으로 나가는거 해결안됨. 수정해야될 부분.
		if (temp == true) {
			super.move();
		}
	}
	
	//스프라이트의 이미지를 바꿔줄때 사용한다.(탱크의 상하좌우 움직일때)
	public void change_image(BufferedImage image) {
		this.image = image;
	}
	@Override
	public void handleCollision(Sprite other) {
		if (other instanceof Block) {
			move(false);
		}

	}
}