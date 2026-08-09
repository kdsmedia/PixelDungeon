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
package com.altomedia.pixeldungeon.items.wands

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Actor
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.actors.blobs.Blob
import com.altomedia.pixeldungeon.actors.blobs.VenomGas
import com.altomedia.pixeldungeon.effects.MagicMissile
import com.altomedia.pixeldungeon.items.weapon.enchantments.Venomous
import com.altomedia.pixeldungeon.items.weapon.melee.MagesStaff
import com.altomedia.pixeldungeon.mechanics.Ballistica
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.watabou.noosa.audio.Sample
import com.watabou.utils.Callback
import com.watabou.utils.PathFinder

class WandOfVenom : DamageWand() {
    init {
        image = ItemSpriteSheet.WAND_VENOM

        collisionProperties = Ballistica.STOP_TARGET or Ballistica.STOP_TERRAIN
    }

    override fun min(lvl: Int): Int = 1 + lvl

    override fun max(lvl: Int): Int = 6 + 2 * lvl

    override fun giveDamage(enemy: Char): Damage = super.giveDamage(enemy).addElement(Damage.Element.POISON)

    override fun onZap(bolt: Ballistica) {
        Actor.findChar(bolt.collisionPos)?.let {
            val dmg = giveDamage(it)
            onMissileHit(it, Dungeon.hero, dmg)
            it.takeDamage(dmg)

            it.sprite.burst(0x8844ff, level() / 2 + 2)
        }

        val venomGas = Blob.seed(bolt.collisionPos, 40 + 10 * level(), VenomGas::class.java)
        (venomGas as VenomGas).setStrength(level() + 1)
        GameScene.add(venomGas)
    }

    override fun fx(bolt: Ballistica, callback: Callback) {
        MagicMissile.poison(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback)
        Sample.INSTANCE.play(Assets.SND_ZAP)
    }

    override fun onHit(staff: MagesStaff, damage: Damage) {
        //acts like venomous enchantment
        Venomous().proc(staff, damage)
    }

    override fun particleColor(): Int = 0x8844FF

    override fun staffFx(particle: MagesStaff.StaffParticle) {
        particle.color(particleColor())
        particle.am = 0.6f
        particle.setLifespan(0.6f)
        particle.acc.set(0f, 40f)
        particle.setSize(0f, 3f)
        particle.shuffleXY(2f)
    }

}
