package ball;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

//�ҽ��� �Է��ϰ� Ctrl+Shift+O�� ������ �ʿ��� ������ �����Ѵ�. 
public class Sprite {
	public int x; // ���� ��ġ�� x��ǥ
	public int y; // ���� ��ġ�� y��ǥ
	protected int dx; // �����ð��� �����̴� x���� �Ÿ�
	protected int dy; // �����ð��� �����̴� y���� �Ÿ�
	public Image image; // ��������Ʈ�� ������ �ִ� �̹���
	public int type;	//sprite ��̸���Ʈ �ȿ��� ���ϴ� ��������Ʈ�� ã������ ���� ����.
	public int hp;		//��������Ʈ�� ü��;

	// ������
	public Sprite(Image image, int x, int y) {
		this.image = image;
		this.x = x;
		this.y = y;
	}

	// ��������Ʈ�� ���� ���̸� ��ȯ�Ѵ�.
	public int getWidth() {
		return image.getWidth(null);
	}

	// ��������Ʈ�� ���� ���̸� ��ȯ�Ѵ�.
	public int getHeight() {
		return image.getHeight(null);
	}

	// ��������Ʈ�� ȭ�鿡 �׸���.
	public void draw(Graphics g) {
		g.drawImage(image, x, y, null);
	}

	// ��������Ʈ�� �����δ�.
	public void move() {
		x += dx;
		y += dy;
	}
	
	//move �����ε�
	public void move(boolean temp) {
		x += dx;
		y += dy;
	}
	//move �����ε�
	public void move(Sprite other) {	//�� �ٱ����� �����°� �ذ�ȵ�. �����ؾߵ� �κ�.
		
	}
	// dx�� �����Ѵ�.
	public void setDx(int dx) {		this.dx = dx;	}

	// dy�� �����Ѵ�.
	public void setDy(int dy) {		this.dy = dy;	}

	// dx�� ��ȯ�Ѵ�.
	public int getDx() {	return dx;	}

	// dy�� ��ȯ�Ѵ�.
	public int getDy() {	return dy;	}

	// x�� �����Ѵ�.
	public void setX() {	this.x = x;	  }

	// y�� �����Ѵ�.s
	public void setY() {	this.y = y;	  }
	
	// x�� ��ȯ�Ѵ�.
	public int getX() {		return x;	  }

	// y�� ��ȯ�Ѵ�.
	public int getY() {		return y;	  }
	
	// �ٸ� ��������Ʈ���� �浹 ���θ� ����Ѵ�. �浹�̸� true�� ��ȯ�Ѵ�.
	public boolean checkCollision(Sprite other) {
		Rectangle myRect = new Rectangle();
		Rectangle otherRect = new Rectangle();
		myRect.setBounds(x, y, getWidth(), getHeight());
		otherRect.setBounds(other.getX(), other.getY(), other.getWidth(),
				other.getHeight());

		return myRect.intersects(otherRect);
	}

	// �浹�� ó���Ѵ�.
	public void handleCollision(Sprite other) {

	}

	//��������Ʈ�� �̹����� �ٲ��ٶ� ����Ѵ�.(��ũ�� �����¿� �����϶�)
	public void change_image(BufferedImage image) {
		this.image = image;
	}

	
}