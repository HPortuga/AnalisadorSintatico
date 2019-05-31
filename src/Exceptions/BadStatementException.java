package Exceptions;

import Nomes.Names;

public class BadStatementException extends CompilerException {
   public BadStatementException(Names expected, Names shown, int linha) {
      super("Statement mal formado; Esperado: " + expected + ", apareceu: " + shown + " na linha " + linha);
   }
}
