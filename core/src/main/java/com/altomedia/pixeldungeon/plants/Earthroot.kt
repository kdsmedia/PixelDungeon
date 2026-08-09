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

import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Actor
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.effects.CellEmitter
import com.altomedia.pixeldungeon.items.potions.PotionOfParalyticGas
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.altomedia.pixeldungeon.effects.particles.EarthParticle
import com.altomedia.pixeldungeon.ui.BuffIndicator
import com.watabou.noosa.Camera
import com.watabou.utils.Bundle

class Earthroot : Plant(5) {
    override fun activate() {
        val ch = Actor.findChar(pos)

        if (ch === Dungeon.hero) {
            Buff.affect(ch!!, Armor::class.java).level(ch.HT)
        }

        if (Dungeon.visible[pos]) {
            CellEmitter.bottom(pos).start(EarthParticle.FACTORY, 0.05f, 8)
            Camera.main.shake(1f, 0.4f)
        }
    }

    class Seed : Plant.Seed(Earthroot::class.java, PotionOfParalyticGas::class.java) {
        init {
            image = ItemSpriteSheet.SEED_EARTHROOT

            bones = true
        }
    }

    class Armor : Buff() {
        private var pos: Int = 0
        private var level: Int = 0

        init {
            type = buffType.POSITIVE
        }

        override fun attachTo(target: Char): Boolean {
            pos = target.pos
            return super.attachTo(target)
        }

        override fun act(): Boolean {
            if (target.pos != pos) {
                detach()
            }
            spend(STEP)
            return true
        }

        fun procTakenDamage(dmg: Damage) {
            dmg.value = absorb(dmg.value)
        }

        private fun absorb(damage: Int): Int = if (level <= damage - damage / 2) {
            detach()
            damage - level
        } else {
            level -= damage - damage / 2
            damage / 2
        }

        fun level(value: Int) {
            if (level < value) {
                level = value
            }
            pos = target.pos
        }

        override fun icon(): Int = BuffIndicator.ARMOR

        override fun toString(): String = Messages.get(this, "name")

        override fun desc(): String = Messages.get(this, "desc", level)

        override fun storeInBundle(bundle: Bundle) {
            super.storeInBundle(bundle)
            bundle.put(POS, pos)
            bundle.put(LEVEL, level)
        }

        override fun restoreFromBundle(bundle: Bundle) {
            super.restoreFromBundle(bundle)
            pos = bundle.getInt(POS)
            level = bundle.getInt(LEVEL)
        }

        companion object {
            private const val STEP = 1f

            private const val POS = "pos"
            private const val LEVEL = "level"
        }
    }
}
