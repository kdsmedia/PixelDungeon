package com.altomedia.pixeldungeon.levels.diggers.unordinary

import com.altomedia.pixeldungeon.actors.mobs.npcs.Yvette
import com.altomedia.pixeldungeon.items.potions.PotionOfLiquidFlame
import com.altomedia.pixeldungeon.items.scrolls.ScrollOfTeleportation
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.levels.diggers.DigResult
import com.altomedia.pixeldungeon.levels.diggers.Rect
import com.altomedia.pixeldungeon.levels.diggers.Wall
import com.altomedia.pixeldungeon.levels.diggers.normal.RectDigger
import com.watabou.utils.Point
import com.watabou.utils.Random

class TrappedRangerDigger : RectDigger() {
    private val y = Yvette()

    override fun chooseRoomSize(wall: Wall) =
            Point(Random.HighIntRange(6, 10), Random.HighIntRange(6, 10))

    override fun dig(level: Level, wall: Wall, rect: Rect): DigResult {
        val dr = super.dig(level, wall, rect)

        val maxInner = (Math.min(rect.width, rect.height) - 3) / 2
        val i = Random.NormalIntRange(1, maxInner)
        Fill(level, rect.shrink(i), Terrain.WALL)

        val innerSpace = rect.shrink(i + 1)
        Fill(level, innerSpace, Terrain.EMPTY_SP)
        val innerDoor = innerSpace.random()
        when (Random.Int(4)) {
            0 -> innerDoor.x = innerSpace.x1 - 1
            1 -> innerDoor.x = innerSpace.x2 + 1
            2 -> innerDoor.y = innerSpace.y1 - 1
            else -> innerDoor.y = innerSpace.y2 + 1
        }
        Set(level, innerDoor, Terrain.BARRICADE)

        level.addItemToSpawn(PotionOfLiquidFlame())
        level.addItemToSpawn(ScrollOfTeleportation())

        if (Random.Int(4) == 0) {
            val heap = Yvette.CreateSkeletonHeap()
            heap.pos = level.pointToCell(innerSpace.random())
            level.heaps.put(heap.pos, heap)
        } else {
            y.pos = level.pointToCell(innerSpace.random())
            level.mobs.add(y)
        }

        return DigResult(dr.rect, dr.walls, DigResult.Type.Special)
    }
}