package com.deco2800.potatoes.managers;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShaderManager extends Manager {
	public static final String EXTRA_COLOR_ATTRIBUTE = "v_extra_color";
	private static final Logger LOGGER = LoggerFactory.getLogger(ShaderManager.class);
	private static final String VERTEX_SHADER = createVertexShader();
	private static final String FRAG_SHADER = createFragmentShader();

	ShaderProgram shader;

	public ShaderManager() {
		// So we don't have to define everything if we don't want to
		ShaderProgram.pedantic = false;
		shader = new ShaderProgram(VERTEX_SHADER, FRAG_SHADER);
		if (!shader.isCompiled()) {
			LOGGER.error("Shader failed to compile! Rendering the game will probably crash or be broken");
		}
		// Set extra colour to white as default
		shader.setUniformf(EXTRA_COLOR_ATTRIBUTE, 1, 1, 1, 1);
	}

	/**
	 * Creates the vertex shader (the default vertex shader from
	 * {@link com.badlogic.gdx.graphics.g2d.SpriteBatch#createDefaultShader()}
	 */
	private static String createVertexShader() {
		return "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
				+ "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
				+ "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
				+ "uniform mat4 u_projTrans;\n" //
				+ "varying vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "\n" //
				+ "void main()\n" //
				+ "{\n" //
				+ "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
				+ "   v_color.a = v_color.a * (255.0/254.0);\n" //
				+ "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
				+ "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
				+ "}\n";
	}

	/**
	 * Creates the fragment shader (the default vertex shader from
	 * {@link com.badlogic.gdx.graphics.g2d.SpriteBatch#createDefaultShader() with extra uniform value}
	 */
	private static String createFragmentShader() {
		return "#ifdef GL_ES\n" //
				+ "#define LOWP lowp\n" //
				+ "precision mediump float;\n" //
				+ "#else\n" //
				+ "#define LOWP \n" //
				+ "#endif\n" //
				+ "varying LOWP vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "uniform sampler2D u_texture;\n" //
				+ "uniform vec4 " + EXTRA_COLOR_ATTRIBUTE + ";\n" //
				+ "void main()\n"//
				+ "{\n" //
				+ "  gl_FragColor = " + EXTRA_COLOR_ATTRIBUTE + " * v_color * texture2D(u_texture, v_texCoords);\n" //
				+ "}";
	}

	public ShaderProgram getShader() {
		return shader;
	}
}
