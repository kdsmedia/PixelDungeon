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
package com.altomedia.pixeldungeon.ui;

import com.altomedia.pixeldungeon.scenes.PixelScene;

public class CheckBox extends RedButton {

  private boolean checked = false;

  public CheckBox(String label) {
    super(label);

    icon(Icons.get(Icons.UNCHECKED));
  }

  @Override
  protected void layout() {
    super.layout();

    // CheckBox left-aligns its label and puts the tick icon on the right,
    // so the label must also leave room for the icon (super.layout() only
    // accounts for the full button width). Re-fit the label to the space
    // between the left margin and the icon.
    text.scale.set(1f, 1f);
    float margin = (height - text.baseLine()) / 2f;
    float iconX = x + width - margin - icon.width;
    float maxTextWidth = Math.max(1f, iconX - (x + margin) - 2f);
    if (text.width() > maxTextWidth && text.width() > 0) {
      float s = Math.max(0.5f, maxTextWidth / text.width());
      text.scale.set(s, s);
      margin = (height - text.baseLine()) / 2f;
      iconX = x + width - margin - icon.width;
    }

    text.x = x + margin;
    text.y = y + margin;
    PixelScene.align(text);

    icon.x = iconX;
    icon.y = y + (height - icon.height) / 2f;
    PixelScene.align(icon);
  }

  public boolean checked() {
    return checked;
  }

  public void checked(boolean value) {
    if (checked != value) {
      checked = value;
      icon.copy(Icons.get(checked ? Icons.CHECKED : Icons.UNCHECKED));
    }
  }

  @Override
  protected void onClick() {
    super.onClick();
    checked(!checked);
  }
}
