package com.altomedia.pixeldungeon.items.inscriptions

import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.items.EquipableItem
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.inscriptions.good.AntiMagic
import com.altomedia.pixeldungeon.items.inscriptions.good.Healthy
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.sprites.ItemSprite
import com.altomedia.pixeldungeon.utils.GLog
import com.watabou.utils.Bundlable
import com.watabou.utils.Bundle
import com.watabou.utils.Random

abstract class Inscription : Bundlable {

    // affection
    open fun procTakenDamage(equipment: EquipableItem, dmg: Damage) {}
    open fun procGivenDamage(dmg: Damage) {}
    open fun mhpFix(mhp: Int): Int = mhp

    abstract fun glowing(): ItemSprite.Glowing

    fun name(equipmentName: String): String = Messages.get(this, "name", equipmentName)

    fun name(): String = name(
            if (curse()) Messages.get(Item::class.java, "curse")
            else Messages.get(this, "inscription"))

    fun desc(): String = Messages.get(this, "desc")

    fun curse() = false

    fun checkIfKilledOwner(owner: Char): Boolean {
        return if (!owner.isAlive && owner is Hero) {
            Dungeon.fail(javaClass)
            GLog.n(Messages.get(this, "killed", name()))

            // Badges.validateDeathFromGlyph()
            true
        } else
            false
    }

    // todo: random

    override fun storeInBundle(bundle: Bundle) {}
    override fun restoreFromBundle(bundle: Bundle) {}

    companion object {
        fun RandomInscription(): Inscription = Random.chances(Inscriptions).newInstance()

        fun RandomCurse(): Inscription = Random.chances(Curses).newInstance()

        private val Inscriptions: HashMap<Class<out Inscription>, Float> = hashMapOf(
                AntiMagic::class.java to 1f, 
                Healthy::class.java to 1f
        )

        private val Curses: HashMap<Class<out Inscription>, Float> = hashMapOf()
    }
}

