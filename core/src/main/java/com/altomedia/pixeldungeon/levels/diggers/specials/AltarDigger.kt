package com.altomedia.pixeldungeon.levels.diggers.specials

import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.DungeonTilemap
import com.altomedia.pixeldungeon.actors.blobs.Blob
import com.altomedia.pixeldungeon.actors.blobs.SacrificialFire
import com.altomedia.pixeldungeon.effects.particles.SacrificialParticle
import com.altomedia.pixeldungeon.items.weapon.curses.Sacrificial
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.levels.diggers.DigResult
import com.altomedia.pixeldungeon.levels.diggers.Rect
import com.altomedia.pixeldungeon.levels.diggers.Wall
import com.altomedia.pixeldungeon.levels.diggers.normal.DiamondDigger
import com.watabou.noosa.particles.Emitter
import com.watabou.utils.PathFinder

class AltarDigger : DiamondDigger() {

    override fun dig(level: Level, wall: Wall, rect: Rect): DigResult {
        val dr = super.dig(level, wall, rect)

        for (i in rect.getAllPoints().map { level.pointToCell(it) })
            if (level.map[i] == Terrain.EMPTY)
                Set(level, i, Terrain.EMPTY_SP)

        val cen = level.pointToCell(rect.center)
        for (i in PathFinder.NEIGHBOURS8)
            Set(level, cen + i, Terrain.EMBERS)

        val sf = level.blobs[SacrificialFire::class.java]
        val fire = if (sf == null) SacrificialFire() else sf as SacrificialFire

        fire.seed(level, cen, (Dungeon.depth + 1) * 2)
        level.blobs[SacrificialFire::class.java] = fire

        return dr
    }
}