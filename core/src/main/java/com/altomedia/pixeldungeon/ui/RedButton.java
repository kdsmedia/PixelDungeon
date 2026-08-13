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

import com.altomedia.pixeldungeon.Assets;
import com.altomedia.pixeldungeon.Chrome;
import com.altomedia.pixeldungeon.scenes.PixelScene;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;

public class RedButton extends Button {

  protected NinePatch bg;
  protected RenderedText text;
  protected Image icon;

  public RedButton(String label) {
    this(label, PixelScene.FONT_SIZE_BUTTON);
  }

  public RedButton(String label, int size) {
    super();

    text = PixelScene.renderText(size);
    text.text(label);
    add(text);
  }

  @Override
  protected void createChildren() {
    super.createChildren();

    bg = Chrome.get(Chrome.Type.BUTTON);
    add(bg);
  }

  @Override
  protected void layout() {

    super.layout();

    bg.x = x;
    bg.y = y;
    bg.size(width, height);

    // Fit label inside the button: shrink font scale so the text never
    // overflows the available width and overlaps siblings/buttons. Floor at
    // 0.45 so long Indonesian labels (e.g. "Kelompok") stay readable yet fit
    // narrow buttons.
    text.scale.set(1f, 1f);
    float iconSpace = (icon != null) ? icon.width() + 4f : 0f;
    float maxTextWidth = Math.max(1f, width - 4f - iconSpace);
    if (text.width() > maxTextWidth && text.width() > 0) {
      float s = Math.max(0.45f, maxTextWidth / text.width());
      text.scale.set(s, s);
    }

    text.x = x + (width - text.width()) / 2f;
    text.y = y + (height - text.baseLine()) / 2f;
    PixelScene.align(text);

    if (icon != null) {
      icon.x = text.x - icon.width() - 2;
      icon.y = y + (height - icon.height()) / 2;
      PixelScene.align(icon);
    }
  }

  @Override
  protected void onTouchDown() {
    bg.brightness(1.2f);
    Sample.INSTANCE.play(Assets.SND_CLICK);
  }

  @Override
  protected void onTouchUp() {
    bg.resetColor();
  }

  public void enable(boolean value) {
    active = value;
    text.alpha(value ? 1.0f : 0.3f);
  }

  public void text(String value) {
    text.text(value);
    layout();
  }

  public void textColor(int value) {
    text.hardlight(value);
  }

  public void icon(Image icon) {
    if (this.icon != null) {
      remove(this.icon);
    }
    this.icon = icon;
    if (this.icon != null) {
      add(this.icon);
      layout();
    }
  }

  public float reqWidth() {
    return text.width() + 2f;
  }

  public float reqHeight() {
    return text.baseLine() + 4;
  }
}
