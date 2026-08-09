package com.altomedia.pixeldungeon.actors.mobs.npcs

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.sprites.MobSprite
import com.altomedia.pixeldungeon.windows.WndOptions
import com.watabou.noosa.MovieClip
import com.watabou.noosa.TextureFilm

class RobotREN : NPC() {
    init {
        spriteClass = Sprite::class.java

        properties.add(Property.IMMOVABLE)
    }

    override fun interact(): Boolean {
        sprite.turnTo(pos, Dungeon.hero.pos)

        GameScene.show(object : WndOptions(Sprite(), name,
                Messages.get(RobotREN::class.java, "greetings"),
                Messages.get(RobotREN::class.java, "ac_yourself"),
                Messages.get(RobotREN::class.java, "ac_wherefrom")) {
            override fun onSelect(index: Int) {
                onSelectHero(index)
            }
        })

        return false
    }

    private fun onSelectHero(index: Int) {
        when (index) {
            0 -> tell(Messages.get(this, "introduction"))
            1 -> tell(Messages.get(this, "wherefrom"))
        }
    }

    companion object {
        class Sprite : MobSprite() {
            init {
                texture(Assets.REN)

                val frames = TextureFilm(texture, 12, 14)
                idle = MovieClip.Animation(1, true)
                idle.frames(frames, 0, 1, 2, 3)

                run = MovieClip.Animation(20, true)
                run.frames(frames, 0)

                die = MovieClip.Animation(20, true)
                die.frames(frames, 0)

                play(idle)
            }
        }
    }

    // unbreakable
    override fun reset() = true

    override fun act(): Boolean {
        throwItem()
        return super.act()
    }

    override fun defenseSkill(enemy: Char): Float = 1000f

    override fun takeDamage(dmg: Damage): Int = 0

    override fun add(buff: Buff) = Unit
}