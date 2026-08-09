package com.altomedia.pixeldungeon.actors.mobs.npcs

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.Statistics
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.messages.M
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.sprites.MobSprite
import com.altomedia.pixeldungeon.utils.GLog
import com.altomedia.pixeldungeon.windows.WndDialogue
import com.altomedia.pixeldungeon.windows.WndOptions
import com.altomedia.pixeldungeon.windows.WndQuest
import com.watabou.noosa.MovieClip
import com.watabou.noosa.TextureFilm

/**
 * Created by 93942 on 10/10/2018.
 */

class Minstrel : NPC.Unbreakable() {
    init {
        spriteClass = MinstrelSprite::class.java
    }

    override fun interact(): Boolean {
        sprite.turnTo(pos, Dungeon.hero.pos)

        WndDialogue.Show(this, M.L(this, "hello"),
                M.L(this, "ac_sing"), M.L(this, "ac_yourself"), M.L(this, "ac_leave")) {
            onSelectHello(it)
        }

        return false
    }

    // unbreakable
    override fun reset() = true

    override fun act(): Boolean {
        // leave after some time
        if (Statistics.Duration > 8000f) {
            die(null)
        }

        return super.act()
    }

    private fun onSelectHello(index: Int) {
        // 0 sing, 1 leave
        when (index) {
            0 -> {
                val poetries = arrayOf("away")
                GameScene.show(object : WndOptions(MinstrelSprite(), name,
                        Messages.get(Minstrel::class.java, "select_poetry"),
                        *poetries) {
                    override fun onSelect(index: Int) {
                        tell(Messages.get(Minstrel::class.java, "poetry_" + poetries[index]))
                    }
                })
            }
            1 -> tell(Messages.get(Minstrel::class.java, "introduction"))
            2 -> GLog.p(Messages.get(Minstrel::class.java, "farewell"))
        }
    }

    class MinstrelSprite : MobSprite() {
        init {
            texture(Assets.MINSTREL)

            // set animations
            val frames = TextureFilm(texture, 12, 15)
            idle = Animation(1, true)
            idle.frames(frames, 0, 1)

            run = Animation(20, true)
            run.frames(frames, 0)

            die = Animation(20, true)
            die.frames(frames, 0)

            play(idle)
        }

    }
}
