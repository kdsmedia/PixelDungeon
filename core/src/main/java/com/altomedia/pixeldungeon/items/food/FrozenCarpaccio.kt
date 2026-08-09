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

import com.altomedia.pixeldungeon.actors.buffs.*
import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.actors.buffs.Barkskin
import com.altomedia.pixeldungeon.actors.buffs.Bleeding
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.actors.buffs.Cripple
import com.altomedia.pixeldungeon.actors.buffs.Drowsy
import com.altomedia.pixeldungeon.actors.buffs.Hunger
import com.altomedia.pixeldungeon.actors.buffs.Invisibility
import com.altomedia.pixeldungeon.actors.buffs.Poison
import com.altomedia.pixeldungeon.actors.buffs.Slow
import com.altomedia.pixeldungeon.actors.buffs.Vertigo
import com.altomedia.pixeldungeon.actors.buffs.Weakness
import com.altomedia.pixeldungeon.effects.Speck
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.altomedia.pixeldungeon.utils.GLog
import com.watabou.utils.Random

class FrozenCarpaccio : Food(Hunger.STARVING - Hunger.HUNGRY, 1) {
    init {
        image = ItemSpriteSheet.CARPACCIO
    }

    override fun execute(hero: Hero, action: String) {
        super.execute(hero, action)

        if (action == AC_EAT) effect(hero)
    }

    override fun price(): Int = 10 * quantity

    companion object {

        fun effect(hero: Hero) {
            when (Random.Int(5)) {
                0 -> {
                    GLog.i(Messages.get(FrozenCarpaccio::class.java, "invis"))
                    Buff.affect(hero, Invisibility::class.java, Invisibility.DURATION)
                }
                1 -> {
                    GLog.i(Messages.get(FrozenCarpaccio::class.java, "hard"))
                    Buff.affect(hero, Barkskin::class.java).level(hero.HT / 4)
                }
                2 -> {
                    GLog.i(Messages.get(FrozenCarpaccio::class.java, "refresh"))
                    Buff.detach(hero, Poison::class.java)
                    Buff.detach(hero, Cripple::class.java)
                    Buff.detach(hero, Weakness::class.java)
                    Buff.detach(hero, Bleeding::class.java)
                    Buff.detach(hero, Drowsy::class.java)
                    Buff.detach(hero, Slow::class.java)
                    Buff.detach(hero, Vertigo::class.java)
                }
                3 -> {
                    GLog.i(Messages.get(FrozenCarpaccio::class.java, "better"))
                    if (hero.HP < hero.HT) {
                        hero.HP = Math.min(hero.HP + hero.HT / 4, hero.HT)
                        hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1)
                    }
                }
            }
        }

        fun cook(ingredient: MysteryMeat): Food = FrozenCarpaccio().apply { quantity = ingredient.quantity() }
    }
}
