package ball;

import java.awt.Image;

//�ҽ��� �Է��ϰ� Ctrl+Shift+O�� ������ �ʿ��� ������ �����Ѵ�. 

public class Block extends Sprite {
	private GalagaGame game;

	//�浹�� �Ѿ˰� ��ũ���� �ϱ⶧���� ���ٸ� �Լ��� �߰����� ����.
	public Block(GalagaGame game, Image image, int x, int y) {
		super(image, x, y);		//SpriteŬ���� ���
		this.game = game;
		this.type = 3;
		hp = 1;
	}
	
}
	
