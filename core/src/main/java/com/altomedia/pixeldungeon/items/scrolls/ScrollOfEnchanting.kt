/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.altomedia.pixeldungeon.items.scrolls

import com.altomedia.pixeldungeon.effects.Enchanting
import com.altomedia.pixeldungeon.effects.Speck
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.armor.Armor
import com.altomedia.pixeldungeon.items.weapon.Weapon
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.utils.GLog
import com.altomedia.pixeldungeon.windows.WndBag

class ScrollOfEnchanting : InventoryScroll() {

    init {
        initials = 2
        mode = WndBag.Mode.ENCHANTABLE

        bones = true
    }

    override fun onItemSelected(item: Item) {

        if (item is Weapon)
            item.enchant()
        else
            (item as Armor).inscribe()

        GLog.p(Messages.get(this, "enchant", item.name()))

        Item.curUser.sprite.emitter().start(Speck.factory(Speck.UP), 0.2f, 3)
        Enchanting.show(Item.curUser, item)
    }

    override fun price(): Int = if (isKnown) 40 * quantity else super.price()
}
