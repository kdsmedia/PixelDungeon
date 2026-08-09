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
package com.altomedia.pixeldungeon

import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.armor.Armor
import com.altomedia.pixeldungeon.items.armor.ClassArmor
import com.altomedia.pixeldungeon.items.armor.ClothArmor
import com.altomedia.pixeldungeon.items.artifacts.HornOfPlenty
import com.altomedia.pixeldungeon.items.food.Blandfruit
import com.altomedia.pixeldungeon.items.food.Food
import com.altomedia.pixeldungeon.items.food.OverpricedRation
import com.altomedia.pixeldungeon.items.potions.PotionOfHealing
import com.altomedia.pixeldungeon.items.unclassified.Dewdrop

object Challenges {

    const val NO_FOOD = 1 shl 0
    const val NO_ARMOR = 1 shl 1
    const val NO_HEALING = 1 shl 2
    const val NO_HERBALISM = 1 shl 3
    const val SWARM_INTELLIGENCE = 1 shl 4
    const val THE_LONG_NIGHT = 1 shl 5
    const val NO_SCROLLS = 1 shl 6

    const val MAX_VALUE = (1 shl 7) - 1

    val NAME_IDS = arrayOf("no_food", "no_armor", "no_healing",
            "no_herbalism", "swarm_intelligence", "the-long-night",
            "no_scrolls")

    val MASKS = intArrayOf(NO_FOOD, NO_ARMOR, NO_HEALING,
            NO_HERBALISM, SWARM_INTELLIGENCE, THE_LONG_NIGHT,
            NO_SCROLLS)

    fun isForbidden(item: Item): Boolean {
        if (Dungeon.isChallenged(NO_FOOD))
            if (item is Food && item !is OverpricedRation)
                return true
            else if (item is HornOfPlenty)
                return true

        if (Dungeon.isChallenged(NO_ARMOR))
            if (item is Armor && !(item is ClothArmor || item is ClassArmor))
                return true

        if (Dungeon.isChallenged(NO_HEALING))
            if (item is PotionOfHealing) return true
            else if (item is Blandfruit && item.potionAttrib is PotionOfHealing) return true

        if (Dungeon.isChallenged(NO_HERBALISM))
            if (item is Dewdrop) return true

        return false
    }

}