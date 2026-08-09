package com.altomedia.pixeldungeon.actors.blobs;

import com.altomedia.pixeldungeon.Badges;
import com.altomedia.pixeldungeon.Dungeon;
import com.altomedia.pixeldungeon.actors.Actor;
import com.altomedia.pixeldungeon.actors.Char;
import com.altomedia.pixeldungeon.actors.Damage;
import com.altomedia.pixeldungeon.actors.hero.Hero;
import com.altomedia.pixeldungeon.effects.BlobEmitter;
import com.altomedia.pixeldungeon.effects.Speck;
import com.altomedia.pixeldungeon.messages.Messages;
import com.altomedia.pixeldungeon.utils.GLog;
import com.watabou.utils.Random;

/**
 * Created by 93942 on 4/25/2018.
 */

public class HighlyToxicGas extends Blob implements Hero.Doom {

  @Override
  protected void evolve() {
    super.evolve();

    // %40 sharper
    int levelDamage = (5 + Dungeon.depth * 5) * 7 / 5;

    Char ch;
    int cell;

    for (int i = area.left; i < area.right; i++) {
      for (int j = area.top; j < area.bottom; j++) {
        cell = i + j * Dungeon.level.width();
        if (cur[cell] > 0 && (ch = Actor.findChar(cell)) != null) {
          int damage = (ch.HT + levelDamage) / 40;
          if (Random.Int(40) < (ch.HT + levelDamage) % 40) {
            damage++;
          }

          ch.takeDamage(new Damage(damage, this, ch).addElement(Damage
                  .Element.POISON));
          // ch.damage( damage, this );
        }
      }
    }
  }

  @Override
  public void use(BlobEmitter emitter) {
    super.use(emitter);

    emitter.pour(Speck.factory(Speck.DPD_HIGHLY_TOXIC), 0.4f);
  }

  @Override
  public String tileDesc() {
    return Messages.get(this, "desc");
  }

  @Override
  public void onDeath() {
    Badges.validateDeathFromGas();

    Dungeon.fail(getClass());
    GLog.n(Messages.get(this, "ondeath"));
  }
}
