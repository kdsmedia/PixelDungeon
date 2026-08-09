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

import com.altomedia.pixeldungeon.Dungeon;
import com.altomedia.pixeldungeon.PropertyConfiger;
import com.altomedia.pixeldungeon.actors.Actor;
import com.altomedia.pixeldungeon.actors.Char;
import com.altomedia.pixeldungeon.actors.Damage;
import com.altomedia.pixeldungeon.actors.buffs.Buff;
import com.altomedia.pixeldungeon.actors.buffs.Burning;
import com.altomedia.pixeldungeon.actors.buffs.Corruption;
import com.altomedia.pixeldungeon.actors.buffs.Poison;
import com.altomedia.pixeldungeon.effects.Pushing;
import com.altomedia.pixeldungeon.items.potions.PotionOfHealing;
import com.altomedia.pixeldungeon.levels.Level;
import com.altomedia.pixeldungeon.levels.Terrain;
import com.altomedia.pixeldungeon.levels.features.Door;
import com.altomedia.pixeldungeon.scenes.GameScene;
import com.altomedia.pixeldungeon.sprites.SwarmSprite;
import com.altomedia.pixeldungeon.items.Item;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Swarm extends Mob {

  {
    PropertyConfiger.INSTANCE.set(this, "Swarm");

    spriteClass = SwarmSprite.class;
    loot = new PotionOfHealing();
  }

  private static final float SPLIT_DELAY = 1f;

  int generation = 0;

  private static final String GENERATION = "generation";

  @Override
  public void storeInBundle(Bundle bundle) {
    super.storeInBundle(bundle);
    bundle.put(GENERATION, generation);
  }

  @Override
  public void restoreFromBundle(Bundle bundle) {
    super.restoreFromBundle(bundle);
    generation = bundle.getInt(GENERATION);
    if (generation > 0) EXP = 0;
  }

  @Override
  public Damage giveDamage(Char target) {
    return Random.Int(3) == 0 ?
            new Damage(Random.NormalIntRange(1, 3), this, target).type(Damage
                    .Type.MENTAL) :
            new Damage(Random.NormalIntRange(1, 4), this, target);
  }

  @Override
  public Damage defenseProc(Damage damage) {
    // Char enemy	=	

    if (HP >= damage.value + 2) {
      ArrayList<Integer> candidates = new ArrayList<>();
      boolean[] passable = Level.Companion.getPassable();

      int[] neighbours = {pos + 1, pos - 1, pos + Dungeon.level.width(), pos
              - Dungeon.level.width()};
      for (int n : neighbours) {
        if (passable[n] && Actor.findChar(n) == null) {
          candidates.add(n);
        }
      }

      if (candidates.size() > 0) {

        Swarm clone = split();
        clone.HP = (HP - damage.value) / 2;
        clone.pos = Random.element(candidates);
        clone.state = clone.HUNTING;
        clone.properties.add(Property.PHANTOM);

        if (Dungeon.level.getMap()[clone.pos] == Terrain.DOOR) {
          Door.INSTANCE.Enter(clone.pos, clone);
        }

        GameScene.add(clone, SPLIT_DELAY);
        addDelayed(new Pushing(clone, pos, clone.pos), -1);

        HP -= clone.HP;
      }
    }

    return super.defenseProc(damage);
  }

  private Swarm split() {
    Swarm clone = new Swarm();
    clone.generation = generation + 1;
    clone.EXP = 0;
    if (buff(Burning.class) != null) {
      Buff.affect(clone, Burning.class).reignite(clone);
    }
    if (buff(Poison.class) != null) {
      Buff.affect(clone, Poison.class).set(2);
    }
    if (buff(Corruption.class) != null) {
      Buff.affect(clone, Corruption.class);
    }
    return clone;
  }

  @Override
  public void die(Object cause) {
    //sets drop chance
    lootChance = 1f / ((6 + 2 * Dungeon.limitedDrops.swarmHP.count) *
            (generation + 1));
    super.die(cause);
  }

  @Override
  protected Item createLoot() {
    Dungeon.limitedDrops.swarmHP.count++;
    return super.createLoot();
  }
}
