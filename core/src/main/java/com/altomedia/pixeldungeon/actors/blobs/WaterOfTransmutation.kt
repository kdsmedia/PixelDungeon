package com.altomedia.pixeldungeon.actors.blobs

import com.altomedia.pixeldungeon.Journal
import com.altomedia.pixeldungeon.effects.BlobEmitter
import com.altomedia.pixeldungeon.effects.Speck
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.Generator
import com.altomedia.pixeldungeon.items.artifacts.Artifact
import com.altomedia.pixeldungeon.items.helmets.Helmet
import com.altomedia.pixeldungeon.items.potions.Potion
import com.altomedia.pixeldungeon.items.potions.PotionOfMight
import com.altomedia.pixeldungeon.items.potions.PotionOfStrength
import com.altomedia.pixeldungeon.items.rings.Ring
import com.altomedia.pixeldungeon.items.scrolls.Scroll
import com.altomedia.pixeldungeon.items.scrolls.ScrollOfUpgrade
import com.altomedia.pixeldungeon.items.wands.Wand
import com.altomedia.pixeldungeon.items.weapon.Weapon
import com.altomedia.pixeldungeon.items.weapon.melee.MagesStaff
import com.altomedia.pixeldungeon.items.weapon.melee.MeleeWeapon
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.plants.Plant

class WaterOfTransmutation : WellWater() {
    override fun affectItem(item: Item): Item? {
        val new = when (item) {
            is MagesStaff -> changeStaff(item)
            is MeleeWeapon -> changeWeapon(item)
            is Scroll -> changeScroll(item)
            is Potion -> changePotion(item)
            is Ring -> changeRing(item)
            is Wand -> changeWand(item)
            is Plant.Seed -> changeSeed(item)
            is Artifact -> changeArtifact(item)
            is Helmet -> changeHelmet(item)
            else -> null
        }

        if (new != null) Journal.remove(Journal.Feature.WELL_OF_TRANSMUTATION)
        return new
    }

    private fun changeStaff(staff: MagesStaff): MagesStaff? {
        return staff.wandClass()?.let {
            var wand = Generator.WAND.generate() as Wand
            while (wand.javaClass == it)
                wand = Generator.WAND.generate() as Wand
            wand.level(0)
            staff.imbueWand(wand, null)

            staff
        }
    }

    private fun changeWeapon(weap: MeleeWeapon): Weapon? {
        var newWeap = Generator.WEAPON.MELEE.tier(weap.tier).generate()
        while (weap.javaClass == newWeap.javaClass)
            newWeap = Generator.WEAPON.MELEE.tier(weap.tier).generate()

        val n = newWeap as Weapon
        n.level(0)
        val lvl = weap.level()
        if (lvl > 0) n.upgrade(lvl) else if (lvl < 0) n.degrade(-lvl)

        n.enchantment = weap.enchantment
        n.levelKnown = weap.levelKnown
        n.cursedKnown = weap.cursedKnown
        n.cursed = weap.cursed
        n.imbue = weap.imbue

        return n
    }

    private fun changeRing(r: Ring): Ring? {
        var n = Generator.RING.generate() as Ring
        while (n.javaClass == r.javaClass) n = Generator.RING.generate() as Ring
        n.level(0)

        val lvl = r.level()
        if (lvl > 0) n.upgrade(lvl) else if (lvl < 0) n.degrade(-lvl)

        n.levelKnown = r.levelKnown
        n.cursedKnown = r.cursedKnown
        n.cursed = r.cursed

        return n
    }

    private fun changeArtifact(a: Artifact): Artifact? {
        val n = Generator.ARTIFACT.generate()

        if (n !is Artifact) return null // if we run out of artifacts, do nothing

        n.cursedKnown = a.cursedKnown
        n.cursed = a.cursed
        n.levelKnown = a.levelKnown
        n.transferUpgrade(a.visiblyUpgraded())

        return n
    }

    private fun changeWand(w: Wand): Wand? {
        var n = Generator.WAND.generate() as Wand
        while (n.javaClass == w.javaClass) n = Generator.WAND.generate() as Wand
        n.level(0)

        n.upgrade(w.level())

        n.levelKnown = w.levelKnown
        n.cursedKnown = w.cursedKnown
        n.cursed = w.cursed

        return n
    }

    private fun changeSeed(s: Plant.Seed): Plant.Seed? {
        var n = Generator.SEED.generate() as Plant.Seed
        while (n.javaClass == s.javaClass) n = Generator.SEED.generate() as Plant.Seed

        return n
    }

    private fun changeScroll(s: Scroll): Scroll? = if (s is ScrollOfUpgrade) null else {
        var n = Generator.SCROLL.generate() as Scroll
        while (n.javaClass == s.javaClass) n = Generator.SCROLL.generate() as Scroll
        n
    }

    private fun changePotion(p: Potion): Potion? = when (p) {
        is PotionOfStrength -> PotionOfMight()
        is PotionOfMight -> PotionOfStrength()
        else -> {
            var n = Generator.POTION.generate() as Potion
            while (n.javaClass == p.javaClass) n = Generator.POTION.generate() as Potion
            n
        }
    }

    private fun changeHelmet(h: Helmet): Helmet? {
        var n = Generator.HELMET.generate() as Helmet
        while (n.javaClass == h.javaClass) n = Generator.HELMET.generate() as Helmet
        n.cursed = h.cursed
        n.cursedKnown = h.cursedKnown

        return n
    }

    override fun use(emitter: BlobEmitter) {
        super.use(emitter)
        emitter.pour(Speck.factory(Speck.CHANGE), 0.3f)
    }

    override fun tileDesc(): String = Messages.get(this, "desc")
}