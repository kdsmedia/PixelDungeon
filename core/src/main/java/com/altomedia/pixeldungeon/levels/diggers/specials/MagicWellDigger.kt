package com.altomedia.pixeldungeon.levels.diggers.specials

import com.altomedia.pixeldungeon.DarkestPixelDungeon
import com.altomedia.pixeldungeon.actors.blobs.WaterOfAwareness
import com.altomedia.pixeldungeon.actors.blobs.WaterOfHealth
import com.altomedia.pixeldungeon.actors.blobs.WaterOfTransmutation
import com.altomedia.pixeldungeon.actors.blobs.WellWater
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.levels.diggers.*
import com.altomedia.pixeldungeon.levels.diggers.normal.RoundDigger
import com.watabou.utils.PathFinder
import com.watabou.utils.Random

/**
 * Created by 93942 on 2018/12/12.
 */

class MagicWellDigger : RoundDigger() {
    override fun dig(level: Level, wall: Wall, rect: Rect): DigResult {
        val dr = super.dig(level, wall, rect)

        // set a well
        val cen = level.pointToCell(rect.center)
        for (i in PathFinder.NEIGHBOURS8)
            if (Random.Int(2) == 0)
                Set(level, cen + i, Terrain.GRASS)
        Set(level, cen, Terrain.WELL)

        val cls = Random.element(WATERS) as Class<out WellWater>

        val l = level.blobs[cls]
        val ww = if(l==null) cls.newInstance() else l as WellWater
        
        ww.seed(level, cen, 1)
        level.blobs[cls] = ww

        return dr
    }

    companion object {
        private val WATERS = arrayOf(WaterOfAwareness::class.java, WaterOfHealth::class.java, WaterOfTransmutation::class.java)
    }

}
