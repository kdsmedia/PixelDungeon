package com.altomedia.pixeldungeon.items.helmets

import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet

/**
 * Created by 93942 on 9/24/2018.
 */

class CrownOfDwarf : Helmet() {
    init {
        image = ItemSpriteSheet.DWARF_CROWN
    }

    override fun isIdentified(): Boolean = true

    override fun price() = 500
}
