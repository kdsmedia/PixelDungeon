package com.altomedia.pixeldungeon.windows

import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.messages.M
import com.altomedia.pixeldungeon.sprites.ItemSprite

// now this only work of item sell
open class WndTradeItem(item: Item, private val owner: WndBag?) :
        WndOptions(ItemSprite(item), item.name(), item.info()) {

    override fun hide() {
        super.hide()

        if (owner != null) {
            owner.hide()
            // Merchant.sell
        }
    }

    protected open fun sell(item: Item) {

    }

    companion object {
        private const val GAP = 2f
        private const val WIDTH = 120f
        private const val BTN_HEIGHT = 16
    }
}