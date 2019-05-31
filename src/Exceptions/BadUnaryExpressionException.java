package Exceptions;

import Nomes.Names;

public class BadUnaryExpressionException extends CompilerException {
   public BadUnaryExpressionException(Names expected, Names shown, int linha) {
      super("Parametro mal formado; Esperado: " + (expected == null? "+ || -" : expected)
            + ", apareceu: " + shown + " na linha " + linha);
   }
}
