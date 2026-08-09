package com.altomedia.pixeldungeon.levels.diggers.secret

import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.items.unclassified.Gold
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.levels.diggers.DigResult
import com.altomedia.pixeldungeon.levels.diggers.Digger
import com.altomedia.pixeldungeon.levels.diggers.Rect
import com.altomedia.pixeldungeon.levels.diggers.Wall
import com.altomedia.pixeldungeon.levels.diggers.normal.RectDigger
import com.altomedia.pixeldungeon.levels.traps.DisintegrationTrap
import com.altomedia.pixeldungeon.levels.traps.PoisonTrap
import com.altomedia.pixeldungeon.levels.traps.RockfallTrap
import com.altomedia.pixeldungeon.levels.traps.Trap
import com.watabou.utils.Point
import com.watabou.utils.Random

class SecretTreasuryDigger : RectDigger() {
    override fun chooseRoomSize(wall: Wall) = Point(Random.IntRange(3, 6), Random.IntRange(3, 6))

    override fun dig(level: Level, wall: Wall, rect: Rect): DigResult {
        Fill(level, rect, Terrain.EMPTY)
        Set(level, rect.center, Terrain.STATUE)
        Set(level, overlappedWall(wall, rect).random(), Terrain.SECRET_DOOR)

        val cls: Class<out Trap> = when {
            Random.Int(2) == 0 -> RockfallTrap::class.java
            Dungeon.depth >= 0 -> DisintegrationTrap::class.java
            else -> PoisonTrap::class.java
        }

        val golds = rect.area / 2
        val ratio = 8f / golds.toFloat()

        for (i in 1..golds) {
            var pos = level.pointToCell(rect.random())
            while (level.heaps.get(pos) != null || level.map[pos] != Terrain.EMPTY)
                pos = level.pointToCell(rect.random())

            val gold = Gold().random()
            gold.quantity(Math.round(gold.quantity() * ratio))
            level.drop(gold, pos)
        }

        // put traps
        for (cell in rect.getAllPoints().map { level.pointToCell(it) })
            if (Random.Int(2) == 0 && level.map[cell] == Terrain.EMPTY) {
                level.setTrap(cls.newInstance().reveal(), cell)
                Digger.Set(level, cell, Terrain.TRAP)
            }

        return DigResult(rect, DigResult.Type.Secret)
    }
}
