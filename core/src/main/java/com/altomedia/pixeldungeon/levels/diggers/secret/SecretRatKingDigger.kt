package com.altomedia.pixeldungeon.levels.diggers.secret

import com.altomedia.pixeldungeon.actors.mobs.npcs.RatKing
import com.altomedia.pixeldungeon.items.Heap
import com.altomedia.pixeldungeon.items.unclassified.Gold
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.levels.diggers.DigResult
import com.altomedia.pixeldungeon.levels.diggers.Rect
import com.altomedia.pixeldungeon.levels.diggers.Wall
import com.altomedia.pixeldungeon.levels.diggers.normal.RectDigger
import com.watabou.utils.Point
import com.watabou.utils.Random

class SecretRatKingDigger : RectDigger() {
    override fun chooseRoomSize(wall: Wall) = Point(Random.Int(3, 6), Random.Int(3, 6))

    override fun dig(level: Level, wall: Wall, rect: Rect): DigResult {
        Fill(level, rect, Terrain.EMPTY_SP)
        val door = level.pointToCell(overlappedWall(wall, rect).random())
        Set(level, door, Terrain.SECRET_DOOR)

        for (x in rect.x1..rect.x2) {
            dropChest(level, x, rect.y1, door)
            dropChest(level, x, rect.y2, door)
        }
        for (y in rect.y1..rect.y2) {
            dropChest(level, rect.x1, y, door)
            dropChest(level, rect.x2, y, door)
        }

        val r = RatKing().apply {
            pos = level.pointToCell(rect.random(1))
        }
        level.mobs.add(r)

        return DigResult(rect, DigResult.Type.Secret)
    }

    private fun dropChest(level: Level, x: Int, y: Int, door: Int) {
        if (door in arrayOf(level.xy2cell(x - 1, y), level.xy2cell(x + 1, y),
                        level.xy2cell(x, y - 1), level.xy2cell(x, y + 1)))
            return

        level.drop(Gold(Random.IntRange(1, 25)), level.xy2cell(x, y)).type = Heap.Type.CHEST
    }
}