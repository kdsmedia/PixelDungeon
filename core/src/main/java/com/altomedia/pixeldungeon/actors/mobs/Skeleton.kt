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

import com.altomedia.pixeldungeon.actors.Damage

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.PropertyConfiger
import com.altomedia.pixeldungeon.actors.Actor
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.Generator
import com.altomedia.pixeldungeon.items.artifacts.HandOfTheElder
import com.altomedia.pixeldungeon.items.weapon.melee.MeleeWeapon
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.sprites.SkeletonSprite
import com.altomedia.pixeldungeon.utils.GLog
import com.watabou.noosa.audio.Sample
import com.watabou.utils.PathFinder
import com.watabou.utils.Random

class Skeleton : Mob() {
    init {
        PropertyConfiger.set(this, "Skeleton")

        spriteClass = SkeletonSprite::class.java
        loot = Generator.WEAPON.generate()
    }


    override fun die(cause: Any?) {
        super.die(cause)

        var heroKilled = false
        for (i in PathFinder.NEIGHBOURS8) {
            Actor.findChar(i + pos)?.let {
                if (it.isAlive) {
                    val dmg = Damage(Random.NormalIntRange(4, 10), this@Skeleton, it).addElement(Damage.Element.FIRE)
                    it.takeDamage(it.defendDamage(dmg))
                    if (it === Dungeon.hero && !it.isAlive)
                        heroKilled = true
                }
            }
        }

        if (Dungeon.visible[pos]) Sample.INSTANCE.play(Assets.SND_BONES)

        if (heroKilled) {
            Dungeon.fail(javaClass)
            GLog.n(Messages.get(this, "explo_kill"))
        }
    }

    override fun createLoot(): Item? {
        return if (!Dungeon.limitedDrops.handOfElder.dropped() && Random.Float() < 0.04f) {
            Dungeon.limitedDrops.handOfElder.drop()
            HandOfTheElder().random()
        } else {
            var loot: Item
            do {
                loot = Generator.WEAPON.generate()
                //50% chance of re-rolling tier 4 or 5 items
            } while (loot is MeleeWeapon && loot.tier >= 4 &&
                    Random.Int(2) == 0)
            loot.level(0)

            loot
        }
    }
}
