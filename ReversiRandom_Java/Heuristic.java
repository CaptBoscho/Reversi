
public class Heuristic {

	public static int colorDiff(int[][] state, int team) {
	
		int enemy = (team % 2) + 1;
		int diff = 0;
		for (int n = 0; n < 8; n++) {
			for (int m = 0; m < 8; m++) {
			     if (state[n][m] == team)
			    	 diff++;
			     if (state[n][m] == enemy)
			    	 diff--;
			}
		}
		
		return diff;
	}
	
	static final int EDGE_BENEFIT = 5;
    static final int GROUNDED_EDGE = 15;
	static final int CORNER_BENEFIT = 40;
	static final int SWEET_SIXTEEN = 5;
	public static int colorDiffWithCorners(int[][] state, int team, int turn) {
		
		int enemy = (team % 2) + 1;
		int diff = 0;
		for (int n = 0; n < 8; n++) {
			for (int m = 0; m < 8; m++) {
                if (turn > 30) {
                    if (state[n][m] == team)
                        diff++;
                    else if (state[n][m] == enemy)
                        diff--;
                }
                if (turn < 54) {
                    //Edges
                    if (n == 0 || n == 7 && (m > 0 && m < 7)) {
                        boolean grounded = true;
                        int test1 = m - 1;
                        while (test1 >= 0) {
                            if (state[n][test1] != team) {
                                grounded = false;
                            }
                            test1--;
                        }

                        boolean grounded2 = true;
                        test1 = m + 1;
                        while (test1 <= 7) {
                            if (state[n][test1] != team) {
                                grounded = false;
                            }
                            test1++;
                        }

                        if (grounded || grounded2) {
                            if (state[n][m] == team) {
                                diff += GROUNDED_EDGE;
                            } else {
                                diff -= GROUNDED_EDGE;
                            }
                        } else {
                            //Disjoint Edges
                            int place = m - 1;
                            boolean good = true;
                            while (place >= 1) {
                                if (state[n][place] == enemy) {
                                    good = false;
                                } else if (state[n][place] == 0) {
                                    break;
                                }
                                place--;
                            }

                            place = m + 1;
                            boolean good2 = true;
                            while (place <= 6) {
                                if (state[n][place] == enemy) {
                                    good2 = false;
                                } else if (state[n][place] == 0) {
                                    break;
                                }
                                place++;
                            }

                            if (good && good2) {
                                if (state[n][m] == team) {
                                    diff += GROUNDED_EDGE;
                                } else {
                                    diff -= GROUNDED_EDGE;
                                }
                            } else {
                                if (state[n][m] == team) {
                                    diff -= GROUNDED_EDGE;
                                } else {
                                    diff += GROUNDED_EDGE;
                                }
                            }

                        }
                    }

                    if (m == 0 || m == 7 && (n > 0 && n < 7)) {
                        boolean grounded = true;
                        int test1 = n - 1;
                        while (test1 >= 0) {
                            if (state[test1][m] != team) {
                                grounded = false;
                            }
                            test1--;
                        }

                        boolean grounded2 = true;
                        test1 = n + 1;
                        while (test1 <= 7) {
                            if (state[test1][m] != team) {
                                grounded = false;
                            }
                            test1++;
                        }

                        if (grounded || grounded2) {
                            if (state[n][m] == team) {
                                diff += GROUNDED_EDGE;
                            } else {
                                diff -= GROUNDED_EDGE;
                            }

                        } else {
                            //Disjoint Edges
                            int place = n - 1;
                            boolean good = true;
                            while (place >= 1) {
                                if (state[place][m] == enemy) {
                                    good = false;
                                } else if (state[place][m] == 0) {
                                    break;
                                }
                                place--;
                            }

                            place = n + 1;
                            boolean good2 = true;
                            while (place <= 6) {
                                if (state[place][m] == enemy) {
                                    good2 = false;
                                } else if (state[place][m] == 0) {
                                    break;
                                }
                                place++;
                            }

                            if (good && good2) {
                                if (state[n][m] == team) {
                                    diff += GROUNDED_EDGE;
                                } else {
                                    diff -= GROUNDED_EDGE;
                                }
                            } else {
                                if (state[n][m] == team) {
                                    diff -= GROUNDED_EDGE;
                                } else {
                                    diff += GROUNDED_EDGE;
                                }
                            }
                        }
                    }


                    //CORNER
                    if ((n == 0 && m == 0) || (n == 0 && m == 7) || (n == 7 && m == 0) || (n == 7 && m == 7)) {
                        if (state[n][m] == team)
                            diff += CORNER_BENEFIT;
                        else if (state[n][m] == enemy)
                            diff -= CORNER_BENEFIT;
                    }


                    //SWEET SIXTEEN
                    if (turn < 30) {
                        if ((n >= 2) && (n <= 5) && (m >= 2) && (m <= 5)) {
                            if (state[n][m] == team) {
                                diff += SWEET_SIXTEEN;
                            } else if (state[n][m] == enemy) {
                                diff -= SWEET_SIXTEEN;
                            }
                        }
                    }

                    //Most undesirable squares
                    if ((n == 1 && m == 1 && state[0][0] != team) || (n == 1 && m == 6 && state[0][7] != team)
                            || (n == 6 && m == 1 && state[7][0] != team) || (n == 6 && m == 6 && state[7][7] != team)) {
                        if (state[n][m] == team) {
                            diff = -1000;
                        } else if (state[n][m] == enemy) {
                            diff = 1000;
                        }
                    }
                }
            }
		}
		return diff;
		
	}
}
