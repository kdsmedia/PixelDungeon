package com.altomedia.pixeldungeon.levels

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.actors.Actor
import com.altomedia.pixeldungeon.actors.mobs.Rat
import com.altomedia.pixeldungeon.items.artifacts.DriedRose
import com.altomedia.pixeldungeon.items.unclassified.Dewdrop
import com.altomedia.pixeldungeon.items.unclassified.RegenerationRune
import com.altomedia.pixeldungeon.levels.diggers.Digger
import com.altomedia.pixeldungeon.levels.diggers.Direction
import com.altomedia.pixeldungeon.levels.diggers.Rect
import com.altomedia.pixeldungeon.levels.diggers.Wall
import com.altomedia.pixeldungeon.levels.diggers.specials.AltarDigger

class EmptyLevel : RegularLevel() {
    init {
        color1 = 0x48763c
        color2 = 0x59994a
    }

    override fun tilesTex(): String = Assets.TILES_SEWERS

    override fun waterTex(): String = Assets.WATER_SEWERS

    override fun water(): BooleanArray = BooleanArray(length())

    override fun grass(): BooleanArray = BooleanArray(length())

    override fun decorate() {}

    override fun createMobs() {}

    override fun respawner(): Actor? = null

    override fun createItems() {
//        for(i in 1..20) drop(Generator.ARTIFACT.generate(), xy2cell(5, 5+i))
//        for(i in 1..20) drop(Generator.WEAPON.generate(), xy2cell(6, 5+i))
//        for(i in 1..20) drop(Generator.ARMOR.generate(), xy2cell(7, 5+i))
//        for(i in 1..20) drop(Generator.WAND.generate(), xy2cell(8, 5+i))
//        for(i in 1..20) drop(Generator.RING.generate(), xy2cell(9, 5+i))
//        for(i in 1..20) drop(Generator.POTION.generate(), xy2cell(10, 5+i))
//        for(i in 1..20) drop(Generator.SCROLL.generate(), xy2cell(11, 5+i))
//        for(i in 1..20) drop(Generator.SEED.generate(), xy2cell(12, 5+i))

        for (i in 1..20) drop(DriedRose.Companion.Petal(), xy2cell(13, 5 + i))

        drop(RegenerationRune(), xy2cell(15, 15))
        drop(Dewdrop(), xy2cell(16, 16))
    }

    override fun build(iteration: Int): Boolean {
        Digger.Fill(this, 1, 1, width - 2, height - 2, Terrain.EMPTY)

        entrance = xy2cell(width / 2, height / 2)
        exit = entrance + 1
        map[entrance] = Terrain.ENTRANCE
        map[exit] = Terrain.EXIT

        map[entrance + 3] = Terrain.ALCHEMY
        map[entrance + 4] = Terrain.ENCHANTING_STATION

        // Digger.Fill(this, Rect(11, 16, 10, 16).shrink(-1), Terrain.WALL)
        val ad = AltarDigger()
        ad.dig(this, Wall(10, 10, 10, 16, Direction.Right), Rect(11, 16, 10, 16))

        mobs.add(Rat().apply { pos = xy2cell(10, 10) })
        mobs.add(Rat().apply { pos = xy2cell(11, 11) })

        return true
    }
}