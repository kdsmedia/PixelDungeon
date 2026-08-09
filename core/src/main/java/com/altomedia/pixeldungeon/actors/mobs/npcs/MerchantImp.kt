package com.altomedia.pixeldungeon.actors.mobs.npcs

import com.altomedia.pixeldungeon.effects.Speck

import com.altomedia.pixeldungeon.items.Generator
import com.altomedia.pixeldungeon.items.unclassified.Torch
import com.altomedia.pixeldungeon.items.armor.PlateArmor
import com.altomedia.pixeldungeon.items.scrolls.ScrollOfIdentify
import com.altomedia.pixeldungeon.items.scrolls.ScrollOfMagicMapping
import com.altomedia.pixeldungeon.items.scrolls.ScrollOfRemoveCurse
import com.altomedia.pixeldungeon.items.weapon.melee.Claymore
import com.altomedia.pixeldungeon.items.weapon.melee.WarHammer
import com.altomedia.pixeldungeon.sprites.ImpSprite
import com.watabou.utils.Random

/**
 * Created by 93942 on 8/21/2018.
 */

class MerchantImp : Merchant() {
    init {
        spriteClass = ImpSprite::class.java
    }

    override fun initSellItems() {
        // devil would be place by painter, here, add extra items
        repeat(2) {
            addItemToSell(Generator.POTION.generate())
        }

        addItemToSell(ScrollOfRemoveCurse())
        addItemToSell(ScrollOfMagicMapping())
        addItemToSell(Generator.SCROLL.generate())

        repeat(2) {
            addItemToSell(if (Random.Int(2) == 0) Generator.POTION.generate()
            else Generator.SCROLL.generate())
        }

        addItemToSell(Generator.WEAPON.MELEE.T5.generate().identify())

        repeat(2) {
            addItemToSell(Generator.WEAPON.MISSSILE.generate())
        }

        addItemToSell(PlateArmor().identify())

        repeat(3) { addItemToSell(Torch()) }
    }

    override fun flee() {
        destroy()

        sprite.emitter().burst(Speck.factory(Speck.WOOL), 15)
        sprite.killAndErase()
    }
}
