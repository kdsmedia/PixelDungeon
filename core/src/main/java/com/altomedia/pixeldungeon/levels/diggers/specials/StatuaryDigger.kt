package com.altomedia.pixeldungeon.levels.diggers.specials

import com.altomedia.pixeldungeon.actors.mobs.npcs.Statuary
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.levels.diggers.*
import com.altomedia.pixeldungeon.levels.diggers.normal.DiamondDigger
import com.watabou.utils.PathFinder

/**
 * Created by 93942 on 2018/12/5.
 */

class StatuaryDigger : DiamondDigger() {
    private val s = Statuary().random()

    override fun dig(level: Level, wall: Wall, rect: Rect): DigResult {
        val dr = super.dig(level, wall, rect)

        val cen = level.pointToCell(rect.center)
        for (i in PathFinder.NEIGHBOURS9)
            Set(level, cen + i, Terrain.EMPTY_SP)

        s.pos = cen
        level.mobs.add(s)

        return dr
    }
}
