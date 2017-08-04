package com.deco2800.potatoes.renderering;

/**
 * An object that can be rendered on the screen.
 * Renderables should have a texture that they can return
 * when asked using the onRender function.
 *
 * Textures should be size suitable to the game.
 */
public interface Renderable {

    /**
     * Renderables must impliment the onRender function.
     * This function allows the current rendering system to request a texture
     * from the object being rendered.
     *
     * Returning null will render an error image in this items place.
     * @return The texture to be rendered onto the screen
     */
    String getTexture();
    float getPosX();
    float getPosY();
    float getPosZ();
    float getXRenderLength();
    float getYRenderLength();
}
