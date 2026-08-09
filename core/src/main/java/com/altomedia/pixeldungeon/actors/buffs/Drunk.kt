package com.altomedia.pixeldungeon.actors.buffs

import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.items.rings.RingOfElements
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.ui.BuffIndicator

class Drunk : Vertigo() {
    override fun icon(): Int = BuffIndicator.DRUNK

    override fun toString(): String = Messages.get(this, "name")

    override fun desc(): String = Messages.get(this, "desc", dispTurns())

    companion object {
        private const val BASE_DURATION = 30f
        
        fun duration(ch: Char): Float {
            val r = ch.buff(RingOfElements.Resistance::class.java)
            return if (r == null) BASE_DURATION else r.durationFactor() * BASE_DURATION
        }

        fun procOutcomingDamage(dmg: Damage): Damage {
            dmg.value = (dmg.value * 1.2f).toInt()
            return dmg
        }
    }
}