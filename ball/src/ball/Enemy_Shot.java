package ball;

import java.awt.Image;

//�ҽ��� �Է��ϰ� Ctrl+Shift+O�� ������ �ʿ��� ������ �����Ѵ�. 

public class Enemy_Shot extends Sprite {
	private GalagaGame game;

	public Enemy_Shot(GalagaGame game, Image image, int x, int y) {
		super(image, x, y);		//SpriteŬ���� ���
		this.game = game;
		dy = 0;
		dx = 0;
		this.type = 2;		//sprite ��̸���Ʈ �ȿ��� ���ϴ� ��������Ʈ�� ã������ ���� ����.
		hp = 1;				//sprite�� ü��
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
	
	//�� ��ź�� ��ϰ��� �浹, �� ��ź�� ���ΰ� ��ũ�� �浹�� �ٷ�ϴ�.
	@Override
	public void handleCollision(Sprite other) {
		if (other instanceof Block) {	//���ΰ� �Ѿ��̶� ����̶� �ε�������
			this.hp = this.hp-1;		//������ ü���� ����ϴ�.
			other.hp = other.hp-1;
			if(this.hp == 0)
				game.removeSprite(this);	//ü���� 0�̵Ǹ� ������� �˴ϴ�.
			if(other.hp == 0)
				game.removeSprite(other);	//����� ü���� 0�̵Ǹ� ������� �˴ϴ�.
		}
		if (other instanceof Mytank) {	//�� �Ѿ��̶� ���ΰ� ��ũ�� �ε�������
			this.hp = this.hp-1;			//������ ü���� ����ϴ�.
			other.hp = other.hp-1;
			if(this.hp == 0)
				game.removeSprite(this);	//ü���� 0�̵Ǹ� ������� �˴ϴ�.
			if(other.hp == 0)
				game.removeSprite(other);	//���ΰ� ��ũ�� ü���� 0�̵Ǹ� ������� �˴ϴ�.
		}
	}
}