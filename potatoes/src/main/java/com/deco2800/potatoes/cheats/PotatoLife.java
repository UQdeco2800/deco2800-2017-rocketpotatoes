package com.deco2800.potatoes.cheats;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.ParticleManager;
import com.deco2800.potatoes.managers.TextureManager;
import com.deco2800.potatoes.renderering.particles.ParticleEmitter;
import com.deco2800.potatoes.renderering.particles.types.GlobalEmitterType;

public class PotatoLife implements CheatExecution {
    boolean active = false;
    ParticleEmitter emitter;

    @Override
    public void run() {
        ParticleManager m = GameManager.get().getManager(ParticleManager.class);
        TextureManager t = GameManager.get().getManager(TextureManager.class);
        if (active) {
            m.stopEmitter(emitter);
        }
        else {
            if (emitter == null) {
                emitter = new ParticleEmitter(0, 0,
                        new GlobalEmitterType(5000, 5000.0f, 75.0f, 60, t.getTexture("potate")));
            }

            m.addParticleEmitter(0, emitter);
        }
    }
}
