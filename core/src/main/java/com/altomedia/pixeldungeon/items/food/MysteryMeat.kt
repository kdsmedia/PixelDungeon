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
package com.altomedia.pixeldungeon.items.food

import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.altomedia.pixeldungeon.utils.GLog
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.actors.buffs.Burning
import com.altomedia.pixeldungeon.actors.buffs.Hunger
import com.altomedia.pixeldungeon.actors.buffs.Paralysis
import com.altomedia.pixeldungeon.actors.buffs.Poison
import com.altomedia.pixeldungeon.actors.buffs.Roots
import com.altomedia.pixeldungeon.actors.buffs.Slow
import com.watabou.utils.Random

class MysteryMeat : Food(Hunger.STARVING - Hunger.HUNGRY, 1) {

    init {
        image = ItemSpriteSheet.MEAT
    }

    override fun execute(hero: Hero, action: String) {

        super.execute(hero, action)

        if (action == AC_EAT) {
            effect(hero)
        }
    }

    override fun price(): Int = 5 * quantity

    companion object {

        fun effect(hero: Hero) {
            when (Random.Int(5)) {
                0 -> {
                    GLog.w(Messages.get(MysteryMeat::class.java, "hot"))
                    Buff.affect(hero, Burning::class.java).reignite(hero)
                }
                1 -> {
                    GLog.w(Messages.get(MysteryMeat::class.java, "legs"))
                    Buff.prolong(hero, Roots::class.java, Paralysis.duration(hero))
                }
                2 -> {
                    GLog.w(Messages.get(MysteryMeat::class.java, "not_well"))
                    Buff.affect(hero, Poison::class.java).set(Poison.durationFactor(hero) * hero.HT / 5)
                }
                3 -> {
                    GLog.w(Messages.get(MysteryMeat::class.java, "stuffed"))
                    Buff.prolong(hero, Slow::class.java, Slow.duration(hero))
                }
            }
        }
    }
}
