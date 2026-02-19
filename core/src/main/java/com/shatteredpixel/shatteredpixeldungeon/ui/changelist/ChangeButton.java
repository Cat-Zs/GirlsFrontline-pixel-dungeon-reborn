/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.ui.changelist;

import com.shatteredpixel.shatteredpixeldungeon.GirlsFrontlinePixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlacksmithSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.ui.Component;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

//not actually a button, but functions as one.
public class ChangeButton extends Component {

	protected Image icon;
    protected float size = -1;
    protected int   color= -1;
	protected String title;
	protected String message;
    protected MovieClip.Animation action;
    protected Boolean cycle;
    protected ArrayList<Image> pageImage;
    protected ArrayList<Float> pageSize;
    protected ArrayList<Integer> pageColor;
    protected ArrayList<MovieClip.Animation> pageAction;
    protected ArrayList<Boolean> pageCycle;
    protected ArrayList<String> pageTitle;
	protected ArrayList<String> pageMessages;
    private void resetAll(){
        this.title          = null;
        this.message        = null;
        this.action         = null;
        this.cycle          = null;
        this.pageImage      = null;
        this.pageSize       = null;
        this.pageAction     = null;
        this.pageCycle      = null;
        this.pageColor      = null;
        this.pageTitle      = null;
        this.pageMessages   = null;
    }
    public ChangeButton( Image icon, float size,
                         MovieClip.Animation action, Boolean cycle,
                         int color, String title,
                         String message, int ignore) {
        super();

        resetAll();
        if (icon==null)
            icon = new BlacksmithSprite();
        if (title==null)
            title = "";
        if (message==null)
            message = "";
        this.icon = icon;
        if (size > 0F) {
            this.icon.scale.set(size);
            this.size = size;
        }
        if (this.icon instanceof CharSprite) {
            if (action != null)
                ((CharSprite) this.icon).playCycle(action, cycle);
            if (color > 0)
                ((CharSprite) this.icon).aura(color);
        }
        else
            if (color > 0)
                this.icon.glow = new Image.Glow(color);
        this.color = color;
        add(this.icon);
        if (cycle!=null){
            this.action = action;
            this.cycle  = cycle;
        }
        this.title = Messages.titleCase(title);
        this.message = message;

        layout();
    }
    //标准单窗口
    public ChangeButton( ArrayList<Image> icon, ArrayList<Float> size,
                         ArrayList<MovieClip.Animation> action, ArrayList<Boolean> cycle,
                         ArrayList<Integer> color, ArrayList<String> title,
                         ArrayList<String> pageMessages, int ignore) {
        super();

        resetAll();
        if (icon == null)
            icon = new ArrayList<>();
        if (icon.isEmpty())
            icon.add(new BlacksmithSprite());
        if (icon.get(0)==null)
            for (Image i: icon)
                if (i!=null){
                    icon.set(0, i);
                    break;
                }
        if (icon.get(0)==null)
            icon.set(0, new BlacksmithSprite());
        if (size == null)
            size = new ArrayList<>();
        if (size.isEmpty())
            size.add(-1F);
        if (size.get(0)==null)
            for (Float i: size)
                if (i!=null){
                    size.set(0, i);
                    break;
                }
        if (size.get(0)==null)
            size.set(0, -1F);
        if (action == null)
            action = new ArrayList<>();
        if (action.isEmpty())
            action.add(null);
        if (cycle == null)
            cycle = new ArrayList<>();
        if (cycle.isEmpty())
            cycle.add(null);
        if (color == null)
            color = new ArrayList<>();
        if (color.isEmpty())
            color.add(-1);
        if (color.get(0)==null)
            for (Integer i: color)
                if (i!=null){
                    color.set(0, i);
                    break;
                }
        if (color.get(0)==null)
            color.set(0, -1);
        if (title == null)
            title = new ArrayList<>();
        if (title.isEmpty())
            title.add("");
        if (pageMessages == null)
            pageMessages = new ArrayList<>();
        if (pageMessages.isEmpty())
            pageMessages.add("");
        if (title.get(0)==null)
            for (String i : title)
                if (i!=null){
                    title.set(0, i);
                    break;
                }
        try {
            if (icon.get(0) instanceof HeroSprite){
                Constructor<?> cons = icon.get(0).getClass().getDeclaredConstructor(HeroClass.class, int.class);
                this.icon = (HeroSprite) cons.newInstance(((HeroSprite) icon.get(0)).heroClass, ((HeroSprite) icon.get(0)).armorTier);
            }else if (icon.get(0) instanceof CharSprite){
                Constructor<?> cons = icon.get(0).getClass().getDeclaredConstructor();
                this.icon = (CharSprite) cons.newInstance();
            }else
                this.icon = new Image(icon.get(0));
        } catch (Exception ignored) {}
        if (size.get(0) > 0F)
            this.icon.scale.set(size.get(0));
        if (this.icon instanceof CharSprite){
            if (action.get(0) != null)
                ((CharSprite) this.icon).playCycle(action.get(0), pageCycle.get(0));
            if (color.get(0) > 0)
                ((CharSprite) this.icon).aura(color.get(0));
        }
        else
            if (color.get(0) > 0)
                this.icon.glow = new Image.Glow(color.get(0));

        add(this.icon);

        this.pageImage = new ArrayList<>();
        for (int i =0; i<pageMessages.size(); i++){

            //令前六个ArrayList中长度未对齐的填充到与信息表对齐
            if (i>=icon.size())
                icon.add(icon.get(0));
            if (i>=size.size())
                if (icon.get(i)==icon.get(0))
                    size.add(size.get(0));
                else
                    size.add(-1F);
            if (i>=action.size())
                if (icon.get(i)==icon.get(0))
                    action.add(action.get(0));
                else
                    action.add(null);
            if (i>=cycle.size())
                if (icon.get(i)==icon.get(0))
                    cycle.add(cycle.get(0));
                else
                    cycle.add(null);
            if (i>=color.size())
                if (icon.get(i)==icon.get(0))
                    color.add(color.get(0));
                else
                    color.add(-1);
            if (i>=title.size())
                title.add(title.get(0));

            //令前六个ArrayList中除了action和cycle以外的ArrayList出现null的位置填充东西以免读取到null并用null赋值
            if (icon.get(i)==null)
                icon.set(i, icon.get(0));
            if (size.get(i)==null)
                if (icon.get(i)==icon.get(0))
                    size.set(i, size.get(0));
                else
                    size.set(i, -1F);
            if (color.get(i)==null)
                if (icon.get(i)==icon.get(0))
                    color.set(i, color.get(0));
                else
                    color.set(i, -1);
            if (title.get(i)==null)
                title.set(i, title.get(0));

            //如混有怪物贴图，直接添加而非复制图片
            if (icon.get(i) instanceof CharSprite)
                this.pageImage.add(icon.get(i));
            //将大小、发光塞到图片里边
            else {
                Image image = new Image(icon.get(i));
                if (size.get(i)  > 0F)
                    image.scale.set(size.get(i));
                if (color.get(i)  > 0)
                    image.glow = new Image.Glow(color.get(i));
                this.pageImage.add(image);
            }
        }
        this.pageSize       = size;
        this.pageAction     = action;
        this.pageCycle      = cycle;
        this.pageColor      = color;
        this.pageTitle      = title;
        this.pageMessages = pageMessages;
        layout();
    }
    //标准列表
    protected void onClick() {
        if (pageMessages != null)
            //已添加防蠢机制，纯空数组也会塞个东西进去，所以必定不会出现空数组
            GirlsFrontlinePixelDungeon.scene().add(new ChangesWindowWithPages(pageImage, pageSize, pageAction, pageCycle, pageColor, pageTitle, pageMessages, 0));
        else
            GirlsFrontlinePixelDungeon.scene().add(new ChangesWindow(icon, size, action, cycle, color, title, message));
    }

