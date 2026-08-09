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

import com.altomedia.pixeldungeon.effects.CellEmitter
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Actor
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.actors.mobs.Mob
import com.altomedia.pixeldungeon.effects.Speck
import com.altomedia.pixeldungeon.items.potions.PotionOfMindVision
import com.altomedia.pixeldungeon.items.scrolls.ScrollOfTeleportation
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet

class Fadeleaf : Plant(6) {

    override fun activate() {
        val ch = Actor.findChar(pos)

        if (ch is Hero) {
            ScrollOfTeleportation.teleportHero(ch)
            ch.curAction = null
        } else if (ch is Mob && !ch.properties().contains(Char.Property.IMMOVABLE) && !Dungeon.bossLevel()) {
            var count = 10
            var newPos: Int
            do {
                newPos = Dungeon.level.randomRespawnCell()
                if (count-- <= 0) {
                    break
                }
            } while (newPos == -1)

            if (newPos != -1) {
                ch.pos = newPos
                if (ch.state === ch.HUNTING) ch.state = ch.WANDERING
                ch.sprite.place(ch.pos)
                ch.sprite.visible = Dungeon.visible[ch.pos]
            }
        }

        if (Dungeon.visible[pos])
            CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3)
    }

    class Seed : Plant.Seed(Fadeleaf::class.java, PotionOfMindVision::class.java) {
        init {
            image = ItemSpriteSheet.SEED_FADELEAF
        }
    }
}
