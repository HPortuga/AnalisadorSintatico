package Exceptions;

import Nomes.Names;

public class BadAllocExpressionException extends CompilerException {
   public BadAllocExpressionException(Names expected, Names shown, int linha) {
      super("Expressao de alocacao mal formada; Esperado: " + expected + ", apareceu: " + shown + " na linha " + linha);
   }
}
