package com.altomedia.pixeldungeon.actors.blobs;

import com.altomedia.pixeldungeon.actors.Actor;
import com.altomedia.pixeldungeon.actors.Char;
import com.altomedia.pixeldungeon.actors.buffs.Buff;
import com.altomedia.pixeldungeon.actors.buffs.Cripple;
import com.altomedia.pixeldungeon.effects.BlobEmitter;
import com.altomedia.pixeldungeon.effects.particles.FlameParticle;

/**
 * Created by 93942 on 7/29/2018.
 */

public class RoaringFire extends Fire {

  @Override
  protected void burn(int pos) {
    Char ch = Actor.findChar(pos);
    if (ch != null)
      Buff.prolong(ch, Cripple.class, Cripple.DURATION / 2);

    super.burn(pos);
  }

  @Override
  public void use(BlobEmitter emitter) {
    super.use(emitter);
    emitter.start(RoaringFlameParticle.FACTORY, 0.03f, 0);
  }

  // use different color, more 'red'
  public static class RoaringFlameParticle extends FlameParticle {

    public RoaringFlameParticle() {
      super();

      color(0xEE3322);
    }

  }
}
