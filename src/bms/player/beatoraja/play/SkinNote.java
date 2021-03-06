package bms.player.beatoraja.play;

import bms.player.beatoraja.MainState;
import bms.player.beatoraja.SkinConfig;
import bms.player.beatoraja.skin.*;
import bms.player.beatoraja.skin.Skin.SkinObjectRenderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * ノーツオブジェクト
 * 
 * @author exch
 */
public class SkinNote extends SkinObject {

	private SkinLane[] lanes;
	
	private LaneRenderer renderer;
	private long time;

	public SkinNote(SkinSource[] note, SkinSource[][] longnote, SkinSource[] minenote) {
		lanes = new SkinLane[note.length];
		for(int i = 0;i < lanes.length;i++) {
			lanes[i] = new SkinLane(note[i],longnote[i], minenote[i]);
		}
		
        this.setDestination(0, 0, 0, 0, 0, 0, 0, 255, 255, 255, 0, 0, 0, 0, 0, 0, new int[0]);
	}

	public void setLaneRegion(Rectangle[] region, float[] scale, int[] dstnote2, Skin skin) {
		for(int i = 0;i < lanes.length;i++) {
			for(int oid : this.getOffsetID()) {
				SkinConfig.Offset offset = skin.getOffset().get(oid);
				if(offset != null) {
					region[i].x += offset.x - offset.w / 2;
					region[i].y += offset.y - offset.h / 2;
					region[i].width += offset.w;
					scale[i] += offset.h;
//					region[i].r += offset[4];
				}
			}
			lanes[i].setDestination(0,region[i].x, region[i].y, region[i].width, region[i].height, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
			lanes[i].scale =  scale[i];
			lanes[i].dstnote2 =  dstnote2[i];
		}
	}

	@Override
	public void prepare(long time, MainState state) {
		if(renderer == null) {
			final BMSPlayer player = (BMSPlayer) state;
			if (player.getLanerender() == null) {
				draw = false;
				return;
			}
			renderer = player.getLanerender();
		}
		this.time = time;
		super.prepare(time, state);
		for(SkinLane lane : lanes) {
			lane.prepare(time, state);
		}
	}

	public void draw(SkinObjectRenderer sprite) {
		renderer.drawLane(sprite, time, lanes);
	}
	
	public void draw(SkinObjectRenderer sprite, long time, MainState state) {
		draw(sprite);
	}

	@Override
	public void dispose() {
		if (lanes != null) {
			disposeAll(lanes);
			lanes = null;
		}
	}

	static class SkinLane extends SkinObject {
		/**
		 * ノーツ画像
		 */
		SkinSource note;
		/**
		 * ロングノーツ画像
		 */
		SkinSource[] longnote = new SkinSource[10];
		/**
		 * 地雷ノーツ画像
		 */
		SkinSource minenote;

		float scale;
		int dstnote2;

		/**
		 * 不可視ノーツ画像
		 */
		SkinSource hiddennote;
		/**
		 * 処理済ノーツ画像
		 */
		SkinSource processednote;

		public SkinLane(SkinSource note, SkinSource[] longnote, SkinSource minenote) {
			this.note = note;
			this.longnote = longnote;
			this.minenote = minenote;

			Pixmap hn = new Pixmap(32, 8, Pixmap.Format.RGBA8888);
			hn.setColor(Color.ORANGE);
			hn.drawRectangle(0, 0, hn.getWidth(), hn.getHeight());
			hn.drawRectangle(1, 1, hn.getWidth() - 2, hn.getHeight() - 2);
			Pixmap pn = new Pixmap(32, 8, Pixmap.Format.RGBA8888);
			pn.setColor(Color.CYAN);
			pn.drawRectangle(0, 0, hn.getWidth(), hn.getHeight());
			pn.drawRectangle(1, 1, hn.getWidth() - 2, hn.getHeight() - 2);
			hiddennote = new SkinSourceImage(new TextureRegion(new Texture(hn)));
			processednote = new SkinSourceImage(new TextureRegion(new Texture(pn)));
			hn.dispose();
			pn.dispose();			
		}

		public SkinLane(SkinSource note, SkinSource[] longnote, SkinSource minenote, SkinSource hiddennote, SkinSource processednote) {
			this.note = note;
			this.longnote = longnote;
			this.minenote = minenote;
			this.hiddennote = hiddennote;
			this.processednote = processednote;
		}

		public void draw(SkinObjectRenderer sprite) {
		}

		public void draw(SkinObjectRenderer sprite, long time, MainState state) {
		}
		
		@Override
		public void dispose() {
			if (note != null) {
				note.dispose();
				note = null;
			}
			if (longnote != null) {
				disposeAll(longnote);
				longnote = null;
			}
			if (minenote != null) {
				minenote.dispose();
				minenote = null;
			}
			if (hiddennote != null) {
				hiddennote.dispose();
				hiddennote = null;
			}
			if (processednote != null) {
				processednote.dispose();
				processednote = null;
			}
		}
	}
}

