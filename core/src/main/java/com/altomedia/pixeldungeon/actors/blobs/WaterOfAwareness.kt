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
package com.altomedia.pixeldungeon.actors.blobs

import com.altomedia.pixeldungeon.actors.buffs.Awareness
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.effects.BlobEmitter
import com.altomedia.pixeldungeon.effects.Identification
import com.altomedia.pixeldungeon.effects.Speck
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.utils.GLog
import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Badges
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.DungeonTilemap
import com.altomedia.pixeldungeon.Journal
import com.altomedia.pixeldungeon.items.Item
import com.watabou.noosa.audio.Sample

class WaterOfAwareness : WellWater() {

    override fun affectHero(hero: Hero): Boolean {

        Sample.INSTANCE.play(Assets.SND_DRINK)
        emitter.parent.add(Identification(DungeonTilemap.tileCenterToWorld(pos)))

        hero.belongings.observe()

        for (i in 0 until Dungeon.level.length()) {

            val terr = Dungeon.level.map[i]
            if (Terrain.flags[terr] and Terrain.SECRET != 0) {

                Dungeon.level.discover(i)

                if (Dungeon.visible[i]) {
                    GameScene.discoverTile(i, terr)
                }
            }
        }

        Buff.affect(hero, Awareness::class.java, Awareness.DURATION)
        Dungeon.observe()

        Dungeon.hero.interrupt()

        GLog.p(Messages.get(this, "procced"))

        Journal.remove(Journal.Feature.WELL_OF_AWARENESS)

        return true
    }

    override fun affectItem(item: Item): Item? {
        if (item.isIdentified) {
            return null
        }

        item.identify()
        Badges.validateItemLevelAquired(item)

        emitter.parent.add(Identification(DungeonTilemap.tileCenterToWorld(pos)))

        Journal.remove(Journal.Feature.WELL_OF_AWARENESS)

        return item
    }

    override fun use(emitter: BlobEmitter) {
        super.use(emitter)
        emitter.pour(Speck.factory(Speck.QUESTION), 0.3f)
    }

    override fun tileDesc(): String = Messages.get(this, "desc")
}
