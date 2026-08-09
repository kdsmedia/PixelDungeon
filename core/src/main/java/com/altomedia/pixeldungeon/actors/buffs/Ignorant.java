package com.altomedia.pixeldungeon.actors.buffs;

import com.altomedia.pixeldungeon.messages.Messages;
import com.altomedia.pixeldungeon.ui.BuffIndicator;

/**
 * Created by 93942 on 9/25/2018.
 */

// check in Char::takeDamage
public class Ignorant extends Buff{
  {
    type  = buffType.NEGATIVE;
  }
  
  @Override
  public int icon(){
    return BuffIndicator.IGNORANT;
  }
  
  @Override
  public String toString(){ return Messages.get(this, "name"); }
  
  @Override
  public String desc(){ return Messages.get(this, "desc"); }
}
