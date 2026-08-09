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

import com.altomedia.pixeldungeon.effects.CellEmitter;
import com.altomedia.pixeldungeon.effects.Speck;
import com.altomedia.pixeldungeon.utils.GLog;
import com.altomedia.pixeldungeon.messages.Messages;
import com.altomedia.pixeldungeon.sprites.TrapSprite;

public class WornTrap extends Trap {

  {
    color = TrapSprite.BLACK;
    shape = TrapSprite.DOTS;
  }

  @Override
  public Trap hide() {
    //this one can't be hidden
    return reveal();
  }

  @Override
  public void activate() {
    CellEmitter.get(pos).burst(Speck.factory(Speck.STEAM), 6);
    GLog.i(Messages.get(this, "nothing"));
  }
}
