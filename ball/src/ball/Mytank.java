package ball;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

//소스를 입력하고 Ctrl+Shift+O를 눌러서 필요한 파일을 포함한다. 

public class Mytank extends Sprite {
	private GalagaGame game;
	
	public Mytank(GalagaGame game, Image image, int x, int y) {
		super(image, x, y);		//Sprite클래스 상속
		this.game = game;
		dx = 0;
		dy = 0;
		this.type = 1;
		hp = 3;
	}

	@Override
	public void move(boolean temp) {	
		if (temp == true) {
			super.move();
		}
	}
	
	//스프라이트의 이미지를 바꿔줄때 사용한다.(탱크의 상하좌우 움직일때)
	public void change_image(BufferedImage image) {
		this.image = image;
	}
	
	//주인공탱크와 블록과의 충돌을 다룹니다.
	@Override
	public void handleCollision(Sprite other) {
		//블록과 움직이면 움직이지 않게 해줍니다.
		if (other instanceof Block) {
			move(false);
		}
		
	}
}