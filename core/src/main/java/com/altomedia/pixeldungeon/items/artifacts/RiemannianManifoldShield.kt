package com.altomedia.pixeldungeon.items.artifacts

import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Actor
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.actors.buffs.ResistAny
import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.altomedia.pixeldungeon.utils.GLog
import com.watabou.utils.Bundle
import com.watabou.utils.Random

/**
 * Created by 93942 on 9/4/2018.
 */

//* buff used in Hero::resistDamage
class RiemannianManifoldShield : Artifact() {
    init {
        image = ItemSpriteSheet.RIEMANNIAN_SHIELD

        levelCap = 10
        exp = 0
        cooldown = 1

        defaultAction = "NONE" // to put into quick slot
    }

    // called when ResistAny detached
    fun recharge() {
        // curUser is assigned in the execute method!!!
        if (isEquipped(Dungeon.hero)) {
            if (level() <= 5)
                cooldown = 45 - level() * 3
            else
                cooldown = 30 - (level() - 5) * 3
        }
    }

    // more likely to be cursed.
    override fun random(): Item = this.apply { cursed = Random.Float() < 0.7f }

    override fun doUnequip(hero: Hero, collect: Boolean, single: Boolean): Boolean {
        GLog.w(Messages.get(this, "unequipped"))
        recharge()

        return super.doUnequip(hero, collect, single)
    }

    // recharge buff
    override fun passiveBuff(): ArtifactBuff = Recharge()

    inner class Recharge : Artifact.ArtifactBuff() {
        override fun act(): Boolean {
            if (cursed) {
                spend(Actor.TICK)
                return true
            }

            if (--cooldown == 0 && Dungeon.hero.buff(ResistAny::class.java) == null) {
                Buff.affect(Dungeon.hero, ResistAny::class.java).set(1)

                exp += 1
                // check upgrade
                val requireExp = level() * level() + 1
                if (exp > requireExp && level() < levelCap) {
                    exp -= requireExp
                    upgrade()
                    GLog.p(Messages.get(RiemannianManifoldShield::class.java, "levelup"))
                }
            }
            updateQuickslot()

            spend(Actor.TICK)
            return true
        }
    }

    override fun status(): String? = if (cooldown > 0) "$cooldown" else null

    override fun desc(): String {
        var desc = super.desc()

        if (isIdentified && cursed)
            desc += "\n\n" + Messages.get(this, "desc_cursed")

        return desc
    }
}
