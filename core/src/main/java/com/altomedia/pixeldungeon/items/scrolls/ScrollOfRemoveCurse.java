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

import com.altomedia.pixeldungeon.actors.buffs.Weakness;
import com.altomedia.pixeldungeon.actors.hero.Hero;
import com.altomedia.pixeldungeon.items.armor.Armor;
import com.altomedia.pixeldungeon.items.bags.Bag;
import com.altomedia.pixeldungeon.items.weapon.Weapon;
import com.altomedia.pixeldungeon.utils.GLog;
import com.altomedia.pixeldungeon.windows.WndBag;
import com.altomedia.pixeldungeon.effects.Flare;
import com.altomedia.pixeldungeon.effects.particles.ShadowParticle;
import com.altomedia.pixeldungeon.items.Item;
import com.altomedia.pixeldungeon.items.rings.Ring;
import com.altomedia.pixeldungeon.messages.Messages;

public class ScrollOfRemoveCurse extends InventoryScroll {

  {
    initials = 8;
    mode = WndBag.Mode.UNIDED_OR_CURSED;
  }

  @Override
  protected void onItemSelected(Item item) {
    new Flare(6, 32).show(curUser.sprite, 2f);

    boolean procced = uncurse(curUser, item);

    Weakness.detach(curUser, Weakness.class);

    if (procced) {
      GLog.p(Messages.get(this, "cleansed"));
    } else {
      GLog.i(Messages.get(this, "not_cleansed"));
    }
  }

  public static boolean uncurse(Hero hero, Item... items) {

    boolean procced = false;
    for (Item item : items) {
      if(item!=null){
        if(item.cursed) procced = true;
        
        item.cursed = false;
        item.cursedKnown = true;
      }
      
      if (item instanceof Weapon) {
        Weapon w = (Weapon) item;
        if (w.hasCurseEnchant()) {
          w.enchant(null);
          w.cursed = false;
          procced = true;
        }
      }
      if (item instanceof Armor) {
        Armor a = (Armor) item;
        if (a.hasCurseGlyph()) {
          a.inscribe(null);
          a.cursed = false;
          procced = true;
        }
      }
      if (item instanceof Ring && item.level() <= 0) {
        item.upgrade(1 - item.level());
      }
      if (item instanceof Bag) {
        for (Item bagItem : ((Bag) item).items) {
          if (bagItem != null && bagItem.cursed) {
            bagItem.cursed = false;
            procced = true;
          }
        }
      }
    }
    
    if (procced) 
      hero.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);

    return procced;
  }

  @Override
  public int price() {
    return isKnown() ? 30 * quantity : super.price();
  }
}
