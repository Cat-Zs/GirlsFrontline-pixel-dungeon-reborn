package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.DMR;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import com.shatteredpixel.shatteredpixeldungeon.Assets;

//这里说明AN94这把武器属于DMR这一武器大类
public class AN94 extends DesignatedMarksmanRifle {

    //private static final String AC_SWITCH_MODE = "SWITCH_MODE"; //切换模式的按钮
    //private boolean onceShootMode = false; //普通射击模式

	{
		image = ItemSpriteSheet.AN94; //该武器使用AN94的图像
		hitSound = Assets.Sounds.HIT_CRUSH; //该武器使用这个攻击音效
		hitSoundPitch = 0.5f;  //音调降低半度

		tier = 4; // 阶数为4
		RCH = 4; // 射程为4格
		
	}

    //这里决定了AN94的基础伤害数值和升级成长

	@Override
	public int min(int lvl) {
		return 4 + // 基础伤害为4
				lvl;   // 每级成长1
	}

	@Override
	public int max(int lvl) {
		return 25 +  // 基础最大值25
				lvl * 7;   // 每级成长7
	}
}