package com.altomedia.pixeldungeon.items.weapon.melee

import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.items.wands.WandOfBlastWave
import com.altomedia.pixeldungeon.mechanics.Ballistica
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.watabou.utils.Random

class DaggerAxe : MeleeWeapon() {
    init {
        image = ItemSpriteSheet.DAGGER_AXE

        tier = 3
        DLY = 1.5f
        RCH = 2
    }

    // like spear
    override fun max(lvl: Int): Int = Math.round((6.67f + 1.33f * lvl) * (tier + 1))

    override fun proc(dmg: Damage): Damage {
        // chance to knock back
        val chance = 0.1f + 0.25 * (1f - Math.pow(.7, level() / 3.0).toFloat())
        if (dmg.to is Char && Random.Float() < chance) {
            val tgt = dmg.to as Char
            val opposite = tgt.pos + (tgt.pos - (dmg.from as Char).pos)
            val shot = Ballistica(tgt.pos, opposite, Ballistica.MAGIC_BOLT)

            WandOfBlastWave.throwChar(tgt, shot, 1)
        }

        return super.proc(dmg)
    }

    override fun accuracyFactor(hero: Hero, target: Char): Float =
            if (Dungeon.level.adjacent(hero.pos, target.pos)) 0.4f
            else 1f
}