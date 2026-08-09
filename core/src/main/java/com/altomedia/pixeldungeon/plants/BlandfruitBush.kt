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
import com.altomedia.pixeldungeon.items.food.Blandfruit
import com.altomedia.pixeldungeon.items.potions.PotionOfInvisibility
import com.altomedia.pixeldungeon.items.potions.PotionOfPhysique
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet

class BlandfruitBush : Plant(8) {

    override fun activate() {
        Dungeon.level.drop(Blandfruit(), pos).sprite.drop()
    }

    class Seed : Plant.Seed(plantClass = BlandfruitBush::class.java, 
            alchemyClass = PotionOfPhysique::class.java) {
        init {
            image = ItemSpriteSheet.SEED_BLANDFRUIT
        }

        override fun price(): Int = 20* quantity
    }
}
