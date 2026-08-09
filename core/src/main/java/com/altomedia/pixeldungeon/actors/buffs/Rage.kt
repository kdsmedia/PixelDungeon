package com.altomedia.pixeldungeon.actors.buffs

import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.ui.BuffIndicator

class Rage: FlavourBuff() {
    init {
        type = buffType.POSITIVE
    }

    override fun icon(): Int = BuffIndicator.FURY

    override fun toString(): String = Messages.get(this, "name")

    override fun heroMessage(): String = Messages.get(this, "heromsg")

    override fun desc(): String = Messages.get(this, "desc", dispTurns())
}