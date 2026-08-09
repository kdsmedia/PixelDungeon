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
package com.altomedia.pixeldungeon.items.quest

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Actor
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.actors.mobs.Mob
import com.altomedia.pixeldungeon.effects.CellEmitter
import com.altomedia.pixeldungeon.effects.particles.ElmoParticle
import com.altomedia.pixeldungeon.items.Heap
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.altomedia.pixeldungeon.actors.mobs.NewbornElemental
import com.altomedia.pixeldungeon.items.Item
import com.watabou.noosa.audio.Sample
import com.watabou.utils.PathFinder
import com.watabou.utils.Random

import java.util.ArrayList


class CeremonialCandle : Item() {
    init {
        image = ItemSpriteSheet.CANDLE

        defaultAction = AC_THROW

        unique = true
        stackable = true
    }

    override fun isUpgradable(): Boolean = false

    override fun isIdentified(): Boolean = true

    override fun doDrop(hero: Hero) {
        super.doDrop(hero)
        checkCandles()
    }

    override fun onThrow(cell: Int) {
        super.onThrow(cell)
        checkCandles()
    }

    companion object {
        //generated with the wandmaker quest
        var ritualPos: Int = 0

        private fun checkCandles() {
            val heaps = PathFinder.NEIGHBOURS4.map { ritualPos + it }.map { Dungeon.level.heaps.get(it) }
            if (heaps.all { it?.peek() is CeremonialCandle }) {
                // yes, summon
                heaps.forEach { it.pickUp() }
                val elemental = NewbornElemental()
                if (Actor.findChar(ritualPos) != null) {
                    val candidates = PathFinder.NEIGHBOURS8.map { ritualPos + it }.filter {
                        (Level.passable[it] || Level.avoid[it]) && Actor.findChar(it) == null
                    }
                    elemental.pos = if (candidates.isNotEmpty()) candidates.random() else ritualPos
                } else
                    elemental.pos = ritualPos
                elemental.state = elemental.HUNTING
                GameScene.add(elemental, 1f)
                PathFinder.NEIGHBOURS9.forEach {
                    CellEmitter.get(ritualPos + it).burst(ElmoParticle.FACTORY, 10)
                }
                Sample.INSTANCE.play(Assets.SND_BURNING)
            }
        }
    }
}
