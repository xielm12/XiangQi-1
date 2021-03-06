/*
XQWLMIDlet.java - Source Code for XiangQi Wizard Light, Part III

XiangQi Wizard Light - a Chinese Chess Program for Java ME
Designed by Morning Yellow, Version: 1.42, Last Modified: May 2010
Copyright (C) 2004-2010 www.xqbase.com

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package com.jameschen.game.chess;

import android.app.Activity;

import com.jameschen.game.util.Util;

public class ChessGame extends Activity {
	private static final String STORE_NAME = "XQWLight";

	static final String[] SOUND_NAME = { "click", "illegal", "move", "move2",
			"capture", "capture2", "check", "check2", "win", "draw", "loss", };

	static final int RS_DATA_LEN = 512;

	/**
	 * 0: Status, 0 = Startup Form, 1 = Red To Move, 2 = Black To Move<br>
	 * 16: Player, 0 = Red, 1 = Black (Flipped), 2 = Both<br>
	 * 17: Handicap, 0 = None, 1 = 1 Knight, 2 = 2 Knights, 3 = 9 Pieces<br>
	 * 18: Level, 0 = Beginner, 1 = Amateur, 2 = Expert<br>
	 * 19: Sound Level, 0 = Mute, 5 = Max<br>
	 * 20: Music Level, 0 = Mute, 5 = Max<br>
	 * 256-511: Squares
	 */
	byte[] rsData = new byte[RS_DATA_LEN];
	int moveMode, handicap, level, sound, music;

	private ChessView mChessView;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mChessView = new ChessView(this);
		setContentView(mChessView);
		Position.readBookData(this);
		startApp();
	}

	private boolean started = false;

	public void startApp() {
		if (started) {
			return;
		}
		started = true;
		for (int i = 0; i < RS_DATA_LEN; i++) {
			rsData[i] = 0;
		}
		rsData[19] = 3;
		rsData[20] = 2;

		moveMode = Util.MIN_MAX(0, rsData[16], 2);
		handicap = Util.MIN_MAX(0, rsData[17], 3);
		level = Util.MIN_MAX(0, rsData[18], 2);
		sound = Util.MIN_MAX(0, rsData[19], 5);
		music = Util.MIN_MAX(0, rsData[20], 5);

		mChessView.load(rsData, handicap, moveMode, level);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		destroyApp(true);
		super.onDestroy();
	}

	public void destroyApp(boolean unc) {
		rsData[16] = (byte) moveMode;
		rsData[17] = (byte) handicap;
		rsData[18] = (byte) level;
		rsData[19] = (byte) sound;
		rsData[20] = (byte) music;

		started = false;
	}
}