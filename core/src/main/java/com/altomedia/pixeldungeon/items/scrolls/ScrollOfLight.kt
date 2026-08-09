package com.altomedia.pixeldungeon.items.scrolls

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.buffs.Blindness
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.actors.buffs.Invisibility
import com.altomedia.pixeldungeon.actors.buffs.Light
import com.altomedia.pixeldungeon.actors.buffs.Shock
import com.altomedia.pixeldungeon.actors.mobs.Mob
import com.altomedia.pixeldungeon.effects.ExpandHalo
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.wands.WandOfBlastWave
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.mechanics.Ballistica
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.utils.GLog
import com.watabou.noosa.audio.Sample

/**
 * Created by 93942 on 10/15/2018.
 */

class ScrollOfLight : Scroll() {
    init {
        initials = 12

        bones = true
    }

    override fun doRead() {
        // light!
        ExpandHalo(8f, 48f).show(Item.curUser.sprite, 0.5f)
        Sample.INSTANCE.play(Assets.SND_READ)
        Invisibility.dispel()

        // give light, shock nearby mobs
        Buff.affect(Item.curUser, Light::class.java).prolong(10f)

        Dungeon.level.mobs.filter { Level.fieldOfView[it.pos] }.forEach {
            val dis = Dungeon.level.distance(it.pos, Item.curUser.pos)
            if (dis <= AFFECT_RANGE && it.isAlive) {
                Buff.prolong(it, Shock::class.java, (AFFECT_RANGE - dis).toFloat())

                val b = Ballistica(Item.curUser.pos, it.pos, Ballistica.MAGIC_BOLT)
                if (b.path.size > b.dist + 1) {
                    val bb = Ballistica(it.pos, b.path[b.dist + 1], Ballistica.MAGIC_BOLT)
                    WandOfBlastWave.throwChar(it, bb, AFFECT_RANGE - dis)
                }
            }
        }

        GLog.i(Messages.get(this, "cast"))

        setKnown()
        readAnimation()
    }

    companion object {

        private const val AFFECT_RANGE = 8
    }


}

