package com.altomedia.pixeldungeon.levels.diggers.specials

import com.altomedia.pixeldungeon.Dungeon

import com.altomedia.pixeldungeon.items.Generator
import com.altomedia.pixeldungeon.items.keys.IronKey
import com.altomedia.pixeldungeon.items.scrolls.Scroll
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.levels.diggers.DigResult
import com.altomedia.pixeldungeon.levels.diggers.Direction
import com.altomedia.pixeldungeon.levels.diggers.Rect
import com.altomedia.pixeldungeon.levels.diggers.Wall
import com.altomedia.pixeldungeon.levels.diggers.normal.RectDigger
import com.watabou.utils.Point
import com.watabou.utils.Random

/**
 * Created by 93942 on 2018/12/5.
 */

class LibraryDigger : RectDigger() {
    override fun chooseRoomSize(wall: Wall) = Point(Random.IntRange(4, 6), Random.IntRange(4, 6))


    override fun dig(level: Level, wall: Wall, rect: Rect): DigResult {
        Fill(level, rect, Terrain.EMPTY)

        // lock the door
        val door = overlappedWall(wall, rect).random(0)
        Set(level, door, Terrain.LOCKED_DOOR)
        level.addItemToSpawn(IronKey(Dungeon.depth))

        // book shelf
        val sa: Point
        val sb: Point
        when (wall.direction) {
            Direction.Left -> {
                LinkVertical(level, rect.x1, rect.y1, rect.y2, Terrain.BOOKSHELF)
                sa = Point(door.x - 1, door.y - 1)
                sb = Point(door.x - 1, door.y + 1)
            }
            Direction.Right -> {
                LinkVertical(level, rect.x2, rect.y1, rect.y2, Terrain.BOOKSHELF)
                sa = Point(door.x + 1, door.y - 1)
                sb = Point(door.x + 1, door.y + 1)
            }
            Direction.Up -> {
                LinkHorizontal(level, rect.y1, rect.x1, rect.x2, Terrain.BOOKSHELF)
                sa = Point(door.x - 1, door.y - 1)
                sb = Point(door.x + 1, door.y - 1)
            }
            Direction.Down -> {
                LinkHorizontal(level, rect.y2, rect.x1, rect.x2, Terrain.BOOKSHELF)
                sa = Point(door.x - 1, door.y + 1)
                sb = Point(door.x + 1, door.y + 1)
            }
        }

        // statuary
        if (level.map[level.pointToCell(sa)] == Terrain.EMPTY)
            Set(level, sa, Terrain.STATUE)
        if (level.map[level.pointToCell(sb)] == Terrain.EMPTY)
            Set(level, sb, Terrain.STATUE)

        // items
        val n = Random.IntRange(2, 3)
        for (i in 1..n) {
            var pos: Int
            do {
                pos = level.pointToCell(rect.random())
            } while (level.map[pos] != Terrain.EMPTY || level.heaps.get(pos) != null)

            level.drop(prize(level), pos)
        }


        return DigResult(rect, DigResult.Type.Locked)
    }

    private fun prize(level: Level) = level.findPrizeItem(Scroll::class.java)
            ?: Generator.SCROLL.generate()

}
