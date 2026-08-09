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

import com.altomedia.pixeldungeon.items.potions.PotionOfFrost
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.blobs.Fire
import com.altomedia.pixeldungeon.actors.blobs.Freezing
import com.altomedia.pixeldungeon.utils.BArray
import com.watabou.utils.PathFinder

class Icecap : Plant(1) {
    
    override fun activate() {

        PathFinder.buildDistanceMap(pos, BArray.not(Level.losBlocking, null), 1)

        val fire = Dungeon.level.blobs[Fire::class.java] as Fire?

        for (i in PathFinder.distance.indices)
            if (PathFinder.distance[i] < Integer.MAX_VALUE)
                Freezing.affect(i, fire)
    }

    class Seed : Plant.Seed(plantClass = Icecap::class.java,
            alchemyClass = PotionOfFrost::class.java) {
        init {
            image = ItemSpriteSheet.SEED_ICECAP


        }
    }
}
