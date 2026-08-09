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
package com.altomedia.pixeldungeon.items.potions

import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.actors.buffs.MindVision
import com.altomedia.pixeldungeon.actors.buffs.SharpVision
import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.messages.M
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.utils.GLog

class PotionOfMindVision : Potion() {

    init {
        initials = 7
    }

    override fun canBeReinforced(): Boolean = !reinforced

    override fun apply(hero: Hero) {
        setKnown()
        Buff.affect(hero, MindVision::class.java, MindVision.DURATION)
        Dungeon.observe()

        if (Dungeon.level.mobs.size > 0) GLog.i(M.L(this, "see_mobs"))
        else GLog.i(M.L(this, "see_none"))

        if (reinforced) Buff.prolong(hero, SharpVision::class.java, SharpVision.DURATION)
    }

    override fun price(): Int = if (isKnown) 30 * (quantity * if (reinforced) 1.5f else 1f).toInt()
    else super.price()
}
