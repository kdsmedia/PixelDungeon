package com.altomedia.pixeldungeon.items.scrolls;

import com.altomedia.pixeldungeon.Assets;
import com.altomedia.pixeldungeon.effects.CellEmitter;
import com.altomedia.pixeldungeon.effects.particles.ShadowParticle;
import com.altomedia.pixeldungeon.items.Item;
import com.altomedia.pixeldungeon.messages.Messages;
import com.altomedia.pixeldungeon.utils.GLog;
import com.altomedia.pixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;

import java.security.cert.TrustAnchor;

/**
 * Created by 93942 on 10/15/2018.
 */

public class ScrollOfCurse extends InventoryScroll{
  {
    initials = 13;
    mode = WndBag.Mode.EQUIPMENT;
    
    bones = true;
  }

  @Override
  protected void onItemSelected(Item item) {
    CellEmitter.get(curUser.pos).burst(ShadowParticle.CURSE, 5);
    Sample.INSTANCE.play(Assets.SND_CURSED);

    item.cursed = true;
    item.cursedKnown = true;
    updateQuickslot();
    
    GLog.w(Messages.get(this, "cursed", item.name()));
  }
}
