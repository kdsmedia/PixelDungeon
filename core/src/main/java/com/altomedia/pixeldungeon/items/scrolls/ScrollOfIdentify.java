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
package com.altomedia.pixeldungeon.items.scrolls;

import com.altomedia.pixeldungeon.Badges;
import com.altomedia.pixeldungeon.effects.Identification;
import com.altomedia.pixeldungeon.messages.Messages;
import com.altomedia.pixeldungeon.utils.GLog;
import com.altomedia.pixeldungeon.windows.WndBag;
import com.altomedia.pixeldungeon.items.Item;

public class ScrollOfIdentify extends InventoryScroll {

  {
    initials = 0;
    mode = WndBag.Mode.UNIDENTIFED;

    bones = true;
  }

  @Override
  protected void onItemSelected(Item item) {

    curUser.sprite.parent.add(new Identification(curUser.sprite.center()
            .offset(0, -16)));

    item.identify();
    GLog.i(Messages.get(this, "it_is", item));

    Badges.validateItemLevelAquired(item);
  }

  @Override
  public int price() {
    return isKnown() ? 30 * quantity : super.price();
  }
}
