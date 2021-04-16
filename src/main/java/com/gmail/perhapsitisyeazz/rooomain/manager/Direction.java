package com.gmail.perhapsitisyeazz.rooomain.manager;

public enum Direction {
	NORTH,
	NORTH_EAST,
	EAST,
	SOUTH_EAST,
	SOUTH,
	SOUTH_WEST,
	WEST,
	NORTH_WEST,
	NOT_FOUND;

	public static final Direction[] directions = {NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST};

	public static boolean isOpposite(Direction cp1, Direction cp2) {
		return check(cp1, cp2, SOUTH, SOUTH_WEST, WEST, NORTH_WEST, NORTH, NORTH_EAST, EAST, SOUTH_EAST);
	}
	public static boolean isAtRight(Direction cp1, Direction cp2) {
		return check(cp1, cp2, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST, NORTH, NORTH_EAST);
	}
	public static boolean isAtLeft(Direction cp1, Direction cp2) {
		return check(cp1, cp2, WEST, NORTH_WEST, NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST);
	}
	public static boolean isAtHalfRight(Direction cp1, Direction cp2) {
		return check(cp1, cp2, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST, NORTH);
	}
	public static boolean isAtHalfLeft(Direction cp1, Direction cp2) {
		return check(cp1, cp2, NORTH_WEST, NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST);
	}
	public static boolean isAtOneAndAHalfRight(Direction cp1, Direction cp2) {
		return check(cp1, cp2, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST, NORTH, NORTH_EAST, EAST);
	}
	public static boolean isAtOneAndAHalfLeft(Direction cp1, Direction cp2) {
		return check(cp1, cp2, SOUTH_WEST, WEST, NORTH_WEST, NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH);
	}
	private static boolean check(Direction cp1, Direction cp2, Direction d1, Direction d2, Direction d3,
	                             Direction d4, Direction d5, Direction d6, Direction d7, Direction d8) {
		if (cp1 == NORTH && cp2 == d1)
			return true;
		else if (cp1 == NORTH_EAST && cp2 == d2)
			return true;
		else if (cp1 == EAST && cp2 == d3)
			return true;
		else if (cp1 == SOUTH_EAST && cp2 == d4)
			return true;
		else if (cp1 == SOUTH && cp2 == d5)
			return true;
		else if (cp1 == SOUTH_WEST && cp2 == d6)
			return true;
		else if (cp1 == WEST && cp2 == d7)
			return true;
		else return (cp1 == NORTH_WEST && cp2 == d8);
	}
}
