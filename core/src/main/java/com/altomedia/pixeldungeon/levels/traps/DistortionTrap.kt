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
package com.altomedia.pixeldungeon.levels.traps

import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.hero.Belongings
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.artifacts.LloydsBeacon
import com.altomedia.pixeldungeon.scenes.InterlevelScene
import com.altomedia.pixeldungeon.sprites.TrapSprite
import com.watabou.noosa.Game

class DistortionTrap : Trap() {

    init {
        color = TrapSprite.TEAL
        shape = TrapSprite.LARGE_DOT
    }

    override fun activate() {
        InterlevelScene.returnDepth = Dungeon.depth

        with(Dungeon.hero.belongings) {
            ironKeys[Dungeon.depth] = 0
            specialKeys[Dungeon.depth] = 0
            getItem(LloydsBeacon::class.java)?.let {
                if (it.returnDepth == Dungeon.depth)
                    it.returnDepth = -1
            }
        }
        
        InterlevelScene.mode = InterlevelScene.Mode.RESET
        Game.switchScene(InterlevelScene::class.java)
    }
}
