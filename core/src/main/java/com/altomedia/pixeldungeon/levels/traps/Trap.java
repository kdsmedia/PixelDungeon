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
import com.altomedia.pixeldungeon.actors.hero.Hero;
import com.altomedia.pixeldungeon.messages.Messages;
import com.altomedia.pixeldungeon.sprites.TrapSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public abstract class Trap implements Bundlable {

  public String name = Messages.get(this, "name");

  public int color;
  public int shape;

  public int pos;

  public TrapSprite sprite;
  public boolean visible;
  public boolean active = true;

  public Trap set(int pos) {
    this.pos = pos;
    return this;
  }

  public Trap reveal() {
    visible = true;
    if (sprite != null && sprite.visible == false) {
      sprite.visible = true;
      sprite.alpha(0);
      sprite.parent.add(new AlphaTweener(sprite, 1, 0.6f));
    }
    return this;
  }

  public Trap hide() {
    visible = false;
    if (sprite != null)
      sprite.visible = false;
    return this;
  }

  public void trigger() {
    if (active) {
      if (Dungeon.visible[pos]) {
        Sample.INSTANCE.play(Assets.SND_TRAP);
      }

      // when trigger a hidden trap, up pressure 
      if (!visible) {
        Char ch = Actor.findChar(pos);
        if (ch instanceof Hero && ch.isAlive()) {
          ch.takeDamage(new Damage(Random.NormalIntRange(1, 9), this, ch).type(Damage.Type.MENTAL));
        }
      }
      
      disarm();
      reveal();
      activate();
    }
  }

  public abstract void activate();

  protected void disarm() {
    Dungeon.level.disarmTrap(pos);
    active = false;
    if (sprite != null) {
      sprite.reset(this);
    }
  }

  private static final String POS = "pos";
  private static final String VISIBLE = "visible";
  private static final String ACTIVE = "active";

  @Override
  public void restoreFromBundle(Bundle bundle) {
    pos = bundle.getInt(POS);
    visible = bundle.getBoolean(VISIBLE);
    if (bundle.contains(ACTIVE)) {
      active = bundle.getBoolean(ACTIVE);
    }
  }

  @Override
  public void storeInBundle(Bundle bundle) {
    bundle.put(POS, pos);
    bundle.put(VISIBLE, visible);
    bundle.put(ACTIVE, active);
  }

  public String desc() {
    return Messages.get(this, "desc");
  }
}
