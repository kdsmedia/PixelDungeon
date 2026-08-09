package com.altomedia.pixeldungeon.items.food

import com.altomedia.pixeldungeon.actors.buffs.Hunger
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet

class StewedMeat : Food(Hunger.STARVING - Hunger.HUNGRY, 1) {
    init {
        image = ItemSpriteSheet.STEWED
    }

    override fun price(): Int = 6 * quantity

    companion object {
        fun Cook(meat: MysteryMeat): Food = StewedMeat().apply {
            quantity = meat.quantity()
        }
    }
}