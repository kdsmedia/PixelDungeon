package com.altomedia.pixeldungeon.levels.traps

import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.effects.CellEmitter
import com.altomedia.pixeldungeon.effects.particles.EarthParticle
import com.altomedia.pixeldungeon.items.Generator
import com.altomedia.pixeldungeon.items.Heap
import com.altomedia.pixeldungeon.messages.M
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.sprites.TrapSprite
import com.altomedia.pixeldungeon.utils.GLog
import com.watabou.utils.Random

// actually this is not a trap, huh
class PrizeTrap : Trap() {
    init {
        color = TrapSprite.VIOLET
        shape = TrapSprite.DIAMOND
    }

    override fun activate() {
        // todo: may use a specified heap type.
        val heap = Heap()
        heap.type = if (Random.Int(3) == 0) Heap.Type.SKELETON else Heap.Type.CHEST

        heap.drop(Generator.GOLD.generate()) // always give some gold
        heap.drop(Generator.generate())

        heap.pos = pos
        Dungeon.level.heaps.put(heap.pos, heap)
        GameScene.add(heap)

        if(Dungeon.visible[pos]) {
            heap.sprite.drop()
            GLog.p(M.L(this, "prize_showed"))
            CellEmitter.bottom(pos).start(EarthParticle.FACTORY, 0.05f, 8)
        }
    }
}