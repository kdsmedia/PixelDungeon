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

import com.altomedia.pixeldungeon.Dungeon;
import com.altomedia.pixeldungeon.actors.Actor;
import com.altomedia.pixeldungeon.actors.Char;
import com.altomedia.pixeldungeon.actors.mobs.Mob;
import com.altomedia.pixeldungeon.effects.particles.WindParticle;
import com.altomedia.pixeldungeon.items.Heap;
import com.altomedia.pixeldungeon.levels.Level;
import com.altomedia.pixeldungeon.levels.Terrain;
import com.altomedia.pixeldungeon.scenes.GameScene;
import com.altomedia.pixeldungeon.sprites.TrapSprite;
import com.altomedia.pixeldungeon.items.Item;
import com.altomedia.pixeldungeon.levels.features.Chasm;

public class PitfallTrap extends Trap {

  {
    color = TrapSprite.RED;
    shape = TrapSprite.DIAMOND;
  }

  @Override
  public void activate() {
    Heap heap = Dungeon.level.getHeaps().get(pos);

    if (heap != null) {
      for (Item item : heap.getItems()) {
        Dungeon.dropToChasm(item);
      }
      heap.getSprite().kill();
      GameScene.discard(heap);
      Dungeon.level.getHeaps().remove(pos);
    }

    Char ch = Actor.findChar(pos);

    if (ch == Dungeon.hero) {
      Chasm.INSTANCE.HeroFall(pos);
    } else if (ch != null) {
      Chasm.INSTANCE.MobFall((Mob) ch);
    }
  }

  @Override
  protected void disarm() {
    super.disarm();

    //if making a pit here wouldn't block any paths, make a pit tile instead 
    // of a disarmed trap tile.
    if (!(Dungeon.level.Companion.getSolid()[pos - Dungeon.level.width()] && Dungeon.level.Companion.getSolid()[pos + Dungeon.level.width()])
            && !(Dungeon.level.Companion.getSolid()[pos - 1] && Dungeon.level.Companion.getSolid()[pos +
            1])) {

      int c = Dungeon.level.getMap()[pos - Dungeon.level.width()];

      if (c == Terrain.WALL || c == Terrain.WALL_DECO) {
        Level.Companion.set(pos, Terrain.CHASM_WALL);
      } else {
        Level.Companion.set(pos, Terrain.CHASM_FLOOR);
      }

      sprite.parent.add(new WindParticle.Wind(pos));
      sprite.kill();
      GameScene.updateMap(pos);
    }
  }
}
