package com.altomedia.pixeldungeon.levels.diggers.secret


import com.altomedia.pixeldungeon.items.Heap
import com.altomedia.pixeldungeon.items.Generator
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.levels.diggers.DigResult
import com.altomedia.pixeldungeon.levels.diggers.Rect
import com.altomedia.pixeldungeon.levels.diggers.Wall
import com.altomedia.pixeldungeon.levels.diggers.normal.RectDigger
import com.altomedia.pixeldungeon.levels.traps.SummoningTrap
import com.watabou.utils.Point
import com.watabou.utils.Random

class SecretSummoningDigger : RectDigger() {

    override fun chooseRoomSize(wall: Wall) = Point(Random.Int(3, 6), Random.Int(3, 6))

    override fun dig(level: Level, wall: Wall, rect: Rect): DigResult {
        Fill(level, rect, Terrain.SECRET_TRAP)
        Set(level, overlappedWall(wall, rect).random(), Terrain.SECRET_DOOR)

        level.drop(Generator.generate(), level.pointToCell(rect.center)).type = Heap.Type.SKELETON

        for (cell in rect.getAllPoints().map { level.pointToCell(it) })
            if (level.map[cell] == Terrain.SECRET_TRAP)
                level.setTrap(SummoningTrap().hide(), cell)

        return DigResult(rect, DigResult.Type.Secret)
    }
}