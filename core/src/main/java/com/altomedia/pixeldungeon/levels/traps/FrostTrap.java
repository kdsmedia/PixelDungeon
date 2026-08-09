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
import com.altomedia.pixeldungeon.actors.buffs.Chill;
import com.altomedia.pixeldungeon.actors.buffs.Frost;
import com.altomedia.pixeldungeon.effects.Splash;
import com.altomedia.pixeldungeon.utils.GLog;
import com.altomedia.pixeldungeon.Assets;
import com.altomedia.pixeldungeon.Dungeon;
import com.altomedia.pixeldungeon.items.Heap;
import com.altomedia.pixeldungeon.messages.Messages;
import com.altomedia.pixeldungeon.sprites.TrapSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class FrostTrap extends Trap {

  {
    color = TrapSprite.WHITE;
    shape = TrapSprite.STARS;
  }

  @Override
  public void activate() {

    if (Dungeon.visible[pos]) {
      Splash.at(sprite.center(), 0xFFB2D6FF, 10);
      Sample.INSTANCE.play(Assets.SND_SHATTER);
    }

    Heap heap = Dungeon.level.getHeaps().get(pos);
    if (heap != null) heap.freeze();

    Char ch = Actor.findChar(pos);
    if (ch != null) {
      ch.takeDamage(new Damage(Random.NormalIntRange(1, Dungeon.depth), this,
              ch).addElement(Damage.Element.ICE));
      Chill.prolong(ch, Frost.class, 10f + Random.Int(Dungeon.depth));
      if (!ch.isAlive() && ch == Dungeon.hero) {
        Dungeon.fail(getClass());
        GLog.n(Messages.get(this, "ondeath"));
      }
    }
  }
}
