package ball;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

//�ҽ��� �Է��ϰ� Ctrl+Shift+O�� ������ �ʿ��� ������ �����Ѵ�. 

public class GalagaGame extends JPanel implements KeyListener  {
	long start_time = 0;
	private boolean running = true;					//gameloop�� ���� ���� ���� ����ϴ� boolean����
	private boolean fire_size = true;				//��ź �߻縦(spaceŰ) Ű�ٿ��Ҷ� ���Ǵ� boolean����.
	private boolean Mytank_CanMove = true;			//�� ��ũ�� ����̶� �ε������� �˷��ִ� boolean����
	private boolean EnemyMytank_CanMove = true;		//�� ��ũ�� ����̶� �ε������� �˷��ִ� boolean����
	private boolean see_up = false;					//�� ��ũ�� ��,��,��,���� ��� �����ִ��� �˷��ִ� ����.
	private boolean see_down = false;
	private boolean see_left = false;
	private boolean see_right = false;

	int tank_count = 0;				//�� ��ũ�� �ʹ� ���� ������ �ٲ��� �ʰ� �����ϱ� ���� ����
	int tank_fire_dic = 1;			//�� ��ũ�� �ٶ󺸴� ������ �˷��ִ� ����.
	private ArrayList sprites = new ArrayList();		//��������Ʈ���� ��Ƶδ� ���Ŭ����.
	private Sprite Mytank;			//���ΰ� ��ũ
	private Sprite Enemytank;		//���� ��ũ
	private Image img;				//�����ӿ� ����Ǵ� ����ȭ���� �׷��ִ� image ��ü
	private Graphics img_g;			//�۾��� ȭ�鿡 ������۸����� �׷���.
	
	
	//���ӿ� �ʿ��� �̹�����.
	private BufferedImage alienImage;
	private BufferedImage shot_smallImage;
	private BufferedImage shot_middleImage;
	private BufferedImage shot_bigImage;
	private BufferedImage Mytank_upImage;
	private BufferedImage Mytank_downImage;
	private BufferedImage Mytank_leftImage;
	private BufferedImage Mytank_rightImage;
	private BufferedImage enemytank_upImage;
	private BufferedImage enemytank_downImage;
	private BufferedImage enemytank_leftImage;
	private BufferedImage enemytank_rightImage;
	private BufferedImage breakbrick_image;
	private BufferedImage solidbrick_image;
	private BufferedImage ghostbrick_image;
	
	
	public GalagaGame() {
		JFrame frame = new JFrame("TANK 2D Game");		
		frame.setSize(1100, 800);						
		frame.add(this);								
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//�̹������� ���� �ڹٿ� �ҷ�����.
		try {
			shot_smallImage = ImageIO.read(new File("fireball_small.png"));
			shot_middleImage = ImageIO.read(new File("fireball_middle.png"));
			shot_bigImage = ImageIO.read(new File("fireball_big.png"));
			Mytank_upImage = ImageIO.read(new File("player1_tank_up.png"));
			Mytank_downImage = ImageIO.read(new File("player1_tank_down.png"));
			Mytank_leftImage = ImageIO.read(new File("player1_tank_left.png"));
			Mytank_rightImage = ImageIO.read(new File("player1_tank_right.png"));
			enemytank_upImage = ImageIO.read(new File("enemy_tank_up.png"));
			enemytank_downImage = ImageIO.read(new File("enemy_tank_down.png"));
			enemytank_leftImage = ImageIO.read(new File("enemy_tank_left.png"));
			enemytank_rightImage = ImageIO.read(new File("enemy_tank_right.png"));
			breakbrick_image = ImageIO.read(new File("break_brick.png"));
			solidbrick_image = ImageIO.read(new File("solid_brick.png"));
			ghostbrick_image = ImageIO.read(new File("ghost_brick.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		this.requestFocus();
		this.startGame();			//���� ����
		addKeyListener(this);

	}
	
	//��ϵ��� ������ ��ġ�����ִ� �Լ�. ��ϵ��� �� ���ึ�� �������� ��ġ�ȴ�.
	private void bulid_map() {
		int [][] bricks_position = new int [21][15];	// ���� 50x22ĭ, ���� 50x16ĭ�� ����� �������� �迭.
		double randomValue = 0;
		//������ 30%�� �ƹ�������� ä���.
		//�� �� ����� �ѹ��� ������ ��������, ������ ������ �ø�Ʈ������ �ִ�.
		for(int a=0; a<18; a++) {
			for(int b=0; b<15; b++) {
				randomValue = Math.random();
				if(randomValue <= 0.3) 
					bricks_position[a][b] = 1;
			}
		}
		
		//������ ������ ���ϴ� ��,�� ���� ����
		for(int a=-1; a<18; a++) {
			Block non_brick = new Block(this, ghostbrick_image, a*50, -50);
			Block non_brick2 = new Block(this, ghostbrick_image, a*50, 750);	
			non_brick.hp = 99999;
			non_brick2.hp = 99999;
			sprites.add(non_brick);
			sprites.add(non_brick2);
		}
		//������ ������ ���ϴ� ��, �� ���� ����
		for(int a=-1; a<16; a++) {
			Block non_brick = new Block(this, ghostbrick_image, -50, a*50);
			Block non_brick2 = new Block(this, ghostbrick_image, 900, a*50);	
			non_brick.hp = 99999;
			non_brick2.hp = 99999;
			sprites.add(non_brick);
			sprites.add(non_brick2);
		}
		
		//��������, �ø�Ʈ������ ��������.
		System.out.print(Mytank.getX());
		for(int a=0; a<18; a++) {
			for(int b=1; b<15; b++) {
				if(bricks_position[a][b] == 1) {
					//�Ʒ��� if���� ���ΰ� ��ũ�� �ִ°����� ������ �������� �ʰ��� ������ ��ũ�� ������ �ʰ�����.
					if(!(a*50 <= Mytank.getX() && Mytank.getX() <= (a+1)*50 &&
							b*50 <= Mytank.getY() && Mytank.getY() <= (b+1)*50)) {
						randomValue = Math.random();
						if(randomValue <= 0.7) {	//���������� ��ü�� 70%
							Block breakbrick = new Block(this, breakbrick_image, a*50, b*50);
							sprites.add(breakbrick);
							}
						else {						//�ø�Ʈ������ ��ü�� 30%
							Block breakbrick = new Block(this, solidbrick_image, a*50, b*50);
							breakbrick.hp = 3;
							sprites.add(breakbrick);
						}
					}
				}
			}
		}
	}
	
	//���ӿ� ���� ��ä���� ������ִ� �Լ�
	private void initSprites() {
		Mytank = new Mytank(this, Mytank_upImage, 405, 505);
		Enemytank =  new Enemy_tank(this, enemytank_upImage, 100, 0);
		sprites.add(Mytank);
		sprites.add(Enemytank);
		bulid_map();
	}
	
	//������ ���۽�Ű�� �Լ�.
	private void startGame() {
		sprites.clear();
		initSprites();
	}
	
	//���� ��ũ�� �����̰� �ϴ� �Լ�.
	//���ΰ� ��ũ�� ��ġ�� ������� ���ΰ� ��ũ������ �����Դϴ�.
	private void EnemyMove() {
		int my_x, my_y, enemy_x, enemy_y;
		my_x = Mytank.getX();
		my_y = Mytank.getX();
		enemy_x = Enemytank.getX();
		enemy_y = Enemytank.getY();
		
		double randomValue;
		randomValue = Math.random();		//���� ����.
		//�� ����� �¿��̵�, �����̵�, ���� ���� �ϳ��� �����մϴ�.
		//�¿��̵��� �����Ҷ�
		if(randomValue <= 0.33) {
			//���ΰ� ��ũ�� ���� ���� ������������ �������� ���󰩴ϴ�.
			if(my_x <= enemy_x) {		
				Enemytank.setDx(-1);
				Enemytank.setDy(0);
				Enemytank.change_image(enemytank_leftImage);
				tank_fire_dic = 3;		//������ ����ϴ� ����.
				}
			else{
				Enemytank.setDx(1);
				Enemytank.setDy(0);
				Enemytank.change_image(enemytank_rightImage);
				tank_fire_dic = 4;		//������ ����ϴ� ����.
				}
		}
		//�����̵��� �����Ҷ�
		else if(randomValue <= 0.66){
			if(my_y <= enemy_y) {
				Enemytank.setDx(0);
				Enemytank.setDy(-1);
				Enemytank.change_image(enemytank_downImage);
				tank_fire_dic = 1;		//���� ����ϴ� ����.
				}
			else{
				Enemytank.setDx(0);
				Enemytank.setDy(1);
				Enemytank.change_image(enemytank_upImage);
				tank_fire_dic = 2;		//�Ʒ��� ����ϴ� ����.
			}
		}
		// �����ֱ⸦ �����Ҷ�
		else {
			Enemytank.setDx(0);			
			Enemytank.setDy(0);
		}
		
	}
	
	//����ũ�� ������ ����ϴ� �Լ�.
	private void EnemyFire() {
		//�μ��� ���� ���ũ��  , ��� ������� , ��� �������� �� ���մϴ�.
		fire(Enemytank, 1,tank_fire_dic);
	}

	//��������Ʈ �����Լ�.
	public void removeSprite(Sprite sprite) {
		sprites.remove(sprite);
	}

	
	//����Ʈ ������Ʈ
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		img = createImage(1100, 800);							//��� �׸����� �׷��� 800x600�������� ��ȭ���� �ϳ� ����.
		img_g = img.getGraphics();
		img_g.setColor(Color.black);	
		img_g.fillRect(0, 0, 900, 800);							//900,800ũ���� ������ run�ϴ� ���� ���� ����.
		img_g.setColor(Color.gray);
		img_g.fillRect(900, 0, 200, 800);						//200,800ũ���� ĳ������ �������̽��� ��Ÿ���� ȸ������ ����.
		img_g.setFont(new Font("���ü", Font.BOLD, 25));			//�ܰ�� ���� ��Ʈ�� ����.
		img_g.setColor(Color.WHITE);							//������ ȭ��Ʈ
		img_g.drawString("Me: "+Integer.toString(Mytank.hp), 930, 20);				//���ΰ� ü���� ǥ��.
		img_g.drawString("Enemy: "+Integer.toString(Enemytank.hp), 930, 50);		//�� ��ũ ü���� ǥ��.
		
		for (int i = 0; i < sprites.size(); i++) {				//��̸���Ʈ ���� ��������Ʈ���� ���� �׷��ݴϴ�.
			Sprite sprite = (Sprite) sprites.get(i);
			sprite.draw(img_g);			
		}
		if(running == false) {									//������ �������϶�(������ �׾�����)
			img_g.setFont(new Font("���ü", Font.BOLD, 85));		//��Ʈ�� ����.
			if(Mytank.hp == 0) {								//�� ü���� 0�̸� ������ �¸�!
				img_g.drawString("Enemy Win!", 220, 160);
			}
			if(Enemytank.hp == 0) {								//�� ü���� 0�̸� ���� �¸�!
				img_g.drawString("You Win!", 270, 160);
			}
			img_g.drawString("Press F5 To Restart", 80, 540);	//f5 Ű�� ���� ������ ����� �Ҽ� �ְ� ��.
		}
		
		
		g.drawImage(img, 0, 0, null);							//��ä���� ������۸����� �׸�.
	}

	
	//������ �������� ����Ǵ� �Լ�.
	public void gameLoop() {
		double randomValue = 0;
		while (true){	
			if(running == true) {	//running���°� true�϶��� ������ ����.
				
				//���� ��ũ�� ���� �������� 15������ �ݺ��ϰ�(0.15��) ���� �������� �����ϰ� �˴ϴ�.
				if (tank_count == 0) {randomValue = Math.random();}
				if(randomValue <= 0.3) {EnemyMove();}
				if(randomValue <= 0.1) {EnemyFire();}
				tank_count++;
				tank_count = tank_count % 15;
				
				Mytank_CanMove = true;
				EnemyMytank_CanMove = true;
				
				//��������Ʈ���� �浹�� �ٷ��.
				for (int p = 0; p < sprites.size(); p++) {
					for (int s = p + 1; s < sprites.size(); s++) {
						Sprite me = (Sprite) sprites.get(p);
						Sprite other = (Sprite) sprites.get(s);
						if (me.checkCollision(other)) {
							me.handleCollision(other);
							other.handleCollision(me);
						}
						
						//me�� other�� ��ũ�� ����϶� �ε��� �Ÿ���ŭ ������ �������� �̵��ϰԵ�.
						//������ ������Ѻ��� �ε����� ���� �и��� �ݺ��Ͽ� ��ġ �����ϴ°�ó�� ���̰� �˴ϴ�.
						if (me.checkCollision(other) && (me.type==1) && (other.type==3)) {
							Mytank_CanMove = false;							//���ΰ� ��ũ�� ������ ���¸� false�� ����.
							EnemyMytank_CanMove = false;					//�� ��ũ�� ������ ���¸� false�� ����.
							int temp_x, temp_y;
							temp_x = (me.getX()+21 - other.getX()+25);		//��ä�� �߽ɰŸ� x�� ����.
							temp_y = (me.getY()+17 - other.getY()+25);		//��ä�� �߽ɰŸ� y�� ����.
							if( 0 <= temp_x && temp_x<= 16 && 0 <= temp_y && temp_y <= 84) 			//������ ���ʿ��� �ε�������
								me.x = me.getX()-3;													//�������� 3��ŭ �̵�
							else if( 13 <= temp_x && temp_x<= 95 && 0 <= temp_y && temp_y <= 12) 	//������ ���ʿ��� �ε�������
								me.y = me.getY()-3;													//�������� 3��ŭ �̵�
							else if( 13 <= temp_x && temp_x<= 95 &&  85 <= temp_y && temp_y < 94) 	//������ �Ʒ��ʿ��� �ε�������
								me.y = me.getY()+3;													//�Ʒ������� 3��ŭ �̵�
							else if( 80 <= temp_x && temp_x<= 95 && 0 <= temp_y && temp_y <= 85) 	//������ �����ʿ��� �ε�������
								me.x = me.getX()+3;													//���������� 3��ŭ �̵�	
						}

					}
				}
				
				//sprite���� ���� �˸°� move()��ŵ�ϴ�.
				for (int i = 0; i < sprites.size(); i++) {
					Sprite sprite = (Sprite) sprites.get(i);
					if(!(sprite.type == 1 && Mytank_CanMove == false)) {
						sprite.move();
					}
				}
				//���̳� �Ʊ���ũ ü���� ���� �ϳ��� 0�϶� ������ ����.
				if(Enemytank.hp == 0 || Mytank.hp == 0) {
					running = false;
				}
				
				
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					}
			}
			repaint();
		}
	}

	//space�ٸ� ���� �������� ���ΰ��� �Ұ����� Ŀ����, �����ϴ�.
	//�󸶳� ���� �������� üũ�ϴ� �Լ�.
	public void keydown_fire() {		//spaceŰ�� ������������ �� ū �Ұ��� ���󰡰� �ϴ� �Լ�.
		if(fire_size == true) {		//�ѹ� keypress�Է��� ������ keyrelease�� �ֱ� ������ �Լ��� ������������.
			fire_size = false;
			start_time = System.currentTimeMillis(); //������ �ð��� ms������ ����
		}
	}
	
	//�Ҳ��� �����¿� ��� ������ �����ִ� �Լ�.
	public void set_direction(int a) {
		if (a == 1) {//����
			see_up = true;
			see_down = false;
			see_left = false;
			see_right = false;
		}
		if (a == 2) {//�Ʒ���
			see_up = false;
			see_down = true;
			see_left = false;
			see_right = false;
		}
		if (a == 3) {//����
			see_up = false;
			see_down = false;
			see_left = true;
			see_right = false;	
		}
		if (a == 4) {//������
			see_up = false;
			see_down = false;
			see_left = false;
			see_right = true;
		}
	}
	
	//�Ҳ��� ���ִ� �Լ�.
	public void fire(Sprite sprite,int kind_tank, int fire_dir) {
		int fire_x = sprite.getX(), fire_y = sprite.getY();		//�Ұ��� ��ǥ
		int direction=1;				//��ũ�� �ٶ󺸴� ����, �̶� ��ũ�� ��ó���� ���� ���������Ƿ� up������ ����Ű�� 1�� �ʱ�ȭ.
		long end_time = System.currentTimeMillis();
		long temp = end_time - start_time;		//space Ű �����ð��� ��������.
		
		//�Ҳ��� ��ũ�� ������������ ���󰡰Բ� �Ҳ��� ������ġ�� �����ϰ� ����.
		if(see_up == true) {
			fire_x += 10;
			fire_y += 0;
			direction = 1;
		}
		else if(see_down == true) {
			fire_x += 10;
			fire_y += 40;
			direction = 2;
		}
		else if(see_left == true) {
			fire_x += -10;
			fire_y += 15;
			direction = 3;
		}
		else if(see_right == true) {
			fire_x += 30;
			fire_y += 15;
			direction = 4;
		}
		
		//���ΰ� ��ũ�϶� ��ź ó��.
		if(kind_tank == 0) {
			if(temp >= 0 && temp <= 999) {	//0.000�ʺ��� 0.999�ʻ��� �ð����� Ű�ٿ�ÿ��� ������ �߻�.
				ShotSprite shot = new ShotSprite(this, shot_smallImage, fire_x + 5, fire_y);	//���� ũ���� �Ұ��� �߻���.
				shot.set_move(direction);	//���� �� ������ ����.
				sprites.add(shot);
			}
			else if (temp >= 1000  && temp <= 1999) {	//1.000�ʺ��� 1.999�ʻ��� �ð����� Ű�ٿ�ÿ��� �߰��� �߻�.
				ShotSprite shot = new ShotSprite(this, shot_middleImage, fire_x, fire_y-5);		//�߰� ũ���� �Ұ��� �߻���.
				shot.hp = 2;				//�Ѿ� ü���� 2�� ����.(���ݷ� 2)
				shot.set_move(direction);	//���� �� ������ ����.
				sprites.add(shot);
			}
			else if (temp >= 2000) {		//2.000���̻� Ű�ٿ�ÿ��� ū�� �߻�.
				ShotSprite shot = new ShotSprite(this, shot_bigImage, fire_x-5, fire_y - 10);	//Ŀ�ٶ� �Ұ��� �߻���.
				shot.hp = 3;				//�Ѿ� ü���� 3�� ����.(���ݷ� 3)
				shot.set_move(direction);	//���� �� ������ ����.
				sprites.add(shot);
			}
			fire_size = true;
		}
		//�� ��ũ�϶� ��ź ó��.
		if(kind_tank == 1) {
			Enemy_Shot shot = new Enemy_Shot(this, shot_smallImage, fire_x + 5, fire_y);		//����ũ�� ���� �Ұ��� �߻�.
			shot.set_move(fire_dir);
			sprites.add(shot);
		}
			
	}
	//Ű �Է� �̺�Ʈ ��ũ�� �̹��ڿ� ������ �ٲ��ش�.
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			set_direction(3);							//������ ������ �˷���.
			Mytank.change_image(Mytank_leftImage);		//�̹����� ������ �ٶ󺸴� ��ũ�� ����.
			Mytank.setDx(-3);
			}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			set_direction(4);							//������ ������ �˷���.
			Mytank.change_image(Mytank_rightImage);		//�̹����� �������� �ٶ󺸴� ��ũ�� ����.
			Mytank.setDx(+3);
			}
		if (e.getKeyCode() == KeyEvent.VK_UP) {			
			set_direction(1);							//������ ������ �˷���.
			Mytank.change_image(Mytank_upImage);		//�̹����� ������ �ٶ󺸴� ��ũ�� ����.
			Mytank.setDy(-3);
			}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			set_direction(2);							//�Ʒ����� ������ �˷���.
			Mytank.change_image(Mytank_downImage);		//�̹����� �Ʒ����� �ٶ󺸴� ��ũ�� ����.
			Mytank.setDy(+3);
			}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			keydown_fire();								//�� �������� ū ũ���� ��ź�� �߻���.(���� ��Ÿ�ϸ� ������ ���� ��ź�� �����ϴ�.)
			}
		if (e.getKeyCode() == KeyEvent.VK_F5) {			//������ ������ϴ� ��ư.
			if(running == false) {
				startGame();
				running = true;
			}
		}
	}

	//Ű �� �̺�Ʈ. dx, dy�� 0���� ���� �����ش�.
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			Mytank.setDx(0);
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			Mytank.setDx(0);
		if (e.getKeyCode() == KeyEvent.VK_UP)
			Mytank.setDy(0);
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			Mytank.setDy(0);
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
			fire(Mytank,0,0);							//���� �ð��� ���Ͽ� ũ�Ⱑ Ŀ���� ��ź�� �߻�.
	}

	//main�Լ�.
	public static void main(String argv[]) {
		GalagaGame g = new GalagaGame();
		g.gameLoop();
		System.out.print("End");
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


}