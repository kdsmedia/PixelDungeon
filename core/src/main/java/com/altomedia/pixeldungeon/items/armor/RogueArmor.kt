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

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Actor
import com.altomedia.pixeldungeon.actors.buffs.Blindness
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.actors.mobs.Mob
import com.altomedia.pixeldungeon.effects.CellEmitter
import com.altomedia.pixeldungeon.effects.Speck
import com.altomedia.pixeldungeon.items.scrolls.ScrollOfTeleportation
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.messages.M
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.scenes.CellSelector
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.altomedia.pixeldungeon.utils.GLog
import com.watabou.noosa.audio.Sample

class RogueArmor : ClassArmor() {
    init {
        image = ItemSpriteSheet.ARMOR_ROGUE
    }

    override fun doSpecial() {
        GameScene.selectCell(teleporter)
    }

    companion object {
        private val teleporter: CellSelector.Listener = object : CellSelector.Listener {

            override fun onSelect(target: Int?) {
                if (target != null) {

                    if (!Level.fieldOfView[target] ||
                            !(Level.passable[target] || Level.avoid[target]) ||
                            Actor.findChar(target) != null) {

                        GLog.w(M.L(RogueArmor::class.java, "fov"))
                        return
                    }

                    curUser.HP -= curUser.HP / 3

                    Dungeon.level.mobs.filter { Level.fieldOfView[it.pos] }.forEach {
                        Buff.prolong(it, Blindness::class.java, 2f)
                        if (it.state === it.HUNTING) it.state = it.WANDERING
                        it.sprite.emitter().burst(Speck.factory(Speck.LIGHT), 4)
                    }

                    // use a teleportation scroll
                    ScrollOfTeleportation.appear(curUser, target)
                    CellEmitter.get(target).burst(Speck.factory(Speck.WOOL), 10)
                    Sample.INSTANCE.play(Assets.SND_PUFF)
                    Dungeon.level.press(target, curUser)
                    Dungeon.observe()
                    GameScene.updateFog()

                    curUser.spendAndNext(Actor.TICK)
                }
            }

            override fun prompt(): String = M.L(RogueArmor::class.java, "prompt")
        }
    }
}