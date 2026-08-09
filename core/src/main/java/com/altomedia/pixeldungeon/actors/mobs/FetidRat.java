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
import com.altomedia.pixeldungeon.actors.buffs.Ooze;
import com.altomedia.pixeldungeon.actors.mobs.npcs.Ghost;
import com.altomedia.pixeldungeon.actors.blobs.Blob;
import com.altomedia.pixeldungeon.actors.blobs.StenchGas;
import com.altomedia.pixeldungeon.scenes.GameScene;
import com.altomedia.pixeldungeon.sprites.FetidRatSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class FetidRat extends Rat {

  {
    PropertyConfiger.INSTANCE.set(this, "FetidRat");
    spriteClass = FetidRatSprite.class;

    state = WANDERING;
  }

  @Override
  public Damage attackProc(Damage damage) {
    if (Random.Int(3) == 0) {
      Buff.affect((Char) damage.to, Ooze.class);
    }

    return damage;
  }

  @Override
  public Damage defenseProc(Damage damage) {

    GameScene.add(Blob.seed(pos, 20, StenchGas.class));

    return super.defenseProc(damage);
  }

  @Override
  public void die(Object cause) {
    super.die(cause);

    Ghost.Quest.INSTANCE.process();
  }

  private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

  static {
    IMMUNITIES.add(StenchGas.class);
  }

  @Override
  public HashSet<Class<?>> immunizedBuffs() {
    return IMMUNITIES;
  }
}