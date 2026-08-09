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

import com.altomedia.pixeldungeon.Journal;
import com.altomedia.pixeldungeon.actors.Char;
import com.altomedia.pixeldungeon.actors.Damage;
import com.altomedia.pixeldungeon.actors.buffs.Dementage;
import com.altomedia.pixeldungeon.items.Generator;
import com.altomedia.pixeldungeon.items.weapon.Weapon;
import com.altomedia.pixeldungeon.items.weapon.enchantments.Vampiric;
import com.altomedia.pixeldungeon.messages.Messages;
import com.altomedia.pixeldungeon.sprites.StatueSprite;
import com.altomedia.pixeldungeon.Dungeon;
import com.altomedia.pixeldungeon.items.weapon.melee.MeleeWeapon;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Statue extends Mob {

  {
    spriteClass = StatueSprite.class;

    EXP = 0;
    state = PASSIVE;

    addResistances(Damage.Element.all(), 0.15f);
    magicalResistance = 0.2f;
  }

  protected Weapon weapon;

  public Statue() {
    super();

    do {
      weapon = (Weapon) Generator.WEAPON.INSTANCE.generate();
    } while (!(weapon instanceof MeleeWeapon) || weapon.cursed);

    weapon.identify();
    weapon.enchant(Weapon.Enchantment.random());

    HP = HT = 15 + Dungeon.depth * 5;
    defSkill = 4 + Dungeon.depth;
  }

  private static final String WEAPON = "weapon";

  @Override
  public void storeInBundle(Bundle bundle) {
    super.storeInBundle(bundle);
    bundle.put(WEAPON, weapon);
  }

  @Override
  public void restoreFromBundle(Bundle bundle) {
    super.restoreFromBundle(bundle);
    weapon = (Weapon) bundle.get(WEAPON);
  }

  @Override
  protected boolean act() {
    if (Dungeon.visible[pos]) {
      Journal.INSTANCE.add(name);
    }
    return super.act();
  }

  @Override
  public Damage giveDamage(Char target) {
    return new Damage(Random.NormalIntRange(weapon.min(), weapon.max()),
            this, target);
  }

  @Override
  public Damage defendDamage(Damage dmg) {
    dmg = weapon.defendDamage(dmg);
    dmg.value -= Random.NormalIntRange(0, Dungeon.depth);

    return dmg;
  }

  @Override
  public float attackSkill(Char target) {
    return (9 + Dungeon.depth) * weapon.ACC;
  }

  @Override
  protected float attackDelay() {
    return weapon.DLY;
  }

  @Override
  protected boolean canAttack(Char enemy) {
    return Dungeon.level.distance(pos, enemy.pos) <= weapon.RCH;
  }

  @Override
  public int takeDamage(Damage dmg) {

    if (state == PASSIVE) {
      state = HUNTING;
    }

    return super.takeDamage(dmg);
  }

  @Override
  public Damage attackProc(Damage damage) {
    return weapon.proc(damage);
  }

  @Override
  public void beckon(int cell) {
    // Do nothing
  }

  @Override
  public void die(Object cause) {
    Dungeon.level.drop(weapon, pos).getSprite().drop();
    super.die(cause);
  }

  @Override
  public void destroy() {
    Journal.INSTANCE.remove(name);
    super.destroy();
  }

  @Override
  public boolean reset() {
    state = PASSIVE;
    return true;
  }

  @Override
  public String description() {
    return Messages.get(this, "desc", weapon.name());
  }

  private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

  static {
    IMMUNITIES.add(Vampiric.class);
    IMMUNITIES.add(Dementage.class);
  }

  @Override
  public Damage resistDamage(Damage dmg) {
    if (dmg.isFeatured(Damage.Feature.DEATH))
      dmg.value *= 0.8;
    return super.resistDamage(dmg);
  }

  @Override
  public HashSet<Class<?>> immunizedBuffs() {
    return IMMUNITIES;
  }
}
