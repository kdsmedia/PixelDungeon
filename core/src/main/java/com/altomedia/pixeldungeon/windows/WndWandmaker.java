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
package com.altomedia.pixeldungeon.windows;

import com.altomedia.pixeldungeon.items.quest.Embers;
import com.altomedia.pixeldungeon.ui.RenderedTextMultiline;
import com.altomedia.pixeldungeon.Dungeon;
import com.altomedia.pixeldungeon.actors.mobs.npcs.Wandmaker;
import com.altomedia.pixeldungeon.items.Item;
import com.altomedia.pixeldungeon.items.quest.CorpseDust;
import com.altomedia.pixeldungeon.items.wands.Wand;
import com.altomedia.pixeldungeon.messages.Messages;
import com.altomedia.pixeldungeon.plants.Rotberry;
import com.altomedia.pixeldungeon.scenes.PixelScene;
import com.altomedia.pixeldungeon.sprites.ItemSprite;
import com.altomedia.pixeldungeon.ui.RedButton;
import com.altomedia.pixeldungeon.ui.Window;
import com.altomedia.pixeldungeon.utils.GLog;

public class WndWandmaker extends Window {

  private static final int WIDTH = 120;
  private static final int BTN_HEIGHT = 20;
  private static final float GAP = 2;

  public WndWandmaker(final Wandmaker wandmaker, final Item item) {

    super();

    IconTitle titlebar = new IconTitle();
    titlebar.icon(new ItemSprite(item.image(), null));
    titlebar.label(Messages.titleCase(item.name()));
    titlebar.setRect(0, 0, WIDTH, 0);
    add(titlebar);

    String msg = "";
    if (item instanceof CorpseDust) {
      msg = Messages.get(this, "dust");
    } else if (item instanceof Embers) {
      msg = Messages.get(this, "ember");
    } else if (item instanceof Rotberry.Seed) {
      msg = Messages.get(this, "berry");
    }

    RenderedTextMultiline message = PixelScene.renderMultiline(msg, PixelScene.FONT_SIZE_BODY);
    message.maxWidth(WIDTH);
    message.setPos(0, titlebar.bottom() + GAP);
    add(message);

    RedButton btnWand1 = new RedButton(Wandmaker.Quest.wand1.name()) {
      @Override
      protected void onClick() {
        selectReward(wandmaker, item, Wandmaker.Quest.wand1);
      }
    };
    btnWand1.setRect(0, message.top() + message.height() + GAP, WIDTH, 
            BTN_HEIGHT);
    add(btnWand1);

    RedButton btnWand2 = new RedButton(Wandmaker.Quest.wand2.name()) {
      @Override
      protected void onClick() {
        selectReward(wandmaker, item, Wandmaker.Quest.wand2);
      }
    };
    btnWand2.setRect(0, btnWand1.bottom() + GAP, WIDTH, BTN_HEIGHT);
    add(btnWand2);

    resize(WIDTH, (int) btnWand2.bottom());
  }

  private void selectReward(Wandmaker wandmaker, Item item, Wand reward) {

    hide();

    item.detach(Dungeon.hero.getBelongings().backpack);

    reward.identify();
    if (reward.doPickUp(Dungeon.hero)) {
      GLog.i(Messages.get(Dungeon.hero, "you_now_have", reward.name()));
    } else {
      Dungeon.level.drop(reward, wandmaker.pos).getSprite().drop();
    }

    wandmaker.yell(Messages.get(this, "farewell", Dungeon.hero.givenName()));
    wandmaker.destroy();

    wandmaker.sprite.die();

    Wandmaker.Quest.complete();
  }
}
