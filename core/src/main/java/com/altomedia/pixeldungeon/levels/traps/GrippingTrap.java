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
package com.altomedia.pixeldungeon.levels.traps;

import com.altomedia.pixeldungeon.actors.Actor;
import com.altomedia.pixeldungeon.actors.Char;
import com.altomedia.pixeldungeon.actors.Damage;
import com.altomedia.pixeldungeon.actors.buffs.Bleeding;
import com.altomedia.pixeldungeon.actors.buffs.Buff;
import com.altomedia.pixeldungeon.actors.buffs.Cripple;
import com.altomedia.pixeldungeon.actors.buffs.Roots;
import com.altomedia.pixeldungeon.effects.Wound;
import com.altomedia.pixeldungeon.Dungeon;
import com.altomedia.pixeldungeon.sprites.TrapSprite;

public class GrippingTrap extends Trap {

  {
    color = TrapSprite.GREY;
    shape = TrapSprite.CROSSHAIR;
  }

  @Override
  public void activate() {

    Char c = Actor.findChar(pos);

    if (c != null) {
      int damage = c.defendDamage(new Damage(Dungeon.depth, this, c)).value;
      if (damage < 0) damage = 0;
      Buff.affect(c, Bleeding.class).set(damage);
      Buff.prolong(c, Cripple.class, 15f);
      Buff.prolong(c, Roots.class, 5f);
      Wound.hit(c);
    } else {
      Wound.hit(pos);
    }

  }
}
