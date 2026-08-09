package com.altomedia.pixeldungeon.actors.buffs

import com.altomedia.pixeldungeon.messages.M
import com.altomedia.pixeldungeon.ui.BuffIndicator

class Disarm: FlavourBuff() {
    init {
        type = buffType.NEGATIVE
    }

    override fun icon(): Int = BuffIndicator.DISARM

    override fun toString(): String = M.L(this, "name")

    override fun desc(): String = M.L(this, "desc", dispTurns())
}