package com.altomedia.pixeldungeon.levels.diggers.normal

import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.levels.diggers.DigResult
import com.altomedia.pixeldungeon.levels.diggers.Rect
import com.altomedia.pixeldungeon.levels.diggers.Wall
import com.watabou.utils.Point
import com.watabou.utils.Random

class SmallCornerDigger : RectDigger() {
    override fun chooseRoomSize(wall: Wall): Point = Point(3, 3)

    override fun dig(level: Level, wall: Wall, rect: Rect): DigResult {
        val dr = super.dig(level, wall, rect)

        val cen = when (Random.Int(10)) {
            in 0..2 -> Terrain.EMPTY
            3 -> Terrain.EMPTY_WELL
            4 -> Terrain.CHASM
            else -> Terrain.WALL
        }
        Set(level, rect.center, cen)

        return dr
    }

}