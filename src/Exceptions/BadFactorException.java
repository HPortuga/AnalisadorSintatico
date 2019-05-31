package Exceptions;

import Nomes.Names;

public class BadFactorException extends CompilerException {
   public BadFactorException(Names expected, Names shown, int linha) {
      super("Classe mal formada; Esperado: " + expected + ", apareceu: " + shown + " na linha " + linha);
   }
}
