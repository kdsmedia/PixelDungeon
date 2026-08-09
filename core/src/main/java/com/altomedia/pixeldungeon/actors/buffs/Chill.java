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
package com.altomedia.pixeldungeon.actors.buffs;

import com.altomedia.pixeldungeon.actors.hero.Hero;
import com.altomedia.pixeldungeon.Dungeon;
import com.altomedia.pixeldungeon.actors.Char;
import com.altomedia.pixeldungeon.actors.mobs.Thief;
import com.altomedia.pixeldungeon.items.Item;
import com.altomedia.pixeldungeon.items.food.FrozenCarpaccio;
import com.altomedia.pixeldungeon.items.food.MysteryMeat;
import com.altomedia.pixeldungeon.items.potions.Potion;
import com.altomedia.pixeldungeon.items.potions.PotionOfMight;
import com.altomedia.pixeldungeon.items.potions.PotionOfStrength;
import com.altomedia.pixeldungeon.messages.Messages;
import com.altomedia.pixeldungeon.sprites.CharSprite;
import com.altomedia.pixeldungeon.ui.BuffIndicator;
import com.altomedia.pixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.text.DecimalFormat;

public class Chill extends FlavourBuff {

  {
    type = buffType.NEGATIVE;
  }

  @Override
  public boolean attachTo(Char target) {
    //can't chill what's frozen!
    if (target.buff(Frost.class) != null) return false;

    if (super.attachTo(target)) {
      detach(target, Burning.class);

      //chance of potion breaking is the same as speed factor.
      if (Random.Float(1f) > speedFactor() && target instanceof Hero) {

        Hero hero = (Hero) target;
        Item item = hero.getBelongings().randomUnequipped();
        if (item instanceof Potion
                && !(item instanceof PotionOfStrength || item instanceof 
                PotionOfMight)) {

          item = item.detach(hero.getBelongings().backpack);
          GLog.w(Messages.get(this, "freezes", item.toString()));
          ((Potion) item).shatter(hero.pos);

        } else if (item instanceof MysteryMeat) {

          item = item.detach(hero.getBelongings().backpack);
          FrozenCarpaccio carpaccio = new FrozenCarpaccio();
          if (!carpaccio.collect(hero.getBelongings().backpack)) {
            Dungeon.level.drop(carpaccio, target.pos).getSprite().drop();
          }
          GLog.w(Messages.get(this, "freezes", item.toString()));

        }
      } else if (target instanceof Thief) {

        Item item = ((Thief) target).item;

        if (item instanceof Potion && !(item instanceof PotionOfStrength || 
                item instanceof PotionOfMight)) {
          ((Potion) ((Thief) target).item).shatter(target.pos);
          ((Thief) target).item = null;
        }

      }
      return true;
    } else {
      return false;
    }
  }

  //reduces speed by 10% for every turn remaining, capping at 50%
  public float speedFactor() {
    return Math.max(0.5f, 1 - cooldown() * 0.1f);
  }

  @Override
  public int icon() {
    return BuffIndicator.FROST;
  }

  @Override
  public void fx(boolean on) {
    if (on) target.sprite.add(CharSprite.State.CHILLED);
    else target.sprite.remove(CharSprite.State.CHILLED);
  }

  @Override
  public String toString() {
    return Messages.get(this, "name");
  }

  @Override
  public String desc() {
    return Messages.get(this, "desc", dispTurns(), new DecimalFormat("#.##").format((1f - speedFactor()) * 100f));
  }
}
