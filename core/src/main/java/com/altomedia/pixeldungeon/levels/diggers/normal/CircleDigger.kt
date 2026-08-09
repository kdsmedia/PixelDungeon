package com.altomedia.pixeldungeon.levels.diggers.normal

import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.levels.diggers.*
import com.watabou.utils.Point
import com.watabou.utils.Random

/**
 * Created by 93942 on 2018/12/17.
 */

class CircleDigger : RectDigger() {
    override fun dig(level: Level, wall: Wall, rect: Rect): DigResult {
        val dr = super.dig(level, wall, rect)
        
        val maxInner = (Math.min(rect.width, rect.height) - 1) / 2
        val inner = Random.NormalIntRange(1, maxInner)
        Fill(level, rect.shrink(inner),
                if (Random.Int(4) == 0) Terrain.CHASM else Terrain.WALL)

        return dr
    }
}
