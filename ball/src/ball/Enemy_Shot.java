package ball;

import java.awt.Image;

//소스를 입력하고 Ctrl+Shift+O를 눌러서 필요한 파일을 포함한다. 

public class Enemy_Shot extends Sprite {
	private GalagaGame game;

	public Enemy_Shot(GalagaGame game, Image image, int x, int y) {
		super(image, x, y);		//Sprite클래스 상속
		this.game = game;
		dy = 0;
		dx = 0;
		this.type = 2;		//sprite 어레이리스트 안에서 원하는 스프라이트를 찾기위해 만든 변수.
		hp = 1;				//sprite의 체력
	}

	@Override
	public void move() {
		super.move();
	}
	public void set_move(int a) {
		if (a == 1) {		//up
			dx = 0;
			dy = -3;
		}
		if (a == 2)	{		//down
			dx = 0;
			dy = +3;
		}
		if (a == 3) {		//left
			dx = -3;
			dy = 0;
		}
		if (a == 4)	{		//right
			dx = 3;
			dy = 0;
		}
	}
	
	//적 포탄과 블록과의 충돌, 적 포탄과 주인공 탱크의 충돌을 다룹니다.
	@Override
	public void handleCollision(Sprite other) {
		if (other instanceof Block) {	//주인공 총알이랑 블록이랑 부딛혔을때
			this.hp = this.hp-1;		//서로의 체력을 깎습니다.
			other.hp = other.hp-1;
			if(this.hp == 0)
				game.removeSprite(this);	//체력은 0이되면 사라지게 됩니다.
			if(other.hp == 0)
				game.removeSprite(other);	//블록의 체력은 0이되면 사라지게 됩니다.
		}
		if (other instanceof Mytank) {	//적 총알이랑 주인공 탱크랑 부딛혔을때
			this.hp = this.hp-1;			//서로의 체력을 깎습니다.
			other.hp = other.hp-1;
			if(this.hp == 0)
				game.removeSprite(this);	//체력은 0이되면 사라지게 됩니다.
			if(other.hp == 0)
				game.removeSprite(other);	//주인공 탱크의 체력이 0이되면 사라지게 됩니다.
		}
	}
}