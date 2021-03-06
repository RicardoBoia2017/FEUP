intermediateBoard(Board):-
	whiteKing(WhiteKing),
	whiteQueen(WhiteQueen),
	whiteRook1(WhiteRook1),
	whiteRook2(WhiteRook2),
	whiteBishop1(WhiteBishop1),
	whiteBishop2(WhiteBishop2),
	whiteKnigth1(WhiteKnigth1),
	whiteKnigth2(WhiteKnigth2),

	blackKing(BlackKing),
	blackQueen(BlackQueen),
	blackRook1(BlackRook1),
	blackRook2(BlackRook2),
	blackBishop1(BlackBishop1),
	blackBishop2(BlackBishop2),
	blackKnigth1(BlackKnigth1),
	blackKnigth2(BlackKnigth2),

	nonePiece(NonePiece),

	append([], [
	[BlackRook1, BlackBishop2, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece],
	[BlackKing, BlackQueen, NonePiece, NonePiece, NonePiece, NonePiece, WhiteRook1, WhiteKing],
	[NonePiece, WhiteRook2, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece],
	[NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece],
	[NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, BlackBishop1, NonePiece, NonePiece],
	[NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece],
	[NonePiece, NonePiece, NonePiece, BlackKnigth1, WhiteKnigth1, WhiteBishop1, WhiteBishop2, NonePiece],
  [NonePiece, BlackRook2, BlackBishop2, BlackKnigth2, WhiteKnigth2, NonePiece, NonePiece, NonePiece]], Board).



finalBoard(Board):-
  whiteKing(WhiteKing),
  whiteQueen(WhiteQueen),
  whiteRook1(WhiteRook1),
  whiteRook2(WhiteRook2),
  whiteBishop1(WhiteBishop1),
  whiteBishop2(WhiteBishop2),
  whiteKnigth1(WhiteKnigth1),
  whiteKnigth2(WhiteKnigth2),

  blackKing(BlackKing),
  blackQueen(BlackQueen),
  blackRook1(BlackRook1),
  blackRook2(BlackRook2),
  blackBishop1(BlackBishop1),
  blackBishop2(BlackBishop2),
  blackKnigth1(BlackKnigth1),
  blackKnigth2(BlackKnigth2),

  nonePiece(NonePiece),

  append([], [
  [NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, WhiteKing, NonePiece],
  [NonePiece, WhiteRook1, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece],
  [NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece],
  [BlackKing, NonePiece, NonePiece, NonePiece, NonePiece, BlackBishop1, NonePiece, NonePiece],
  [NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece],
  [NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece],
  [NonePiece, BlackRook1, NonePiece, BlackKnigth1, WhiteKnigth1, WhiteBishop1, NonePiece, NonePiece],
  [NonePiece, NonePiece, BlackBishop2, BlackKnigth2, WhiteKnigth2, NonePiece, NonePiece, NonePiece]], Board).

testBoard(Board):-
	  whiteKing(WhiteKing),
	  whiteQueen(WhiteQueen),
	  whiteRook1(WhiteRook1),
	  whiteRook2(WhiteRook2),
	  whiteBishop1(WhiteBishop1),
	  whiteBishop2(WhiteBishop2),
	  whiteKnigth1(WhiteKnigth1),
	  whiteKnigth2(WhiteKnigth2),

	  blackKing(BlackKing),
	  blackQueen(BlackQueen),
	  blackRook1(BlackRook1),
	  blackRook2(BlackRook2),
	  blackBishop1(BlackBishop1),
	  blackBishop2(BlackBishop2),
	  blackKnigth1(BlackKnigth1),
	  blackKnigth2(BlackKnigth2),

	  nonePiece(NonePiece),

		append([], [
		[NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece],
		[NonePiece, BlackKing, NonePiece, NonePiece, NonePiece, NonePiece, WhiteKing, NonePiece],
		[NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece],
		[NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece],
		[NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece],
		[NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece],
		[NonePiece, BlackRook1, BlackBishop1, BlackKnigth1, WhiteKnigth1, WhiteBishop1, WhiteRook1, NonePiece],
	  [BlackQueen, BlackRook2, BlackBishop2, BlackKnigth2, WhiteKnigth2, WhiteBishop2, WhiteRook2, WhiteQueen]], Board).


stalemateBoard(Board):-
	whiteKing(WhiteKing),
	whiteQueen(WhiteQueen),
	whiteRook1(WhiteRook1),
	whiteRook2(WhiteRook2),
	whiteBishop1(WhiteBishop1),
	whiteBishop2(WhiteBishop2),
	whiteKnigth1(WhiteKnigth1),
	whiteKnigth2(WhiteKnigth2),

	blackKing(BlackKing),
	blackQueen(BlackQueen),
	blackRook1(BlackRook1),
	blackRook2(BlackRook2),
	blackBishop1(BlackBishop1),
	blackBishop2(BlackBishop2),
	blackKnigth1(BlackKnigth1),
	blackKnigth2(BlackKnigth2),

	nonePiece(NonePiece),

	append([], [
	  [NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece],
	  [NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece],
	  [NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece],
	  [NonePiece, NonePiece, NonePiece, BlackQueen, NonePiece, NonePiece, NonePiece, WhiteQueen],
	  [BlackKing, NonePiece, NonePiece, WhiteKnigth2, NonePiece, NonePiece, NonePiece, NonePiece],
	  [NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, WhiteKing, NonePiece],
	  [NonePiece, NonePiece, WhiteKnigth1, WhiteRook1, NonePiece, NonePiece, NonePiece, NonePiece],
		[NonePiece, NonePiece, NonePiece, NonePiece, NonePiece, WhiteRook2, NonePiece, NonePiece]], Board).
