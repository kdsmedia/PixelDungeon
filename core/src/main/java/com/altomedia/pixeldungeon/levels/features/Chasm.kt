package com.altomedia.pixeldungeon.levels.features

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Badges
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.actors.buffs.Bleeding
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.actors.buffs.Cripple
import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.actors.mobs.Mob
import com.altomedia.pixeldungeon.actors.mobs.npcs.AbyssHero
import com.altomedia.pixeldungeon.actors.mobs.npcs.GhostHero
import com.altomedia.pixeldungeon.items.artifacts.TimekeepersHourglass
import com.altomedia.pixeldungeon.levels.RegularLevel
import com.altomedia.pixeldungeon.levels.diggers.DigResult
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.scenes.InterlevelScene
import com.altomedia.pixeldungeon.sprites.MobSprite
import com.altomedia.pixeldungeon.utils.GLog
import com.altomedia.pixeldungeon.windows.WndOptions
import com.watabou.noosa.Camera
import com.watabou.noosa.Game
import com.watabou.noosa.audio.Sample
import com.watabou.utils.Random

object Chasm {
    var JumpConfirmed = false

    fun HeroJump(hero: Hero) {
        GameScene.show(object : WndOptions(Messages.get(Chasm::class.java, "chasm"),
                Messages.get(Chasm::class.java, "jump"),
                Messages.get(Chasm::class.java, "yes"), Messages.get(Chasm::class.java, "no")) {
            override fun onSelect(index: Int) {
                if (index == 0) {
                    JumpConfirmed = true
                    hero.resume()
                }
            }
        })
    }

    fun HeroFall(pos: Int) {
        JumpConfirmed = false

        Sample.INSTANCE.play(Assets.SND_FALLING)

        Dungeon.hero.buff(TimekeepersHourglass.TimeFreeze::class.java)?.detach()

        Dungeon.level.mobs.filter { it is GhostHero || it is AbyssHero }.forEach { it.destroy() }

        if (!Dungeon.hero.isAlive) {
            // dead before jump?...
            Dungeon.hero.sprite.visible = false
        } else {
            Dungeon.hero.interrupt()
            InterlevelScene.mode = InterlevelScene.Mode.FALL

            InterlevelScene.fallIntoPit = (Dungeon.level is RegularLevel) &&
                    (Dungeon.level as RegularLevel).spaceAt(pos)?.type == DigResult.Type.WeakFloor

            Game.switchScene(InterlevelScene::class.java)
        }
    }

    fun HeroLand() {
        Dungeon.hero.apply {
            sprite.burst(sprite.blood(), 10)
            takeDamage(Damage(Random.NormalIntRange(HP / 4, HT / 4), {
                Badges.validateDeathFromFalling()
                Dungeon.fail(javaClass)
                GLog.n(Messages.get(Chasm::class.java, "ondeath"))
            }, this))

            Buff.prolong(this, Cripple::class.java, Cripple.DURATION)
            Buff.affect(this, Bleeding::class.java).set(HT / 8)
        }

        Camera.main.shake(4f, 0.2f)
    }

    fun MobFall(mob: Mob) {
        mob.die(null)
        (mob.sprite as MobSprite).fall()
    }
}