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
package com.altomedia.pixeldungeon.actors.mobs;

import com.altomedia.pixeldungeon.PropertyConfiger;
import com.altomedia.pixeldungeon.actors.Char;
import com.altomedia.pixeldungeon.actors.Damage;
import com.altomedia.pixeldungeon.actors.buffs.Buff;
import com.altomedia.pixeldungeon.actors.buffs.Burning;
import com.altomedia.pixeldungeon.actors.buffs.Chill;
import com.altomedia.pixeldungeon.actors.buffs.Frost;
import com.altomedia.pixeldungeon.effects.Speck;
import com.altomedia.pixeldungeon.items.potions.PotionOfLiquidFlame;
import com.altomedia.pixeldungeon.items.wands.WandOfFireblast;
import com.altomedia.pixeldungeon.items.weapon.enchantments.Blazing;
import com.altomedia.pixeldungeon.levels.Level;
import com.altomedia.pixeldungeon.sprites.ElementalSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Elemental extends Mob {

  {
    PropertyConfiger.INSTANCE.set(this, "Elemental");
    spriteClass = ElementalSprite.class;

    loot = new PotionOfLiquidFlame();

    flying = true;
  }

  @Override
  public Damage giveDamage(Char target) {
    return super.giveDamage(target).addElement(Damage.Element.FIRE);
  }

  @Override
  public Damage attackProc(Damage damage) {
    Char enemy = (Char) damage.to;
    if (Random.Int(2) == 0) {
      Buff.affect(enemy, Burning.class).reignite(enemy);
    }

    return damage;
  }

  @Override
  public void add(Buff buff) {
    if (buff instanceof Burning) {
      if (HP < HT) {
        HP++;
        sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
      }
    } else if (buff instanceof Frost || buff instanceof Chill) {
      if (Level.Companion.getWater()[this.pos])
        takeDamage(new Damage(Random.NormalIntRange(HT / 2, HT), buff, this)
                .addElement(Damage.Element.ICE));
      else
        takeDamage(new Damage(Random.NormalIntRange(1, HT * 2 / 3), buff, 
                this).addElement(Damage.Element.ICE));
    } else {
      super.add(buff);
    }
  }

  private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

  static {
    IMMUNITIES.add(Burning.class);
    IMMUNITIES.add(Blazing.class);
    IMMUNITIES.add(WandOfFireblast.class);
  }

  @Override
  public HashSet<Class<?>> immunizedBuffs() {
    return IMMUNITIES;
  }
}
