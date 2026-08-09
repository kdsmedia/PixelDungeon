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
package com.altomedia.pixeldungeon.plants

import com.altomedia.pixeldungeon.actors.Actor
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.buffs.*
import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.actors.mobs.Mob
import com.altomedia.pixeldungeon.items.potions.PotionOfPurity
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.altomedia.pixeldungeon.utils.GLog
import com.altomedia.pixeldungeon.actors.buffs.Bleeding
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.actors.buffs.Cripple
import com.altomedia.pixeldungeon.actors.buffs.Drowsy
import com.altomedia.pixeldungeon.actors.buffs.MagicalSleep
import com.altomedia.pixeldungeon.actors.buffs.Poison
import com.altomedia.pixeldungeon.actors.buffs.Slow
import com.altomedia.pixeldungeon.actors.buffs.Vertigo
import com.altomedia.pixeldungeon.actors.buffs.Weakness

class Dreamfoil : Plant(10) {

    override fun activate() {
        Actor.findChar(pos)?.let {
            when (it) {
                is Mob -> Buff.affect(it, MagicalSleep::class.java)
                is Hero -> {
                    GLog.i(Messages.get(this, "refreshed"))
                    for (buffClass in buffs) Buff.detach(it, buffClass)
                }
                else->{}
            }
        }
    }

    class Seed : Plant.Seed(Dreamfoil::class.java, PotionOfPurity::class.java) {
        init {
            image = ItemSpriteSheet.SEED_DREAMFOIL
        }
    }

    companion object {
        private val buffs = setOf(Poison::class.java,
                Cripple::class.java,
                Weakness::class.java,
                Bleeding::class.java,
                Drowsy::class.java,
                Slow::class.java,
                Vertigo::class.java,
                Vulnerable::class.java,
                Drunk::class.java)
    }
}