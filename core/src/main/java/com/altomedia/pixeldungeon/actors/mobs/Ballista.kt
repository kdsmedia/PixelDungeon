package com.altomedia.pixeldungeon.actors.mobs

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.PropertyConfiger
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.actors.buffs.Amok
import com.altomedia.pixeldungeon.actors.buffs.Sleep
import com.altomedia.pixeldungeon.actors.buffs.Terror
import com.altomedia.pixeldungeon.effects.particles.ElmoParticle
import com.altomedia.pixeldungeon.items.unclassified.Gold
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.Generator
import com.altomedia.pixeldungeon.items.wands.WandOfBlastWave
import com.altomedia.pixeldungeon.items.weapon.missiles.Dart
import com.altomedia.pixeldungeon.items.weapon.missiles.Javelin
import com.altomedia.pixeldungeon.mechanics.Ballistica
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.sprites.MissileSprite
import com.altomedia.pixeldungeon.sprites.MobSprite
import com.watabou.noosa.TextureFilm
import com.watabou.noosa.audio.Sample
import com.watabou.utils.Bundle
import com.watabou.utils.Callback
import com.watabou.utils.Random
import java.util.HashSet

class Ballista : Mob() {
    private var loaded = true

    init {
        PropertyConfiger.set(this, "Ballista")

        spriteClass = Sprite::class.java
    }

    override fun viewDistance(): Int = 6

    override fun giveDamage(enemy: Char): Damage = super.giveDamage(enemy).addFeature(Damage.Feature.RANGED)

    override fun canAttack(enemy: Char): Boolean {
        val trace = Ballistica(pos, enemy.pos, Ballistica.PROJECTILE)
        return trace.collisionPos == enemy.pos && loaded
    }

    override fun attack(enemy: Char): Boolean {
        loaded = false
        return super.attack(enemy)
    }

    override fun onAttackComplete() {
        // loaded = false
        if (Dungeon.level.adjacent(enemy.pos, pos))
            super.onAttackComplete()
        else {
            // show animation
            (sprite.parent.recycle(MissileSprite::class.java) as MissileSprite).reset(pos, enemy.pos, Dart(), Callback {
                next()
                if (enemy != null) attack(enemy)
            })
        }
    }

    override fun attackProc(dmg: Damage): Damage {
        // chance to knock back
        val chance = when (Dungeon.level.distance((dmg.from as Char).pos, (dmg.to as Char).pos)) {
            0, 1 -> 0.4f
            in 2..5 -> 0.25f
            else -> 0f
        }

        if (dmg.to is Char && Random.Float() < chance) {
            val tgt = dmg.to as Char
            val opposite = tgt.pos + (tgt.pos - pos)
            val shot = Ballistica(tgt.pos, opposite, Ballistica.MAGIC_BOLT)

            WandOfBlastWave.throwChar(tgt, shot, 1)
        }

        return super.attackProc(dmg)
    }

    override fun getCloser(target: Int): Boolean {
        return if (!loaded) {
            reload()
            true
        } else
            super.getCloser(target)
    }

    private fun reload() {
        loaded = true
        if (Dungeon.visible[pos]) {
            sprite.showStatus(0xffffff, Messages.get(this, "loaded"))
            Sample.INSTANCE.play(Assets.SND_RELOAD)
        }
    }

    override fun createLoot(): Item = when (Random.Int(4)) {
        0 -> Javelin(Random.IntRange(1, 3))
        1 -> Generator.WEAPON.MISSSILE.generate()
        else -> Gold().random()
    }

    override fun storeInBundle(bundle: Bundle) {
        super.storeInBundle(bundle)
        bundle.put("loaded", loaded)
    }

    override fun restoreFromBundle(bundle: Bundle) {
        super.restoreFromBundle(bundle)
        loaded = bundle.getBoolean("loaded")
    }

    override fun immunizedBuffs(): HashSet<Class<*>> = IMMUNITIES

    companion object {
        val IMMUNITIES = hashSetOf<Class<*>>(Amok::class.java, Terror::class.java, Sleep::class.java)

        class Sprite : MobSprite() {
            init {
                texture(Assets.BALLISTA)

                val frames = TextureFilm(texture, 16, 16)

                idle = Animation(2, true)
                idle.frames(frames, 0, 0, 0, 1)

                run = Animation(2, true)
                run.frames(frames, 0, 2)

                attack = Animation(8, false)
                attack.frames(frames, 0, 2, 3)

                zap = attack.clone()

                die = Animation(8, false)
                die.frames(frames, 4, 5, 6)

                play(idle)
            }

            override fun blood(): Int = 0xff80706c.toInt()

            override fun onComplete(anim: Animation) {
                if (anim == die)
                    emitter().burst(ElmoParticle.FACTORY, 4)

                super.onComplete(anim)
            }
        }

    }
}