import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Buliet {
      private int x,y;
      public static final int BULIETSIZEX=10;
      public static final int BULIETSIZEY=10;
      public static final int BULIETSPEEDX=10;
      public static final int BULIETSPEEDY=10;
      private Tank.Direction dir;
      private boolean isEnemy=true;
      private Tank tank;
      private TankClient tankClient;
      public Buliet(int x,int y,Tank.Direction dir,boolean isEnemy,Tank tank,TankClient tankClient){
    	  this.x=x;
    	  this.y=y;
    	  this.dir=dir;
    	  this.isEnemy=isEnemy;
    	  this.tank=tank;
    	  this.tankClient=tankClient;
      }
      public void drawBuliet(Graphics g){
    	  Color color=g.getColor();
    	  g.setColor(Color.WHITE);
    	  g.fillOval(x, y, BULIETSIZEX, BULIETSIZEY);
    	  g.setColor(color);
    	  move();
      }
      
      public void move() {
  		switch (dir) {
  		case U:
  			y -= BULIETSPEEDY;
  			break;
  		case D:
  			y += BULIETSPEEDY;
  			break;
  		case L:
  			x -= BULIETSPEEDX;
  			break;
  		case R:
  			x += BULIETSPEEDX;
  			break;
  		case LU:
  			x -= BULIETSPEEDX;
  			y -= BULIETSPEEDY;
  			break;
  		case LD:
  			x -= BULIETSPEEDX;
  			y += BULIETSPEEDY;
  			break;
  		case RU:
  			x += BULIETSPEEDX;
  			y -= BULIETSPEEDY;
  			break;
  		case RD:
  			x += BULIETSPEEDX;
  			y += BULIETSPEEDY;
  			break;
  		case STOP:
  			return;
  		default:
  			return;
  		}
  		if(hitWall()||outBounds()){
  			if(tankClient!=null){
  				tankClient.buliets.remove(this);
  			}
  		}
        hitTank();
	}
      
      public Rectangle getBulietRectangle(){
    	  return new Rectangle(x, y, BULIETSIZEX, BULIETSIZEY);
      }
      
      public boolean hitWall(){
    	  for(int i=0;i<tankClient.walls.size();i++){
    		  if(this.getBulietRectangle().intersects(tankClient.walls.get(i).getWallRectangle())){
    			  return true;
    		  }
    	  }
    	  return false;
    	  
      }
      
      public boolean outBounds(){
    	  if (x < 0 || x > TankClient.GAMEWIDTH || y < 0 || y > TankClient.GAMEHEIGHT) {
  			return true;
  		} else {
  			return false;
  		}
      }
      
      public int hitTank(){
    	  if(isEnemy){
    		  if(tankClient.myTank!=null&&this.getBulietRectangle().intersects(tankClient.myTank.getTankRectangle())){
    			  Tank tank=tankClient.myTank;
    			  tank.setLife(tank.getLife()-20);
    			  tankClient.buliets.remove(this);
    			  if(tank.getLife()==0){
    				  tankClient.myTank=null;
    				  tankClient.explosions.add(new Explosion(tank.getX(), tank.getY(), this.tankClient ));
    			  }
    			  
    			  return 1;
    		  }
    		  for(int i=0;i<tankClient.tanks.size();i++){
    			  if(this.getBulietRectangle().intersects(tankClient.tanks.get(i).getTankRectangle())&&
    					  this.tank!=tankClient.tanks.get(i)){
    				  tankClient.buliets.remove(this);
    				  return 3;
    			  }
    		  }
    	  }else {
    		  if(tankClient.myTank!=null&&this.getBulietRectangle().intersects(tankClient.myTank.getTankRectangle())&&
    				  this.tank!=tankClient.myTank){
    			  tankClient.buliets.remove(this);
    			  return 3;
    		  }
    		  for(int i=0;i<tankClient.tanks.size();i++){
    			  if(this.getBulietRectangle().intersects(tankClient.tanks.get(i).getTankRectangle())){
					tankClient.killEnemyCount++;
    				  tankClient.tanks.get(i).setLife(0);
    				  tankClient.buliets.remove(this);
    				  tankClient.explosions.add(new Explosion(tankClient.tanks.get(i).getX(), tankClient.tanks.get(i).getY(), this.tankClient ));
    				  tankClient.tanks.remove(i);
    				  return 2;
    			  }
    		  }
		}
    	  return 4;
    	  
      }
      
      public Tank getTank(){
    	  return this.tank;
      }
      
      public boolean getIsenemy(){
    	  return this.isEnemy;
      }
      
      
}
