package com.altomedia.pixeldungeon.levels.features

import com.altomedia.pixeldungeon.Challenges
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.buffs.Barkskin
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.actors.hero.HeroSubClass
import com.altomedia.pixeldungeon.effects.CellEmitter
import com.altomedia.pixeldungeon.effects.particles.LeafParticle

import com.altomedia.pixeldungeon.items.Generator
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.armor.glyphs.Camouflage
import com.altomedia.pixeldungeon.items.artifacts.SandalsOfNature
import com.altomedia.pixeldungeon.items.unclassified.Dewdrop
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.plants.BlandfruitBush
import com.altomedia.pixeldungeon.plants.Plant
import com.altomedia.pixeldungeon.scenes.GameScene
import com.watabou.utils.Random

object HighGrass {
    fun Trample(level: Level, pos: Int, ch: Char?) {
        // called only if HIGH_GRASS, HIGH_GRASS_COLLECTED, see Level::press
        if (level.map[pos] == Terrain.HIGH_GRASS) {
            Level.set(pos, Terrain.HIGH_GRASS_COLLECTED)
            val prize = Harvest(level, pos, ch)
            if (prize != null) level.drop(prize, pos).sprite.drop()

            if (prize is Plant.Seed) Level.set(pos, Terrain.GRASS)

            GameScene.updateMap(pos)
        }

        var leaves = 4
        if (ch is Hero) {
            if (ch.subClass == HeroSubClass.WARDEN) {
                Buff.affect(ch, Barkskin::class.java).level(ch.HT / 10)
                leaves += 4
            }

            if (ch.belongings.armor?.hasGlyph(Camouflage::class.java) == true) {
                Buff.affect(ch, Camouflage.Camo::class.java).set(2 + ch.belongings.armor.level())
                leaves += 4
            }
        }

        CellEmitter.get(pos).burst(LeafParticle.LEVEL_SPECIFIC, leaves)
        if (ch != Dungeon.hero) Dungeon.observe()
    }

    fun Harvest(level: Level, pos: Int, ch: Char?): Item? {
        if (Dungeon.isChallenged(Challenges.NO_HERBALISM)) return null

        var naturalismLevel = 0
        ch?.buff(SandalsOfNature.Naturalism::class.java)?.let { naturalism ->
            naturalismLevel = if (naturalism.isCursed) -1 else {
                naturalism.charge()
                naturalism.itemLevel() + 1
            }
        }

        if (naturalismLevel >= 0) {
            // less seed in the village
            val chance = if (Dungeon.depth == 0) 30 else (18 - naturalismLevel * 3)
            if (Random.Int(chance) == 0) {
                //^ drop seed
                val seed = Generator.SEED.generate()
                if (seed is BlandfruitBush.Seed) {
                    if (Random.Int(15) - Dungeon.limitedDrops.blandfruitSeed.count >= 0) {
                        Dungeon.limitedDrops.blandfruitSeed.count++
                    } else return null
                }

                return seed
            }

            // dew, 1/6 ~ 1/4
            if (Random.Int(12 - naturalismLevel) < 2) return Dewdrop()
        }

        return null
    }

}