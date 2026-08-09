package com.altomedia.pixeldungeon.actors.buffs;

import com.altomedia.pixeldungeon.Dungeon;
import com.altomedia.pixeldungeon.actors.Char;
import com.altomedia.pixeldungeon.actors.Damage;
import com.altomedia.pixeldungeon.actors.hero.Hero;
import com.altomedia.pixeldungeon.items.rings.RingOfElements;
import com.altomedia.pixeldungeon.messages.Messages;
import com.altomedia.pixeldungeon.sprites.CharSprite;
import com.altomedia.pixeldungeon.ui.BuffIndicator;
import com.altomedia.pixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by 93942 on 5/5/2018.
 */

public class SoulBurning extends Buff implements Hero.Doom {
  {
    type = buffType.NEGATIVE;
  }

  private static final float DURATION = 8f;
  private float left_;

  private static final String LEFT = "left";

  @Override
  public void storeInBundle(Bundle bundle) {
    super.storeInBundle(bundle);
    bundle.put(LEFT, left_);
  }

  @Override
  public void restoreFromBundle(Bundle bundle) {
    super.restoreFromBundle(bundle);
    left_ = bundle.getFloat(LEFT);
  }

  @Override
  public boolean act() {
    if (target.isAlive()) {
      int maxDmg = Dungeon.depth;
      int dmgHP = Random.Int(2, maxDmg);

      if (target instanceof Hero) {
        //todo: affect hero
      } else {
        target.takeDamage(new Damage(dmgHP, this, target).type(Damage.Type.MAGICAL).addElement(Damage.Element.SHADOW));
      }
    } else {
      detach();
    }

    spend(TICK);
    left_ -= TICK;
    if (left_ <= 0)
      detach();

    return true;
  }

  public void reignite(Char ch) {
    left_ = duration(ch);
  }

  @Override
  public int icon() {
    return BuffIndicator.SOUL_FIRE;
  }

  @Override
  public void fx(boolean on) {
    if (on) target.sprite.add(CharSprite.State.SOUL_BURNING);
    else target.sprite.remove(CharSprite.State.SOUL_BURNING);
  }

  @Override
  public String heroMessage() {
    return Messages.get(this, "heromsg");
  }

  @Override
  public String toString() {
    return Messages.get(this, "name");
  }

  public static float duration(Char ch) {
    RingOfElements.Resistance r = ch.buff(RingOfElements.Resistance.class);
    return r != null ? r.durationFactor() * DURATION : DURATION;
  }

  @Override
  public String desc() {
    return Messages.get(this, "desc", dispTurns(left_));
  }

  @Override
  public void onDeath() {
    //todo: add badges
    Dungeon.fail(getClass());
    GLog.n(Messages.get(this, "ondeath"));
  }
}
