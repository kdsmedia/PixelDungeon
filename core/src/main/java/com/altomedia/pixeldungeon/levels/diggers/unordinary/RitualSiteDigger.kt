package com.altomedia.pixeldungeon.levels.diggers.unordinary

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.items.quest.CeremonialCandle
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.levels.diggers.*
import com.altomedia.pixeldungeon.levels.diggers.normal.RoundDigger
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.ui.CustomTileVisual
import com.watabou.utils.PathFinder

/**
 * Created by 93942 on 2018/12/18.
 */

class RitualSiteDigger : RoundDigger() {
    override fun dig(level: Level, wall: Wall, rect: Rect): DigResult {
        val dr = super.dig(level, wall, rect)

        // put ritual
        val rm = RitualMarker()
        rm.pos(rect.center.x - 1, rect.center.y - 1)
        level.customTiles.add(rm)

        val cen = level.pointToCell(rect.center)
        for (i in PathFinder.NEIGHBOURS9)
            Set(level, cen + i, Terrain.EMPTY_DECO)

        for (i in 0..3)
            level.addItemToSpawn(CeremonialCandle())
        CeremonialCandle.ritualPos = cen

        return dr
    }

    class RitualMarker : CustomTileVisual() {
        init {
            name = Messages.get(this, "name")

            tx = Assets.PRISON_QUEST
            txY = 0
            txX = txY
            tileH = 3
            tileW = tileH
        }

        override fun desc(): String? {
            return Messages.get(this, "desc")
        }
    }
}
