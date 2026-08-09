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
package com.altomedia.pixeldungeon.items.armor.curses;

import com.altomedia.pixeldungeon.actors.Damage;
import com.altomedia.pixeldungeon.actors.hero.Hero;
import com.altomedia.pixeldungeon.Dungeon;
import com.altomedia.pixeldungeon.DarkestPixelDungeon;
import com.altomedia.pixeldungeon.actors.Actor;
import com.altomedia.pixeldungeon.actors.Char;
import com.altomedia.pixeldungeon.actors.mobs.Bestiary;
import com.altomedia.pixeldungeon.actors.mobs.Mob;
import com.altomedia.pixeldungeon.actors.mobs.npcs.MirrorImage;
import com.altomedia.pixeldungeon.items.armor.Armor;
import com.altomedia.pixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.altomedia.pixeldungeon.levels.Level;
import com.altomedia.pixeldungeon.scenes.GameScene;
import com.altomedia.pixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Multiplicity extends Armor.Glyph {

  private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing(0x000000);

  @Override
  public Damage proc(Armor armor, Damage damage) {
    Char attacker = (Char) damage.from;
    Char defender = (Char) damage.to;

    if (Random.Int(20) == 0) {
      ArrayList<Integer> spawnPoints = new ArrayList<>();

      for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
        int p = defender.pos + PathFinder.NEIGHBOURS8[i];
        if (Actor.findChar(p) == null && (Level.Companion.getPassable()[p] || Level.Companion.getAvoid()[p])) {
          spawnPoints.add(p);
        }
      }

      if (spawnPoints.size() > 0) {

        Mob m = null;
        if (Random.Int(2) == 0 && defender instanceof Hero) {
          m = new MirrorImage();
          ((MirrorImage) m).duplicate((Hero) defender);

        } else {
          if (attacker.properties().contains(Char.Property.BOSS) || attacker
                  .properties().contains(Char.Property.MINIBOSS)) {
            m = Bestiary.INSTANCE.mutable(Dungeon.depth % 5 == 0 ? Dungeon.depth - 1 :
                    Dungeon.depth);
          } else {
            try {
              m = (Mob) attacker.getClass().newInstance();
              Bundle store = new Bundle();
              attacker.storeInBundle(store);
              m.restoreFromBundle(store);
              m.HP = m.HT;
            } catch (Exception e) {
              DarkestPixelDungeon.reportException(e);
              m = null;
            }
          }

        }

        if (m != null) {
          GameScene.add(m);
          ScrollOfTeleportation.Companion.appear(m, Random.element(spawnPoints));
        }

      }
    }

    return damage;
  }

  @Override
  public ItemSprite.Glowing glowing() {
    return BLACK;
  }

  @Override
  public boolean curse() {
    return true;
  }
}
