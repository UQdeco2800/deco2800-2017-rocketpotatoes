package com.deco2800.potatoes.entities.effects;

import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.projectiles.Projectile.ProjectileType;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.TextureManager;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.util.WorldUtil;

public abstract class Effect extends AbstractEntity implements Tickable {

	protected float damage = 0;
	protected float range = 0;
	protected static transient String TEXTURE = "default";
	protected EffectType effectType;
	protected String[] textureArray;
	protected Class<?> targetClass;
	protected float rotationAngle = 0;
	protected boolean animate = true;
	protected boolean loopAnimation = false;

	public enum EffectType {
		AOE {
			public String toString() {
				return "aoe";
			}

			public int numberOfTextures() {
				return 3;
			}
		},
		EXPLOSION {
			public String toString() {
				return "explosion";
			}

			public int numberOfTextures() {
				return 3;
			}
		},
		LIGHTNING {
			public String toString() {
				return "lightning";
			}

			public int numberOfTextures() {
				return 1;
			}
		},
		LAZER {
			public String toString() {
				return "lightning";
			}

			public int numberOfTextures() {
				return 1;
			}
		},
		DAMAGED_GROUND {
			public String toString() {
				return "DamagedGroundTemp";
			}

			public int numberOfTextures() {
				return 3;
			}
		},
		SWIPE {
			public String toString() {
				return "swipe";
			}

			public int numberOfTextures() {
				return 3;
			}
		};

		public int numberOfTextures() {
			return 0;
		}
	}

	public Effect() {

	}

	public Effect(Class<?> targetClass, Vector3 position, float xLength, float yLength, float zLength,
			float xRenderLength, float yRenderLength, float damage, float range, EffectType effectType) {
		super(position.x, position.y, position.z, xLength, yLength, zLength, xRenderLength, yRenderLength, true,
				effectType.toString());

		if (targetClass != null)
			this.targetClass = targetClass;
		else
			this.targetClass = MortalEntity.class;

		this.damage = damage;
		this.range = range;

		setTextureArray(effectType, effectType.numberOfTextures());
	}

	protected void setTextureArray(EffectType effectType, int numberOfTextures) {
		textureArray = new String[effectType.numberOfTextures()];

		if (effectType != null && !effectType.toString().isEmpty())
			this.effectType = effectType;

		for (int t = 0; t < effectType.numberOfTextures(); t++) {
			textureArray[t] = this.effectType + Integer.toString(t + 1);
		}
	}

	public void drawEffect(SpriteBatch batch) {

	}

	@Override
	public void onTick(long time) {
		animate();

		Box3D newPos = getBox3D();
		newPos.setX(this.getPosX());
		newPos.setY(this.getPosY());

		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();

		for (AbstractEntity entity : entities.values()) {
			if (!targetClass.isInstance(entity)) {
				continue;
			}
			if (newPos.overlaps(entity.getBox3D())) {
				if (entity instanceof Player) {
					GameManager.get().getManager(PlayerManager.class).getPlayer().setDamaged(true);
				}
				((MortalEntity) entity).damage(damage);
			}
		}
	}

	protected int effectTimer;
	protected int currentSpriteIndexCount;

	protected void animate() {
		if (animate) {
			effectTimer++;
			if (effectTimer % 4 == 0) {
				setTexture(textureArray[currentSpriteIndexCount]);
				if (currentSpriteIndexCount == effectType.numberOfTextures() - 1) {
					if (loopAnimation)
						currentSpriteIndexCount = 0;
					else
						GameManager.get().getWorld().removeEntity(this);
				} else {
					currentSpriteIndexCount++;
				}
			}
		}
	}

	public void drawTextureBetween(SpriteBatch batch, String texture, float xPos, float yPos, float fxPos,
			float fyPos) {
		int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
		int tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");

		TextureManager reg = (TextureManager) GameManager.get().getManager(TextureManager.class);
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

		int srcX = 0;
		int srcY = 0;
		int srcWidth = tex.getWidth();
		int srcHeight = tex.getHeight();
		batch.draw(tex, lX, lY, originX, originY, lWidth, lHeight, lScaleX, lScaleY,
				WorldUtil.rotation(xPos, yPos, fxPos, fyPos) - 45, srcX, srcY, srcWidth, srcHeight, false, false);

	}

	public float getDamage() {
		return damage;
	}

}
