package com.deco2800.potatoes.entities.effects;

import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.TextureManager;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.util.WorldUtil;

public abstract class Effect extends AbstractEntity implements Tickable {

	protected float damage = 0;
	protected float range = 0;
	protected EffectTexture effectTexture;
	protected Class<?> targetClass;
	protected float rotationAngle = 0;
	protected boolean animate = true;
	protected boolean loopAnimation = false;
	protected Vector3 position;

	/**
	 * Used as a container for Effect textures and a lookup for the ones that
	 * currently exist and are usable
	 */
	public enum EffectTexture {
		AOE {
			public String[] textures() {
				return new String[] { "aoe1", "aoe2", "aoe3" };
			}
		},
		EXPLOSION {
			public String[] textures() {
				return new String[] { "explosion1", "explosion2", "explosion3" };
			}
		},
		LIGHTNING {
			public String[] textures() {
				return new String[] { "lightning" };
			}
		},
		LAZER {
			public String[] textures() {
				return new String[] { "lightning" };
			}
		},
		DAMAGED_GROUND {
			public String[] textures() {
				return new String[] { "DamagedGroundTemp1", "DamagedGroundTemp2", "DamagedGroundTemp3" };
			}
		},
		SWIPE {
			public String[] textures() {
				return new String[] { "swipe1", "swipe2", "swipe3" };
			}
		},
		LARGE_FOOTSTEP {
			public String[] textures() {
				return new String[] { "TankFootstepTemp1", "TankFootstepTemp2", "TankFootstepTemp3" };
			}
		},
		HEALING {
			public String[] textures() {
				return new String[] { "Healing1", "Healing2", "Healing3" };
			}
		};

		public String[] textures() {
			return new String[] { "default" };
		}
	}

	public Effect() {

	}

	/**
	 * The Effect classes render a texture at a location and may cause damage to
	 * specific targets in its collision area
	 * 
	 * @param targetClass
	 *            the target's class i.e. MortalEntity.class
	 * @param position
	 *            starting position
	 * @param xLength
	 * @param yLength
	 * @param zLength
	 * @param xRenderLength
	 * @param yRenderLength
	 * @param damage
	 *            damage effect deals to target(s)
	 * @param range
	 *            radius of effect
	 * @param effectTexture
	 *            which set of textures to use for animation. Uses EffectTexture
	 *            enum as lookup
	 */
	public Effect(Class<?> targetClass, Vector3 position, float xLength, float yLength, float zLength,
			float xRenderLength, float yRenderLength, float damage, float range, EffectTexture effectTexture) {
		super(position.x, position.y, position.z, xLength, yLength, zLength, xRenderLength, yRenderLength, true,
				effectTexture.textures()[0]);

		if (targetClass != null)
			this.targetClass = targetClass;
		else
			this.targetClass = MortalEntity.class;

		if (effectTexture == null)
			throw new RuntimeException("projectile type must not be null");
		else
			this.effectTexture = effectTexture;

		this.damage = damage;
		this.range = range;
		this.position = position;
	}

	/**
	 * Called every frame for Effect classes
	 * 
	 * @param batch
	 *            the SpriteBatch to render to
	 */
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
				((MortalEntity) entity).damage(damage);
			}
		}
	}

	protected int effectTimer;
	protected int currentSpriteIndexCount;

	/*
	 * Loops through texture array and sets sprite every frame. Looking into using
	 * AnimationFactory as animation controller
	 */
	protected void animate() {
		if (animate) {
			effectTimer++;
			if (effectTimer % 4 == 0) {
				setTexture(effectTexture.textures()[currentSpriteIndexCount]);
				if (currentSpriteIndexCount == effectTexture.textures().length - 1) {
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

	/**
	 * Renders a line between two points
	 * 
	 * @param batch
	 *            the SpriteBatch to render to
	 * @param texture
	 *            the texture to draw
	 * @param xPos
	 *            start x position
	 * @param yPos
	 *            start y position
	 * @param fxPos
	 *            end x position
	 * @param fyPos
	 *            end y position
	 */
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

		// length of line in x direction
		float lX = startPos.x - (lWidth - tileWidth) / 2;
		// length of line in y direction
		float lY = 0 - startPos.y - (lHeight - tileHeight) / 2;

		float originX = tex.getWidth() / 2;
		float originY = tex.getHeight() / 2;

		// stretch texture using x scale
		float lScaleX = (float) (Math.sqrt(l * l + h * h));
		float lScaleY = 0.4f;

		int srcX = 0;
		int srcY = 0;
		int srcWidth = tex.getWidth();
		int srcHeight = tex.getHeight();
		batch.draw(tex, lX, lY, originX, originY, lWidth, lHeight, lScaleX, lScaleY,
				WorldUtil.rotation(xPos, yPos, fxPos, fyPos) - 45, srcX, srcY, srcWidth, srcHeight, false, false);

	}

	public float getPosX() {
		return position.x;
	}

	public float getPosY() {
		return position.y;
	}

	/**
	 * Gets effect damage
	 * 
	 * @return damage
	 */
	public float getDamage() {
		return damage;
	}

}
