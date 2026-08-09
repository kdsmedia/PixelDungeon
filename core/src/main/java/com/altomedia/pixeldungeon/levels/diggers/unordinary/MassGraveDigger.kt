package com.altomedia.pixeldungeon.levels.diggers.unordinary

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.actors.mobs.Skeleton

import com.altomedia.pixeldungeon.items.unclassified.Gold
import com.altomedia.pixeldungeon.items.Heap
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.Generator
import com.altomedia.pixeldungeon.items.potions.PotionOfLiquidFlame
import com.altomedia.pixeldungeon.items.quest.CorpseDust
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.levels.diggers.DigResult
import com.altomedia.pixeldungeon.levels.diggers.Rect
import com.altomedia.pixeldungeon.levels.diggers.Wall
import com.altomedia.pixeldungeon.levels.diggers.normal.RectDigger
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.ui.CustomTileVisual
import com.watabou.utils.Point
import com.watabou.utils.Random

import java.util.ArrayList

/**
 * Created by 93942 on 2018/12/18.
 */

class MassGraveDigger : RectDigger() {
    override fun chooseRoomSize(wall: Wall) = Point(Random.IntRange(5, 9), Random.IntRange(5, 9))


    override fun dig(level: Level, wall: Wall, rect: Rect): DigResult {
        Fill(level, rect, Terrain.EMPTY_SP)

        val door = overlappedWall(wall, rect).random()
        Set(level, door, Terrain.BARRICADE)
        level.addItemToSpawn(PotionOfLiquidFlame())

        level.customTiles.addAll(CustomTileVisual.CustomTilesForRect(rect.shrink(-1), Bones::class.java))

        // 50% 1 skeleton, 50% 2 skeletons
        for (i in 0..Random.Int(2)) {
            val s = Skeleton()
            do {
                s.pos = level.pointToCell(rect.random())
            } while (level.map[s.pos] != Terrain.EMPTY_SP || level.findMobAt(s.pos) != null)
            level.mobs.add(s)
        }

        val items = ArrayList<Item>()
        //100% corpse dust, 2x100% 1 coin, 2x30% coins, 1x60% random item, 1x30% 
        // armor
        items.add(CorpseDust())
        items.add(Gold(1))
        items.add(Gold(1))
        if (Random.Float() <= 0.3f) items.add(Gold())
        if (Random.Float() <= 0.3f) items.add(Gold())
        if (Random.Float() <= 0.6f) items.add(Generator.generate())
        if (Random.Float() <= 0.3f) items.add(Generator.ARMOR.generate())

        for (i in items) {
            var pos: Int
            do {
                pos = level.pointToCell(rect.random())
            } while (level.map[pos] != Terrain.EMPTY_SP || level.heaps.get(pos) != null)
            val h = level.drop(i, pos)
            h.type = Heap.Type.SKELETON
        }

        return DigResult(rect, DigResult.Type.Locked)
    }

    class Bones : CustomTileVisual() {
        init {
            name = Messages.get(this, "name")

            tx = Assets.PRISON_QUEST
            txX = 3
            txY = 0
        }

        override fun desc(): String? {
            return if (ofsX == 1 && ofsY == 1) {
                Messages.get(this, "desc")
            } else {
                null
            }
        }
    }
}
