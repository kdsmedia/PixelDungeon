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

import com.altomedia.pixeldungeon.PropertyConfiger
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.actors.buffs.Amok
import com.altomedia.pixeldungeon.actors.buffs.Sleep
import com.altomedia.pixeldungeon.actors.buffs.Terror
import com.altomedia.pixeldungeon.actors.mobs.npcs.Imp
import com.altomedia.pixeldungeon.sprites.GolemSprite
import com.watabou.utils.Random

import java.util.HashSet

class Golem : Mob() {
    init {
        PropertyConfiger.set(this, "Golem")

        spriteClass = GolemSprite::class.java
    }

    override fun attackDelay(): Float = 1.5f

    override fun die(cause: Any?) {
        Imp.Quest.process(this)

        super.die(cause)
    }

    override fun immunizedBuffs(): HashSet<Class<*>> = IMMUNITIES

    companion object {
        private val IMMUNITIES = hashSetOf<Class<*>>(Amok::class.java, Terror::class.java, Sleep::class.java)
    }
}
