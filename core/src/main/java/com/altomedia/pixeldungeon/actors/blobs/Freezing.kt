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
package com.altomedia.pixeldungeon.actors.blobs

import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Actor
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.actors.buffs.Frost
import com.altomedia.pixeldungeon.effects.CellEmitter
import com.altomedia.pixeldungeon.effects.particles.SnowParticle
import com.altomedia.pixeldungeon.items.Heap
import com.altomedia.pixeldungeon.levels.Level
import com.watabou.utils.Random

object Freezing {

    // Returns true, if this cell is visible
    fun affect(cell: Int, fire: Fire?): Boolean {

        Actor.findChar(cell)?.let {
            val duration = Frost.duration(it)* if(Level.water[it.pos]) Random.Float(5f, 7.5f) else Random.Float(1f, 1.5f)
            Buff.prolong(it, Frost::class.java, duration)
        }

        fire?.clear(cell)

        Dungeon.level.heaps.get(cell)?.freeze()

        return if (Dungeon.visible[cell]) {
            CellEmitter.get(cell).start(SnowParticle.FACTORY, 0.2f, 6)
            true
        } else false
    }
}
