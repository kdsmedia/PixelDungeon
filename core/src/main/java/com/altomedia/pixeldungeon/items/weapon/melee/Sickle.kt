package com.altomedia.pixeldungeon.items.weapon.melee

import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.effects.CellEmitter
import com.altomedia.pixeldungeon.effects.particles.LeafParticle
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.levels.Terrain
import com.altomedia.pixeldungeon.levels.features.HighGrass
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.watabou.utils.PathFinder
import java.util.ArrayList
import kotlin.math.round

class Sickle : MeleeWeapon() {
    init {
        image = ItemSpriteSheet.SICKLE

        tier = 2

        defaultAction = CUT
    }

    override fun actions(hero: Hero): ArrayList<String> = super.actions(hero).apply { add(CUT) }

    override fun execute(hero: Hero, action: String) {
        super.execute(hero, action)
        if (action == CUT) {
            hero.sprite.attack(hero.pos) {
                var affected = false
                for (i in PathFinder.NEIGHBOURS8) {
                    val cell = hero.pos + i
                    if (Dungeon.level.map[cell] == Terrain.HIGH_GRASS || Dungeon.level.map[cell] == Terrain.HIGH_GRASS_COLLECTED) {
                        affected = true

                        val harvest = Dungeon.level.map[cell] == Terrain.HIGH_GRASS
                        Level.set(cell, Terrain.GRASS)
                        if (harvest)
                            HighGrass.Harvest(Dungeon.level, cell, hero)?.let {
                                Dungeon.level.drop(it, cell).sprite.drop()
                            }

                        GameScene.updateMap(cell)

                        CellEmitter.get(cell).burst(LeafParticle.LEVEL_SPECIFIC, 6)
                    }
                    Dungeon.level.plants.get(cell)?.trigger()
                }

                if (affected) Dungeon.observe()
            }
            hero.spendAndNext(1f)
        }
    }

    override fun proc(dmg: Damage): Damage {
        val pos = (dmg.to as Char).pos
        for (i in PathFinder.NEIGHBOURS8) {
            val mob = Dungeon.level.findMobAt(pos + i)
            if (mob != null && mob.camp == Char.Camp.ENEMY)
                mob.takeDamage(mob.defendDamage(Damage(round(dmg.value * 0.3f).toInt(), dmg.from, dmg.to).type(dmg.type)))
        }

        return super.proc(dmg)
    }

    override fun STRReq(lvl: Int): Int = super.STRReq(lvl) + 1

    companion object {
        private const val CUT = "cut"
    }
}