package com.kandl.ropgame.view.sandwichView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.kandl.ropgame.RopGame;
import com.kandl.ropgame.model.Recipe.CookState;
import com.kandl.ropgame.model.Sandwich;

public class CutView extends Group {
	private Sandwich sandwich;
	private Image bread, dragUI;
	private Group cuts, dragImages;
	
	private final int OFFSET = 155;
	
	public CutView(Sandwich s) {
		cuts = new Group();
		dragImages = new Group();
		sandwich = s;
		bread = sandwich.getBread().getLargeTopView(CookState.fromTime(sandwich.getCookTime()));
		setSize(bread.getWidth() + 200, bread.getHeight() + 200);
		
		/*Pixmap p = new Pixmap(128, 128, Pixmap.Format.RGBA8888);
		p.setColor(Color.WHITE);
		p.fill();
		Texture t = new Texture(p);*/
		
		dragUI = new Image();//new Image(new TiledDrawable(new TextureRegion(t)));
		dragUI.setSize(bread.getWidth() + 300, bread.getHeight() + 300);
		DragListener listener = new DragListener() {
			private Vector2 cut, orig;
			private Image beginCircle, endCircle, cutLine;
			private Sprite line;
			
			@Override
			public void dragStart(InputEvent e, float x, float y, int pointer) {
				orig = CutView.this.dragUI.stageToLocalCoordinates(new Vector2(e.getStageX(), e.getStageY()));
				cut = new Vector2(0,0);
				
				beginCircle = new Image(new TextureRegionDrawable(RopGame.assets.get("img/icons/buttons.atlas", TextureAtlas.class).findRegion("endpiece")), Scaling.none);
				endCircle = new Image(beginCircle.getDrawable(), Scaling.none);
				line = RopGame.assets.get("img/icons/buttons.atlas", TextureAtlas.class).createSprite("cut_line");
				cutLine = new Image(new SpriteDrawable(line), Scaling.none);
				CutView.this.dragImages.addActor(cutLine);
				CutView.this.dragImages.addActor(beginCircle);
				CutView.this.dragImages.addActor(endCircle);
				beginCircle.setPosition(orig.x - 10, orig.y - 10);
				endCircle.setPosition(orig.x - 10, orig.y - 10);
				cutLine.setPosition(orig.x, orig.y - 5);
				line.setScale(0, 1);
				line.setOrigin(0, 5);
				cut.set(0, 0);
			}
			
			@Override
			public void drag(InputEvent e, float x, float y, int pointer) {
				cut = CutView.this.dragUI.stageToLocalCoordinates(cut.set(e.getStageX(), e.getStageY()));
				if (cut.x < 0 || cut.x > CutView.this.dragUI.getWidth() || cut.y < 0 || cut.y > CutView.this.dragUI.getHeight()) {
					if (cut.x < 0) {
						if (cut.y < 0) {
							float m = (orig.y - cut.y) / (orig.x - cut.x != 0 ? orig.x - cut.x : 1f / Float.POSITIVE_INFINITY);
							float diffX = -cut.x;
							if (cut.y + m * diffX >= 0) cut.set(0, cut.y + m * diffX);
							else {
								float diffY = -cut.y;
								m = 1f/(m != 0 ? m : 1f/Float.POSITIVE_INFINITY);
								cut.set(cut.x + m * diffY, 0);
							}
						} else if (cut.y > CutView.this.dragUI.getHeight()) {
							float m = (orig.y - cut.y) / (orig.x - cut.x != 0 ? orig.x - cut.x : 1f / Float.POSITIVE_INFINITY);
							float diffX = -cut.x;
							if (cut.y + m * diffX <= CutView.this.dragUI.getHeight()) cut.set(0, cut.y + m * diffX);
							else {
								float diffY = CutView.this.dragUI.getHeight() - cut.y;
								m = 1f/(m != 0 ? m : 1f/Float.POSITIVE_INFINITY);
								cut.set(cut.x + m * diffY, CutView.this.dragUI.getHeight());
							}
						} else {
							float m = (orig.y - cut.y) / (orig.x - cut.x != 0 ? orig.x - cut.x : 1f / Float.POSITIVE_INFINITY);
							float diffX = -cut.x;
							cut.set(0, cut.y + m * diffX);
						}
					}
					else if (cut.x > CutView.this.dragUI.getWidth()) {
						if (cut.y < 0) {
							float m = (cut.y - orig.y) / (cut.x - orig.x != 0 ? cut.x - orig.x : 1f/Float.POSITIVE_INFINITY);
							float diffX = CutView.this.dragUI.getWidth() - cut.x;
							if (cut.y + m * diffX >= 0) cut.set(CutView.this.dragUI.getWidth(), cut.y + m * diffX);
							else {
								float diffY = -cut.y;
								m = 1f/(m != 0 ? m : 1f/Float.POSITIVE_INFINITY);
								cut.set(cut.x + m * diffY, 0);
							}
						} else if (cut.y > CutView.this.dragUI.getHeight()) {
							float m = (cut.y - orig.y) / (cut.x - orig.x != 0 ? cut.x - orig.x : 1f/Float.POSITIVE_INFINITY);
							float diffX = CutView.this.dragUI.getWidth() - cut.x;
							if (cut.y + m * diffX <= CutView.this.dragUI.getHeight()) cut.set(CutView.this.dragUI.getWidth(), cut.y + m * diffX);
							else {
								float diffY = CutView.this.dragUI.getHeight() - cut.y;
								m = 1f/(m != 0 ? m : 1f/Float.POSITIVE_INFINITY);
								cut.set(cut.x + m * diffY, CutView.this.dragUI.getHeight());
							}
						} else {
							float m = (cut.y - orig.y)/ (cut.x - orig.x != 0 ? cut.x - orig.x : 1f/Float.POSITIVE_INFINITY);
							float diffX = CutView.this.dragUI.getWidth() - cut.x;
							cut.set(CutView.this.dragUI.getWidth(), cut.y + m * diffX);
						}
					} else {
						if (cut.y < 0) {
							float m = (orig.x - cut.x)/(orig.y - cut.y != 0 ? orig.y - cut.y : 1f/Float.POSITIVE_INFINITY);
							float diffY = -cut.y;
							cut.set(cut.x + m * diffY, 0);
						} else {
							float m = (cut.x - orig.x)/(cut.y-orig.y != 0 ? cut.y - orig.y : 1f/Float.POSITIVE_INFINITY);
							float diffY = CutView.this.dragUI.getHeight() - cut.y;
							cut.set(cut.x + m * diffY, CutView.this.dragUI.getHeight());
						}
					}
				}
				cut.sub(orig);
				line.setScale(cut.len() / 10f, 1);
				line.setRotation(cut.angle());
				endCircle.setPosition(cut.x + orig.x - 10, cut.y + orig.y - 10);
			}
			
			@Override
			public void dragStop(InputEvent e, float x, float y, int pointer) {
				beginCircle.remove();
				endCircle.remove();
				cutLine.remove();
				if (cut.len() < bread.getWidth()) {CutView.this.cantCut(); return;}
				if (cut.angle() >= 180) {
					cut.rotate(-180);
					orig.sub(cut);
				}
				cut.add(orig);
				if ((orig.y >= CutView.this.dragUI.getHeight() - OFFSET || cut.y <= OFFSET) ||
					(orig.x > OFFSET && orig.y > OFFSET && orig.x < CutView.this.dragUI.getWidth() - OFFSET) ||
					(cut.x > OFFSET && cut.x < CutView.this.dragUI.getWidth() - OFFSET &&
							cut.y < CutView.this.dragUI.getHeight() - OFFSET)) {CutView.this.cantCut(); return;}
				
				if (orig.x <= OFFSET) {
						if (cut.x <= OFFSET) {CutView.this.cantCut(); return;}
					if (orig.y <= OFFSET) {
							if (cut.y <= OFFSET) {CutView.this.cantCut(); return;}
						float m = (cut.y - orig.y)/(cut.x - orig.x != 0 ? cut.x - orig.x : 1f/Float.POSITIVE_INFINITY); //dy/dx
						float diffX = OFFSET - orig.x;
						float diffY;
						if (orig.y + m * diffX >= OFFSET) {
							orig.set(OFFSET, orig.y + m*diffX);
								if (orig.y >= CutView.this.dragUI.getHeight() - OFFSET) {CutView.this.cantCut(); return;}
						} else {
							m = 1f/(m != 0 ? m : 1f/Float.POSITIVE_INFINITY);
							diffY = OFFSET - orig.y;
							orig.set(orig.x + m * diffY, OFFSET);
								if (orig.x >= CutView.this.dragUI.getWidth() - OFFSET) {CutView.this.cantCut(); return;}
							m = 1f/(m != 0 ? m : 1f/Float.POSITIVE_INFINITY);
						}
						
						// m = dy/dx
						if (cut.y >= CutView.this.dragUI.getHeight() - OFFSET) {
							if (cut.x >= CutView.this.dragUI.getWidth() - OFFSET) {
								diffX = (CutView.this.dragUI.getWidth() - OFFSET) - cut.x;
								if (cut.y + m * diffX <= CutView.this.dragUI.getHeight())  {
									cut.set(CutView.this.dragUI.getWidth() - OFFSET, cut.y + m * diffX);
								} else {
									m = 1f/(m != 0 ? m : 1f/Float.POSITIVE_INFINITY);
									diffY = (CutView.this.dragUI.getHeight() - OFFSET) - cut.y;
									cut.set(cut.x + m * diffY, CutView.this.dragUI.getHeight() - OFFSET);
								}
							} else {
								m = 1f/(m != 0 ? m : 1f/Float.POSITIVE_INFINITY);
								diffY = (CutView.this.dragUI.getHeight() - OFFSET) - cut.y;
								cut.set(cut.x + m * diffY, CutView.this.dragUI.getHeight() - OFFSET);
							}
						} else {
							diffX = (CutView.this.dragUI.getWidth() - OFFSET) - cut.x;
							cut.set(CutView.this.dragUI.getWidth() - OFFSET, cut.y + m * diffX);
						}
					} else { // middle origin
						float m = (cut.y - orig.y)/(cut.x - orig.x); //dy/dx
						float diffX = OFFSET - orig.x;
						orig.set(OFFSET, orig.y + m * diffX);
							if (orig.y < OFFSET || orig.y >= CutView.this.dragUI.getHeight() - OFFSET) {CutView.this.cantCut(); return;}
						
						if (cut.x < CutView.this.dragUI.getWidth() - OFFSET){ //cut.y > orig.y, always, so cut.y > height
							m = 1f/(m != 0 ? m : 1f/Float.POSITIVE_INFINITY); //dx/dy
							float diffY = (CutView.this.dragUI.getHeight() - OFFSET) - cut.y;
							cut.set(cut.x + m * diffY, CutView.this.dragUI.getHeight() - OFFSET);
						} else {
							if (cut.y >= CutView.this.dragUI.getHeight() - OFFSET) {
								m = 1f/(m != 0 ? m : 1f/Float.POSITIVE_INFINITY); //dx/dy
								float diffY = (CutView.this.dragUI.getHeight() - OFFSET) - cut.y;
								if (cut.x + m * diffY <= CutView.this.dragUI.getWidth() - OFFSET) {
									cut.set(cut.x + m * diffY, CutView.this.dragUI.getHeight() - OFFSET);
								}
								else {
									m = 1f/(m != 0 ? m : 1f/Float.POSITIVE_INFINITY); //dy/dx
									diffX = (CutView.this.dragUI.getWidth() - OFFSET) - cut.x;
									cut.set(CutView.this.dragUI.getWidth(), cut.y + m * diffX);
								}
							} else {
								diffX = (CutView.this.dragUI.getWidth() - OFFSET) - cut.x;
								cut.set(CutView.this.dragUI.getWidth() - OFFSET, cut.y + m * diffX);
							}
						}
					}
				} else if (orig.x >= CutView.this.dragUI.getWidth() - OFFSET) { // right origin
						if (cut.x >= CutView.this.dragUI.getWidth() - OFFSET) {CutView.this.cantCut(); return;}
					if (orig.y <= OFFSET) {
							if (cut.y <= OFFSET) {CutView.this.cantCut(); return;}
						float m = (orig.x - cut.x)/(orig.y - cut.y != 0 ? orig.y-cut.y:1f/Float.POSITIVE_INFINITY);
						float diffY = OFFSET - orig.y;
						float diffX;
						if (orig.x + m * diffY <= CutView.this.dragUI.getWidth() - OFFSET) {
							orig.set(orig.x + m * diffY, OFFSET);
								if (orig.x <= OFFSET) {CutView.this.cantCut(); return;}
						} else {
							m = 1f/(m != 0 ? m : 1f/Float.POSITIVE_INFINITY);
							diffX = (CutView.this.dragUI.getWidth() - OFFSET) - orig.x;
							orig.set(CutView.this.dragUI.getWidth() - OFFSET, orig.y + m * diffX);
								if (orig.y >= CutView.this.dragUI.getHeight() - OFFSET) {CutView.this.cantCut(); return;}
							m = 1f/(m != 0 ? m : 1f/Float.POSITIVE_INFINITY);
						}
						
						// m = dx/dy
						if (cut.y >= CutView.this.getHeight() - OFFSET) {
							if (cut.x < OFFSET) {
								diffY = (CutView.this.dragUI.getHeight() - OFFSET) - cut.y;
								if (cut.x + m * diffY >= OFFSET) {
									cut.set(cut.x + m * diffY, CutView.this.dragUI.getHeight() - OFFSET);
								} else {
									m = 1f/(m != 0 ? m : 1f/Float.POSITIVE_INFINITY);
									diffX = OFFSET - cut.x;
									cut.set(OFFSET, cut.y + m * diffX);
								}
							} else {
								diffY = (CutView.this.dragUI.getHeight() - OFFSET) - cut.y;
								cut.set(cut.x + m * diffY, CutView.this.dragUI.getHeight() - OFFSET);
							}
						} else {
							m = 1f/(m != 0 ? m : 1f/Float.POSITIVE_INFINITY);
							diffX = OFFSET - cut.x;
							cut.set(OFFSET, cut.y + m * diffX);
						}
					} else {
						float m = (orig.y - cut.y)/(orig.x - cut.x != 0 ? orig.x-cut.x:1f/Float.POSITIVE_INFINITY);
						float diffX = (CutView.this.dragUI.getWidth() - OFFSET) - orig.x;
						float diffY;
						orig.set(CutView.this.dragUI.getWidth() - OFFSET, orig.y + m * diffX);
							if (orig.y >= CutView.this.dragUI.getHeight() - OFFSET) {CutView.this.cantCut(); return;}
							
						if (cut.y >= CutView.this.getHeight() - OFFSET) {
							if (cut.x < OFFSET) {
								diffX = OFFSET - cut.x;
								if (cut.y + m * diffX <= CutView.this.dragUI.getHeight() - OFFSET) {
									cut.set(OFFSET, cut.y + m * diffX);
								} else {
									m = 1f/(m != 0 ? m : 1f/Float.POSITIVE_INFINITY);
									diffY = (CutView.this.dragUI.getHeight() - OFFSET) - cut.y;
									cut.set(cut.x + m * diffY, CutView.this.dragUI.getHeight() - OFFSET);
								}
							} else {
								m = 1f/(m != 0 ? m : 1f/Float.POSITIVE_INFINITY);
								diffY = (CutView.this.dragUI.getHeight() - OFFSET) - cut.y;
								cut.set(cut.x + m * diffY, CutView.this.dragUI.getHeight() - OFFSET);
							}
						} else {
							diffX = OFFSET - cut.x;
							cut.set(OFFSET, cut.y + m * diffX);
						}
					}
				} else { // bottom origin
						if (cut.y <= OFFSET) {CutView.this.cantCut(); return;}
					float m = (orig.x - cut.x)/(orig.y - cut.y != 0 ? orig.y - cut.y : 1f/Float.NEGATIVE_INFINITY);
					float diffX;
					float diffY = OFFSET - orig.y;
					orig.set(orig.x + m * diffY, OFFSET);
						if (orig.x <= OFFSET || orig.x >= CutView.this.dragUI.getWidth() - OFFSET) {CutView.this.cantCut(); return;}
					
					// m = dx/dy
					if (cut.y >= CutView.this.dragUI.getHeight() - OFFSET) {
						diffY = (CutView.this.dragUI.getHeight() - OFFSET) - cut.y;
						if (cut.x < OFFSET) {
							if (cut.x + m * diffY >= OFFSET) {
								cut.set(cut.x + m * diffY, CutView.this.dragUI.getHeight() - OFFSET);
							} else {
								m = 1f/(m != 0 ? m : 1f/Float.POSITIVE_INFINITY);
								diffX = OFFSET - cut.x;
								cut.set(OFFSET, cut.y + m * diffX);
							}
						} else if (cut.x > CutView.this.dragUI.getWidth() - OFFSET) {
							if (cut.x + m * diffY <= CutView.this.dragUI.getWidth() - OFFSET) {
								cut.set(cut.x + m * diffY, CutView.this.dragUI.getHeight() - OFFSET);
							} else {
								m = 1f/(m != 0 ? m : 1f/Float.POSITIVE_INFINITY);
								diffX = (CutView.this.dragUI.getWidth() - OFFSET) - cut.x;
								cut.set(CutView.this.dragUI.getWidth() - OFFSET, cut.y + m * diffX);
							}
						} else {
							cut.set(cut.x + diffY * m, CutView.this.dragUI.getHeight() - OFFSET);
						}
					} else {
						m = 1f/(m != 0 ? m : 1f/Float.POSITIVE_INFINITY);
						if (cut.x <= OFFSET) {
							diffX = OFFSET - cut.x;
							cut.set(OFFSET, cut.y + m * diffX);
						} else {
							diffX = (CutView.this.dragUI.getWidth() - OFFSET) - cut.x;
							cut.set(CutView.this.dragUI.getWidth() - OFFSET, cut.y + m * diffX);
						}
					}
				}
				cut.sub(orig);
				CutView.this.cut(cut, orig);
			}
		};
		listener.setTapSquareSize(1);
		dragUI.addListener(listener);
		addActor(bread);
		addActor(cuts);
		addActor(dragUI);
		addActor(dragImages);
		dragImages.setTouchable(Touchable.disabled);
		bread.setPosition(150, 150);
	}
	
