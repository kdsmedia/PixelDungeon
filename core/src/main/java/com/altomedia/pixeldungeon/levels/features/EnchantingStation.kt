package com.altomedia.pixeldungeon.levels.features

import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.armor.Armor
import com.altomedia.pixeldungeon.items.weapon.Weapon
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.utils.GLog
import com.altomedia.pixeldungeon.windows.WndEnchanting

object EnchantingStation {
    fun Operate(hero: Hero) {
        GameScene.show(WndEnchanting())
    }

    fun CanTransform(src: Item, tgt: Item): String? = when {
        !src.isIdentified || !tgt.isIdentified -> Messages.get(this, "unidentified")
        src.cursed || tgt.cursed -> Messages.get(this, "cursed")
        src is Weapon -> when {
            src.enchantment == null -> Messages.get(this, "no_enchantment")
            tgt !is Weapon -> Messages.get(this, "wrong_type")
            else -> null
        }
        src is Armor -> when {
            src.glyph == null -> Messages.get(this, "no_enchantment")
            tgt !is Armor -> Messages.get(this, "wrong_type")
            else -> null
        }
        else -> "bad operation." // never be here
    }

    fun Transform(src: Item, tgt: Item): Boolean {
        GLog.p(Messages.get(this, "transformed", src.name(), tgt.name()))

        return when (src) {
            is Weapon -> {
                (tgt as Weapon).enchant(src.enchantment)
                true
            }
            is Armor -> {
                (tgt as Armor).inscribe(src.glyph)
                src.checkSeal()?.let { brokenSeal -> tgt.affixSeal(brokenSeal) }
                true
            }
            else -> false
        }
    }

}