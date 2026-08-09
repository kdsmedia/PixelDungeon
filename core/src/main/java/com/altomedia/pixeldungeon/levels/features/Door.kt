package com.altomedia.pixeldungeon.levels.features

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.scenes.GameScene
import com.watabou.noosa.audio.Sample

object Door {
    // ch can be null: throw weapon to open door
    fun Enter(pos: Int, ch: Char?) {
        Level.set(pos, Terrain.OPEN_DOOR)
        GameScene.updateMap(pos)

        if (ch === Dungeon.hero) {
            // don't observe here: already observed when hero moving
            Sample.INSTANCE.play(Assets.SND_OPEN)
        } else if (Dungeon.visible[pos]) {
            Sample.INSTANCE.play(Assets.SND_OPEN)
            Dungeon.observe()
        }
    }

    fun Leave(pos: Int, ch: Char) {
        if (Dungeon.level.heaps.get(pos) == null) {
            // now, only the hero close the door behind
            if (ch === Dungeon.hero) {
                Level.set(pos, Terrain.DOOR)
                GameScene.updateMap(pos)
            }

//            if (ch !== Dungeon.hero && Dungeon.visible[pos])
//                Dungeon.observe()
        }
    }

}