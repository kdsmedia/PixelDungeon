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

import com.altomedia.pixeldungeon.Assets;
import com.altomedia.pixeldungeon.Dungeon;
import com.altomedia.pixeldungeon.actors.Actor;
import com.altomedia.pixeldungeon.actors.Char;
import com.altomedia.pixeldungeon.actors.Damage;
import com.altomedia.pixeldungeon.actors.buffs.Buff;
import com.altomedia.pixeldungeon.actors.buffs.Paralysis;
import com.altomedia.pixeldungeon.actors.mobs.Mob;
import com.altomedia.pixeldungeon.effects.CellEmitter;
import com.altomedia.pixeldungeon.effects.Speck;
import com.altomedia.pixeldungeon.levels.Level;
import com.altomedia.pixeldungeon.messages.Messages;
import com.altomedia.pixeldungeon.sprites.TrapSprite;
import com.altomedia.pixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.security.cert.TrustAnchor;

public class RockfallTrap extends Trap {

  {
    color = TrapSprite.GREY;
    shape = TrapSprite.DIAMOND;
  }

  @Override
  public void activate() {
    fallRocks(pos);
  }

  public static void fallRocks(int pos) {
    boolean seen = false;

    for (int i : PathFinder.NEIGHBOURS9) {

      if (Level.Companion.getSolid()[pos + i])
        continue;

      if (Dungeon.visible[pos + i]) {
        CellEmitter.get(pos + i - Dungeon.level.width()).start(Speck.factory
                (Speck.ROCK), 0.07f, 10);
        if (!seen) {
          Camera.main.shake(3, 0.7f);
          Sample.INSTANCE.play(Assets.SND_ROCKS);
          seen = true;
        }
      }

      Char ch = Actor.findChar(pos + i);

      if (ch != null) {
        int damage = Random.NormalIntRange(Dungeon.depth, Dungeon.depth * 2);
        ch.takeDamage(ch.defendDamage(new Damage(damage, new RockfallTrap(), 
                ch)));

        Buff.prolong(ch, Paralysis.class, Paralysis.duration(ch) / 2);

        if (!ch.isAlive() && ch == Dungeon.hero) {
          Dungeon.fail(RockfallTrap.class);
          GLog.n(Messages.get(RockfallTrap.class, "ondeath"));
        }
      }
    }
    
    for(Mob mob: Dungeon.level.getMobs().toArray(new Mob[0])){
      if(Dungeon.level.distance(mob.pos, pos)< 12)
        mob.beckon(pos);
    }
    GLog.n(Messages.get(RockfallTrap.class, "roar"));
  }
}
