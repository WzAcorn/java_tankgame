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

//소스를 입력하고 Ctrl+Shift+O를 눌러서 필요한 파일을 포함한다. 

public class GalagaGame extends JPanel implements KeyListener  {
	long start_time = 0;
	private boolean running = true;					//gameloop를 실행 할지 말지 담당하는 boolean변수
	private boolean fire_size = true;				//포탄 발사를(space키) 키다운할때 사용되는 boolean변수.
	private boolean Mytank_CanMove = true;			//내 탱크가 블록이랑 부딛혔을때 알려주는 boolean변수
	private boolean EnemyMytank_CanMove = true;		//적 탱크가 블록이랑 부딛혔을때 알려주는 boolean변수
	private boolean see_up = false;					//내 탱크가 상,하,좌,우중 어디를 보고있는지 알려주는 변수.
	private boolean see_down = false;
	private boolean see_left = false;
	private boolean see_right = false;

	int tank_count = 0;				//적 탱크가 너무 자주 방향을 바꾸지 않게 억제하기 위한 변수
	int tank_fire_dic = 1;			//적 탱크가 바라보는 방형을 알려주는 변수.
	private ArrayList sprites = new ArrayList();		//스프라이트들을 담아두는 어레이클래스.
	private Sprite Mytank;			//주인공 탱크
	private Sprite Enemytank;		//적군 탱크
	private Image img;				//프레임에 실행되는 게임화면을 그려주는 image 객체
	private Graphics img_g;			//글씨를 화면에 더블버퍼링으로 그려줌.
	
	
	//게임에 필요한 이미지들.
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
		
