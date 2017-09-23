package com.deco2800.potatoes.entities;

import java.util.HashMap;
import java.util.Map;

import com.deco2800.potatoes.entities.HasDirection.Direction;
import com.deco2800.potatoes.entities.Player.PlayerState;
import com.deco2800.potatoes.entities.animation.AnimationFactory;
import com.deco2800.potatoes.entities.animation.TimeAnimation;

public class Caveman {
	
	private TimeAnimation cavemanWalkSouthEast = AnimationFactory.createSimpleTimeAnimation(1000, new String[] { 
			"caveman_walk_SE_1", 
			"caveman_walk_SE_2", 
			"caveman_walk_SE_3", 
			"caveman_walk_SE_4", 
			"caveman_walk_SE_5", 
			"caveman_walk_SE_6", 
			"caveman_walk_SE_7", 
			"caveman_walk_SE_8" 
	});
    
    private TimeAnimation cavemanWalkSouth = AnimationFactory.createSimpleTimeAnimation(1000, new String[] { 
			"caveman_walk_S_1", 
			"caveman_walk_S_2", 
			"caveman_walk_S_3", 
			"caveman_walk_S_4", 
			"caveman_walk_S_5", 
			"caveman_walk_S_6", 
			"caveman_walk_S_7", 
			"caveman_walk_S_8" 
	});
    
    public Map<Direction, TimeAnimation> cavemanIdleAnimations = new HashMap<>();
    public Map<Direction, TimeAnimation> cavemanAttackAnimations = new HashMap<>();
    public Map<Direction, TimeAnimation> cavemanDeathAnimations = new HashMap<>();
    
    
    public static Map<Direction, TimeAnimation> cavemanWalkAnimations() {
    		Map<Direction, TimeAnimation> animations = new HashMap<>();
    		int animationTime = 1000;
    		for (Direction direction : Direction.values()) {
    			String[] frames = new String[] {};
    			for (int i=1; i<=8; i++) {
    				frames[i-1] = "";
    			}
    			animations.put(Direction.North, AnimationFactory.createSimpleTimeAnimation(animationTime, frames));
    		}
    		return null;
    }
    
    /**
     * Creates a map of player directions with player state animations. Use a 
     * direction as a key to receive the respective animation.
     * 
     * @param playerType
     * 			A string representing the type of player.
     * @param state
     * 			The state of the player.
     * @param frameCount
     * 			The number of frames in the animation.
     * @param animationTime
     * 			The time per animation cycle.
     * @return
     * 		A map of directions with animations for the specified state.
     */
    public static Map<Direction, TimeAnimation> makePlayerAnimation(String playerType, PlayerState state, int frameCount, int animationTime) {
		Map<Direction, TimeAnimation> animations = new HashMap<>();
		
		for (Direction direction : Direction.values()) {
			String[] frames = new String[] {};
			
			for (int i=1; i<=frameCount; i++) {
				frames[i-1] = playerType + "_" + state.name() + "_" + direction.toString() + i;
			}
			
			animations.put(Direction.North, AnimationFactory.createSimpleTimeAnimation(animationTime, frames));
		}
		return animations;
    }

}
