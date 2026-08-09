/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.altomedia.pixeldungeon.actors.mobs

import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.PropertyConfiger
import com.altomedia.pixeldungeon.actors.Actor
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.buffs.Amok
import com.altomedia.pixeldungeon.actors.buffs.Terror
import com.altomedia.pixeldungeon.actors.mobs.npcs.Imp
import com.altomedia.pixeldungeon.items.food.Food
import com.altomedia.pixeldungeon.items.weapon.melee.Knuckles
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.sprites.MonkSprite
import com.altomedia.pixeldungeon.utils.GLog
import com.watabou.utils.Bundle
import com.watabou.utils.Random

import java.util.HashSet

open class Monk : Mob() {
    private var hitsToDisarm = 0

    init {
        spriteClass = MonkSprite::class.java

        PropertyConfiger.set(this, "Monk")
        loot = Food()
    }

    override fun giveDamage(target: Char): Damage {
        val dmg = super.giveDamage(target)
        if (dmg.value > 20) dmg.addFeature(Damage.Feature.CRITICAL)
        return dmg
    }

    override fun attackDelay(): Float = 0.45f

    override fun die(cause: Any?) {
        Imp.Quest.process(this)

        super.die(cause)
    }

    override fun attackProc(damage: Damage): Damage {
        if (damage.to === Dungeon.hero) {

            val hero = Dungeon.hero
            val weapon = hero.belongings.weapon

            if (weapon != null && weapon !is Knuckles && !weapon.cursed) {
                if (hitsToDisarm == 0) hitsToDisarm = Random.NormalIntRange(3, 7)

                if (--hitsToDisarm == 0) {
                    hero.belongings.weapon = null
                    Dungeon.quickslot.clearItem(weapon)
                    weapon.updateQuickslot()

                    val ops = hero.pos + (hero.pos - pos)
                    val dst = if (Level.passable[ops] && Actor.findChar(ops) == null) ops else hero.pos

                    Dungeon.level.drop(weapon, dst).sprite.drop()
                    GLog.w(Messages.get(this, "disarm", weapon.name()))
                }
            }
        }

        return damage
    }

    override fun immunizedBuffs(): HashSet<Class<*>> = IMMUNITIES

    override fun storeInBundle(bundle: Bundle) {
        super.storeInBundle(bundle)
        bundle.put(DISARMHITS, hitsToDisarm)
    }

    override fun restoreFromBundle(bundle: Bundle) {
        super.restoreFromBundle(bundle)
        hitsToDisarm = bundle.getInt(DISARMHITS)
    }

    companion object {

        private val IMMUNITIES = hashSetOf<Class<*>>(Amok::class.java, Terror::class.java)

        private const val DISARMHITS = "hitsToDisarm"
    }
}
