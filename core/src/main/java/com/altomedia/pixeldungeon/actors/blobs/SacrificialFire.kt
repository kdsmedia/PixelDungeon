package com.altomedia.pixeldungeon.actors.blobs

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.DungeonTilemap
import com.altomedia.pixeldungeon.Journal
import com.altomedia.pixeldungeon.actors.Actor
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.actors.buffs.FlavourBuff
import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.actors.mobs.Mob
import com.altomedia.pixeldungeon.effects.BlobEmitter
import com.altomedia.pixeldungeon.effects.Flare
import com.altomedia.pixeldungeon.effects.Wound
import com.altomedia.pixeldungeon.effects.particles.SacrificialParticle
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.Generator
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.messages.M
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.ui.BuffIndicator
import com.altomedia.pixeldungeon.utils.GLog
import com.watabou.noosa.audio.Sample
import com.watabou.utils.Bundle
import com.watabou.utils.Random

class SacrificialFire : Blob() {

    var pos = 0

    override fun evolve() {
        off[pos] = cur[pos]
        volume = off[pos]

        // affect char
        Actor.findChar(pos)?.let {
            if (Dungeon.visible[pos] && it.buff(Marked::class.java) == null) {
                it.sprite.emitter().burst(SacrificialParticle.FACTORY, 20)
                Sample.INSTANCE.play(Assets.SND_BURNING)
            }
            Buff.prolong(it, Marked::class.java, 50f)
        }

        if (Dungeon.visible[pos])
            Journal.add(Journal.Feature.SACRIFICIAL_FIRE)

    }

    // no decrease
    override fun seed(level: Level, cell: Int, amount: Int) {
        super.seed(level, cell, amount)

        cur[pos] = 0
        pos = cell
        cur[pos] = amount
        volume = cur[pos]

        area.setEmpty()
        area.union(cell % level.width(), cell / level.width())
    }

    override fun use(emitter: BlobEmitter) {
        super.use(emitter)

        emitter.pour(SacrificialParticle.FACTORY, 0.04f)
    }

    override fun tileDesc(): String = Messages.get(this, "desc")

    override fun restoreFromBundle(bundle: Bundle) {
        super.restoreFromBundle(bundle)

        for (i in cur.indices)
            if (cur[i] > 0) {
                pos = i
                break
            }
    }

    class Marked : FlavourBuff() {
        override fun icon(): Int = BuffIndicator.SACRIFICE

        override fun toString(): String = M.L(this, "name")

        override fun desc(): String = M.L(this, "desc", dispTurns())

        fun onEnemySlayed(ch: Char) {
            if (Sacrifice(ch)) detach()
        }
    }

    companion object {
        fun Sacrifice(ch: Char): Boolean {
            Wound.hit(ch)

            Dungeon.level.blobs[SacrificialFire::class.java]?.let {
                val fire = it as SacrificialFire
                val exp = when (ch) {
                    is Mob -> ch.exp() * Random.IntRange(1, 3)
                    is Hero -> ch.maxExp()
                    else -> 0
                }

                if (exp > 0) {
                    val vol = fire.volume - exp
                    if (vol > 0) {
                        fire.seed(Dungeon.level, fire.pos, vol)
                        GLog.w(Messages.get(SacrificialFire::class.java, "worthy"))
                    } else {
                        // enough!
                        fire.seed(Dungeon.level, fire.pos, 0)
                        Journal.remove(Journal.Feature.SACRIFICIAL_FIRE)

                        GLog.w(Messages.get(SacrificialFire::class.java, "reward"))
                        GameScene.effect(Flare(7, 32f).color(0x66ffff, true).show(
                                ch.sprite.parent, DungeonTilemap.tileCenterToWorld(fire.pos), 2f))
                        Dungeon.level.drop(Prize(), fire.pos).sprite.drop()
                        return true
                    }
                } else {
                    GLog.w(Messages.get(SacrificialFire::class.java, "unworthy"))
                }
            }

            return false
        }

        private fun Prize(): Item = Generator.RUNE.generate()
    }

}