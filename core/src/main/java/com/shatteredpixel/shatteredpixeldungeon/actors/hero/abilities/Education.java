package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;

public class Education extends ArmorAbility {

	{
		baseChargeUse = 0f;
	}

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {}

	@Override
	public int icon() {
		return HeroIcon.BERSERKER;
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.HIGH_EDUCATION};
	}

}
