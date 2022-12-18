package ball;

import java.awt.Image;

//�ҽ��� �Է��ϰ� Ctrl+Shift+O�� ������ �ʿ��� ������ �����Ѵ�. 

public class ShotSprite extends Sprite {
	private GalagaGame game;

	public ShotSprite(GalagaGame game, Image image, int x, int y) {
		super(image, x, y);		//SpriteŬ���� ���
		this.game = game;
		dy = 0;
		dx = 0;
		this.type = 2;
		hp = 1;
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
	
	//���ΰ� ��ź�� ��ϰ��� �浹, ���ΰ� ��ź�� �� ��ũ�� �浹�� �ٷ�ϴ�.
	@Override
	public void handleCollision(Sprite other) {
		if (other instanceof Block) {	//���ΰ� �Ѿ��̶� ����̶� �ε�������
			this.hp = this.hp-1;		//������ ü���� ����ϴ�.
			other.hp = other.hp-1;
			if(this.hp == 0)
				game.removeSprite(this);	//�Ѿ��� ü���� 0�̵Ǹ� ������� �˴ϴ�.
			if(other.hp == 0)
				game.removeSprite(other);	//����� ü���� 0�̵Ǹ� ������� �˴ϴ�.
		}
		if (other instanceof Enemy_tank) {	//���ΰ� �Ѿ��̶� �� ��ũ�� �ε�������
			this.hp = this.hp-1;			//���� ������ ü���� ����ϴ�.
			other.hp = other.hp-1;
			if(this.hp == 0)
				game.removeSprite(this);	//�Ѿ�ü���� 0�̵Ǹ� ������� �˴ϴ�.
			if(other.hp == 0)
				game.removeSprite(other);	//����ũ�� ü���� 0�̵Ǹ� ������� �˴ϴ�.
		}
	}
}
