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

import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.sprites.BatSprite
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.PropertyConfiger
import com.altomedia.pixeldungeon.Statistics
import com.altomedia.pixeldungeon.effects.Speck
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.potions.PotionOfHealing
import com.watabou.utils.Random

class Bat : Mob() {

    init {
        PropertyConfiger.set(this, "Bat")

        spriteClass = BatSprite::class.java
        loot = PotionOfHealing()

        baseSpeed = 2f
        flying = true
    }

    override fun viewDistance(): Int = seeDistance()

    override fun giveDamage(target: Char): Damage {
        return if (Random.Int(4) == 0)
            Damage(Random.NormalIntRange(1, 5), this, target).type(Damage.Type.MENTAL)
        else {
            val dmg = super.giveDamage(target).addElement(Damage.Element.SHADOW)
            if (Statistics.Clock.state != Statistics.ClockTime.State.Day)
                dmg.value += dmg.value / 4

            dmg
        }
    }

    override fun attackProc(damage: Damage): Damage {
        if (damage.type != Damage.Type.MENTAL) {
            val reg = Math.min(damage.value, HT - HP) / 3

            if (reg > 0) {
                HP += reg
                sprite.emitter().burst(Speck.factory(Speck.HEALING), 1)
            }
        }

        return damage
    }

    override fun die(cause: Any?) {
        //sets drop chance
        lootChance = 1f / (8 + Dungeon.limitedDrops.batHP.count)
        super.die(cause)
    }

    override fun createLoot(): Item? {
        Dungeon.limitedDrops.batHP.count++
        return super.createLoot()
    }

}
