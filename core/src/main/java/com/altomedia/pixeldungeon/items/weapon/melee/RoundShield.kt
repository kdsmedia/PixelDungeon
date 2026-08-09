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
package com.altomedia.pixeldungeon.items.weapon.melee

import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.watabou.utils.Random
import kotlin.math.round

class RoundShield : Shield() {
    init {
        image = ItemSpriteSheet.ROUND_SHIELD

        tier = 3
    }

    //12 base, down from 20, +2 per level, down from +4
    override fun max(lvl: Int): Int = 3 * (tier + 1) + lvl * (tier - 1)

    // base: 4
    override fun def(level: Int): Int = 4 + 2 * level
}