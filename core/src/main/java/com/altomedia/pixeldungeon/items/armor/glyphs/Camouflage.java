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
package com.altomedia.pixeldungeon.items.armor.glyphs;

import com.altomedia.pixeldungeon.Assets;
import com.altomedia.pixeldungeon.actors.Char;
import com.altomedia.pixeldungeon.actors.Damage;
import com.altomedia.pixeldungeon.actors.buffs.Invisibility;
import com.altomedia.pixeldungeon.items.armor.Armor;
import com.altomedia.pixeldungeon.messages.Messages;
import com.altomedia.pixeldungeon.sprites.ItemSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class Camouflage extends Armor.Glyph {

  private static ItemSprite.Glowing GREEN = new ItemSprite.Glowing(0x448822);

  @Override
  public Damage proc(Armor armor, Damage damage) {
    //no proc effect, see HighGrass.trample
    return damage;
  }

  @Override
  public ItemSprite.Glowing glowing() {
    return GREEN;
  }

  public static class Camo extends Invisibility {
    private int pos;
    private int left;

    @Override
    public boolean act() {
      left--;
      if (left == 0 || target.pos != pos) {
        detach();
      } else {
        spend(TICK);
      }
      return true;
    }

    public void set(int time) {
      left = time;
      pos = target.pos;
      Sample.INSTANCE.play(Assets.SND_MELD);
    }

    @Override
    public String toString() {
      return Messages.get(this, "name");
    }

    @Override
    public String desc() {
      return Messages.get(this, "desc", dispTurns(left));
    }

    private static final String POS = "pos";
    private static final String LEFT = "left";

    @Override
    public void storeInBundle(Bundle bundle) {
      super.storeInBundle(bundle);
      bundle.put(POS, pos);
      bundle.put(LEFT, left);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
      super.restoreFromBundle(bundle);
      pos = bundle.getInt(POS);
      left = bundle.getInt(LEFT);
    }
  }

}

