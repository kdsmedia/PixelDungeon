package com.altomedia.pixeldungeon.levels.diggers.unordinary

import com.altomedia.pixeldungeon.actors.mobs.npcs.Blacksmith

import com.altomedia.pixeldungeon.items.Generator
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.levels.diggers.*
import com.altomedia.pixeldungeon.levels.diggers.normal.RectDigger
import com.altomedia.pixeldungeon.levels.traps.FireTrap
import com.watabou.utils.Point
import com.watabou.utils.Random

/**
 * Created by 93942 on 12/4/2018.
 */

class BlackSmithDigger : RectDigger() {
    override fun chooseRoomSize(wall: Wall) = Point(Random.IntRange(4, 7), Random.IntRange(4, 7))

    override fun dig(level: Level, wall: Wall, rect: Rect): DigResult {
        Fill(level, rect, Terrain.TRAP)
        Fill(level, rect.shrink(1), Terrain.EMPTY_SP)

        val din = overlappedWall(wall, rect).random(0)
        Set(level, din, Terrain.DOOR)

        // 2 weapons
        for (i in 0..1) {
            var pos: Int
            do {
                pos = level.pointToCell(rect.random(1))
            } while (level.map[pos] != Terrain.EMPTY_SP)
            level.drop(Random.oneOf(Generator.ARMOR, Generator.WEAPON).generate(), pos)
        }

        // smith
        val npc = Blacksmith()
        do {
            npc.pos = level.pointToCell(rect.random(1))
        } while (level.heaps.get(npc.pos) != null)
        level.mobs.add(npc)

        // traps
        for (p in rect.getAllPoints()) {
            val cell = level.pointToCell(p)
            if (level.map[cell] == Terrain.TRAP)
                level.setTrap(FireTrap().reveal(), cell)
        }

        return DigResult(rect, Wall.ArroundBut(rect, wall.direction.opposite), DigResult.Type.Special)
    }
}
