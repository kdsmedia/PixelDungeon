package com.altomedia.pixeldungeon.levels.diggers.secret

import com.altomedia.pixeldungeon.actors.blobs.Foliage
import com.altomedia.pixeldungeon.items.wands.WandOfRegrowth
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.levels.diggers.DigResult
import com.altomedia.pixeldungeon.levels.diggers.Rect
import com.altomedia.pixeldungeon.levels.diggers.Wall
import com.altomedia.pixeldungeon.levels.diggers.normal.PatchDigger
import com.altomedia.pixeldungeon.plants.Plant
import com.altomedia.pixeldungeon.plants.Starflower
import com.watabou.utils.Point
import com.watabou.utils.Random

class SecretGardenDigger : PatchDigger() {
    override fun chooseRoomSize(wall: Wall) = Point(Random.IntRange(4, 8), Random.IntRange(4, 8))

    override fun patchTile(): Int = Terrain.GRASS

    override fun dig(level: Level, wall: Wall, rect: Rect): DigResult {
        super.dig(level, wall, rect)

        // make secret
        val ow = overlappedWall(wall, rect)
        Fill(level, ow, Terrain.WALL)
        Set(level, ow.random(), Terrain.SECRET_DOOR)

        // give plant
        val randomPlant = { seed: Plant.Seed ->
            var pos = level.pointToCell(rect.random())
            while (level.plants.get(pos) != null)
                pos = level.pointToCell(rect.random())

            level.plant(seed, pos)
        }

        randomPlant(Starflower.Seed())
        randomPlant(WandOfRegrowth.Dewcatcher.Seed())
        randomPlant(WandOfRegrowth.Seedpod.Seed())
        randomPlant(if (Random.Int(2) == 0) WandOfRegrowth.Dewcatcher.Seed()
        else WandOfRegrowth.Seedpod.Seed())

        // effects, as in garden 
        val l = level.blobs[Foliage::class.java]
        val light: Foliage = if(l==null) Foliage() else l as Foliage

        for (p in rect.getAllPoints())
            light.seed(level, level.pointToCell(p), 1)
        level.blobs[Foliage::class.java] = light
        
        return DigResult(rect, DigResult.Type.Secret)
    }
}