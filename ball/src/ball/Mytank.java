package ball;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

//�ҽ��� �Է��ϰ� Ctrl+Shift+O�� ������ �ʿ��� ������ �����Ѵ�. 

public class Mytank extends Sprite {
	private GalagaGame game;
	
	public Mytank(GalagaGame game, Image image, int x, int y) {
		super(image, x, y);		//SpriteŬ���� ���
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
	
	//��������Ʈ�� �̹����� �ٲ��ٶ� ����Ѵ�.(��ũ�� �����¿� �����϶�)
	public void change_image(BufferedImage image) {
		this.image = image;
	}
	
	//���ΰ���ũ�� ��ϰ��� �浹�� �ٷ�ϴ�.
	@Override
	public void handleCollision(Sprite other) {
		//��ϰ� �����̸� �������� �ʰ� ���ݴϴ�.
		if (other instanceof Block) {
			move(false);
		}
		
	}
}