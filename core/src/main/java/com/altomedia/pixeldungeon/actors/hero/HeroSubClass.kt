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
package com.altomedia.pixeldungeon.actors.hero

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.buffs.Berserk
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.actors.hero.perks.Assassin
import com.altomedia.pixeldungeon.actors.hero.perks.Fearless
import com.altomedia.pixeldungeon.actors.hero.perks.Optimistic
import com.altomedia.pixeldungeon.effects.Speck
import com.altomedia.pixeldungeon.effects.SpellSprite
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.artifacts.Astrolabe
import com.altomedia.pixeldungeon.items.artifacts.UrnOfShadow
import com.altomedia.pixeldungeon.items.unclassified.ExtractionFlask
import com.altomedia.pixeldungeon.items.unclassified.TomeOfMastery
import com.altomedia.pixeldungeon.messages.M
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.utils.GLog
import com.watabou.noosa.audio.Sample
import com.watabou.utils.Bundle

enum class HeroSubClass(private val title: String) {
    NONE(""),

    GLADIATOR("gladiator"),
    BERSERKER("berserker"),

    WARLOCK("warlock"),
    BATTLEMAGE("battlemage"),

    ASSASSIN("assassin"),
    FREERUNNER("freerunner"),

    SNIPER("sniper"),
    WARDEN("warden"),

    STARGAZER("stargazer"),
    WITCH("witch");

    fun title(): String = Messages.get(this, title)

    fun desc(): String = Messages.get(this, title + "_desc")

    fun storeInBundle(bundle: Bundle) {
        bundle.put(SUBCLASS, toString())
    }

    companion object {

        private const val SUBCLASS = "subClass"

        fun RestoreFromBundle(bundle: Bundle): HeroSubClass = valueOf(bundle.getString(SUBCLASS))

        fun Choose(hero: Hero, way: HeroSubClass) {
            hero.subClass = way

            Sample.INSTANCE.play(Assets.SND_MASTERY)
            SpellSprite.show(hero, SpellSprite.MASTERY)
            hero.sprite.emitter().burst(Speck.factory(Speck.MASTERY), 12)

            GLog.w(M.L(TomeOfMastery::class.java, "way", way.title()))

            // on choose
            when (way) {
                BERSERKER -> {
                    Buff.affect(hero, Berserk::class.java)
                    hero.heroPerk.add(Fearless())
                }
                ASSASSIN -> hero.heroPerk.add(Assassin())
                WARLOCK -> {
                    val uos = UrnOfShadow().identify()
                    if (uos.doPickUp(hero)) GLog.w(Messages.get(hero, "you_now_have", uos.name()))
                    else Dungeon.level.drop(uos, hero.pos).sprite.drop()
                }
                WITCH -> {
                    hero.belongings.getItem(ExtractionFlask::class.java)?.reinforce()
                    //^ may lose perk
                }
                STARGAZER -> {
                    hero.heroPerk.add(Optimistic())

                    val a = Astrolabe().identify()
                    if (a.doPickUp(hero)) GLog.w(Messages.get(hero, "you_now_have", a.name()))
                    else Dungeon.level.drop(a, hero.pos).sprite.drop()
                }
                else -> {}
            }
        }
    }

}
