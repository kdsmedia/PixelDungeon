package com.altomedia.pixeldungeon.actors.buffs

import com.altomedia.pixeldungeon.actors.Actor
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.messages.M
import com.altomedia.pixeldungeon.ui.BuffIndicator
import com.watabou.utils.Bundle

class SeeThrough(var enemyid: Int = 0) : FlavourBuff() {
    init {
        type = buffType.POSITIVE
    }

    override fun icon(): Int = BuffIndicator.MARK

    override fun toString(): String = M.L(this, "name")

    override fun desc(): String = M.L(this, "desc")

    override fun storeInBundle(bundle: Bundle) {
        super.storeInBundle(bundle)
        bundle.put(ENEMY_ID, enemyid)

    }

    override fun restoreFromBundle(bundle: Bundle) {
        super.restoreFromBundle(bundle)
        enemyid = bundle.getInt(ENEMY_ID)
    }

    fun processDamage(dmg: Damage) {
        if ((dmg.to as Actor).id() == enemyid) dmg.addFeature(Damage.Feature.PURE or Damage.Feature.ACCURATE)
        detach()
    }

    companion object {
        private const val ENEMY_ID = "enemy"
    }
}