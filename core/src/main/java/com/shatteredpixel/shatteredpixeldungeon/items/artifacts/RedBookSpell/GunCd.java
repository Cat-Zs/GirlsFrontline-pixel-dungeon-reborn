package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBookSpell;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.ShootGun;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public class GunCd extends ItemSpell{

    public static final GunCd INSTANCE = new GunCd();

    public int icon() {
        return 41;
    }

    {
        chargeUse=1;
        timeUse=0;
    }

    @Override
    protected boolean usableOnItem( Item item ){
        return item instanceof ShootGun&& ((ShootGun) item).cooldownLeft>0;
    }

    @Override
    protected void onItemAct( Item item ){
        ((ShootGun) item).cooldownLeft=Math.max(0, ((ShootGun) item).cooldownLeft-10);
    }

}
