package com.deco2800.potatoes.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple tree to test the engine.
 * This tree is a hasProgress to test that interface
 * @Author Tim Hadwen
 */
public class Tree extends AbstractEntity implements HasProgress {

    private static final transient Logger LOGGER = LoggerFactory.getLogger(Tree.class);
    
    private final static transient String TEXTURE = "tree";

    public Tree() {
		// empty for serialization
	}


    /**
     * Constructor for a Tree
     * @param posX
     * @param posY
     * @param posZ
     */
    public Tree(float posX, float posY, float posZ) {
        super(posX, posY, posZ, 1f, 1f, 1f, TEXTURE);

        /* Sets the texture of the tree to tree */
        LOGGER.info("Making a tree @ [" + posX + ", " + posY + "]");
    }

    @Override
    public int getProgress() {
        return 100;
    }

	@Override
	public void setProgress(int p) {
		return;
	}

    @Override
    public boolean showProgress() {
        return true;
	}

	@Override
	public float getProgressRatio() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxProgress() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxProgress(int p) {
		// TODO Auto-generated method stub

	}
}
