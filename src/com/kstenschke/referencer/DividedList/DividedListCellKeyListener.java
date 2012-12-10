/*
 * Copyright 2012 Kay Stenschke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kstenschke.referencer.DividedList;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.awt.event.KeyEvent.*;

public class DividedListCellKeyListener implements KeyListener {

	Boolean isVkControlDown = false;
	private Boolean isVkDownDown	= false;
	private Boolean isVkUpDown		= false;

	Boolean isGoingDown		= false;
	Boolean isGoingUp		= false;

	private Boolean isSectionJumpAlreadyDone = true;	// only one jump per cursor key press



	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == VK_CONTROL) isVkControlDown = true;

		if ( e.getKeyCode() == VK_DOWN ) {
				isVkDownDown	= true;
				isGoingDown		= true;
				isGoingUp		= false;
				isSectionJumpAlreadyDone	= false;
		} else if ( e.getKeyCode() == VK_UP ) {
			isVkUpDown		= true;
			isGoingDown		= false;
			isGoingUp		= true;
			isSectionJumpAlreadyDone	= false;
		}
	}



	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == VK_CONTROL ) {
			isVkControlDown = false;
		}
		if ( e.getKeyCode() == VK_DOWN ) {
			isVkDownDown	= false;
			isSectionJumpAlreadyDone	= true;
		}
		if ( e.getKeyCode() == VK_UP ) {
			isVkUpDown	= false;
			isSectionJumpAlreadyDone	= true;
		}
	}



	public void keyTyped(KeyEvent e) {

	}

}
