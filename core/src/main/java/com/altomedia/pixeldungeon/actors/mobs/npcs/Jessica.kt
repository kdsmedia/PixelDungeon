package com.altomedia.pixeldungeon.actors.mobs.npcs

import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.items.Heap
import com.altomedia.pixeldungeon.items.Generator
import com.altomedia.pixeldungeon.items.books.textbook.CallysDiary
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.messages.M
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.sprites.JessicaSprite
import com.altomedia.pixeldungeon.utils.GLog
import com.altomedia.pixeldungeon.windows.WndDialogue
import com.watabou.utils.Bundle
import com.watabou.utils.Random

/**
 * Created by 93942 on 5/5/2018.
 */

class Jessica : NPC.Unbreakable() {
    init {
        spriteClass = JessicaSprite::class.java
    }

    /// do something
    override fun interact(): Boolean {
        sprite.turnTo(pos, Dungeon.hero.pos)
        if (!Quest.completed_) {
            val cd = Dungeon.hero.belongings.getItem(CallysDiary::class.java)
            if (cd == null) {
                Quest.given_ = true
                WndDialogue.Show(this, M.L(this, "please"), M.L(this, "ok"), M.L(this, "sorry")) {}
            } else {
                cd.detach(Dungeon.hero.belongings.backpack)
                GLog.w(Messages.get(this, "return_book"))
                Quest.completed_ = true
                tell(Messages.get(this, "thank_you"))
            }
        } else {
            tell(Messages.get(this, "farewell"))
        }

        return false
    }

    // unbreakable
    override fun reset(): Boolean {
        return true
    }

    override fun description(): String {
        return Messages.get(this, if (Quest.completed_) "desc_2" else "desc")
    }

    object Quest {

        var spawned_: Boolean = false
        var given_: Boolean = false
        var completed_: Boolean = false

        // bundle
        private const val NODE = "jessica"

        private const val SPAWNED = "spawned"
        private const val GIVEN = "given"
        private const val COMPLETED = "completed"

        fun reset() {
            spawned_ = false
            given_ = false
            completed_ = false
        }

        fun storeInBundle(bundle: Bundle) {
            val node = Bundle()
            node.put(SPAWNED, spawned_)
            node.put(GIVEN, given_)
            node.put(COMPLETED, completed_)

            bundle.put(NODE, node)
        }

        fun restoreFromBundle(bundle: Bundle) {
            val node = bundle.getBundle(NODE)
            if (!node.isNull) {
                spawned_ = node.getBoolean(SPAWNED)
                given_ = node.getBoolean(GIVEN)
                completed_ = node.getBoolean(COMPLETED)
            } else {
                reset()
            }
        }

        // prison level indeed
        fun spawnBook(level: Level): Boolean {
            if (!given_ || spawned_)
                return true

            if (Dungeon.depth > 5 && Random.Int(10 - Dungeon.depth) == 0) {
                val heap = Heap()
                heap.type = Heap.Type.SKELETON
                // heap.drop(new Book().setTitle(Book.Title.COLLIES_DIARY));
                heap.drop(CallysDiary())
                heap.drop(Generator.RING.generate())

                heap.pos = level.randomRespawnCell()
                level.heaps.put(heap.pos, heap)

                //        level.heaps.put(level.randomRespawnCell(), heap);
                spawned_ = true
            }

            return true
        }
    }

}
