package com.altomedia.pixeldungeon.actors.mobs.npcs

import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.sprites.ScholarSprite
import com.altomedia.pixeldungeon.utils.GLog
import com.altomedia.pixeldungeon.windows.WndQuest

/**
 * Created by 93942 on 4/29/2018.
 */

class Scholar : NPC.Unbreakable() {

    init {
        name = Messages.get(this, "name")
        spriteClass = ScholarSprite::class.java
    }

    /// do something
    override fun interact(): Boolean {
        sprite.turnTo(pos, Dungeon.hero.pos)
        tell(Messages.get(this, "hello"))

        return false
    }

    override fun reset(): Boolean = true
}
