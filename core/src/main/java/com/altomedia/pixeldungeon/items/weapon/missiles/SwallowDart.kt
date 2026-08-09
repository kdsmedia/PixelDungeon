package com.altomedia.pixeldungeon.items.weapon.missiles

import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet

class SwallowDart(number: Int = 1) : MissileWeapon(2) {
    init {
        image = ItemSpriteSheet.SWALLOW_DART

        quantity = number
        ACC = 1.25f
    }
}