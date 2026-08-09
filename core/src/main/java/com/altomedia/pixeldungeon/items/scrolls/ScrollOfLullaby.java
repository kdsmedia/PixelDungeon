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

import com.altomedia.pixeldungeon.actors.buffs.Buff;
import com.altomedia.pixeldungeon.actors.buffs.Invisibility;
import com.altomedia.pixeldungeon.actors.mobs.Mob;
import com.altomedia.pixeldungeon.levels.Level;
import com.altomedia.pixeldungeon.Assets;
import com.altomedia.pixeldungeon.Dungeon;
import com.altomedia.pixeldungeon.actors.buffs.Drowsy;
import com.altomedia.pixeldungeon.effects.Speck;
import com.altomedia.pixeldungeon.messages.Messages;
import com.altomedia.pixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfLullaby extends Scroll {

  {
    initials = 1;
  }

  @Override
  protected void doRead() {

    curUser.sprite.centerEmitter().start(Speck.factory(Speck.NOTE), 0.3f, 5);
    Sample.INSTANCE.play(Assets.SND_LULLABY);
    Invisibility.dispel();

    for (Mob mob : Dungeon.level.getMobs().toArray(new Mob[0])) {
      if (Level.Companion.getFieldOfView()[mob.pos]) {
        Buff.affect(mob, Drowsy.class);
        mob.sprite.centerEmitter().start(Speck.factory(Speck.NOTE), 0.3f, 5);
      }
    }

    Buff.affect(curUser, Drowsy.class);

    GLog.i(Messages.get(this, "sooth"));

    setKnown();

    readAnimation();
  }

  @Override
  public int price() {
    return isKnown() ? 40 * quantity : super.price();
  }
}