    @Override
    protected void layout() {
        super.layout();

        icon.x = x + (width - icon.width()) / 2f;
        icon.y = y + (height - icon.height()) / 2f;
        PixelScene.align(icon);
    }
    public static class MobTitle extends Component {

        private static final int GAP	= 2;

        private CharSprite image;
        private RenderedTextBlock title;

        public MobTitle( CharSprite mob, String title, float size, MovieClip.Animation action, int color ,Boolean cycle) {

            this.title = PixelScene.renderTextBlock( title, 9 );
            this.title.hardlight( 0xFFFF44 );
            add( this.title );

            try {
                if (mob instanceof HeroSprite){
                    Constructor<?> cons = mob.getClass().getDeclaredConstructor(HeroClass.class, int.class);
                    this.image = (HeroSprite) cons.newInstance(((HeroSprite) mob).heroClass, ((HeroSprite) mob).armorTier);
                }else {
                    Constructor<?> cons = mob.getClass().getDeclaredConstructor();
                    this.image = (CharSprite) cons.newInstance();
                }
            } catch (Exception ignored) {}
            if (size>0)
                this.image.scale.set(size);
            if (color>0)
                this.image.aura(color);
            if (action!=null)
                this.image.playCycle(action, cycle);
            add( this.image );
        }

        @Override
        protected void layout() {

            image.x = 0;
            image.y = Math.max( 0, (title.height() - image.height())/2 );

            float w = width - image.width() - GAP;

            title.maxWidth((int)w);
            title.setPos(x + image.width()+GAP,
                    image.height() > title.height() ? y +(image.height()-title.height()) / 2 : y);
            height = Math.max( image.height() , title.bottom() );
        }
    }

