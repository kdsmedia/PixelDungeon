package com.altomedia.pixeldungeon.levels.diggers.specials

import com.altomedia.pixeldungeon.Dungeon

import com.altomedia.pixeldungeon.items.Generator
import com.altomedia.pixeldungeon.items.keys.IronKey
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.levels.diggers.DigResult
import com.altomedia.pixeldungeon.levels.diggers.Rect
import com.altomedia.pixeldungeon.levels.diggers.Wall
import com.altomedia.pixeldungeon.levels.diggers.normal.RectDigger
import com.watabou.utils.Point
import com.watabou.utils.Random

/**
 * Created by 93942 on 2018/12/12.
 */

class ArmoryDigger : RectDigger() {
    override fun chooseRoomSize(wall: Wall) = Point(Random.IntRange(4, 6), Random.IntRange(4, 6))

    override fun dig(level: Level, wall: Wall, rect: Rect): DigResult {
        Fill(level, rect, Terrain.EMPTY)
        val door = overlappedWall(wall, rect).random()
        Set(level, door, Terrain.LOCKED_DOOR)

        Set(level, rect.random(1), Terrain.STATUE)

        val n = Random.IntRange(2, 3)
        for (i in 1..n) {
            var pos: Int
            do {
                pos = level.pointToCell(rect.random())
            } while (level.map[pos] != Terrain.EMPTY || level.heaps.get(pos) != null)
            level.drop(prize(level), pos)
        }

        level.addItemToSpawn(IronKey(Dungeon.depth))

        return DigResult(rect, DigResult.Type.Locked)
    }

    private fun prize(level: Level) = Random.chances(PrizeGenerators).generate()
    
    companion object {
        private val PrizeGenerators =   hashMapOf(
                Generator.ARMOR to 0.75f,
                Generator.WEAPON to 1f,
                Generator.HELMET to 0.5f
        )
    }
}
