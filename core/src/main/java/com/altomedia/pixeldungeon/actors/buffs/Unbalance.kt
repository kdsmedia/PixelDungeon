package com.altomedia.pixeldungeon.actors.buffs

import com.altomedia.pixeldungeon.messages.M
import com.altomedia.pixeldungeon.ui.BuffIndicator

// see Char::checkHit
class Unbalance : FlavourBuff() {
    init {
        type = buffType.NEGATIVE
    }

    override fun icon(): Int = BuffIndicator.UNBALANCE

    override fun toString(): String = M.L(this, "name")

    override fun desc(): String = M.L(this, "desc", dispTurns())

}