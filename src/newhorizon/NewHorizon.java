package newhorizon;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import arc.util.Time;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.mod.Mod;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import newhorizon.content.*;
import newhorizon.effects.EffectTrail;
import newhorizon.func.NHSetting;
import newhorizon.func.SettingDialog;

import java.io.IOException;

import static newhorizon.func.TableFuncs.*;


public class NewHorizon extends Mod{
	public static final String NHNAME = "new-horizon-";
	
	private void links(){
		BaseDialog dialog = new BaseDialog("@links");
		addLink(dialog.cont, Icon.github, "Github", "https://github.com/Yuria-Shikibe/NewHorizonMod.git");
		dialog.cont.button("@back", Icon.left, Styles.cleart, dialog::hide).size(LEN * 3, LEN).padLeft(OFFSET / 2);
		dialog.show();
	}
	
	private void addLink(Table table, TextureRegionDrawable icon, String buttonName, String link){
		table.button(buttonName, icon, Styles.cleart, () -> {
			BaseDialog dialog = new BaseDialog("@link");
			dialog.addCloseListener();
			dialog.cont.pane(t -> t.add("[gray]" + Core.bundle.get("confirm.link") + ": [accent]" + link + " [gray]?")).fillX().height(LEN / 2f).row();
			dialog.cont.image().fillX().pad(8).height(4f).color(Pal.accent).row();
			dialog.cont.pane(t -> {
				t.button("@back", Icon.left, Styles.cleart, dialog::hide).size(LEN * 3, LEN);
				t.button("@confirm", Icon.link, Styles.cleart, () -> Core.app.openURI(link)).size(LEN * 3, LEN).padLeft(OFFSET / 2);
			}).fillX();
			dialog.show();
		}).size(LEN * 3, LEN).left().row();
	}
	
	private void logShow(){
		new BaseDialog("@log"){{
			cont.table(Tex.buttonEdge3, table -> {
				table.button("@back", Icon.left, this::hide).fillX().height(LEN).row();
				table.image().fillX().height(OFFSET / 3).pad(OFFSET / 2).row();
				this.addCloseListener();
				table.pane(t -> {
					t.add(
						"Fixed crash problems.\n\nAdd two turrets.\n\nFixed some UI problems.\n\n(I really don't want to write this garbage.)"
					);
				}).fill();
			}).fill();
		}}.show();
	}
	
    public NewHorizon(){
        Log.info("Loaded NewHorizon Mod constructor.");
        Events.on(ClientLoadEvent.class, e -> Time.runTask(10f, () -> {
			BaseDialog dialog = new BaseDialog("Welcome");
			dialog.addCloseListener();
			dialog.cont.pane(table -> {
				table.image(Core.atlas.find(NHNAME + "upgrade")).row();
				table.image().width(LEN * 5).height(OFFSET / 2.5f).pad(OFFSET / 3f).color(Color.white).row();
				table.add("[white]<< Powered by NewHorizonMod >>", Styles.techLabel).row();
				table.image().width(LEN * 5).height(OFFSET / 2.5f).pad(OFFSET / 3f).color(Color.white).row();
				table.add("").row();
			}).width(Core.graphics.getWidth() - LEN).growY().center().row();
			dialog.cont.table(Tex.clear, table -> {
				table.button("@back", Icon.left, Styles.cleart, () -> {
					dialog.hide();
					NHSetting.settingApply();
				}).size(LEN * 2f, LEN);
				table.button("@links", Icon.link, Styles.cleart, this::links).size(LEN * 2f, LEN).padLeft(OFFSET / 2);
				table.button("@settings", Icon.settings, Styles.cleart, () -> new SettingDialog().show()).size(LEN * 2f, LEN).padLeft(OFFSET / 2);
				table.button("@log", Icon.book, Styles.cleart, this::logShow).size(LEN * 2f, LEN).padLeft(OFFSET / 2);
			}).fillX().height(LEN + OFFSET);
			dialog.show();
			tableMain();
        }));
    }
    
    @Override
    public void loadContent(){
	    try{
		    NHSetting.settingFile();
		    NHSetting.initSetting();
		    NHSetting.initSettingList();
	    }catch(IOException e){
		    throw new IllegalArgumentException(e);
	    }
	    Log.info("Loading NewHorizon Mod Objects");
	    NHSounds.load();
		NHLoader loader = new NHLoader();
		loader.load();
	    new NHItems().load();
	    new NHLiquids().load();
	    new NHBullets().load();
		new NHUpgradeDatas().load();
		new NHUnits().load();
		new NHBlocks().load();
		//new NHPlanets().load();
	    new NHTechTree().load();
	    loader.loadLast();
    }
	
	
}
