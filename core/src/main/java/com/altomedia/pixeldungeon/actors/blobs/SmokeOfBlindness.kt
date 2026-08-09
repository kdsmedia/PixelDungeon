package com.altomedia.pixeldungeon.actors.blobs

import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Actor
import com.altomedia.pixeldungeon.actors.buffs.Blindness
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.effects.BlobEmitter
import com.altomedia.pixeldungeon.effects.Speck
import com.altomedia.pixeldungeon.messages.Messages
import com.watabou.utils.Random

class SmokeOfBlindness : Blob() {
    override fun evolve() {
        super.evolve()

        for (cell in area.points.map { Dungeon.level.pointToCell(it) })
            if (cur[cell] > 0) {
                val ch = Actor.findChar(cell)
                if (ch != null && !ch.immunizedBuffs().contains(javaClass))
                    Buff.prolong(ch, Blindness::class.java, Random.Int(1, 3).toFloat())
            }
    }

    override fun use(emitter: BlobEmitter) {
        super.use(emitter)

        emitter.pour(Speck.factory(Speck.DPD_FOG), .25f)
    }

    override fun tileDesc(): String = Messages.get(this, "desc")
}