    public ChangeButton(Object A){
        this(A, null, null, null, null, null, (String) null);
    }
    //预留这个的意义，我觉得只有展示物品或者生物的面板的作用，输入其他的东西也会自动分配，但毫无意义
    public ChangeButton(String message){
        this(null, null, null, null, null, null, message);
    }
    //缺六项
    public ChangeButton( Object A, String message){
        this(A, null, null, null, null, null, message);
    }
    //缺五项
    public ChangeButton( Object A, Object B,
                         String message){
        this(A, B, null, null, null, null, message);
    }
    //缺四项
    public ChangeButton( Object A, Object B,
                         Object C, String message){
        this(A, B, C, null, null, null, message);
    }
    //缺三项
    public ChangeButton( Object A, Object B, Object C, Object D, String message){
        this(A, B, C, D, null, null, message);
    }
    //缺两项
    public ChangeButton( Object A, Object B, Object C, Object D, Object E, String message){
        this(A, B, C, D, E, null, message);
    }
    //缺一项
    public ChangeButton( Object A, Object B, Object C, Object D, Object E, Object F, String message){
        this(getFirst(A), getFirst(B), getFirst(C), getFirst(D), getFirst(E), getFirst(F), message, true);
    }
    //不缺项（拆包）（很难以想象有人会往单窗口塞入数组）
    private static Object getFirst(Object A){
        if (A instanceof ArrayList<?>){
            return getFirst((ArrayList<?>) A);
        }
        else if (A instanceof Object[]){
            return getFirst((Object[]) A);
        }
        else if (A instanceof float[]){
            return getFirst((float[]) A);
        }
        else if (A instanceof boolean[]){
            return getFirst((boolean[]) A);
        }
        else if (A instanceof int[]){
            return getFirst((int[]) A);
        }
        else {
            return A;
        }
    }
    private static Object getFirst(ArrayList<?> A){
        if (A == null)
            return null;
        if (A.isEmpty())
            return null;
        for (Object i :A)
            if (i!=null)
                return i;
        return null;
    }
    private static Object getFirst(Object[] A){
        if (A == null)
            return null;
        for (Object i :A)
            if (i!=null)
                return i;
        return null;
    }
    private static Object getFirst(float[] A){
        if (A == null)
            return null;
        if (A.length == 0)
            return null;
        return A[0];
    }
    private static Object getFirst(boolean[] A){
        if (A == null)
            return null;
        if (A.length == 0)
            return null;
        return A[0];
    }
    private static Object getFirst(int[] A){
        if (A == null)
            return null;
        if (A.length == 0)
            return null;
        return A[0];
    }
    public ChangeButton( Object A, Object B,
                         Object C, Object D,
                         Object E, Object F,
                         String message, boolean ignore){
        this(getImage(A, B ,C, D, E, F),
                getFloat(B, C, D, E, F, A),
                getAction(C, D, E, F, A, B),
                getBoolean(D, E, F, A, B, C),
                getInt(E, F, A, B, C, D),
                getTitle(F, A, B, C, D, E, message),
                getInfo(message, F, E, D, C, B, A),
                0
                );
    }
    //不缺项（分拣）
    private static Image getImage(Object A, Object B,
                                  Object C, Object D,
                                  Object E, Object F){
        Image image;
        if ((image = getSignalImage(A)) == null
                && (image = getSignalImage(B)) == null
                && (image = getSignalImage(C)) == null
                && (image = getSignalImage(D)) == null
                && (image = getSignalImage(E)) == null)
            image = getSignalImage(F);
        return image;
    }
    private static Image getSignalImage(Object A){
        if (A instanceof Image)
            return (Image) A;
        else if(A instanceof Mob)
            return ((Mob) A).sprite();
        else if(A instanceof Hero)
            return new HeroSprite(((Hero) A).heroClass, 1);
        else if(A instanceof Item)
            return new ItemSprite((Item) A);
        else
            return null;
    }
    private static float getFloat(Object A, Object B,
                                  Object C, Object D,
                                  Object E, Object F){
        float size;
        if ((size = getSignalFloat(A)) == -2F
                && (size = getSignalFloat(B)) == -2F
                && (size = getSignalFloat(C)) == -2F
                && (size = getSignalFloat(D)) == -2F
                && (size = getSignalFloat(E)) == -2F) {
            size = getSignalFloat(F);
        }
        return size;
    }
    private static float getSignalFloat(Object A){
        if (A instanceof Float)
            return (float) A;
        else
            return -2F;
    }
    private static MovieClip.Animation getAction(Object A, Object B,
                                                 Object C, Object D,
                                                 Object E, Object F){
        MovieClip.Animation action;
        if ((action = getSignalAction(A)) == null
                &&(action = getSignalAction(B)) == null
                &&(action = getSignalAction(C)) == null
                &&(action = getSignalAction(D)) == null
                &&(action = getSignalAction(E)) == null)
            action = getSignalAction(F);
        return action;
    }
    private static MovieClip.Animation getSignalAction(Object A){
        if (A instanceof MovieClip.Animation)
            return (MovieClip.Animation) A;
        else
            return null;
    }
    private static Boolean getBoolean(Object A, Object B,
                                  Object C, Object D,
                                  Object E, Object F){
        Boolean cycle;
        if ((cycle = getSignalBoolean(A))==null
                &&(cycle = getSignalBoolean(B))==null
                &&(cycle = getSignalBoolean(C))==null
                &&(cycle = getSignalBoolean(D))==null
                &&(cycle = getSignalBoolean(E))==null)
            cycle = getSignalBoolean(F);
        return cycle;
    }
    private static Boolean getSignalBoolean(Object A){
        if (A instanceof Boolean)
            return (Boolean) A;
        else
            return null;
    }
    private static int getInt(Object A, Object B,
                                  Object C, Object D,
                                  Object E, Object F){
        int color;
        if ((color = getSignalInt(A)) == -2
                && (color = getSignalInt(B)) == -2
                && (color = getSignalInt(C)) == -2
                && (color = getSignalInt(D)) == -2
                && (color = getSignalInt(E)) == -2) {
            color = getSignalInt(F);
        }
        return color;
    }
    private static int getSignalInt(Object A){
        if (A instanceof Integer)
            return (int) A;
        else
            return -2;
    }
    private static String getTitle(Object A, Object B,
                                   Object C, Object D,
                                   Object E, Object F,
                                   String message){
        String title;
        String info = getInfo(message, A, F, E, D, C, B);
        if ((title = passBody(A, info)) == null
                && (title = passBody(B, info)) == null
                && (title = passBody(C, info)) == null
                && (title = passBody(D, info)) == null
                && (title = passBody(E, info)) == null)
            title = passBody(F, info);
        return title;
    }
    private static String passBody(Object A, String body){
        if (A == body)
            return null;
        else
            return getSignalTitle(A);
    }
    private static String getSignalTitle(Object A){
        if (A instanceof String)
            return (String) A;
        else
            return getSignalName(A);
    }
    private static String getSignalName(Object A){
        if (A instanceof Char)
            return ((Char) A).name();
        else if (A instanceof Item)
            return A.toString();
        else
            return null;
    }
    private static String getInfo(Object A, Object B,
                                  Object C, Object D,
                                  Object E, Object F,
                                  Object G){
        String info;
        if ((info = getSignalMessage(A)) == null
                && (info = getSignalMessage(B)) == null
                && (info = getSignalMessage(C)) == null
                && (info = getSignalMessage(D)) == null
                && (info = getSignalMessage(E)) == null
                && (info = getSignalMessage(F)) == null)
            info = getSignalMessage(G);
        return info;
    }
    private static String getSignalMessage(Object A){
        if (A instanceof String)
            return (String) A;
        else
            return getSignalInfo(A);
    }
    private static String getSignalInfo(Object A){
        if (A instanceof Mob)
            return ((Mob) A).info();
        else if (A instanceof Hero)
            return "有憨憨！不仅没写主体内容，还塞了个没有描述的单位（玩家）进来";
        else if (A instanceof Item)
            return ((Item) A).info();
        else
            return null;
    }
    public ChangeButton(Object[] A){
        this(getArray(A));
    }
    //缺六项（末项数组）
    private static ArrayList<?> checkString(ArrayList<?> A){
        if (A!=null&&!A.isEmpty())
            for (Object i : A)
                if (i instanceof String)
                    return A;
        return null;
    }
    private static ArrayList<?> notString(ArrayList<?> A){
        if (A!=null&&!A.isEmpty())
            for (Object i : A)
                if (i instanceof String)
                    return null;
        return A;
    }
    private static ArrayList<?> getArray(Object[] A){
        return A!=null?new ArrayList<>(Arrays.asList(A)):null;
    }
    public ChangeButton(ArrayList<?> A){
        this(notString(A),
                null, null, null, null, null,
                checkString(A));
    }
    //缺六项（预留这个的意义，我想就只有展示物品或者生物的面板了）
    public ChangeButton(Object A, Object[] B){
        this(A, getArray(B));
    }
    //缺五项（末项数组）
    public ChangeButton(Object A, ArrayList<?> B){
        this(A, notString(B),
                null, null, null, null,
                checkString(B));
    }
    //缺五项（缺贴图倒是能理解，毕竟默认贴图是波波沙）
    public ChangeButton(Object A, Object B,
                        Object[] C){
        this(A, B, getArray(C));
    }
    //缺四项（末项数组）
    public ChangeButton(Object A, Object B,
                        ArrayList<?> C){
        this(A, B, notString(C),
                null, null, null,
                checkString(C));
    }
    //缺四项
    public ChangeButton(Object A, Object B,
                        Object C, Object[] D){
        this(A, B, C, getArray(D));
    }
    //缺三项（末项数组）
    public ChangeButton(Object A,Object B,
                        Object C,ArrayList<?> D){
        this(A, B, C, notString(D),
                null, null,
                checkString(D));
    }
    //缺三项
    public ChangeButton(Object A, Object B,
                        Object C, Object D,
                        Object[] E){
        this(A, B, C, D, getArray(E));
    }
    //缺二项（末项数组）
    public ChangeButton(Object A,Object B,
                        Object C,Object D,
                        ArrayList<?> E){
        this(A, B, C, D,
                notString(E),
                null,
                checkString(E));
    }
    //缺二项
    public ChangeButton(Object A, Object B,
                        Object C, Object D,
                        Object E, Object[] F){
        this(A, B, C, D, E, getArray(F));
    }
    //缺一项（末项数组）
    public ChangeButton(Object A, Object B,
                        Object C, Object D,
                        Object E, ArrayList<?> F){
        this(A, B, C, D, E,
                notString(F),
                checkString(F));
    }
    //缺一项
    public ChangeButton(Object A, Object B,
                        Object C, Object D,
                        Object E, Object F,
                        Object[] G){
        this(A, B, C, D, E, F, getArray(G));
    }
    //不缺项（末项数组）
    public ChangeButton(Object A, Object B,
                        Object C, Object D,
                        Object E, Object F,
                        ArrayList<?> G){
        this(ObjectToArray(A),
                ObjectToArray(B),
                    ObjectToArray(C),
                        ObjectToArray(D),
                            ObjectToArray(E),
                                ObjectToArray(F),
                G,
                true);
    }
    //不缺项（打包）
    private static ArrayList<Object> mixList(Object[] A){
        ArrayList<Object> Array = new ArrayList<>();
        for (Object i: A)
                if (i instanceof Image||i instanceof Item||i instanceof Char)
                    Array.add(i);
        return Array;
        //贴图、物品、生物混合的数组
    }
    private static ArrayList<?> ListToArray(Object[] A){
        if (A.length>0)
            if (A.getClass().getComponentType() == Object.class)
                return mixList(A);
            else if (A instanceof Image[])
                return new ArrayList<>(Arrays.asList((Image[]) A));
            else if (A instanceof Float[])
                return new ArrayList<>(Arrays.asList((Float[]) A));
            else if (A instanceof MovieClip.Animation[])
                return new ArrayList<>(Arrays.asList((MovieClip.Animation[]) A));
            else if (A instanceof Boolean[])
                return new ArrayList<>(Arrays.asList((Boolean[]) A));
            else if (A instanceof Integer[])
                return new ArrayList<>(Arrays.asList((Integer[]) A));
            else if (A instanceof String[])
                return new ArrayList<>(Arrays.asList((String[]) A));
            else if (A instanceof Item[])
                return new ArrayList<>(Arrays.asList((Item[]) A));
                //物品特供
            else if (A instanceof Char[])
                return new ArrayList<>(Arrays.asList((Char[]) A));
                //按理说我要求放进来的该是贴图，但你要是放进来Char单位，也行吧
            else
                return null;
                //你丢了什么鬼数组进来啊
        else
            return null;
            //空数组会填充波波沙
    }
    private static ArrayList<?> baseObjectToArray(Object A){
        if (A instanceof float[]&&((float[]) A).length>0){
            ArrayList<Float> Array = new ArrayList<>();
            for (float i: ((float[]) A))
                Array.add(i);
            return Array;
        }
        else if (A instanceof boolean[]&&((boolean[]) A).length>0){
            ArrayList<Boolean> Array = new ArrayList<>();
            for(boolean i:(boolean[]) A)
                Array.add(i);
            return Array;
        }
        else if (A instanceof int[]&&((int[]) A).length>0){
            ArrayList<Integer> Array = new ArrayList<>();
            for(int i: (int[]) A)
                Array.add(i);
            return Array;
        }
        else {
            return new ArrayList<>(Collections.nCopies(1, A));
        }
    }
    private static ArrayList<?> ObjectToArray(Object A){
        if (A instanceof ArrayList<?>)
            return (ArrayList<?>) A;
        else if (A instanceof Object[])
            return ListToArray((Object[]) A);
        else
            return baseObjectToArray(A);
    }
    public ChangeButton(ArrayList<?> A,ArrayList<?> B,
                        ArrayList<?> C,ArrayList<?> D,
                        ArrayList<?> E,ArrayList<?> F,
                        ArrayList<?> G, boolean ignore){
        this(getImageList(A, B, C, D, E, F, G),
                getFloatList(B, C, D, E, F, G, A),
                getActionList(C, D, E, F, G, A, B),
                getBooleanList(D, E, F, G, A, B, C),
                getIntegerList(E, F, G, A, B, C, D),
                getTitleList(F, G, A, B, C, D, E),
                getBodyList(G, F, E, D, B, C, A),
                0);
    }
    //不缺项（分拣）
    private static ArrayList<Image> getImageList(ArrayList<?> A, ArrayList<?> B,
                                                 ArrayList<?> C, ArrayList<?> D,
                                                 ArrayList<?> E, ArrayList<?> F,
                                                 ArrayList<?> G){
        ArrayList<?> mixList = getMixList(A, B, C, D, E, F, G);
        if (mixList!=null)
            return getImage(mixList);
        else
            return null;
    }
    private static ArrayList<?> getMixList(ArrayList<?> A, ArrayList<?> B,
                                                 ArrayList<?> C, ArrayList<?> D,
                                                 ArrayList<?> E, ArrayList<?> F,
                                                 ArrayList<?> G){
        if (isImage(A))
            return A;
        if (isImage(B))
            return B;
        if (isImage(C))
            return C;
        if (isImage(D))
            return D;
        if (isImage(E))
            return E;
        if (isImage(F))
            return F;
        if (isImage(G))
            return G;
        return null;
    }
    private static boolean notMixList(ArrayList<?> A){
        if (A == null||A.isEmpty())
            return true;
        for (Object i : A)
            if (i instanceof Char||i instanceof Item)
                return false;
        return true;
    }
    private static boolean isImage(ArrayList<?> A){
        if (A == null)
            return false;
        for (Object i: A)
            if (i != null)
                return i instanceof Image || i instanceof Char || i instanceof Item;
        return false;
    }
    private static ArrayList<Image> getImage(ArrayList<?> A){
        ArrayList<Image> Array = new ArrayList<>();
        Image image;
        for (Object i: A)
            if ((image = getSignalImage(i))!=null)
                Array.add(image);
        return Array;
    }
    private static ArrayList<Float> getFloatList(ArrayList<?> A, ArrayList<?> B,
                                                 ArrayList<?> C, ArrayList<?> D,
                                                 ArrayList<?> E, ArrayList<?> F,
                                                 ArrayList<?> G){
        if (getFirst(A) instanceof Float)
            return (ArrayList<Float>)A;
        if (getFirst(B) instanceof Float)
            return (ArrayList<Float>)B;
        if (getFirst(C) instanceof Float)
            return (ArrayList<Float>)C;
        if (getFirst(D) instanceof Float)
            return (ArrayList<Float>)D;
        if (getFirst(E) instanceof Float)
            return (ArrayList<Float>)E;
        if (getFirst(F) instanceof Float)
            return (ArrayList<Float>)F;
        if (getFirst(G) instanceof Float)
            return (ArrayList<Float>)G;
        return null;
    }
    private static ArrayList<MovieClip.Animation> getActionList(ArrayList<?> A, ArrayList<?> B,
                                                               ArrayList<?> C, ArrayList<?> D,
                                                               ArrayList<?> E, ArrayList<?> F,
                                                               ArrayList<?> G){
        if (getFirst(A) instanceof MovieClip.Animation)
            return (ArrayList<MovieClip.Animation>)A;
        if (getFirst(B) instanceof MovieClip.Animation)
            return (ArrayList<MovieClip.Animation>)B;
        if (getFirst(C) instanceof MovieClip.Animation)
            return (ArrayList<MovieClip.Animation>)C;
        if (getFirst(D) instanceof MovieClip.Animation)
            return (ArrayList<MovieClip.Animation>)D;
        if (getFirst(E) instanceof MovieClip.Animation)
            return (ArrayList<MovieClip.Animation>)E;
        if (getFirst(F) instanceof MovieClip.Animation)
            return (ArrayList<MovieClip.Animation>)F;
        if (getFirst(G) instanceof MovieClip.Animation)
            return (ArrayList<MovieClip.Animation>)G;
        return null;
    }
    private static ArrayList<Integer> getIntegerList(ArrayList<?> A, ArrayList<?> B,
                                                     ArrayList<?> C, ArrayList<?> D,
                                                     ArrayList<?> E, ArrayList<?> F,
                                                     ArrayList<?> G){
        if (getFirst(A) instanceof Integer)
            return (ArrayList<Integer>)A;
        if (getFirst(B) instanceof Integer)
            return (ArrayList<Integer>)B;
        if (getFirst(C) instanceof Integer)
            return (ArrayList<Integer>)C;
        if (getFirst(D) instanceof Integer)
            return (ArrayList<Integer>)D;
        if (getFirst(E) instanceof Integer)
            return (ArrayList<Integer>)E;
        if (getFirst(F) instanceof Integer)
            return (ArrayList<Integer>)F;
        if (getFirst(G) instanceof Integer)
            return (ArrayList<Integer>)G;
        return null;
    }
    private static ArrayList<Boolean> getBooleanList(ArrayList<?> A, ArrayList<?> B,
                                                     ArrayList<?> C, ArrayList<?> D,
                                                     ArrayList<?> E, ArrayList<?> F,
                                                     ArrayList<?> G){
        if (getFirst(A) instanceof Boolean)
            return (ArrayList<Boolean>)A;
        if (getFirst(B) instanceof Boolean)
            return (ArrayList<Boolean>)B;
        if (getFirst(C) instanceof Boolean)
            return (ArrayList<Boolean>)C;
        if (getFirst(D) instanceof Boolean)
            return (ArrayList<Boolean>)D;
        if (getFirst(E) instanceof Boolean)
            return (ArrayList<Boolean>)E;
        if (getFirst(F) instanceof Boolean)
            return (ArrayList<Boolean>)F;
        if (getFirst(G) instanceof Boolean)
            return (ArrayList<Boolean>)G;
        return null;
    }
    private static ArrayList<String> getTitleList(ArrayList<?> A, ArrayList<?> B,
                                                  ArrayList<?> C, ArrayList<?> D,
                                                  ArrayList<?> E, ArrayList<?> F,
                                                  ArrayList<?> G){
        if (!onlyOneString(A, B, C, D, E, F, G)){
            //只有一个String列表的时候，默认这是内容列表，此时将其留给主体内容，标题空出，获取混合列表的名称
            ArrayList<String> title = passBodyList(A, B, C, D, E, F, G, getStringList(B, A, G, F, E, D, C));
            if (title==null||title.isEmpty())
                return getNameList(A, B, C, D, E, F, G);
            ArrayList<?> mixList = getMixList(A, B, C, D, E, F, G);
            if (notMixList(mixList))
                return title;
            if (title.size()<mixList.size())
                //标题数组长度小于混合数组长度，以混合数组超出长度的项进行补充（允许已提供足够长度的标题/内容数组时，塞进来混合数组而不改变标题/内容页数）
                for (int i = title.size(); i < mixList.size() ; i++)
                    title.add(getSignalName(mixList.get(i)));
            return title;
        }
        else
            return getNameList(A, B, C, D, E, F, G);
    }
    private static ArrayList<String> getStringList(ArrayList<?> A, ArrayList<?> B,
                                                   ArrayList<?> C, ArrayList<?> D,
                                                   ArrayList<?> E, ArrayList<?> F,
                                                   ArrayList<?> G){
        if (getFirst(A) instanceof String)
            return (ArrayList<String>)A;
        if (getFirst(B) instanceof String)
            return (ArrayList<String>)B;
        if (getFirst(C) instanceof String)
            return (ArrayList<String>)C;
        if (getFirst(D) instanceof String)
            return (ArrayList<String>)D;
        if (getFirst(E) instanceof String)
            return (ArrayList<String>)E;
        if (getFirst(F) instanceof String)
            return (ArrayList<String>)F;
        if (getFirst(G) instanceof String)
            return (ArrayList<String>)G;
        return null;
    }
    private static ArrayList<String> getNameList(ArrayList<?> A, ArrayList<?> B,
                                                 ArrayList<?> C, ArrayList<?> D,
                                                 ArrayList<?> E, ArrayList<?> F,
                                                 ArrayList<?> G){
        ArrayList<?> mixList = getMixList(A, B, C, D, E, F, G);
        if (mixList!=null){
            return getNameList(mixList);
        }
        return null;
    }
    private static ArrayList<String> getNameList(ArrayList<?> A){
        ArrayList<String> title = new ArrayList<>();
        for (Object i :A) {
            String name = getSignalName(i);
            if (name!=null)
                title.add(name);
        }
        return title;
    }
    private static boolean onlyOneString(ArrayList<?> A, ArrayList<?> B,
                                         ArrayList<?> C, ArrayList<?> D,
                                         ArrayList<?> E, ArrayList<?> F,
                                         ArrayList<?> G){
        int StringNum = 0;
        if (getFirst(A) instanceof String)
            StringNum++;
        if (getFirst(B) instanceof String)
            StringNum++;
        if (getFirst(C) instanceof String)
            StringNum++;
        if (getFirst(D) instanceof String)
            StringNum++;
        if (getFirst(E) instanceof String)
            StringNum++;
        if (getFirst(F) instanceof String)
            StringNum++;
        if (getFirst(G) instanceof String)
            StringNum++;
        return StringNum<=1;
    }
    private static ArrayList<String> passBodyList(ArrayList<?> A, ArrayList<?> B,
                                                  ArrayList<?> C, ArrayList<?> D,
                                                  ArrayList<?> E, ArrayList<?> F,
                                                  ArrayList<?> G, ArrayList<String> body){
        ArrayList<String> SecondString;
        if ((SecondString = passBodyList(A, body))==null
                &&(SecondString = passBodyList(B, body))==null
                &&(SecondString = passBodyList(C, body))==null
                &&(SecondString = passBodyList(D, body))==null
                &&(SecondString = passBodyList(E, body))==null
                &&(SecondString = passBodyList(F, body))==null){
            SecondString = passBodyList(G, body);
        }
        return SecondString;
    }
    private static ArrayList<String> passBodyList(ArrayList<?> A, ArrayList<String> body){
        if (A!=body&&getFirst(A) instanceof String)
            return (ArrayList<String>) A;
        return null;
    }
    private static ArrayList<String> getBodyList(ArrayList<?> A, ArrayList<?> B,
                                                 ArrayList<?> C, ArrayList<?> D,
                                                 ArrayList<?> E, ArrayList<?> F,
                                                 ArrayList<?> G){
        ArrayList<String> body = getStringList(A, B, C, D, E, F, G);
        if (body==null)
            return getInfoList(A, B, C, D, E, F, G);
        else {
            ArrayList<?> mixList = getMixList(A, B, C, D, E, F ,G);
            if (notMixList(mixList))
                return body;
            if (body.size()<mixList.size())
                //内容数组长度小于混合数组的情况下，以混合数组超出长度部分进行补充（允许已提供足够长度的标题/内容数组时，塞进来混合数组而不改变标题/内容页数）
                for (int i = body.size(); i<mixList.size(); i++)
                    body.add(getSignalInfo(mixList.get(i)));
            return body;
        }
    }
    private static ArrayList<String> getInfoList(ArrayList<?> A, ArrayList<?> B,
                                                 ArrayList<?> C, ArrayList<?> D,
                                                 ArrayList<?> E, ArrayList<?> F,
                                                 ArrayList<?> G){
        ArrayList<?> mixList = getMixList(A, B, C, D, E, F, G);
        if (mixList!=null){
            return getInfoList(mixList);
        }
        return null;
    }
    private static ArrayList<String> getInfoList(ArrayList<?> A){
        ArrayList<String> body = new ArrayList<>();
        for (Object i :A) {
            String info = getSignalInfo(i);
            if (info!=null)
                body.add(info);
        }
        return body;
    }
}
