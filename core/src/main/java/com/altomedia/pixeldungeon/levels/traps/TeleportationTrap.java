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

import com.altomedia.pixeldungeon.actors.mobs.Mob;
import com.altomedia.pixeldungeon.effects.CellEmitter;
import com.altomedia.pixeldungeon.Assets;
import com.altomedia.pixeldungeon.Dungeon;
import com.altomedia.pixeldungeon.actors.Actor;
import com.altomedia.pixeldungeon.actors.Char;
import com.altomedia.pixeldungeon.actors.hero.Hero;
import com.altomedia.pixeldungeon.effects.Speck;
import com.altomedia.pixeldungeon.items.Heap;
import com.altomedia.pixeldungeon.items.Item;
import com.altomedia.pixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.altomedia.pixeldungeon.messages.Messages;
import com.altomedia.pixeldungeon.sprites.TrapSprite;
import com.altomedia.pixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class TeleportationTrap extends Trap {

  {
    color = TrapSprite.TEAL;
    shape = TrapSprite.DOTS;
  }

  @Override
  public void activate() {

    CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
    Sample.INSTANCE.play(Assets.SND_TELEPORT);

    Char ch = Actor.findChar(pos);
    if (ch instanceof Hero) {
      ScrollOfTeleportation.Companion.teleportHero((Hero) ch);
    } else if (ch != null) {
      int count = 10;
      int pos;
      do {
        pos = Dungeon.level.randomRespawnCell();
        if (count-- <= 0) {
          break;
        }
      } while (pos == -1);

      if (pos == -1 || Dungeon.bossLevel()) {

        GLog.w(Messages.get(ScrollOfTeleportation.class, "no_tele"));

      } else {
        ch.pos = pos;
        if(ch instanceof Mob && ((Mob) ch).state== ((Mob) ch).HUNTING)
          ((Mob) ch).state = ((Mob) ch).WANDERING;
        
        ch.sprite.place(ch.pos);
        ch.sprite.visible = Dungeon.visible[pos];

      }
    }

    Heap heap = Dungeon.level.getHeaps().get(pos);

    if (heap != null) {
      int cell = Dungeon.level.randomRespawnCell();

      Item item = heap.pickUp();

      if (cell != -1) {
        Dungeon.level.drop(item, cell);
      }
    }
  }
}
