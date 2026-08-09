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
package com.altomedia.pixeldungeon.items.armor

import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.actors.buffs.Burning
import com.altomedia.pixeldungeon.effects.particles.ElmoParticle
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Actor
import com.altomedia.pixeldungeon.actors.buffs.FlavourBuff
import com.altomedia.pixeldungeon.actors.buffs.Roots
import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.unclassified.GreatBlueprint
import com.altomedia.pixeldungeon.mechanics.Ballistica
import com.altomedia.pixeldungeon.messages.M
import com.altomedia.pixeldungeon.scenes.CellSelector
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.utils.GLog
import com.watabou.noosa.audio.Sample
import com.watabou.utils.Bundle
import com.watabou.utils.Random
import java.util.ArrayList

class MageArmor : ClassArmor(), GreatBlueprint.Enchantable {
    init {
        image = ItemSpriteSheet.ARMOR_MAGE
    }

    var enhanced = false

    override fun actions(hero: Hero): ArrayList<String> {
        val actions = super.actions(hero)
        if (enhanced && hero.buff(FlyCoolDown::class.java) == null) actions.add(AC_FLY)
        return actions
    }

    override fun execute(hero: Hero, action: String) {
        super.execute(hero, action)
        if (action == AC_FLY) {
            GameScene.selectCell(jumper)
        }
    }

    override fun doSpecial() {
        Dungeon.level.mobs.filter { Level.fieldOfView[it.pos] }.forEach {
            Buff.affect(it, Burning::class.java).reignite(it)
            Buff.prolong(it, Roots::class.java, 3f)
        }

        Item.curUser.apply {
            HP -= HP / 3

            spend(Actor.TICK)
            sprite.operate(pos)
            busy()
        }

        Item.curUser.sprite.centerEmitter().start(ElmoParticle.FACTORY, 0.15f, 4)
        Sample.INSTANCE.play(Assets.SND_READ)
    }

    override fun enchantByBlueprint() {
        enhanced = true
        image = ItemSpriteSheet.ARMOR_MAGE_ENHANCED
    }

    override fun desc(): String {
        var desc = super.desc()
        if (enhanced) desc += "\n\n" + M.L(this, "enhanced_desc")
        return desc
    }

    private val jumper = object : CellSelector.Listener {
        override fun onSelect(cell: Int?) {
            if (cell != null && cell != Item.curUser.pos) {
                val dst = Ballistica(Item.curUser.pos, cell, Ballistica.PROJECTILE).collisionPos

                Buff.prolong(Item.curUser, FlyCoolDown::class.java, Random.Float(20f, 25f))
                Item.curUser.busy()
                Item.curUser.sprite.jump(Item.curUser.pos, dst) {
                    Item.curUser.move(dst)
                    Dungeon.level.press(dst, Item.curUser)
                    Dungeon.observe()
                    GameScene.updateFog()

                    Item.curUser.spendAndNext(1f)
                }
                Sample.INSTANCE.play(Assets.SND_PUFF)
            }
        }

        override fun prompt(): String = M.L(MageArmor::class.java, "prompt")
    }

    override fun storeInBundle(bundle: Bundle) {
        super.storeInBundle(bundle)
        bundle.put(ENHANCED, enhanced)
    }

    override fun restoreFromBundle(bundle: Bundle) {
        super.restoreFromBundle(bundle)
        enhanced = bundle.getBoolean(ENHANCED)
        if (enhanced) enchantByBlueprint()
    }

    companion object {
        private const val AC_FLY = "fly"
        private const val ENHANCED = "enhanced"
    }

    class FlyCoolDown : FlavourBuff() {
        override fun detach() {
            GLog.p(M.L(MageArmor::class.java, "fly_ready"))
            super.detach()
        }
    }
}