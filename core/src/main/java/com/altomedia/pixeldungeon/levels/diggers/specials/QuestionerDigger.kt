package com.altomedia.pixeldungeon.levels.diggers.specials

import com.altomedia.pixeldungeon.actors.mobs.npcs.Questioner
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.levels.diggers.*
import com.altomedia.pixeldungeon.levels.diggers.normal.RectDigger

/**
 * Created by 93942 on 2018/12/17.
 */

class QuestionerDigger : RectDigger() {
    override fun dig(level: Level, wall: Wall, rect: Rect): DigResult {
        Fill(level, rect, Terrain.EMPTY)

        // no door
        val q = Questioner().random().hold(rect)
        q.pos = level.pointToCell(overlappedWall(wall, rect).random())
        Set(level, q.pos, Terrain.WALL_SPECIAL)
        level.mobs.add(q)

        return DigResult(rect, DigResult.Type.Locked)
    }
}
