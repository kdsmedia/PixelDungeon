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
package com.altomedia.pixeldungeon.actors.mobs

import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.PropertyConfiger
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.actors.buffs.Chill
import com.altomedia.pixeldungeon.actors.buffs.Frost
import com.altomedia.pixeldungeon.items.quest.Embers
import com.altomedia.pixeldungeon.sprites.NewbornElementalSprite

class NewbornElemental : Elemental() {

    init {
        PropertyConfiger.set(this, "NewbornElemental")

        spriteClass = NewbornElementalSprite::class.java
        HP = HT / 2
    }

    override fun add(buff: Buff) {
        if (buff is Frost || buff is Chill) {
            die(buff)
        } else {
            super.add(buff)
        }
    }

    override fun die(cause: Any) {
        super.die(cause)
        Dungeon.level.drop(Embers(), pos).sprite.drop()
    }
}
