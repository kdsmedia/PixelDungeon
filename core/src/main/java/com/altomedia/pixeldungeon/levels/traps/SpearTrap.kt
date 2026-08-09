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
package com.altomedia.pixeldungeon.levels.traps

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Actor
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.effects.Wound
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.utils.GLog
import com.altomedia.pixeldungeon.sprites.TrapSprite
import com.watabou.noosa.audio.Sample
import com.watabou.utils.Random

class SpearTrap : Trap() {

    init {
        color = TrapSprite.GREY
        shape = TrapSprite.DOTS
    }

    override fun trigger() {
        if (Dungeon.visible[pos]) {
            Sample.INSTANCE.play(Assets.SND_TRAP)
        }
        //this trap is not disarmed by being triggered
        reveal()
        Level.set(pos, Terrain.TRAP)
        activate()
    }

    override fun activate() {
        if (Dungeon.visible[pos]) {
            Sample.INSTANCE.play(Assets.SND_HIT)
            Wound.hit(pos)
        }

        val ch = Actor.findChar(pos)
        if (ch != null && !ch.flying) {
            val damage = Random.NormalIntRange(Dungeon.depth, Dungeon.depth * 2)
            ch.takeDamage(ch.defendDamage(Damage(damage, this, ch)))
            if (!ch.isAlive && ch === Dungeon.hero) {
                Dungeon.fail(javaClass)
                GLog.n(Messages.get(this, "ondeath"))
            }
        }
    }
}
