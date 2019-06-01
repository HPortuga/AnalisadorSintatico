package Exceptions;

import Nomes.Names;

public class BadAtribStatException extends CompilerException {
   public BadAtribStatException(Names expected, Names shown, int linha) {
      super("Statement de atribuicao mal formado; Esperado: " + expected + ", apareceu: " + shown + " na linha " + linha);
   }
}
