package com.altomedia.pixeldungeon.levels.features

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.effects.CellEmitter
import com.altomedia.pixeldungeon.effects.particles.ElmoParticle
import com.altomedia.pixeldungeon.levels.DeadEndLevel
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.utils.GLog
import com.altomedia.pixeldungeon.windows.WndMessage
import com.watabou.noosa.audio.Sample

object Sign {

    fun ShowInDepth(depth: Int) = depth in listOf(0, 5, 6, 11, 15, 16, 20)

    fun Read(pos: Int) {
        if (Dungeon.level is DeadEndLevel)
            GameScene.show(WndMessage(Messages.get(Sign::class.java, "dead_end")))
        else {
            if (ShowInDepth(Dungeon.depth))
                GameScene.show(WndMessage(Messages.get(Sign::class.java, "tip_${Dungeon.depth}")))
            else {
                // destroy 
                Dungeon.level.destroy(pos)
                GameScene.updateMap(pos)
                GameScene.discoverTile(pos, Terrain.SIGN)

                GLog.w(Messages.get(Sign::class.java, "burn"))

                CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6)
                Sample.INSTANCE.play(Assets.SND_BURNING)
            }
        }
    }

}