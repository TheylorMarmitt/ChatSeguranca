package br.edu.unoesc.gnupg;

public class TesteGnuPG {

	public static void main(String[] args){
        GnuPGContext ctx = new GnuPGContext();
        GnuPGKey[] keylist;

        if(args != null && args.length == 1){
            keylist = ctx.searchKeys(args[0]);
        }
        else{
            keylist = ctx.searchKeys("");
        }
        
        for(GnuPGKey key : keylist){
            System.out.printf("%s\n", key);
        }
    }
}
