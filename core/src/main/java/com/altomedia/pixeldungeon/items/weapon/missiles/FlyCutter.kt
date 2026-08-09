package com.altomedia.pixeldungeon.items.weapon.missiles

import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet

class FlyCutter(number: Int = 1) : MissileWeapon(3) {
    init {
        image = ItemSpriteSheet.FLY_CUTTER

        quantity = number
    }
}