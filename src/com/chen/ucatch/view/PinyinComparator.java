package com.chen.ucatch.view;

import java.util.Comparator;

import com.chen.ucatch.model.UserVO;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<UserVO> {

	public int compare(UserVO o1, UserVO o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
