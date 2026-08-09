package com.altomedia.pixeldungeon.actors.buffs

import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.messages.M
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.ui.BuffIndicator

class TimeDilation : FlavourBuff() {
    override fun icon(): Int = BuffIndicator.TIME_DILATION

    override fun attachTo(target: Char?): Boolean {
        if(super.attachTo(target)){
            GameScene.setColorLayer(0x4c006699)
            return true
        }

        return false
    }

    override fun detach() {
        super.detach()
        GameScene.resetColorLayer()
    }

    override fun toString(): String = M.L(this, "name")

    override fun desc(): String = M.L(this, "desc", dispTurns())
}