		//이미지들을 전부 자바에 불러들임.
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
		this.startGame();			//게임 시작
		addKeyListener(this);

	}
	
	//블록들을 적절히 배치시켜주는 함수. 블록들은 매 시행마다 랜덤으로 배치된다.
	private void bulid_map() {
		int [][] bricks_position = new int [21][15];	// 가로 50x22칸, 세로 50x16칸에 블록을 놓기위한 배열.
		double randomValue = 0;
		//영역의 30%를 아무블록으로 채운다.
		//이 때 블록은 한번에 깨지는 갈색벽돌, 세번에 깨지는 시멘트벽돌이 있다.
		for(int a=0; a<18; a++) {
			for(int b=0; b<15; b++) {
				randomValue = Math.random();
				if(randomValue <= 0.3) 
					bricks_position[a][b] = 1;
			}
		}
		
		//밖으로 나가지 못하는 상,하 투명벽 생성
		for(int a=-1; a<18; a++) {
			Block non_brick = new Block(this, ghostbrick_image, a*50, -50);
			Block non_brick2 = new Block(this, ghostbrick_image, a*50, 750);	
			non_brick.hp = 99999;
			non_brick2.hp = 99999;
			sprites.add(non_brick);
			sprites.add(non_brick2);
		}
		//밖으로 나가지 못하는 좌, 우 투명벽 생성
		for(int a=-1; a<16; a++) {
			Block non_brick = new Block(this, ghostbrick_image, -50, a*50);
			Block non_brick2 = new Block(this, ghostbrick_image, 900, a*50);	
			non_brick.hp = 99999;
			non_brick2.hp = 99999;
			sprites.add(non_brick);
			sprites.add(non_brick2);
		}
		
		//갈색벽돌, 시멘트벽돌을 랜덤생성.
		System.out.print(Mytank.getX());
		for(int a=0; a<18; a++) {
			for(int b=1; b<15; b++) {
				if(bricks_position[a][b] == 1) {
					//아래의 if문은 주인공 탱크가 있는곳에는 벽돌을 생성하지 않게해 벽돌이 탱크를 가리지 않게해줌.
					if(!(a*50 <= Mytank.getX() && Mytank.getX() <= (a+1)*50 &&
							b*50 <= Mytank.getY() && Mytank.getY() <= (b+1)*50)) {
						randomValue = Math.random();
						if(randomValue <= 0.7) {	//갈색벽돌이 전체의 70%
							Block breakbrick = new Block(this, breakbrick_image, a*50, b*50);
							sprites.add(breakbrick);
							}
						else {						//시멘트벽돌이 전체의 30%
							Block breakbrick = new Block(this, solidbrick_image, a*50, b*50);
							breakbrick.hp = 3;
							sprites.add(breakbrick);
						}
					}
				}
			}
		}
	}
	
	//게임에 들어가는 객채들을 만들어주는 함수
	private void initSprites() {
		Mytank = new Mytank(this, Mytank_upImage, 405, 505);
		Enemytank =  new Enemy_tank(this, enemytank_upImage, 100, 0);
		sprites.add(Mytank);
		sprites.add(Enemytank);
		bulid_map();
	}
	
	//게임을 시작시키는 함수.
	private void startGame() {
		sprites.clear();
		initSprites();
	}
	
	//적군 탱크를 움직이게 하는 함수.
	//주인공 탱크의 위치를 기반으로 주인공 탱크쪽으로 움직입니다.
	private void EnemyMove() {
		int my_x, my_y, enemy_x, enemy_y;
		my_x = Mytank.getX();
		my_y = Mytank.getX();
		enemy_x = Enemytank.getX();
		enemy_y = Enemytank.getY();
		
		double randomValue;
		randomValue = Math.random();		//난수 생성.
		//매 시행시 좌우이동, 상하이동, 멈춤 셋중 하나를 선택합니다.
		//좌우이동을 선택할때
		if(randomValue <= 0.33) {
			//주인공 탱크가 본인 기준 좌측에있으면 왼쪽으로 따라갑니다.
			if(my_x <= enemy_x) {		
				Enemytank.setDx(-1);
				Enemytank.setDy(0);
				Enemytank.change_image(enemytank_leftImage);
				tank_fire_dic = 3;		//좌측을 담당하는 숫자.
				}
			else{
				Enemytank.setDx(1);
				Enemytank.setDy(0);
				Enemytank.change_image(enemytank_rightImage);
				tank_fire_dic = 4;		//우측을 담당하는 숫자.
				}
		}
		//상하이동을 선택할때
		else if(randomValue <= 0.66){
			if(my_y <= enemy_y) {
				Enemytank.setDx(0);
				Enemytank.setDy(-1);
				Enemytank.change_image(enemytank_downImage);
				tank_fire_dic = 1;		//위를 담당하는 숫자.
				}
			else{
				Enemytank.setDx(0);
				Enemytank.setDy(1);
				Enemytank.change_image(enemytank_upImage);
				tank_fire_dic = 2;		//아래를 담당하는 숫자.
			}
		}
		// 가만있기를 선택할때
		else {
			Enemytank.setDx(0);			
			Enemytank.setDy(0);
		}
		
	}
	
	//적탱크의 공격을 담당하는 함수.
	private void EnemyFire() {
		//인수가 각각 어떤탱크가  , 어느 명령으로 , 어느 방향으로 를 뜻합니다.
		fire(Enemytank, 1,tank_fire_dic);
	}

	//스프라이트 제거함수.
	public void removeSprite(Sprite sprite) {
		sprites.remove(sprite);
	}

	
	//페인트 컴포넌트
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		img = createImage(1100, 800);							//모든 그림들이 그려질 800x600사이즈의 도화지를 하나 생성.
		img_g = img.getGraphics();
		img_g.setColor(Color.black);	
		img_g.fillRect(0, 0, 900, 800);							//900,800크기의 게임이 run하는 검은 바탕 생성.
		img_g.setColor(Color.gray);
		img_g.fillRect(900, 0, 200, 800);						//200,800크기의 캐릭터의 인터페이스를 나타내는 회색바탕 생성.
		img_g.setFont(new Font("고딕체", Font.BOLD, 25));			//단계와 점수 폰트를 정함.
		img_g.setColor(Color.WHITE);							//색상은 화이트
		img_g.drawString("Me: "+Integer.toString(Mytank.hp), 930, 20);				//주인공 체력을 표시.
		img_g.drawString("Enemy: "+Integer.toString(Enemytank.hp), 930, 50);		//적 탱크 체력을 표시.
		
		for (int i = 0; i < sprites.size(); i++) {				//어레이리스트 안의 스프라이트들을 전부 그려줍니다.
			Sprite sprite = (Sprite) sprites.get(i);
			sprite.draw(img_g);			
		}
		if(running == false) {									//게임이 정지중일때(누군가 죽었을때)
			img_g.setFont(new Font("고딕체", Font.BOLD, 85));		//폰트를 정함.
			if(Mytank.hp == 0) {								//내 체력이 0이면 적군의 승리!
				img_g.drawString("Enemy Win!", 220, 160);
			}
			if(Enemytank.hp == 0) {								//적 체력이 0이면 나의 승리!
				img_g.drawString("You Win!", 270, 160);
			}
			img_g.drawString("Press F5 To Restart", 80, 540);	//f5 키를 눌러 게임을 재시작 할수 있게 함.
		}
		
		
		g.drawImage(img, 0, 0, null);							//객채들을 더블버퍼링으로 그림.
	}

	
	//게임의 루프문이 실행되는 함수.
	public void gameLoop() {
		double randomValue = 0;
		while (true){	
			if(running == true) {	//running상태가 true일때만 게임이 실행.
				
				//적군 탱크는 같은 움직임을 15프레임 반복하고(0.15초) 다음 움직임을 결정하게 됩니다.
				if (tank_count == 0) {randomValue = Math.random();}
				if(randomValue <= 0.3) {EnemyMove();}
				if(randomValue <= 0.1) {EnemyFire();}
				tank_count++;
				tank_count = tank_count % 15;
				
				Mytank_CanMove = true;
				EnemyMytank_CanMove = true;
				
				//스프라이트들의 충돌을 다룬다.
				for (int p = 0; p < sprites.size(); p++) {
					for (int s = p + 1; s < sprites.size(); s++) {
						Sprite me = (Sprite) sprites.get(p);
						Sprite other = (Sprite) sprites.get(s);
						if (me.checkCollision(other)) {
							me.handleCollision(other);
							other.handleCollision(me);
						}
						
						//me와 other이 탱크와 블록일때 부딛힌 거리만큼 맞은편 방향으로 이동하게됨.
						//게임을 실행시켜보면 부딛히자 마자 밀리고를 반복하여 마치 진동하는거처럼 보이게 됩니다.
						if (me.checkCollision(other) && (me.type==1) && (other.type==3)) {
							Mytank_CanMove = false;							//주인공 탱크의 움직임 상태를 false로 제한.
							EnemyMytank_CanMove = false;					//적 탱크의 움직임 상태를 false로 제한.
							int temp_x, temp_y;
							temp_x = (me.getX()+21 - other.getX()+25);		//객채의 중심거리 x를 구함.
							temp_y = (me.getY()+17 - other.getY()+25);		//객채의 중심거리 y를 구함.
							if( 0 <= temp_x && temp_x<= 16 && 0 <= temp_y && temp_y <= 84) 			//벽돌과 왼쪽에서 부딛혔으면
								me.x = me.getX()-3;													//왼쪽으로 3만큼 이동
							else if( 13 <= temp_x && temp_x<= 95 && 0 <= temp_y && temp_y <= 12) 	//벽돌과 위쪽에서 부딛혔으면
								me.y = me.getY()-3;													//위쪽으로 3만큼 이동
							else if( 13 <= temp_x && temp_x<= 95 &&  85 <= temp_y && temp_y < 94) 	//벽돌과 아래쪽에서 부딛혔으면
								me.y = me.getY()+3;													//아래쪽으로 3만큼 이동
							else if( 80 <= temp_x && temp_x<= 95 && 0 <= temp_y && temp_y <= 85) 	//벽돌과 오른쪽에서 부딛혔으면
								me.x = me.getX()+3;													//오른쪽으로 3만큼 이동	
						}

					}
				}
				
				//sprite들을 전부 알맞게 move()시킵니다.
				for (int i = 0; i < sprites.size(); i++) {
					Sprite sprite = (Sprite) sprites.get(i);
					if(!(sprite.type == 1 && Mytank_CanMove == false)) {
						sprite.move();
					}
				}
				//적이나 아군탱크 체력이 둘중 하나가 0일때 게임을 멈춤.
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

	//space바를 오래 누를수록 주인공의 불공격이 커지고, 세집니다.
	//얼마나 오래 눌렀는지 체크하는 함수.
	public void keydown_fire() {		//space키를 오래누를수록 더 큰 불공이 날라가게 하는 함수.
		if(fire_size == true) {		//한번 keypress입력을 받으면 keyrelease가 있기 전까지 함수가 재실행되지않음.
			fire_size = false;
			start_time = System.currentTimeMillis(); //현재의 시간을 ms단위로 얻어옴
		}
	}
	
	//불꽃을 상하좌우 쏘는 방향을 정해주는 함수.
	public void set_direction(int a) {
		if (a == 1) {//위쪽
			see_up = true;
			see_down = false;
			see_left = false;
			see_right = false;
		}
		if (a == 2) {//아래쪽
			see_up = false;
			see_down = true;
			see_left = false;
			see_right = false;
		}
		if (a == 3) {//왼쪽
			see_up = false;
			see_down = false;
			see_left = true;
			see_right = false;	
		}
		if (a == 4) {//오른쪽
			see_up = false;
			see_down = false;
			see_left = false;
			see_right = true;
		}
	}
	
	//불꽃을 쏴주는 함수.
	public void fire(Sprite sprite,int kind_tank, int fire_dir) {
		int fire_x = sprite.getX(), fire_y = sprite.getY();		//불공의 좌표
		int direction=1;				//탱크가 바라보는 방향, 이때 탱크는 맨처음에 위를 보고있으므로 up방향을 가르키는 1로 초기화.
		long end_time = System.currentTimeMillis();
		long temp = end_time - start_time;		//space 키 누른시간을 리턴해줌.
		
		//불꽃이 탱크의 포구에서부터 날라가게끔 불꽃의 시작위치를 적절하게 수정.
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
		
		//주인공 탱크일때 포탄 처리.
		if(kind_tank == 0) {
			if(temp >= 0 && temp <= 999) {	//0.000초부터 0.999초사이 시간동안 키다운시에는 작은공 발사.
				ShotSprite shot = new ShotSprite(this, shot_smallImage, fire_x + 5, fire_y);	//작은 크기의 불공을 발사함.
				shot.set_move(direction);	//포를 쏠 방향을 정함.
				sprites.add(shot);
			}
			else if (temp >= 1000  && temp <= 1999) {	//1.000초부터 1.999초사이 시간동안 키다운시에는 중간공 발사.
				ShotSprite shot = new ShotSprite(this, shot_middleImage, fire_x, fire_y-5);		//중간 크기의 불공을 발사함.
				shot.hp = 2;				//총알 체력을 2로 설정.(공격력 2)
				shot.set_move(direction);	//포를 쏠 방향을 정함.
				sprites.add(shot);
			}
			else if (temp >= 2000) {		//2.000초이상 키다운시에는 큰공 발사.
				ShotSprite shot = new ShotSprite(this, shot_bigImage, fire_x-5, fire_y - 10);	//커다란 불공을 발사함.
				shot.hp = 3;				//총알 체력을 3로 설정.(공격력 3)
				shot.set_move(direction);	//포를 쏠 방향을 정함.
				sprites.add(shot);
			}
			fire_size = true;
		}
		//적 탱크일때 포탄 처리.
		if(kind_tank == 1) {
			Enemy_Shot shot = new Enemy_Shot(this, shot_smallImage, fire_x + 5, fire_y);		//적탱크는 작은 불공만 발사.
			shot.set_move(fire_dir);
			sprites.add(shot);
		}
			
	}
	//키 입력 이벤트 탱크의 이미자와 방향을 바꿔준다.
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			set_direction(3);							//왼쪽의 방향을 알려줌.
			Mytank.change_image(Mytank_leftImage);		//이미지를 왼쪽을 바라보는 탱크로 변경.
			Mytank.setDx(-3);
			}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			set_direction(4);							//오쪽의 방향을 알려줌.
			Mytank.change_image(Mytank_rightImage);		//이미지를 오른쪽을 바라보는 탱크로 변경.
			Mytank.setDx(+3);
			}
		if (e.getKeyCode() == KeyEvent.VK_UP) {			
			set_direction(1);							//위쪽의 방향을 알려줌.
			Mytank.change_image(Mytank_upImage);		//이미지를 위쪽을 바라보는 탱크로 변경.
			Mytank.setDy(-3);
			}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			set_direction(2);							//아래쪽의 방향을 알려줌.
			Mytank.change_image(Mytank_downImage);		//이미지를 아래쪽을 바라보는 탱크로 변경.
			Mytank.setDy(+3);
			}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			keydown_fire();								//꾹 누를수록 큰 크기의 포탄을 발사함.(빨리 연타하면 빠르게 작은 포탄이 나갑니다.)
			}
		if (e.getKeyCode() == KeyEvent.VK_F5) {			//게임을 재시작하는 버튼.
			if(running == false) {
				startGame();
				running = true;
			}
		}
	}

	//키 뗌 이벤트. dx, dy를 0으로 리셋 시켜준다.
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
			fire(Mytank,0,0);							//누른 시간에 비레하여 크기가 커지는 포탄을 발사.
	}

	//main함수.
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