	public void cantCut() {
		Label l = new Label("Can't cut like that.", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/score.fnt"), false), Color.WHITE));
		dragImages.addActor(l);
		l.setSize(dragUI.getWidth(), 70);
		l.setPosition(0, 400);
		l.setAlignment(Align.center);
		l.addAction(Actions.sequence(Actions.parallel(Actions.moveBy(0, 100, 1), Actions.fadeOut(1)),
				Actions.removeActor()));
	}
	
	public void cut(Vector2 v, Vector2 p) {
		Vector2 adjDir = new Vector2(v).nor();
		Vector2 adjPos = new Vector2(p).add(v.x / 2f, v.y / 2f).sub(OFFSET, OFFSET).mul(1f/bread.getWidth(), 1f/bread.getHeight());
		sandwich.addCut(adjDir, adjPos);
		Sprite s = RopGame.assets.get("img/icons/buttons.atlas", TextureAtlas.class).createSprite("blank_box");
		Image i = new Image(new SpriteDrawable(s), Scaling.none);
		cuts.addActor(i);
		i.setPosition(p.x, p.y - 5);
		s.setOrigin(0, 5);
		s.setScale(v.len()/10f, 1);
		s.setRotation(v.angle());
	}
	
	public Sandwich getSandwich() {
		return sandwich;
	}
}
