package com.altomedia.pixeldungeon.actors.buffs

import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.ui.BuffIndicator

class MoonNight : FlavourBuff() {
    init {
        type = buffType.NEUTRAL
    }

    override fun icon(): Int = BuffIndicator.MOON_NIGHT
    
    override fun toString(): String = Messages.get(this, "name")

    override fun desc(): String = Messages.get(this, "desc")

    override fun attachTo(target: Char?): Boolean {
        val attached = super.attachTo(target)
        if (attached) {
            Dungeon.observe()
            GameScene.updateFog()
        }
        
        return attached
    }

    override fun detach() {
        super.detach()
        Dungeon.observe()
        GameScene.updateFog()
    }

    companion object {
        const val DURATION = 100f
    }
}