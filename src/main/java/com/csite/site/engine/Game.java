package com.csite.site.engine;

public class Game {

    private String id;
    private Piece board[][] = new Piece[8][8];
    private int turn;

    public Game() {
        this.id = "nkfadjf";
        reset_board(); //sets Piece Board[][] to default value.
        this.turn = 0; //0 for white, 1 for black.
    }
    public Game(String id) {
        this.id = id;
        reset_board(); //sets Piece Board[][] to default value.
        this.turn = 0; //0 for white, 1 for black.
    }

    public Game(String id, String board, int turn) {
        this.id = id;
        board_from_string(board);
        this.turn = turn;
    }

    public String getId() {
        return this.id;
    }

    public String getBoard() {
        String g = "";
        System.out.println(board[7][7].to_string());
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null)  { g += "."; }
                else                      { g += board[i][j].to_string(); }
            }
            if (i <= 6) { g += "\n"; }
        }
        return g;
    }

    public int getTurn() {
        return this.turn;
    }

    public void board_from_string(String str_pure) {
        String bdd[] = str_pure.split("\n");
        int color = -1;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                color = (bdd[i].substring(j, j + 1).equals(bdd[i].substring(j, j + 1).toLowerCase())) ? 0 : 1;
                switch (bdd[i].substring(j, j + 1).toLowerCase()) {
                    case "p":  board[i][j] =   new   Pawn(i, j, color);  break;
                    case "k":  board[i][j] =   new   King(i, j, color);  break;
                    case "q":  board[i][j] =   new  Queen(i, j, color);  break;
                    case "b":  board[i][j] =   new Bishop(i, j, color);  break;
                    case "n":  board[i][j] =   new Knight(i, j, color);  break;
                    case "r":  board[i][j] =   new   Rook(i, j, color);  break;
                    default:   board[i][j] =   null;                     break;
                }
            }
        }
    }

    private void invert_turn() {
        turn = Math.abs(turn - 1);
    }

    public boolean is_checkmate() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null && board[i][j].Color == turn) {
                    for (int k = 0; k < 8; k++) {
                        for (int c = 0; c < 8; c++) {
                            if (valid_move(i, j, k, c, true)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean undo_check(int posy, int posx, int new_posy, int new_posx) {
        //Move piece.
        Piece old_spot = board[new_posy][new_posx];
        board[new_posy][new_posx] = board[posy][posx];
        board[posy][posx] = null;
        board[new_posy][new_posx].update_position(new_posy, new_posx);

        //Get if that color is still in check.
        boolean checked = is_check();

        //undo move.
        board[posy][posx] = board[new_posy][new_posx];
        board[new_posy][new_posx] = old_spot;
        board[posy][posx].update_position(posy, posx);

        return checked;
    }

    public boolean is_check() {
        int k_pos[];
        if (turn == 0)  { k_pos = get_position_piece("k"); }
        else            { k_pos = get_position_piece("K"); }
        invert_turn();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (valid_move(i, j, k_pos[0], k_pos[1], false)) { invert_turn(); return true; }
            }
        }

        invert_turn();
        return false;
    }

    public boolean valid_move(int posy, int posx, int n_posy, int n_posx, boolean u_check) {
        if (board[posy][posx] == null || board[posy][posx].Color != turn || (posy == n_posy && posx == n_posx)) { return false; }
        if (!board[posy][posx].is_valid_move(board, n_posy, n_posx)) { return false; }
        if ( u_check) { return !undo_check(posy, posx, n_posy, n_posx); }

        return true;
    }

    public boolean move_piece(int posy, int posx, int n_posy, int n_posx) {
        if (!valid_move(posy, posx, n_posy, n_posx, true)) {
            return false;
        }
        board[n_posy][n_posx] = board[posy][posx];
        board[n_posy][n_posx].update_position(n_posy, n_posx);
        board[posy][posx] = null;
        invert_turn();
        return true;
    }

    private void reset_board() {
        String strboard = "rnbqkbnr\n" +
                          "pppppppp\n" +
                          "        \n" +
                          "        \n" +
                          "        \n" +
                          "        \n" +
                          "PPPPPPPP\n" +
                          "RNBQKBNR";

        board_from_string(strboard);
    }

    public int[] get_position_piece(String piece) {
        int[] abc = {-1, -1};
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null && board[i][j].to_string().equals(piece)) {
                    abc[0] = i; abc[1] = j;
                    return abc;
                }
            }
        }
        return abc;
    }




}
