package com.altomedia.pixeldungeon.actors.mobs.npcs

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.items.unclassified.Amulet
import com.altomedia.pixeldungeon.messages.M
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.sprites.CharSprite
import com.altomedia.pixeldungeon.sprites.MobSprite
import com.altomedia.pixeldungeon.windows.WndDialogue
import com.altomedia.pixeldungeon.windows.WndOptions
import com.watabou.noosa.TextureFilm
import com.watabou.utils.Random

class SPDBattleMage : NPC() {
    init {
        spriteClass = Sprite::class.java

        properties.add(Property.IMMOVABLE)
    }

    override fun interact(): Boolean {
        sprite.turnTo(pos, Dungeon.hero.pos)

        if (Dungeon.hero.belongings.getItem(Amulet::class.java) == null) {
        } else {
        }

        WndDialogue.Show(this, M.L(this, "greetings"), M.L(this, "ac_yourself"), M.L(this, "ac_suggest")) {
            onSelectHero(it)
        }

        return false
    }

    private fun onSelectHero(index: Int) {
        when (index) {
            0 -> tell(Messages.get(this, "introduction"))
            1 -> {
                val a = Dungeon.hero.belongings.getItem(Amulet::class.java)
                tell(Messages.get(this, if (a == null) "suggestion_${Random.Int(4)}" else "suggestion_no"))
            }
        }
    }

    companion object {
        class Sprite : MobSprite() {
            init {
                texture(Assets.SPD_BATTLE_MAGE)

                val frames = TextureFilm(texture, 16, 16)

                idle = Animation(1, true)
                idle.frames(frames, 0, 1)

                run = Animation(20, true)
                run.frames(frames, 0)

                die = Animation(20, true)
                die.frames(frames, 0)

                play(idle)
            }

            override fun link(ch: Char) {
                super.link(ch)

                add(CharSprite.State.CHILLED)
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