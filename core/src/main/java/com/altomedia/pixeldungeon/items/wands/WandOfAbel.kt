package com.altomedia.pixeldungeon.items.wands

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Actor
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.actors.buffs.Paralysis
import com.altomedia.pixeldungeon.effects.MagicMissile
import com.altomedia.pixeldungeon.effects.Speck
import com.altomedia.pixeldungeon.items.Heap
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.weapon.enchantments.Stunning
import com.altomedia.pixeldungeon.items.weapon.melee.MagesStaff
import com.altomedia.pixeldungeon.mechanics.Ballistica
import com.altomedia.pixeldungeon.messages.M
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.altomedia.pixeldungeon.utils.GLog
import com.watabou.noosa.audio.Sample
import com.watabou.utils.Callback
import com.watabou.utils.Random

class WandOfAbel : DamageWand() {
    init {
        image = ItemSpriteSheet.WAND_SWAP

        collisionProperties = Ballistica.STOP_TARGET or Ballistica.STOP_TERRAIN
    }

    override fun min(lvl: Int): Int = 2 + lvl

    override fun max(lvl: Int): Int = 8 + lvl * 5 / 2

    override fun giveDamage(enemy: Char): Damage = super.giveDamage(enemy).addElement(Damage.Element.SHADOW)

    override fun onZap(bolt: Ballistica) {
        // swap with char
        val ch = Actor.findChar(bolt.collisionPos)
        if (ch != null) {
            // swap
            if (!ch.rooted && !Dungeon.hero.rooted && !ch.properties().contains(Char.Property.IMMOVABLE)) {
                swap(ch, Dungeon.hero)
                Dungeon.observe()
            } else GLog.w(M.L(this, "cannot_swap"))

            val dmg = giveDamage(ch)
            onMissileHit(ch, Item.curUser, dmg)
            ch.takeDamage(dmg)

            if (ch.isAlive)
                Buff.prolong(ch, Paralysis::class.java, 0.2f + level() / 5f)

            return
        }

        // swap with heap
        val heap = Dungeon.level.heaps.get(bolt.collisionPos)
        if (heap != null && !heap.empty()) {
            val heroPos = Item.curUser.pos
            moveChar(Item.curUser, bolt.collisionPos)

            val newHeap = Heap().apply {
                type = heap.type
                drop(heap.pickUp())
                pos = heroPos
            }
            Dungeon.level.heaps.put(newHeap.pos, newHeap)
            GameScene.add(newHeap)

            return
        }
    }

    override fun fx(bolt: Ballistica, callback: Callback) {
        MagicMissile.coldLight(Item.curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback)
        Sample.INSTANCE.play(Assets.SND_ZAP)
    }

    override fun onHit(staff: MagesStaff, damage: Damage) {
//        Stunning().proc(staff, damage)
        if (Random.Float() < 0.25f) {
            val defender = damage.to as Char
            Buff.prolong(defender, Paralysis::class.java, Random.Float(1f, 2f))
            defender.sprite.emitter().burst(Speck.factory(Speck.LIGHT), 12)
        }

    }

    override fun particleColor(): Int = 0x9efaff

    override fun staffFx(particle: MagesStaff.StaffParticle) {
        particle.color(particleColor())
        particle.am = 0.6f
        particle.setLifespan(0.6f)
        particle.acc.set(0f, 40f)
        particle.setSize(2f, 4f)
        particle.shuffleXY(2f)
    }

    private fun moveChar(ch: Char, pos: Int) {
        ch.sprite.move(ch.pos, pos)
        ch.move(pos)
    }

    private fun swap(ch1: Char, ch2: Char) {
        val pos1 = ch1.pos
        moveChar(ch1, ch2.pos)
        moveChar(ch2, pos1)
    }
}