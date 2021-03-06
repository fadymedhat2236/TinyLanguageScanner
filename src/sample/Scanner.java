package sample;


import javafx.scene.control.TextArea;

import java.util.ArrayList;

public class Scanner {
    private ArrayList<Token> tokens;

    private enum STATE_TYPES{
        START,INCOMMENT,INNUM,INID,INASSIGN
    }
    private static boolean inComment=false;
    public Scanner(){
        this.tokens=null;
    }
    public Scanner(ArrayList<Token> t1) {
        this.tokens = t1;
    }
    private static boolean isAReservedWord(String input){
        String reservedWords[]={"if","then","else","end","repeat","until","read","write"};
        for(int i=0;i<reservedWords.length;i++){
            if(reservedWords[i].equals(input))
                return true;
        }
        return false;
    }
    private static boolean isADigit(char c){
        if((int)c>=48 && (int)c<=57)
            return true;
        return false;
    }
    private static boolean isALetter(char c){
        if(((int)c>=65 && (int)c<=90) || ((int)c>=97 && (int)c<=122))
            return true;
        return false;
    }
    public void scan(String input) {
        STATE_TYPES state;
        if(inComment)
            state=STATE_TYPES.INCOMMENT;
        else
            state = STATE_TYPES.START;
        Token t=new Token();
        StringBuilder s=new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            switch (state) {
                case START:
                    if (input.charAt(i) == ' ' || input.charAt(i)=='\t' )
                        state = STATE_TYPES.START;
                    else if (input.charAt(i) == '{') {
                        state = STATE_TYPES.INCOMMENT;
                        if(i==input.length()-1)
                            inComment=true;
                    }
                    else if (isADigit(input.charAt(i))) {
                        t.setType(Constants.NUMBER);
                        s.append(input.charAt(i));
                        state = STATE_TYPES.INNUM;
                        if (i == input.length() - 1) {
                            t.setValue(s.toString());
                            Token temp=new Token(t.getType(),t.getValue());
                            tokens.add(temp);
                        }
                    } else if (isALetter(input.charAt(i))) {
                        s.append(input.charAt(i));
                        state = STATE_TYPES.INID;
                        if (i == input.length() - 1) {
                            t.setValue(s.toString());
                            if (isAReservedWord(s.toString()))
                                t.setType(Constants.RESERVED_WORD);
                            else
                                t.setType(Constants.IDENTIFIER);
                            Token temp=new Token(t.getType(),t.getValue());
                            tokens.add(temp);
                        }
                    }
                    else if (input.charAt(i) == ':') {
                        s.append(input.charAt(i));
                        state = STATE_TYPES.INASSIGN;
                    } else {
                        t.setType(Constants.SPECIAL_SYMBOL);
                        s.append(input.charAt(i));
                        t.setValue(s.toString());
                        s.delete(0, s.length());
                        if(input.charAt(i)=='+')
                            t.setType(t.getType() + Constants.PLUS);
                        else if(input.charAt(i)=='-')
                            t.setType(t.getType() + Constants.MINUS);
                        else if(input.charAt(i)=='*')
                            t.setType(t.getType() + Constants.MULTIPLY);
                        else if(input.charAt(i)=='/')
                            t.setType(t.getType() + Constants.DIVIDE);
                        else if(input.charAt(i)=='(')
                            t.setType(t.getType() + Constants.OPENING_BRACKET);
                        else if(input.charAt(i)==')')
                            t.setType(t.getType() + Constants.CLOSING_BRACKET);
                        else if (input.charAt(i)=='<')
                            t.setType(t.getType() + Constants.SMALLER_THAN);
                        Token temp=new Token(t.getType(),t.getValue());
                        tokens.add(temp);
                    }
                    break;
                case INCOMMENT:
                    if (input.charAt(i) != '}') {
                        state = STATE_TYPES.INCOMMENT;
                        if(i==input.length()-1)
                            inComment=true;
                    } else {
                        inComment=false;
                        state = STATE_TYPES.START;
                    }
                    break;
                case INNUM:
                    if (isADigit(input.charAt(i))) {
                        s.append(input.charAt(i));
                        state = STATE_TYPES.INNUM;
                        if (i == input.length() - 1) {
                            t.setValue(s.toString());
                            Token temp=new Token(t.getType(),t.getValue());
                            tokens.add(temp);
                        }
                    }
                    else {
                        t.setValue(s.toString());
                        i--;
                        s.delete(0, s.length());
                        state = STATE_TYPES.START;
                        Token temp=new Token(t.getType(),t.getValue());
                        tokens.add(temp);
                    }
                    break;
                case INID:
                    if (isALetter(input.charAt(i))) {
                        s.append(input.charAt(i));
                        state = STATE_TYPES.INID;
                        if (i == input.length() - 1) {
                            t.setValue(s.toString());
                            if (isAReservedWord(s.toString()))
                                t.setType(Constants.RESERVED_WORD);
                            else
                                t.setType(Constants.IDENTIFIER);
                            Token temp=new Token(t.getType(),t.getValue());
                            tokens.add(temp);
                        }
                    }
                    else {
                        t.setValue(s.toString());
                        i--;
                        if (isAReservedWord(s.toString()))
                            t.setType(Constants.RESERVED_WORD);
                        else
                            t.setType(Constants.IDENTIFIER);
                        s.delete(0, s.length());
                        state = STATE_TYPES.START;
                        Token temp=new Token(t.getType(),t.getValue());
                        tokens.add(temp);
                    }
                    break;
                case INASSIGN:
                    if (input.charAt(i) == '=') {
                        s.append(input.charAt(i));
                        t.setType(Constants.ASSIGN);
                        t.setValue(s.toString());
                        s.delete(0, s.length());
                        state = STATE_TYPES.START;
                        Token temp=new Token(t.getType(),t.getValue());
                        tokens.add(temp);

                    } else {
                        s.delete(0, s.length());
                        state = STATE_TYPES.START;
                        Token temp=new Token(t.getType(),t.getValue());
                        tokens.add(temp);
                        i--;
                    }
                    break;
            }
        }

    }
}