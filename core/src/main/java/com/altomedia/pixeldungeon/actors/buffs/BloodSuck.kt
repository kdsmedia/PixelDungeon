package com.altomedia.pixeldungeon.actors.buffs

import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.mobs.Mob
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.sprites.CharSprite
import com.altomedia.pixeldungeon.ui.BuffIndicator
import com.watabou.utils.Random

// called in Hero::onKillChar
class BloodSuck : FlavourBuff() {
    init {
        type = buffType.POSITIVE
    }
    
    override fun icon(): Int = BuffIndicator.BLOOD_SUCK

    override fun toString(): String = Messages.get(this, "name")

    override fun desc(): String = Messages.get(this, "desc", dispTurns())

    fun onEnemySlayed(c: Char) {
        var d = Random.IntRange(1, 3)
        if (c is Mob && c.exp() > 0)
            d += Random.IntRange(1, 3)

        target.HT += d
        target.HP += d

        target.sprite.showStatus(CharSprite.WARNING, Messages.get(this, "nibble"))
    }
}