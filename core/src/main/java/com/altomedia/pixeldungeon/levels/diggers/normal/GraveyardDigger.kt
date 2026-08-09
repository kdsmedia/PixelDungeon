package com.altomedia.pixeldungeon.levels.diggers.normal


import com.altomedia.pixeldungeon.items.unclassified.Gold
import com.altomedia.pixeldungeon.items.Heap
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.Generator
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.levels.diggers.DigResult
import com.altomedia.pixeldungeon.levels.diggers.Digger
import com.altomedia.pixeldungeon.levels.diggers.Rect
import com.altomedia.pixeldungeon.levels.diggers.Wall
import com.watabou.utils.PathFinder
import com.watabou.utils.Random

class GraveyardDigger : Digger() {
    private val shapeDigger = when (Random.Int(8)) {
        0 -> RoundDigger()
        1 -> DiamondDigger()
        else -> RectDigger()
    }

    override fun chooseDigArea(wall: Wall) = shapeDigger.chooseDigArea(wall)

    override fun dig(level: Level, wall: Wall, rect: Rect): DigResult {
        val dr = shapeDigger.dig(level, wall, rect)

        // grass 
        rect.getAllPoints().map { level.pointToCell(it) }.filter { level.map[it] == Terrain.EMPTY }.forEach { level.map[it] = Terrain.GRASS }

        // put tombs
        val tryPlaceATomb = { item: Item ->
            for (i in 1..30) {
                val cell = level.pointToCell(rect.random(1))
                if (level.map[cell] == Terrain.GRASS && PathFinder.NEIGHBOURS9.all { level.heaps.get(cell + it) == null }) {
                    level.drop(item, cell).type = Heap.Type.TOMB
                    break
                }
            }
        }

        val n = Random.IntRange(2, 4)
        val index = Random.Int(n) // random item
        for (i in 0 until n)
            tryPlaceATomb(if (i == index) Generator.generate() else Gold().random())

        return dr
    }
}