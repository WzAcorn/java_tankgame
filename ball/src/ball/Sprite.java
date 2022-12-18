package ball;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

//소스를 입력하고 Ctrl+Shift+O를 눌러서 필요한 파일을 포함한다. 
public class Sprite {
	public int x; // 현재 위치의 x좌표
	public int y; // 현재 위치의 y좌표
	protected int dx; // 단위시간에 움직이는 x방향 거리
	protected int dy; // 단위시간에 움직이는 y방향 거리
	public Image image; // 스프라이트가 가지고 있는 이미지
	public int type;	//sprite 어레이리스트 안에서 원하는 스프라이트를 찾기위해 만든 변수.
	public int hp;		//스프라이트의 체력;

	// 생성자
	public Sprite(Image image, int x, int y) {
		this.image = image;
		this.x = x;
		this.y = y;
	}

	// 스프라이트의 가로 길이를 반환한다.
	public int getWidth() {
		return image.getWidth(null);
	}

	// 스프라이트의 세로 길이를 반환한다.
	public int getHeight() {
		return image.getHeight(null);
	}

	// 스프라이트를 화면에 그린다.
	public void draw(Graphics g) {
		g.drawImage(image, x, y, null);
	}

	// 스프라이트를 움직인다.
	public void move() {
		x += dx;
		y += dy;
	}
	
	//move 오버로딩
	public void move(boolean temp) {
		x += dx;
		y += dy;
	}
	//move 오버로딩
	public void move(Sprite other) {	//맵 바깥으로 나가는거 해결안됨. 수정해야될 부분.
		
	}
	// dx를 설정한다.
	public void setDx(int dx) {		this.dx = dx;	}

	// dy를 설정한다.
	public void setDy(int dy) {		this.dy = dy;	}

	// dx를 반환한다.
	public int getDx() {	return dx;	}

	// dy를 반환한다.
	public int getDy() {	return dy;	}

	// x를 설정한다.
	public void setX() {	this.x = x;	  }

	// y를 설정한다.s
	public void setY() {	this.y = y;	  }
	
	// x를 반환한다.
	public int getX() {		return x;	  }

	// y를 반환한다.
	public int getY() {		return y;	  }
	
	// 다른 스프라이트와의 충돌 여부를 계산한다. 충돌이면 true를 반환한다.
	public boolean checkCollision(Sprite other) {
		Rectangle myRect = new Rectangle();
		Rectangle otherRect = new Rectangle();
		myRect.setBounds(x, y, getWidth(), getHeight());
		otherRect.setBounds(other.getX(), other.getY(), other.getWidth(),
				other.getHeight());

		return myRect.intersects(otherRect);
	}

	// 충돌을 처리한다.
	public void handleCollision(Sprite other) {

	}

	//스프라이트의 이미지를 바꿔줄때 사용한다.(탱크의 상하좌우 움직일때)
	public void change_image(BufferedImage image) {
		this.image = image;
	}

	
}