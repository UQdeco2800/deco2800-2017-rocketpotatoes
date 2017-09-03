package com.deco2800.potatoes.entities.effects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.TextureManager;

import java.util.Random;

public class LightningEffect extends Effect {

	private float lifetime = 0.25f;
	private float segmentStep=150f;

	float xPos = 0;
	float yPos = 0;
	float fxPos = 1;
	float fyPos = 1;

	public LightningEffect(float xPos, float yPos, float fxPos, float fyPos) {
		setTexture("lightning");
		this.xPos = xPos;
		this.yPos = yPos;
		this.fxPos = fxPos;
		this.fyPos = fyPos;
	}

	public float[][] positions(float angle,float xPos, float yPos, float fxPos, float fyPos){


		float lengthX=(fxPos-xPos);
		float lengthY=(fyPos-yPos);

		float magnitude=(float)Math.sqrt(lengthX*lengthX+lengthY*lengthY);

		int segments=(int)Math.ceil(magnitude/segmentStep);//do rounding here
		float[][] pos=new float[segments][2];//remove round

		Random random =new Random();

		//20
		for(int i=0;i<segments;i++){
			float rand=random.nextFloat();

			float x=((lengthX)/magnitude)*(float)Math.cos(angle)*rand;
			float y=((lengthY)/magnitude)*(float)Math.sin(angle)*rand;
//			pos[i]=[[]];
			pos[i][0]=x;
			pos[i][1]=y;
		}
		return pos;
	}

	@Override
	public void drawEffect(SpriteBatch batch) {

		int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
		int tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");

		TextureManager reg = GameManager.get().getManager(TextureManager.class);
		Texture tex = reg.getTexture(this.getTexture());

		float lWidth = tex.getWidth();
		float lHeight = tex.getHeight();

		Vector2 startPos = worldToScreenCoordinates(xPos, yPos, 0);

		Vector2 endPos = worldToScreenCoordinates(fxPos, fyPos, 0);

		float l = endPos.x - startPos.x;
		float h = endPos.y - startPos.y;

		float lX = startPos.x - (lWidth - tileWidth) / 2;
		float lY = 0 - startPos.y - (lHeight - tileHeight) / 2;

		float originX = tex.getWidth() / 2;
		float originY = tex.getHeight() / 2;

		float lScaleX = (float) (Math.sqrt(l * l + h * h));
		float lScaleY = 0.4f;

		float rotation = (float) (Math.atan2(l, h) * 180 / Math.PI) - 90;
		int srcX = 0;
		int srcY = 0;
		int srcWidth = tex.getWidth();
		int srcHeight = tex.getHeight();
	//here
		System.out.println(positions(rotation,startPos.x,startPos.y,endPos.x,endPos.y)[0][0]+" " + positions(rotation,startPos.x,startPos.y,endPos.x,endPos.y)[0][1]);
		batch.draw(tex, lX, lY, originX, originY, lWidth, lHeight, lScaleX, lScaleY, rotation, srcX, srcY, srcWidth,
				srcHeight, false, false);

	}

	@Override
	public void onTick(long time) {
		lifetime -= 0.05;
		if (lifetime <= 0)
			GameManager.get().getWorld().removeEntity(this);
	}

	@Override
	public float getDamage() {
		// TODO Auto-generated method stub
		return 0;
	}

}
